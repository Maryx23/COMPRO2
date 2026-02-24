import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class App {
    private ArrayList<Grades> grades;
    private Scanner scanner;

    public App() {
        grades = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n Student Grades ");
            System.out.println("1. Add Grades");
            System.out.println("2. Display Grades");
            System.out.println("3. Search Grades");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    addGrades();
                    break;
                case 2:
                    displayGrades();
                    break;
                case 3:
                    searchGrades();
                    break;
                case 4:
                    exitProgram();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private void addGrades() {
        System.out.println("\n Add New Subject Grades ");
        System.out.print("Enter Subject Name: ");
        String subject = scanner.nextLine();
        System.out.print("Enter Prelim Grade: ");
        double prelim = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Midterm Grade: ");
        double midterm = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Final Grade: ");
        double final_grade = Double.parseDouble(scanner.nextLine());
        Grades newGrade = new Grades (subject, prelim, midterm, final_grade);
        grades.add(newGrade);
        System.out.println("Grades added successfully!");
    }

    private void displayGrades() {
        System.out.println("\n All Subjects Grades ");
        if (grades.isEmpty()) {
            System.out.println("No subjects stored yet.");
            return;
        }
        for (int i = 0; i < grades.size(); i++) {
            System.out.println((i + 1) + ". " + grades.get(i).displayInfo());
        }
    }

    private void searchGrades() {
        System.out.println("\n Search Subject");
        if (grades.isEmpty()) {
            System.out.println("No subjects to search.");
            return;
        }
        System.out.print("Enter subject name to search: ");
        String searchSubject = scanner.nextLine();
        boolean found = false;
        for (Grades grade : grades) {
            if (grade.getSubject().equalsIgnoreCase(searchSubject)) {
                System.out.println("Subject Found");
                System.out.println(grade.displayInfo());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Subject '" + searchSubject + "' not found.");
        }
    }

    private void exitProgram() {
        System.out.println("\n--- Saving Data and Exiting ---");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("grades.csv"))) {
            writer.write("Subject,Prelim,Midterm,Final");
            writer.newLine();
            for (Grades grade : grades) {
                writer.write(grade.toCSV());
                writer.newLine();
            }
            System.out.println("Data successfully saved to grades.csv");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
        System.out.println("Thank you for using Student Grade Portfolio!");
    }
}