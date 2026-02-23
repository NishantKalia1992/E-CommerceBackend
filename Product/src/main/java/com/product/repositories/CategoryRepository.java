package com.product.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Category findByName(String name);
//	List<Category> getAllCategories(List<String> name);
}
