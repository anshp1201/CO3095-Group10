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
	
	//Tests  valid and invalid partitions for usernames.
	@Test
    void testUsernamePartitions() {
        // Valid Partitions
        assertTrue(isValidUsername("user123"));         
        assertTrue(isValidUsername("johndoe"));        
        
        // Invalid Partitions
        assertFalse(isValidUsername("ab"));           
        assertFalse(isValidUsername("user@123"));      
        assertFalse(isValidUsername("a".repeat(21)));  
    }
    
	//Tests  valid and  invalid partition for password.
    @Test
    void testPasswordPartitions() {
        // Valid Partitions
        assertTrue(isValidPassword("password123"));     
        assertTrue(isValidPassword("Pass1234!"));      
        
        // Invalid Partitions
        assertFalse(isValidPassword("pass"));          
        assertFalse(isValidPassword("password"));      
    }
    
    // Validates the username based on specified criteria.
    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{3,20}$");
    }
    
    // Validates the password based on specified criteria.
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }
}

