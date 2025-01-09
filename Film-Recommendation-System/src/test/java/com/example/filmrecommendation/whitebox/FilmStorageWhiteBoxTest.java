package com.example.filmrecommendation.whitebox;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

import static org.junit.jupiter.api.Assertions.*;

public class FilmStorageWhiteBoxTest {
	 private FilmStorage filmStorage;

	    @BeforeEach
	    void setUp() {
	        filmStorage = new FilmStorage();
	    }

	    @Test
	    void testFilterByTitleOnly() {
	        // Title contains "Inception"
	        List<Film> results = filmStorage.searchFilms("Inception");
	        assertEquals(1, results.size());
	        assertTrue(results.get(0).getTitle().contains("Inception"));
	    }

	    @Test
	    void testFilterByDirectorOnly() {
	        // Director contains "Christopher Nolan"
	        List<Film> results = filmStorage.searchFilms("Christopher Nolan");
	        assertEquals(3, results.size());
	        results.forEach(film -> assertTrue(film.getDirector().contains("Christopher Nolan")));
	    }

	    @Test
	    void testFilterByGenreOnly() {
	        // Genre contains "Sci-Fi"
	        List<Film> results = filmStorage.searchFilms("Sci-Fi");
	        assertEquals(4, results.size());
	        results.forEach(film -> assertTrue(film.getGenre().contains("Sci-Fi")));
	    }

	    @Test
	    void testNoResultsForUnknownQuery() {
	        // No match for "Unknown Movie"
	        List<Film> results = filmStorage.searchFilms("Unknown Movie");
	        assertTrue(results.isEmpty());
	    }

	    @Test
	    void testAllConditionsHit() {
	        // Ensure all branches are tested
	        List<Film> resultsByTitle = filmStorage.searchFilms("Inception");
	        assertEquals(1, resultsByTitle.size());

	        List<Film> resultsByDirector = filmStorage.searchFilms("Christopher Nolan");
	        assertEquals(3, resultsByDirector.size());

	        List<Film> resultsByGenre = filmStorage.searchFilms("Sci-Fi");
	        assertEquals(4, resultsByGenre.size());
	    }

	    @Test
	    void testCaseInsensitiveSearch() {
	        // Test for case insensitivity
	        List<Film> results = filmStorage.searchFilms("sci-fi");
	        assertEquals(4, results.size());
	    }
	}

