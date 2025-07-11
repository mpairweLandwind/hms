package com.hms2.enums;

public enum StaffStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave"),
    SUSPENDED("Suspended"),
    TERMINATED("Terminated"),
    VERIFIED("Verified"),
    REJECTED("Rejected"),
    PENDING_VERIFICATION("Pending Verification");
    
    private final String displayName;
    
    StaffStatus(String displayName) {
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
