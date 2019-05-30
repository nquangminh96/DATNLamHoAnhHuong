package com.example.datn20182;

public class User {
    private String imageURL;
    private String username;

    public User() {
    }

    public User(String imageURL, String username) {
        this.imageURL = imageURL;
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
