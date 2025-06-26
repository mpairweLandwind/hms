package com.hms2.service.impl;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import com.hms2.repository.DoctorRepository;
import com.hms2.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DoctorServiceImpl implements DoctorService {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Override
    public Doctor createDoctor(Doctor doctor) {
        logger.info("Creating new doctor: {}", doctor.getFullName());
        
        if (!isEmailUnique(doctor.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        if (!isLicenseNumberUnique(doctor.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists");
        }
        
        return doctorRepository.save(doctor);
    }
    
    @Override
    public Doctor updateDoctor(Doctor doctor) {
        logger.info("Updating doctor: {}", doctor.getFullName());
        
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctor.getDoctorId());
        if (existingDoctor.isEmpty()) {
            throw new IllegalArgumentException("Doctor not found");
        }
        
        Doctor existing = existingDoctor.get();
        
        // Check unique constraints only if values changed
        if (!existing.getEmail().equals(doctor.getEmail()) && 
            !isEmailUnique(doctor.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        if (!existing.getLicenseNumber().equals(doctor.getLicenseNumber()) && 
            !isLicenseNumberUnique(doctor.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists");
        }
        
        return doctorRepository.update(doctor);
    }
    
    @Override
    public void deleteDoctor(Long doctorId) {
        logger.info("Soft deleting doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.softDelete("SYSTEM");
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public void restoreDoctor(Long doctorId) {
        logger.info("Restoring doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.restore("SYSTEM");
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public void permanentlyDeleteDoctor(Long doctorId) {
        logger.info("Permanently deleting doctor with ID: {}", doctorId);
        doctorRepository.deleteById(doctorId);
    }
    
    @Override
    public Optional<Doctor> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }
    
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    @Override
    public List<Doctor> getDeletedDoctors() {
        return doctorRepository.findDeletedDoctors();
    }
    
    @Override
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByActive(true);
    }
    
    @Override
    public List<Doctor> getDoctorsByDepartment(Department department) {
        return doctorRepository.findByDepartment(department);
    }
    
    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    
    @Override
    public List<Doctor> getDoctorsByStatus(String status) {
        return doctorRepository.findByStatus(status);
    }
    
    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    
    @Override
    public Optional<Doctor> getDoctorByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber);
    }
    
    @Override
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.searchByName(name);
    }
    
    @Override
    public void verifyDoctor(Long doctorId) {
        logger.info("Verifying doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.verify();
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public void rejectDoctor(Long doctorId) {
        logger.info("Rejecting doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.reject();
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public void activateDoctor(Long doctorId) {
        logger.info("Activating doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setActive(true);
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public void deactivateDoctor(Long doctorId) {
        logger.info("Deactivating doctor with ID: {}", doctorId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setActive(false);
            doctorRepository.update(doctor);
        } else {
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Override
    public long getDoctorCountByDepartment(Department department) {
        return doctorRepository.countByDepartment(department);
    }
    
    @Override
    public long getDoctorCountBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }
    
    @Override
    public long getDoctorCountByStatus(String status) {
        return doctorRepository.countByStatus(status);
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        return doctorRepository.findByEmail(email).isEmpty();
    }
    
    @Override
    public boolean isLicenseNumberUnique(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber).isEmpty();
    }
}
