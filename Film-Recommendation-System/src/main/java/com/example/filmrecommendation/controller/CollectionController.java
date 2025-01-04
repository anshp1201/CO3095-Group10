package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.Collection;
import com.example.filmrecommendation.service.CollectionStorage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    @Autowired
    private CollectionStorage collectionStorage;
    
    
    //End point used to Create a new Collection for a specific user.
    @PostMapping("/add")
    public ResponseEntity<String> createCollection(
            @RequestParam("username") String username,
            @RequestParam("collectionName") String collectionName) {
        try {
            collectionStorage.createCollection(username, collectionName);
            return ResponseEntity.ok("Collection created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create collection: " + e.getMessage());
        }
    }
    
    
    //End point for adding movies to a specific collection for a user.
    @PostMapping("/{username}/{collectionName}/add-movie")
    public ResponseEntity<String> addMovieToCollection(
            @PathVariable("username") String username,
            @PathVariable("collectionName") String collectionName,
            @RequestParam("movieTitle") String movieTitle) {

        System.out.println("Received request to add movie:");
        System.out.println("Username: " + username);
        System.out.println("Collection: " + collectionName);
        System.out.println("Movie: " + movieTitle);

        try {
            boolean added = collectionStorage.addMovie(username, collectionName, movieTitle);

            if (!added) {
                String message = "Failed to add movie. Either the collection doesn't exist, the movie isn't found, or it already exists in the collection.";
                System.out.println(message);
                return ResponseEntity.badRequest().body(message);
            }

            String message = "Movie added to collection successfully.";
            System.out.println(message);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            String message = "Failed to add movie: " + e.getMessage();
            System.out.println("Error: " + message);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(message);
        }
    }

    
    //End point to retrieve a specific collection for a user by username and collection name.
    @GetMapping("/{username}/{collectionName}")
    public ResponseEntity<Collection> getCollection(
            @PathVariable("username") String username,
            @PathVariable("collectionName") String collectionName) {
        
        Collection collection = collectionStorage.findCollection(username, collectionName);
        if (collection == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(collection);
    }
}