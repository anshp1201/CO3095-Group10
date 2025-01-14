package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.Notification;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationStorage {

    private static final String FILE_PATH = "notifications.dat";
    private List<Notification> notifications;

    public NotificationStorage() {
        notifications = loadNotifications();
    }

    @SuppressWarnings("unchecked")
	private List<Notification> loadNotifications() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Notification>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveNotifications() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(notifications);
        }
    }

    public void addNotification(String title, String message) throws IOException {
        Notification newNotification = new Notification(title, message);
        notifications.add(newNotification);
        saveNotifications();  
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public void removeNotificationById(String id) throws IOException {
        Optional<Notification> notificationToRemove = notifications.stream()
                .filter(notification -> notification.getId().equals(id))
                .findFirst();

        notificationToRemove.ifPresent(notification -> {
            notifications.remove(notification);
            try {
                saveNotifications();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void clearAllNotifications() throws IOException {
        notifications.clear();
        saveNotifications(); 
    }
}

