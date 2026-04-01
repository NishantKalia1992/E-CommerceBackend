package com.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Mono;

@Configuration
public class ReteLimitConfig {
	
	@Bean
	public KeyResolver userIpKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
	}
}
