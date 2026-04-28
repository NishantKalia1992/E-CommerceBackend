package com.product.services.impl;

import com.product.constant.ConstantFile;
import com.product.services.StorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class LocalStorageServiceImpl implements StorageService {
	
	
	private ConstantFile constantFile;

    @Value("${storage.local.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            Path destination = uploadPath.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/" + uploadDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file locally", e);
        }
    }
 // ── Validation helpers ──────────────────────────────────────────────────────

 	/**
 	 * Validates that a caller-supplied image URL uses an allowed scheme.
 	 * java.net.URL is used purely for scheme extraction — no network call is made.
 	 */
    
 	public void validateImageUrl(String imageUrl) {
 		try {
 			java.net.URL url = new java.net.URL(imageUrl);
 			String scheme = url.getProtocol();
 			if (!constantFile.ALLOWED_MIME_TYPES.contains(scheme)) {
 				throw new IllegalArgumentException(
 						"Image URL scheme '" + scheme + "' is not allowed. Use http or https.");
 			}
 		} catch (java.net.MalformedURLException e) {
 			throw new IllegalArgumentException("Invalid image URL format: " + imageUrl, e);
 		}
 	}

 	/**
 	 * Validates an uploaded MultipartFile for size and MIME type.
 	 * Both checks run on every file before any storage upload occurs.
 	 */
 	public void validateUploadedFile(MultipartFile file) {
 		if (file.isEmpty()) {
 			throw new IllegalArgumentException("Uploaded file must not be empty");
 		}
 		if (file.getSize() > constantFile.MAX_FILE_SIZE_BYTES) {
 			throw new IllegalArgumentException(
 					"File '" + file.getOriginalFilename() + "' exceeds maximum allowed size of "
 							+ (constantFile.MAX_FILE_SIZE_BYTES / (1024 * 1024)) + " MB");
 		}
 		String contentType = file.getContentType();
 		if (contentType == null || !constantFile.ALLOWED_MIME_TYPES.contains(contentType)) {
 			throw new IllegalArgumentException(
 					"File type '" + contentType + "' is not allowed. Permitted types: " + constantFile.ALLOWED_MIME_TYPES);
 		}
 	}
}