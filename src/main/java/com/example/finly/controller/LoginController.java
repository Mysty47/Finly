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

    // Dependency Injection
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // POST Request for comparing provided data to the one in firestore
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            String username = loginService.login(loginDTO.getEmail(), loginDTO.getPassword());

            if(username != null) {
                return ResponseEntity.ok(username);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}
