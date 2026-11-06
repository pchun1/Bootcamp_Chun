package org.example;

import org.mindrot.jbcrypt.BCrypt;

public class User {

    private String username;
    private String passwordHash;
    private String role;

    // Create a user account
    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    // Verify the password against the stored hash
    public boolean checkPassword(String input) {
        return BCrypt.checkpw(input, passwordHash);
    }

    public String getRole() {
        return role;
    }

    public boolean isManager() {
        return role.equals("MANAGER");
    }
}