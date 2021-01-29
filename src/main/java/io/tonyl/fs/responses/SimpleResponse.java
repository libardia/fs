package io.tonyl.fs.responses;

import lombok.Data;

@Data
public class SimpleResponse {
	public SimpleResponse(String message) {
		this.message = message;
	}

	private String message;
}
