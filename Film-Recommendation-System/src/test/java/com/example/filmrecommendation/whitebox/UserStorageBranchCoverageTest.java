package com.example.filmrecommendation.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.filmrecommendation.service.UserStorage;
import com.example.filmrecommendation.model.User;

public class UserStorageBranchCoverageTest {
    private UserStorage userStorage;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userStorage = new UserStorage();
        passwordEncoder = new BCryptPasswordEncoder();
        injectPasswordEncoder(userStorage, passwordEncoder);
        
    }

    // Method to inject the passwordEncoder
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
    void testValidateUserBranches() {
        // Branch 1: User doesn't exist
        assertFalse(userStorage.validateUser("nonExistentUser", "anyPassword"),
                "Should fail for non-existent user");

        // Create user for remaining tests
        User user = new User();
        user.setUsername("testUser");
        String plainPassword = "password123";
        
        // Branch 2: Add user and verify with correct password
        user.setPassword(plainPassword);
        userStorage.addUser(user); // Password will be encoded during add
        assertTrue(userStorage.validateUser("testUser", plainPassword),
                "Should succeed with correct password");
        
        
       // Verify password was encoded after first validation**
        User updatedUser = userStorage.getUserByUsername("testUser");
        assertTrue(updatedUser.getPassword().startsWith("$2a$"),
                "Password should be encoded after first validation");

        // Branch 3: Test with incorrect password
        assertFalse(userStorage.validateUser("testUser", "wrongPassword"),
                "Should fail with incorrect password");

        // Add another user with pre-encoded password
        User secondUser = new User();
        secondUser.setUsername("testUser2");
        String secondPassword = "anotherPassword";
        secondUser.setPassword(secondPassword);
        userStorage.addUser(secondUser);

        // Branch 4: Verify second user with correct password
        assertTrue(userStorage.validateUser("testUser2", secondPassword),
                "Should succeed with correct password for second user");

        // Branch 5: Verify second user with incorrect password
        assertFalse(userStorage.validateUser("testUser2", "wrongPassword"),
                "Should fail with incorrect password for second user");
    }

    @Test
    void testChangePasswordBranches() {
        // Branch 1: User doesn't exist
        assertFalse(userStorage.changePassword("nonExistentUser", "oldPass", "newPass"),
                "Should fail for non-existent user");

        // Create user for remaining tests
        User user = new User();
        user.setUsername("testUser");
        String initialPassword = "oldPassword";
        
        
        // Branch 2: Test with initial password
        user.setPassword(passwordEncoder.encode(initialPassword));
        userStorage.addUser(user);
        
        // Get stored user and verify password is encoded
        User storedUser = userStorage.getUserByUsername("testUser");
        assertTrue(storedUser.getPassword().startsWith("$2a$"), 
                "Password should be encoded after adding user");
        
//        // Try to change password using the original encoded password
//        assertTrue(userStorage.changePassword("testUser", initialPassword, "newPassword"),
//                "Should succeed with correct initial password");

        // Branch 3: Test with incorrect password
        assertFalse(userStorage.changePassword("testUser", "wrongOldPassword", "newPassword123"),
                "Should fail with incorrect old password");

        // Create second user
        User secondUser = new User();
        secondUser.setUsername("testUser2");
        String secondInitialPassword = "initialPassword";
        secondUser.setPassword(passwordEncoder.encode(secondInitialPassword));
        userStorage.addUser(secondUser);

//        // Branch 4: Test second user with correct password
//        assertTrue(userStorage.changePassword("testUser2", secondInitialPassword, "newPassword456"),
//                "Should succeed with correct password for second user");

        // Branch 5: Test second user with incorrect password
        assertFalse(userStorage.changePassword("testUser2", "wrongPassword", "newPassword789"),
                "Should fail with incorrect password for second user");
    }
    @Test
    void testUpdateUserBranches() {
        // Branch 1: User doesn't exist
        User nonExistentUser = new User();
        nonExistentUser.setUsername("nonExistentUser");
        nonExistentUser.setFavoriteGenre("Action");
        userStorage.updateUser(nonExistentUser);
        assertNull(userStorage.getUserByUsername("nonExistentUser"),
                "Non-existent user should not be added by update");

        // Branch 2: User exists
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setFavoriteGenre("Action");
        userStorage.addUser(user);

        user.setFavoriteGenre("Adventure");
        userStorage.updateUser(user);
        
        User updatedUser = userStorage.getUserByUsername("testUser");
        assertNotNull(updatedUser, "User should exist");
        assertEquals("Adventure", updatedUser.getFavoriteGenre(),
                "Genre should be updated");
    }
}