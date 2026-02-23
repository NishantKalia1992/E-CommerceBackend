package com.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entities.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
