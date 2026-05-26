package com.phonebook.services;

import com.phonebook.models.Contact;
import java.io.*;
import java.util.*;

public class PhoneBookService {
    
    private HashMap<String, Contact> contacts;
    
    public PhoneBookService() {
        contacts = new HashMap<>();
    }
    
    public boolean addContact(Contact c) {
        if (contacts.containsKey(c.getName())) {
            return false;
        }
        contacts.put(c.getName(), c);
        return true;
    }
    
    public Contact searchContact(String name) {
        return contacts.get(name);
    }
    
    public boolean removeContact(String name) {
        if (contacts.containsKey(name)) {
            contacts.remove(name);
            return true;
        }
        return false;
    }
    
    public HashMap<String, Contact> getAllContacts() {
        return contacts;
    }
    
    public void saveToCSV(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (Contact c : contacts.values()) {
                writer.write(c.toCsvString() + "\n");
            }
            writer.close();
            System.out.println("Saved " + contacts.size() + " contacts");
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    
    public void loadFromCSV(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("No saved contacts found");
                return;
            }
            
            Scanner fileScanner = new Scanner(file);
            int count = 0;
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length == 3) {
                    Contact c = new Contact(parts[0], parts[1], parts[2]);
                    contacts.put(parts[0], c);
                    count++;
                }
            }
            
            fileScanner.close();
            System.out.println("Loaded " + count + " contacts");
            
        } catch (IOException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }
}