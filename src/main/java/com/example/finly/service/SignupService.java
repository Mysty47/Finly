package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SignupService {

    // Saving user into firestore
    public String saveUser(Map<String, Object> data) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String email = (String) data.get("email");
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        data.put("createdAt", FieldValue.serverTimestamp());

        DocumentReference ref = usersRef.add(data).get();
        return ref.getId();
    }

    // Returns user based on provided id
    public Map<String, Object> getUser(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection("users").document(id).get().get();
        return document.exists() ? document.getData() : null;
    }
}
