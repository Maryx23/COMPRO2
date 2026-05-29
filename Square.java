package com.Game;

import java.io.Serializable;

public class Square implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int position;
    private int nextPosition;
    private String type;
    
    public Square(int position) {
        this.position = position;
        this.nextPosition = position;
        this.type = "normal";
    }
    
    public int getPosition() {
        return position;
    }
    
    public int getNextPosition() {
        return nextPosition;
    }
    
    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String toString() {
        if (type.equals("snake")) {
            return "S " + String.format("%3d", position);
        } else if (type.equals("ladder")) {
            return "L " + String.format("%3d", position);
        }
        return ". " + String.format("%3d", position);
    }
}