package net.paxyinc.region;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.Tag;

import java.io.*;

public class RegionChunkFile {

    public static final int MAGIC = 0xFFFE6502;
    public static final int FILE_VERSION = 0;

    private final RegionFile file;
    private long creationTime;
    private int chunkX, chunkY, chunkZ;

    private int headerSize;

    public RegionChunkFile(RegionFile file, int chunkX, int chunkY, int chunkZ) {
        this(file, System.currentTimeMillis(), chunkX, chunkY, chunkZ);
    }

    public RegionChunkFile(RegionFile file, long creationTime, int chunkX, int chunkY, int chunkZ) {
        this.file = file;
        this.creationTime = creationTime;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
    }

    private void tryCreateHeader(OutputStream os, long creationTime, int chunkX, int chunkY, int chunkZ) throws IOException {
        this.creationTime = creationTime;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        tryWriteHeader(os);
    }

    private void tryWriteHeader(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(MAGIC);
        dos.writeInt(FILE_VERSION);
        dos.writeLong(creationTime);
        dos.writeInt(chunkX);
        dos.writeInt(chunkY);
        dos.writeInt(chunkZ);
    }

    private void tryReadHeader(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int magic = dis.readInt();
        if (magic != MAGIC) throw new RuntimeException();
        int fileVersion = dis.readInt();
        if (fileVersion != FILE_VERSION) throw new RuntimeException();
        creationTime = dis.readLong();
        chunkX = dis.readInt();
        chunkY = dis.readInt();
        chunkZ = dis.readInt();
    }

    public void write(Tag<?> nbt, OutputStream os) throws IOException {
        int index = RegionFile.getChunkIndex(chunkX, chunkY, chunkZ);
        boolean exists = file.doesChunkExist(index);
        if(exists) tryWriteHeader(os);
        else tryCreateHeader(os, creationTime, chunkX, chunkY, chunkZ);
        NBTSerializer serializer = new NBTSerializer(false);
        serializer.toStream(new NamedTag(null, nbt), os);
    }

    public void write(Tag<?> nbt) throws IOException {
        int index = RegionFile.getChunkIndex(chunkX, chunkY, chunkZ);
        OutputStream os = file.writeChunk(index);
        write(nbt, os);
        os.close();
    }

    public Tag<?> read() throws IOException {
        int index = RegionFile.getChunkIndex(chunkX, chunkY, chunkZ);
        InputStream is = file.readChunk(index);
        tryReadHeader(is);
        NBTDeserializer serializer = new NBTDeserializer(false);
        NamedTag tag = serializer.fromStream(is);
        is.close();
        return tag.getTag();
    }

    public boolean exists() {
        int index = RegionFile.getChunkIndex(chunkX, chunkY, chunkZ);
        return file.doesChunkExist(index);
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public RegionFile getFile() {
        return file;
    }
}
