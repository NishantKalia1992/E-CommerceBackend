package com.order.feignClient.payment;

import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class PaymentClientWrapper {
	private final PaymentClient paymentClient;
	
	@Retry(name = "payment")
	@CircuitBreaker(name = "payment", fallbackMethod = "paymentFallback")
	public long paymentProcess(Payment payment) {
		return paymentClient.requestOrderPayment(payment);
	}
	public long paymentFallback(Payment payment, Throwable exception) {
		throw new RuntimeException("Payment service is down! due to triggering fallback");
	}
		

}
