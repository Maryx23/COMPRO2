package com.phonebook;


import com.phonebook.models.Contact;
import com.phonebook.services.PhonebookService;

import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "contacts.csv";

    public static void main(String[] args) {
        PhonebookService service = new PhonebookService();
        service.loadFromCSV(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== PHONEBOOK MENU ===");
            System.out.println("1. Add Contact");
            System.out.println("2. Search Contact");
            System.out.println("3. Remove Contact");
            System.out.println("4. Display All");
            System.out.println("5. Save to CSV");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    Contact contact = new Contact(name, phone, email);
                    service.addContact(contact);
                    System.out.println("Contact added.");
                    break;
                case "2":
                    System.out.print("Enter Name to search: ");
                    String searchName = scanner.nextLine();
                    Contact found = service.searchContact(searchName);
                    if (found != null) {
                        System.out.println("Found: " + found);
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;
                case "3":
                    System.out.print("Enter Name to remove: ");
                    String removeName = scanner.nextLine();
                    if (service.searchContact(removeName) != null) {
                        service.removeContact(removeName);
                        System.out.println("Contact removed.");
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;
                case "4":
                    if (service.getContacts().isEmpty()) {
                        System.out.println("Phonebook is empty.");
                    } else {
                        System.out.println("\n--- All Contacts ---");
                        for (Contact c : service.getContacts().values()) {
                            System.out.println(c);
                        }
                    }
                    break;
                case "5":
                    service.saveToCSV(FILE_NAME);
                    break;
                case "0":
                    service.saveToCSV(FILE_NAME);
                    running = false;
                    System.out.println("Exiting Phonebook.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}