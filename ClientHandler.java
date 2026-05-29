package com.Server;

import com.Game.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Object outputLock = new Object();
    private final GameServer server;
    private final int playerId;
    private String username;
    private volatile boolean ready;

    public ClientHandler(Socket socket, GameServer server, int playerId) {
        this.socket = socket;
        this.server = server;
        this.playerId = playerId;
        this.ready = false;
    }

    @Override
    public void run() {
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error setting up streams for player " + playerId + ": " + e.getMessage());
            cleanup();
            return;
        }

        try {
            while (!Thread.currentThread().isInterrupted()) {
                GameProtocol protocol = (GameProtocol) input.readObject();
                if (protocol == null)
                    break;

                switch (protocol.getType()) {
                    case "LOGIN":
                        handleLogin(protocol);
                        break;
                    case "REGISTER":
                        handleRegister(protocol);
                        break;
                    case "ROLL_DICE":
                        handleRollDice();
                        break;
                    case "READY":
                        this.ready = true;
                        server.playerReady(playerId);
                        break;
                    case "GET_LEADERBOARD":
                        sendLeaderboard();
                        break;
                    case "SAVE_GAME":
                        handleSaveGame();
                        break;
                    case "LOAD_GAME":
                        handleLoadGame();
                        break;
                    case "DISCONNECT":
                        return;
                    default:
                        System.out.println("Unknown protocol type from player " + playerId
                                + ": " + protocol.getType());
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerId + " disconnected: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Protocol serialization error with player " + playerId + ": " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handleLogin(GameProtocol protocol) {
        String[] credentials = protocol.getMessage().split(":", 2);

        if (credentials.length < 2) {
            sendError("Invalid login format");
            return;
        }

        String targetUsername = credentials[0];
        String password = credentials[1];

        AuthSystem auth = new AuthSystem();
        boolean success = auth.authenticate(targetUsername, password);

        GameProtocol response = new GameProtocol("LOGIN_RESPONSE", success ? "SUCCESS" : "FAIL");
        if (success) {
            this.username = targetUsername;
            response.setPlayerId(playerId);
            System.out.println("Player " + playerId + " logged in as " + this.username);
        } else {
            System.out.println("Failed login attempt for username: " + targetUsername);
        }

        sendMessage(response);
    }

    private void handleRegister(GameProtocol protocol) {
        String[] credentials = protocol.getMessage().split(":", 2);

        if (credentials.length < 2) {
            sendError("Invalid registration format");
            return;
        }

        String targetUsername = credentials[0];
        String password = credentials[1];

        AuthSystem auth = new AuthSystem();
        boolean success = auth.register(targetUsername, password);

        GameProtocol response = new GameProtocol("REGISTER_RESPONSE", success ? "SUCCESS" : "FAIL");
        sendMessage(response);

        if (success) {
            System.out.println("New user registered: " + targetUsername);
        }
    }

    private void handleRollDice() {
        if (username == null) {
            sendError("You must log in before taking actions.");
            return;
        }

        if (!server.isGameRunning()) {
            sendError("Game is not running");
            return;
        }

        if (server.getCurrentPlayer() != playerId) {
            sendError("Not your turn!");
            return;
        }

        int roll = server.rollDice();
        int newPosition = server.movePlayer(playerId, roll);

        GameProtocol response = new GameProtocol("DICE_RESULT", "Player " + username + " rolled: " + roll);
        response.setDiceRoll(roll);
        response.setNewPosition(newPosition);
        response.setPlayerId(playerId);
        response.setData(server.getPlayers());
        server.broadcast(response);

        if (server.checkWin(playerId)) {
            GameProtocol winMessage = new GameProtocol("GAME_OVER", "Player " + playerId + " wins!");
            winMessage.setWinner(username);
            winMessage.setGameOver(true);
            server.broadcast(winMessage);
            server.endGame(playerId, username);
        } else {
            server.nextTurn();
            GameProtocol turnMessage = new GameProtocol("TURN_CHANGE",
                    "Player " + server.getCurrentPlayer() + "'s turn");
            turnMessage.setData(server.getPlayers());
            server.broadcast(turnMessage);
        }
    }

    private void handleSaveGame() {
        if (username == null) {
            sendError("You must log in to save a game.");
            return;
        }

        if (!server.isGameRunning()) {
            sendError("No game in progress to save");
            return;
        }

        Player[] playersArray = server.getPlayers().toArray(new Player[0]);
        GameState state = new GameState(username, playersArray, server.getCurrentPlayer());
        state.setPlayerId(playerId);

        try {
            File saveDir = new File("data/saves");
            if (!saveDir.exists())
                saveDir.mkdirs();

            File saveFile = new File(saveDir, "save_game_" + username + ".dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oos.writeObject(state);
            }

            sendMessage(new GameProtocol("SAVE_RESPONSE", "Game saved successfully!"));
            System.out.println("Game saved for user: " + username);
        } catch (IOException e) {
            sendError("Failed to save game: " + e.getMessage());
        }
    }

    private void handleLoadGame() {
        if (username == null) {
            sendError("You must log in to load a game.");
            return;
        }

        File saveFile = new File("data/saves/save_game_" + username + ".dat");

        if (!saveFile.exists()) {
            sendError("No saved game found!");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameState loadedState = (GameState) ois.readObject();

            if (!loadedState.getUsername().equals(username)) {
                sendError("Save file does not belong to this user!");
                return;
            }

            server.loadGameState(loadedState);

            GameProtocol response = new GameProtocol("LOAD_RESPONSE", "Game loaded successfully!");
            response.setData(loadedState);
            sendMessage(response);
            System.out.println("Game loaded for user: " + username);

        } catch (IOException | ClassNotFoundException e) {
            sendError("Failed to load game: " + e.getMessage());
        }
    }

    private void sendLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        List<User> topPlayers = leaderboard.getTopPlayers();

        GameProtocol response = new GameProtocol("LEADERBOARD_DATA", "");
        response.setData(topPlayers);
        sendMessage(response);
    }

    private void sendError(String errorMessage) {
        sendMessage(new GameProtocol("ERROR", errorMessage));
    }

    public void sendMessage(GameProtocol protocol) {
        synchronized (outputLock) {
            if (output == null) return;
            try {
                output.reset();
                output.writeObject(protocol);
                output.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to player " + playerId + ": " + e.getMessage());
            }
        }
    }

    private void cleanup() {
        server.removePlayer(playerId);
        try {
            if (input != null) input.close();
        } catch (IOException ignored) {}
        try {
            if (output != null) output.close();
        } catch (IOException ignored) {}
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {}
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public boolean getReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}