package com;

import com.Server.GameServer;
import com.Client.GameClient;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("     SNAKES AND LADDERS MULTIPLAYER     ");
        System.out.println("=========================================");
        System.out.println("1. Start Server");
        System.out.println("2. Start Client");
        System.out.println("3. Exit");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();

        if (choice == 1) {
            GameServer.main(args);
        } else if (choice == 2) {
            GameClient.main(args);
        } else {
            System.out.println("Goodbye!");
        }
    }
}