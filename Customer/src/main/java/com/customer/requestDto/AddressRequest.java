package com.customer.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
	private long id;
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String zipcode;
}
