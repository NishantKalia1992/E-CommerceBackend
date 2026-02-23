package com.payment.responseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.payment.entities.Customer;
import com.payment.entities.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentResponse {
	private long id;
	private BigDecimal amount;
	private PaymentMethod paymentMethod;
	private long orderId;
	private String orderRefrence;
	private Customer customer;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}
