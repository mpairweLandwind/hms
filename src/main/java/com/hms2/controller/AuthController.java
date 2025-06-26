package com.hms2.controller;

import com.hms2.model.User;
import com.hms2.enums.UserRole;
import com.hms2.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("authController")
@SessionScoped
public class AuthController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Inject
    private AuthenticationService authenticationService;
    
    private String username;
    private String password;
    private User currentUser;
    private boolean loggedIn = false;
    
    public String login() {
        try {
            logger.info("Attempting login for user: {}", username);
            
            User user = authenticationService.authenticate(username, password);
            if (user != null) {
                currentUser = user;
                loggedIn = true;
                
                addSuccessMessage("Login successful! Welcome " + user.getUsername());
                
                // Redirect based on user role
                return redirectBasedOnRole(user.getRole());
            } else {
                addErrorMessage("Invalid username or password");
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Login error", e);
            addErrorMessage("Login failed: " + e.getMessage());
            return null;
        }
    }
    
    private String redirectBasedOnRole(UserRole role) {
        switch (role) {
            case PATIENT:
                return "patient-dashboard?faces-redirect=true";
            case DOCTOR:
                return "doctor-dashboard?faces-redirect=true";
            case STAFF:
                return "staff-dashboard?faces-redirect=true";
            case ADMIN:
                return "admin-dashboard?faces-redirect=true";
            default:
                return "dashboard?faces-redirect=true";
        }
    }
    
    public String logout() {
        logger.info("User {} logging out", currentUser != null ? currentUser.getUsername() : "unknown");
        
        currentUser = null;
        loggedIn = false;
        username = null;
        password = null;
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        addSuccessMessage("You have been logged out successfully");
        
        return "index?faces-redirect=true";
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
}
