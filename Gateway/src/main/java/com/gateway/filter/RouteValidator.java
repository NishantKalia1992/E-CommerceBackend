package com.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.*;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
	public static final List<String> openEndPoints = List.of
			("/api/auth/customer/add",
					"api/auth/login","/eureka");
	
	public Predicate<ServerHttpRequest> isSecured =
            request -> openEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
