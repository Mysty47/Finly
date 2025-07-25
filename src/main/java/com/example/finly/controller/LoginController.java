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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class LoginController {

    // LOGGER
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final LoginService loginService;

    // Dependency Injection
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // POST Request for comparing provided data to the one in firestore
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String username = loginService.login(loginDTO.getEmail(), loginDTO.getPassword());

            if(username != null) {
                Map<String, String> response = new HashMap<>();
                response.put("username", username);
                response.put("email", loginDTO.getEmail());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}
