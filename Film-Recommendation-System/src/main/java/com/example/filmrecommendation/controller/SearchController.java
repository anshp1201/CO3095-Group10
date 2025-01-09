package com.example.filmrecommendation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;

@Controller
public class SearchController {
	
	@Autowired
	private FilmStorage filmStorage;
	
	@GetMapping("/search")
	public String searchFilms(@RequestParam("query") String query, Model model) {
		List<Film> searchResults = filmStorage.searchFilms(query);
		model.addAttribute("films", searchResults);
		model.addAttribute("query", query);
		return "searchResults";
		
		
	}
	

}
