package com.order.requestDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.dto.PaymentMethod;
import com.order.entities.OrderLine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderRequest {
	private long id;
	private String refrence;
	private BigDecimal totalAmount;
	private PaymentMethod paymentMethod;
	private Long customerId;
	private List<OrderLine> orderLines;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}
