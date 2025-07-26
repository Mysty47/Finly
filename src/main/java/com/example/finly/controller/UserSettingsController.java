package com.example.finly.controller;

import com.example.finly.dto.PasswordUpdateDTO;
import com.example.finly.dto.UsernameUpdateDTO;
import com.example.finly.service.UserSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("api/users/settings")
public class UserSettingsController {

    @Autowired
    private final UserSettingsService userSettingsService;

    // Dependency injection
    @Autowired
    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    // PUT request to change the name
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody UsernameUpdateDTO usernameUpdateDTO) {
        try {
            String username = usernameUpdateDTO.getUsername();
            String userID = usernameUpdateDTO.getEmail();


            userSettingsService.updateUsername(userID, username);
            return ResponseEntity.ok("Successfully changed");
        }
        catch(Exception e)  {
            log.info("Error with saving into the firebase: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // PUT request to change the password
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        try {
            String password = passwordUpdateDTO.getPassword();
            String userID = passwordUpdateDTO.getEmail();
            userSettingsService.updatePassword(userID, password);
            return ResponseEntity.ok("Successfully changed");
        }
        catch(Exception e)  {
            log.info("Error with saving into the firebase: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
