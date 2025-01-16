package com.example.filmrecommendation.blackbox;

import com.example.filmrecommendation.controller.SocialSharingController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest 
@AutoConfigureMockMvc 
public class SocialSharingControllerBlackBoxTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmStorage filmStorage;  

    @MockBean
    private UserStorage userStorage;  

    @Autowired
    private SocialSharingController controller; 

    private User loggedInUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        loggedInUser = new User();
        loggedInUser.setUsername("testUser");

        testFilm = new Film();
        testFilm.setFilmid("123");
        testFilm.setTitle("Test Film");
    }

    @Test
    void testGetShare_FilmExists() throws Exception {
       
        when(filmStorage.getFilm("123")).thenReturn(testFilm);

        
        mockMvc.perform(get("/share")
                .param("FilmID", "123")
                .sessionAttr("loggedInUser", loggedInUser))  
                .andExpect(status().isOk())
                .andExpect(view().name("share"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("film"))
                .andExpect(model().attribute("user", loggedInUser))
                .andExpect(model().attribute("film", testFilm));

        
        verify(filmStorage, times(1)).getFilm("123");
    }

    @Test
    void testGetShare_FilmNotFound() throws Exception {
        
        when(filmStorage.getFilm("123")).thenReturn(null);

        
        mockMvc.perform(get("/share")
                .param("FilmID", "123")
                .sessionAttr("loggedInUser", loggedInUser))  
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Film not found for FilmID: 123"));

        
        verify(filmStorage, times(1)).getFilm("123");
    }
}
