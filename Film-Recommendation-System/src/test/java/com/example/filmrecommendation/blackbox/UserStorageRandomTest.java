package com.example.filmrecommendation.blackbox;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.filmrecommendation.service.UserStorage;
import com.example.filmrecommendation.model.User;

import java.util.Random;
import java.util.UUID;

public class UserStorageRandomTest {
    private UserStorage userStorage;
    private Random random;

    @BeforeEach
    void setUp() {
        userStorage = new UserStorage();
        random = new Random();
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ReflectionTestUtils.setField(userStorage, "passwordEncoder", passwordEncoder);
    }

    // Tests random user registration with usernames and passwords of varying lengths.
    @ParameterizedTest
    @ValueSource(ints = {8, 16, 32, 64})
    void testRandomUserRegistration(int length) {
        // Generate random username and password of specified length
        String username = generateRandomString(length);
        String password = generateRandomString(length);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // First registration should succeed
        assertTrue(userStorage.addUser(user));

        // Verify user exists
        User storedUser = userStorage.getUserByUsername(username);
        assertNotNull(storedUser);
        assertEquals(username, storedUser.getUsername());
    }

    // Tests random password changes for a user.
    @Test
    void testRandomPasswordChanges() {
        // Create initial user
        String username = generateRandomString(10);
        String initialPassword = generateRandomString(10);

        User user = new User();
        user.setUsername(username);
        user.setPassword(initialPassword);
        userStorage.addUser(user);

        // Try changing password multiple times with random passwords
        for (int i = 0; i < 5; i++) {
            String newPassword = generateRandomString(random.nextInt(10) + 8); // At least 8 chars
            assertTrue(userStorage.changePassword(username, initialPassword, newPassword));
            initialPassword = newPassword; // Update for next iteration
        }
    }

 // Tests random user validation with valid and invalid combinations.
    @Test
    void testRandomUserValidation() {
        // Create a set of random users
        int userCount = 5;
        String[] usernames = new String[userCount];
        String[] passwords = new String[userCount];

        for (int i = 0; i < userCount; i++) {
            usernames[i] = generateRandomString(10);
            passwords[i] = generateRandomString(12);

            User user = new User();
            user.setUsername(usernames[i]);
            user.setPassword(passwords[i]);
            userStorage.addUser(user);
        }

        // Try random valid and invalid combinations
        for (int i = 0; i < userCount * 2; i++) {
            if (random.nextBoolean()) {
                // Test valid combination
                int index = random.nextInt(userCount);
                assertTrue(userStorage.validateUser(usernames[index], passwords[index]));
            } else {
                // Test invalid combination
                assertFalse(userStorage.validateUser(
                    generateRandomString(10), // Random username
                    generateRandomString(12)  // Random password
                ));
            }
        }
    }

    // Generates a random alphanumeric string of the specified length.
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }
}