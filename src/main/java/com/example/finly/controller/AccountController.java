package com.example.finly.controller;

import com.example.finly.entity.Account;
import com.example.finly.dto.CreateAccountDTO;
import com.example.finly.service.FirestoreAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private FirestoreAccountService accountService;

    /**
     * Create a new account for a user
     */
    @PostMapping("/{userEmail}")
    public ResponseEntity<Account> createAccount(@PathVariable String userEmail, @RequestBody CreateAccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setName(accountDTO.getName());
            account.setBalance(accountDTO.getBalance());
            account.setUsername(accountDTO.getUsername());
            
            Account savedAccount = accountService.saveAccount(userEmail, account);
            return ResponseEntity.ok(savedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all accounts for a user
     */
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable String userEmail) {
        try {
            System.out.println("Fetching accounts for user: " + userEmail);
            List<Account> accounts = accountService.getAccountsByUser(userEmail);
            System.out.println("Found " + accounts.size() + " accounts");
            for (Account account : accounts) {
                System.out.println("Account: " + account.getName() + ", Balance: " + account.getBalance() + ", ID: " + account.getId());
            }
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get a specific account by ID
     */
    @GetMapping("/{userEmail}/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable String userEmail, @PathVariable String accountId) {
        try {
            Account account = accountService.getAccountById(userEmail, accountId);
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing account
     */
    @PutMapping("/{userEmail}/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable String userEmail, 
                                               @PathVariable String accountId, 
                                               @RequestBody Account account) {
        try {
            account.setId(Long.parseLong(accountId));
            Account updatedAccount = accountService.updateAccount(userEmail, account);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete an account
     */
    @DeleteMapping("/{userEmail}/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String userEmail, @PathVariable String accountId) {
        try {
            accountService.deleteAccount(userEmail, accountId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 