package com.order.feignClients.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductClientResponse {
	private Object status; // We can ignore this or map it
    private String message;
    private List<PurchaseResponse> data; // <-- This is the gold we actually want!
}
