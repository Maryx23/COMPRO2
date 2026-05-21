package midterms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class FileHandler {
    private static final String FILE_NAME = "results.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveRound(Round round) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            pw.println(gson.toJson(round));
            System.out.println("Round saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving round: " + e.getMessage());
        }
    }
}