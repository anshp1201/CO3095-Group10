package com.example.filmrecommendation.whitebox;

import com.example.filmrecommendation.model.Notification;
import com.example.filmrecommendation.service.NotificationStorage;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationStorageBranchCoverageTest {

    private static final String FILE_PATH = "test_notifications.dat";
    private NotificationStorage notificationStorage;

    @BeforeEach
    void setUp() {
        // Initialize NotificationStorage with a test file path
        notificationStorage = new NotificationStorage(FILE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up after each test
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    void testLoadNotifications_FileNotFound() {
        // Test the scenario where the file is not found
        List<Notification> notifications = notificationStorage.loadNotifications();
        assertTrue(notifications.isEmpty(), "Notifications list should be empty if the file does not exist");
    }

    @Test
    void testLoadNotifications_ValidFile() throws IOException, ClassNotFoundException {
        // Test the scenario where the file exists and contains notifications
        notificationStorage.addNotification("Test", "Message");
        List<Notification> notifications = notificationStorage.loadNotifications();
        
        assertEquals(1, notifications.size(), "There should be one notification loaded from the file");
        assertEquals("Test", notifications.get(0).getTitle());
        assertEquals("Message", notifications.get(0).getMessage());
    }

    @Test
    void testRemoveNotificationById_NotificationExists() throws IOException {
        // Test the scenario where the notification exists and is removed
        notificationStorage.addNotification("Test Title", "Test Message");
        String id = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(id);
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The notification should be removed from the list");
    }

    @Test
    void testRemoveNotificationById_NotificationDoesNotExist() throws IOException {
        // Test the scenario where the notification does not exist
        notificationStorage.addNotification("Test Title", "Test Message");
        notificationStorage.removeNotificationById("non-existent-id");
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "The list should remain unchanged when trying to remove a non-existent notification");
    }

    @Test
    void testClearAllNotifications() throws IOException {
        // Test the scenario where notifications exist and are cleared
        notificationStorage.addNotification("Notification 1", "Message 1");
        notificationStorage.addNotification("Notification 2", "Message 2");
        notificationStorage.clearAllNotifications();
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "All notifications should be cleared from the list");
    }

    @Test
    void testClearAllNotifications_EmptyList() throws IOException {
        // Test the scenario where the list is already empty and clearAllNotifications is called
        notificationStorage.clearAllNotifications();
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The list should remain empty if there are no notifications to clear");
    }

    @Test
    void testAddNotification() throws IOException {
        // Test adding a notification to the list and ensure it is saved and loaded correctly
        notificationStorage.addNotification("New Notification", "Message");
        List<Notification> notifications = notificationStorage.getAllNotifications();
        
        assertEquals(1, notifications.size(), "There should be one notification in the list");
        assertEquals("New Notification", notifications.get(0).getTitle());
        assertEquals("Message", notifications.get(0).getMessage());
    }
}

