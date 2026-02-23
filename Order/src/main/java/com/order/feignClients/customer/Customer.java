package com.order.feignClients.customer;

import lombok.Data;

@Data
public class Customer {
	private Long id;
	private String fullName;
	private String username;
	private String contactNo;
}
