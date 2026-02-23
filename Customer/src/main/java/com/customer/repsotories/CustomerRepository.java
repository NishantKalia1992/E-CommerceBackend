package com.customer.repsotories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByUsername(String username);
	Optional<Customer> findByContactNo(String contactNo);
	
}
