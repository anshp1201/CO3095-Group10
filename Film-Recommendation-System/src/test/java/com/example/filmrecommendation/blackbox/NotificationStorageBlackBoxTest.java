package com.example.filmrecommendation.blackbox;

import com.example.filmrecommendation.model.Notification;
import com.example.filmrecommendation.service.NotificationStorage;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationStorageBlackBoxTest {

    private static final String FILE_PATH = "test_notifications_blackbox.dat";
    private NotificationStorage notificationStorage;

    @BeforeEach
    void setUp() {
        notificationStorage = new NotificationStorage(FILE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    void testAddSingleNotification() throws IOException {
        // Test adding a single notification
        notificationStorage.addNotification("Test Title 1", "Test Message 1");

        // Verify the notification is added correctly
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "There should be one notification.");
        assertEquals("Test Title 1", notifications.get(0).getTitle());
        assertEquals("Test Message 1", notifications.get(0).getMessage());
    }

    @Test
    void testAddMultipleNotifications() throws IOException {
        // Test adding multiple notifications
        notificationStorage.addNotification("Test Title 1", "Test Message 1");
        notificationStorage.addNotification("Test Title 2", "Test Message 2");
        notificationStorage.addNotification("Test Title 3", "Test Message 3");

        // Verify the notifications are added correctly
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(3, notifications.size(), "There should be three notifications.");
        assertEquals("Test Title 1", notifications.get(0).getTitle());
        assertEquals("Test Title 2", notifications.get(1).getTitle());
        assertEquals("Test Title 3", notifications.get(2).getTitle());
    }

    @Test
    void testRemoveNotificationById_ValidId() throws IOException {
        // Test removing a notification with a valid ID
        notificationStorage.addNotification("Test Title", "Test Message");
        String idToRemove = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(idToRemove);

        // Verify the notification is removed
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The notification should be removed.");
    }

    @Test
    void testRemoveNotificationById_InvalidId() throws IOException {
        // Test removing a notification with an invalid ID
        notificationStorage.addNotification("Test Title", "Test Message");
        notificationStorage.removeNotificationById("non-existent-id");

        // Verify the notification is still present (list should remain unchanged)
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "The notification should remain in the list.");
    }

    @Test
    void testClearAllNotifications() throws IOException {
        // Test clearing all notifications
        notificationStorage.addNotification("Test Title 1", "Test Message 1");
        notificationStorage.addNotification("Test Title 2", "Test Message 2");
        notificationStorage.clearAllNotifications();

        // Verify all notifications are cleared
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "All notifications should be cleared.");
    }

    @Test
    void testClearAllNotifications_EmptyList() throws IOException {
        // Test clearing all notifications when the list is already empty
        notificationStorage.clearAllNotifications();

        // Verify the list is still empty
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The list should remain empty.");
    }

    @Test
    void testLoadNotifications_EmptyFile() {
        // Test loading notifications from an empty file
        List<Notification> notifications = notificationStorage.loadNotifications();
        assertTrue(notifications.isEmpty(), "The list should be empty if no notifications are saved.");
    }

    @Test
    void testLoadNotifications_AfterAdding() throws IOException {
        // Test loading notifications after adding a notification
        notificationStorage.addNotification("Test Title", "Test Message");
        List<Notification> notifications = notificationStorage.loadNotifications();
        
        assertEquals(1, notifications.size(), "There should be one notification loaded.");
        assertEquals("Test Title", notifications.get(0).getTitle());
        assertEquals("Test Message", notifications.get(0).getMessage());
    }

    @Test
    void testLoadNotifications_AfterRemoving() throws IOException {
        // Test loading notifications after removing a notification
        notificationStorage.addNotification("Test Title", "Test Message");
        String idToRemove = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(idToRemove);

        List<Notification> notifications = notificationStorage.loadNotifications();
        assertTrue(notifications.isEmpty(), "The list should be empty after removing all notifications.");
    }
}

