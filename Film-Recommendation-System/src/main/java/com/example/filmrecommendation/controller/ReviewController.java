package com.example.filmrecommendation.controller;

import com.example.filmrecommendation.model.Review;
import com.example.filmrecommendation.service.ReviewStorage;
import com.example.filmrecommendation.service.UserStorage;
import com.example.filmrecommendation.service.FilmStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewStorage reviewStorage;
    
    @Autowired
    private UserStorage userStorage;
    
    @Autowired
    private FilmStorage filmStorage;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        // Validate user exists
        if (userStorage.getUserByUsername(review.getUsername()) == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Validate film exists
        boolean filmExists = filmStorage.getAllFilms().stream()
                .anyMatch(film -> film.getTitle().equals(review.getFilmTitle()));
        if (!filmExists) {
            return ResponseEntity.badRequest().body("Film not found");
        }

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }

        reviewStorage.addReview(review);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/film/{filmTitle}")
    public ResponseEntity<List<Review>> getFilmReviews(@PathVariable String filmTitle) {
        return ResponseEntity.ok(reviewStorage.getReviewsByFilm(filmTitle));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable String username) {
        return ResponseEntity.ok(reviewStorage.getReviewsByUser(username));
    }
}
