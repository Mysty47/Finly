package com.example.finly.controller;

import com.example.finly.entity.Transaction;
import com.example.finly.dto.CreateTransactionDTO;
import com.example.finly.service.FirestoreTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private FirestoreTransactionService transactionService;

    /**
     * Create a new transaction for an account
     */
    @PostMapping("/{userEmail}/{accountId}")
    public ResponseEntity<Transaction> createTransaction(@PathVariable String userEmail, 
                                                      @PathVariable String accountId, 
                                                      @RequestBody CreateTransactionDTO transactionDTO) {
        try {
            System.out.println("Creating transaction for user: " + userEmail + ", account: " + accountId);
            System.out.println("Transaction data: " + transactionDTO);
            
            Transaction transaction = new Transaction();
            transaction.setDescription(transactionDTO.getDescription());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDate(transactionDTO.getDate());
            transaction.setType(transactionDTO.getType());
            
            System.out.println("Created transaction object: " + transaction);
            
            Transaction savedTransaction = transactionService.saveTransaction(userEmail, accountId, transaction);
            System.out.println("Saved transaction: " + savedTransaction);
            
            return ResponseEntity.ok(savedTransaction);
        } catch (Exception e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all transactions for a specific account
     */
    @GetMapping("/{userEmail}/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable String userEmail, 
                                                                    @PathVariable String accountId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccount(userEmail, accountId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all transactions for a user across all accounts
     */
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByUser(@PathVariable String userEmail) {
        try {
            List<Transaction> transactions = transactionService.getAllTransactionsByUser(userEmail);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get a specific transaction by ID
     */
    @GetMapping("/{userEmail}/{accountId}/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String userEmail, 
                                                        @PathVariable String accountId, 
                                                        @PathVariable String transactionId) {
        try {
            Transaction transaction = transactionService.getTransactionById(userEmail, accountId, transactionId);
            if (transaction != null) {
                return ResponseEntity.ok(transaction);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing transaction
     */
    @PutMapping("/{userEmail}/{accountId}/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String userEmail, 
                                                       @PathVariable String accountId, 
                                                       @PathVariable String transactionId, 
                                                       @RequestBody Transaction transaction) {
        try {
            transaction.setId(Long.parseLong(transactionId));
            Transaction updatedTransaction = transactionService.updateTransaction(userEmail, accountId, transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a transaction
     */
    @DeleteMapping("/{userEmail}/{accountId}/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String userEmail, 
                                                @PathVariable String accountId, 
                                                @PathVariable String transactionId) {
        try {
            transactionService.deleteTransaction(userEmail, accountId, transactionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get transactions by type (expense/income) for a specific account
     */
    @GetMapping("/{userEmail}/{accountId}/type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable String userEmail, 
                                                                 @PathVariable String accountId, 
                                                                 @PathVariable String type) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByType(userEmail, accountId, type);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 