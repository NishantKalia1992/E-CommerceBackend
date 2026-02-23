package com.order.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.apiResponse.SuccessResponse;
import com.order.kafka.OrderConfirmation;
import com.order.kafka.OrderProducer;
import com.order.requestDto.OrderRequest;
import com.order.responseDto.OrderResponseDto;
import com.order.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	
	final OrderService orderService;
	final OrderProducer orderProducer;
	
	@PostMapping("/create")
	public ResponseEntity<SuccessResponse<OrderResponseDto>> createOrder(@Valid @RequestBody OrderRequest request){
		OrderResponseDto order = orderService.createOrder(request);
		SuccessResponse<OrderResponseDto> successResponse = new SuccessResponse<>(HttpStatus.CREATED, "Order created successfully ", order);
		return new ResponseEntity<SuccessResponse<OrderResponseDto>>(successResponse, HttpStatus.CREATED);
	}
	
	@GetMapping("/orderId/{id}")
	public ResponseEntity<SuccessResponse<OrderResponseDto>> getOrders(@PathVariable Long id){
		OrderResponseDto orderById = orderService.getOrderById(id);
		SuccessResponse<OrderResponseDto> successResponse = new SuccessResponse<>(HttpStatus.OK, "Order id found successfully !", orderById);
		return new ResponseEntity<SuccessResponse<OrderResponseDto>>(successResponse, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<SuccessResponse<List<OrderResponseDto>>> getAllOrders(){
		List<OrderResponseDto> order = orderService.getAllOrder();
		SuccessResponse<List<OrderResponseDto>> successResponse = new SuccessResponse<>(HttpStatus.OK, "Orders list found successfully", order);
		return new ResponseEntity<SuccessResponse<List<OrderResponseDto>>>(successResponse, HttpStatus.OK);
	}
	
	@GetMapping("/trigger")
	public ResponseEntity<String> triggerProducer(){
		OrderConfirmation confirmation = new OrderConfirmation("TEST-REF-999", BigDecimal.valueOf(999.99), null, null);
		orderProducer.sendOrderConfirmation(confirmation);
//		SuccessResponse<OrderConfirmation> response = new SuccessResponse<>(HttpStatus.CREATED, "Order produced successfully", confirmation);
		return new ResponseEntity<String>("Dummy message sent to Kafka! Check your Notification Service console.",HttpStatus.CREATED);
	}
}
