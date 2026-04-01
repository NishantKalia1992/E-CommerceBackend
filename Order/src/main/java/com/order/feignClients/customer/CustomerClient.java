package com.order.feignClients.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.order.apiResponse.SuccessResponse;

//@FeignClient(name = "Customer", url = "${customer.client.url}")
@FeignClient(name = "customer-service", path = "/api/auth/customer")
public interface CustomerClient {
	
	@GetMapping("/customerId/{id}")
	public SuccessResponse<Customer> getCustomer(@PathVariable Long id);
}
