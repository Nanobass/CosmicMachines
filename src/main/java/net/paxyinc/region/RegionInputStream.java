package net.paxyinc.region;

import java.io.ByteArrayInputStream;

public class RegionInputStream extends ByteArrayInputStream {

	private final RegionFile file;
	private final int index;
	private final long timestamp;

	public RegionInputStream(RegionFile file, int index, long timestamp, byte[] data) {
		super(data);
		this.file = file;
		this.index = index;
		this.timestamp = timestamp;
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
