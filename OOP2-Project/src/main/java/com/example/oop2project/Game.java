package com.example.oop2project;

import java.sql.Connection;
import java.time.LocalDateTime;

public class Game {
    private int id, playerId, score=0, levelReached;
    private LocalDateTime dateTime;

    public Game(int playerId, int score, int levelReached, LocalDateTime dateTime) {
        this.playerId = playerId;
        this.score = score;
        this.levelReached = levelReached;
        this.dateTime = dateTime;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getScore() {
        return score;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }


}
