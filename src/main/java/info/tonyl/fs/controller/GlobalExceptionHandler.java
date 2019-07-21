package info.tonyl.fs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import info.tonyl.fs.responses.ErrorResponse;
import javassist.NotFoundException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse notReadable(HttpMessageNotReadableException e) {
		return new ErrorResponse("The request could not be read", e);
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse error(Throwable t) {
		return new ErrorResponse("An error was encountered", t);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse badUuid(IllegalArgumentException e) {
		return new ErrorResponse("The supplied string is not a valid UUID", e);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse badUuid(NotFoundException e) {
		return new ErrorResponse("No entry could be found for the given UUID", e);
	}
}
