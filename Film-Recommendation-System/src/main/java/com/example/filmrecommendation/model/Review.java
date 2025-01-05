package com.example.filmrecommendation.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String filmTitle;
    private String content;
    private int rating;
    private LocalDateTime createdAt;

    public Review() {}

    public Review(String username, String filmTitle, String content, int rating) {
        this.username = username;
        this.filmTitle = filmTitle;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
