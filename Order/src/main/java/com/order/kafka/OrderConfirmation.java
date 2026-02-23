package com.order.kafka;

import java.math.BigDecimal;
import com.order.feignClients.customer.Customer;
import com.order.dto.PaymentMethod;

public record OrderConfirmation(
		String orderReference,
		BigDecimal totalAmount,
		PaymentMethod paymentMethod,
		Customer customer
		) {
	
}
