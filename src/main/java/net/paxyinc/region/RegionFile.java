package net.paxyinc.region;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RegionFile {

	public static final int MAGIC = 0xFFFC6502;
	public static final int FILE_VERSION = 0;

	private final File file;
	private String rafMode;
	private RandomAccessFile raf;

	private int headerSize = 0;
	private final long[] offsets = new long[4096];
	private final int[] sizes = new int[4096];
	private final int[] timestamps = new int[4096];
	
	private long creationTime;
	private int regionX, regionY, regionZ;

	public RegionFile(File file, int regionX, int regionY, int regionZ) throws IOException, CorruptedFileException {
		this(file, System.currentTimeMillis(), regionX, regionY, regionZ);
	}
	
	public RegionFile(File file, long creationTime, int regionX, int regionY, int regionZ) throws IOException, CorruptedFileException {
		this.file = file;
		ensureFileOpen("rw");
		try {
			tryReadHeader();
		} catch (IOException parseHeaderFailed) {
			try {
				tryCreateHeader(creationTime, regionX, regionY, regionZ);
			} catch (Exception createHeaderFailed) {
				throw new CorruptedFileException(file);
			}
		}
	}

	private void ensureFileOpen(String mode) throws IOException {
		if(raf != null && !mode.equals(rafMode)) raf.close();
		raf = new RandomAccessFile(file, mode);
		rafMode = mode;
	}

	private void closeFile() throws IOException {
		if(raf != null) raf.close();
		raf = null;
		rafMode = null;
	}

	public static int getChunkIndex(int chunkX, int chunkY, int chunkZ) {
		return chunkY * 256 + chunkX * 16 + chunkZ;
	}
	
	private void tryCreateHeader(long creationTime, int regionX, int regionY, int regionZ) throws IOException {
		this.creationTime = creationTime;
		this.regionX = regionX;
		this.regionY = regionY;
		this.regionZ = regionZ;
		for (int i = 0; i < 4096; i++) {
			offsets[i] = sizes[i] = timestamps[i] = 0;
		}
		tryWriteHeader();		
	}

	private void tryWriteHeader() throws IOException {
		raf.seek(0);
		raf.writeInt(MAGIC);
		raf.writeInt(FILE_VERSION);
		raf.writeLong(creationTime);
		raf.writeInt(regionX);
		raf.writeInt(regionY);
		raf.writeInt(regionZ);
		for (int i = 0; i < 4096; i++) {
			raf.writeLong(offsets[i]);
			raf.writeInt(sizes[i]);
		}
		for (int i = 0; i < 4096; i++) {
			raf.writeInt(timestamps[i]);
		}
		headerSize = (int) raf.getFilePointer();
	}

	private void tryReadHeader() throws IOException {
		raf.seek(0);
		int magic = raf.readInt();
		if (magic != MAGIC) throw new RuntimeException();
		int fileVersion = raf.readInt();
		if (fileVersion != FILE_VERSION) throw new RuntimeException();
		creationTime = raf.readLong();
		regionX = raf.readInt();
		regionY = raf.readInt();
		regionZ = raf.readInt();
		for (int i = 0; i < 4096; i++) {
			offsets[i] = raf.readLong();
			sizes[i] = raf.readInt();
		}
		for (int i = 0; i < 4096; i++) {
			timestamps[i] = raf.readInt();
		}
		headerSize = (int) raf.getFilePointer();
	}

	public boolean doesChunkExist(int index) {
		return offsets[index] != 0 && sizes[index] != 0;
	}

	byte[] readChunkBytes(int index) throws IOException {
		ensureFileOpen("r");
		if (!doesChunkExist(index)) throw new IOException("chunk not present");
		long offset = offsets[index];
		int size = sizes[index];
		byte[] data = new byte[size];
		raf.seek(offset);
		raf.read(data);
		return data;
	}

	void writeChunkBytes(int index, long timestamp, byte[] data) throws IOException {
		ensureFileOpen("r");
		File copyFile = new File(file.getAbsolutePath() + ".sav");
		try (FileOutputStream copy = new FileOutputStream(copyFile)) {
			// copy header
			byte[] header = new byte[headerSize];
			raf.seek(0);
			raf.readFully(header);
			copy.write(header);

			byte[] chunkBuffer = new byte[1024 * 4096];

			// copy chunk until index - 1
			for (int i = 0; i < index; i++) {
				long copyChunkOffset = offsets[i];
				int copyChunkSize = sizes[i];
				if (doesChunkExist(i)) continue;
				raf.seek(copyChunkOffset);
				raf.readFully(chunkBuffer, 0, copyChunkSize);
				copy.write(chunkBuffer, 0, copyChunkSize);
			}

			// write index
			offsets[index] = copy.getChannel().position();
			sizes[index] = data.length;
			timestamps[index] = (int) (timestamp - creationTime); 
			copy.write(data, 0, data.length);

			// write from index + 1 and offset all pointers
			long offsetOffset = copy.getChannel().position() - raf.getFilePointer();
			for (int i = index + 1; i < 4096; i++) {
				long copyChunkOffset = offsets[i];
				int copyChunkSize = sizes[i];
				if (doesChunkExist(i)) continue;
				raf.readFully(chunkBuffer, 0, copyChunkSize);
				copy.write(chunkBuffer, 0, copyChunkSize);
				offsets[i] = copyChunkOffset + offsetOffset;
			}

		}

        closeFile();
		Files.move(copyFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		ensureFileOpen("rw");
		tryWriteHeader();
	}

	public void writeChunks(RegionFileWriteHandler handler) throws IOException {
		ensureFileOpen("r");
		raf.seek(0);
		File copyFile = new File(file.getAbsolutePath() + ".sav");
		try (FileOutputStream copy = new FileOutputStream(copyFile)) {

			// copy header
			byte[] header = new byte[headerSize];
			raf.seek(0);
			raf.readFully(header);
			copy.write(header);

			byte[] chunkBuffer = new byte[1024 * 1024 * 8]; // 8 MiB

			for (int i = 0; i < 4096; i++) {
				long offset = copy.getChannel().position();
				ByteArrayOutputStream os = new ByteArrayOutputStream(); // TODO create reusable stream
				if (handler.write(i, os)) {
					timestamps[i] = (int) (System.currentTimeMillis() - creationTime);
					byte[] data = os.toByteArray();
					copy.write(data, 0, data.length);
					offsets[i] = offset;
					sizes[i] = data.length;
				} else if (doesChunkExist(i)) {
					long copyChunkOffset = offsets[i];
					int copyChunkSize = sizes[i];
					raf.seek(copyChunkOffset);
					raf.readFully(chunkBuffer, 0, copyChunkSize);
					copy.write(chunkBuffer, 0, copyChunkSize);
					offsets[i] = offset;
				}
			}
		}

		closeFile();
		Files.move(copyFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		ensureFileOpen("rw");
		tryWriteHeader();
	}

	public InputStream readChunk(int index) throws IOException {
		byte[] data = readChunkBytes(index);
		return new RegionInputStream(this, index, timestamps[index] + creationTime, data);
	}

	public OutputStream writeChunk(int index) throws IOException {
		return writeChunk(index, System.currentTimeMillis());
	}

	public OutputStream writeChunk(int index, long timestamp) throws IOException {
		return new RegionOutputStream(this, index, timestamp);
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public int getRegionX() {
		return regionX;
	}
	
	public int getRegionY() {
		return regionY;
	}
	
	public int getRegionZ() {
		return regionZ;
	}

	public void close() throws IOException {
		raf.close();
	}

}
