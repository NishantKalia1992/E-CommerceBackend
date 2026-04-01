package com.order.feignClient.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment", url = "${payment.client.url}")
public interface PaymentClient {
	
	@PostMapping("/create")
	long requestOrderPayment(@RequestBody Payment payment);

}
