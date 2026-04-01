package com.order.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewaySecurityFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String gatewaySecret = req.getHeader("X-Gateway-Secret");
		if(gatewaySecret==null||!gatewaySecret.equals("MySuperSecretPassword123!")) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.getWriter().write("ACCESS DENIED: You must go through the API Gateway!");
			return;
		}
		chain.doFilter(request, response);
	}

}
