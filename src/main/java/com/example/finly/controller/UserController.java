package com.example.finly.controller;

import com.example.finly.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private FirebaseService firebaseService;

    //POST request for saving a user's info into firestore
    @PostMapping("/{id}")
    public ResponseEntity<?> saveUser(@PathVariable String id, @RequestBody Map<String, Object> data) {
        try {
            System.out.println("Starting the save of the user: " + id);
            String updateTime = firebaseService.saveUser(id, data);
            return ResponseEntity.ok(updateTime);
        } catch (Exception e) {
            System.err.println("Error with saving into the firebase: " + e.getMessage());
            e.printStackTrace(); // показва пълния stacktrace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // GET request for searching user by id
    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable String id) throws Exception {
        return firebaseService.getUser(id);
    }
}
