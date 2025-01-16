package com.example.filmrecommendation.whitebox;

import com.example.filmrecommendation.controller.SocialSharingController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SocialSharingControllerWhiteBoxTest {

    @Mock
    private FilmStorage filmStorage; 

    @Mock
    private UserStorage userStorage; 

    @Mock
    private HttpSession session; 

    @Mock
    private Model model;  

    @InjectMocks  
    private SocialSharingController controller;

    private User loggedInUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        loggedInUser = new User();
        loggedInUser.setUsername("testUser");

        testFilm = new Film();
        testFilm.setFilmid("123");
        testFilm.setTitle("Test Film");
    }

    @Test
    void testGetShare_FilmExists() {
        
        when(session.getAttribute("loggedInUser")).thenReturn(loggedInUser);
        when(filmStorage.getFilm("123")).thenReturn(testFilm);

        
        String viewName = controller.getShare(session, model, "123");

        
        verify(filmStorage, times(1)).getFilm("123");
        verify(model).addAttribute("user", loggedInUser);
        verify(model).addAttribute("film", testFilm);

        
        assertEquals("share", viewName);
    }

    @Test
    void testGetShare_FilmNotFound() {
        
        when(session.getAttribute("loggedInUser")).thenReturn(loggedInUser);
        when(filmStorage.getFilm("123")).thenReturn(null);

        
        String viewName = controller.getShare(session, model, "123");

        
        verify(filmStorage, times(1)).getFilm("123");
        verify(model).addAttribute("error", "Film not found for FilmID: 123");

       
        assertEquals("error", viewName);
    }
}
