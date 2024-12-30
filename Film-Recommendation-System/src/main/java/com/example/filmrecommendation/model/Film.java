package com.example.filmrecommendation.model;

import java.io.Serializable;



public class Film implements Serializable{
	private static final long serialVersionUID =1L;
	private String filmid;
	private String title;
	private String genre;
	private String director;
	private double averageRating;
	
	
	public Film() {}
	
	public Film(String filmid, String title, String genre, String director, double averageRating) {
		super();
		this.filmid = filmid;
		this.title = title;
		this.genre = genre;
		this.director = director;
		this.averageRating = averageRating;
	}
	
	public String getFilmid() {
		return filmid;
	}
	
	public void setFilmid(String filmid) {
		this.filmid = filmid;
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


	
