package com.product.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.URL;

/**
 * DTO for the add-image-by-URL endpoint.
 *
 * Replaces the raw @RequestBody String that previously accepted any arbitrary
 * string. Using a typed DTO here allows Bean Validation annotations to reject
 * malformed or missing URLs before the request even reaches the service layer.
 *
 * @URL (Hibernate Validator) enforces syntactic URL correctness.
 * The service layer additionally enforces allowed schemes (http/https only)
 * to block javascript:, file://, data:, and similar dangerous protocols.
 */
@Getter
@Setter
public class ImageUrlRequest {

    @NotBlank(message = "Image URL must not be blank")
    @URL(message = "imageUrl must be a valid URL")
    @Size(max = 2048, message = "Image URL must not exceed 2048 characters")
    private String imageUrl;
}