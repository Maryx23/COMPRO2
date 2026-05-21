package midterms;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 8000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String raw;
        while ((raw = in.readLine()) != null) {
            GameChat msg = gson.fromJson(raw, GameChat.class);
            System.out.println(msg.getContent());
            if (msg.getType().equals("PROMPT")) {
                GameChat response = new GameChat();
                response.setType("Input");
                response.setContent(scanner.nextLine());
                out.println(gson.toJson(response));
            }
            if (msg.getType().equals("Game Over")) {
                break;
            }
        }
        scanner.close();
        socket.close();
    }
}