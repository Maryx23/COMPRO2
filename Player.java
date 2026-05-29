package com.Game;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int currentPosition;
    private int id;
    private String username;

    public Player(String name) {
        this.name = name;
        this.currentPosition = 1;
    }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public int getCurrentPosition()             { return currentPosition; }
    public void setCurrentPosition(int position){ this.currentPosition = position; }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getUsername()                 { return username; }
    public void setUsername(String username)    { this.username = username; }

    public void move(int steps) {
        currentPosition += steps;
        if (currentPosition > 100) {
            currentPosition = 100 - (currentPosition - 100);
        }
        if (currentPosition < 1) {
            currentPosition = 1;
        }
    }

    public boolean hasWon() {
        return currentPosition == 100;
    }

    public String toString() {
        return name + " at position " + currentPosition;
    }
}