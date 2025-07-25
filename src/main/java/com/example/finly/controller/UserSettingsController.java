package com.example.finly.controller;

import com.example.finly.service.UserSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/users/settings")
public class UserSettingsController {

    // LOGGER
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private final UserSettingsService userSettingsService;

    // Dependency injection
    @Autowired
    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    // PUT request to change the name
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(@RequestBody Map<String, String> data) {
        try {
            String username = data.get("username");
            String userID = data.get("email");
            userSettingsService.updateUsername(userID, username);
            return ResponseEntity.ok("Successfully changed");
        }
        catch(Exception e)  {
            logger.info("Error with saving into the firebase: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
