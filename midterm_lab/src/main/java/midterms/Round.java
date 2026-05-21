package midterms;

import java.util.ArrayList;

public class Round {
    private Player player1;
    private Player player2;
    private String matchWinner;
    private ArrayList<Result> rounds;

    public Round(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rounds = new ArrayList<>();
    }

    public void addRoundResult(Result result) {
        rounds.add(result);
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public ArrayList<Result> getRounds() {
        return rounds;
    }
}