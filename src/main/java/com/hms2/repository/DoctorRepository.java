package com.hms2.repository;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends GenericRepository<Doctor, Long> {
    
    Optional<Doctor> findByEmail(String email);
    
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByStatus(String status);
    
    List<Doctor> findByActive(boolean active);
    
    List<Doctor> searchByName(String name);
    
    List<Doctor> findDeletedDoctors();
    
    long countBySpecialization(String specialization);
    
    long countByStatus(String status);
    
    /**
     * Find doctor by user ID
     */
    Optional<Doctor> findByUserId(Long userId);

    void deleteDoctorsByIds(List<Long> doctorIds);
    
    // Only keep CRUD and batch delete logic for DoctorTableDTO as per new implementation
}
