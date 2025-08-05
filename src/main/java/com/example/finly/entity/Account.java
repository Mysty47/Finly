package com.example.finly.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String name;
    
    @JsonProperty("balance")
    private BigDecimal balance;
    
    private String username;
    
    // Custom getter for balance to ensure proper serialization
    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }
    
    // Custom setter for balance to handle string conversion
    public void setBalance(Object balance) {
        if (balance instanceof String) {
            this.balance = new BigDecimal((String) balance);
        } else if (balance instanceof Number) {
            this.balance = new BigDecimal(balance.toString());
        } else if (balance instanceof BigDecimal) {
            this.balance = (BigDecimal) balance;
        } else {
            this.balance = BigDecimal.ZERO;
        }
    }
}
