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
		

		films.add(new Film("1","Inception", "Sci-Fi", "Christopher Nolan", 9.0));
		films.add(new Film("2","The Dark Knight", "Action", "Christopher Nolan", 9.0));
        films.add(new Film("3","Titanic", "Romance", "James Cameron", 7.9));
        films.add(new Film("4","Interstellar", "Sci-Fi", "Christopher Nolan", 8.6));
        films.add(new Film("5","The Godfather", "Crime", "Francis Ford Coppola", 9.2));
        films.add(new Film("6","Pulp Fiction", "Crime", "Quentin Tarantino", 8.9));
        films.add(new Film("7","The Shawshank Redemption", "Drama", "Frank Darabont", 9.3));
        films.add(new Film("8","Forrest Gump", "Drama", "Robert Zemeckis", 8.8));
        films.add(new Film("9","The Matrix", "Sci-Fi", "The Wachowskis", 8.7));
        films.add(new Film("10","Schindler's List", "Biography", "Steven Spielberg", 9.0));
        films.add(new Film("11","Goodfellas", "Crime", "Martin Scorsese", 8.7));
        films.add(new Film("12","The Silence of the Lambs", "Thriller", "Jonathan Demme", 8.6));
        films.add(new Film("13","Star Wars: Episode IV - A New Hope", "Sci-Fi", "George Lucas", 8.6));
        films.add(new Film("14","One Flew Over the Cuckoo's Nest", "Drama", "Milos Forman", 8.7));
        
	}


	public List<Film> getTrendingFilms() {
		return films.stream()
			.sorted((f1, f2) -> Integer.compare(f2.getViewCount(), f1.getViewCount()))
                        .limit(5)  // Get top 5 trending films
                        .collect(Collectors.toList());
    }

        public void incrementFilmViewCount(String title) {
		films.stream()
			.filter(film -> film.getTitle().equals(title))
			.findFirst()
			.ifPresent(Film::incrementViewCount);
    }

	
	public List<Film> getAllFilms() {
		return films;
	}
	
	public boolean hasFilm(String movieTitle) {
	    System.out.println("Checking if film exists: " + movieTitle);
	    boolean exists = films.stream()
	            .anyMatch(film -> film.getTitle().equals(movieTitle));
	    System.out.println("Film exists: " + exists);
	    return exists;
	}
	
	public List<Film> getRecommendations(String favoriteGenre, List<String> viewedFilms) {
	    return films.stream()
	            .filter(film -> film.getGenre().equalsIgnoreCase(favoriteGenre) && !viewedFilms.contains(film.getTitle()))
	            .sorted((f1, f2) -> Double.compare(f2.getAverageRating(), f1.getAverageRating()))
	            .collect(Collectors.toList());
	}
  

	
	public Film getFilm(String FilmID) {
		
		for(Film x : films) {
			
			String xID = x.getFilmid();
			
			if(FilmID.equals(xID)) {
				return x;
			}
			
		}
		
		return null;
		
	}

	public List<Film> getMoviesByGenre(String genre) {
        return films.stream()
                .filter(film -> film.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }



}
