package com.Notification_Service.order;

import java.math.BigDecimal;

public record Product(
		long productId, 
		String name, 
		String description, 
		BigDecimal price, 
		double quantity) {

}
