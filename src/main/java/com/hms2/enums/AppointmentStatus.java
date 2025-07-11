package com.hms2.enums;

/**
 * Enum representing the status of an appointment in the system.
 */
public enum AppointmentStatus {
    
    SCHEDULED("Scheduled", "Appointment is scheduled but not confirmed"),
    CONFIRMED("Confirmed", "Appointment is confirmed by the patient"),
    IN_PROGRESS("In Progress", "Patient has checked in and appointment is ongoing"),
    COMPLETED("Completed", "Appointment has been completed successfully"),
    CANCELLED("Cancelled", "Appointment was cancelled"),
    NO_SHOW("No Show", "Patient did not show up for the appointment"),
    RESCHEDULED("Rescheduled", "Appointment was rescheduled to a different time"),
    AVAILABLE("Available", "Time slot is available for booking");
    
    private final String displayName;
    private final String description;
    
    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isUpcoming() {
        return this == SCHEDULED || this == CONFIRMED;
    }
    
    public boolean isActive() {
        return this == IN_PROGRESS;
    }
    
    public boolean isCompleted() {
        return this == COMPLETED;
    }
    
    public boolean isCancelled() {
        return this == CANCELLED || this == NO_SHOW;
    }
    
    public boolean canBeCancelled() {
        return this == SCHEDULED || this == CONFIRMED;
    }
    
    public boolean canBeRescheduled() {
        return this == SCHEDULED || this == CONFIRMED;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
