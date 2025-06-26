package com.hms2.dto.appointment;

import jakarta.validation.constraints.*;
import java.util.Date;

public class AppointmentRequestDTO {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private Date appointmentDate;
    
    @NotBlank(message = "Reason for visit is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
    
    @Pattern(regexp = "ROUTINE|URGENT|EMERGENCY", message = "Priority must be ROUTINE, URGENT, or EMERGENCY")
    private String priority = "ROUTINE";
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    private String preferredTime;
    
    // Constructors
    public AppointmentRequestDTO() {}
    
    public AppointmentRequestDTO(Long patientId, Long doctorId, Date appointmentDate, String reason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }
    
    // Getters and setters
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getPreferredTime() {
        return preferredTime;
    }
    
    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }
}
