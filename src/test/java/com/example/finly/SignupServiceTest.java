package com.example.finly;

import com.example.finly.service.SignupService;
import com.example.finly.service.FirestoreAccountService;
import com.example.finly.service.HashString;
import com.example.finly.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {

    @Mock
    private HashString hashString;

    @Mock
    private FirestoreAccountService firestoreAccountService;

    @InjectMocks
    private SignupService signupService;

    @Test
    public void testSignupWithDefaultAccounts() throws Exception {
        // Arrange
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", "test@example.com");
        userData.put("password", "password123");
        userData.put("username", "testuser");

        when(hashString.hashPassword(any())).thenReturn("hashedPassword");
        
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setName("Test Account");
        mockAccount.setBalance(BigDecimal.ZERO);
        mockAccount.setUsername("test@example.com");
        
        when(firestoreAccountService.saveAccount(any(), any())).thenReturn(mockAccount);

        // Act
        String result = signupService.saveUser(userData);

        // Assert
        assertNotNull(result);
        verify(hashString).hashPassword("password123");
        verify(firestoreAccountService, times(3)).saveAccount(eq("test@example.com"), any(Account.class));
    }

    @Test
    public void testSignupWithMissingEmail() {
        // Arrange
        Map<String, Object> userData = new HashMap<>();
        userData.put("password", "password123");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> signupService.saveUser(userData));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email is required", exception.getReason());
    }

    @Test
    public void testSignupWithMissingPassword() {
        // Arrange
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", "test@example.com");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> signupService.saveUser(userData));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Password is required", exception.getReason());
    }
} 