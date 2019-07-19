package info.tonyl.fs.server.responses;

import lombok.Data;

@Data
public class ErrorResponse {
	public ErrorResponse(String message, Throwable t) {
		this.message = message;
		exceptionMessage = t.getLocalizedMessage();
		type = t.getClass().getName();
	}

	private String message;
	private String exceptionMessage;
	private String type;
}
