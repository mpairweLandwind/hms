package com.hms2.repository;

import com.hms2.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(String role);
    
    List<User> findByStatus(String status);
    
    List<User> findActiveUsers();
    
    List<User> findDeletedUsers();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    long countByRole(String role);
    
    long countByStatus(String status);
}
