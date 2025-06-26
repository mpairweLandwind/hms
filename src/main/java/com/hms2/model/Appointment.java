package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq")
    @SequenceGenerator(name = "appointment_seq", sequenceName = "appointment_seq", allocationSize = 1)
    @Column(name = "appointment_id")
    private Long appointmentId;

    // Many-to-one relationship with Patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    // Many-to-one relationship with Doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required")
    private Doctor doctor;

    @NotNull(message = "Appointment date and time is required")
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDateTime;

    @NotBlank(message = "Appointment type is required")
    @Size(max = 50, message = "Appointment type must not exceed 50 characters")
    @Column(name = "appointment_type", nullable = false, length = 50)
    private String appointmentType;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    @Column(name = "reason", length = 500)
    private String reason;

    @Pattern(regexp = "SCHEDULED|CONFIRMED|IN_PROGRESS|COMPLETED|CANCELLED|NO_SHOW",
             message = "Status must be SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, or NO_SHOW")
    @Column(name = "status", length = 20)
    private String status = "SCHEDULED";

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration must not exceed 8 hours")
    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30;

    // Constructors
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String appointmentType) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentType = appointmentType;
    }

    // Business methods
    public boolean isScheduled() {
        return "SCHEDULED".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public void confirm() {
        this.status = "CONFIRMED";
    }

    public void startAppointment() {
        this.status = "IN_PROGRESS";
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void markNoShow() {
        this.status = "NO_SHOW";
    }

    public LocalDateTime getEndDateTime() {
        if (appointmentDateTime != null && durationMinutes != null) {
            return appointmentDateTime.plusMinutes(durationMinutes);
        }
        return null;
    }

    // Getters and setters
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

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
