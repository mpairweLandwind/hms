package com.hms2.repository;

import com.hms2.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends GenericRepository<Patient, Long> {
    
    Optional<Patient> findByEmail(String email);
    
    List<Patient> findByActive(boolean active);
    
    List<Patient> searchByName(String name);
    
    List<Patient> findByBloodType(String bloodType);
    
    List<Patient> findByGender(String gender);
    
    List<Patient> findDeletedPatients();
    
    long countByActive(boolean active);
    
    long countByGender(String gender);
}
