package com.product.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entities.Products;
@Repository
public interface ProductRepository extends JpaRepository<Products, Long>{
	
//	List<Products> findAllByProducts(List<Long> products);
	// Notice we changed Id to ProductId
	List<Products> findAllByProductIdInOrderByProductId(List<Long> productIds);
	
}
