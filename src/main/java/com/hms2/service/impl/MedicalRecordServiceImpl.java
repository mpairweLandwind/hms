package com.hms2.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hms2.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.repository.MedicalRecordRepository;
import com.hms2.repository.PatientRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import com.hms2.enums.MedicalRecordStatus;

@ApplicationScoped
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    @Inject
    private MedicalRecordRepository medicalRecordRepository;

    @Inject
    private PatientRepository patientRepository;

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        try {
            logger.info("Saving medical record for patient: {}", medicalRecord.getPatient().getPatientId());
            medicalRecord.setCreatedAt(LocalDateTime.now());
            medicalRecord.setUpdatedAt(LocalDateTime.now());
            return medicalRecordRepository.save(medicalRecord);
        } catch (Exception e) {
            logger.error("Error saving medical record", e);
            throw new RuntimeException("Error saving medical record", e);
        }
    }

    @Override
    public Optional<MedicalRecord> findById(Long id) {
        try {
            return medicalRecordRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error finding medical record by id: {}", id, e);
            throw new RuntimeException("Error finding medical record", e);
        }
    }

    @Override
    public List<MedicalRecord> findAll() {
        try {
            return medicalRecordRepository.findAll();
        } catch (Exception e) {
            logger.error("Error finding all medical records", e);
            throw new RuntimeException("Error finding medical records", e);
        }
    }

    @Override
    public MedicalRecord update(MedicalRecord medicalRecord) {
        try {
            logger.info("Updating medical record: {}", medicalRecord.getRecordId());
            medicalRecord.setUpdatedAt(LocalDateTime.now());
            return medicalRecordRepository.update(medicalRecord);
        } catch (Exception e) {
            logger.error("Error updating medical record", e);
            throw new RuntimeException("Error updating medical record", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            logger.info("Deleting medical record: {}", id);
            Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
            if (recordOpt.isPresent()) {
                medicalRecordRepository.delete(recordOpt.get());
            }
            // No exception 'e' is available here, so remove this line or move it inside the catch block if needed
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void softDelete(Long id) {
        try {
            logger.info("Soft deleting medical record: {}", id);
            Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
            if (recordOpt.isPresent()) {
                MedicalRecord record = recordOpt.get();
                record.softDelete("SYSTEM");
                record.setUpdatedAt(LocalDateTime.now());
                medicalRecordRepository.update(record);
            }
        } catch (Exception e) {
            logger.error("Error soft deleting medical record", e);
            throw new RuntimeException("Error soft deleting medical record", e);
        }
    }

    @Override
    public List<MedicalRecord> findByPatient(Patient patient) {
        try {
            return medicalRecordRepository.findByPatient(patient);
        } catch (Exception e) {
            logger.error("Error finding medical records by patient", e);
            throw new RuntimeException("Error finding medical records by patient", e);
        }
    }

    @Override
    public List<MedicalRecord> getRecordsByPatientId(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                return medicalRecordRepository.findByPatient(patientOpt.get());
            }
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding medical records by patient id: {}", patientId, e);
            throw new RuntimeException("Error finding medical records by patient", e);
        }
    }

    @Override
    public List<MedicalRecord> getRecentRecordsByPatient(Long patientId, int limit) {
        try {
            List<MedicalRecord> allRecords = getRecordsByPatientId(patientId);
            return allRecords.stream()
                .sorted((r1, r2) -> r2.getVisitDate().compareTo(r1.getVisitDate()))
                .limit(limit)
                .toList();
        } catch (Exception e) {
            logger.error("Error finding recent medical records by patient id: {}", patientId, e);
            throw new RuntimeException("Error finding recent medical records", e);
        }
    }

    @Override
    public List<MedicalRecord> findByDoctor(Doctor doctor) {
        try {
            return medicalRecordRepository.findByDoctor(doctor);
        } catch (Exception e) {
            logger.error("Error finding medical records by doctor", e);
            throw new RuntimeException("Error finding medical records by doctor", e);
        }
    }

    @Override
    public List<MedicalRecord> getRecordsByDoctorId(Long doctorId) {
        try {
            // This would need DoctorRepository injection to implement properly
            // For now, return empty list
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding medical records by doctor id: {}", doctorId, e);
            throw new RuntimeException("Error finding medical records by doctor", e);
        }
    }

    @Override
    public List<MedicalRecord> findByAppointment(Appointment appointment) {
        try {
            return medicalRecordRepository.findByAppointment(appointment);
        } catch (Exception e) {
            logger.error("Error finding medical records by appointment", e);
            throw new RuntimeException("Error finding medical records by appointment", e);
        }
    }

    @Override
    public List<MedicalRecord> findByStatus(String status) {
        try {
            return medicalRecordRepository.findByStatus(status);
        } catch (Exception e) {
            logger.error("Error finding medical records by status: {}", status, e);
            throw new RuntimeException("Error finding medical records by status", e);
        }
    }

    @Override
    public List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return medicalRecordRepository.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            logger.error("Error finding medical records by date range", e);
            throw new RuntimeException("Error finding medical records by date range", e);
        }
    }

    @Override
    public List<MedicalRecord> findByDiagnosis(String diagnosis) {
        try {
            return medicalRecordRepository.findByDiagnosis(diagnosis);
        } catch (Exception e) {
            logger.error("Error finding medical records by diagnosis: {}", diagnosis, e);
            throw new RuntimeException("Error finding medical records by diagnosis", e);
        }
    }

    @Override
    public List<MedicalRecord> findDeletedRecords() {
        try {
            return medicalRecordRepository.findDeletedRecords();
        } catch (Exception e) {
            logger.error("Error finding deleted medical records", e);
            throw new RuntimeException("Error finding deleted medical records", e);
        }
    }

    @Override
    public long countByStatus(String status) {
        try {
            return medicalRecordRepository.countByStatus(status);
        } catch (Exception e) {
            logger.error("Error counting medical records by status: {}", status, e);
            throw new RuntimeException("Error counting medical records by status", e);
        }
    }

    @Override
    public long countByPatient(Patient patient) {
        try {
            return medicalRecordRepository.countByPatient(patient);
        } catch (Exception e) {
            logger.error("Error counting medical records by patient", e);
            throw new RuntimeException("Error counting medical records by patient", e);
        }
    }

    @Override
    public long getTotalRecordsCount() {
        try {
            return medicalRecordRepository.findAll().size();
        } catch (Exception e) {
            logger.error("Error getting total records count", e);
            throw new RuntimeException("Error getting total records count", e);
        }
    }

    @Override
    public long getRecordsCountByPatient(Long patientId) {
        try {
            Optional<Optional<Patient>> patientOpt = Optional.ofNullable(patientRepository.findById(patientId));
            if (patientOpt.isPresent()) {
                return medicalRecordRepository.count();
            }
            return 0;
        } catch (Exception e) {
            logger.error("Error getting records count by patient id: {}", patientId, e);
            throw new RuntimeException("Error getting records count by patient", e);
        }
    }

    @Override
    public MedicalRecord createRecord(MedicalRecord medicalRecord) {
        try {
            logger.info("Creating new medical record");
            medicalRecord.setStatus(MedicalRecordStatus.DRAFT);
            return save(medicalRecord);
        } catch (Exception e) {
            logger.error("Error creating medical record", e);
            throw new RuntimeException("Error creating medical record", e);
        }
    }

    @Override
    public MedicalRecord updateRecord(MedicalRecord medicalRecord) {
        try {
            return update(medicalRecord);
        } catch (Exception e) {
            logger.error("Error updating medical record", e);
            throw new RuntimeException("Error updating medical record", e);
        }
    }

    @Override
    public void completeRecord(Long recordId) {
        try {
            Optional<MedicalRecord> recordOpt = findById(recordId);
            if (recordOpt.isPresent()) {
                MedicalRecord record = recordOpt.get();
                record.complete();
                update(record);
                logger.info("Medical record {} marked as completed", recordId);
            }
        } catch (Exception e) {
            logger.error("Error completing medical record: {}", recordId, e);
            throw new RuntimeException("Error completing medical record", e);
        }
    }

    @Override
    public void reviewRecord(Long recordId) {
        try {
            Optional<MedicalRecord> recordOpt = findById(recordId);
            if (recordOpt.isPresent()) {
                MedicalRecord record = recordOpt.get();
                record.review();
                update(record);
                logger.info("Medical record {} marked as reviewed", recordId);
            }
        } catch (Exception e) {
            logger.error("Error reviewing medical record: {}", recordId, e);
            throw new RuntimeException("Error reviewing medical record", e);
        }
    }

    @Override
    public void archiveRecord(Long recordId) {
        try {
            Optional<MedicalRecord> recordOpt = findById(recordId);
            if (recordOpt.isPresent()) {
                MedicalRecord record = recordOpt.get();
                record.archive();
                update(record);
                logger.info("Medical record {} archived", recordId);
            }
        } catch (Exception e) {
            logger.error("Error archiving medical record: {}", recordId, e);
            throw new RuntimeException("Error archiving medical record", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return findById(id).isPresent();
        } catch (Exception e) {
            logger.error("Error checking if medical record exists: {}", id, e);
            return false;
        }
    }
}
