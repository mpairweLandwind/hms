package com.hms2.enums;

public enum PrescriptionStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    EXPIRED("Expired"),
    ON_HOLD("On Hold");
    
    private final String displayName;
    
    PrescriptionStatus(String displayName) {
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
