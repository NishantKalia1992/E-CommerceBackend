package com.product.requestDto;

import java.math.BigDecimal;

import com.product.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private long productId;
	private String name;
	private String description;
	private double availableQuantity;
	private BigDecimal price;
	private long categoryId;
	private Category category;
}
