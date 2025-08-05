package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.example.finly.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SignupService {

    private static final Logger logger = LoggerFactory.getLogger(SignupService.class);

    @Autowired
    private final HashString hashString;
    
    @Autowired
    private final FirestoreAccountService firestoreAccountService;

    // Dependency Injection
    public SignupService(HashString hashString, FirestoreAccountService firestoreAccountService) {
        this.hashString = hashString;
        this.firestoreAccountService = firestoreAccountService;
    }

    // Saving user into firestore
    public String saveUser(Map<String, Object> data) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String email = (String) data.get("email");
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        String plainPassword = (String) data.get("password");
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        String hashedPassword = hashString.hashPassword(plainPassword);
        data.put("password", hashedPassword);

        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        data.put("createdAt", FieldValue.serverTimestamp());

        DocumentReference ref = usersRef.document(email); // The name of the document in firestore is set to the email
        ref.set(data).get();
        
        // Create 3 default accounts for the new user
        // createDefaultAccounts(email); // Commented out to not create default accounts
        
        return ref.getId();
    }

    /**
     * Creates 3 default accounts for a new user
     * @param userEmail The user's email
     */
    private void createDefaultAccounts(String userEmail) {
        try {
            logger.info("Creating default accounts for user: {}", userEmail);
            
            // Default account 1: Main Account
            Account mainAccount = new Account();
            mainAccount.setName("Main Account");
            mainAccount.setBalance(BigDecimal.ZERO);
            mainAccount.setUsername(userEmail);
            Account savedMainAccount = firestoreAccountService.saveAccount(userEmail, mainAccount);
            logger.info("Created Main Account for user: {} with ID: {}", userEmail, savedMainAccount.getId());

            // Default account 2: Savings Account
            Account savingsAccount = new Account();
            savingsAccount.setName("Savings Account");
            savingsAccount.setBalance(BigDecimal.ZERO);
            savingsAccount.setUsername(userEmail);
            Account savedSavingsAccount = firestoreAccountService.saveAccount(userEmail, savingsAccount);
            logger.info("Created Savings Account for user: {} with ID: {}", userEmail, savedSavingsAccount.getId());

            // Default account 3: Emergency Fund
            Account emergencyAccount = new Account();
            emergencyAccount.setName("Emergency Fund");
            emergencyAccount.setBalance(BigDecimal.ZERO);
            emergencyAccount.setUsername(userEmail);
            Account savedEmergencyAccount = firestoreAccountService.saveAccount(userEmail, emergencyAccount);
            logger.info("Created Emergency Fund for user: {} with ID: {}", userEmail, savedEmergencyAccount.getId());
            
            logger.info("Successfully created all default accounts for user: {}", userEmail);
            
        } catch (Exception e) {
            // Log the error but don't fail the signup process
            logger.error("Failed to create default accounts for user: {}. Error: {}", userEmail, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    // Returns user based on provided id
    public Map<String, Object> getUser(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection("users").document(id).get().get();
        return document.exists() ? document.getData() : null;
    }
}
