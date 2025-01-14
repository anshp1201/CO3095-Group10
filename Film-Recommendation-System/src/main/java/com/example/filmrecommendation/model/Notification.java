package com.example.filmrecommendation.model;

import java.io.Serializable;
import java.util.UUID;

public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;      
    private String title;
    private String message;
   // private String status;

    public Notification(String title, String message) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.message = message;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    /*
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    */
    
}

