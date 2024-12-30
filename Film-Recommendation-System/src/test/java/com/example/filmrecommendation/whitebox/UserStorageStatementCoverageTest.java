package com.example.filmrecommendation.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.filmrecommendation.service.UserStorage;
import com.example.filmrecommendation.model.User;

import java.io.File;

public class UserStorageStatementCoverageTest {
    private UserStorage userStorage;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userStorage = new UserStorage();
        passwordEncoder = new BCryptPasswordEncoder();
        
        
        try {
            java.lang.reflect.Field encoderField = UserStorage.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(userStorage, passwordEncoder);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject password encoder", e);
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the users.dat file
        File usersFile = new File("users.dat");
        if (usersFile.exists()) {
            usersFile.delete();
        }
    }

    @Test
    void testAddUserStatements() {
        // Create first user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        
        // First add should succeed
        assertTrue(userStorage.addUser(user), "First user should be added successfully");
        
        // Verify user exists
        assertNotNull(userStorage.getUserByUsername("testUser"), "User should exist after adding");
        
        // Try to add duplicate user
        User duplicateUser = new User();
        duplicateUser.setUsername("testUser");
        duplicateUser.setPassword("differentPassword");
        
        // Second add should fail
        assertFalse(userStorage.addUser(duplicateUser), "Duplicate user should not be added");
    }

    @Test
    void testValidateUserStatements() {
        // Create and add user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        userStorage.addUser(user);
        
        // Test initial validation (should handle plain text)
        assertTrue(userStorage.validateUser("testUser", "password123"), 
            "Initial validation should succeed");
        
        // Second validation (password should be encrypted now)
        assertTrue(userStorage.validateUser("testUser", "password123"), 
            "Second validation should succeed");
        
        // Test wrong password
        assertFalse(userStorage.validateUser("testUser", "wrongPassword"), 
            "Wrong password should fail");
        
        // Test non-existent user
        assertFalse(userStorage.validateUser("nonExistentUser", "password123"), 
            "Non-existent user should fail");
    }

    @Test
    void testChangePasswordStatements() {
        // Create and add user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        userStorage.addUser(user);
        
        // First password change
        assertTrue(userStorage.changePassword("testUser", "password123", "newPassword123"),
            "First password change should succeed");
        
        // Verify new password works
        assertTrue(userStorage.validateUser("testUser", "newPassword123"),
            "Should validate with new password");
        
        // Change password again
        assertTrue(userStorage.changePassword("testUser", "newPassword123", "newerPassword123"),
            "Second password change should succeed");
        
        // Test with wrong password
        assertFalse(userStorage.changePassword("testUser", "wrongPassword", "newPassword123"),
            "Wrong password should fail");
        
        // Test with non-existent user
        assertFalse(userStorage.changePassword("nonExistentUser", "password123", "newPassword123"),
            "Non-existent user should fail");
    }

    @Test
    void testGetUserByUsernameStatements() {
        // Create and add user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        userStorage.addUser(user);
        
        // Test getting existing user
        User foundUser = userStorage.getUserByUsername("testUser");
        assertNotNull(foundUser, "Should find existing user");
        assertEquals("testUser", foundUser.getUsername(), "Username should match");
        
        // Test getting non-existent user
        assertNull(userStorage.getUserByUsername("nonExistentUser"),
            "Should return null for non-existent user");
    }
}