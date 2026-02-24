import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CsvApp {
    static String[] names;
    static double[][] healthData;

    public static void main(String[] args) {
        names = new String[50];
        healthData = new double[50][2];

        Scanner sc = new Scanner(System.in);

        for (int r = 0; r < 3; r++) {
            System.out.print("Enter name: ");
            names[r] = sc.nextLine();

            System.out.print("Enter weight: ");
            try {
                healthData[r][0] = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid number");
            }

            System.out.print("Enter height: ");
            try {
                healthData[r][1] = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid number");
            }

            sc.nextLine();
            System.out.println();
        }
        writeData();

    }

    public static void writeData() {
        StringBuilder sb = new StringBuilder();

        sb.append("Name,Weight,Height\n");
        for (int r = 0; r < names.length; r++) {
            if(names[r] == null) 
                break;

            sb.append(names[r]);
            for (int c = 0; c < healthData[r].length; c++) {
                sb.append(",").append(healthData[r][c]);
            }
            sb.append("\n");
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv"))){
            bw.write(sb.toString());
            bw.flush();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println(sb.toString());
    }
}