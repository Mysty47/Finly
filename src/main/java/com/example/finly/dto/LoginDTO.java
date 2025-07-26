package com.example.finly.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// DTO for login compare
@Setter
@Getter
public class LoginDTO {
    private String username;

    @Email(message = "The email is not valid")
    @NotBlank(message = "Fill the email field")
    private String email;

    @NotBlank(message = "Fill the password field")
    private String password;
}
