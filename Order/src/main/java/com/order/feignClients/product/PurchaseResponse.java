package com.order.feignClients.product;

import java.math.BigDecimal;

public record PurchaseResponse(
		Long productId,
	    String productName,        // Matched JSON!
	    BigDecimal totalPrice,     // Matched JSON!
	    Double quantityPurchased,  // Matched JSON!
	    Double remainingQuantity,  // Matched JSON!
	    String message) {
}
