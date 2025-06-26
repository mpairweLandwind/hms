package com.hms2.dto.auth;

import java.util.Date;

public class LoginResponseDTO {
    
    private boolean success;
    private String message;
    private String username;
    private String role;
    private String fullName;
    private Date lastLogin;
    private String redirectUrl;
    
    // Constructors
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static LoginResponseDTO success(String username, String role, String fullName, String redirectUrl) {
        LoginResponseDTO response = new LoginResponseDTO(true, "Login successful");
        response.setUsername(username);
        response.setRole(role);
        response.setFullName(fullName);
        response.setRedirectUrl(redirectUrl);
        response.setLastLogin(new Date());
        return response;
    }
    
    public static LoginResponseDTO failure(String message) {
        return new LoginResponseDTO(false, message);
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Date getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
