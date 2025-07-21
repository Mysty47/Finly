package com.example.finly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//DTO for user's info
public class SignupRequestDTO {
    private String username;
    private String email;
    private String password;
}
