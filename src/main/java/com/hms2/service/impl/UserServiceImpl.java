package com.hms2.service.impl;

import java.util.List;

import com.hms2.enums.UserRole;
import com.hms2.model.User;
import com.hms2.repository.UserRepository;
import com.hms2.service.UserService;
import com.hms2.util.PasswordUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getDeletedUsers() {
        return userRepository.findDeletedUsers();
    }

    @Override
    public java.util.Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(User user) {
        System.err.println("[UPDATE] User received from form: " + user);
        try {
            User updatedUser = userRepository.update(user);
            System.err.println("[SUCCESS] User updated: " + updatedUser);
            return updatedUser;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update user: " + user);
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        System.err.println("[SERVICE] deleteUser called for userId: " + userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            System.err.println("[SERVICE] User fetched for soft delete: " + user);
            user.softDelete("ADMIN");
            userRepository.update(user);
            System.err.println("[SERVICE] User soft deleted and updated in DB: " + userId);
        } catch (Exception e) {
            System.err.println("[SERVICE][ERROR] Failed to soft delete user: " + userId + ", reason: " + e.getMessage());
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @Override
    public void restoreUser(Long userId) {
        System.err.println("[RESTORE] User restore requested for ID: " + userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            System.err.println("[DEBUG] Found user to restore: " + user);
            user.restore();
            User restoredUser = userRepository.update(user);
            System.err.println("[SUCCESS] User restored: " + restoredUser);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to restore user: " + userId);
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @Override
    public void permanentlyDeleteUser(Long userId) {
        System.err.println("[SERVICE] permanentlyDeleteUser called for userId: " + userId);
        try {
            userRepository.deleteById(userId);
            System.err.println("[SERVICE] User permanently deleted in repository: " + userId);
        } catch (Exception e) {
            System.err.println("[SERVICE][ERROR] Failed to permanently delete user: " + userId + ", reason: " + e.getMessage());
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @Override
    public User createUser(User user) {
        System.err.println("[CREATE] User received from form: " + user);
        try {
            System.err.println("Creating new user: " + user.getUsername());
            // Validate unique constraints
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            // Hash the password
            user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
            // Set status and role if not set
            if (user.getStatus() == null || user.getStatus().isEmpty()) {
                user.setStatus("PENDING_VERIFICATION");
            }
            if (user.getRole() == null && user instanceof User) {
                user.setRole(UserRole.PATIENT); // Default role if not set
            }
            User savedUser = userRepository.save(user);
            System.err.println("[SUCCESS] User created: " + savedUser);
            return savedUser;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to create user: " + user);
            e.printStackTrace(System.err);
            throw e;
        }
    }
} 