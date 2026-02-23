package com.customer.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.customer.apiResponse.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDUplicateResouceException(DuplicateResourceException exception, WebRequest request){
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				"Resorce Not found",
				exception.getMessage(),
				request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Resource Not Found",
				exception.getMessage(),
				request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest request){
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				exception.getMessage(),
				request.getDescription(false).replace("uri = ", ""));
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
