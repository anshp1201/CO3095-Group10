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
    void testNoFilmsInStorage() {
        FilmStorage emptyStorage = new FilmStorage();
        emptyStorage.getAllFilms().clear(); // Clear all films

        List<Film> recommendations = emptyStorage.getRecommendations("Sci-Fi", List.of());
        assertTrue(recommendations.isEmpty(), "Recommendations should be empty when no films exist in storage");
    }
    
}
	

