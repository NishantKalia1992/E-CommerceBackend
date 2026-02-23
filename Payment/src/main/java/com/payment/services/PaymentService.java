package com.payment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.entities.Payment;
import com.payment.repositories.PaymentRepository;
import com.payment.requestDto.PaymentRequest;
import com.payment.responseDto.PaymentResponse;

@Service
public class PaymentService {
	@Autowired
	PaymentRepository paymentRepository;
	
	PaymentResponse createPayment(PaymentRequest request) {
		
		return null;
	}
	
	PaymentResponse maptoPaymentResponse(Payment payment) {
		PaymentResponse response = new PaymentResponse();
		response.setId(payment.getId());
		response.setAmount(payment.getAmount());
		response.setCreatedDate(payment.getCreatedDate());
		response.setLastModifiedDate(payment.getLastModifiedDate());
		response.setOrderId(payment.getOrderId());
		return response;
	}
	
}
