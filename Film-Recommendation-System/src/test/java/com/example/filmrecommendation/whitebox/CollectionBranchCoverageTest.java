package com.example.filmrecommendation.whitebox;

import com.example.filmrecommendation.controller.CollectionController;
import com.example.filmrecommendation.model.Collection;
import com.example.filmrecommendation.service.CollectionStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectionBranchCoverageTest {
    @Mock
    private CollectionStorage collectionStorage;

    @InjectMocks
    private CollectionController collectionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


 
    //Tests the behavior when a null username is provided.
    @Test
    void testCreateCollection_NullUsername() {
        
        when(collectionStorage.createCollection(null, "collection"))
            .thenThrow(new IllegalArgumentException("Username and collection name cannot be empty"));
        ResponseEntity<String> response = collectionController.createCollection(null, "collection");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid input: Username and collection name cannot be empty", response.getBody());
    }

    //Tests the behavior when a blank collection name is provided.
    @Test
    void testCreateCollection_BlankCollectionName() {
       
        when(collectionStorage.createCollection("user", "  "))
            .thenThrow(new IllegalArgumentException("Username and collection name cannot be empty"));
        ResponseEntity<String> response = collectionController.createCollection("user", "  ");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid input: Username and collection name cannot be empty", response.getBody());
    }

    //Tests the behavior when attempting to create a duplicate collection.
    @Test
    void testCreateCollection_DuplicateCollection() {
    
        when(collectionStorage.createCollection("user", "collection"))
            .thenThrow(new IllegalStateException("Collection already exists"));
        ResponseEntity<String> response = collectionController.createCollection("user", "collection");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Collection already exists", response.getBody());
    }

    //Tests retrieving a collection that exists.
    @Test
    void testGetCollection_CollectionExists() {
    
        Collection mockCollection = new Collection("john", "Favorites");
        when(collectionStorage.findCollection("john", "Favorites")).thenReturn(mockCollection);
        ResponseEntity<Collection> response = collectionController.getCollection("john", "Favorites");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCollection, response.getBody());
    }

    //Tests retrieving a collection that does not exist.
    @Test
    void testGetCollection_CollectionNotFound() {
       
        when(collectionStorage.findCollection("jane", "Nonexistent")).thenReturn(null);
        ResponseEntity<Collection> response = collectionController.getCollection("jane", "Nonexistent");
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

 

    //Tests the behavior when a movie cannot be added to a collection.
    @Test
    void testAddMovieToCollection_MovieNotAdded() {
       
        when(collectionStorage.addMovie("user", "Favorites", "Inception")).thenReturn(false);
        ResponseEntity<String> response = collectionController.addMovieToCollection("user", "Favorites", "Inception");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Failed to add movie. Either the collection doesn't exist, the movie isn't found, or it already exists in the collection.", 
                    response.getBody());
    }

    //Tests the behavior when an exception is thrown during the movie addition process.
    @Test
    void testAddMovieToCollection_ExceptionThrown() {
      
        when(collectionStorage.addMovie("user", "Favorites", "Inception"))
            .thenThrow(new RuntimeException("Storage error"));
         ResponseEntity<String> response = collectionController.addMovieToCollection("user", "Favorites", "Inception");
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Failed to add movie: Storage error", response.getBody());
    }
}