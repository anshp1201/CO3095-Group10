package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {
    
    @Autowired
    private FilmStorage filmStorage;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check for logged in user
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        // Add user data to model
        model.addAttribute("user", loggedInUser);
        
        // Add films data
        List<Film> allFilms = filmStorage.getAllFilms();
        model.addAttribute("films", allFilms);
        
        // Add trending films
        List<Film> trendingFilms = filmStorage.getTrendingFilms();
        model.addAttribute("trendingFilms", trendingFilms);
        
        // Add genres list
        List<String> genres = Arrays.asList("Action", "Romance", "Sci-Fi", "Comedy", "Drama", "Crime", "Thriller", "Biography");
        model.addAttribute("genres", genres);
        
        return "dashboard";
    }
}