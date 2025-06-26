package com.hms2.service.impl;

import com.hms2.model.User;
import com.hms2.repository.UserRepository;
import com.hms2.service.AuthenticationService;
import com.hms2.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    @Inject
    private UserRepository userRepository;
    
    @Override
    public User createUser(String username, String email, String password, String role) {
        logger.info("Creating new user: {}", username);
        
        // Validate unique constraints
        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setRole(role);
        user.setStatus("PENDING_VERIFICATION");
        
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (validatePassword(password, user.getPasswordHash()) && user.isActive()) {
                logger.info("Authentication successful for user: {}", username);
                return Optional.of(user);
            }
        }
        
        logger.warn("Authentication failed for user: {}", username);
        return Optional.empty();
    }
    
    @Override
    public boolean validatePassword(String password, String hashedPassword) {
        return PasswordUtil.verifyPassword(password, hashedPassword);
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        logger.info("Changing password for user ID: {}", userId);
        
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
        
        logger.info("Password changed successfully for user ID: {}", userId);
    }
    
    @Override
    public void resetPassword(String email) {
        logger.info("Resetting password for email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        String newPassword = PasswordUtil.generateRandomPassword(8);
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userRepository.update(user);
        
        // In a real application, you would send the new password via email
        logger.info("Password reset successfully for email: {}. New password: {}", email, newPassword);
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
        logger.info("Activating user ID: {}", userId);
        
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
        logger.info("Deactivating user ID: {}", userId);
        
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
        logger.info("Locking user ID: {}", userId);
        
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
        logger.info("Unlocking user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("ACTIVE");
            userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
