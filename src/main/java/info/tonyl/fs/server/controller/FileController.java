package info.tonyl.fs.server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.server.daos.StoredFileRepository;
import info.tonyl.fs.server.models.StoredFile;
import info.tonyl.fs.server.responses.ErrorResponse;
import info.tonyl.fs.server.responses.UploadResponse;

@RestController
public class FileController {
	@Autowired
	private StoredFileRepository sfRepo;

	@PostMapping("/upload")
	public UploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		StoredFile sf = new StoredFile();
		sf.setName(file.getOriginalFilename());
		sf.setType(file.getContentType());
		sf.setSize(file.getSize());
		sf.setData(file.getBytes());
		sf = sfRepo.save(sf);
		return new UploadResponse(sf.getId());
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse ioException(IOException e) {
		return new ErrorResponse("The file could not be uploaded", e);
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse multipartException(MultipartException e) {
		return new ErrorResponse("A multipart-file must be included in the request", e);
	}
}
