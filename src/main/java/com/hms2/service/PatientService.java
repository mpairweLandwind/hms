package com.hms2.service;

import java.util.List;

import com.hms2.model.Doctor;
import com.hms2.model.Patient;

public interface PatientService {

    // Basic CRUD operations
    Patient save(Patient patient);
    Patient update(Patient patient);
    void delete(Long id);
    Patient findOne(Long id);
    List<Patient> findAll();

    // Soft Delete operations
    void softDelete(Long id);
    void restore(Long id);
    List<Patient> getDeletedPatients();
    void permanentlyDelete(Long id);

    // Additional methods for compatibility
    Patient createPatient(Patient patient);
    Patient updatePatient(Patient patient);
    void deletePatient(Long patientId);
    Patient getPatientById(Long patientId);
    List<Patient> getAllPatients();
    Patient findById(Long patientId);

    // Business methods
    Patient getPatientByEmail(String email);
    List<Patient> searchPatientsByName(String name);
    List<Patient> getPatientsByBloodType(String bloodType);
    List<Patient> getRecentPatientsByDoctor(Doctor doctor);
    long getTotalPatientCountByDoctor(Doctor doctor);
    List<Patient> getActivePatients();
    List<Patient> getInactivePatients();
    void activatePatient(Long patientId);
    void deactivatePatient(Long patientId);
    long getTotalPatientCount();
    long getActivePatientCount();
    long getNewPatientsThisMonth();
    boolean hasAllergies(Long patientId);
    int calculateAge(Long patientId);
    
    /**
     * Find patient by user ID
     */
    Patient findByUserId(Long userId);
}
