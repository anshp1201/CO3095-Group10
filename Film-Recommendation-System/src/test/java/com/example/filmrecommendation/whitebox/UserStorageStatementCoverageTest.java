package com.example.filmrecommendation.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.filmrecommendation.service.UserStorage;
import com.example.filmrecommendation.model.User;

public class UserStorageStatementCoverageTest {
    private UserStorage userStorage;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userStorage = new UserStorage();
        passwordEncoder = new BCryptPasswordEncoder();
        // Manually inject the passwordEncoder
        injectPasswordEncoder(userStorage, passwordEncoder);
    }

    // Utility method to inject the passwordEncoder
    private void injectPasswordEncoder(UserStorage userStorage, BCryptPasswordEncoder passwordEncoder) {
        try {
            java.lang.reflect.Field encoderField = UserStorage.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(userStorage, passwordEncoder);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddUserStatements() {
        // Test statement coverage for addUser method
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        
        // First attempt should succeed as user doesn't exist
        assertTrue(userStorage.addUser(user));
        
        // Create another user with same username
        User duplicateUser = new User();
        duplicateUser.setUsername("testUser");
        duplicateUser.setPassword("differentPassword");
        
        // Second attempt should fail as user already exists
        assertFalse(userStorage.addUser(duplicateUser));
    }


    @Test
    void testValidateUserStatements() {
        // Create and add a user
        User user = new User();
        user.setUsername("testUser");
        String plainPassword = "password123";
        
        // Set password and add user - password will be encoded during add
        user.setPassword(plainPassword);
        userStorage.addUser(user);
        
        // First validation should work with plain text password against encoded stored password
        assertTrue(userStorage.validateUser("testUser", plainPassword),
                "First validation should succeed with correct password");
        
        // Second validation should still work
        assertTrue(userStorage.validateUser("testUser", plainPassword),
                "Second validation should still succeed");
        
        // Test invalid password
        assertFalse(userStorage.validateUser("testUser", "wrongPassword"),
                "Validation with wrong password should fail");
        
        // Test non-existent user
        assertFalse(userStorage.validateUser("nonExistentUser", plainPassword),
                "Validation with non-existent user should fail");
    }

    @Test
    void testChangePasswordStatements() {
        // Create and add a user
        User user = new User();
        user.setUsername("testUser");
        String initialPassword = "password123";
        user.setPassword(initialPassword);
        userStorage.addUser(user);
        
        // Test changing password with correct old password
        assertTrue(userStorage.changePassword("testUser", initialPassword, "newPassword123"),
                "Password change should succeed with correct old password");
        
        // Test changing password again with the new password
        assertTrue(userStorage.changePassword("testUser", "newPassword123", "newerPassword123"),
                "Second password change should succeed with correct password");
        
        // Test with incorrect old password
        assertFalse(userStorage.changePassword("testUser", "wrongPassword", "newPassword123"),
                "Password change should fail with incorrect old password");
        
        // Test with non-existent user
        assertFalse(userStorage.changePassword("nonExistentUser", initialPassword, "newPassword123"),
                "Password change should fail for non-existent user");
    }

    @Test
    void testGetUserByUsernameStatements() {
        // Create and add a user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        userStorage.addUser(user);

        // Statement: Test retrieving existing user
        assertNotNull(userStorage.getUserByUsername("testUser"));

        // Statement: Test retrieving non-existent user
        assertNull(userStorage.getUserByUsername("nonExistentUser"));
    }
}
