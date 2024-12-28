package com.example.filmrecommendation.whitebox;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationWhiteBoxTest {
	private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmStorage();
    }

    @Test
    void testFilterLogicIncludesCorrectGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("Drama", List.of());
        assertTrue(recommendations.stream().allMatch(film -> film.getGenre().equalsIgnoreCase("Drama")));
    }

    @Test
    void testFilterLogicExcludesViewedFilms() {
        List<Film> recommendations = filmStorage.getRecommendations("Crime", List.of("The Godfather"));
        assertTrue(recommendations.stream().noneMatch(film -> film.getTitle().equals("The Godfather")));
    }

    @Test
    void testSortingLogic() {
        List<Film> recommendations = filmStorage.getRecommendations("Crime", List.of());
        assertEquals("The Godfather", recommendations.get(0).getTitle()); // Highest rated
        assertEquals("Pulp Fiction", recommendations.get(1).getTitle());
    }
    
    @Test
    void testNullGenre() {
        List<Film> recommendations = filmStorage.getRecommendations(null, List.of());
        assertTrue(recommendations.isEmpty(), "Recommendations should be empty when genre is null");
    }
    
    @Test
    void testEmptyFilmList() {
        FilmStorage emptyStorage = new FilmStorage();
        emptyStorage.getAllFilms().clear(); // Ensure no films are available
        List<Film> recommendations = emptyStorage.getRecommendations("Sci-Fi", List.of());
        assertTrue(recommendations.isEmpty(), "Recommendations should be empty when no films are in the storage");
    }
    
    @Test
    void testCaseInsensitiveGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("sci-fi", List.of());
        assertEquals(3, recommendations.size(), "Recommendations should be case-insensitive for genre");
    }
    
    @Test
    void testNoFilmsInStorage() {
        FilmStorage emptyStorage = new FilmStorage();
        emptyStorage.getAllFilms().clear(); // Clear all films

        List<Film> recommendations = emptyStorage.getRecommendations("Sci-Fi", List.of());
        assertTrue(recommendations.isEmpty(), "Recommendations should be empty when no films exist in storage");
    }
    
    @Test
    void testFilmsWithSameRating() {
        filmStorage.getAllFilms().add(new Film("Film A", "Sci-Fi", "Director A", 8.7));
        filmStorage.getAllFilms().add(new Film("Film B", "Sci-Fi", "Director B", 8.7));

        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertEquals(5, recommendations.size(), "All Sci-Fi films should be recommended");
        assertTrue(recommendations.get(0).getAverageRating() >= recommendations.get(1).getAverageRating(), 
                "Recommendations should maintain sorting even with identical ratings");
    }
    
    @Test
    void testSortingStability() {
        filmStorage.getAllFilms().add(new Film("Equal Rating A", "Sci-Fi", "Director A", 8.7));
        filmStorage.getAllFilms().add(new Film("Equal Rating B", "Sci-Fi", "Director B", 8.7));

        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertEquals("Inception", recommendations.get(0).getTitle());
        assertEquals("The Matrix", recommendations.get(1).getTitle());
        assertTrue(recommendations.stream().anyMatch(film -> 
            film.getTitle().equals("Equal Rating A") || film.getTitle().equals("Equal Rating B")),
            "Films with equal ratings should be included and maintain their relative order");
    }
    
    @Test
    void testRecommendationsWhenAllFilmsViewed() {
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", 
            List.of("Inception", "The Matrix", "Star Wars: Episode IV - A New Hope"));
        assertTrue(recommendations.isEmpty(), "No recommendations should be available when all films are viewed");
    }
    
   


    
   







}
	

