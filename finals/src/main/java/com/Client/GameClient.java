package com.Client;

import com.Game.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class GameClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Object outputLock = new Object();
    private Scanner scanner;
    private int playerId;
    private String username;
    private String pendingUsername;
    private volatile boolean gameRunning = false;
    private volatile boolean clientConnected = true;
    private volatile boolean myTurn = false;
    private final Object turnLock = new Object();
    private Board board;
    private List<Player> playersList = new ArrayList<>();

    public GameClient() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to game server");

            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.setDaemon(true);
            receiveThread.start();

            showAuthMenu();

        } catch (IOException e) {
            System.out.println("Cannot connect to server. Make sure server is running.");
        }
    }

    private void sendToServer(GameProtocol protocol) {
        synchronized (outputLock) {
            try {
                output.writeObject(protocol);
                output.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to server: " + e.getMessage());
            }
        }
    }

    private void showAuthMenu() {
        while (true) {
            System.out.println("\n=========================================");
            System.out.println("        SNAKES AND LADDERS GAME         ");
            System.out.println("=========================================");
            System.out.println("    1. Login                            ");
            System.out.println("    2. Register                         ");
            System.out.println("    3. Exit                             ");
            System.out.println("=========================================");
            System.out.print("Choose option: ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                if (login()) {
                    showGameMenu();
                    break;
                }
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                System.exit(0);
            }
        }
    }

    private boolean login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        this.pendingUsername = username;
        sendToServer(new GameProtocol("LOGIN", username + ":" + password));

        synchronized (this) {
            long startTime = System.currentTimeMillis();
            while (this.username == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    long remaining = 5000 - (System.currentTimeMillis() - startTime);
                    if (remaining <= 0) break;
                    wait(remaining);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        if (this.username != null) {
            System.out.println("Login successful! Welcome " + username);
            return true;
        } else {
            System.out.println("Login failed! Invalid credentials.");
            return false;
        }
    }

    private void register() {
        System.out.print("Choose username: ");
        String username = scanner.nextLine();
        System.out.print("Choose password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String confirm = scanner.nextLine();

        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match!");
            return;
        }

        sendToServer(new GameProtocol("REGISTER", username + ":" + password));
    }

    private void showGameMenu() {
        while (true) {
            System.out.println("\nGAME MENU");
            System.out.println("1. Start New Game");
            System.out.println("2. Load Saved Game");
            System.out.println("3. Save Current Game");
            System.out.println("4. View Leaderboard");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                this.gameRunning = true;
                startGame();
                playGameLoop();
                break;
            } else if (choice == 2) {
                this.gameRunning = true;
                loadGame();
                playGameLoop();
                break;
            } else if (choice == 3) {
                saveGame();
            } else if (choice == 4) {
                viewLeaderboard();
            } else if (choice == 5) {
                logout();
                break;
            }
        }
    }

    private void startGame() {
        sendToServer(new GameProtocol("READY", ""));
        System.out.println("Waiting for other player to be ready...");
    }

    private void playGameLoop() {
        while (gameRunning && clientConnected) {
            synchronized (turnLock) {
                while (!myTurn && gameRunning && clientConnected) {
                    try {
                        turnLock.wait(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (!gameRunning || !clientConnected) break;

            try {
                if (System.in.available() > 0) {
                    while (System.in.available() > 0) {
                        System.in.read();
                    }
                    this.scanner = new Scanner(System.in);
                }
            } catch (IOException ignored) {
            }

            System.out.print("Your turn! Press Enter to roll dice... ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("save")) {
                saveGame();
            } else if (input.equalsIgnoreCase("exit")) {
                logout();
                break;
            } else {
                myTurn = false;
                sendToServer(new GameProtocol("ROLL_DICE", ""));
            }
        }
    }

    private void saveGame() {
        sendToServer(new GameProtocol("SAVE_GAME", ""));
    }

    private void loadGame() {
        sendToServer(new GameProtocol("LOAD_GAME", ""));
    }

    private void viewLeaderboard() {
        sendToServer(new GameProtocol("GET_LEADERBOARD", ""));
    }

    private void logout() {
        sendToServer(new GameProtocol("DISCONNECT", ""));
        this.username = null;
        this.pendingUsername = null;
        this.playerId = -1;
        this.gameRunning = false;
        showAuthMenu();
    }

    @SuppressWarnings("unchecked")
    private void receiveMessages() {
        try {
            while (clientConnected) {
                GameProtocol protocol = (GameProtocol) input.readObject();
                if (protocol == null) break;

                switch (protocol.getType()) {

                    case "LOGIN_RESPONSE":
                        if (protocol.getMessage().equals("SUCCESS")) {
                            this.username = this.pendingUsername;
                            this.playerId = protocol.getPlayerId();
                        } else {
                            this.pendingUsername = null;
                        }
                        synchronized (this) {
                            notifyAll();
                        }
                        break;

                    case "REGISTER_RESPONSE":
                        if (protocol.getMessage().equals("SUCCESS")) {
                            System.out.println("Registration successful! Please login.");
                        } else {
                            System.out.println("Registration failed! Username may already exist.");
                        }
                        break;

                    case "GAME_START":
                        this.gameRunning = true;
                        Map<String, Object> initData = (Map<String, Object>) protocol.getData();
                        this.board = (Board) initData.get("board");
                        this.playersList = (List<Player>) initData.get("players");
                        this.board.display(this.playersList);
                        System.out.println("Game started! You are Player " + playerId);
                        break;

                    case "TURN_CHANGE":
                        if (protocol.getData() instanceof List) {
                            this.playersList = (List<Player>) protocol.getData();
                        }
                        System.out.println("\n" + protocol.getMessage());
                        if (protocol.getMessage().contains("Player " + playerId)) {
                            synchronized (turnLock) {
                                myTurn = true;
                                turnLock.notifyAll();
                            }
                        }
                        break;

                    case "DICE_RESULT":
                        if (protocol.getData() instanceof List) {
                            this.playersList = (List<Player>) protocol.getData();
                        }
                        System.out.println("\n" + protocol.getMessage());
                        System.out.println("New position: " + protocol.getNewPosition());
                        if (this.board != null) {
                            this.board.display(this.playersList);
                        }
                        break;

                    case "MOVE":
                        System.out.println(protocol.getMessage());
                        break;

                    case "GAME_OVER":
                        System.out.println("\n=========================================");
                        System.out.println("         GAME OVER! " + protocol.getWinner() + " WINS!");
                        System.out.println("=========================================");
                        gameRunning = false;
                        synchronized (turnLock) {
                            turnLock.notifyAll();
                        }
                        showGameMenu();
                        return;

                    case "LEADERBOARD_DATA":
                        List<User> topPlayers = (List<User>) protocol.getData();
                        System.out.println("\nLEADERBOARD");
                        System.out.println("=========================================");
                        System.out.printf("%-3s %-15s %-8s %-6s %-10s%n",
                                "#", "Username", "Played", "Won", "Win Rate");
                        System.out.println("=========================================");
                        for (int i = 0; i < topPlayers.size(); i++) {
                            User user = topPlayers.get(i);
                            System.out.printf("%-3d %-15s %-8d %-6d %-9.1f%%%n",
                                    (i + 1), user.getUsername(), user.getGamesPlayed(),
                                    user.getGamesWon(), user.getWinRate());
                        }
                        System.out.println("=========================================\n");
                        break;

                    case "SAVE_RESPONSE":
                        System.out.println(protocol.getMessage());
                        break;

                    case "LOAD_RESPONSE":
                        System.out.println(protocol.getMessage());
                        break;

                    case "ERROR":
                        System.out.println("Error: " + protocol.getMessage());
                        break;

                    case "INFO":
                        System.out.println("Info: " + protocol.getMessage());
                        break;

                    default:
                        System.out.println("Unknown message type: " + protocol.getType());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected from server");
            clientConnected = false;
            gameRunning = false;
            synchronized (turnLock) {
                turnLock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        client.start();
    }
}