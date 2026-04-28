package com.product.constant;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ConstantFile {
	/**
	 * Maximum number of files accepted per upload request.
	 * Prevents abuse via unbounded List<MultipartFile> in the controller.
	 */
//	private static final int MAX_FILES_PER_UPLOAD = 10;

	/**
	 * Maximum allowed file size per upload in bytes (5 MB).
	 */
	public static final int MAX_FILES_PER_UPLOAD = 10;
	public static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024;

	/**
	 * Whitelist of permitted MIME types for product images.
	 * Only explicit matches are allowed — no wildcard or prefix matching.
	 */
	public static final Set<String> ALLOWED_MIME_TYPES = Set.of(
			"image/jpeg",
			"image/png",
			"image/webp",
			"image/gif"
	);
	

	/**
	 * Whitelist of allowed URL schemes for externally-supplied image URLs.
	 * Blocks javascript:, file://, data:, and other dangerous protocols.
	 */
	public static final Set<String> ALLOWED_URL_SCHEMES = Set.of("http", "https");
}
