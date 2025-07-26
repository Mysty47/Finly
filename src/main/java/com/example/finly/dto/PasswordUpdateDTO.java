package com.example.finly.dto;

import lombok.Getter;
import lombok.Setter;

// DTO for updating password
@Getter
@Setter
public class PasswordUpdateDTO {
    private String password;
    private String email;
}
