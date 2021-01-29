package io.tonyl.fs.responses;

import lombok.Data;

@Data
public class ErrorResponse {
	public ErrorResponse(String message, Throwable t) {
		this.message = message;
		exceptionMessage = t.getLocalizedMessage();
		type = t.getClass().getName();
		t.printStackTrace();
	}

	private String message;
	private String exceptionMessage;
	private String type;
}
