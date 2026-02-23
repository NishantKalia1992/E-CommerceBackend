package com.order.services;

import java.util.List;

import com.order.requestDto.OrderRequest;
import com.order.responseDto.OrderResponseDto;

public interface OrderService {
	public OrderResponseDto createOrder(OrderRequest request);
	public OrderResponseDto getOrderById(long orderId);
	public List<OrderResponseDto> getAllOrder();
	
}
