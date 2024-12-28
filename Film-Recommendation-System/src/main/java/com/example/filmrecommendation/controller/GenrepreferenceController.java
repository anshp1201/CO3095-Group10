package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenrepreferenceController {

    private final FilmStorage filmStorage;

    public GenrepreferenceController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping("/genres/{genre}")
    public ResponseEntity<List<Film>> getMoviesByGenre(@PathVariable("genre") String genre) {
        List<Film> filmsByGenre = filmStorage.getMoviesByGenre(genre);
        if (filmsByGenre.isEmpty()) {
            // Return 404 if no films are found for the given genre
            return ResponseEntity.notFound().build();
        }
        // Return 200 OK with the list of films
        return ResponseEntity.ok(filmsByGenre);
    }
}
