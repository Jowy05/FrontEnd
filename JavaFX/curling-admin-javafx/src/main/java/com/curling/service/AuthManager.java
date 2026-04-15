package com.curling.service;

import com.curling.model.User;

public class AuthManager {
    private static AuthManager instance;
    private String token;
    private User currentUser;
    
    private AuthManager() {}
    
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
    public boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
    
    public void clear() {
        this.token = null;
        this.currentUser = null;
    }
}