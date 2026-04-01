package com.payment.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.payment.dto.NotificationProducer;
import com.payment.dto.PaymentMapper;
import com.payment.dto.PaymentNotificationRequest;
import com.payment.entities.Payment;
import com.payment.repositories.PaymentRepository;
import com.payment.requestDto.PaymentRequest;
import com.payment.responseDto.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PaymentService {
	
	@Value("${stripe.api.key}")
	private String stripeApiKey;
	
	private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final NotificationProducer producer;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    
    
	
	public long createPayment(PaymentRequest request) {
		Optional<Payment> existingPayment = paymentRepository.findById(request.orderId());
		if(existingPayment.isPresent()) {
			LOGGER.info("payment idempotency hit ! for order was already processed", request.orderId());
			return existingPayment.get().getId();
		}		
		Stripe.apiKey=stripeApiKey;
		try {
			LOGGER.info("Attempting to charge card via Stripe for Order {}...",request.orderId());
			long amountInDollar = request.amount().longValue()*100;
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
			.setAmount(amountInDollar)
			.setCurrency("usd")
			.setPaymentMethod("pm_card_visa")
			.setConfirm(true)
			.setAutomaticPaymentMethods(PaymentIntentCreateParams.
					AutomaticPaymentMethods.
					builder().
					setEnabled(true).
					setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER).build()).build();
			PaymentIntent paymentIntent = PaymentIntent.create(params);
			LOGGER.info("Stripe payment successful ! transaction Id - ",paymentIntent.getId());
			
			Payment payment = this.paymentRepository.save(mapper.toPayment(request));
			this.producer.sendNotification(new PaymentNotificationRequest(request.orderRefrence(),
					request.amount(), 
					request.paymentMethod(), 
					request.customer().firstName(), 
					request.customer().lastName(), 
					request.customer().email()));
			return payment.getId();
			
		}catch (Exception e) {
			LOGGER.error("Stripe payment failed for Order {}: {}", request.orderId(), e.getMessage());
	        // Throwing this exception is what tells the Order Service to put the inventory back!
	        throw new RuntimeException("Payment Gateway Error: " + e.getMessage());
		}
//		Payment payment = this.paymentRepository.save(mapper.toPayment(request));
//		this.producer.sendNotification(new PaymentNotificationRequest(request.orderRefrence(),
//				request.amount(), 
//				request.paymentMethod(), 
//				request.customer().firstName(), 
//				request.customer().lastName(), 
//				request.customer().email()));
//		return payment.getId();
		
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
