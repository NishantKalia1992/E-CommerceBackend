package com.product.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.apiResponse.SuccessResponse;
import com.product.requestDto.CategoryRequest;
import com.product.requestDto.CategoryResponse;
import com.product.requestDto.ProductPurchaseRequest;
import com.product.requestDto.ProductPurchaseResponse;
import com.product.requestDto.ProductRequest;
import com.product.responseDto.ProductResponse;
import com.product.services.ProductServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class ProductsController {
	private final ProductServices productServices;
	
	@PostMapping("/add")
	public ResponseEntity<SuccessResponse<CategoryResponse>> addCategory (@Valid @RequestBody CategoryRequest request){
		CategoryResponse category = productServices.addCategory(request);
		SuccessResponse<CategoryResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Category add successful", category);
		return new ResponseEntity<SuccessResponse<CategoryResponse>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateCategory/{id}")
	public ResponseEntity<SuccessResponse<CategoryResponse>> updateCategory(@PathVariable long id, @Valid @RequestBody CategoryRequest request){
		CategoryResponse updateCategory = productServices.updateCategory(id, request);
		SuccessResponse<CategoryResponse> response = new SuccessResponse<>(HttpStatus.OK, "Category update successful", updateCategory);
		return new ResponseEntity<SuccessResponse<CategoryResponse>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getCategory/{id}")
	public ResponseEntity<SuccessResponse<CategoryResponse>> getCategory(@PathVariable long id){
		CategoryResponse category = productServices.getCategory(id);
		SuccessResponse<CategoryResponse> response = new SuccessResponse<>(HttpStatus.OK, "Category id found successful", category);
		return new ResponseEntity<SuccessResponse<CategoryResponse>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<SuccessResponse<List<CategoryResponse>>> getAll(){	
		List<CategoryResponse> categories = productServices.getAllCategories();
		SuccessResponse<List<CategoryResponse>> response = new SuccessResponse<>(HttpStatus.OK, "Getting All category successful", categories);
		return new ResponseEntity<SuccessResponse<List<CategoryResponse>>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<SuccessResponse<CategoryResponse>> deleteCategory(@PathVariable long id){
		CategoryResponse deleteCategory = productServices.deleteCategory(id);
		SuccessResponse<CategoryResponse> response = new SuccessResponse<>(HttpStatus.OK, "Delete category successful", deleteCategory);
		return new ResponseEntity<SuccessResponse<CategoryResponse>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/addProduct/{id}")
	public ResponseEntity<SuccessResponse<ProductResponse>> addProducts (@PathVariable long id, @Valid @RequestBody ProductRequest request){
		ProductResponse product = productServices.addProduct(id, request);
		SuccessResponse<ProductResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Product add successfully", product);
		return new ResponseEntity<SuccessResponse<ProductResponse>>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/addProducts")
	public ResponseEntity<SuccessResponse<ProductResponse>> addProducts (@Valid @RequestBody ProductRequest request){
		ProductResponse product = productServices.addProduct(request);
		SuccessResponse<ProductResponse> response = new SuccessResponse<>(HttpStatus.CREATED, "Product add successfully", product);
		return new ResponseEntity<SuccessResponse<ProductResponse>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateProduct/{id}")
	public ResponseEntity<SuccessResponse<ProductResponse>> updateProduct(@PathVariable long id, @Valid @RequestBody ProductRequest request){
		ProductResponse updateProduct = productServices.updateProduct(id, request);
		SuccessResponse<ProductResponse> response = new SuccessResponse<>(HttpStatus.OK, "Update product successfully", updateProduct);
		return new ResponseEntity<SuccessResponse<ProductResponse>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getProduct/{id}")
	public ResponseEntity<SuccessResponse<ProductResponse>> getProducts(@PathVariable long id){
		ProductResponse product = productServices.getProduct(id);
		SuccessResponse<ProductResponse> response = new SuccessResponse<>(HttpStatus.OK, "Product get successful", product);
		return new ResponseEntity<SuccessResponse<ProductResponse>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getAllProducts")
	public ResponseEntity<SuccessResponse<List<ProductResponse>>> getAllProducts(){
		List<ProductResponse> allProduct = productServices.getAllProduct();
		SuccessResponse<List<ProductResponse>> response = new SuccessResponse<>(HttpStatus.OK, "Product list successful", allProduct);
		return new ResponseEntity<SuccessResponse<List<ProductResponse>>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<SuccessResponse<ProductResponse>> deleteProduct(@PathVariable long id){
		ProductResponse deleteProduct = productServices.deleteProduct(id);
		SuccessResponse<ProductResponse> response = new SuccessResponse<>(HttpStatus.OK, "Delet Product successfully", deleteProduct);
		return new ResponseEntity<SuccessResponse<ProductResponse>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<SuccessResponse<ProductPurchaseResponse>> purchaseProduct(@Valid @RequestBody ProductPurchaseRequest request){
		ProductPurchaseResponse purchaseProduct = productServices.purchaseProduct(request);
		SuccessResponse<ProductPurchaseResponse> response = new SuccessResponse<>(HttpStatus.OK, "product purchased successful", purchaseProduct);
		return new ResponseEntity<SuccessResponse<ProductPurchaseResponse>>(response, HttpStatus.OK);
	}
	@PostMapping("/getAllPurchasedList")
	public ResponseEntity<SuccessResponse<List<ProductPurchaseResponse>>> purchaseMultipleProducts(@Valid @RequestBody List<ProductPurchaseRequest> requests) {	    
	    List<ProductPurchaseResponse> purchaseItem = productServices.listOfPurchaseItem(requests);	    
	    SuccessResponse<List<ProductPurchaseResponse>> response = new SuccessResponse<>(HttpStatus.OK, "Order list successful", purchaseItem);	
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PostMapping("/release")
	public ResponseEntity<SuccessResponse<String>> releaseProduct(@RequestBody List<ProductPurchaseRequest> requests){
		productServices.releaseProduct(requests);
		SuccessResponse<String> response = new SuccessResponse<>(HttpStatus.OK, "Product successfully release back to inventory", "Success");
		return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.OK);
	}

}
