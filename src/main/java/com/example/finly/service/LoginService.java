package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class LoginService {

    @Autowired
    private final HashString hashString;

    // Dependency Injection
    public LoginService(HashString hashString) {
        this.hashString = hashString;
    }

    // Compare the users credentials to the provided one
    public String login(String email, String password) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection("users");

        ApiFuture<QuerySnapshot> query = users.whereEqualTo("email", email).get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        if(documents.isEmpty()) {
            return null;
        }

        DocumentSnapshot userDoc = documents.get(0);
        String storedPassword = userDoc.getString("password");

        if(storedPassword != null && hashString.CheckHashedString(password, storedPassword)) {
            return userDoc.getString("username");
        } else {
            return null;
        }
    }
}
