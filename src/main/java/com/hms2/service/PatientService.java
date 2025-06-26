package com.hms2.service;

import com.hms2.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    
    Patient createPatient(Patient patient);
    
    Patient updatePatient(Patient patient);
    
    void deletePatient(Long patientId);
    
    void restorePatient(Long patientId);
    
    void permanentlyDeletePatient(Long patientId);
    
    Optional<Patient> getPatientById(Long patientId);
    
    List<Patient> getAllPatients();
    
    List<Patient> getDeletedPatients();
    
    List<Patient> getActivePatients();
    
    List<Patient> getInactivePatients();
    
    Optional<Patient> getPatientByEmail(String email);
    
    List<Patient> searchPatientsByName(String name);
    
    List<Patient> getPatientsByBloodType(String bloodType);
    
    List<Patient> getPatientsByGender(String gender);
    
    void activatePatient(Long patientId);
    
    void deactivatePatient(Long patientId);
    
    long getPatientCountByGender(String gender);
    
    long getActivePatientCount();
    
    boolean isEmailUnique(String email);
}
