package com.product.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.entities.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	
	List<ProductImage> findByProductProductId(long productId);
}
