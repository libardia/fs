package io.tonyl.fs.exceptions;

public class FileExistsException extends RuntimeException {
	private static final long serialVersionUID = 6659239013947744401L;

	public FileExistsException(String message) {
		super(message);
	}
}
