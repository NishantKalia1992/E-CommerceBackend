package com.product.responseDto;

import java.util.ArrayList;
import java.util.List;

import com.product.entities.Products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryResponse {
	private long id;
	private String name;
	private String description;
	private List<Products> products = new ArrayList<>();
}
