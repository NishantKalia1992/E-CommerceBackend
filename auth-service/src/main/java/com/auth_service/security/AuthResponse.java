package com.auth_service.security;

import java.time.LocalDateTime;

public class AuthResponse {
	private String username;
	private String token;
	private String timestamp;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public AuthResponse(String username, String token, String timestamp) {
		
		this.username = username;
		this.token = token;
		this.timestamp = LocalDateTime.now().toString();
	}
	
}
