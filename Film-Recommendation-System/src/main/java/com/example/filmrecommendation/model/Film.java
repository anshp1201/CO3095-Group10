package com.example.filmrecommendation.model;

import java.io.Serializable;
import java.util.List;



public class Film implements Serializable{
	private static final long serialVersionUID =1L;
	private String filmid;
	private String title;
	private String genre;
	private String director;
	private double averageRating;
	private int viewCount; 
	private List<String>actors;
	
	
	public Film() {}
	
	public Film(String filmid, String title, String genre, String director, double averageRating, int viewCount,  List<String> actors) {
		super();
		this.filmid = filmid;
		this.title = title;
		this.genre = genre;
		this.director = director;
		this.averageRating = averageRating;
		this.viewCount = 0;
		this.actors = actors;
	}
	
	public int getViewCount() {
		return viewCount;
    }
	
	 public void incrementViewCount() {
	        this.viewCount++;
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

	public List<String> getActors() {
		return actors;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public void setViewCount(int i) {
		// TODO Auto-generated method stub
		
	}
	
	

}


	
