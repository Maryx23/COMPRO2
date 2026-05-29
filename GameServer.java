package com.Server;

import com.Game.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {
    private static final int PORT = 12345;
    private final Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private boolean gameRunning = false;
    private int currentPlayerIndex = 0;
    private final List<Integer> turnOrder = new ArrayList<>();
    private Board board;

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            int playerIdCounter = 1;
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this, playerIdCounter);
                clients.put(playerIdCounter, handler);
                handler.start();
                playerIdCounter++;
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    public synchronized void playerReady(int playerId) {
        ClientHandler handler = clients.get(playerId);
        if (handler == null) return;

        int activePlayersCount = 0;
        boolean allReady = true;

        for (ClientHandler client : clients.values()) {
            if (client.getUsername() != null) {
                activePlayersCount++;
                if (!client.getReady()) {
                    allReady = false;
                }
            }
        }

        if (allReady && activePlayersCount >= 2) {
            startGame();
        }
    }

    private synchronized void startGame() {
        gameRunning = true;
        board = new Board();
        turnOrder.clear();
        players.clear();

        turnOrder.addAll(clients.keySet());
        currentPlayerIndex = 0;

        for (int id : turnOrder) {
            ClientHandler client = clients.get(id);
            Player player = new Player(client.getUsername() != null ? client.getUsername() : "Player " + id);
            player.setId(id);
            player.setUsername(client.getUsername());
            players.put(id, player);
        }

        GameProtocol startMsg = new GameProtocol("GAME_START", "Game has started!");
        
        Map<String, Object> gameInitData = new HashMap<>();
        gameInitData.put("board", board);
        gameInitData.put("players", getPlayers());
        startMsg.setData(gameInitData);
        
        broadcast(startMsg);
        sendTurnNotification();
    }

    private void sendTurnNotification() {
        int currentId = getCurrentPlayer();
        GameProtocol turnMsg = new GameProtocol("TURN_CHANGE", "Player " + currentId + "'s turn");
        turnMsg.setData(getPlayers());
        broadcast(turnMsg);
    }

    public synchronized void broadcast(GameProtocol protocol) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(protocol);
        }
    }

    public synchronized void removePlayer(int playerId) {
        clients.remove(playerId);
        players.remove(playerId);
        turnOrder.remove(Integer.valueOf(playerId));
        if (clients.isEmpty()) {
            gameRunning = false;
        }
    }

    public synchronized boolean isGameRunning() {
        return gameRunning;
    }

    public synchronized int getCurrentPlayer() {
        if (turnOrder.isEmpty()) return -1;
        return turnOrder.get(currentPlayerIndex);
    }

    public int rollDice() {
        return new Random().nextInt(6) + 1;
    }

    public synchronized int movePlayer(int playerId, int roll) {
        Player player = players.get(playerId);
        if (player == null) return 1;

        player.move(roll);

        int currentPos = player.getCurrentPosition();
        int finalPos = board.getNewPosition(currentPos);
        
        if (finalPos != currentPos) {
            player.setCurrentPosition(finalPos);
        }

        return player.getCurrentPosition();
    }

    public synchronized boolean checkWin(int playerId) {
        Player player = players.get(playerId);
        return player != null && player.hasWon();
    }

    public synchronized void nextTurn() {
        if (turnOrder.isEmpty()) return;
        currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }

    public synchronized void endGame(int winnerId, String winnerUsername) {
        gameRunning = false;
        AuthSystem auth = new AuthSystem();
        Leaderboard leaderboard = new Leaderboard();

        for (Player p : players.values()) {
            if (p.getUsername() == null) continue;
            User user = auth.getUser(p.getUsername());
            if (user != null) {
                boolean won = p.getId() == winnerId;
                int finalScore = won ? 100 : p.getCurrentPosition();
                user.addGameResult(won, finalScore);
                auth.updateUser(user);
                leaderboard.updateUser(user);
            }
        }
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public synchronized void loadGameState(GameState state) {
        this.board = new Board();
        this.players.clear();
        this.turnOrder.clear();

        List<String> names = state.getPlayerNames();
        List<Integer> positions = state.getPlayerPositions();

        int index = 0;
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if (index >= names.size()) break;

            int id = entry.getKey();
            Player player = new Player(names.get(index));
            player.setId(id);
            player.setCurrentPosition(positions.get(index));
            player.setUsername(entry.getValue().getUsername());

            players.put(id, player);
            turnOrder.add(id);
            index++;
        }

        this.currentPlayerIndex = turnOrder.indexOf(state.getPlayerId());
        if (this.currentPlayerIndex == -1) {
            this.currentPlayerIndex = 0;
        }
        this.gameRunning = true;

        GameProtocol loadMsg = new GameProtocol("GAME_START", "Saved game loaded!");
        Map<String, Object> gameInitData = new HashMap<>();
        gameInitData.put("board", board);
        gameInitData.put("players", getPlayers());
        loadMsg.setData(gameInitData);
        broadcast(loadMsg);

        sendTurnNotification();
    }
}