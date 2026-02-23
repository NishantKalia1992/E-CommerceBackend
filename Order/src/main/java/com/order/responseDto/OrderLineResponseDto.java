package com.order.responseDto;

import com.order.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderLineResponseDto {
	private long id;
	private long productId;
	private double quantity;
	private Order order;
}
