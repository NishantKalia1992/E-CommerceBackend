package com.payment.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
	private long id;
	@NotNull(message = "Full name is required")
	private String fullName;
	@NotNull(message = "username must required")
	private String username;
}
