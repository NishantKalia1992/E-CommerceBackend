package com.payment.dto;

import java.math.BigDecimal;

import com.payment.entities.PaymentMethod;

import lombok.Data;
@Data
public class PaymentNotificationRequest
{
	private String orderReference;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String customerFulltname;
    private String customerLastname;
    private String customerEmail;
}
