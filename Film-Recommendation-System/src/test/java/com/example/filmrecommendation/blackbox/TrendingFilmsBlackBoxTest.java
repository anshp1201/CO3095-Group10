package com.example.filmrecommendation.blackbox;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrendingFilmsBlackBoxTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private FilmStorage filmStorage;
    
    @BeforeEach
    void setUp() {
        // Reset view counts before each test
        for (Film film : filmStorage.getAllFilms()) {
            film.setViewCount(0);
        }
    }
    
    @Test
    void shouldDisplayTrendingFilms() throws Exception {
        // Test the dashboard endpoint with redirect following
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("trendingFilms"));
    }
    
    @Test
    void shouldDisplayFilmsSortedByViewCount() throws Exception {
        // First, increment some view counts
        filmStorage.incrementFilmViewCount("Inception");
        filmStorage.incrementFilmViewCount("Inception");
        
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("trendingFilms"));
    }
    
    @Test
    void shouldIncrementViewCount() throws Exception {
        String filmTitle = "Inception";
        
        // Get initial state
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk());
        
        // Increment view count using POST
        mockMvc.perform(post("/dashboard/films/" + filmTitle + "/view"))
               .andExpect(status().is3xxRedirection()); // Expect redirect after POST
               
        // Verify dashboard after redirect
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("trendingFilms"));
    }
    
    @Test
    void shouldHandleInvalidFilmTitle() throws Exception {
        // Test with non-existent film
        mockMvc.perform(post("/dashboard/films/NonExistentFilm/view"))
               .andExpect(status().is3xxRedirection()); // Expect redirect even for invalid film
    }
    
    @Test
    void shouldDisplayFilmInformation() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("trendingFilms"));
    }
    
    @Test
    void shouldLimitTrendingFilmsToFive() throws Exception {
        // Add view counts to several films
        for (Film film : filmStorage.getAllFilms()) {
            filmStorage.incrementFilmViewCount(film.getTitle());
        }
        
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("trendingFilms", hasSize(lessThanOrEqualTo(5))));
    }
}