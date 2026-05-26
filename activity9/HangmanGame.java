package activity9;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HangmanGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] playerNames = new String[50];
        int[] playerScores = new int[50];
        int playerCount = 0;

        String[] wordBank = new String[50];

        try {
            File file = new File("word.txt");
            Scanner fileReader = new Scanner(file);
            int index = 0;
            while (fileReader.hasNextLine() && index < 50) {
                wordBank[index] = fileReader.nextLine();
                index++;
            }
            fileReader.close();
            System.out.println("Loaded " + index + " words from file.");
        } catch (IOException e) {
            System.out.println("Error reading word.txt file. Using default words.");
            wordBank[0] = "program";
            wordBank[1] = "computer";
            wordBank[2] = "hangman";
            wordBank[3] = "java";
            wordBank[4] = "coding";
            wordBank[5] = "variable";
            wordBank[6] = "method";
            wordBank[7] = "string";
            wordBank[8] = "integer";
            wordBank[9] = "boolean";
            for (int i = 10; i < 50; i++) {
                wordBank[i] = "word" + i;
            }
        }

        boolean keepPlaying = true;

        while (keepPlaying && playerCount < 50) {
            System.out.print("Enter player name: ");
            String playerName = scanner.nextLine();

            int playerIndex = playerCount;

            try {
                File userFile = new File("users.json");
                if (userFile.exists()) {
                    String content = new String(Files.readAllBytes(Paths.get("users.json")));
                    if (content.contains("\"name\":\"" + playerName + "\"")) {
                        System.out.println("Welcome back " + playerName + "!");
                    } else {
                        System.out.println("New user! Creating account.");
                        FileWriter fw = new FileWriter("users.json", true);
                        PrintWriter pw = new PrintWriter(fw);
                        pw.println("{\"name\":\"" + playerName + "\"}");
                        pw.close();
                    }
                } else {
                    System.out.println("New user! Creating account.");
                    FileWriter fw = new FileWriter("users.json", true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println("{\"name\":\"" + playerName + "\"}");
                    pw.close();
                }
            } catch (IOException e) {
                System.out.println("Error with user file.");
            }

            playerNames[playerIndex] = playerName;

            Random rand = new Random();
            String word = wordBank[rand.nextInt(50)];

            String hidden = "";
            for (int i = 0; i < word.length(); i++) {
                hidden = hidden + "*";
            }

            char[] guessed = new char[26];
            int guessCount = 0;
            int wrong = 0;
            int points = 0;
            boolean won = false;

            System.out.println("\nStarting game!");
            System.out.println("The word has " + word.length() + " letters.");

            while (wrong < 6) {
                System.out.print("Word: " + hidden + " > ");
                String input = scanner.nextLine();
                char guess;
                if (input.length() > 0) {
                    guess = input.charAt(0);
                } else {
                    System.out.println("Please enter a letter.");
                    continue;
                }

                boolean alreadyGuessed = false;
                for (int i = 0; i < guessCount; i++) {
                    if (guessed[i] == guess) {
                        alreadyGuessed = true;
                        break;
                    }
                }

                if (alreadyGuessed) {
                    System.out.println("You already guessed that letter!");
                    continue;
                }

                guessed[guessCount] = guess;
                guessCount++;

                boolean correct = false;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guess) {
                        correct = true;
                        break;
                    }
                }

                if (correct) {
                    System.out.println("Good guess!");
                    points = points + 10;
                    char[] hiddenArray = hidden.toCharArray();
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess) {
                            hiddenArray[i] = guess;
                        }
                    }
                    hidden = new String(hiddenArray);
                } else {
                    System.out.println("Sorry, " + guess + " is not in the word.");
                    wrong++;
                    System.out.println("Wrong guesses: " + wrong + "/6");

                    if (wrong == 1) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println("      |");
                        System.out.println("      |");
                        System.out.println("      |");
                        System.out.println("=========");
                    } else if (wrong == 2) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println("  |   |");
                        System.out.println("      |");
                        System.out.println("      |");
                        System.out.println("=========");
                    } else if (wrong == 3) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println(" /|   |");
                        System.out.println("      |");
                        System.out.println("      |");
                        System.out.println("=========");
                    } else if (wrong == 4) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println(" /|\\  |");
                        System.out.println("      |");
                        System.out.println("      |");
                        System.out.println("=========");
                    } else if (wrong == 5) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println(" /|\\  |");
                        System.out.println(" /    |");
                        System.out.println("      |");
                        System.out.println("=========");
                    } else if (wrong == 6) {
                        System.out.println("  +---+");
                        System.out.println("  |   |");
                        System.out.println("  O   |");
                        System.out.println(" /|\\  |");
                        System.out.println(" / \\  |");
                        System.out.println("      |");
                        System.out.println("=========");
                    }
                }

                boolean allFound = true;
                for (int i = 0; i < hidden.length(); i++) {
                    if (hidden.charAt(i) == '*') {
                        allFound = false;
                        break;
                    }
                }

                if (allFound) {
                    won = true;
                    break;
                }
            }

            if (won) {
                System.out.println("Congratulations! You guessed the word: " + word);
                int finalScore = 100 - (wrong * 5) + points;
                if (finalScore < 0)
                    finalScore = 0;
                System.out.println("Your score: " + finalScore);
                playerScores[playerIndex] = finalScore;
            } else {
                System.out.println("Game Over! The word was: " + word);
                System.out.println("Your score: 0");
                playerScores[playerIndex] = 0;
            }

            playerCount++;

            System.out.print("Another player? (y/n): ");
            String answer = scanner.nextLine();
            if (answer.equals("n")) {
                keepPlaying = false;
            }
        }

        for (int i = 0; i < playerCount - 1; i++) {
            for (int j = i + 1; j < playerCount; j++) {
                if (playerScores[i] < playerScores[j]) {
                    int tempScore = playerScores[i];
                    playerScores[i] = playerScores[j];
                    playerScores[j] = tempScore;

                    String tempName = playerNames[i];
                    playerNames[i] = playerNames[j];
                    playerNames[j] = tempName;
                }
            }
        }

        try {
            FileWriter fw = new FileWriter("scores.json");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("[");
            for (int i = 0; i < playerCount; i++) {
                pw.println("  {\"name\":\"" + playerNames[i] + "\",\"score\":" + playerScores[i] + "},");
            }
            pw.println("]");
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving scores.");
        }

        System.out.println("\n===== LEADERBOARD =====");
        for (int i = 0; i < playerCount; i++) {
            System.out.println((i + 1) + ". " + playerNames[i] + " - " + playerScores[i] + " points");
        }

        scanner.close();
    }
}