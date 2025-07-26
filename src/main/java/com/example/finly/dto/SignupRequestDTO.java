package com.example.finly.dto;


import lombok.Getter;
import lombok.Setter;

//DTO for user's info
@Getter
@Setter
public class SignupRequestDTO {
    private String username;
    private String email;
    private String password;
}
