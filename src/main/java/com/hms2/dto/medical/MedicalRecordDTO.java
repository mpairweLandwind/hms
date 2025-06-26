package com.hms2.dto.medical;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

public class MedicalRecordDTO {
    
    private Long recordId;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Visit date is required")
    private Date visitDate;
    
    @NotBlank(message = "Chief complaint is required")
    @Size(max = 500, message = "Chief complaint must not exceed 500 characters")
    private String chiefComplaint;
    
    @Size(max = 1000, message = "History of present illness must not exceed 1000 characters")
    private String historyOfPresentIllness;
    
    @Size(max = 1000, message = "Physical examination must not exceed 1000 characters")
    private String physicalExamination;
    
    @Size(max = 500, message = "Diagnosis must not exceed 500 characters")
    private String diagnosis;
    
    @Size(max = 1000, message = "Treatment plan must not exceed 1000 characters")
    private String treatmentPlan;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    // Vital signs
    @DecimalMin(value = "0.0", message = "Temperature must be positive")
    @DecimalMax(value = "50.0", message = "Temperature cannot exceed 50°C")
    private BigDecimal temperature;
    
    @Min(value = 0, message = "Blood pressure systolic must be positive")
    @Max(value = 300, message = "Blood pressure systolic cannot exceed 300")
    private Integer bloodPressureSystolic;
    
    @Min(value = 0, message = "Blood pressure diastolic must be positive")
    @Max(value = 200, message = "Blood pressure diastolic cannot exceed 200")
    private Integer bloodPressureDiastolic;
    
    @Min(value = 0, message = "Heart rate must be positive")
    @Max(value = 300, message = "Heart rate cannot exceed 300")
    private Integer heartRate;
    
    @Min(value = 0, message = "Respiratory rate must be positive")
    @Max(value = 100, message = "Respiratory rate cannot exceed 100")
    private Integer respiratoryRate;
    
    @DecimalMin(value = "0.0", message = "Weight must be positive")
    @DecimalMax(value = "1000.0", message = "Weight cannot exceed 1000 kg")
    private BigDecimal weight;
    
    @DecimalMin(value = "0.0", message = "Height must be positive")
    @DecimalMax(value = "300.0", message = "Height cannot exceed 300 cm")
    private BigDecimal height;
    
    // Calculated fields
    private BigDecimal bmi;
    private String bmiCategory;
    
    // Patient and doctor names for display
    private String patientName;
    private String doctorName;
    
    // Constructors
    public MedicalRecordDTO() {}
    
    // Business methods
    public void calculateBMI() {
        if (weight != null && height != null && height.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal heightInMeters = height.divide(new BigDecimal("100"));
            bmi = weight.divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);
            
            if (bmi.compareTo(new BigDecimal("18.5")) < 0) {
                bmiCategory = "Underweight";
            } else if (bmi.compareTo(new BigDecimal("25")) < 0) {
                bmiCategory = "Normal";
            } else if (bmi.compareTo(new BigDecimal("30")) < 0) {
                bmiCategory = "Overweight";
            } else {
                bmiCategory = "Obese";
            }
        }
    }
    
    // Getters and setters
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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
    
    public Date getVisitDate() {
        return visitDate;
    }
    
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
    
    public String getChiefComplaint() {
        return chiefComplaint;
    }
    
    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }
    
    public String getHistoryOfPresentIllness() {
        return historyOfPresentIllness;
    }
    
    public void setHistoryOfPresentIllness(String historyOfPresentIllness) {
        this.historyOfPresentIllness = historyOfPresentIllness;
    }
    
    public String getPhysicalExamination() {
        return physicalExamination;
    }
    
    public void setPhysicalExamination(String physicalExamination) {
        this.physicalExamination = physicalExamination;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getTreatmentPlan() {
        return treatmentPlan;
    }
    
    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public BigDecimal getTemperature() {
        return temperature;
    }
    
    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }
    
    public Integer getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }
    
    public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }
    
    public Integer getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }
    
    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }
    
    public Integer getHeartRate() {
        return heartRate;
    }
    
    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }
    
    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }
    
    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }
    
    public BigDecimal getWeight() {
        return weight;
    }
    
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
        calculateBMI();
    }
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public void setHeight(BigDecimal height) {
        this.height = height;
        calculateBMI();
    }
    
    public BigDecimal getBmi() {
        return bmi;
    }
    
    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }
    
    public String getBmiCategory() {
        return bmiCategory;
    }
    
    public void setBmiCategory(String bmiCategory) {
        this.bmiCategory = bmiCategory;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
