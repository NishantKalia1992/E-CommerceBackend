package com.customer.requestDto;

import java.util.ArrayList;
import java.util.List;

import com.customer.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
	private long id;
	private String fullName;
	private String username;
	private String password;
	private String contactNo;
	private String authProvider="LOCAL";
	private List<Address> address = new ArrayList<>();
}
