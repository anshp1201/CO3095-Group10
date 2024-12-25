package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.Film;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmStorage {
	private final List<Film> films;
	
	
	public FilmStorage() {
		films = new ArrayList<>();
		
		films.add(new Film("Incetion", "Sci-Fi", "Christopher Nolan", 9.0));
		films.add(new Film("The Dark Knight", "Action", "Christopher Nolan", 9.0));
        films.add(new Film("Titanic", "Romance", "James Cameron", 7.9));
        films.add(new Film("Interstellar", "Sci-Fi", "Christopher Nolan", 8.6));
	}
	
	public List<Film> getAllFilms() {
		return films;
	}
	
	public List<Film> getRecommendations(String favoriteGenre, List<String> viewedFilms) {
		return films.stream()
				.filter(film -> film.getGenre().equals(favoriteGenre) && viewedFilms.contains(film.getTitle()))
				.sorted((f1, f2) -> Double.compare(f2.getAverageRating(), f1.getAverageRating()))
				.collect(Collectors.toList());

	}

}
