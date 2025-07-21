package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    //Saving user into firestore
    public String saveUser(String id, Map<String, Object> data) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> result = db.collection("users").document(id).set(data);
        return result.get().getUpdateTime().toString();
    }

    //Getting user when given id
    public Map<String, Object> getUser(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection("users").document(id).get().get();
        return document.exists() ? document.getData() : null;
    }
}
