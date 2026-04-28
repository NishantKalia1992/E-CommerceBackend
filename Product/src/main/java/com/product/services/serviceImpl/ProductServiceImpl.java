package com.product.services.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.constant.ConstantFile;
import com.product.entities.Category;
import com.product.entities.ProductImage;
import com.product.entities.Products;
import com.product.exceptionHandler.DuplicateResourceExecption;
import com.product.exceptionHandler.ProductPurchaseException;
import com.product.exceptionHandler.ResourceNotFoundException;
import com.product.repositories.CategoryRepository;
import com.product.repositories.ProductImageRepository;
import com.product.repositories.ProductRepository;
import com.product.requestDto.CategoryRequest;
import com.product.requestDto.ProductPurchaseRequest;
import com.product.requestDto.ProductRequest;
import com.product.responseDto.CategoryResponse;
import com.product.responseDto.ProductPurchaseResponse;
import com.product.responseDto.ProductResponse;
import com.product.services.ProductServices;
import com.product.services.StorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);


	final ProductRepository productRepository;
	final CategoryRepository categoryRepository;
	final ProductImageRepository productImageRepository;
	final ConstantFile constantFile;

	@Autowired
	private StorageService storageService;

	// ── Product CRUD ────────────────────────────────────────────────────────────

	@Override
	public ProductResponse addProduct(long id, ProductRequest request) {
		LOGGER.info("Add product with category id={} called", id);
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

		Products products = new Products();
		products.setCategory(category);
		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());

		Products saved = productRepository.save(products);
		LOGGER.info("Product saved successfully with id={}", saved.getProductId());
		return mapToProductResponse(saved);
	}

	@Override
	public ProductResponse addProduct(ProductRequest request) {
		LOGGER.info("Add product called");
		long categoryId = request.getCategoryId();
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

		Products products = new Products();
		products.setCategory(category);
		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());

		Products saved = productRepository.save(products);
		LOGGER.info("Product saved successfully with id={}", saved.getProductId());
		return mapToProductResponse(saved);
	}

	@Override
	public ProductResponse updateProduct(long id, ProductRequest request) {
		LOGGER.info("Update product id={} called", id);
		Products products = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

		products.setName(request.getName());
		products.setDescription(request.getDescription());
		products.setAvailableQuantity(request.getAvailableQuantity());
		products.setPrice(request.getPrice());
		products.setCategory(request.getCategory());

		Products saved = productRepository.save(products);
		return mapToProductResponse(saved);
	}

	@Override
	public ProductResponse getProduct(long id) {
		LOGGER.info("Get product id={} called", id);
		Products products = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		return mapToProductResponse(products);
	}

	@Override
	public List<ProductResponse> getAllProduct() {
		LOGGER.info("Get all products called");
		return productRepository.findAll()
				.stream()
				.map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Override
	public ProductResponse deleteProduct(long id) {
		LOGGER.info("Delete product id={} called", id);
		Products products = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		productRepository.delete(products);
		return mapToProductResponse(products);
	}

	// ── Category CRUD ───────────────────────────────────────────────────────────

	@Override
	public CategoryResponse addCategory(CategoryRequest request) {
		LOGGER.info("Add category called");
		if (categoryRepository.findById(request.getId()).isPresent()) {
			throw new DuplicateResourceExecption("Category Id is already present");
		}
		Category category = new Category();
		category.setName(request.getName());
		category.setDescription(request.getDescription());

		Category saved = categoryRepository.save(category);
		return mapToCategortResponse(saved);
	}

	@Override
	public CategoryResponse updateCategory(long id, CategoryRequest request) {
		LOGGER.info("Update category id={} called", id);
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		Category saved = categoryRepository.save(category);
		return mapToCategortResponse(saved);
	}

	@Override
	public CategoryResponse getCategory(long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
		return mapToCategortResponse(category);
	}

	@Override
	public CategoryResponse deleteCategory(long id) {
		LOGGER.info("Delete category id={} called", id);
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
		categoryRepository.delete(category);
		return mapToCategortResponse(category);
	}

	@Override
	public List<CategoryResponse> getAllCategories() {
		LOGGER.info("Get all categories called");
		return categoryRepository.findAll()
				.stream()
				.map(this::mapToCategortResponse)
				.collect(Collectors.toList());
	}

	// ── Purchase ────────────────────────────────────────────────────────────────

	@Override
	public ProductPurchaseResponse purchaseProduct(ProductPurchaseRequest purchaseRequest) {
		Products products = productRepository.findById(purchaseRequest.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Product not found with id " + purchaseRequest.getProductId()));

		if (products.getAvailableQuantity() < purchaseRequest.getQuantity()) {
			throw new IllegalArgumentException(
					"Insufficient stock. Available: " + products.getAvailableQuantity());
		}

		double newQuantity = products.getAvailableQuantity() - purchaseRequest.getQuantity();
		products.setAvailableQuantity(newQuantity);

		BigDecimal quantityPrice = BigDecimal.valueOf(purchaseRequest.getQuantity());
		BigDecimal totalPrice = products.getPrice().multiply(quantityPrice);

		productRepository.save(products);
		LOGGER.info("Product id={} purchased. Remaining stock={}", products.getProductId(), newQuantity);

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
		LOGGER.info("Bulk purchase called for {} items", requests.size());

		var productIds = requests.stream()
				.map(ProductPurchaseRequest::getProductId)
				.toList();
		var orderedProducts = productRepository.findAllByProductIdInOrderByProductId(productIds);

		if (productIds.size() != orderedProducts.size()) {
			throw new ProductPurchaseException("One or more products do not exist");
		}

		var sortedRequests = requests.stream()
				.sorted(Comparator.comparing(ProductPurchaseRequest::getProductId))
				.toList();

		var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
		for (int i = 0; i < orderedProducts.size(); i++) {
			var product = orderedProducts.get(i);
			var productRequest = sortedRequests.get(i);

			if (product.getAvailableQuantity() < productRequest.getQuantity()) {
				throw new ProductPurchaseException(
						"Insufficient stock for product id " + product.getProductId());
			}

			var newAvailableQuantity = product.getAvailableQuantity() - productRequest.getQuantity();
			product.setAvailableQuantity(newAvailableQuantity);
			productRepository.save(product);
			purchasedProducts.add(toproductPurchaseResponse(product, productRequest.getQuantity()));
		}
		return purchasedProducts;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void releaseProduct(List<ProductPurchaseRequest> requests) {
		LOGGER.info("Release products back to inventory called");
		for (ProductPurchaseRequest purchaseRequest : requests) {
			Products products = productRepository.findById(purchaseRequest.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Product not found with id " + purchaseRequest.getProductId()));
			double newAvailableQuantity = products.getAvailableQuantity() + purchaseRequest.getQuantity();
			products.setAvailableQuantity(newAvailableQuantity);
			productRepository.save(products);
		}
	}

	// ── Image management ────────────────────────────────────────────────────────

	/**
	 * Adds an externally-hosted image URL to a product.
	 *
	 * Security: validates that the URL scheme is in ALLOWED_URL_SCHEMES
	 * (http/https only) to prevent SSRF via javascript:, file://, data:, etc.
	 * Further URL format validation is enforced via @URL on ImageUrlRequest.
	 */
	@Override
	public ProductResponse addImageToProduct(long productId, String imageUrl) {
		LOGGER.info("Add image URL to product id={}", productId);

		 storageService.validateImageUrl(imageUrl);

		Products products = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Product not found with id " + productId));

		ProductImage image = new ProductImage();
		image.setImageUrl(imageUrl);
		image.setProduct(products);

		// Null-safe initialisation — guards against legacy records with no image list
		if (products.getImages() == null) {
			products.setImages(new ArrayList<>());
		}
		products.getImages().add(image);
		productRepository.save(products);

		return mapToProductResponse(products);
	}

	/**
	 * Uploads multipart image files to storage and links them to a product.
	 *
	 * Security & reliability improvements over the original:
	 *  1. Enforces MAX_FILES_PER_UPLOAD to prevent unbounded batch abuse.
	 *  2. Validates each file's MIME type against ALLOWED_MIME_TYPES before
	 *     any upload attempt — rejects executables, scripts, etc.
	 *  3. Validates each file's size against MAX_FILE_SIZE_BYTES.
	 *  4. Performs all validation up-front before the storage upload loop so
	 *     that a bad file on position N does not leave orphaned files in
	 *     storage from positions 0..N-1.
	 *  5. Uses ResourceNotFoundException (consistent with the rest of the
	 *     service) instead of a raw RuntimeException.
	 *  6. Null-safe initialisation of the images list before addAll().
	 */
	@Override
	public ProductResponse uploadProductImages(List<MultipartFile> files, long productId) {
		LOGGER.info("Upload {} image file(s) for product id={}", files.size(), productId);

		// 1. Guard against unbounded file count
		if (files.size() > constantFile.MAX_FILES_PER_UPLOAD) {
			throw new IllegalArgumentException(
					"Too many files. Maximum allowed per request is " + constantFile.MAX_FILES_PER_UPLOAD);
		}

		// 2. Validate ALL files before touching storage — prevents partial uploads
		//    that would leave orphaned objects if a later file fails validation.
		for (MultipartFile file : files) {
			storageService.validateUploadedFile(file);
		}

		// 3. Look up the product after validation passes to avoid the round-trip
		//    on invalid input
		Products products = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Product not found with id " + productId));

		// 4. Null-safe images list initialisation
		if (products.getImages() == null) {
			products.setImages(new ArrayList<>());
		}

		// 5. Upload to storage and build image entities
		List<ProductImage> images = new ArrayList<>();
		for (MultipartFile file : files) {
			String imageUrl = storageService.uploadFile(file);
			ProductImage image = new ProductImage();
			image.setImageUrl(imageUrl);
			image.setProduct(products);
			images.add(image);
		}

		products.getImages().addAll(images);
		productRepository.save(products);

		return mapToProductResponse(products);
	}

//	// ── Validation helpers ──────────────────────────────────────────────────────
//
//	/**
//	 * Validates that a caller-supplied image URL uses an allowed scheme.
//	 * java.net.URL is used purely for scheme extraction — no network call is made.
//	 */
//	private void validateImageUrl(String imageUrl) {
//		try {
//			java.net.URL url = new java.net.URL(imageUrl);
//			String scheme = url.getProtocol();
//			if (!ALLOWED_URL_SCHEMES.contains(scheme)) {
//				throw new IllegalArgumentException(
//						"Image URL scheme '" + scheme + "' is not allowed. Use http or https.");
//			}
//		} catch (java.net.MalformedURLException e) {
//			throw new IllegalArgumentException("Invalid image URL format: " + imageUrl, e);
//		}
//	}
//
//	/**
//	 * Validates an uploaded MultipartFile for size and MIME type.
//	 * Both checks run on every file before any storage upload occurs.
//	 */
//	private void validateUploadedFile(MultipartFile file) {
//		if (file.isEmpty()) {
//			throw new IllegalArgumentException("Uploaded file must not be empty");
//		}
//		if (file.getSize() > MAX_FILE_SIZE_BYTES) {
//			throw new IllegalArgumentException(
//					"File '" + file.getOriginalFilename() + "' exceeds maximum allowed size of "
//							+ (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + " MB");
//		}
//		String contentType = file.getContentType();
//		if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
//			throw new IllegalArgumentException(
//					"File type '" + contentType + "' is not allowed. Permitted types: " + ALLOWED_MIME_TYPES);
//		}
//	}

	// ── Mappers ─────────────────────────────────────────────────────────────────

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
		BigDecimal quantityPrice = BigDecimal.valueOf(quantity);
		BigDecimal totalPrice = products.getPrice().multiply(quantityPrice);

		return ProductPurchaseResponse.builder()
				.productName(products.getName())
				.quantityPurchased(quantity)
				.totalPrice(totalPrice)
				.remainingQuantity(products.getAvailableQuantity())
				.message("Purchase successful")
				.build();
	}
}