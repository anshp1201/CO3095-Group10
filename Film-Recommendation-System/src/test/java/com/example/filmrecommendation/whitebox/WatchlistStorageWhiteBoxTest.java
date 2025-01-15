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
    void testLoadWatchlists_EmptyFile() throws IOException {
        WatchlistStorage newStorage = new WatchlistStorage();
        List<Film> watchlist = newStorage.getWatchlistForUser(testUser);
        assertTrue(watchlist.isEmpty(), "Should have empty watchlist for new user");
    }

    @Test
    void testSaveWatchlists_ConcurrentAccess() throws InterruptedException {
        Thread thread1 = new Thread(() -> watchlistStorage.addToWatchlist(testUser, "Movie 1"));
        Thread thread2 = new Thread(() -> watchlistStorage.addToWatchlist(testUser, "Movie 2"));

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        List<Film> watchlist = watchlistStorage.getWatchlistForUser(testUser);
        assertTrue(watchlist.size() <= 2, "Concurrent adds should be handled safely");
    }

    @Test
    void testAddToWatchlist_NullUser() {
        assertThrows(NullPointerException.class, 
            () -> watchlistStorage.addToWatchlist(null, "Test Movie"),
            "Should throw NullPointerException for null user");
    }

    @Test
    void testAddToWatchlist_NullMovieTitle() {
        boolean result = watchlistStorage.addToWatchlist(testUser, null);
        assertFalse(result, "Should fail to add null movie title");
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

    @Test
    void testAddToWatchlist_FileSystemError() throws IOException {
        watchlistFile.setWritable(false);
        boolean result = watchlistStorage.addToWatchlist(testUser, "Test Movie");
        assertTrue(result, "Should still add movie to in-memory watchlist despite file system error");
        watchlistFile.setWritable(true);
    }

    @Test
    void testWatchlistPersistence() {
        watchlistStorage.addToWatchlist(testUser, "Test Movie");
        WatchlistStorage newStorage = new WatchlistStorage();
        boolean result = newStorage.isInWatchlist(testUser, "Test Movie");
        assertTrue(result, "Movie should persist in watchlist across instances");
    }

    @Test
    void testMemoryStateConsistency() {
        watchlistStorage.addToWatchlist(testUser, "Test Movie");
        boolean inWatchlist = watchlistStorage.isInWatchlist(testUser, "Test Movie");
        List<Film> watchlist = watchlistStorage.getWatchlistForUser(testUser);
        
        assertTrue(inWatchlist, "isInWatchlist should match internal state");
        assertEquals(1, watchlist.size(), "Watchlist size should match internal state");
    }
}