package com.example.filmrecommendation.blackbox;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CollectionSpecificationTest {
    @Mock
    private CollectionStorage collectionStorage;

    @InjectMocks
    private CollectionController collectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    //Test Creating a collection with valid input.
    @Test
    void testCreateCollection_ValidInput() {
        
        Collection mockCollection = new Collection("alice", "Favorites");
        when(collectionStorage.createCollection("alice", "Favorites")).thenReturn(mockCollection);
        ResponseEntity<String> response = collectionController.createCollection("alice", "Favorites");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Collection created successfully.", response.getBody());
        verify(collectionStorage).createCollection("alice", "Favorites");
    }


    //Test creating a collection with invalid input.
    @Test
    void testCreateCollection_InvalidInput() {
        
        doThrow(new IllegalArgumentException("Username and collection name cannot be empty"))
                .when(collectionStorage).createCollection("", "");
        ResponseEntity<String> response = collectionController.createCollection("", "");
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().startsWith("Invalid input"));
    }

    //Test creating a collection that already exists. 
    @Test
    void testCreateCollection_ExistingCollection() {
        
        doThrow(new IllegalStateException("Collection already exists"))
            .when(collectionStorage).createCollection("bob", "Favorites");
        ResponseEntity<String> response = collectionController.createCollection("bob", "Favorites");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Collection already exists", response.getBody());
        verify(collectionStorage).createCollection("bob", "Favorites");
    }

    //Test adding a movie to a collection with valid input.
    @Test
    void testAddMovieToCollection_ValidInput() {
        
        when(collectionStorage.addMovie("alice", "Favorites", "Inception"))
            .thenReturn(true);
        ResponseEntity<String> response = collectionController.addMovieToCollection(
            "alice", "Favorites", "Inception");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Movie added to collection successfully.", response.getBody());
        verify(collectionStorage).addMovie("alice", "Favorites", "Inception");
    }

    //Test adding a movie to a collection when the movie cannot be added
    @Test
    void testAddMovieToCollection_MovieNotAdded() {
        
        when(collectionStorage.addMovie("alice", "Favorites", "NonExistentMovie"))
            .thenReturn(false);
        ResponseEntity<String> response = collectionController.addMovieToCollection(
            "alice", "Favorites", "NonExistentMovie");
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().startsWith("Failed to add movie"));
        verify(collectionStorage).addMovie("alice", "Favorites", "NonExistentMovie");
    }

    //Test retrieving a collection that exists.
    @Test
    void testGetCollection_ExistingCollection() {
        
        Collection mockCollection = new Collection("alice", "Favorites");
        when(collectionStorage.findCollection("alice", "Favorites"))
            .thenReturn(mockCollection);
        ResponseEntity<Collection> response = collectionController.getCollection("alice", "Favorites");
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("alice", response.getBody().getUsername());
        assertEquals("Favorites", response.getBody().getCollectionName());
        verify(collectionStorage).findCollection("alice", "Favorites");
    }

    //Test retrieving a collection that does not exist.
    @Test
    void testGetCollection_NonExistingCollection() {
        
        when(collectionStorage.findCollection("alice", "NonExistent"))
            .thenReturn(null);
        ResponseEntity<Collection> response = collectionController.getCollection("alice", "NonExistent");
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(collectionStorage).findCollection("alice", "NonExistent");
    }
}
