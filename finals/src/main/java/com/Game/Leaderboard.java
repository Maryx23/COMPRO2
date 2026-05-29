package com.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<User> leaderboard;
    private String leaderboardFile;
    
    public Leaderboard() {
        this.leaderboard = new ArrayList<>();
        this.leaderboardFile = "src/main/resources/leaderboard.dat";
        loadLeaderboard();
    }
    
    @SuppressWarnings("unchecked")
    private void loadLeaderboard() {
        File file = new File(leaderboardFile);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                leaderboard = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                leaderboard = new ArrayList<>();
            }
        }
    }
    
    private void saveLeaderboard() {
        try {
            File file = new File(leaderboardFile);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(leaderboard);
            }
        } catch (IOException e) {
            System.out.println("Error saving leaderboard");
        }
    }
    
    public void updateUser(User user) {
        boolean found = false;
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).getUsername().equals(user.getUsername())) {
                leaderboard.set(i, user);
                found = true;
                break;
            }
        }
        if (!found) {
            leaderboard.add(user);
        }
        saveLeaderboard();
    }
    
    public List<User> getTopPlayers() {
        leaderboard.sort((u1, u2) -> Integer.compare(u2.getGamesWon(), u1.getGamesWon()));
        return leaderboard.subList(0, Math.min(10, leaderboard.size()));
    }
}