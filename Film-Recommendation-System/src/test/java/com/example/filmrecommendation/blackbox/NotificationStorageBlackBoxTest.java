package com.example.filmrecommendation.blackbox;

import com.example.filmrecommendation.model.Notification;
import com.example.filmrecommendation.service.NotificationStorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationStorageBlackBoxTest {

    private static final String TEST_FILE_PATH = "test_notifications_blackbox.dat"; 
    private NotificationStorage notificationStorage;

    @BeforeEach
    void setUp() throws IOException {
        
        notificationStorage = new NotificationStorage(TEST_FILE_PATH);

        
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @AfterEach
    void tearDown() throws IOException {
       
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    void testAddSingleNotification() throws IOException {
       
        notificationStorage.addNotification("Test Title 1", "Test Message 1");

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "There should be one notification.");
        assertEquals("Test Title 1", notifications.get(0).getTitle());
        assertEquals("Test Message 1", notifications.get(0).getMessage());
    }

    @Test
    void testAddMultipleNotifications() throws IOException {
        
        notificationStorage.addNotification("Test Title 1", "Test Message 1");
        notificationStorage.addNotification("Test Title 2", "Test Message 2");
        notificationStorage.addNotification("Test Title 3", "Test Message 3");

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(3, notifications.size(), "There should be three notifications.");
        assertEquals("Test Title 1", notifications.get(0).getTitle());
        assertEquals("Test Title 2", notifications.get(1).getTitle());
        assertEquals("Test Title 3", notifications.get(2).getTitle());
    }

    @Test
    void testRemoveNotificationById_ValidId() throws IOException {
        
        notificationStorage.addNotification("Test Title", "Test Message");
        String idToRemove = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(idToRemove);

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The notification should be removed.");
    }

    @Test
    void testRemoveNotificationById_InvalidId() throws IOException {
        
        notificationStorage.addNotification("Test Title", "Test Message");
        notificationStorage.removeNotificationById("non-existent-id");

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "The notification should remain in the list.");
    }

    @Test
    void testClearAllNotifications() throws IOException {
        
        notificationStorage.addNotification("Test Title 1", "Test Message 1");
        notificationStorage.addNotification("Test Title 2", "Test Message 2");
        notificationStorage.clearAllNotifications();

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "All notifications should be cleared.");
    }

    @Test
    void testClearAllNotifications_EmptyList() throws IOException {
        
        notificationStorage.clearAllNotifications();

        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The list should remain empty.");
    }

    @Test
    void testLoadNotifications_AfterAdding() throws IOException {
       
        notificationStorage.addNotification("Test Title", "Test Message");
        List<Notification> notifications = notificationStorage.loadNotifications();
        
        assertEquals(1, notifications.size(), "There should be one notification loaded.");
        assertEquals("Test Title", notifications.get(0).getTitle());
        assertEquals("Test Message", notifications.get(0).getMessage());
    }

    @Test
    void testLoadNotifications_AfterRemoving() throws IOException {
       
        notificationStorage.addNotification("Test Title", "Test Message");
        String idToRemove = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(idToRemove);

        List<Notification> notifications = notificationStorage.loadNotifications();
        assertTrue(notifications.isEmpty(), "The list should be empty after removing all notifications.");
    }
}
