package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public boolean login(String email, String password) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection("users");

        ApiFuture<QuerySnapshot> query = users.whereEqualTo("email", email).get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        if(documents.isEmpty()) {
            return false;
        }

        DocumentSnapshot userDoc = documents.get(0);
        String storedPassword = userDoc.getString("password");

        return storedPassword != null && storedPassword.equals(password);
    }
}
