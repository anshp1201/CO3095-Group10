package com.example.filmrecommendation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.filmrecommendation.model.Film;
import com.example.filmrecommendation.service.FilmStorage;


@RestController
@RequestMapping("/api")
public class FilmController {
    @Autowired
    private FilmStorage filmStorage;
   
    
    @GetMapping("/films/trending")
    public List<Film> getTrendingFilms() {
        return filmStorage.getTrendingFilms();
    }
    
    @PostMapping("/films/{title}/view")
    public ResponseEntity<Void> trackFilmView(@PathVariable String title) {
        try {
            filmStorage.incrementFilmViewCount(title);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
//    @PostMapping("/films/{title}/view")
//    public void trackFilmView(@PathVariable String title) {
//        filmStorage.incrementFilmViewCount(title);
//    }
}
