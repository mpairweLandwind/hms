package com.hms2.dto.prescription;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PrescriptionRequestDTO {
    
    private Long prescriptionId;
    
    @NotNull(message = "Patient is required")
    private Long patientId;
    
    @NotNull(message = "Doctor is required")
    private Long doctorId;
    
    @NotNull(message = "Prescription date is required")
    private Date prescriptionDate;
    
    @Size(max = 1000, message = "Diagnosis must not exceed 1000 characters")
    private String diagnosis;
    
    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters")
    private String symptoms;
    
    @Size(max = 1000, message = "Treatment plan must not exceed 1000 characters")
    private String treatmentPlan;
    
    @Size(max = 100, message = "Status must not exceed 100 characters")
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED, EXPIRED
    
    private LocalDate validUntil;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    private Boolean followUpRequired = false;
    
    private Date followUpDate;
    
    @Size(max = 100, message = "Follow-up type must not exceed 100 characters")
    private String followUpType; // REVIEW, TEST, PROCEDURE
    
    // Medication details
    private List<MedicationDTO> medications;
    
    // Allergy information
    private String allergies;
    private String allergyReactions;
    
    // Insurance and billing
    private String insuranceProvider;
    private String insuranceNumber;
    private Boolean insuranceCovered = false;
    private Double totalCost;
    
    // Prescription metadata
    private String prescribedBy;
    private Date prescribedDate;
    private String prescriptionNumber;
    private Boolean isRefillable = false;
    private Integer refillCount = 0;
    private Integer maxRefills = 0;
    
    // Constructors
    public PrescriptionRequestDTO() {}
    
    public PrescriptionRequestDTO(Long patientId, Long doctorId, Date prescriptionDate, String diagnosis) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
    }
    
    // Getters and Setters
    public Long getPrescriptionId() {
        return prescriptionId;
    }
    
    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
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
    
    public Date getPrescriptionDate() {
        return prescriptionDate;
    }
    
    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getSymptoms() {
        return symptoms;
    }
    
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    
    public String getTreatmentPlan() {
        return treatmentPlan;
    }
    
    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getValidUntil() {
        return validUntil;
    }
    
    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public String getFollowUpType() {
        return followUpType;
    }
    
    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType;
    }
    
    public List<MedicationDTO> getMedications() {
        return medications;
    }
    
    public void setMedications(List<MedicationDTO> medications) {
        this.medications = medications;
    }
    
    public String getAllergies() {
        return allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    public String getAllergyReactions() {
        return allergyReactions;
    }
    
    public void setAllergyReactions(String allergyReactions) {
        this.allergyReactions = allergyReactions;
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
    
    public Double getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
    
    public String getPrescribedBy() {
        return prescribedBy;
    }
    
    public void setPrescribedBy(String prescribedBy) {
        this.prescribedBy = prescribedBy;
    }
    
    public Date getPrescribedDate() {
        return prescribedDate;
    }
    
    public void setPrescribedDate(Date prescribedDate) {
        this.prescribedDate = prescribedDate;
    }
    
    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }
    
    public void setPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }
    
    public Boolean getIsRefillable() {
        return isRefillable;
    }
    
    public void setIsRefillable(Boolean isRefillable) {
        this.isRefillable = isRefillable;
    }
    
    public Integer getRefillCount() {
        return refillCount;
    }
    
    public void setRefillCount(Integer refillCount) {
        this.refillCount = refillCount;
    }
    
    public Integer getMaxRefills() {
        return maxRefills;
    }
    
    public void setMaxRefills(Integer maxRefills) {
        this.maxRefills = maxRefills;
    }
    
    @Override
    public String toString() {
        return "PrescriptionRequestDTO{" +
                "prescriptionId=" + prescriptionId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", prescriptionDate=" + prescriptionDate +
                ", diagnosis='" + diagnosis + '\'' +
                ", status='" + status + '\'' +
                ", medications=" + medications +
                '}';
    }
} 