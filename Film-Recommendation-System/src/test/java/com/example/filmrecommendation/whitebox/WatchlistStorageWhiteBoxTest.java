package com.example.filmrecommendation.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.WatchlistStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@SpringBootTest
public class WatchlistStorageWhiteBoxTest {

    @TempDir
    Path tempDir;

    @MockBean
    private FilmStorage filmStorage;

    private WatchlistStorage watchlistStorage;
    private User testUser;
    private Film testFilm;
    private File watchlistFile;

    @BeforeEach
    void setUp() throws IOException {
        watchlistFile = tempDir.resolve("watchlists.dat").toFile();
        watchlistFile.createNewFile();

        watchlistStorage = new WatchlistStorage();
        testUser = new User();
        testUser.setUsername("testUser");
        
        testFilm = new Film();
        testFilm.setTitle("Test Movie");
        
        when(filmStorage.hasFilm(anyString())).thenReturn(true);
        when(filmStorage.getAllFilms()).thenReturn(Arrays.asList(testFilm));
    }

    @Test
    void testLoadWatchlists_FileDoesNotExist() {
        watchlistFile.delete();
        WatchlistStorage newStorage = new WatchlistStorage();
        assertNotNull(newStorage, "Should create new storage with empty watchlists");
    }

   



    @Test
    void testAddToWatchlist_NullUser() {
        assertThrows(NullPointerException.class, 
            () -> watchlistStorage.addToWatchlist(null, "Test Movie"),
            "Should throw NullPointerException for null user");
    }

   

    @Test
    void testRemoveFromWatchlist_EmptyWatchlist() {
        boolean result = watchlistStorage.removeFromWatchlist(testUser, "Test Movie");
        assertFalse(result, "Should fail to remove from empty watchlist");
    }

    @Test
    void testGetWatchlistForUser_NullUser() {
        assertThrows(NullPointerException.class, 
            () -> watchlistStorage.getWatchlistForUser(null),
            "Should throw NullPointerException for null user");
    }

    @Test
    void testIsInWatchlist_UserWithNoWatchlist() {
        boolean result = watchlistStorage.isInWatchlist(testUser, "Test Movie");
        assertFalse(result, "Should return false for user with no watchlist");
    }

    

   
    
}