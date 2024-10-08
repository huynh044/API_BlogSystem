package com.apimobilestore.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdate {
	@Size(min = 8, message = "INVALID_PASSWORD") // Tối thiểu 8 ký tự
	String password;
	String firstname;
	String lastname;
}
