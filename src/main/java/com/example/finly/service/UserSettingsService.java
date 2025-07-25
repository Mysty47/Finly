package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserSettingsService {

    // Update username
    public void updateUsername(String userID, String newUsername) {
        updateField(userID, "username", newUsername);
    }

    // Changing the value of the field in the firestore
    private void updateField(String userId, String field, String newValue) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put(field, newValue);

        try {
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            System.out.println("Updated " + field + " at: " + writeResult.get().getUpdateTime());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update " + field, e);
        }
    }
}
