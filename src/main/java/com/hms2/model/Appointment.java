package com.hms2.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hms2.enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30; // Default 30 minutes

    @Column(name = "appointment_type")
    private String appointmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "symptoms", columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "priority")
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "cancelled_reason")
    private String cancelledReason;

    @Column(name = "cancelled_by")
    private String cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Constructors
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime,
                       String appointmentType, String reason) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentType = appointmentType;
        this.reason = reason;
    }

    // Business methods
    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel(String reason, String cancelledBy) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelledReason = reason;
        this.cancelledBy = cancelledBy;
        this.cancelledAt = LocalDateTime.now();
    }

    public void checkIn() {
        this.status = AppointmentStatus.IN_PROGRESS;
        this.checkedInAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markNoShow() {
        this.status = AppointmentStatus.NO_SHOW;
    }

    public void reschedule(LocalDateTime newDateTime) {
        this.appointmentDateTime = newDateTime;
        this.status = AppointmentStatus.RESCHEDULED;
        this.confirmedAt = null;
    }

    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now()) && status.isUpcoming();
    }

    public boolean isToday() {
        return appointmentDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public boolean canBeCancelled() {
        return status.canBeCancelled();
    }

    public boolean canBeRescheduled() {
        return status.canBeRescheduled();
    }

    public LocalDateTime getEndTime() {
        return appointmentDateTime.plusMinutes(durationMinutes != null ? durationMinutes : 30);
    }

    public String getAppointmentTime() {
        if (appointmentDateTime != null) {
            return appointmentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public String getAppointmentDate() {
        if (appointmentDateTime != null) {
            return appointmentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    public String getFormattedDateTime() {
        if (appointmentDateTime != null) {
            return appointmentDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
        }
        return "";
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getPriorityDisplayName() {
        if (priority == null) return "Normal";

        switch (priority.toUpperCase()) {
            case "LOW": return "Low";
            case "NORMAL": return "Normal";
            case "HIGH": return "High";
            case "URGENT": return "Urgent";
            default: return priority;
        }
    }

    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Boolean getFollowUpRequired() {
        return followUpRequired;
    }

    public void setFollowUpRequired(Boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }

    public LocalDateTime getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDateTime followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patient=" + (patient != null ? patient.getFirstName() + " " + patient.getLastName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "null") +
                ", appointmentDateTime=" + appointmentDateTime +
                ", status=" + status +
                ", appointmentType='" + appointmentType + '\'' +
                '}';
    }
}
