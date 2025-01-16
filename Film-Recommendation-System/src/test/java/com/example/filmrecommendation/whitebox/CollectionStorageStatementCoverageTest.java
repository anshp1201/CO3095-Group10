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