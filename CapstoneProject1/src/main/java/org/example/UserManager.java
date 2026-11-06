package org.example;

import org.mindrot.jbcrypt.BCrypt;

public class UserManager {

    private User employee;
    private User manager;

    // Create the demonstration accounts
    public UserManager() {
        String employeeHash = BCrypt.hashpw("Employee123!", BCrypt.gensalt(12));
        String managerHash = BCrypt.hashpw("Manager123!", BCrypt.gensalt(12));

        employee = new User("pchunemployee", employeeHash, "EMPLOYEE");
        manager = new User("pchunmanager", managerHash, "MANAGER");
    }

    // Check login credentials
    public User login(String username, String password) {
        if (employee.getUsername().equalsIgnoreCase(username)
                && employee.checkPassword(password)) {
            return employee;
        }

        if (manager.getUsername().equalsIgnoreCase(username)
                && manager.checkPassword(password)) {
            return manager;
        }

        return null;
    }

    // Verify manager authorization
    public boolean verifyManagerPassword(String password) {
        return manager.checkPassword(password);
    }
}