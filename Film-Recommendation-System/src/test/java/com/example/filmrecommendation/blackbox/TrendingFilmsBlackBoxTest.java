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
    

}