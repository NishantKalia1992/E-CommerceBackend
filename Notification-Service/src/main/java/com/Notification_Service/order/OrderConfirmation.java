package com.Notification_Service.order;
//@Bean
import java.math.BigDecimal;
import java.util.List;

//import com.Notification_Service.order.payment.PaymentMethod;
//@Component

public record OrderConfirmation(
		String orderReference,
		BigDecimal totalAmount,
//		PaymentMethod paymentMethod,
		Customer customer,
		List<Product> products) {
	
}
