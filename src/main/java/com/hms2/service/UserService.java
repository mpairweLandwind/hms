package com.hms2.service;

import java.util.List;

import com.hms2.model.User;

public interface UserService {
    List<User> getAllUsers();
    List<User> getDeletedUsers();
    java.util.Optional<User> getUserById(Long userId);
    User updateUser(User user);
    void deleteUser(Long userId); // soft delete
    void restoreUser(Long userId);
    void permanentlyDeleteUser(Long userId);
    User createUser(User user);
} 