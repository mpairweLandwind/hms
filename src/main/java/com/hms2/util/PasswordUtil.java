package com.hms2.util;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class PasswordUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final int BCRYPT_ROUNDS = 12;
    
    /**
     * Hashes a password using BCrypt
     */
    public static String hashPassword(String password) {
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));
        } catch (Exception e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verifies a password against a BCrypt hash
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Generates a random password
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
    
    /**
     * Validates password strength
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0) hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
