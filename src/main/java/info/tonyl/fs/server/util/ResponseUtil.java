package info.tonyl.fs.server.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import info.tonyl.fs.server.models.ErrorDetails;
import info.tonyl.fs.server.models.Response;

public abstract class ResponseUtil {
	public static ResponseEntity<Response> build(String message) {
		Response r = new Response();
		r.setStatus(HttpStatus.OK.value());
		r.setMessage(message);
		return new ResponseEntity<>(r, HttpStatus.OK);
	}

	public static ResponseEntity<Response> build(HttpStatus status, String message, Throwable t) {
		Response r = new Response();
		r.setStatus(status.value());
		r.setMessage(message);
		r.setError(new ErrorDetails(t));
		return new ResponseEntity<>(r, status);
	}
}
