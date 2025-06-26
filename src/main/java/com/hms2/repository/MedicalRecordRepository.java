package com.hms2.repository;

import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordRepository extends GenericRepository<MedicalRecord, Long> {
    
    List<MedicalRecord> findByPatient(Patient patient);
    
    List<MedicalRecord> findByDoctor(Doctor doctor);
    
    List<MedicalRecord> findByAppointment(Appointment appointment);
    
    List<MedicalRecord> findByStatus(String status);
    
    List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<MedicalRecord> findByDiagnosis(String diagnosis);
    
    List<MedicalRecord> findDeletedRecords();
    
    long countByStatus(String status);
    
    long countByPatient(Patient patient);
}
