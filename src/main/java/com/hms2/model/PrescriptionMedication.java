package com.hms2.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "prescription_medications")
public class PrescriptionMedication extends BaseEntity {

    @Column(name = "prescription_medication_id")
    private Long prescriptionMedicationId;

    // Many-to-one relationship with Prescription
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    @NotNull(message = "Prescription is required")
    private Prescription prescription;

    // Many-to-one relationship with Medication
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    @NotNull(message = "Medication is required")
    private Medication medication;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage must not exceed 100 characters")
    @Column(name = "dosage", nullable = false, length = 100)
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    @Column(name = "frequency", nullable = false, length = 50)
    private String frequency;

    @Min(value = 1, message = "Duration must be at least 1 day")
    @Column(name = "duration_days")
    private Integer durationDays;

    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    @Column(name = "instructions", length = 500)
    private String instructions;

    @DecimalMin(value = "0.0", message = "Unit price must be positive")
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", message = "Total price must be positive")
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Pattern(regexp = "PENDING|DISPENSED|PARTIALLY_DISPENSED|CANCELLED",
            message = "Status must be PENDING, DISPENSED, PARTIALLY_DISPENSED, or CANCELLED")
    @Column(name = "status", length = 20)
    private String status = "PENDING";

    // Constructors
    public PrescriptionMedication() {}

    public PrescriptionMedication(Prescription prescription, Medication medication, 
                                Integer quantity, String dosage, String frequency) {
        this.prescription = prescription;
        this.medication = medication;
        this.quantity = quantity;
        this.dosage = dosage;
        this.frequency = frequency;
        calculateTotalPrice();
    }

    // Business methods
    public void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isDispensed() {
        return "DISPENSED".equals(status);
    }

    // Getters and setters
    public Long getPrescriptionMedicationId() {
        return prescriptionMedicationId;
    }

    public void setPrescriptionMedicationId(Long prescriptionMedicationId) {
        this.prescriptionMedicationId = prescriptionMedicationId;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
        if (medication != null) {
            this.unitPrice = medication.getUnitPrice();
            calculateTotalPrice();
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
