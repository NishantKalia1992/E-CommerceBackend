package com.order.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceExecption extends RuntimeException {

	public DuplicateResourceExecption(String message) {
		super(message);
	}
	
}
