package com.example.finly.controller;

import com.example.finly.service.SignupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class SignupController {

    @Autowired
    private final SignupService signupService;

    // Dependency Injection
    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    //POST request for saving a user's info into firestore
    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@Valid @RequestBody Map<String, Object> data) {
        try {
            log.info("Starting to save the user");
            String updateTime = signupService.saveUser(data);
            return ResponseEntity.ok(updateTime);
        } catch (Exception e) {
            log.info("Error with saving into the firebase: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // GET request for searching user by id
    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable String id) throws Exception {
        return signupService.getUser(id);
    }
}
