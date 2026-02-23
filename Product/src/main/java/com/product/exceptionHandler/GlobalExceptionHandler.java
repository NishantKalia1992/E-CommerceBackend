package com.product.exceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.product.apiResponse.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(DuplicateResourceExecption.class)
	public ResponseEntity<ErrorResponse> handleDUplicateResouceException(DuplicateResourceExecption exception,
			WebRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				"Resorce Not found", exception.getMessage(), request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"Resource Not Found", exception.getMessage(), request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				"BAD REQUEST", exception.getMessage(), request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
