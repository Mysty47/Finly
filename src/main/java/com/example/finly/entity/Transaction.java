package com.example.finly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    
    private String description;
    
    private BigDecimal amount;
    
    private String date; // Changed from LocalDate to String
    
    private String type; // "expense" или "income"
    
    private Account account;
} 