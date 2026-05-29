package com.Game;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private Square[] squares;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    private static final int BOARD_SIZE = 100;

    public Board() {
        squares = new Square[BOARD_SIZE + 1];
        snakes = new HashMap<>();
        ladders = new HashMap<>();
        initializeBoard();
        initializeSnakesAndLadders();
    }

    private void initializeBoard() {
        for (int i = 1; i <= BOARD_SIZE; i++) {
            squares[i] = new Square(i);
        }
    }

    private void initializeSnakesAndLadders() {
        int[][] snakePositions = {
            {99, 54}, {95, 75}, {93, 73}, {87, 24}, {64, 60},
            {62, 19}, {56, 53}, {49, 11}, {47, 26}, {16, 6}
        };

        int[][] ladderPositions = {
            {9, 31}, {15, 41}, {23, 57}, {28, 84}, {36, 44},
            {51, 67}, {71, 91}, {80, 100}, {4, 14}, {7, 29}
        };

        for (int[] snake : snakePositions) {
            snakes.put(snake[0], snake[1]);
            squares[snake[0]].setNextPosition(snake[1]);
            squares[snake[0]].setType("snake");
        }

        for (int[] ladder : ladderPositions) {
            ladders.put(ladder[0], ladder[1]);
            squares[ladder[0]].setNextPosition(ladder[1]);
            squares[ladder[0]].setType("ladder");
        }
    }

    public int getNewPosition(int position) {
        if (snakes.containsKey(position)) {
            return snakes.get(position);
        }
        if (ladders.containsKey(position)) {
            return ladders.get(position);
        }
        return position;
    }

    public Square getSquare(int position) {
        return squares[position];
    }

    public void display() {
        display(new java.util.ArrayList<>());
    }

    public void display(Collection<Player> players) {
        Map<Integer, String> playerTags = new HashMap<>();
        for (Player p : players) {
            int pos = p.getCurrentPosition();
            playerTags.merge(pos, "P" + p.getId(), (old, val) -> old + "," + val);
        }

        System.out.println("\n=== BOARD (S=Snake, L=Ladder, P=Player) ===\n");
        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col < 10; col++) {
                int position;
                if (row % 2 == 0) {
                    position = (row * 10) + (col + 1);
                } else {
                    position = (row * 10) + (10 - col);
                }

                String cellStr;
                if (playerTags.containsKey(position)) {
                    cellStr = String.format("[%s]%03d", playerTags.get(position), position);
                } else {
                    Square sq = squares[position];
                    if (sq.getType().equals("snake")) {
                        cellStr = String.format(" S %03d", position);
                    } else if (sq.getType().equals("ladder")) {
                        cellStr = String.format(" L %03d", position);
                    } else {
                        cellStr = String.format(" . %03d", position);
                    }
                }
                System.out.printf("%-12s", cellStr);
            }
            System.out.println();
        }
        System.out.println("\n========================================================================================================================\n");
    }
}