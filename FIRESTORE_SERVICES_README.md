# Firestore Services Documentation

## Overview

This project includes Firestore services for managing accounts and transactions as sub-collections in Firestore. The structure follows this hierarchy:

```
users (collection)
  └── {userEmail} (document)
      └── accounts (sub-collection)
          └── {accountId} (document)
              └── transactions (sub-collection)
                  └── {transactionId} (document)
```

## Services Created

### 1. FirestoreAccountService
Handles CRUD operations for accounts as sub-collections under users.

**Methods:**
- `saveAccount(String userEmail, Account account)` - Create new account
- `getAccountsByUser(String userEmail)` - Get all accounts for a user
- `getAccountById(String userEmail, String accountId)` - Get specific account
- `updateAccount(String userEmail, Account account)` - Update existing account
- `deleteAccount(String userEmail, String accountId)` - Delete account

### 2. FirestoreTransactionService
Handles CRUD operations for transactions as sub-collections under accounts.

**Methods:**
- `saveTransaction(String userEmail, String accountId, Transaction transaction)` - Create new transaction
- `getTransactionsByAccount(String userEmail, String accountId)` - Get all transactions for an account
- `getAllTransactionsByUser(String userEmail)` - Get all transactions for a user across all accounts
- `getTransactionById(String userEmail, String accountId, String transactionId)` - Get specific transaction
- `updateTransaction(String userEmail, String accountId, Transaction transaction)` - Update existing transaction
- `deleteTransaction(String userEmail, String accountId, String transactionId)` - Delete transaction
- `getTransactionsByType(String userEmail, String accountId, String type)` - Get transactions by type (expense/income)

## REST API Endpoints

### Account Endpoints

**Create Account:**
```
POST /api/accounts/{userEmail}
Content-Type: application/json

{
  "name": "Main Bank Account",
  "balance": 1000.00,
  "username": "john.doe@example.com"
}
```

**Get All Accounts for User:**
```
GET /api/accounts/{userEmail}
```

**Get Specific Account:**
```
GET /api/accounts/{userEmail}/{accountId}
```

**Update Account:**
```
PUT /api/accounts/{userEmail}/{accountId}
Content-Type: application/json

{
  "name": "Updated Account Name",
  "balance": 1500.00,
  "username": "john.doe@example.com"
}
```

**Delete Account:**
```
DELETE /api/accounts/{userEmail}/{accountId}
```

### Transaction Endpoints

**Create Transaction:**
```
POST /api/transactions/{userEmail}/{accountId}
Content-Type: application/json

{
  "description": "Grocery shopping",
  "amount": 50.00,
  "date": "2024-01-15",
  "type": "expense"
}
```

**Get All Transactions for Account:**
```
GET /api/transactions/{userEmail}/{accountId}
```

**Get All Transactions for User:**
```
GET /api/transactions/{userEmail}
```

**Get Specific Transaction:**
```
GET /api/transactions/{userEmail}/{accountId}/{transactionId}
```

**Update Transaction:**
```
PUT /api/transactions/{userEmail}/{accountId}/{transactionId}
Content-Type: application/json

{
  "description": "Updated description",
  "amount": 75.00,
  "date": "2024-01-15",
  "type": "expense"
}
```

**Delete Transaction:**
```
DELETE /api/transactions/{userEmail}/{accountId}/{transactionId}
```

**Get Transactions by Type:**
```
GET /api/transactions/{userEmail}/{accountId}/type/{type}
```
Where `{type}` can be "expense" or "income"

## Example Usage

### Creating a new account:
```java
@Autowired
private FirestoreAccountService accountService;

Account account = new Account();
account.setName("Savings Account");
account.setBalance(new BigDecimal("5000.00"));
account.setUsername("user@example.com");

Account savedAccount = accountService.saveAccount("user@example.com", account);
```

### Creating a new transaction:
```java
@Autowired
private FirestoreTransactionService transactionService;

Transaction transaction = new Transaction();
transaction.setDescription("Salary payment");
transaction.setAmount(new BigDecimal("3000.00"));
transaction.setDate(LocalDate.now());
transaction.setType("income");

Transaction savedTransaction = transactionService.saveTransaction("user@example.com", "accountId", transaction);
```

## Prerequisites

1. Firebase Admin SDK is already configured in the project
2. Firebase service account JSON file is in the resources folder
3. Firestore database is set up in your Firebase project

## Notes

- All operations are asynchronous and use Firestore's ApiFuture
- Error handling is implemented for all operations
- The services automatically generate document IDs for new accounts and transactions
- The structure ensures data isolation between users
- All endpoints support CORS for frontend integration 