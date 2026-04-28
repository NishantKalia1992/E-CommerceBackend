package com.product.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Uploads a file and returns its URL.
     *
     * @param file the file to upload
     * @return the URL of the uploaded file
     */
    String uploadFile(MultipartFile file);
    public void validateImageUrl(String imageUrl);
    public void validateUploadedFile(MultipartFile file);
}