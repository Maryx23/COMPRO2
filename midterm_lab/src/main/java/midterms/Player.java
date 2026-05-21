package midterms;

public class Player {
    private String name;
    private String playerID;
    private int wins;
    private int loses;
    private int draws;

    public Player(String name, String playerId) {
        this.name = name;
        this.playerID = playerId;
        this.wins = 0;
        this.loses = 0;
        this.draws = 0;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        loses++;
    }

    public void addDraw() {
        draws++;
    }

    public String getName() {
        return name;
    }

    public String getplayerID() {
        return playerID;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDraws() {
        return draws;
    }

    @Override
    public String toString() {
        return name + " (ID: " + playerID + ") | Wins: " + wins + " | Losses: " + loses + " | Draws: " + draws;
    }
}