package com.payment.dto;

import org.springframework.stereotype.Service;

import com.payment.entities.Payment;
import com.payment.requestDto.PaymentRequest;

@Service
public class PaymentMapper {
	
	public Payment toPayment(PaymentRequest request) {
		if(request==null) {
			return null;
		}
		return Payment.builder().
				id(request.id()).
				orderId(request.orderId()).
				amount(request.amount()).
				paymentMethod(request.paymentMethod()).
				build();
				
	}
}
