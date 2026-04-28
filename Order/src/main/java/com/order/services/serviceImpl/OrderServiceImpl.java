package com.order.services.serviceImpl;

import java.math.BigDecimal;
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
import com.order.feignClient.payment.Payment;
import com.order.feignClient.payment.PaymentClient;
import com.order.feignClient.payment.PaymentClientWrapper;
import com.order.feignClients.customer.Customer;
import com.order.feignClients.customer.CustomerClient;
import com.order.feignClients.product.ProductClient;
import com.order.feignClients.product.ProductClientResponse;
import com.order.feignClients.product.PurchaseRequest;
import com.order.feignClients.product.PurchaseResponse;
import com.order.kafka.OrderConfirmation;
import com.order.kafka.OrderProducer;
import com.order.repositories.OrderRepository;
import com.order.requestDto.OrderRequest;
import com.order.responseDto.OrderResponseDto;
import com.order.services.OrderService;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	OrderProducer orderProducer;
	@Autowired
	ProductClient productClient;
	@Autowired
	PaymentClient paymentClient;
	@Autowired
	PaymentClientWrapper paymentClientWrapper;
	
	
//move to feign client as per industry standard
//	@CircuitBreaker(name = "product-service", fallbackMethod = "fallbackPurchase")
//	@Retry(name = "product-service")
	
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
		
		LOGGER.info("sending request to product service to deduct inventory");
		
		List<PurchaseRequest> purchaseRequests = request.getOrderLines()
                .stream()
                .map(line -> new PurchaseRequest(line.getProductId(),line.getQuantity())).toList();
		
		LOGGER.info("Attempting to call Product Service via Feign...");

        // 2. Tell the Product Service to deduct the stock via Feign!
        // If an item is out of stock, Feign will throw an exception here and instantly stop the order from saving.
        ProductClientResponse responseWrapper = productClient.purchaseProducts(purchaseRequests);
        List<PurchaseResponse> purchasedProducts = responseWrapper.getData();
        
        BigDecimal calculatedAmount = purchasedProducts.stream().map(PurchaseResponse::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        LOGGER.info("Successfully purchased {} items from Product Service", purchasedProducts.size());
       
		Order order = new Order();
		order.setRefrence(request.getRefrence());
		order.setPaymentMethod(request.getPaymentMethod());
		order.setCreatedDate(request.getCreatedDate());
		order.setLastModifiedDate(request.getLastModifiedDate());
		order.setTotalAmount(calculatedAmount);
		order.setCustomerId(customer.getId());

		List<OrderLine> orderLines = request.getOrderLines();
//		List<OrderLine> orderLines = request.getOrderLines();
		for (OrderLine line : orderLines) {
			line.setOrder(order);
		}
		order.setOrderLines(orderLines);		
		Order save = orderRepository.save(order);
		
		LOGGER.info("Order saved successfully with reference: {}", save.getRefrence());
//		send notification using kafka
		
		long paymentId;
		try {
			LOGGER.info("attempting the payment process using payment service...");
			Payment paymentRequest = new Payment(calculatedAmount, request.getPaymentMethod(), save.getId(), save.getRefrence(), customer);
//			paymentId = paymentClient.requestOrderPayment(paymentRequest);
			paymentId = paymentClientWrapper.paymentProcess(paymentRequest);
		} 
		catch (Exception e) {
            // 1. PRINT THE REAL ERROR SO WE ARE NOT BLIND
            LOGGER.error("SAGA TRIGGERED! Payment failed because: {}", e.getMessage());
            e.printStackTrace(); // This forces the giant red stack trace to print!
            // 2. LOG BEFORE THE UNDO
            LOGGER.info("Attempting to call Product Service to release inventory...");            
            // undo the inventory
            productClient.releaseProduct(purchaseRequests);            
            LOGGER.info("Inventory successfully back to product service");            
            // order delete or set the status of cancel
            orderRepository.delete(save);
            LOGGER.info("Order record deleted due to payment failure");
            
            throw new RuntimeException("Order failed due to payment processing error. No inventory was lost.");
        }	
		orderProducer.sendOrderConfirmation(
			    new OrderConfirmation(
			        request.getRefrence(), 
			        calculatedAmount,      // <-- THE FIX
			        request.getPaymentMethod(), 
			        customer
			    )
			);
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
//		List<OrderResponseDto> collect = orderRepository.findAllOrdersWithCustomer().stream().map(t -> mapToResponseDto(t)).collect(Collectors.toList());
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
	
	public OrderResponseDto fallbackPurchase(OrderRequest request, Throwable t) {
		LOGGER.error("CIRCUIT BREAKER TRIPPED! Product Service is down. Reason: {}", t.getMessage());
	    
	    // Plan B: Throw a friendly error instead of crashing the server!
	    throw new RuntimeException("Oops! Our inventory system is currently down. Please try your order again in a few minutes.");
	}
	

}
