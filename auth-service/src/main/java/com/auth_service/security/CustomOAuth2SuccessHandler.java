package com.auth_service.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		// 1. Extract the user's profile data from Google's response
		OAuth2User oAuth2User= (OAuth2User) authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		
//		verify from customer-service
		String url = "http://CUSTOMER-SERVICE/api/auth/customer/username/" + email;
		boolean userExist = true;
		
		try {
			 SuccessResponseWrapper wrapper = restTemplate.getForObject(url, SuccessResponseWrapper.class);

		        // Extract using .getData() instead of .getResponse()
		        if (wrapper == null || wrapper.getData() == null) {
		            throw new UsernameNotFoundException("User not found: " + email);
		        }
		}catch (Exception e) {
            // If Customer-Service throws a 404 Not Found, it means the user is new!
            userExist = false; 
        }
		
//		3. if new then customer-service to save
		if(!userExist) {
			String postUrl = "http://CUSTOMER-SERVICE/api/auth/customer/google-register";
			// Building the payload exactly like your Postman JSON body
			Map<String, String> newCustomer = new HashMap<>();
			newCustomer.put("fullName", name);
			newCustomer.put("username", email);
// We intentionally leave out password and contactNo for Google logins
            
            // Make the POST request to save the user
			restTemplate.postForObject(postUrl, newCustomer, Object.class);
			System.out.println("Google user register via customer-service : "+email);			
		}
		else {
			System.out.println("existing Google user logged-In :"+email);
		}
		
		// 4. Generate your system's JWT token
        String token = jwtService.generateToken(email);
        
     // 5. Redirect back to your Frontend with the token in the URL
        String targetUrl = "http://localhost:3000/oauth2/redirect?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

}
