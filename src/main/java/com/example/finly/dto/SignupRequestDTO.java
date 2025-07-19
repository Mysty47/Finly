package com.example.finly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    private String username;
    private String email;
    private String password;
}
