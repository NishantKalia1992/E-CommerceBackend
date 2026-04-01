package com.payment.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record Customer(
		String id,
		@NotNull(message = "first name must be required")
		String firstName,
		@NotNull(message = "last name must be required")
		String lastName,
		@NotNull(message = "Email cannot be empty")
		@Email(message = "email must be proper formated")
		String email) {

}
