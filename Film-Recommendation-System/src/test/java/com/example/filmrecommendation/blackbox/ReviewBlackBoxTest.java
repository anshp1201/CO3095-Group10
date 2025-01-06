package com.example.filmrecommendation.test;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BlackBoxTest {

    @Autowired
    private FilmStorage filmStorage;

    @Test
    public void testGetAllFilms() {
        // Test that getAllFilms returns the expected list of films
        List<Film> films = filmStorage.getAllFilms();
        
        // Check if films list is not null and not empty
        assertNotNull(films);
        assertFalse(films.isEmpty());
        
        // Check if specific films exist
        boolean hasInception = films.stream()
            .anyMatch(film -> film.getTitle().equals("Inception"));
        assertTrue(hasInception, "Inception should be in the film list");
    }

    @Test
    public void testGetRecommendations() {
        // Test the film recommendation functionality
        String favoriteGenre = "Sci-Fi";
        List<String> viewedFilms = Arrays.asList("Inception");
        
        List<Film> recommendations = filmStorage.getRecommendations(favoriteGenre, viewedFilms);
        
        // Check if recommendations are not null
        assertNotNull(recommendations);
        
        // Verify recommendations don't include viewed films
        boolean hasNoViewedFilms = recommendations.stream()
            .noneMatch(film -> viewedFilms.contains(film.getTitle()));
        assertTrue(hasNoViewedFilms, "Recommendations should not include viewed films");
        
        // Verify all recommendations are of the favorite genre
        boolean allCorrectGenre = recommendations.stream()
            .allMatch(film -> film.getGenre().equalsIgnoreCase(favoriteGenre));
        assertTrue(allCorrectGenre, "All recommendations should be of the favorite genre");
    }

    @Test
    public void testRecommendationsOrder() {
        // Test that recommendations are ordered by rating
        String genre = "Crime";
        List<String> viewed = Arrays.asList();
        
        List<Film> recommendations = filmStorage.getRecommendations(genre, viewed);
        
        // Check if list is ordered by rating (descending)
        for (int i = 0; i < recommendations.size() - 1; i++) {
            assertTrue(
                recommendations.get(i).getAverageRating() >= 
                recommendations.get(i + 1).getAverageRating(),
                "Films should be ordered by rating in descending order"
            );
        }
    }
}
