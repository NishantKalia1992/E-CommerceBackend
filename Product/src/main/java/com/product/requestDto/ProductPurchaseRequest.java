package com.product.requestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductPurchaseRequest {
	@NotNull(message = "product id should be required ")
	private long productId;
	@Positive(message = "Quantity should me positive")
	private double quantity;
}
