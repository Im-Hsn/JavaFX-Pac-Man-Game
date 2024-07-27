package com.example.oop2project;

public class Player {
    private int id, highScore;
    private String username, email, password, role;

    public Player(int id, int highScore, String username, String email, String role) {
        this.id = id;
        this.highScore = highScore;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public int getHighScore() {
        return highScore;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
