package com.product.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.constant.ConstantFile;
import com.product.services.StorageService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class S3StorageServiceImpl implements StorageService {
	
	private ConstantFile constantFile;

    @Value("${storage.s3.bucket-name}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Override
    public String uploadFile(MultipartFile file) {
        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            // Ensure the file's input stream is correctly passed to RequestBody
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

        } catch (S3Exception | java.io.IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

	@Override
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

	@Override
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