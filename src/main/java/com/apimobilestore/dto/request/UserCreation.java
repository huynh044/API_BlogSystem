package com.apimobilestore.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreation {
    String firstname;
    String lastname;
    
    @Size(min = 5, message = "USERNAME_INVALID") // Tối thiểu 5 ký tự
    String username;
    
    @Size(min = 8, message = "INVALID_PASSWORD") // Tối thiểu 8 ký tự
    String password;
}

