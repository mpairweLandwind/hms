package com.hms2.service.impl;

import com.hms2.model.Patient;
import com.hms2.repository.PatientRepository;
import com.hms2.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PatientServiceImpl implements PatientService {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);
    
    @Inject
    private PatientRepository patientRepository;
    
    @Override
    public Patient createPatient(Patient patient) {
        logger.info("Creating new patient: {}", patient.getFullName());
        
        if (!isEmailUnique(patient.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return patientRepository.save(patient);
    }
    
    @Override
    public Patient updatePatient(Patient patient) {
        logger.info("Updating patient: {}", patient.getFullName());
        
        Optional<Patient> existingPatient = patientRepository.findById(patient.getPatientId());
        if (existingPatient.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        
        // Check email uniqueness only if changed
        Patient existing = existingPatient.get();
        if (!existing.getEmail().equals(patient.getEmail()) && 
            !isEmailUnique(patient.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return patientRepository.update(patient);
    }
    
    @Override
    public void deletePatient(Long patientId) {
        logger.info("Soft deleting patient with ID: {}", patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.softDelete("SYSTEM");
            patientRepository.update(patient);
        } else {
            throw new IllegalArgumentException("Patient not found");
        }
    }
    
    @Override
    public void restorePatient(Long patientId) {
        logger.info("Restoring patient with ID: {}", patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.restore("SYSTEM");
            patientRepository.update(patient);
        } else {
            throw new IllegalArgumentException("Patient not found");
        }
    }
    
    @Override
    public void permanentlyDeletePatient(Long patientId) {
        logger.info("Permanently deleting patient with ID: {}", patientId);
        patientRepository.deleteById(patientId);
    }
    
    @Override
    public Optional<Patient> getPatientById(Long patientId) {
        return patientRepository.findById(patientId);
    }
    
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    @Override
    public List<Patient> getDeletedPatients() {
        return patientRepository.findDeletedPatients();
    }
    
    @Override
    public List<Patient> getActivePatients() {
        return patientRepository.findByActive(true);
    }
    
    @Override
    public List<Patient> getInactivePatients() {
        return patientRepository.findByActive(false);
    }
    
    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
    
    @Override
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.searchByName(name);
    }
    
    @Override
    public List<Patient> getPatientsByBloodType(String bloodType) {
        return patientRepository.findByBloodType(bloodType);
    }
    
    @Override
    public List<Patient> getPatientsByGender(String gender) {
        return patientRepository.findByGender(gender);
    }
    
    @Override
    public void activatePatient(Long patientId) {
        logger.info("Activating patient with ID: {}", patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setActive(true);
            patientRepository.update(patient);
        } else {
            throw new IllegalArgumentException("Patient not found");
        }
    }
    
    @Override
    public void deactivatePatient(Long patientId) {
        logger.info("Deactivating patient with ID: {}", patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setActive(false);
            patientRepository.update(patient);
        } else {
            throw new IllegalArgumentException("Patient not found");
        }
    }
    
    @Override
    public long getPatientCountByGender(String gender) {
        return patientRepository.countByGender(gender);
    }
    
    @Override
    public long getActivePatientCount() {
        return patientRepository.countByActive(true);
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        return patientRepository.findByEmail(email).isEmpty();
    }
}
