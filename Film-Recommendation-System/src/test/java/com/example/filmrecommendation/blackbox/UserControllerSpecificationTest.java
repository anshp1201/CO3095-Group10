package com.example.filmrecommendation.blackbox;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import com.example.filmrecommendation.controller.UserController;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.UserStorage;

public class UserControllerSpecificationTest {
    private UserController userController;
    private UserStorage userStorage;
    private HttpSession session;
    private RedirectAttributes redirectAttributes;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userStorage = mock(UserStorage.class);
        session = mock(HttpSession.class);
        redirectAttributes = mock(RedirectAttributes.class);
        passwordEncoder = new BCryptPasswordEncoder();
        
        userController = new UserController();
        // Using reflection to set userStorage
        try {
            java.lang.reflect.Field field = UserController.class.getDeclaredField("userStorage");
            field.setAccessible(true);
            field.set(userController, userStorage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSuccessfulRegistration() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        when(userStorage.addUser(any(User.class))).thenReturn(true);

        // Act
        String result = userController.registerUser(user, redirectAttributes);

        // Assert
        assertEquals("redirect:/login", result);
        verify(redirectAttributes).addFlashAttribute("success", "Registration successful!");
    }

    @Test
    void testRegistrationWithExistingUsername() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password123");
        when(userStorage.addUser(any(User.class))).thenReturn(false);

        // Act
        String result = userController.registerUser(user, redirectAttributes);

        // Assert
        assertEquals("redirect:/register", result);
        verify(redirectAttributes).addFlashAttribute("error", "Username already exists!");
    }

    @Test
    void testSuccessfulLogin() {
        // Arrange
        String username = "testUser";
        String password = "password123";
        when(userStorage.validateUser(username, password)).thenReturn(true);
        when(userStorage.getUserByUsername(username)).thenReturn(new User());

        // Act
        String result = userController.login(username, password, session, redirectAttributes);

        // Assert
        assertEquals("redirect:/dashboard", result);
        verify(session).setAttribute(eq("loggedInUser"), any(User.class));
    }

    @Test
    void testFailedLogin() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        when(userStorage.validateUser(username, password)).thenReturn(false);

        // Act
        String result = userController.login(username, password, session, redirectAttributes);

        // Assert
        assertEquals("redirect:/login", result);
        verify(redirectAttributes).addFlashAttribute("error", "Invalid username or password");
    }

    @Test
    void testChangePasswordSuccess() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(session.getAttribute("loggedInUser")).thenReturn(user);
        when(userStorage.changePassword("testUser", "oldPass", "newPassword123")).thenReturn(true);

        // Act
        String result = userController.changePassword("oldPass", "newPassword123", session, redirectAttributes);

        // Assert
        assertEquals("redirect:/profile", result);
        verify(redirectAttributes).addFlashAttribute("success", "Password changed successfully");
    }

    @Test
    void testChangePasswordWithShortPassword() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(session.getAttribute("loggedInUser")).thenReturn(user);

        // Act
        String result = userController.changePassword("oldPass", "short", session, redirectAttributes);

        // Assert
        assertEquals("redirect:/profile", result);
        verify(redirectAttributes).addFlashAttribute("error", "Password must be at least 8 characters long");
    }
}



