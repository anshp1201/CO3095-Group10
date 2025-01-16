package com.example.filmrecommendation.whitebox;

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

public class NotificationStorageBranchCoverageTest {

    private static final String TEST_FILE_PATH = "test_notifications_whitebox.dat";  
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
    void testRemoveNotificationById_NotificationExists() throws IOException {
        
        notificationStorage.addNotification("Test Title", "Test Message");
        String id = notificationStorage.getAllNotifications().get(0).getId();
        notificationStorage.removeNotificationById(id);
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The notification should be removed from the list");
    }

    @Test
    void testRemoveNotificationById_NotificationDoesNotExist() throws IOException {
        
        notificationStorage.addNotification("Test Title", "Test Message");
        notificationStorage.removeNotificationById("non-existent-id");
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertEquals(1, notifications.size(), "The list should remain unchanged when trying to remove a non-existent notification");
    }

    @Test
    void testClearAllNotifications() throws IOException {
        
        notificationStorage.addNotification("Notification 1", "Message 1");
        notificationStorage.addNotification("Notification 2", "Message 2");
        notificationStorage.clearAllNotifications();
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "All notifications should be cleared from the list");
    }

    @Test
    void testClearAllNotifications_EmptyList() throws IOException {
        
        notificationStorage.clearAllNotifications();
        
        List<Notification> notifications = notificationStorage.getAllNotifications();
        assertTrue(notifications.isEmpty(), "The list should remain empty if there are no notifications to clear");
    }

    @Test
    void testAddNotification() throws IOException {
        
        notificationStorage.addNotification("New Notification", "Message");
        List<Notification> notifications = notificationStorage.getAllNotifications();
        
        assertEquals(1, notifications.size(), "There should be one notification in the list");
        assertEquals("New Notification", notifications.get(0).getTitle());
        assertEquals("Message", notifications.get(0).getMessage());
    }
}
