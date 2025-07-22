package com.example.finly.controller;

import com.example.finly.dto.LoginDTO;
import com.example.finly.service.LoginService;
import com.example.finly.service.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            boolean success = loginService.login(loginDTO.getEmail(), loginDTO.getPassword());

            if(success) {
                return ResponseEntity.ok("Login Successfull");
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}
