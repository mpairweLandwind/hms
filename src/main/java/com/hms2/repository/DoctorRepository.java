package com.hms2.repository;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends GenericRepository<Doctor, Long> {
    
    Optional<Doctor> findByEmail(String email);
    
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    List<Doctor> findByDepartment(Department department);
    
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByStatus(String status);
    
    List<Doctor> findByActive(boolean active);
    
    List<Doctor> searchByName(String name);
    
    List<Doctor> findDeletedDoctors();
    
    long countByDepartment(Department department);
    
    long countBySpecialization(String specialization);
    
    long countByStatus(String status);
}
