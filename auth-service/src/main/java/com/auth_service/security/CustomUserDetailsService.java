package com.auth_service.security;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.converters.Auto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//	@Autowired
	CustomerDto customer;
	
	@Autowired
	private RestTemplate restTemplate;

	@CircuitBreaker(name = "customerServiceBreaker", fallbackMethod = "customerServiceFallback")
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        String url = "http://CUSTOMER-SERVICE/api/auth/customer/username/" + username;
        
        // Let Jackson map it automatically now that the names match
        SuccessResponseWrapper wrapper = restTemplate.getForObject(url, SuccessResponseWrapper.class);

        // Extract using .getData() instead of .getResponse()
        if (wrapper == null || wrapper.getData() == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        CustomerDto customer = wrapper.getData();

        if (customer.getPassword() == null) {
            throw new UsernameNotFoundException("Password missing from JSON");
        }

        return User.withUsername(customer.getUsername())
                .password(customer.getPassword())
                .authorities("USER")
                .build();
    }
	
	public UserDetails customerServiceFallback(String username, Throwable throwable) {
		System.out.println("🚨 CIRCUIT BREAKER TRIPPED! Customer Service is down. 🚨");
        System.out.println("Error caught: " + throwable.getMessage());
        throw new RuntimeException("Login temporarily unavailable. Our servers are currently experiencing issues. Please try again in a few minutes.");
	}
}