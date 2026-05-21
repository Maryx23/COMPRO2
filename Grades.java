import java.util.Scanner;

public class Grades {
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        while(true) {
            System.out.println("\nMAIN MENU:");
            System.out.println("[1] Enter Grades");
            System.out.println("[2] Display Grades");
            System.out.println("[3] Exit");
            System.out.print("\nEnter choice: ");
            
            int choice = sc.nextInt();
            
            switch(choice) {
                case 1:
                    enterGradesMenu();
                    break;
                case 2:
                    displayGrades();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void enterGradesMenu() {
        while(true) {
            System.out.println("\nEnter grade for:");
            System.out.println("[1] COMPRO2");
            System.out.println("[2] DSA");
            System.out.println("[3] OOP");
            System.out.println("[0] Go Back");
            System.out.print("\nEnter choice: ");
            
            int choice = sc.nextInt();
            
            switch(choice) {
                case 1:
                    System.out.println("Enter grades for COMPRO2");
                    System.out.println("Grades saved...");
                    break;
                case 2:
                    System.out.println("Enter grades for DSA");
                    System.out.println("Grades saved...");
                    break;
                case 3:
                    System.out.println("Enter grades for OOP");
                    System.out.println("Grades saved...");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void displayGrades() {
        System.out.println("Display Grades - Placeholder");
        System.out.println("This method will show all entered grades.");
    }
}