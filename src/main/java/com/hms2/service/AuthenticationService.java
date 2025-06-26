package com.hms2.service;

import com.hms2.model.User;
import java.util.Optional;

public interface AuthenticationService {
    
    User createUser(String username, String email, String password, String role);
    
    Optional<User> authenticate(String username, String password);
    
    boolean validatePassword(String password, String hashedPassword);
    
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    void resetPassword(String email);
    
    boolean isUsernameAvailable(String username);
    
    boolean isEmailAvailable(String email);
    
    void activateUser(Long userId);
    
    void deactivateUser(Long userId);
    
    void lockUser(Long userId);
    
    void unlockUser(Long userId);
}
