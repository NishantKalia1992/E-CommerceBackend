package com.order.feignClients.product;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

//@FeignClient(name = "product",  url = "${product.client.url}") hardcoded url
@FeignClient(name = "product", path = "/api/category")
public interface ProductClient  {

	@PostMapping("/getAllPurchasedList")
	ProductClientResponse purchaseProducts(@Valid @RequestBody List<PurchaseRequest> requestBody);
	
	@PostMapping("/release")
	void releaseProduct(@RequestBody List<PurchaseRequest> requests);
}
