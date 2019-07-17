package info.tonyl.fs.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import info.tonyl.fs.server.daos.MemoryRepo;
import info.tonyl.fs.server.models.Memory;
import info.tonyl.fs.server.models.RememberRequest;
import info.tonyl.fs.server.models.Response;
import info.tonyl.fs.server.util.ResponseUtil;

@RestController
public class TestController {
	@Autowired
	private MemoryRepo memoryRepo;

	@GetMapping("hello")
	public ResponseEntity<Response> hello() {
		return ResponseUtil.build("Hello, client.");
	}

	@PostMapping("remember")
	public ResponseEntity<Response> remember(@RequestBody RememberRequest req) {
		String message = req.getMessage();
		if (message == null) {
			message = "";
		}

		Memory m = new Memory();
		m.setMessage(message);
		m = memoryRepo.save(m);

		StringBuilder sb = new StringBuilder();
		sb.append("Ok, I've remembered \"");
		sb.append(m.getMessage());
		sb.append("\". It's ID is ");
		sb.append(m.getId());
		sb.append(".");

		return ResponseUtil.build(sb.toString());
	}

	@GetMapping("recall/{id}")
	public ResponseEntity<Response> recall(@PathVariable Long id) {
		try {
			Memory m = memoryRepo.getById(id);
			return ResponseUtil.build(m.getMessage());
		} catch (Exception e) {
			return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "Error while retrieving memory", e);
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> error(HttpMessageNotReadableException ex) {
		return ResponseUtil.build(HttpStatus.BAD_REQUEST, "The request could not be read.", ex);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Response> error(Throwable t) {
		return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "An error was encountered.", t);
	}
}
