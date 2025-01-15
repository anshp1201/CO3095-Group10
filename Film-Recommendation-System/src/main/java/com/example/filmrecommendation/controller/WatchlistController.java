package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.WatchlistStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WatchlistController {

    @Autowired
    private WatchlistStorage watchlistStorage;

    @GetMapping("/watchlist")
    public String getWatchlist(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Film> watchlist = watchlistStorage.getWatchlistForUser(loggedInUser);
        model.addAttribute("watchlistItems", watchlist);
        model.addAttribute("user", loggedInUser);
        
        return "watchlist";
    }

    @PostMapping("/api/watchlist/add")
    @ResponseBody
    public ResponseEntity<String> addToWatchlist(
            @RequestParam("movieTitle") String movieTitle, 
            HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        boolean added = watchlistStorage.addToWatchlist(loggedInUser, movieTitle);
        if (added) {
            return ResponseEntity.ok("Added to watchlist");
        } else {
            return ResponseEntity.badRequest().body("Movie already in watchlist or not found");
        }
    }

    @PostMapping("/api/watchlist/remove")
    @ResponseBody
    public ResponseEntity<String> removeFromWatchlist(
            @RequestParam("movieTitle") String movieTitle, 
            HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        boolean removed = watchlistStorage.removeFromWatchlist(loggedInUser, movieTitle);
        if (removed) {
            return ResponseEntity.ok("Removed from watchlist");
        } else {
            return ResponseEntity.badRequest().body("Movie not found in watchlist");
        }
    }
}