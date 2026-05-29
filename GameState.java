package com.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private List<String> playerNames;
    private List<Integer> playerPositions;
    private int currentPlayerIndex;
    private long timestamp;
    private int playerId;
    private String winner;
    private boolean gameOver;

    public GameState() {
        this.playerNames = new ArrayList<>();
        this.playerPositions = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
        this.gameOver = false;
    }

    public GameState(String username, Player[] players, int currentPlayerIndex) {
        this.username = username;
        this.playerNames = new ArrayList<>();
        this.playerPositions = new ArrayList<>();
        for (Player player : players) {
            if (player != null) {
                this.playerNames.add(player.getName());
                this.playerPositions.add(player.getCurrentPosition());
            }
        }
        this.currentPlayerIndex = currentPlayerIndex;
        this.timestamp = System.currentTimeMillis();
        this.gameOver = false;
    }

    public String getUsername()                             { return username; }
    public void setUsername(String username)                { this.username = username; }

    public List<String> getPlayerNames()                    { return playerNames; }
    public void setPlayerNames(List<String> playerNames)    { this.playerNames = playerNames; }

    public List<Integer> getPlayerPositions()               { return playerPositions; }
    public void setPlayerPositions(List<Integer> p)         { this.playerPositions = p; }

    public int getCurrentPlayerIndex()                      { return currentPlayerIndex; }
    public void setCurrentPlayerIndex(int i)                { this.currentPlayerIndex = i; }

    public long getTimestamp()                              { return timestamp; }
    public void setTimestamp(long timestamp)                { this.timestamp = timestamp; }

    public int getPlayerId()                                { return playerId; }
    public void setPlayerId(int playerId)                   { this.playerId = playerId; }

    public String getWinner()                               { return winner; }
    public void setWinner(String winner)                    { this.winner = winner; }

    public boolean isGameOver()                             { return gameOver; }
    public void setGameOver(boolean gameOver)               { this.gameOver = gameOver; }

    public String toString() {
        return "GameState{username='" + username + "', players=" + playerNames +
               ", positions=" + playerPositions + ", currentPlayer=" + currentPlayerIndex +
               ", gameOver=" + gameOver + "}";
    }
}