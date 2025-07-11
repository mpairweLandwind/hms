package com.hms2.dto.appointment;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppointmentRequestDTO {
    
    private Long appointmentId;
    
    @NotNull(message = "Patient is required")
    private Long patientId;
    
    @NotNull(message = "Doctor is required")
    private Long doctorId;
    
    @NotNull(message = "Appointment date and time is required")
    private Date appointmentDate;
    
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
    
    @Size(max = 100, message = "Appointment type must not exceed 100 characters")
    private String appointmentType = "ROUTINE"; // ROUTINE, URGENT, EMERGENCY, FOLLOW_UP
    
    @Size(max = 20, message = "Priority must not exceed 20 characters")
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT
    
    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters")
    private String symptoms;
    
    @Size(max = 100, message = "Duration must not exceed 100 characters")
    private String duration = "30"; // minutes
    
    private Boolean followUpRequired = false;
    
    private Date followUpDate;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    // Additional fields for enhanced functionality
    private String requestedBy; // STAFF, PATIENT, DOCTOR, ADMIN
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, CONFIRMED, COMPLETED, CANCELLED
    private String rejectionReason;
    private Date approvedDate;
    private Long approvedBy;
    private String approvedByRole;
    
    // Insurance and billing information
    private String insuranceProvider;
    private String insuranceNumber;
    private Boolean insuranceCovered = false;
    private Double estimatedCost;
    
    // Communication preferences
    private Boolean emailReminder = true;
    private Boolean smsReminder = true;
    private Boolean phoneReminder = false;
    
    // Constructors
    public AppointmentRequestDTO() {}
    
    public AppointmentRequestDTO(Long patientId, Long doctorId, Date appointmentDate, String reason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
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
    
    public String getAppointmentType() {
        return appointmentType;
    }
    
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getSymptoms() {
        return symptoms;
    }
    
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public Boolean getFollowUpRequired() {
        return followUpRequired;
    }
    
    public void setFollowUpRequired(Boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }
    
    public Date getFollowUpDate() {
        return followUpDate;
    }
    
    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getRequestedBy() {
        return requestedBy;
    }
    
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public Date getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public Long getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public String getApprovedByRole() {
        return approvedByRole;
    }
    
    public void setApprovedByRole(String approvedByRole) {
        this.approvedByRole = approvedByRole;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    public String getInsuranceNumber() {
        return insuranceNumber;
    }
    
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    
    public Boolean getInsuranceCovered() {
        return insuranceCovered;
    }
    
    public void setInsuranceCovered(Boolean insuranceCovered) {
        this.insuranceCovered = insuranceCovered;
    }
    
    public Double getEstimatedCost() {
        return estimatedCost;
    }
    
    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
    
    public Boolean getEmailReminder() {
        return emailReminder;
    }
    
    public void setEmailReminder(Boolean emailReminder) {
        this.emailReminder = emailReminder;
    }
    
    public Boolean getSmsReminder() {
        return smsReminder;
    }
    
    public void setSmsReminder(Boolean smsReminder) {
        this.smsReminder = smsReminder;
    }
    
    public Boolean getPhoneReminder() {
        return phoneReminder;
    }
    
    public void setPhoneReminder(Boolean phoneReminder) {
        this.phoneReminder = phoneReminder;
    }
    
    @Override
    public String toString() {
        return "AppointmentRequestDTO{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", reason='" + reason + '\'' +
                ", appointmentType='" + appointmentType + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                '}';
    }
}
