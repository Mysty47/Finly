package com.example.finly.service;

import com.example.finly.dto.SignupRequestDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignupService {
    public String saveUser(SignupRequestDTO user) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("email", user.getEmail());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("password", user.getPassword()); // will be crypted soon!
        data.put("createdAt", FieldValue.serverTimestamp());

        DocumentReference ref = db.collection("users").add(data).get();
        return ref.getId();
    }
}
