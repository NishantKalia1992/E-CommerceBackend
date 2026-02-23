package com.order.requestDto;

import com.order.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineRequest {
	private long id;
	private long productId;
	private double quantity;
	private Order order;
}
