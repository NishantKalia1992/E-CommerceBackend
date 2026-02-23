package com.order.services.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.apiResponse.SuccessResponse;
import com.order.entities.Order;
import com.order.entities.OrderLine;
import com.order.exceptionHandler.ResourceNotFoundException;
import com.order.feignClients.customer.Customer;
import com.order.feignClients.customer.CustomerClient;
import com.order.kafka.OrderConfirmation;
import com.order.kafka.OrderProducer;
import com.order.repositories.OrderRepository;
import com.order.requestDto.OrderRequest;
import com.order.responseDto.OrderResponseDto;
import com.order.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	OrderProducer orderProducer;

	@Override
	public OrderResponseDto createOrder(OrderRequest request) {
		LOGGER.info("Add order using customer id !", request.getCustomerId());

		SuccessResponse<Customer> customerResponse = customerClient.getCustomer(request.getCustomerId());
		Customer customer = customerResponse.getData();

		if (customer == null) {
			LOGGER.warn("Customer not found with id : {}", request.getCustomerId());
			throw new ResourceNotFoundException("Customer not found with id : {}" + request.getCustomerId());
		}

		LOGGER.info("Customer successful validation : {}", customer.getUsername());

		Order order = new Order();
		order.setRefrence(request.getRefrence());
		order.setPaymentMethod(request.getPaymentMethod());
		order.setCreatedDate(request.getCreatedDate());
		order.setLastModifiedDate(request.getLastModifiedDate());
		order.setTotalAmount(request.getTotalAmount());
		order.setCustomerId(customer.getId());

//		list orderLines = request.getOrderLines();
		List<OrderLine> orderLines = request.getOrderLines();
		for (OrderLine line : orderLines) {
			line.setOrder(order);
		}
		order.setOrderLines(orderLines);
		
//		send notification using kafka
		orderProducer.sendOrderConfirmation(
				new OrderConfirmation(request.getRefrence(), request.getTotalAmount(), request.getPaymentMethod(), customer));
				
		
		Order save = orderRepository.save(order);
		LOGGER.info("Order saved successfully with reference: {}", save.getRefrence());
		return mapToResponseDto(save);
	}
	
	@Override
	public OrderResponseDto getOrderById(long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("OrderId not found with this id "+orderId));
		return mapToResponseDto(order);
	}

	@Override
	public List<OrderResponseDto> getAllOrder() {
		List<OrderResponseDto> collect = orderRepository.findAll().stream().map(t -> mapToResponseDto(t)).collect(Collectors.toList());
		return collect;
	}
	public OrderResponseDto mapToResponseDto(Order order) {
		OrderResponseDto responseDto = new OrderResponseDto();
		responseDto.setId(order.getId());
		responseDto.setRefrence(order.getRefrence());
		responseDto.setOrderLines(order.getOrderLines());
		responseDto.setPaymentMethod(order.getPaymentMethod());
		responseDto.setCreatedDate(order.getCreatedDate());
		responseDto.setLastModifiedDate(order.getLastModifiedDate());

		responseDto.setTotalAmount(order.getTotalAmount());
		responseDto.setCustomerId(order.getCustomerId());

		return responseDto;
	}

	

}
