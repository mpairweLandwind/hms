package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_record_seq")
    @SequenceGenerator(name = "medical_record_seq", sequenceName = "medical_record_seq", allocationSize = 1)
    @Column(name = "record_id")
    private Long recordId;

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

    // Many-to-one relationship with Appointment (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @NotNull(message = "Visit date is required")
    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @NotBlank(message = "Chief complaint is required")
    @Size(max = 500, message = "Chief complaint must not exceed 500 characters")
    @Column(name = "chief_complaint", nullable = false, length = 500)
    private String chiefComplaint;

    @Size(max = 1000, message = "History of present illness must not exceed 1000 characters")
    @Column(name = "history_present_illness", length = 1000)
    private String historyPresentIllness;

    @Size(max = 1000, message = "Physical examination must not exceed 1000 characters")
    @Column(name = "physical_examination", length = 1000)
    private String physicalExamination;

    @Size(max = 500, message = "Diagnosis must not exceed 500 characters")
    @Column(name = "diagnosis", length = 500)
    private String diagnosis;

    @Size(max = 1000, message = "Treatment plan must not exceed 1000 characters")
    @Column(name = "treatment_plan", length = 1000)
    private String treatmentPlan;

    @Size(max = 500, message = "Follow-up instructions must not exceed 500 characters")
    @Column(name = "follow_up_instructions", length = 500)
    private String followUpInstructions;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Pattern(regexp = "DRAFT|COMPLETED|REVIEWED|ARCHIVED",
             message = "Status must be DRAFT, COMPLETED, REVIEWED, or ARCHIVED")
    @Column(name = "status", length = 20)
    private String status = "DRAFT";

    // Vital signs
    @DecimalMin(value = "0.0", message = "Temperature must be positive")
    @Column(name = "temperature", precision = 4, scale = 1)
    private Double temperature;

    @Min(value = 0, message = "Blood pressure systolic must be positive")
    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @Min(value = 0, message = "Blood pressure diastolic must be positive")
    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @Min(value = 0, message = "Heart rate must be positive")
    @Column(name = "heart_rate")
    private Integer heartRate;

    @Min(value = 0, message = "Respiratory rate must be positive")
    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @DecimalMin(value = "0.0", message = "Weight must be positive")
    @Column(name = "weight", precision = 5, scale = 2)
    private Double weight;

    @DecimalMin(value = "0.0", message = "Height must be positive")
    @Column(name = "height", precision = 5, scale = 2)
    private Double height;

    // Constructors
    public MedicalRecord() {}

    public MedicalRecord(Patient patient, Doctor doctor, LocalDateTime visitDate, String chiefComplaint) {
        this.patient = patient;
        this.doctor = doctor;
        this.visitDate = visitDate;
        this.chiefComplaint = chiefComplaint;
    }

    // Business methods
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void review() {
        this.status = "REVIEWED";
    }

    public void archive() {
        this.status = "ARCHIVED";
    }

    public Double getBMI() {
        if (weight != null && height != null && height > 0) {
            return weight / (height * height);
        }
        return null;
    }

    public String getBloodPressure() {
        if (bloodPressureSystolic != null && bloodPressureDiastolic != null) {
            return bloodPressureSystolic + "/" + bloodPressureDiastolic;
        }
        return null;
    }

    // Getters and setters
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getHistoryPresentIllness() {
        return historyPresentIllness;
    }

    public void setHistoryPresentIllness(String historyPresentIllness) {
        this.historyPresentIllness = historyPresentIllness;
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

    public String getFollowUpInstructions() {
        return followUpInstructions;
    }

    public void setFollowUpInstructions(String followUpInstructions) {
        this.followUpInstructions = followUpInstructions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
