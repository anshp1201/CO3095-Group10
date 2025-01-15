package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WatchlistStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "watchlists.dat";
    private Map<String, Set<String>> userWatchlists; 
    
    @Autowired
    private FilmStorage filmStorage;

    public WatchlistStorage() {
        userWatchlists = loadWatchlists();
        if (userWatchlists == null) {
            userWatchlists = new HashMap<>();
            saveWatchlists();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Set<String>> loadWatchlists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Set<String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Creating new watchlist file");
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private synchronized void saveWatchlists() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userWatchlists);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save watchlists: " + e.getMessage());
        }
    }

    public synchronized boolean addToWatchlist(User user, String movieTitle) {
        if (!filmStorage.hasFilm(movieTitle)) {
            System.out.println("Film not found: " + movieTitle);
            return false;
        }

        String username = user.getUsername();
        userWatchlists.computeIfAbsent(username, k -> new HashSet<>());
        
        boolean added = userWatchlists.get(username).add(movieTitle);
        if (added) {
            saveWatchlists();
            System.out.println("Added " + movieTitle + " to " + username + "'s watchlist");
        }
        return added;
    }

    public synchronized boolean removeFromWatchlist(User user, String movieTitle) {
        String username = user.getUsername();
        if (!userWatchlists.containsKey(username)) {
            return false;
        }

        boolean removed = userWatchlists.get(username).remove(movieTitle);
        if (removed) {
            saveWatchlists();
            System.out.println("Removed " + movieTitle + " from " + username + "'s watchlist");
        }
        return removed;
    }

    public List<Film> getWatchlistForUser(User user) {
        Set<String> movieTitles = userWatchlists.getOrDefault(user.getUsername(), new HashSet<>());
        return filmStorage.getAllFilms().stream()
                .filter(film -> movieTitles.contains(film.getTitle()))
                .collect(Collectors.toList());
    }

    public boolean isInWatchlist(User user, String movieTitle) {
        Set<String> watchlist = userWatchlists.get(user.getUsername());
        return watchlist != null && watchlist.contains(movieTitle);
    }
}