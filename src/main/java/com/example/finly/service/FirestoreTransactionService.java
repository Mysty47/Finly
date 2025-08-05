package com.example.finly.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.example.finly.entity.Transaction;
import com.example.finly.service.FirestoreAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.example.finly.entity.Account;

@Service
public class FirestoreTransactionService {

    private final Firestore firestore = FirestoreClient.getFirestore();
    
    @Autowired
    private FirestoreAccountService accountService;

    /**
     * Save a new transaction as a sub-collection under an account
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @param transaction The transaction to save
     * @return The saved transaction with generated ID
     */
    public Transaction saveTransaction(String userEmail, String accountId, Transaction transaction) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                throw new RuntimeException("Account not found with ID: " + accountId);
            }

            // Create a document reference for the transaction
            DocumentReference transactionRef = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .document();

            // Set the transaction ID to the document ID before saving
            String documentId = transactionRef.getId();
            transaction.setId((long) documentId.hashCode());

            // Save the transaction to Firestore
            ApiFuture<WriteResult> future = transactionRef.set(transaction);
            future.get(); // Wait for the write to complete

            // Update account balance
            BigDecimal amount = transaction.getAmount();
            if (transaction.getType().equals("expense")) {
                amount = amount.negate(); // Make expense negative
            }
            accountService.updateAccountBalance(userEmail, accountDocumentId, amount);

            return transaction;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save transaction to Firestore", e);
        }
    }

    /**
     * Find account document ID by numeric ID
     * @param userEmail The user's email
     * @param numericId The numeric account ID
     * @return The Firestore document ID for the account
     */
    private String findAccountDocumentIdByNumericId(String userEmail, String numericId) {
        try {
            System.out.println("=== DEBUG: Looking for account with numeric ID: " + numericId + " for user: " + userEmail);
            
            ApiFuture<QuerySnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .get();

            QuerySnapshot querySnapshot = future.get();
            System.out.println("=== DEBUG: Found " + querySnapshot.getDocuments().size() + " accounts for user");
            
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Account account = document.toObject(Account.class);
                if (account != null) {
                    System.out.println("=== DEBUG: Checking account: " + account.getName() + 
                                     " with ID: " + account.getId() + 
                                     " (document ID: " + document.getId() + ")");
                    System.out.println("=== DEBUG: Comparing " + account.getId().toString() + " with " + numericId);
                    System.out.println("=== DEBUG: Are they equal? " + account.getId().toString().equals(numericId));
                    
                    if (account.getId().toString().equals(numericId)) {
                        System.out.println("=== DEBUG: FOUND MATCHING ACCOUNT! Document ID: " + document.getId());
                        return document.getId();
                    }
                } else {
                    System.out.println("=== DEBUG: Account object is null for document: " + document.getId());
                }
            }
            System.out.println("=== DEBUG: No matching account found for numeric ID: " + numericId);
            return null;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("=== DEBUG: Error in findAccountDocumentIdByNumericId: " + e.getMessage());
            throw new RuntimeException("Failed to find account document ID", e);
        }
    }

    /**
     * Get all transactions for a specific account
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @return List of transactions
     */
    public List<Transaction> getTransactionsByAccount(String userEmail, String accountId) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                return List.of(); // Return empty list if account not found
            }

            ApiFuture<QuerySnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.getDocuments().stream()
                    .map(document -> {
                        Transaction transaction = document.toObject(Transaction.class);
                        if (transaction != null) {
                            transaction.setId((long) document.getId().hashCode());
                        }
                        return transaction;
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get transactions from Firestore", e);
        }
    }

    /**
     * Get all transactions for a user across all accounts
     * @param userEmail The user's email
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactionsByUser(String userEmail) {
        try {
            // First get all accounts for the user
            ApiFuture<QuerySnapshot> accountsFuture = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .get();

            QuerySnapshot accountsSnapshot = accountsFuture.get();
            
            return accountsSnapshot.getDocuments().stream()
                    .flatMap(accountDoc -> {
                        try {
                            ApiFuture<QuerySnapshot> transactionsFuture = firestore
                                    .collection("users")
                                    .document(userEmail)
                                    .collection("accounts")
                                    .document(accountDoc.getId())
                                    .collection("transactions")
                                    .get();

                            QuerySnapshot transactionsSnapshot = transactionsFuture.get();
                            return transactionsSnapshot.getDocuments().stream()
                                    .map(transactionDoc -> {
                                        Transaction transaction = transactionDoc.toObject(Transaction.class);
                                        if (transaction != null) {
                                            transaction.setId((long) transactionDoc.getId().hashCode());
                                        }
                                        return transaction;
                                    });
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException("Failed to get transactions for account", e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all transactions from Firestore", e);
        }
    }

    /**
     * Get a specific transaction by ID
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @param transactionId The transaction ID
     * @return The transaction or null if not found
     */
    public Transaction getTransactionById(String userEmail, String accountId, String transactionId) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                return null;
            }

            ApiFuture<DocumentSnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .document(transactionId)
                    .get();

            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Transaction transaction = document.toObject(Transaction.class);
                if (transaction != null) {
                    transaction.setId((long) document.getId().hashCode());
                }
                return transaction;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get transaction from Firestore", e);
        }
    }

    /**
     * Update an existing transaction
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @param transaction The transaction to update
     * @return The updated transaction
     */
    public Transaction updateTransaction(String userEmail, String accountId, Transaction transaction) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                throw new RuntimeException("Account not found with ID: " + accountId);
            }

            DocumentReference transactionRef = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .document(transaction.getId().toString());

            ApiFuture<WriteResult> future = transactionRef.set(transaction);
            future.get();

            return transaction;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update transaction in Firestore", e);
        }
    }

    /**
     * Delete a transaction
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @param transactionId The transaction ID to delete
     */
    public void deleteTransaction(String userEmail, String accountId, String transactionId) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                throw new RuntimeException("Account not found with ID: " + accountId);
            }

            ApiFuture<WriteResult> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .document(transactionId)
                    .delete();

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete transaction from Firestore", e);
        }
    }

    /**
     * Get transactions by type (expense/income) for a specific account
     * @param userEmail The user's email
     * @param accountId The account ID (numeric ID from frontend)
     * @param type The transaction type ("expense" or "income")
     * @return List of transactions of the specified type
     */
    public List<Transaction> getTransactionsByType(String userEmail, String accountId, String type) {
        try {
            // First, find the account document by its numeric ID
            String accountDocumentId = findAccountDocumentIdByNumericId(userEmail, accountId);
            if (accountDocumentId == null) {
                return List.of(); // Return empty list if account not found
            }

            ApiFuture<QuerySnapshot> future = firestore
                    .collection("users")
                    .document(userEmail)
                    .collection("accounts")
                    .document(accountDocumentId)
                    .collection("transactions")
                    .whereEqualTo("type", type)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.getDocuments().stream()
                    .map(document -> {
                        Transaction transaction = document.toObject(Transaction.class);
                        if (transaction != null) {
                            transaction.setId((long) document.getId().hashCode());
                        }
                        return transaction;
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get transactions by type from Firestore", e);
        }
    }
} 