package com.product.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.product.requestDto.CategoryRequest;
import com.product.requestDto.ProductPurchaseRequest;
import com.product.requestDto.ProductRequest;
import com.product.responseDto.CategoryResponse;
import com.product.responseDto.ProductPurchaseResponse;
import com.product.responseDto.ProductResponse;

public interface ProductServices {
	ProductResponse addProduct(long id,ProductRequest request);
	ProductResponse addProduct(ProductRequest request);
	ProductResponse updateProduct(long id, ProductRequest request);
	ProductResponse getProduct(long id);
	List<ProductResponse> getAllProduct();
	ProductResponse deleteProduct(long id);
	
	CategoryResponse addCategory( CategoryRequest request);
	CategoryResponse updateCategory( long id, CategoryRequest request);
	CategoryResponse getCategory(long id);
	CategoryResponse deleteCategory(long id);
	List<CategoryResponse> getAllCategories();
	
	ProductPurchaseResponse purchaseProduct(ProductPurchaseRequest purchaseRequest);
	List<ProductPurchaseResponse> listOfPurchaseItem(List<ProductPurchaseRequest> requests);
	void releaseProduct(List<ProductPurchaseRequest> requests);
	
	ProductResponse addImageToProduct(long productId, String imageUrl);
	ProductResponse uploadProductImages(List<MultipartFile> files, long productId);
//	ProductResponse getProductWithImages(long productId);

}