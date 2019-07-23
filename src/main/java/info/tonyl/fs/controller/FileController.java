package info.tonyl.fs.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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
import info.tonyl.fs.exceptions.FileExistsException;
import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepo;
import info.tonyl.fs.responses.ErrorResponse;
import info.tonyl.fs.responses.ListResponse;
import info.tonyl.fs.responses.SimpleResponse;
import info.tonyl.fs.responses.UploadResponse;

@RestController
public class FileController {
	@Autowired
	private StoredFileDao sfDao;

	@Autowired
	private StoredFileRepo sfRepo;

	@GetMapping("truncate")
	public SimpleResponse truncate() {
		sfDao.deleteAll();
		return new SimpleResponse("It is done.");
	}

	@GetMapping("list")
	public ListResponse list() {
		return new ListResponse(sfRepo.findAll());
	}

	@PostMapping("upload")
	public UploadResponse uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "path", required = false) String path) throws IOException {
		if (path == null) {
			path = "";
		}

		StoredFile sf = sfDao.saveNew(file, path);
		return new UploadResponse(sf.getId());
	}

	@ExceptionHandler(FileExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse fileExists(FileExistsException e) {
		return new ErrorResponse("There is already a file with that name at that path", e);
	}

	@GetMapping("download/{id}")
	public ResponseEntity<Resource> download(@PathVariable String id) throws FileNotFoundException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new FileNotFoundException("No entry for ID " + id);
		}
		StoredFile sf = osf.get();
		Path actualPath = sfDao.getActualPath(sf);
		FileSystemResource file = new FileSystemResource(actualPath.toFile());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sf.getName() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(sf.getSize()).body(file);
	}

	@GetMapping("file-details/{id}")
	public StoredFile fileDetails(@PathVariable String id) throws FileNotFoundException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new FileNotFoundException("No entry for ID " + id);
		}
		return osf.get();
	}

	@ExceptionHandler(FileNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse badUuid(FileNotFoundException e) {
		return new ErrorResponse("No entry could be found for the given ID", e);
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
