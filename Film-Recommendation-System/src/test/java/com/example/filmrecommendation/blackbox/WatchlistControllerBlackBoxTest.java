package com.example.filmrecommendation.blackbox;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.filmrecommendation.controller.WatchlistController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.WatchlistStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import java.util.Arrays;
import java.util.ArrayList;

@WebMvcTest(WatchlistController.class)
public class WatchlistControllerBlackBoxTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchlistStorage watchlistStorage;

    private MockHttpSession session;
    private User testUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        
        testFilm = new Film();
        testFilm.setTitle("Test Movie");
        
        session = new MockHttpSession();
        session.setAttribute("loggedInUser", testUser);
    }

    @Test
    void testGetWatchlist_UserLoggedIn() throws Exception {
        when(watchlistStorage.getWatchlistForUser(any(User.class)))
            .thenReturn(Arrays.asList(testFilm));

        mockMvc.perform(get("/watchlist").session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("watchlist"))
            .andExpect(model().attributeExists("watchlistItems"))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    void testGetWatchlist_UserNotLoggedIn() throws Exception {
        session = new MockHttpSession();

        mockMvc.perform(get("/watchlist").session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddToWatchlist_Success() throws Exception {
        when(watchlistStorage.addToWatchlist(any(User.class), anyString()))
            .thenReturn(true);

        mockMvc.perform(post("/api/watchlist/add")
                .param("movieTitle", "Test Movie")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(content().string("Added to watchlist"));
    }

    @Test
    void testAddToWatchlist_MovieAlreadyExists() throws Exception {
        when(watchlistStorage.addToWatchlist(any(User.class), anyString()))
            .thenReturn(false);

        mockMvc.perform(post("/api/watchlist/add")
                .param("movieTitle", "Test Movie")
                .session(session))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Movie already in watchlist or not found"));
    }

    @Test
    void testRemoveFromWatchlist_Success() throws Exception {
        when(watchlistStorage.removeFromWatchlist(any(User.class), anyString()))
            .thenReturn(true);

        mockMvc.perform(post("/api/watchlist/remove")
                .param("movieTitle", "Test Movie")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(content().string("Removed from watchlist"));
    }

    @Test
    void testRemoveFromWatchlist_MovieNotFound() throws Exception {
        when(watchlistStorage.removeFromWatchlist(any(User.class), anyString()))
            .thenReturn(false);

        mockMvc.perform(post("/api/watchlist/remove")
                .param("movieTitle", "Test Movie")
                .session(session))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Movie not found in watchlist"));
    }
}