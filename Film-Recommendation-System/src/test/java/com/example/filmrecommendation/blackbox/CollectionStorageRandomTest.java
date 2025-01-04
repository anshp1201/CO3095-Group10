package com.example.filmrecommendation.blackbox;

import com.example.filmrecommendation.model.Collection;
import com.example.filmrecommendation.service.CollectionStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Random;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CollectionStorageRandomTest {
    private CollectionStorage collectionStorage;
    private Random random;
    private static final int MAX_RANDOM = 1000;

    //Method to set up the test environment before each test execution.
    @BeforeEach
    void setUp() {
        collectionStorage = new CollectionStorage();
        random = new Random();
    }

    //Method to test the creation of collections with random data.
    @RepeatedTest(3)
    void testCreateCollection_RandomData() {
        String username = "user" + random.nextInt(MAX_RANDOM);
        String collectionName = "collection" + random.nextInt(MAX_RANDOM);
        
        assertDoesNotThrow(() -> {
            Collection created = collectionStorage.createCollection(username, collectionName);
            assertNotNull(created);
        });
    }

    //Method to test the creation and retrieval of collections using random data.
    @RepeatedTest(3)
    void testCreateAndFind_RandomData() {
        String username = "user" + random.nextInt(MAX_RANDOM);
        String collectionName = "collection" + random.nextInt(MAX_RANDOM);
        
        assertDoesNotThrow(() -> {
            collectionStorage.createCollection(username, collectionName);
            Collection found = collectionStorage.findCollection(username, collectionName);
            assertNotNull(found);
        });
    }

    //Method to test the behaviour when attempting to create a collection with duplicate names.
    @Test
    void testCreateCollection_DuplicateNames() {
        String username = "user" + random.nextInt(MAX_RANDOM);
        String collectionName = "collection" + random.nextInt(MAX_RANDOM);
        
        assertDoesNotThrow(() -> {
            collectionStorage.createCollection(username, collectionName);
            assertThrows(IllegalStateException.class, () -> 
                collectionStorage.createCollection(username, collectionName));
        });
    }

    //Method to test the creation of collections with the same name but by different users.
    @RepeatedTest(3)
    void testCreateCollection_DifferentUsersWithSameCollectionName() {
        String collectionName = "collection" + random.nextInt(MAX_RANDOM);
        String username1 = "user" + random.nextInt(MAX_RANDOM);
        String username2 = "user" + (random.nextInt(MAX_RANDOM) + MAX_RANDOM); // Ensure different username
        
        assertDoesNotThrow(() -> {
            Collection collection1 = collectionStorage.createCollection(username1, collectionName);
            Collection collection2 = collectionStorage.createCollection(username2, collectionName);
            assertNotNull(collection1);
            assertNotNull(collection2);
        });
    }

    //Method to test the behaviour when searching for a non-existent collection.
    @Test
    void testFindNonExistentCollection() {
        String username = "nonexistent" + random.nextInt(MAX_RANDOM);
        String collectionName = "nonexistent" + random.nextInt(MAX_RANDOM);
        
        Collection found = collectionStorage.findCollection(username, collectionName);
        assertNull(found);
    }
}
