package com.hms2.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.hms2.dto.prescription.PrescriptionRequestDTO;
import com.hms2.enums.PrescriptionStatus;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;

public interface PrescriptionService {
    
    // Basic CRUD operations
    Prescription save(Prescription prescription);
    Prescription update(Prescription prescription);
    void delete(Long id);
    Prescription findOne(Long id);
    List<Prescription> findAll();

    // Additional methods for compatibility
    Prescription createPrescription(Prescription prescription);
    Prescription updatePrescription(Prescription prescription);
    void deletePrescription(Long prescriptionId);
    Prescription getPrescriptionById(Long prescriptionId);
    List<Prescription> getAllPrescriptions();
    Prescription findById(Long prescriptionId);

    // Search methods
    List<Prescription> findByPatient(Patient patient);
    List<Prescription> findByDoctor(Doctor doctor);
    List<Prescription> findByStatus(String status);
    List<Prescription> findByPatientAndStatus(Patient patient, String status);
    List<Prescription> findByDoctorAndStatus(Doctor doctor, String status);
    List<Prescription> findByDateRange(LocalDate startDate, LocalDate endDate);

    // Status-based methods
    List<Prescription> getActivePrescriptions();
    List<Prescription> getCompletedPrescriptions();
    List<Prescription> getExpiredPrescriptions();
    List<Prescription> getOverduePrescriptions();

    // Status update methods
    void markAsCompleted(Long prescriptionId);
    void markAsExpired(Long prescriptionId);
    void cancelPrescription(Long prescriptionId, String reason);
    void refillPrescription(Long prescriptionId, String refilledBy);

    // Statistics methods
    long countPrescriptionsByStatus(String status);
    long countPrescriptionsByPatient(Patient patient);
    long countPrescriptionsByDoctor(Doctor doctor);
    Map<String, Long> getPrescriptionStatistics();

    // Utility methods
    boolean isPrescriptionValid(Prescription prescription);
    boolean canBeRefilled(Prescription prescription);
    LocalDate calculateExpiryDate(Prescription prescription);
    
    // Soft Delete operations
    List<Prescription> getDeletedPrescriptions();
    
    // ==================== ENHANCED PRESCRIPTION MANAGEMENT ====================
    
    /**
     * Create prescription with medications (for doctors)
     */
    Prescription createPrescriptionWithMedications(PrescriptionRequestDTO prescriptionRequest, String prescribedBy);
    
    /**
     * Update prescription with medications
     */
    Prescription updatePrescriptionWithMedications(Long prescriptionId, PrescriptionRequestDTO prescriptionRequest);
    
    /**
     * Get prescriptions by patient
     */
    List<Prescription> getPrescriptionsByPatient(Patient patient);
    
    /**
     * Get prescriptions by doctor
     */
    List<Prescription> getPrescriptionsByDoctor(Doctor doctor);
    
    /**
     * Get recent prescriptions by doctor
     */
    List<Prescription> getRecentPrescriptionsByDoctor(Doctor doctor);
    
    /**
     * Get overdue prescriptions by doctor
     */
    List<Prescription> getOverduePrescriptionsByDoctor(Doctor doctor);
    
    /**
     * Get active prescriptions by patient
     */
    List<Prescription> getActivePrescriptionsByPatient(Patient patient);
    
    /**
     * Get prescriptions requiring refills
     */
    List<Prescription> getPrescriptionsRequiringRefills();
    
    /**
     * Get prescriptions requiring refills by patient
     */
    List<Prescription> getPrescriptionsRequiringRefillsByPatient(Patient patient);
    
    /**
     * Get prescriptions requiring refills by doctor
     */
    List<Prescription> getPrescriptionsRequiringRefillsByDoctor(Doctor doctor);
    
    /**
     * Refill prescription
     */
    Prescription refillPrescription(Long prescriptionId, String refilledBy, String notes);
    
    /**
     * Get prescription statistics by doctor
     */
    Map<String, Long> getPrescriptionStatisticsByDoctor(Long doctorId);
    
    /**
     * Get prescription statistics by patient
     */
    Map<String, Long> getPrescriptionStatisticsByPatient(Long patientId);
    
