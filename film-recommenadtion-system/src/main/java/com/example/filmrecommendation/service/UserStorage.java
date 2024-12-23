package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserStorage {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    private static final String FILE_PATH = "users.dat";
    private List<User> users;

    public UserStorage() {
        users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        users.add(user);
        saveUsers();
        return true;
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    public boolean validateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
            if (!user.getPassword().startsWith("$2a$")) {
            if (user.getPassword().equals(password)) {
                user.setPassword(passwordEncoder.encode(password));
                saveUsers(); 
                return true;
            }
        } else {
            return passwordEncoder.matches(password, user.getPassword());
        }
        
        return false;
    }
    
    public void updateUser(User user) {
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            existingUser.setFavoriteGenre(user.getFavoriteGenre());
 
        }
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (!user.getPassword().startsWith("$2a$")) {
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                return true;
            }
        } else {
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                saveUsers();
                return true;
            }
        }
        
        return false;
    }
}