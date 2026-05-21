package midterms;

public class Result {
    private int round;
    private String player1Move;
    private String player2Move;
    private String result;

    public Result(int round, String player1Move, String player2Move, String result) {
        this.round = round;
        this.player1Move = player1Move;
        this.player2Move = player2Move;
        this.result = result;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getPlayer1Move() {
        return player1Move;
    }

    public void setPlayer1Move(String player1Move) {
        this.player1Move = player1Move;
    }

    public String getPlayer2Move() {
        return player2Move;
    }

    public void setPlayer2Move(String player2Move) {
        this.player2Move = player2Move;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Round " + round + " | Player1: " + player1Move + " vs Player2: " + player2Move + " | Winner: " + result;
    }
}