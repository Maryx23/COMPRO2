package com.phonebook.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.phonebook.models.Contact;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PhonebookService {
    private HashMap<String, Contact> contacts;
    private final Gson gson;

    public PhonebookService() {
        this.contacts = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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

    public void saveToJSON(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println(gson.toJson(contacts));
            System.out.println("Contacts saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    public void loadFromJSON(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Type type = new TypeToken<HashMap<String, Contact>>() {}.getType();
            HashMap<String, Contact> loaded = gson.fromJson(br, type);
            if (loaded != null) {
                contacts = loaded;
                System.out.println("Contacts loaded from " + filename);
            }
        } catch (IOException e) {
            System.out.println("Error loading from JSON: " + e.getMessage());
        }
    }
}