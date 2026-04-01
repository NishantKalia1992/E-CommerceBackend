package com.payment.requestDto;

import java.math.BigDecimal;

import com.payment.entities.Customer;
import com.payment.entities.PaymentMethod;

public record PaymentRequest(
		long id,
		BigDecimal amount,
		PaymentMethod paymentMethod,
		long orderId,
		String orderRefrence,
		Customer customer) {
	
}
