package com.order.feignClient.payment;

import java.math.BigDecimal;

import com.order.dto.PaymentMethod;
import com.order.feignClients.customer.Customer;

public record Payment(
		BigDecimal amount,
	    PaymentMethod paymentMethod,
	    long orderId,
	    String orderReference,
	    Customer customer) {

}
