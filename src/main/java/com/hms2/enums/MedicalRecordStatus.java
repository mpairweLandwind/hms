package com.hms2.enums;

/**
 * Enum representing the status of a medical record in the system.
 */
public enum MedicalRecordStatus {
    
    DRAFT("Draft", "Medical record is in draft state"),
    COMPLETED("Completed", "Medical record is completed"),
    REVIEWED("Reviewed", "Medical record has been reviewed"),
    ARCHIVED("Archived", "Medical record has been archived");
    
    private final String displayName;
    private final String description;
    
    MedicalRecordStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isDraft() {
        return this == DRAFT;
    }
    
    public boolean isCompleted() {
        return this == COMPLETED || this == REVIEWED;
    }
    
    public boolean isArchived() {
        return this == ARCHIVED;
    }
    
    public boolean isEditable() {
        return this == DRAFT;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 