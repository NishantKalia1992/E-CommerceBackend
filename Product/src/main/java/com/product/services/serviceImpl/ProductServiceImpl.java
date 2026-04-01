package com.product.services.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.InsufficientResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.product.entities.Category;
import com.product.entities.Products;
import com.product.exceptionHandler.DuplicateResourceExecption;
import com.product.exceptionHandler.ProductPurchaseException;
import com.product.exceptionHandler.ResourceNotFoundException;
import com.product.repositories.CategoryRepository;
import com.product.repositories.ProductRepository;
import com.product.requestDto.CategoryRequest;
import com.product.requestDto.CategoryResponse;
import com.product.requestDto.ProductPurchaseRequest;
import com.product.requestDto.ProductPurchaseResponse;
import com.product.requestDto.ProductRequest;
import com.product.responseDto.ProductResponse;
import com.product.services.ProductServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServices{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	final ProductRepository productRepository;
	final CategoryRepository categoryRepository;

	@Override
	public ProductResponse addProduct(long id,ProductRequest request) {
		
		LOGGER.info("Add products with category called !");
		
		Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("category not found with id "+id));
		
		Products products = new Products();
		products.setCategory(category);
		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());
		
		Products save = productRepository.save(products);
		LOGGER.info("product add save successfully");
		return mapToProductResponse(save);
	}
	
	@Override
	public ProductResponse addProduct(ProductRequest request) {
		
		LOGGER.info("Add products with category called !");
		
		long categoryId = request.getCategoryId();
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category not found with id "+categoryId));
		
		Products products = new Products();
		products.setCategory(category);
		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());
		
		Products save = productRepository.save(products);
		LOGGER.info("product add save successfully");
		return mapToProductResponse(save);
	}
	

	@Override
	public ProductResponse updateProduct(long id, ProductRequest request) {
		LOGGER.info("Add products with category called !");
		Products products = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("product id not found with id "+id));
		
		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());
		products.setCategory(request.getCategory());
		
		Products save = productRepository.save(products);
		LOGGER.info("product add save successfully");
		return mapToProductResponse(save);
	}

	@Override
	public ProductResponse getProduct(long id) {
		LOGGER.info("Get products with product id called !");
		Products products = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("product id not found with id "+id));
		return mapToProductResponse(products);
	}

	@Override
	public List<ProductResponse> getAllProduct() {
		LOGGER.info("Gett all products called !");
		List<ProductResponse> collect = productRepository.findAll().stream().map(t -> mapToProductResponse(t)).collect(Collectors.toList());
		return collect;
	}

	@Override
	public ProductResponse deleteProduct(long id) {
		LOGGER.info("Delete products called !");
		Products products = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("product id not found with id "+id));
		productRepository.delete(products);
		return mapToProductResponse(products);
	}

	@Override
	public CategoryResponse addCategory(CategoryRequest request) {
		LOGGER.info("Add category called !");
		if(categoryRepository.findById(request.getId()).isPresent()) {
			throw new DuplicateResourceExecption("Category Id is already present");
		}
		Category category = new Category();
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		
		Category save = categoryRepository.save(category);
		LOGGER.info("product add save successfully");
		return mapToCategortResponse(save);
	}

	@Override
	public CategoryResponse updateCategory(long id, CategoryRequest request) {
		LOGGER.info("Update category with categoryId called !");
		Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category id not found with id "+id));
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		Category save = categoryRepository.save(category);
		LOGGER.info("product add save successfully");
		return mapToCategortResponse(save);
	}
	
	@Override
	public CategoryResponse getCategory(long id) {
		Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category id not found wth id "+id));
		return mapToCategortResponse(category);
	}

	@Override
	public CategoryResponse deleteCategory(long id) {
		LOGGER.info("Delete category called!");
		Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category id not found with id "+id));
		categoryRepository.delete(category);
		return mapToCategortResponse(category);
	}

	@Override
	public List<CategoryResponse> getAllCategories() {
		LOGGER.info("Get all category called !");
		List<CategoryResponse> collect = categoryRepository.findAll().stream().map(t -> mapToCategortResponse(t)).collect(Collectors.toList());
		return collect;
	}
	
	public ProductResponse mapToProductResponse(Products products) {
		ProductResponse response = new ProductResponse();
		response.setProductId(products.getProductId());
		response.setAvailableQuantity(products.getAvailableQuantity());
		response.setName(products.getName());
		response.setDescription(products.getDescription());
		response.setPrice(products.getPrice());
		response.setCategory(products.getCategory());
		return response;
	}
	
	public CategoryResponse mapToCategortResponse(Category category) {
		CategoryResponse response = new CategoryResponse();
		response.setId(category.getId());
		response.setName(category.getName());
		response.setDescription(category.getDescription());
		response.setProducts(category.getProducts());
		return response;
	}
	
	public ProductPurchaseResponse toproductPurchaseResponse(Products products, double quantity) {
	    
	    // Calculate the total price for this specific item
	    BigDecimal quantityPrice = BigDecimal.valueOf(quantity);
	    BigDecimal totalPrice = products.getPrice().multiply(quantityPrice);
	    
	    return ProductPurchaseResponse.builder()
	            .productName(products.getName())
	            .quantityPurchased(quantity)
	            .totalPrice(totalPrice)
	            .remainingQuantity(products.getAvailableQuantity()) // This was already reduced in your loop!
	            .message("Purchase successful")
	            .build();
	}
	
	

	@Override
	public ProductPurchaseResponse purchaseProduct(ProductPurchaseRequest purchaseRequest) {
		
//		check product availability
		Products products = productRepository.findById(purchaseRequest.getProductId()).
		orElseThrow(()-> new ResourceNotFoundException("Product id not found with id"+purchaseRequest.getProductId()));
		
//		check stock
		if(products.getAvailableQuantity()<purchaseRequest.getQuantity()) {
			throw new IllegalArgumentException("Insufficient stock available !"+products.getAvailableQuantity());
		}
		
//		reduce Quantity
		double newQuantity = products.getAvailableQuantity() - purchaseRequest.getQuantity();
	    products.setAvailableQuantity(newQuantity);
	    
//	    calculate total price
	    BigDecimal quantityprice = BigDecimal.valueOf(purchaseRequest.getQuantity());
	    BigDecimal totalPrice = products.getPrice().multiply(quantityprice);
	    
	    productRepository.save(products);
	    LOGGER.info("Product purchased. Remaining stock !", newQuantity);
		return ProductPurchaseResponse.builder()
                .productName(products.getName())
                .quantityPurchased(purchaseRequest.getQuantity())
                .totalPrice(totalPrice)
                .remainingQuantity(newQuantity)
                .message("Purchase successful")
                .build();
	}

	@Transactional(rollbackOn = ProductPurchaseException.class)
	@Override
	public List<ProductPurchaseResponse> listOfPurchaseItem(List<ProductPurchaseRequest> requests) {
		LOGGER.info("list of all product purchase item");
		
		var list = requests.stream().map(ProductPurchaseRequest::getProductId).toList();
		var orderById = productRepository.findAllByProductIdInOrderByProductId(list);
		if(list.size()!=orderById.size()) {
			throw new ProductPurchaseException("One or more products doesn't exist");
		}
		var sortedRequest = requests.stream().sorted(Comparator.comparing(ProductPurchaseRequest::getProductId)).toList();
		var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
		for(int i=0;i<orderById.size();i++) {
			var product = orderById.get(i);
			var productRequest = sortedRequest.get(i);
			if(product.getAvailableQuantity()<productRequest.getQuantity()) {
				throw new ProductPurchaseException("insufficient stock for this product id !");
			}
			var newAvailableQuantity = product.getAvailableQuantity() - productRequest.getQuantity();
			product.setAvailableQuantity(newAvailableQuantity);
			productRepository.save(product);
			purchasedProducts.add(toproductPurchaseResponse(product,productRequest.getQuantity()));
		}
		return purchasedProducts;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void releaseProduct(List<ProductPurchaseRequest> requests) {
		LOGGER.info("purchase product back to inventory");
		for(ProductPurchaseRequest purchaseRequest:requests) {
			Products products = productRepository.findById(purchaseRequest.getProductId()).orElseThrow(()-> new ResourceNotFoundException("Product not found with id - "+purchaseRequest.getProductId()));
//			add qantity back to inventory
			double newAvailableQuantity = products.getAvailableQuantity()+purchaseRequest.getQuantity();
			products.setAvailableQuantity(newAvailableQuantity);
			productRepository.save(products);
		}
		
	}

	
}
