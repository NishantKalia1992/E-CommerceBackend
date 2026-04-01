package com.order.feignClients.product;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PurchaseRequest {
	@NotNull(message = "prodcut is mandatory")
	private Long productId;
	@Positive(message = "quantity is mandatory")
	private double quantity;
}
