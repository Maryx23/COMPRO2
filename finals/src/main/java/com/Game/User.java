package com.Game;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private int gamesPlayed;
    private int gamesWon;
    private int totalScore;
    
    public User() {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.totalScore = 0;
    }
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.totalScore = 0;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    
    public int getGamesWon() {
        return gamesWon;
    }
    
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) gamesWon / gamesPlayed * 100;
    }
    
    public void addGameResult(boolean won, int score) {
        gamesPlayed++;
        if (won) {
            gamesWon++;
        }
        totalScore += score;
    }
    
    public String toString() {
        return username + " | Played: " + gamesPlayed + " | Won: " + gamesWon + 
               " | Win Rate: " + String.format("%.1f", getWinRate()) + "%";
    }
}