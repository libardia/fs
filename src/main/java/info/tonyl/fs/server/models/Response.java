package info.tonyl.fs.server.models;

import lombok.Data;

@Data
public class Response {
	private Integer status;
	private String message;
	private ErrorDetails error;
}
