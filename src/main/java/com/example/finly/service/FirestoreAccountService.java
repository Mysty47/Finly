package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.example.finly.entity.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirestoreAccountService {

    private final Firestore firestore = FirestoreClient.getFirestore();

    /**
     * Save a new account as a sub-collection under a user
     * @param userEmail The user's email (used as user ID)
     * @param account The account to save
     * @return The saved account with generated ID
     */
    public Account saveAccount(String userEmail, Account account) {
        try {
            // Create a document reference for the account
            DocumentReference accountRef = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document();

            // Set the account ID to the document ID before saving
            String documentId = accountRef.getId();
            // Use a hash of the document ID as the numeric ID since Firestore IDs are strings
            account.setId((long) documentId.hashCode());

            // Save the account to Firestore
            ApiFuture<WriteResult> future = accountRef.set(account);
            future.get(); // Wait for the write to complete

            return account;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save account to Firestore", e);
        }
    }

    /**
     * Get all accounts for a user
     * @param userEmail The user's email
     * @return List of accounts
     */
    public List<Account> getAccountsByUser(String userEmail) {
        try {
            ApiFuture<QuerySnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.getDocuments().stream()
                    .map(document -> {
                        Account account = document.toObject(Account.class);
                        if (account != null) {
                            account.setId((long) document.getId().hashCode());
                        }
                        return account;
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get accounts from Firestore", e);
        }
    }

    /**
     * Get a specific account by ID
     * @param userEmail The user's email
     * @param accountId The account ID
     * @return The account or null if not found
     */
    public Account getAccountById(String userEmail, String accountId) {
        try {
            ApiFuture<DocumentSnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountId)
                    .get();

            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Account account = document.toObject(Account.class);
                if (account != null) {
                    account.setId((long) document.getId().hashCode());
                }
                return account;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get account from Firestore", e);
        }
    }

    /**
     * Update an existing account
     * @param userEmail The user's email
     * @param account The account to update
     * @return The updated account
     */
    public Account updateAccount(String userEmail, Account account) {
        try {
            DocumentReference accountRef = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(account.getId().toString());

            ApiFuture<WriteResult> future = accountRef.set(account);
            future.get();

            return account;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update account in Firestore", e);
        }
    }

    /**
     * Update account balance after transaction
     * @param userEmail The user's email
     * @param accountDocumentId The account document ID (Firestore document ID)
     * @param amount The transaction amount (positive for income, negative for expense)
     */
    public void updateAccountBalance(String userEmail, String accountDocumentId, BigDecimal amount) {
        try {
            System.out.println("=== DEBUG: Updating balance for account document ID: " + accountDocumentId);
            
            // Get the account directly by document ID
            ApiFuture<DocumentSnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .get();

            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Account account = document.toObject(Account.class);
                if (account != null) {
                    BigDecimal newBalance = account.getBalance().add(amount);
                    account.setBalance(newBalance);
                    
                    System.out.println("=== DEBUG: Updating account balance from " + account.getBalance().subtract(amount) + " to " + newBalance);
                    
                    // Update the account directly
                    ApiFuture<WriteResult> updateFuture = firestore
                            .collection("users")
                            .document(userEmail)
                            .collection("accounts")
                            .document(accountDocumentId)
                            .set(account);
                    
                    updateFuture.get();
                    System.out.println("=== DEBUG: Successfully updated account balance");
                } else {
                    System.out.println("=== DEBUG: Account object is null");
                }
            } else {
                System.out.println("=== DEBUG: Account document not found: " + accountDocumentId);
            }
        } catch (Exception e) {
            System.out.println("=== DEBUG: Error updating account balance: " + e.getMessage());
            throw new RuntimeException("Failed to update account balance", e);
        }
    }

    /**
     * Delete an account
     * @param userEmail The user's email
     * @param accountId The account ID to delete
     */
    public void deleteAccount(String userEmail, String accountId) {
        try {
            ApiFuture<WriteResult> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountId)
                    .delete();

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete account from Firestore", e);
        }
    }
} 