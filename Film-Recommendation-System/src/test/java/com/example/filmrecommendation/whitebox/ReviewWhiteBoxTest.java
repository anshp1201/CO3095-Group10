package com.example.filmrecommendation.whitebox;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewWhiteBoxTest {

    private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmStorage();
    }



    @Test
    void testGetRecommendationsLogic() {
        // Test the internal logic of getRecommendations method
        String genre = "Sci-Fi";
        List<String> viewed = Arrays.asList("Inception", "Interstellar");
        
        List<Film> recommendations = filmStorage.getRecommendations(genre, viewed);
        
        // Check filtering logic
        for (Film film : recommendations) {
            assertTrue(film.getGenre().equalsIgnoreCase(genre), 
                "Each film should be of the requested genre");
            assertFalse(viewed.contains(film.getTitle()), 
                "Recommendations should not include viewed films");
        }
    }

    @Test
    void testEmptyViewedList() {
        // Test recommendation behavior with empty viewed list
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", Arrays.asList());
        
        // Should return all Sci-Fi films
        long scifiCount = filmStorage.getAllFilms().stream()
            .filter(f -> f.getGenre().equalsIgnoreCase("Sci-Fi"))
            .count();
            
        assertEquals(scifiCount, recommendations.size(),
            "Should return all Sci-Fi films when no films are viewed");
    }

    @Test
    void testNonexistentGenre() {
        // Test behavior with a genre that doesn't exist
        List<Film> recommendations = filmStorage.getRecommendations("NonexistentGenre", Arrays.asList());
        
        assertTrue(recommendations.isEmpty(),
            "Should return empty list for nonexistent genre");
    }
}
