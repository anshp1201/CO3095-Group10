package com.example.filmrecommendation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Collection implements Serializable{
private static final long serialVersionUID = 1L;
    
    private String username;
    private String collectionName;
    private List<String> movieTitles;
    
    //Initializes an empty list of movies
    public Collection() {
        this.movieTitles = new ArrayList<>();
    }
    
    
    public Collection(String username, String collectionName) {
        this.username = username;
        this.collectionName = collectionName;
        this.movieTitles = new ArrayList<>();
    }

    //Getters and setters
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getCollectionName() {
		return collectionName;
	}


	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}


	public List<String> getMovieTitles() {
		return movieTitles;
	}


	public void setMovieTitles(List<String> movieTitles) {
		this.movieTitles = movieTitles;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
