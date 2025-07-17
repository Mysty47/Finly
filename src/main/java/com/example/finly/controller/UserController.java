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

    @PostMapping("/{id}")
    public ResponseEntity<?> saveUser(@PathVariable String id, @RequestBody Map<String, Object> data) {
        try {
            System.out.println("🔄 Започваме запис за потребител: " + id);
            String updateTime = firebaseService.saveUser(id, data);
            return ResponseEntity.ok(updateTime);
        } catch (Exception e) {
            System.err.println("❌ Грешка при запис в Firebase: " + e.getMessage());
            e.printStackTrace(); // показва пълния stacktrace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable String id) throws Exception {
        return firebaseService.getUser(id);
    }
}
