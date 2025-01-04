package com.example.filmrecommendation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.filmrecommendation.model.Collection;
import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.CollectionStorage;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.UserStorage;

import jakarta.servlet.http.HttpSession;



@Controller
public class RecommendationController {
	
	@Autowired
	private FilmStorage filmStorage;
	
	@Autowired
	private UserStorage userStorage;
	
	@Autowired
	private CollectionStorage collectionStorage;
	
	@GetMapping("/films")
    public String getAllFilms(HttpSession session, Model model) {
        List<Film> films = filmStorage.getAllFilms();
        model.addAttribute("films", films);
        return "films"; 
    }
	
	@GetMapping("/recommendations")
	public String getRecommendations(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/login";
			
		}
		
		String favoriteGenre = loggedInUser.getFavoriteGenre();
		List<String> viewedFilms = List.of();
		
		List<Film> recommendations = filmStorage.getRecommendations(favoriteGenre, viewedFilms);
		
		List<Collection> userCollections = collectionStorage.getUserCollections(loggedInUser.getUsername());
		
		model.addAttribute("user", loggedInUser);
		model.addAttribute("recommendations", recommendations);
		model.addAttribute("collections", userCollections);
		
		return "recommendations";
		
	}
	

}
