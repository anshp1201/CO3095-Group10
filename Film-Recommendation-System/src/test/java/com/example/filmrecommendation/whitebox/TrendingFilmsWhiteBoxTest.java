package com.example.filmrecommendation.whitebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TrendingFilmsWhiteBoxTest {
    
    private FilmStorage filmStorage;
    
    @BeforeEach
    void setUp() {
        filmStorage = new FilmStorage();
        resetAllViewCounts();
    }
    
    private void resetAllViewCounts() {
        for(Film film : filmStorage.getAllFilms()) {
            film.setViewCount(0);
        }
    }

    @Test
    void shouldReturnEmptyTrendingListWhenNoViews() {
        List<Film> trending = filmStorage.getTrendingFilms();
        assertTrue(trending.stream().allMatch(film -> film.getViewCount() == 0),
                "All films should have zero views initially");
    }

    @Test
    void shouldSortByViewCountDescending() {
        // Set up different view counts
        incrementViewCount("Inception", 5);
        incrementViewCount("The Dark Knight", 3);
        incrementViewCount("Titanic", 4);
        
        List<Film> trending = filmStorage.getTrendingFilms();
        
        assertEquals("Inception", trending.get(0).getTitle(), "Most viewed film should be first");
        assertEquals("Titanic", trending.get(1).getTitle(), "Second most viewed film should be second");
        assertEquals("The Dark Knight", trending.get(2).getTitle(), "Third most viewed film should be third");
    }

    @Test
    void shouldLimitToFiveFilms() {
        // Give views to more than 5 films
        incrementViewCount("Inception", 6);
        incrementViewCount("The Dark Knight", 5);
        incrementViewCount("Titanic", 4);
        incrementViewCount("Interstellar", 3);
        incrementViewCount("The Godfather", 2);
        incrementViewCount("Pulp Fiction", 1);
        
        List<Film> trending = filmStorage.getTrendingFilms();
        
        assertTrue(trending.size() <= 5, "Should not return more than 5 films");
        assertEquals(5, trending.size(), "Should return exactly 5 films when more are available");
    }

    @Test
    void shouldHandleTiesInViewCount() {
        // Create a tie in view counts
        incrementViewCount("Inception", 3);
        incrementViewCount("The Dark Knight", 3);
        
        List<Film> trending = filmStorage.getTrendingFilms();
        
        assertEquals(3, trending.get(0).getViewCount(), "First film should have 3 views");
        assertEquals(3, trending.get(1).getViewCount(), "Second film should have 3 views");
    }

    @Test 
    void shouldUpdateTrendingWhenViewCountChanges() {
        // Initial state
        incrementViewCount("Inception", 2);
        List<Film> initialTrending = filmStorage.getTrendingFilms();
        String initialTopFilm = initialTrending.get(0).getTitle();
        
        // Update view count to change order
        incrementViewCount("The Dark Knight", 3);
        List<Film> updatedTrending = filmStorage.getTrendingFilms();
        
        assertNotEquals(initialTopFilm, updatedTrending.get(0).getTitle(),
                "Top trending film should change after view count update");
        assertEquals("The Dark Knight", updatedTrending.get(0).getTitle(),
                "Film with most views should be first");
    }

   

    private void incrementViewCount(String title, int times) {
        for(int i = 0; i < times; i++) {
            filmStorage.incrementFilmViewCount(title);
        }
    }
}