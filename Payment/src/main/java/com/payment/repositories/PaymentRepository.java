package com.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
