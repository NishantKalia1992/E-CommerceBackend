package com.payment.dto;

import java.math.BigDecimal;

import com.payment.entities.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentNotificationRequest
{
	private String orderReference;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String customerFirstname;
    private String customerLastname;
    private String customerEmail;
}
