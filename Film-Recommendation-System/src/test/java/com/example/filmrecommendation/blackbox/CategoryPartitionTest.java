package com.example.filmrecommendation.blackbox;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Category Partition Testing
 * Categories:
 * 1. Username
 *    - Valid: alphanumeric, 3-20 characters
 *    - Invalid: special characters, too short, too long
 * 2. Password
 *    - Valid: 8+ characters, with numbers
 *    - Invalid: too short, no numbers
 * 3. Authentication State
 *    - Logged in
 *    - Not logged in
 * 4. Genre Preferences
 *    - Valid genres
 *    - Invalid/empty genres
 */

public class CategoryPartitionTest {
	@Test
    void testUsernamePartitions() {
        // Valid Partitions
        assertTrue(isValidUsername("user123"));         // Alphanumeric
        assertTrue(isValidUsername("johndoe"));         // Letters only
        
        // Invalid Partitions
        assertFalse(isValidUsername("ab"));            // Too short
        assertFalse(isValidUsername("user@123"));      // Special characters
        assertFalse(isValidUsername("a".repeat(21)));  // Too long
    }
    
    @Test
    void testPasswordPartitions() {
        // Valid Partitions
        assertTrue(isValidPassword("password123"));     // Valid with numbers
        assertTrue(isValidPassword("Pass1234!"));      // Valid with special chars
        
        // Invalid Partitions
        assertFalse(isValidPassword("pass"));          // Too short
        assertFalse(isValidPassword("password"));      // No numbers
    }
    
    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{3,20}$");
    }
    
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }
}

