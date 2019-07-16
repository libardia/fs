package info.tonyl.fs.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import info.tonyl.fs.server.models.Response;
import info.tonyl.fs.server.util.ResponseUtil;

@RestController
public class TestController {
	@GetMapping("hello")
	public ResponseEntity<Response> hello() {
		return ResponseUtil.build("Hello, client.");
	}

	@GetMapping("error")
	public ResponseEntity<Response> error() {
		return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "This is an example error.",
				new Exception("Example exception message."));
	}
}
