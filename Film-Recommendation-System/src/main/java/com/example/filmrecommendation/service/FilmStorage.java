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
		
		films.add(new Film("Inception", "Sci-Fi", "Christopher Nolan", 9.0));
		films.add(new Film("The Dark Knight", "Action", "Christopher Nolan", 9.0));
        films.add(new Film("Titanic", "Romance", "James Cameron", 7.9));
        films.add(new Film("Interstellar", "Sci-Fi", "Christopher Nolan", 8.6));
        films.add(new Film("The Godfather", "Crime", "Francis Ford Coppola", 9.2));
        films.add(new Film("Pulp Fiction", "Crime", "Quentin Tarantino", 8.9));
        films.add(new Film("The Shawshank Redemption", "Drama", "Frank Darabont", 9.3));
        films.add(new Film("Forrest Gump", "Drama", "Robert Zemeckis", 8.8));
        films.add(new Film("The Matrix", "Sci-Fi", "The Wachowskis", 8.7));
        films.add(new Film("Schindler's List", "Biography", "Steven Spielberg", 9.0));
        films.add(new Film("Goodfellas", "Crime", "Martin Scorsese", 8.7));
        films.add(new Film("The Silence of the Lambs", "Thriller", "Jonathan Demme", 8.6));
        films.add(new Film("Star Wars: Episode IV - A New Hope", "Sci-Fi", "George Lucas", 8.6));
        films.add(new Film("One Flew Over the Cuckoo's Nest", "Drama", "Milos Forman", 8.7));
        films.add(new Film("The hangover", "Comedy", "Todd Phillip", 4.6));
        
	}
	
	public List<Film> getAllFilms() {
		return films;
	}
	
	public List<Film> getRecommendations(String favoriteGenre, List<String> viewedFilms) {
	    return films.stream()
	            .filter(film -> film.getGenre().equalsIgnoreCase(favoriteGenre) && !viewedFilms.contains(film.getTitle()))
	            .sorted((f1, f2) -> Double.compare(f2.getAverageRating(), f1.getAverageRating()))
	            .collect(Collectors.toList());
	}
	public List<Film> getMoviesByGenre(String genre) {
        return films.stream()
                .filter(film -> film.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }


}
