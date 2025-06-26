package com.hms2.enums;

public enum UserRole {
    PATIENT("Patient"),
    DOCTOR("Doctor"),
    STAFF("Staff"),
    ADMIN("Administrator");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
