package com.hms2.service;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    
    Doctor createDoctor(Doctor doctor);
    
    Doctor updateDoctor(Doctor doctor);
    
    void deleteDoctor(Long doctorId);
    
    void restoreDoctor(Long doctorId);
    
    void permanentlyDeleteDoctor(Long doctorId);
    
    Optional<Doctor> getDoctorById(Long doctorId);
    
    List<Doctor> getAllDoctors();
    
    List<Doctor> getDeletedDoctors();
    
    List<Doctor> getActiveDoctors();
    
    List<Doctor> getDoctorsByDepartment(Department department);
    
    List<Doctor> getDoctorsBySpecialization(String specialization);
    
    List<Doctor> getDoctorsByStatus(String status);
    
    Optional<Doctor> getDoctorByEmail(String email);
    
    Optional<Doctor> getDoctorByLicenseNumber(String licenseNumber);
    
    List<Doctor> searchDoctorsByName(String name);
    
    void verifyDoctor(Long doctorId);
    
    void rejectDoctor(Long doctorId);
    
    void activateDoctor(Long doctorId);
    
    void deactivateDoctor(Long doctorId);
    
    long getDoctorCountByDepartment(Department department);
    
    long getDoctorCountBySpecialization(String specialization);
    
    long getDoctorCountByStatus(String status);
    
    boolean isEmailUnique(String email);
    
    boolean isLicenseNumberUnique(String licenseNumber);
}
