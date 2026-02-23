package com.product.responseDto;

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
public class ProductResponse {
	private long productId;
	private String name;
	private String description;
	private double availableQuantity;
	private BigDecimal price;
	private Category category;
	private long categoryId;
}
