package com.example.filmrecommendation.blackbox;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class RecommendationBlackBoxTest {
	private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmStorage();
    }

    @Test
    void testGetRecommendationsByGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertEquals(4, recommendations.size());
        assertTrue(recommendations.stream().allMatch(film -> film.getGenre().equalsIgnoreCase("Sci-Fi")));
    }

    @Test
    void testRecommendationsExcludeViewedFilms() {
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of("Inception"));
        assertEquals(3, recommendations.size());
        assertTrue(recommendations.stream().noneMatch(film -> film.getTitle().equals("Inception")));
    }

    @Test
    void testRecommendationsSortedByRating() {
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertEquals("Inception", recommendations.get(0).getTitle()); 
        assertEquals("The Matrix", recommendations.get(1).getTitle());
    }

    @Test
    void testNoRecommendationsForInvalidGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("Horror", List.of());
        assertTrue(recommendations.isEmpty());
    }
    
    @Test
    void testEmptyViewedFilms() {
        List<Film> recommendations = filmStorage.getRecommendations("Drama", List.of());
        assertEquals(3, recommendations.size(), "All drama films should be recommended if no films are viewed");
    }
    
    @Test
    void testMultiGenreFilms() {
        filmStorage.getAllFilms().add(new Film("Film C", "Sci-Fi,Action", "Director C", 8.5));
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertTrue(recommendations.stream().anyMatch(film -> film.getTitle().equals("Film C")),
                "Multi-genre films should still appear in recommendations");
    }
    
    @Test
    void testUnknownGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("Western", List.of());
        assertTrue(recommendations.isEmpty(), "Recommendations should be empty for unknown genres");
    }
    
    @Test
    void testRecommendationsForNonexistentUser() {
        User nonexistentUser = new User();
        List<Film> recommendations = filmStorage.getRecommendations(nonexistentUser.getFavoriteGenre(), List.of());
        assertTrue(recommendations.isEmpty(), "No recommendations should be available for nonexistent user");
    }
    
    @Test
    void testRecommendationsForUserWithNoFavoriteGenre() {
        User userWithoutGenre = new User();
        List<Film> recommendations = filmStorage.getRecommendations(userWithoutGenre.getFavoriteGenre(), List.of());
        assertTrue(recommendations.isEmpty(), "No recommendations should be available if user's favorite genre is null");
    }
    @Test
    void testRecommendationsForMultiGenreFilm() {
        filmStorage.getAllFilms().add(new Film("Crossover Film", "Sci-Fi,Action", "Director X", 8.5));
        List<Film> recommendations = filmStorage.getRecommendations("Sci-Fi", List.of());
        assertTrue(recommendations.stream().anyMatch(film -> 
            film.getTitle().equals("Crossover Film")), "Films with matching genres should be recommended");
    }
    
    @Test
    void testRecommendationsForNonexistentGenre() {
        List<Film> recommendations = filmStorage.getRecommendations("Fantasy", List.of());
        assertTrue(recommendations.isEmpty(), "No recommendations should be available for nonexistent genre");
    }



    
  



    
   
}