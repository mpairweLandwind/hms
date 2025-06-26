package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_seq")
    @SequenceGenerator(name = "prescription_seq", sequenceName = "prescription_seq", allocationSize = 1)
    @Column(name = "prescription_id")
    private Long prescriptionId;

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

    // Many-to-one relationship with MedicalRecord (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private MedicalRecord medicalRecord;

    @NotNull(message = "Prescription date is required")
    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    @Column(name = "instructions", length = 500)
    private String instructions;

    @Pattern(regexp = "ACTIVE|COMPLETED|CANCELLED|EXPIRED",
             message = "Status must be ACTIVE, COMPLETED, CANCELLED, or EXPIRED")
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    // One-to-many relationship with PrescriptionMedication
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrescriptionMedication> prescriptionMedications;

    // Constructors
    public Prescription() {}

    public Prescription(Patient patient, Doctor doctor, LocalDateTime prescriptionDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.prescriptionDate = prescriptionDate;
    }

    // Business methods
    public boolean isActive() {
        return "ACTIVE".equals(status) && !isExpired();
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void expire() {
        this.status = "EXPIRED";
    }

    // Getters and setters
    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public LocalDateTime getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDateTime prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PrescriptionMedication> getPrescriptionMedications() {
        return prescriptionMedications;
    }

    public void setPrescriptionMedications(List<PrescriptionMedication> prescriptionMedications) {
        this.prescriptionMedications = prescriptionMedications;
    }
}
