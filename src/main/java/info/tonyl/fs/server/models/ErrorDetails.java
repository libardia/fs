package info.tonyl.fs.server.models;

import lombok.Data;

@Data
public class ErrorDetails {
	private String message;
	private String type;

	public ErrorDetails(Throwable t) {
		message = t.getLocalizedMessage();
		type = t.getClass().getName();
	}
}
