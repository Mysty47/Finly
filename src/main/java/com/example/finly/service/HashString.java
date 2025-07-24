package com.example.finly.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class HashString {

    // BCrypt Method
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Hash a String
    public String HashPasswordOnSignup(String password) {
        String hashedPassword = hashPassword(password);
        return hashedPassword;
    }

    // Checking if String is equal to the hashed one
    public boolean CheckHashedString(String plainText, String hashedText) {
        return BCrypt.checkpw(plainText, hashedText);
    }
}
