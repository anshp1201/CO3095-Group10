package com.example.filmrecommendation.blackbox;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

import static org.junit.jupiter.api.Assertions.*;

public class FilmStorageBlackBoxTest {
	    private FilmStorage filmStorage;

	    @BeforeEach
	    void setUp() {
	        filmStorage = new FilmStorage();
	    }

	    @Test
	    void testSearchByTitle() {
	        List<Film> results = filmStorage.searchFilms("Inception");
	        assertEquals(1, results.size());
	        assertEquals("Inception", results.get(0).getTitle());
	    }

	    @Test
	    void testSearchByDirector() {
	        List<Film> results = filmStorage.searchFilms("Christopher Nolan");
	        assertEquals(3, results.size());
	    }

	    @Test
	    void testSearchByGenre() {
	        List<Film> results = filmStorage.searchFilms("Sci-Fi");
	        assertEquals(4, results.size());
	    }

	    @Test
	    void testEmptyQuery() {
	        List<Film> results = filmStorage.searchFilms("");
	        assertTrue(results.isEmpty());
	    }

	    @Test
	    void testNullQuery() {
	        List<Film> results = filmStorage.searchFilms(null);
	        assertTrue(results.isEmpty());
	   }
}




