package com.example.filmrecommendation.blackbox;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.WatchlistStorage;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class WatchlistStorageBlackBoxTest {

    @MockBean
    private FilmStorage filmStorage;

    private WatchlistStorage watchlistStorage;
    private User testUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        watchlistStorage = new WatchlistStorage();
        testUser = new User();
        testUser.setUsername("testUser");
        
        testFilm = new Film();
        testFilm.setTitle("Test Movie");
        testFilm.setDirector("Test Director");
        testFilm.setGenre("Action");
        
        // Mock filmStorage behavior
        when(filmStorage.hasFilm("Test Movie")).thenReturn(true);
        when(filmStorage.hasFilm("Nonexistent Movie")).thenReturn(false);
        when(filmStorage.getAllFilms()).thenReturn(Arrays.asList(testFilm));
    }
    
    @Test
    void testIsInWatchlist_NonexistentMovie() {
        boolean result = watchlistStorage.isInWatchlist(testUser, "Nonexistent Movie");
        assertFalse(result, "Should return false for movie not in watchlist");
    }
    
    @Test
    void testRemoveFromWatchlist_NonexistentMovie() {
        boolean result = watchlistStorage.removeFromWatchlist(testUser, "Nonexistent Movie");
        assertFalse(result, "Should fail to remove nonexistent movie from watchlist");
    }

}