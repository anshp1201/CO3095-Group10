package com.example.filmrecommendation.blackbox;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.filmrecommendation.controller.GenrepreferenceController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GenrepreferenceController.class)
public class GenrepreferenceControllerBlackBoxTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmStorage filmStorage;

    @Autowired
    private ObjectMapper objectMapper;

    private Film testFilm1;
    private Film testFilm2;

    @BeforeEach
    void setUp() {
        testFilm1 = new Film();
        testFilm1.setTitle("Test Action Movie");
        testFilm1.setGenre("Action");
        testFilm1.setDirector("Test Director");

        testFilm2 = new Film();
        testFilm2.setTitle("Another Action Movie");
        testFilm2.setGenre("Action");
        testFilm2.setDirector("Another Director");
    }

    @Test
    void testGetMoviesByGenre_ExistingGenre() throws Exception {
        List<Film> actionFilms = Arrays.asList(testFilm1, testFilm2);
        when(filmStorage.getMoviesByGenre("Action")).thenReturn(actionFilms);

        mockMvc.perform(get("/genres/Action"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(actionFilms)));
    }

    @Test
    void testGetMoviesByGenre_NonExistentGenre() throws Exception {
        when(filmStorage.getMoviesByGenre("NonExistent")).thenReturn(new ArrayList<>());

   
        mockMvc.perform(get("/genres/NonExistent"))

            .andExpect(status().isNotFound());
    }

    @Test
    void testGetMoviesByGenre_CaseInsensitive() throws Exception {

        List<Film> actionFilms = Arrays.asList(testFilm1, testFilm2);
        when(filmStorage.getMoviesByGenre("action")).thenReturn(actionFilms);

        mockMvc.perform(get("/genres/action"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(actionFilms)));
    }

    @Test
    void testGetMoviesByGenre_WithSpaces() throws Exception {
        Film sciFiFilm = new Film();
        sciFiFilm.setTitle("Sci-Fi Movie");
        sciFiFilm.setGenre("Sci-Fi");
        when(filmStorage.getMoviesByGenre("Sci-Fi")).thenReturn(Arrays.asList(sciFiFilm));

        mockMvc.perform(get("/genres/Sci-Fi"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(sciFiFilm))));
    }
}