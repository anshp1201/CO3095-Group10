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
	
    private static final String FILE_PATH = "users.dat"; //Path to store user data
    private List<User> users;

    public UserStorage() {
        users = loadUsers(); //Load users from file to startup
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
    	//Attempt to load users to file, return empty list on errors 
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
    	//Save user data to file 
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//*
    public boolean addUser(User user) {
        //Check for duplicate username before adding
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        // Encode the password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        users.add(user);
        saveUsers();
        return true;
    }
    
    public User getUserByUsername(String username) {
    	//Find user by username using streams
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    public boolean validateUser(String username, String password) {
    	//Validate user credentials with password encoding 
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
    
  //Update user's favorite genre
    public void updateUser(User user) {
    	User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            existingUser.setFavoriteGenre(user.getFavoriteGenre());
 
        }
    }

    //Change user's password
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            saveUsers();
            return true;
        }
        
        return false;
    }
    }