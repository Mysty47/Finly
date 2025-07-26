package com.example.finly.service;

import com.example.finly.controller.SignupController;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserSettingsService {

    private final HashString hashString;

    @Autowired
    public UserSettingsService(HashString hashString) {
        this.hashString = hashString;
    }

    // Update username
    public void updateUsername(String userID, String newUsername) {
        updateUsernameField(userID, "username", newUsername);
    }

    // Update password
    public void updatePassword(String userID, String newPassword) {
        updatePasswordField(userID, "password", newPassword);
    }

    // Changing the value of the email in the firestore
    private void updateUsernameField(String userId, String field, String newValue) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put(field, newValue);

        try {
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            log.info("Updated " + field + " at: " + writeResult.get().getUpdateTime());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update " + field, e);
        }
    }

    // Changing the value of the password field with the new hashed one
    private void updatePasswordField(String userID, String field, String newValue) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(userID);

        Map<String, Object> updates = new HashMap<>();
        String newHashedPassword = hashString.hashPassword(newValue);
        updates.put(field, newHashedPassword);

        try {
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            log.info("Updated " + field + " at: " + writeResult.get().getUpdateTime());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update " + field, e);
        }
    }
}
