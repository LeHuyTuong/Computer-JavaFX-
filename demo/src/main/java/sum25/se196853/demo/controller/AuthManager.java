package sum25.se196853.demo.controller;

import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.entity.User;

public class AuthManager {
    private static AuthManager instance;
    private User currentUser;

    private AuthManager() {}

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot log in with a null user.");
        }
        this.currentUser = user;
        System.out.println("User logged in: " + user.getEmail() + " Role: " + user.getRole());
    }

    public void logout() {
        System.out.println("User logged out: " + (currentUser != null ? currentUser.getEmail() : "null"));
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && "Admin".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean isStaffOrAdmin() {
        return isLoggedIn() && ("Admin".equalsIgnoreCase(currentUser.getRole()) || "Staff".equalsIgnoreCase(currentUser.getRole()));
    }
}
