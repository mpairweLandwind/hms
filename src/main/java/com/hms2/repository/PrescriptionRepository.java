package com.hms2.repository;

import com.hms2.model.Prescription;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.MedicalRecord;
import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository extends GenericRepository<Prescription, Long> {
    
    List<Prescription> findByPatient(Patient patient);
    
    List<Prescription> findByDoctor(Doctor doctor);
    
    List<Prescription> findByMedicalRecord(MedicalRecord medicalRecord);
    
    List<Prescription> findByStatus(String status);
    
    List<Prescription> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Prescription> findActivePrescriptions();
    
    List<Prescription> findExpiredPrescriptions();
    
    List<Prescription> findDeletedPrescriptions();
    
    long countByStatus(String status);
    
    long countByPatient(Patient patient);
}
