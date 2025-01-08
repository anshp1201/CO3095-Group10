package com.example.filmrecommendation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.filmrecommendation.service.FilmStorage;

@RestController
@RequestMapping("/api")
public class FilmController {
    @Autowired
    private FilmStorage filmStorage;

    @PostMapping("/films/{title}/view")
    public void trackFilmView(@PathVariable String title) {
        filmStorage.incrementFilmViewCount(title);
    }
}
