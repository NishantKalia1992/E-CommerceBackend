package com.product.requestDto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductPurchaseResponse {
	private String productName;
	private double quantityPurchased;
	private BigDecimal totalPrice;
	private double remainingQuantity;
	private String message;
}
