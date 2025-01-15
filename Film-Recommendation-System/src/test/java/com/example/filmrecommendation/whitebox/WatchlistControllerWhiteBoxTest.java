package com.example.filmrecommendation.whitebox;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.filmrecommendation.controller.WatchlistController;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.WatchlistStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class WatchlistControllerWhiteBoxTest {

    @Mock
    private WatchlistStorage watchlistStorage;

    @Mock
    private Model model;

    @InjectMocks
    private WatchlistController watchlistController;

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
    }

    @Test
    void testGetWatchlist_SessionNullUser() {
        String result = watchlistController.getWatchlist(session, model);
        assertEquals("redirect:/login", result, "Should redirect to login for null user");
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void testGetWatchlist_EmptyWatchlist() {
        session.setAttribute("loggedInUser", testUser);
        when(watchlistStorage.getWatchlistForUser(testUser))
            .thenReturn(new ArrayList<>());

        String result = watchlistController.getWatchlist(session, model);
        assertEquals("watchlist", result);
        verify(model).addAttribute("watchlistItems", new ArrayList<>());
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void testGetWatchlist_StorageException() {
        session.setAttribute("loggedInUser", testUser);
        when(watchlistStorage.getWatchlistForUser(testUser))
            .thenThrow(new RuntimeException("Storage error"));

        assertThrows(RuntimeException.class, 
            () -> watchlistController.getWatchlist(session, model));
    }

    @Test
    void testAddToWatchlist_NullMovieTitle() {
        session.setAttribute("loggedInUser", testUser);
        ResponseEntity<String> response = watchlistController.addToWatchlist(null, session);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(watchlistStorage, never()).addToWatchlist(any(), any());
    }

    @Test
    void testAddToWatchlist_EmptyMovieTitle() {
        session.setAttribute("loggedInUser", testUser);
        ResponseEntity<String> response = watchlistController.addToWatchlist("", session);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(watchlistStorage, never()).addToWatchlist(any(), any());
    }

    @Test
    void testAddToWatchlist_StorageException() {
        session.setAttribute("loggedInUser", testUser);
        when(watchlistStorage.addToWatchlist(any(), any()))
            .thenThrow(new RuntimeException("Storage error"));

        ResponseEntity<String> response = watchlistController.addToWatchlist("Test Movie", session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRemoveFromWatchlist_ValidMovieTitle() {
        session.setAttribute("loggedInUser", testUser);
        when(watchlistStorage.removeFromWatchlist(testUser, "Test Movie"))
            .thenReturn(true);

        ResponseEntity<String> response = watchlistController.removeFromWatchlist("Test Movie", session);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed from watchlist", response.getBody());
        verify(watchlistStorage).removeFromWatchlist(testUser, "Test Movie");
    }

    @Test
    void testRemoveFromWatchlist_StorageException() {
        session.setAttribute("loggedInUser", testUser);
        when(watchlistStorage.removeFromWatchlist(any(), any()))
            .thenThrow(new RuntimeException("Storage error"));

        ResponseEntity<String> response = watchlistController.removeFromWatchlist("Test Movie", session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testSessionHandling_Consistency() {
        session.setAttribute("loggedInUser", testUser);
        
        watchlistController.addToWatchlist("Movie1", session);
        watchlistController.addToWatchlist("Movie2", session);
        
        User sessionUser = (User) session.getAttribute("loggedInUser");
        assertEquals(testUser.getUsername(), sessionUser.getUsername(), 
            "Session should maintain user consistency");
    }

    @Test
    void testModelAttributes_Consistency() {
        session.setAttribute("loggedInUser", testUser);
        List<Film> watchlist = Arrays.asList(testFilm);
        when(watchlistStorage.getWatchlistForUser(testUser)).thenReturn(watchlist);

        watchlistController.getWatchlist(session, model);
        
        verify(model).addAttribute("watchlistItems", watchlist);
        verify(model).addAttribute("user", testUser);
        verify(model, times(2)).addAttribute(anyString(), any()); // Should have exactly 2 attributes
    }
}