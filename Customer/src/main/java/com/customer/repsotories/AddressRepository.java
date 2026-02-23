package com.customer.repsotories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
