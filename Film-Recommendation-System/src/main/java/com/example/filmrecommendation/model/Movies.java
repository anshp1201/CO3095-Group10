package com.example.filmrecommendation.model;

public class Movies {
    private String title;
    private String genre;
    private String director;
    private double averageRating;

    public Movies(String title, String genre, String director, double averageRating) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.averageRating = averageRating;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

    
}
