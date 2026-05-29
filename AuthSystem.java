package com.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthSystem {
    private List<User> users;
    private final String usersFile = "src/main/resources/users.dat";

    public AuthSystem() {
        this.users = new ArrayList<>();
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(usersFile);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
                System.out.println("Loaded " + users.size() + " users from database");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No existing users found. Creating new database.");
                users = new ArrayList<>();
            }
        } else {
            users = new ArrayList<>();
            createAdminUser();
        }
    }

    private void saveUsers() {
        try {
            File file = new File(usersFile);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void createAdminUser() {
        User admin = new User("admin", "admin123");
        users.add(admin);
        saveUsers();
        System.out.println("Default admin created: admin/admin123");
    }

    public boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password));
        saveUsers();
        return true;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User updated) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updated.getUsername())) {
                users.set(i, updated);
                saveUsers();
                return;
            }
        }
    }
}