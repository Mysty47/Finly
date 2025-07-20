package com.example.finly.controller;

import com.example.finly.dto.SignupRequestDTO;
import com.example.finly.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Разреши достъп от React
@RestController
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService userService) {
        this.signupService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            String id = signupService.saveUser(signupRequestDTO);
            return ResponseEntity.ok("User saved with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed.");
        }
    }
}
