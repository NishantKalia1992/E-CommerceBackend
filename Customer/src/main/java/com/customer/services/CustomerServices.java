package com.customer.services;

import com.customer.requestDto.AddressRequest;
import com.customer.requestDto.CustomerRequest;
import com.customer.responseDto.AddressResponse;
import com.customer.responseDto.CustomerResponse;

public interface CustomerServices {
	CustomerResponse createCustomer(CustomerRequest request);
	CustomerResponse registerWithGoogle(CustomerRequest request);
	CustomerResponse editCustomer(String username, CustomerRequest request);
	CustomerResponse profileComplete(String username, CustomerRequest request);
	CustomerResponse findCustomer(String contactNo, Long customerId, String username);
	CustomerResponse deleteCustomer(String contactNo);
	CustomerResponse deleteCustomerId(Long id);
	CustomerResponse getCustomerId(Long id);
	CustomerResponse getUsername(String username);
	
	AddressResponse addAddress(String contactNo, AddressRequest request);
	AddressResponse editAddress(String contactNo, AddressRequest request);
	AddressResponse findAddress(Long id);
	AddressResponse deleteAddress(Long id);
}
