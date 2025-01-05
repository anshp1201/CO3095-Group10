package com.example.filmrecommendation.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.model.User;
import com.example.filmrecommendation.service.FilmStorage;
import com.example.filmrecommendation.service.UserStorage;

import jakarta.servlet.http.HttpSession;


@Controller
public class SocialSharingController{
	
	@Autowired
	private FilmStorage filmStorage;
	
	@Autowired
	private UserStorage userStorage;
	

	 @RequestMapping("/share")
	    public String getShare(HttpSession session, Model model, @RequestParam("FilmID") String FilmID) {
	        User loggedInUser = (User) session.getAttribute("loggedInUser");

	        Film currentFilm = filmStorage.getFilm(FilmID);

	        if (currentFilm == null) {
	            model.addAttribute("error", "Film not found for FilmID: " + FilmID);
	            return "error"; 
	        }

	        model.addAttribute("user", loggedInUser);
	        model.addAttribute("film", currentFilm);  

	        return "share"; 
	   }
	
}

