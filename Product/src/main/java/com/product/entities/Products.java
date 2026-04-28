package com.product.entities;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;
	@Column(unique = true)
	@NotNull(message = "product name is required")
	private String name;
	@NotNull(message = "product description is required")
	private String description;
	@Positive(message = "Available quantity should be positive")
	private double availableQuantity;
	@Positive(message = "Price should be positive")
	private BigDecimal price;
	@NotNull(message = "product category is required")
	@ManyToOne
	private Category category;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> images;
	
	
}