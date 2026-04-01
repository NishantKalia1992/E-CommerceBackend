package com.payment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	public ResponseEntity<String> handle(EntityNotFoundException exp){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
	}
	
	public ResponseEntity<String> methodArgumentException(MethodArgumentNotValidException exp){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
	}
	
	
	public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException exp){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
	}

}
