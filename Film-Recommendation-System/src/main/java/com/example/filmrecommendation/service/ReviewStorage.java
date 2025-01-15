package com.example.filmrecommendation.service;

import com.example.filmrecommendation.model.Review;
import com.example.filmrecommendation.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrecommendation.service.NotificationStorage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewStorage {
    private static final String FILE_PATH = "reviews.dat";
    private List<Review> reviews;
    
    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private NotificationStorage notificationStorage;

    public ReviewStorage() {
        reviews = loadReviews();
    }

    @SuppressWarnings("unchecked")
    private List<Review> loadReviews() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Review>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveReviews() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(reviews);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addReview(Review review) {
        reviews.add(review);
        updateFilmRating(review.getFilmTitle());
        saveReviews();

        try {
			notificationStorage.addNotification("New Review Added"," ");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public List<Review> getReviewsByFilm(String filmTitle) {
        return reviews.stream()
                .filter(review -> review.getFilmTitle().equals(filmTitle))
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsByUser(String username) {
        return reviews.stream()
                .filter(review -> review.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    private void updateFilmRating(String filmTitle) {
        List<Review> filmReviews = getReviewsByFilm(filmTitle);
        if (!filmReviews.isEmpty()) {
            double averageRating = filmReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            Film film = filmStorage.getAllFilms().stream()
                    .filter(f -> f.getTitle().equals(filmTitle))
                    .findFirst()
                    .orElse(null);
            
            if (film != null) {
                film.setAverageRating(averageRating);
            }
        }
    }
}
