package com.hms2.service.impl;

import java.util.Optional;

import com.hms2.enums.UserRole;
import com.hms2.model.User;
import com.hms2.repository.UserRepository;
import com.hms2.service.AuthenticationService;
import com.hms2.util.PasswordUtil;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Inject
    private UserRepository userRepository;
    
    @Override
    public User createUser(String username, String email, String password, String role) {
        System.err.println("Creating new user: " + username);
        
        // Validate unique constraints
        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create user (generic for all roles)
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setRole(UserRole.valueOf(role));
        user.setStatus("PENDING_VERIFICATION");
        
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> authenticate(String username, String password) {
        System.err.println("Authenticating user: " + username);
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (validatePassword(password, user.getPasswordHash()) ) {
                System.err.println("Authentication successful for user: " + username);
                return Optional.of(user);
            }
        }
        
        System.err.println("Authentication failed for user: " + username);
        return Optional.empty();
    }
    
    @Override
    public boolean validatePassword(String password, String hashedPassword) {
        return PasswordUtil.verifyPassword(password, hashedPassword);
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        System.err.println("Changing password for user ID: " + userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        if (!validatePassword(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userRepository.update(user);
        
        System.err.println("Password changed successfully for user ID: " + userId);
    }
    
    @Override
    public void resetPassword(String email) {
        System.err.println("Resetting password for email: " + email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        String newPassword = PasswordUtil.generateRandomPassword(8);
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userRepository.update(user);
        
        // In a real application, you would send the new password via email
        System.err.println("Password reset successfully for email: " + email + ". New password: " + newPassword);
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    @Override
    public void activateUser(Long userId) {
        System.err.println("Activating user ID: " + userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.activate();
            userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    @Override
    public void deactivateUser(Long userId) {
        System.err.println("Deactivating user ID: " + userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.deactivate();
            userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    @Override
    public void lockUser(Long userId) {
        System.err.println("Locking user ID: " + userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("LOCKED");
            userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    @Override
    public void unlockUser(Long userId) {
        System.err.println("Unlocking user ID: " + userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("ACTIVE");
            userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    @PostConstruct
    private void initDefaultAdmin() {
        String adminUsername = "admin";
        String adminEmail = "admin@hms.com";
        String adminPassword = "admin123";
        try {
            if (isUsernameAvailable(adminUsername) && isEmailAvailable(adminEmail)) {
                System.err.println("No admin account found. Creating default admin account.");
                createUser(adminUsername, adminEmail, adminPassword, UserRole.ADMIN.name());
                System.err.println("Default admin account created: username='" + adminUsername + "', password='" + adminPassword + "'");
            } else {
                System.err.println("Admin account already exists. Skipping default admin creation.");
            }
        } catch (Exception e) {
            System.err.println("Error creating default admin account");
            e.printStackTrace(System.err);
        }
    }
}
