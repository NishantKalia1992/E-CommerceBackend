package com.product.exceptionHandler;

public class ProductPurchaseException extends RuntimeException {
	public ProductPurchaseException(String s) {
		super(s);
	}
}
