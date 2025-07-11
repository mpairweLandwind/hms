package com.hms2.service;

import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    
    MedicalRecord save(MedicalRecord medicalRecord);
    
    Optional<MedicalRecord> findById(Long id);
    
    List<MedicalRecord> findAll();
    
    MedicalRecord update(MedicalRecord medicalRecord);
    
    void delete(Long id);
    
    void softDelete(Long id);
    
    List<MedicalRecord> findByPatient(Patient patient);
    
    List<MedicalRecord> getRecordsByPatientId(Long patientId);
    
    List<MedicalRecord> getRecentRecordsByPatient(Long patientId, int limit);
    
    List<MedicalRecord> findByDoctor(Doctor doctor);
    
    List<MedicalRecord> getRecordsByDoctorId(Long doctorId);
    
    List<MedicalRecord> findByAppointment(Appointment appointment);
    
    List<MedicalRecord> findByStatus(String status);
    
    List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<MedicalRecord> findByDiagnosis(String diagnosis);
    
    List<MedicalRecord> findDeletedRecords();
    
    long countByStatus(String status);
    
    long countByPatient(Patient patient);
    
    long getTotalRecordsCount();
    
    long getRecordsCountByPatient(Long patientId);
    
    MedicalRecord createRecord(MedicalRecord medicalRecord);
    
    MedicalRecord updateRecord(MedicalRecord medicalRecord);
    
    void completeRecord(Long recordId);
    
    void reviewRecord(Long recordId);
    
    void archiveRecord(Long recordId);
    
    boolean existsById(Long id);
}
