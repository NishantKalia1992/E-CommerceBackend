package com.customer.apiResponse;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;
@Data
public class SuccessResponse<T> {
	private LocalDateTime timestamp;
	private int status;
	private String message;
	private T data;
	public SuccessResponse(HttpStatus status, String message, T data) {
		this.timestamp=LocalDateTime.now();
		this.status = status.value();
		this.message = message;
		this.data = data;
	}
	
}
