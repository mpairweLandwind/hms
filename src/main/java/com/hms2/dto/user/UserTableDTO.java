package com.hms2.dto.user;

import com.hms2.enums.UserRole;

public class UserTableDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private Boolean isDeleted;
    private Long version;
    // Add more fields as needed for display/editing

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
} 