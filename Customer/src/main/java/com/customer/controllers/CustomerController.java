package com.customer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.apiResponse.SuccessResponse;
import com.customer.requestDto.AddressRequest;
import com.customer.requestDto.CustomerRequest;
import com.customer.responseDto.AddressResponse;
import com.customer.responseDto.CustomerResponse;
import com.customer.services.CustomerServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
	@Autowired
	CustomerServices customerServices;
	
	@PostMapping("/add")
	public ResponseEntity<SuccessResponse<CustomerResponse>> addCustomer(@Valid @RequestBody CustomerRequest request){
		CustomerResponse customer = customerServices.createCustomer(request);
		SuccessResponse<CustomerResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Customer Register Successfully", customer);
		return new ResponseEntity<> (response, HttpStatus.CREATED);
	}
	@PutMapping("/edit/{username}")
	public ResponseEntity<SuccessResponse<CustomerResponse>> editCustomer(@PathVariable String username, @Valid @RequestBody CustomerRequest request){
		CustomerResponse customerResponse = customerServices.editCustomer(username, request);
		SuccessResponse<CustomerResponse> successResponse = new SuccessResponse<>(HttpStatus.ACCEPTED, "Customer information edited succssfully", customerResponse);
		return new ResponseEntity<>(successResponse, HttpStatus.ACCEPTED);
	}
	@GetMapping("/getDetails")
	public ResponseEntity<SuccessResponse<CustomerResponse>> getDetails(
			@RequestParam(required = false) String contactNo, 
			@RequestParam(required = false) Long id, 
			@RequestParam(required = false) String username)
	{
		CustomerResponse customerResponse = customerServices.findCustomer(contactNo, id, username);
		SuccessResponse<CustomerResponse> successResponse = new SuccessResponse<>(HttpStatus.OK, "Customer details found", customerResponse);
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}
	
	@GetMapping("/customerId/{id}")
	public ResponseEntity<SuccessResponse<CustomerResponse>> getCustomerId(@PathVariable Long id){
		CustomerResponse customerId = customerServices.getCustomerId(id);
		SuccessResponse<CustomerResponse> successResponse = new SuccessResponse<>(HttpStatus.OK, "Customer id found successful ", customerId);
		return new ResponseEntity<SuccessResponse<CustomerResponse>>(successResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{contactNo}")
	public ResponseEntity<SuccessResponse<CustomerResponse>> deleteCustomer(@PathVariable String contactNo){
		CustomerResponse customer = customerServices.deleteCustomer(contactNo);
		SuccessResponse<CustomerResponse> response = new SuccessResponse<>(HttpStatus.OK, "Customer Account delete successfully !", customer);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/addAddress/{contactNo}")
	public ResponseEntity<SuccessResponse<AddressResponse>> addAddress(@PathVariable String contactNo, @Valid @RequestBody AddressRequest request){
		AddressResponse address = customerServices.addAddress(contactNo, request);
		SuccessResponse<AddressResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Address added successfully ", address);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/addAddress/{contactNo}")
	public ResponseEntity<SuccessResponse<AddressResponse>> editAddress(@PathVariable String contactNo, @Valid @RequestBody AddressRequest request){
		AddressResponse address = customerServices.addAddress(contactNo, request);
		SuccessResponse<AddressResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Address added successfully ", address);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/getAddress/{id}")
	public ResponseEntity<SuccessResponse<AddressResponse>> getAddress(@PathVariable Long id){
		AddressResponse address = customerServices.findAddress(id);
		SuccessResponse<AddressResponse> response = new SuccessResponse<>(HttpStatus.OK, "Details found ", address);
		return new ResponseEntity<SuccessResponse<AddressResponse>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAddress/{id}")
	public ResponseEntity<SuccessResponse<AddressResponse>> deleteAddress(@PathVariable Long id){
		AddressResponse address = customerServices.findAddress(id);
		SuccessResponse<AddressResponse> response = new SuccessResponse<>(HttpStatus.OK, "Address delete successfully ", address);
		return new ResponseEntity<SuccessResponse<AddressResponse>>(response, HttpStatus.OK);
	}
}
