package com.example.filmrecommendation.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.filmrecommendation.controller.GenrepreferenceController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

@SpringBootTest
public class GenrepreferenceControllerWhiteBoxTest {

    @Mock
    private FilmStorage filmStorage;

    @InjectMocks
    private GenrepreferenceController genreController;

    private Film testFilm1;
    private Film testFilm2;

    @BeforeEach
    void setUp() {
        testFilm1 = new Film();
        testFilm1.setTitle("Test Action Movie");
        testFilm1.setGenre("Action");
        
        testFilm2 = new Film();
        testFilm2.setTitle("Another Action Movie");
        testFilm2.setGenre("Action");
    }

    @Test
    void testGetMoviesByGenre_NullGenre() {
        when(filmStorage.getMoviesByGenre(null)).thenReturn(new ArrayList<>());
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetMoviesByGenre_EmptyGenre() {
        when(filmStorage.getMoviesByGenre("")).thenReturn(new ArrayList<>());
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre("");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetMoviesByGenre_SingleResult() {
        when(filmStorage.getMoviesByGenre("Action")).thenReturn(Arrays.asList(testFilm1));
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre("Action");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testFilm1.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void testGetMoviesByGenre_MultipleResults() {
        List<Film> actionFilms = Arrays.asList(testFilm1, testFilm2);
        when(filmStorage.getMoviesByGenre("Action")).thenReturn(actionFilms);
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre("Action");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetMoviesByGenre_StorageException() {
        when(filmStorage.getMoviesByGenre("Action"))
            .thenThrow(new RuntimeException("Storage error"));
        
        assertThrows(RuntimeException.class, () -> 
            genreController.getMoviesByGenre("Action"));
    }

    @Test
    void testGetMoviesByGenre_VerifyStorageInteraction() {
        when(filmStorage.getMoviesByGenre("Action"))
            .thenReturn(Arrays.asList(testFilm1, testFilm2));
        
        genreController.getMoviesByGenre("Action");
        verify(filmStorage, times(1)).getMoviesByGenre("Action");
    }

    @Test
    void testGetMoviesByGenre_ResponseConsistency() {
        List<Film> actionFilms = Arrays.asList(testFilm1, testFilm2);
        when(filmStorage.getMoviesByGenre("Action")).thenReturn(actionFilms);
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre("Action");
        assertEquals(actionFilms, response.getBody());
    }

    @Test
    void testGetMoviesByGenre_EmptyListResponse() {
        when(filmStorage.getMoviesByGenre("Unknown")).thenReturn(new ArrayList<>());
        
        ResponseEntity<List<Film>> response = genreController.getMoviesByGenre("Unknown");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}