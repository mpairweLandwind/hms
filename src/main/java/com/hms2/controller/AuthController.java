package com.hms2.controller;

import java.io.Serializable;

import com.hms2.enums.UserRole;
import com.hms2.model.User;
import com.hms2.service.AuthenticationService;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("authController")
@SessionScoped
public class AuthController implements Serializable {
    
    @Inject
    private AuthenticationService authenticationService;
    
    private String username;
    private String password;
    private User currentUser;
    private boolean loggedIn = false;
    private boolean rememberMe = false;
    
    public String login() {
        try {
            System.err.println("Attempting login for user: " + username);
            
            User user = authenticationService.authenticate(username, password).orElse(null);
            if (user != null) {
                currentUser = user;
                loggedIn = true;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
                addSuccessMessage("Login successful! Welcome " + user.getUsername());
                
                // Redirect based on user role
                return redirectBasedOnRole(user.getRole());
            } else {
                addErrorMessage("Invalid username or password");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("Login error");
            e.printStackTrace(System.err);
            addErrorMessage("Login failed: " + e.getMessage());
            return null;
        }
    }
    
    private String redirectBasedOnRole(UserRole role) {
        switch (role) {
            case PATIENT:
                return "/views/dashboard/patient-dashboard.xhtml?faces-redirect=true";
            case DOCTOR:
                return "/views/dashboard/doctor-dashboard.xhtml?faces-redirect=true";
            case STAFF:
                return "/views/dashboard/staff-dashboard.xhtml?faces-redirect=true";
            case ADMIN:
                return "/views/dashboard/admin-dashboard.xhtml?faces-redirect=true";
            default:
                return "/index.xhtml?faces-redirect=true";
        }
    }
    
    public String logout() {
        System.err.println("User " + (currentUser != null ? currentUser.getUsername() : "unknown") + " logging out");
        
        currentUser = null;
        loggedIn = false;
        username = null;
        password = null;
        rememberMe = false;
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        addSuccessMessage("You have been logged out successfully");
        
        return "/index.xhtml?faces-redirect=true";
    }
    
    public boolean isLoggedIn() {
        return loggedIn && currentUser != null;
    }
    
    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().name().equals(role);
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole().getDisplayName() : "";
    }
    
    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getUsername() : "";
    }
    
    public boolean isRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
