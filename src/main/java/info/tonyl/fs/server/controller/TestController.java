package info.tonyl.fs.server.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import info.tonyl.fs.server.daos.MemoryRepository;
import info.tonyl.fs.server.models.Memory;
import info.tonyl.fs.server.requests.RememberRequest;
import info.tonyl.fs.server.responses.SimpleResponse;
import info.tonyl.fs.server.responses.UploadResponse;
import javassist.NotFoundException;

@RestController
public class TestController {
	@Autowired
	private MemoryRepository memoryRepo;

	@GetMapping("hello")
	public SimpleResponse hello() {
		return new SimpleResponse("Hello, client.");
	}

	@PostMapping("remember")
	public UploadResponse remember(@RequestBody RememberRequest req) {
		String message = req.getMessage();
		if (message == null) {
			message = "";
		}
		Memory m = new Memory();
		m.setMessage(message);
		m = memoryRepo.save(m);
		return new UploadResponse(m.getKey());
	}

	@GetMapping("recall/{id}")
	public SimpleResponse recall(@PathVariable String id) throws NotFoundException {
		UUID uuid = UUID.fromString(id);
		Memory m = memoryRepo.getByKey(uuid);
		if (m == null) {
			throw new NotFoundException("No entry for UUID " + id);
		}
		return new SimpleResponse(m.getMessage());
	}
}
