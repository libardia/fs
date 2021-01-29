package io.tonyl.fs.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.tonyl.fs.daos.StoredFileDao;
import io.tonyl.fs.exceptions.FileExistsException;
import io.tonyl.fs.models.StoredFile;
import io.tonyl.fs.responses.ErrorResponse;
import io.tonyl.fs.responses.ListResponse;
import io.tonyl.fs.responses.SimpleResponse;
import io.tonyl.fs.util.Util;

@RestController
public class FileController {
	@Autowired
	private StoredFileDao sfDao;

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@GetMapping("list")
	public ListResponse list(HttpServletRequest request) {
		Util.logAction(log, request);
		return new ListResponse(sfDao.getAll());
	}

	@PostMapping("upload")
	public StoredFile uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "path", required = false) String path, HttpServletRequest request)
			throws IOException {
		Util.logAction(log, request);
		if (path == null) {
			path = "";
		}
		return sfDao.saveNew(file, path);
	}

	@ExceptionHandler(FileExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse fileExists(FileExistsException e) {
		return new ErrorResponse("There is already a file with that name at that path", e);
	}

	@GetMapping("download/{id}")
	public ResponseEntity<Resource> download(@PathVariable String id, HttpServletRequest request)
			throws FileNotFoundException {
		Util.logAction(log, request);
		StoredFile sf = sfDao.get(id);
		if (sf == null) {
			throw new FileNotFoundException("No entry for ID " + id);
		}
		Path actualPath = sfDao.getActualPath(sf);
		FileSystemResource file = new FileSystemResource(actualPath.toFile());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sf.getName() + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(sf.getSize()).body(file);
	}

	@GetMapping("file-details/{id}")
	public StoredFile fileDetails(@PathVariable String id, HttpServletRequest request) throws FileNotFoundException {
		Util.logAction(log, request);
		StoredFile sf = sfDao.get(id);
		if (sf == null) {
			throw new FileNotFoundException("No entry for ID " + id);
		}
		return sf;
	}

	@GetMapping("deleteAll")
	public SimpleResponse deleteAll(HttpServletRequest request) {
		Util.logAction(log, request);
		sfDao.deleteAll();
		return new SimpleResponse("It is done.");
	}

	@GetMapping("delete/{id}")
	public SimpleResponse delete(@PathVariable String id, HttpServletRequest request) {
		Util.logAction(log, request);
		String message;
		if (sfDao.delete(id)) {
			message = "Successfully deleted file with ID " + id;
		} else {
			message = "There was no file with ID " + id;
		}
		return new SimpleResponse(message);
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
