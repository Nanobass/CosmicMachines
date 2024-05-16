package net.paxyinc.region;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegionOutputStream extends ByteArrayOutputStream {

	private final RegionFile file;
	private final int index;
	private final long timestamp;
	
	public RegionOutputStream(RegionFile file, int index, long timestamp) {
		this.file = file;
		this.index = index;
		this.timestamp = timestamp;
	}

	@Override
	public void close() throws IOException {
		file.writeChunkBytes(index, timestamp, toByteArray());
	}
	
	public RegionFile getFile() {
		return file;
	}
	
	public int getIndex() {
		return index;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
}
