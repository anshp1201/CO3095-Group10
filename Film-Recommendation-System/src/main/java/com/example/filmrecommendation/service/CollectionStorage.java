package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionStorage {

    private static final String FILE_PATH = "collections.dat";
    private List<Collection> collections;

    @Autowired
    private FilmStorage filmStorage;

    //Load existing collections from a file or create a new list if file doesn't exists.
    public CollectionStorage() {
        collections = loadCollections();
        if (collections == null) {
            collections = new ArrayList<>();
            saveCollections();
        }
    }

    //Method to load collections from the file. 
    @SuppressWarnings("unchecked")
    private List<Collection> loadCollections() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Collection>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    //Method to save collections to a file to ensure thread safety.
    private synchronized void saveCollections() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(collections);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save collections", e);
        }
    }

    //Method to create a new collection for a user. 
    public synchronized Collection createCollection(String username, String collectionName) {
        if (username == null || username.trim().isEmpty() ||
            collectionName == null || collectionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and collection name cannot be empty");
        }

        boolean exists = collections.stream()
                .anyMatch(c -> c.getUsername().equals(username) && c.getCollectionName().equals(collectionName));

        if (exists) {
            throw new IllegalStateException("Collection already exists");
        }

        Collection collection = new Collection(username, collectionName);
        collections.add(collection);
        saveCollections();
        return collection;
    }

    //Method to find and return a specific collection by username and collection name.
    public Collection findCollection(String username, String collectionName) {
        return collections.stream()
                .filter(c -> c.getUsername().equals(username) && c.getCollectionName().equals(collectionName))
                .findFirst()
                .orElse(null);
    }

    //Method to add a movie to a specific collection for a user.
    public synchronized boolean addMovie(String username, String collectionName, String movieTitle) {
        System.out.println("Attempting to add movie: '" + movieTitle + "' to collection: '" +
                collectionName + "' for user: '" + username + "'");

        //Find the collection
        Collection collection = findCollection(username, collectionName);
        if (collection == null) {
            System.out.println("Collection not found!");
            return false;
        }

        //Check if the movie exists in filmStorage
        if (!filmStorage.hasFilm(movieTitle)) {
            System.out.println("Movie not found in film storage: " + movieTitle);
            return false;
        }

        //Check if the movie already exists in the collection
        if (collection.getMovieTitles().contains(movieTitle)) {
            System.out.println("Movie already exists in the collection: " + movieTitle);
            return false;
        }

        //Add the movie to the collection
        boolean added = collection.getMovieTitles().add(movieTitle);
        if (added) {
            System.out.println("Movie added successfully");
            System.out.println("Current collection contents: " + collection.getMovieTitles());
            saveCollections();
        }
        return added;
    }

    //Method to retrieve all collections for a specific user.
    public List<Collection> getUserCollections(String username) {
        return collections.stream()
                .filter(c -> c.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    //Method to check if a movie exists in the film storage.
    private boolean movieExists(String movieTitle) {
        return filmStorage.getAllFilms().stream()
                .anyMatch(film -> film.getTitle().equals(movieTitle));
    }
}
