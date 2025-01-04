package com.example.filmrecommendation.whitebox;

import com.example.filmrecommendation.model.Collection;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.CollectionStorage;
import com.example.filmrecommendation.service.FilmStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectionStorageStatementCoverageTest {
    private CollectionStorage collectionStorage;

    @Mock
    private FilmStorage filmStorage;

    private static final String TEST_FILE_PATH = "test-collections.dat";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        //Delete test file if it exists
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        collectionStorage = new CollectionStorage();
        
        //Inject mock FilmStorage
        try {
            java.lang.reflect.Field field = CollectionStorage.class.getDeclaredField("filmStorage");
            field.setAccessible(true);
            field.set(collectionStorage, filmStorage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    @Test
    void testFindCollection_StatementCoverage() {
        //Create a test collection
        Collection collection = collectionStorage.createCollection("john", "Favorites");
        assertNotNull(collection);
        
        //Test finding existing collection
        Collection result = collectionStorage.findCollection("john", "Favorites");
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("Favorites", result.getCollectionName());
        
        //Test finding non-existent collection
        Collection nonExistent = collectionStorage.findCollection("jane", "Nonexistent");
        assertNull(nonExistent);
    }

    @Test
    void testCreateCollection_StatementCoverage() {
        //Test creating a valid collection
        Collection collection = collectionStorage.createCollection("alice", "Action Movies");
        assertNotNull(collection);
        assertEquals("alice", collection.getUsername());
        assertEquals("Action Movies", collection.getCollectionName());
        
        //Test creating a duplicate collection
        assertThrows(IllegalStateException.class, () -> 
            collectionStorage.createCollection("alice", "Action Movies"));
    }

    @Test
    void testAddMovie_StatementCoverage() {
        //Create test collection
        collectionStorage.createCollection("bob", "Sci-Fi");
        
        //Mock film storage
        Film mockFilm = new Film();
        mockFilm.setTitle("Inception");
        when(filmStorage.hasFilm("Inception")).thenReturn(true);
        
        //Test adding a movie successfully
        boolean added = collectionStorage.addMovie("bob", "Sci-Fi", "Inception");
        assertTrue(added);
        
        Collection collection = collectionStorage.findCollection("bob", "Sci-Fi");
        assertTrue(collection.getMovieTitles().contains("Inception"));
        
        //Test adding movie to non-existent collection
        boolean nonExistentResult = collectionStorage.addMovie("nonexistent", "Invalid", "Movie");
        assertFalse(nonExistentResult);
        
        //Test adding duplicate movie
        boolean duplicateResult = collectionStorage.addMovie("bob", "Sci-Fi", "Inception");
        assertFalse(duplicateResult);
        
        //Test adding non-existent movie
        when(filmStorage.hasFilm("NonExistentMovie")).thenReturn(false);
        boolean nonExistentMovie = collectionStorage.addMovie("bob", "Sci-Fi", "NonExistentMovie");
        assertFalse(nonExistentMovie);
    }

    @Test
    void testGetUserCollections_StatementCoverage() {
        //Create test collections
        collectionStorage.createCollection("charlie", "Horror");
        collectionStorage.createCollection("charlie", "Comedy");
        
        //Test getting collections for existing user
        List<Collection> userCollections = collectionStorage.getUserCollections("charlie");
        assertEquals(2, userCollections.size());
        assertTrue(userCollections.stream()
            .allMatch(c -> c.getUsername().equals("charlie")));
        
        //Test getting collections for non-existent user
        List<Collection> emptyList = collectionStorage.getUserCollections("nonexistent");
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testCreateCollection_InvalidInputStatementCoverage() {
        //Test null username
        assertThrows(IllegalArgumentException.class, () -> 
            collectionStorage.createCollection(null, "Test"));
        
        //Test empty collection name
        assertThrows(IllegalArgumentException.class, () -> 
            collectionStorage.createCollection("david", ""));
        
        //Test whitespace username
        assertThrows(IllegalArgumentException.class, () -> 
            collectionStorage.createCollection("  ", "Test"));
        
        //Test whitespace collection name
        assertThrows(IllegalArgumentException.class, () -> 
            collectionStorage.createCollection("david", "   "));
    }
}