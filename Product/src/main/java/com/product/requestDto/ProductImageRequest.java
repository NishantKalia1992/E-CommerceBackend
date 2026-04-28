package com.product.requestDto;

import com.product.entities.Products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequest {	
		private long productImageId;
		private String imageUrl;
		private Products products;
}
