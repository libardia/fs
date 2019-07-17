package info.tonyl.fs.server.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.server.daos.MemoryRepository;
import info.tonyl.fs.server.daos.StoredFileRepository;
import info.tonyl.fs.server.models.Memory;
import info.tonyl.fs.server.models.RememberRequest;
import info.tonyl.fs.server.models.Response;
import info.tonyl.fs.server.models.StoredFile;
import info.tonyl.fs.server.util.ResponseUtil;

@RestController
public class TestController {
	@Autowired
	private MemoryRepository memoryRepo;

	@Autowired
	private StoredFileRepository sfRepo;

	@GetMapping("hello")
	public ResponseEntity<Response> hello() {
		return ResponseUtil.build("Hello, client.");
	}

	@PostMapping("/upload")
	public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			StoredFile sf = new StoredFile();
			sf.setName(file.getOriginalFilename());
			sf.setType(file.getContentType());
			sf.setSize(file.getSize());
			sf.setData(file.getBytes());
			sf = sfRepo.save(sf);
			return ResponseUtil.build("Successfully stored file with id " + sf.getId().toString());
		} catch (IOException e) {
			return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file", e);
		}
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
		sb.append("\". It's key is ");
		sb.append(m.getKey());
		sb.append(".");

		return ResponseUtil.build(sb.toString());
	}

	@GetMapping("recall/{id}")
	public ResponseEntity<Response> recall(@PathVariable UUID id) {
		try {
			Memory m = memoryRepo.getByKey(id);
			return ResponseUtil.build(m.getMessage());
		} catch (Exception e) {
			return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "Error while retrieving memory", e);
		}
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<Response> multipartException(MultipartException e) {
		return ResponseUtil.build(HttpStatus.BAD_REQUEST, "The request was not a multipart file", e);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> notReadable(HttpMessageNotReadableException e) {
		return ResponseUtil.build(HttpStatus.BAD_REQUEST, "The request could not be read", e);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Response> error(Throwable t) {
		return ResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "An error was encountered", t);
	}
}
