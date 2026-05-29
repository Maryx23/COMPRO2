package com.Game;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Random random;
    private int lastRoll;
    
    public Dice() {
        this.random = new Random();
        this.lastRoll = 1;
    }
    
    public int roll() {
        lastRoll = random.nextInt(6) + 1;
        return lastRoll;
    }
    
    public int getLastRoll() {
        return lastRoll;
    }
    
    public String toString() {
        return "Dice roll: " + lastRoll;
    }
}