    /**
     * Get prescription history by patient
     */
    List<Prescription> getPrescriptionHistoryByPatient(Long patientId);
    
    /**
     * Get prescription history by doctor
     */
    List<Prescription> getPrescriptionHistoryByDoctor(Long doctorId);
    
    /**
     * Check for drug interactions
     */
    List<String> checkDrugInteractions(List<String> medications);
    
    /**
     * Check for allergies
     */
    List<String> checkAllergies(Long patientId, List<String> medications);
    
    /**
     * Calculate prescription cost
     */
    Double calculatePrescriptionCost(Prescription prescription);
    
    /**
     * Get controlled substance prescriptions
     */
    List<Prescription> getControlledSubstancePrescriptions();
    
    /**
     * Get controlled substance prescriptions by doctor
     */
    List<Prescription> getControlledSubstancePrescriptionsByDoctor(Doctor doctor);
    
    /**
     * Validate prescription for insurance coverage
     */
    boolean validateInsuranceCoverage(Prescription prescription, String insuranceProvider);
    
    /**
     * Send prescription refill reminders
     */
    void sendPrescriptionRefillReminders();
    
    /**
     * Get prescriptions expiring soon
     */
    List<Prescription> getPrescriptionsExpiringSoon(int days);
    
    /**
     * Get prescriptions expiring soon by patient
     */
    List<Prescription> getPrescriptionsExpiringSoonByPatient(Long patientId, int days);
    
    /**
     * Get prescriptions expiring soon by doctor
     */
    List<Prescription> getPrescriptionsExpiringSoonByDoctor(Long doctorId, int days);
    
    // ==================== ADDITIONAL METHODS ====================
    
    /**
     * Soft delete prescription
     */
    void softDelete(Long id);
    
    /**
     * Get prescriptions by patient ID
     */
    List<Prescription> getPrescriptionsByPatientId(Long patientId);
    
    /**
     * Get prescriptions by doctor ID
     */
    List<Prescription> getPrescriptionsByDoctorId(Long doctorId);
    
    /**
     * Get active prescriptions by patient ID
     */
    List<Prescription> getActivePrescriptionsByPatient(Long patientId);
    
    /**
     * Find prescriptions by status enum
     */
    List<Prescription> findByStatus(com.hms2.enums.PrescriptionStatus status);
    
    /**
     * Find prescriptions by patient and status enum
     */
    List<Prescription> findByPatientAndStatus(Patient patient, com.hms2.enums.PrescriptionStatus status);
    
    /**
     * Find prescriptions by doctor and date range
     */
    List<Prescription> findByDoctorAndDateRange(Doctor doctor, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    /**
     * Find active prescriptions
     */
    List<Prescription> findActivePrescriptions();
    
    /**
     * Find expired prescriptions
     */
    List<Prescription> findExpiredPrescriptions();
    
    /**
     * Find recent prescriptions
     */
    List<Prescription> findRecentPrescriptions(int limit);
    
    /**
     * Find deleted prescriptions
     */
    List<Prescription> findDeletedPrescriptions();
    
    /**
     * Count prescriptions by status enum
     */
    long countByStatus(com.hms2.enums.PrescriptionStatus status);
    
    /**
     * Count prescriptions by patient
     */
    long countByPatient(Patient patient);
    
    /**
     * Count prescriptions by doctor
     */
    long countByDoctor(Doctor doctor);
    
    /**
     * Get total prescriptions count
     */
    long getTotalPrescriptionsCount();
    
    /**
     * Get prescriptions count by patient ID
     */
    long getPrescriptionsCountByPatient(Long patientId);
    
    /**
     * Check if prescription exists by patient and medication
     */
    boolean existsByPatientAndMedication(Patient patient, String medicationName);
    
    /**
     * Complete prescription
     */
    void completePrescription(Long prescriptionId);
    
    /**
     * Expire prescription
     */
    void expirePrescription(Long prescriptionId);
    
    /**
     * Renew prescription
     */
    void renewPrescription(Long prescriptionId, java.time.LocalDateTime newExpiryDate);
    
    /**
     * Check if prescription exists by ID
     */
    boolean existsById(Long id);
}
