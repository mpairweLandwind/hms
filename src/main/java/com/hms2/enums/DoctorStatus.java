package com.hms2.enums;

public enum DoctorStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PENDING_VERIFICATION("Pending Verification"),
    SUSPENDED("Suspended"),
    RETIRED("Retired");

    private final String text;

    DoctorStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
} 