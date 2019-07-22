package info.tonyl.fs.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
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
import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepo;
import info.tonyl.fs.responses.ErrorResponse;
import info.tonyl.fs.responses.ListResponse;
import info.tonyl.fs.responses.SimpleResponse;
import info.tonyl.fs.responses.UploadResponse;
import javassist.NotFoundException;

@RestController
public class FileController {
	@Autowired
	private StoredFileDao sfDao;

	@Autowired
	private StoredFileRepo sfRepo;

	@GetMapping("truncate")
	public SimpleResponse truncate() {
		sfRepo.deleteAll();
		return new SimpleResponse("It is done.");
	}

	@GetMapping("list")
	public ListResponse list() {
		return new ListResponse(sfRepo.findAll());
	}

	@PostMapping("upload")
	public UploadResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path)
			throws IOException {
		StoredFile sf = sfDao.saveNew(file, path);
		return new UploadResponse(sf.getId());
	}

	@GetMapping("download/{id}")
	public ResponseEntity<Resource> download(@PathVariable String id)
			throws NotFoundException, SQLException, IOException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new NotFoundException("No entry for ID " + id);
		}
		StoredFile sf = osf.get();
		Path actualPath = sfDao.getActualPath(sf);
		FileSystemResource file = new FileSystemResource(actualPath.toFile());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sf.getName() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(sf.getSize()).body(file);
	}

	@GetMapping("file-details/{id}")
	public StoredFile fileDetails(@PathVariable String id) throws NotFoundException {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (!osf.isPresent()) {
			throw new NotFoundException("No entry for ID " + id);
		}
		return osf.get();
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
