package midterms;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class Server {
    private static final Gson gson = new Gson();
    private static final int TOTAL_ROUNDS = 10;
    private static final String[] MOVES = {"Rock", "Paper", "Scissors"};

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Server started. Waiting for 2 players...");

        Socket socket1 = serverSocket.accept();
        System.out.println("Player 1 connected.");
        Socket socket2 = serverSocket.accept(); 
        System.out.println("Player 2 connected.");

        BufferedReader p1In = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        BufferedReader p2In = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        PrintWriter p1Out = new PrintWriter(socket1.getOutputStream(), true);
        PrintWriter p2Out = new PrintWriter(socket2.getOutputStream(), true);

        send(p1Out, "PROMPT", "Enter your name:");
        send(p2Out, "PROMPT", "Enter your name:");
        String p1Name = receive(p1In);
        String p2Name = receive(p2In);

        Player player1 = new Player(p1Name, "P1");
        Player player2 = new Player(p2Name, "P2");
        Round match = new Round(player1, player2);

        send(p1Out, "INFO", "Welcome " + p1Name + "! Opponent: " + p2Name + ". Game starting");
        send(p2Out, "INFO", "Welcome " + p2Name + "! Opponent: " + p1Name + ". Game starting");
        send(p1Out, "INFO", "Moves: 0 = Rock | 1 = Paper | 2 = Scissors");
        send(p2Out, "INFO", "Moves: 0 = Rock | 1 = Paper | 2 = Scissors");

        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            send(p1Out, "PROMPT", " Round " + round + "/" + TOTAL_ROUNDS + " Please Enter move (0/1/2):");
            send(p2Out, "PROMPT", " Round " + round + "/" + TOTAL_ROUNDS + " Please Enter move (0/1/2):");

            int p1Move = parseMove(receive(p1In));
            int p2Move = parseMove(receive(p2In));

            String p1MoveName = MOVES[p1Move];
            String p2MoveName = MOVES[p2Move];

            String roundWinner = determineWinner(p1Move, p2Move, player1, player2);
            match.addRoundResult(new Result(round, p1MoveName, p2MoveName, roundWinner));

            if (roundWinner.equals(p1Name)) {
                send(p1Out, "Result", "You played " + p1MoveName + " vs " + p2MoveName + " | You won this round!");
                send(p2Out, "Result", "You played " + p2MoveName + " vs " + p1MoveName + " | You lost this round!");
            } else if (roundWinner.equals(p2Name)) {
                send(p1Out, "Result", "You played " + p1MoveName + " vs " + p2MoveName + " | You lost this round!");
                send(p2Out, "Result", "You played " + p2MoveName + " vs " + p1MoveName + " | You win this round!");
            } else {
                send(p1Out, "Result", "You played " + p1MoveName + " vs " + p2MoveName + " | Draw!");
                send(p2Out, "Result", "You played " + p2MoveName + " vs " + p1MoveName + " | Draw!");
            }

            send(p1Out, "SCORE", "Score -> You: " + player1.getWins() + " | Opponent: " + player2.getWins());
            send(p2Out, "SCORE", "Score -> You: " + player2.getWins() + " | Opponent: " + player1.getWins());
        }

        String matchWinner;
        if (player1.getWins() > player2.getWins()) {
            matchWinner = player1.getName();
        } else if (player2.getWins() > player1.getWins()) {
            matchWinner = player2.getName();
        } else {
            matchWinner = "Draw";
        }

        match.setMatchWinner(matchWinner);
        FileHandler.saveRound(match);

        String leaderboard = buildLeaderboard(player1, player2, matchWinner);
        send(p1Out, "Game Over", leaderboard);
        send(p2Out, "Game Over", leaderboard);
        System.out.println(leaderboard);

        socket1.close();
        socket2.close();
        serverSocket.close();
    }

    private static String determineWinner(int p1, int p2, Player player1, Player player2) {
        if (p1 == p2) {
            player1.addDraw();
            player2.addDraw();
            return "Draw";
        }
        if ((p1 == 0 && p2 == 2) || (p1 == 1 && p2 == 0) || (p1 == 2 && p2 == 1)) {
            player1.addWin();
            player2.addLoss();
            return player1.getName();
        }
        player2.addWin();
        player1.addLoss();
        return player2.getName();
    }

    private static int parseMove(String input) {
        try {
            int move = Integer.parseInt(input.trim());
            if (move >= 0 && move <= 2) {
                return move;
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    private static String buildLeaderboard(Player p1, Player p2, String winner) {
        return "\n    LEADERBOARD "
             + "\n1. " + p1.getName() + " | Wins: " + p1.getWins() + " | Loses: " + p1.getLoses() + " | Draws: " + p1.getDraws()
             + "\n2. " + p2.getName() + " | Wins: " + p2.getWins() + " | Loses: " + p2.getLoses() + " | Draws: " + p2.getDraws()
             + "\nMatch Winner: " + winner;
    }

    private static void send(PrintWriter out, String type, String content) {
        out.println(gson.toJson(new GameChat(type, content)));
        out.flush(); 
    }

    private static String receive(BufferedReader in) throws IOException {
        return gson.fromJson(in.readLine(), GameChat.class).getContent();
    }
}