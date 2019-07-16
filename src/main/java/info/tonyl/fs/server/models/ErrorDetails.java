package info.tonyl.fs.server.models;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.Data;

@Data
public class ErrorDetails {
	private String message;
	private String type;
	private String trace;

	public ErrorDetails(Throwable t) {
		message = t.getLocalizedMessage();
		type = t.getClass().getName();

		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));

		trace = sw.toString();
	}
}
