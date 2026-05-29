package com.Game;

import java.io.Serializable;

public class GameProtocol implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String type;
    private String message;
    private Object data;
    private int playerId;
    private int diceRoll;
    private int newPosition;
    private boolean gameOver;
    private String winner;
    
    public GameProtocol() {}
    
    public GameProtocol(String type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    public int getDiceRoll() {
        return diceRoll;
    }
    
    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }
    
    public int getNewPosition() {
        return newPosition;
    }
    
    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public void setWinner(String winner) {
        this.winner = winner;
    }
}