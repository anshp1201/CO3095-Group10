package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        
        // Add both user and films to the model
        List<String> genres = Arrays.asList("Action", "Comedy", "Sci-Fi", "Romance");
        model.addAttribute("user", loggedInUser);
        model.addAttribute("films", filmStorage.getAllFilms());
        model.addAttribute("genres", genres);
        
        return "dashboard";
    }
}
