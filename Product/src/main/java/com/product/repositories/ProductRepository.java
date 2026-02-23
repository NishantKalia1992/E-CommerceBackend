package com.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.entities.Products;

public interface ProductRepository extends JpaRepository<Products, Long>{
	
//	List<Products> findAllByProducts(List<Long> ids) ;
	
//	List<Products> findAllById(List<Long> ids);
	
}
