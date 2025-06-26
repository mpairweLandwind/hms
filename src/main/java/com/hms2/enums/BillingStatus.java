package com.hms2.enums;

public enum BillingStatus {
    PENDING("Pending"),
    PAID("Paid"),
    PARTIALLY_PAID("Partially Paid"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");
    
    private final String displayName;
    
    BillingStatus(String displayName) {
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
