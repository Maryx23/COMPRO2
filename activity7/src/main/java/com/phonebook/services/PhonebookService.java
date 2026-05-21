package com.phonebook.services;


import java.io.*;
import java.util.HashMap;

import com.phonebook.models.Contact;

public class PhonebookService {
    private HashMap<String, Contact> contacts;

    public PhonebookService() {
        this.contacts = new HashMap<>();
    }

    public void addContact(Contact c) {
        contacts.put(c.getName(), c);
    }

    public Contact searchContact(String name) {
        return contacts.get(name);
    }

    public void removeContact(String name) {
        contacts.remove(name);
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }

    public void saveToCSV(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Contact c : contacts.values()) {
                pw.println(c.toCsvString());
            }
            System.out.println("Contacts saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }

    public void loadFromCSV(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Contact c = new Contact(parts[0], parts[1], parts[2]);
                    contacts.put(c.getName(), c);
                }
            }
            System.out.println("Contacts loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading from CSV: " + e.getMessage());
        }
    }
}