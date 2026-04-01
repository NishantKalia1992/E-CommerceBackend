package com.payment.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.requestDto.PaymentRequest;
import com.payment.services.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService service;
	
	@PostMapping("/create")
	public ResponseEntity<Long> createPayment(@RequestBody @Valid PaymentRequest request){
		return ResponseEntity.ok(this.service.createPayment(request));
	}
}
