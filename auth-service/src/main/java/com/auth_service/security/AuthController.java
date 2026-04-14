package com.auth_service.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	
	public ResponseEntity<AuthResponse> login (@RequestBody CustomerDto customerDto){
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerDto.getUsername(), customerDto.getPassword()));
		
		if(authenticate.isAuthenticated()) {
//			1.generate token
			String token = jwtService.generateToken(customerDto.getUsername());
			
//			2.create and clean dto
			AuthResponse response = new AuthResponse(customerDto.getUsername(), token, LocalDateTime.now().toString());
			
			return ResponseEntity.ok(response);
		}else{
			throw new RuntimeException ("invalid access !");
		}
	}
	

}
