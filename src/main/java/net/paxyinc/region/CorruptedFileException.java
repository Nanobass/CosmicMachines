package net.paxyinc.region;

import java.io.File;

public class CorruptedFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final File file;

	public CorruptedFileException(File file) {
		super("The File \"%s\" is corrupted".formatted(file.getAbsolutePath()));
		this.file = file;
	}

	public File getFile() {
		return file;
	}

}
