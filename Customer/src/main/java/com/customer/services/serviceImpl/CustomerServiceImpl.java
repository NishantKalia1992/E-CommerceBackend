package com.customer.services.serviceImpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.entities.Address;
import com.customer.entities.Customer;
import com.customer.exceptions.DuplicateResourceException;
import com.customer.exceptions.ResourceNotFoundException;
import com.customer.repsotories.AddressRepository;
import com.customer.repsotories.CustomerRepository;
import com.customer.requestDto.AddressRequest;
import com.customer.requestDto.CustomerRequest;
import com.customer.responseDto.AddressResponse;
import com.customer.responseDto.CustomerResponse;
import com.customer.services.CustomerServices;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	PasswordEncoder encoder;
	
	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {
		LOGGER.info("create customer called !", request.getFullName());
		if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new DuplicateResourceException("Username is already present : " + request.getUsername());
		}
		Customer customer = new Customer();

		customer.setFullName(request.getFullName());
		customer.setUsername(request.getUsername());
		customer.setPassword(encoder.encode(request.getPassword()));
		customer.setContactNo(request.getContactNo());

		Customer save = customerRepository.save(customer);
		return mapCusotmerToCustomerResponse(save);

	}
	
	@Override
	public CustomerResponse registerWithGoogle(CustomerRequest request) {
		Customer customer = new Customer();
		customer.setFullName(request.getFullName());
		customer.setUsername(request.getUsername());
		customer.setAuthProvider("GOOGLE");
		Customer save = customerRepository.save(customer);
		return mapCusotmerToCustomerResponse(save);
	}

	@Override
	public CustomerResponse profileComplete(String username, CustomerRequest request) {
		LOGGER.info("update profile called - ", username);
		Customer customer = customerRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("username not found - "+username));
		customer.setContactNo(request.getContactNo());
		customer.setPassword(encoder.encode(request.getPassword()));
		Customer save = customerRepository.save(customer);
		return mapCusotmerToCustomerResponse(save);
	}

		
	@Override
	public CustomerResponse editCustomer(String username, CustomerRequest request) {
		LOGGER.info("edit customer information called : " + request.getFullName());

		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new ResourceNotFoundException("Username not found with username : " + request.getUsername()));

		customer.setFullName(request.getFullName());
		customer.setPassword(encoder.encode(request.getPassword()));
		customer.setContactNo(request.getContactNo());

		Customer save = customerRepository.save(customer);
		return mapCusotmerToCustomerResponse(save);
	}

	@Override
	public CustomerResponse findCustomer(String contactNo, Long customerId, String username) {
		
		Optional<Customer> customOptional;
		
		if (username != null && !username.isBlank()) {
			LOGGER.info("Attempting to find user information through usrename", username);
			customOptional=customerRepository.findByUsername(username);
		} else if (contactNo != null && !contactNo.isBlank()) {
			LOGGER.info("Attempting to find user information through contact no", contactNo);
			customOptional= customerRepository.findByContactNo(contactNo);
		}else if (customerId!=null && customerId>0) {
			LOGGER.info("Ateempting to find user infromation through customer Id", customerId);
			customOptional= customerRepository.findById(customerId);
		}else {
			LOGGER.warn("find customer with no parameter");
			throw new IllegalArgumentException("A valid username, contact number, or customer ID is required to find a customer.");
		}
		Customer customerFound = customOptional.orElseThrow(()-> new ResourceNotFoundException("Customer not found with provided user details "));
		return mapCusotmerToCustomerResponse(customerFound);
	}

	@Override
	public CustomerResponse getCustomerId(Long id) {
		LOGGER.info("Get customer account Id ",id);
		Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer Id not found by id : "+id));
		return mapCusotmerToCustomerResponse(customer);
	}
	
//	@Transactional
	@Override
	public CustomerResponse deleteCustomer(String contactNo) {
		LOGGER.info("Deleting customer account ",contactNo);
		Customer customer = customerRepository.findByContactNo(contactNo).orElseThrow(()-> new ResourceNotFoundException("User account not found"));
		customerRepository.delete(customer);
		return mapCusotmerToCustomerResponse(customer);
	}

	@Override
	public CustomerResponse deleteCustomerId(Long id) {
		LOGGER.info("Deleting customer account by id ",id);
		Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User account not found by id : "+id));
		customerRepository.delete(customer);
		return null;
	}
	
	@Override
	public CustomerResponse getUsername(String username) {
		LOGGER.info("finding username ",username);
		Customer customer = customerRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("username not found by : "+username));
		return mapCusotmerToCustomerResponse(customer);
	}

	@Override
	public AddressResponse addAddress(String username, AddressRequest request) {
		LOGGER.info("Add address by customer contact no");
		Customer customer = customerRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("Customer contact no not found : "+username));
		Address address = new Address();
		address.setHouseNo(request.getHouseNo());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setZipcode(request.getZipcode());
		address.setCustomer(customer);
		
		Address save = addressRepository.save(address);
		LOGGER.info("Address add successfull !");
		return mapAddressToAddressResponse(save);
	}

	@Override
	public AddressResponse editAddress(String contactNo, AddressRequest request) {
		LOGGER.info("edit address by customer contact no");
		Customer customer = customerRepository.findByContactNo(contactNo).orElseThrow(()-> new ResourceNotFoundException("Customer contact no not found : "+contactNo));
		Address address = new Address();
		address.setHouseNo(request.getHouseNo());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setZipcode(request.getZipcode());
		address.setCustomer(customer);
		
		Address save = addressRepository.save(address);
		LOGGER.info(" Edit Address successfull !");
		return mapAddressToAddressResponse(save);
	}

	@Override
	public AddressResponse findAddress(Long id) {
		LOGGER.info("Find address called!");
		Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Address if not found with address Id : "+id));
		return mapAddressToAddressResponse(address);
	}
//	@Transactional
	@Override
	public AddressResponse deleteAddress(Long id) {
		LOGGER.info("Delete address called!");
		Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Address if not found with address Id : "+id));
		addressRepository.delete(address);
		return mapAddressToAddressResponse(address);
	}

	public CustomerResponse mapCusotmerToCustomerResponse(Customer customer) {
		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setId(customer.getId());
		customerResponse.setFullName(customer.getFullName());
		customerResponse.setUsername(customer.getUsername());
		customerResponse.setPassword(customer.getPassword());
		customerResponse.setContactNo(customer.getContactNo());
		return customerResponse;
	}

	public AddressResponse mapAddressToAddressResponse(Address address) {
		AddressResponse response = new AddressResponse();
		response.setId(address.getId());
		response.setHouseNo(address.getHouseNo());
		response.setStreet(address.getStreet());
		response.setCity(address.getCity());
		response.setState(address.getState());
		response.setZipcode(address.getZipcode());
		return response;
	}

	
}
