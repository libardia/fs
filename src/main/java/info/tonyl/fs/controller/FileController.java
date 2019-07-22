package info.tonyl.fs.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.daos.StoredFileDao;
import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepository;
import info.tonyl.fs.responses.ErrorResponse;
import info.tonyl.fs.responses.FileDetailsResponse;
import info.tonyl.fs.responses.UploadResponse;
import javassist.NotFoundException;

@RestController
public class FileController {
	@Autowired
	private StoredFileDao sfDao;

	@Autowired
	private StoredFileRepository sfRepo;

	@PostMapping("upload")
	public UploadResponse uploadFile(@RequestParam("file") MultipartFile file)
			throws NoSuchAlgorithmException, IOException {
		StoredFile sf = sfDao.saveNew(file);
		return new UploadResponse(sf.getId());
	}

	@GetMapping("download/{id}")
	public ResponseEntity<byte[]> download(@PathVariable String id) throws NotFoundException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new NotFoundException("No entry for ID " + id);
		}
		StoredFile sf = osf.get();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sf.getName() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(sf.getSize()).body(sf.getData());
	}

	@GetMapping("file-details/{id}")
	public FileDetailsResponse fileDetails(@PathVariable String id) throws NotFoundException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new NotFoundException("No entry for ID " + id);
		}
		return new FileDetailsResponse(osf.get());
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
