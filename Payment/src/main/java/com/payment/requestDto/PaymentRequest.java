package com.payment.requestDto;

import java.math.BigDecimal;

import com.payment.entities.Customer;
import com.payment.entities.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
	private long id;
	private BigDecimal amount;
	private PaymentMethod paymentMethod;
	private long orderId;
	private String orderRefrence;
	private Customer customer;
}
