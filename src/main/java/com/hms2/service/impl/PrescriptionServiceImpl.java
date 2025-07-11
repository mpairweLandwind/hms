package com.hms2.service.impl;

import com.hms2.model.Prescription;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.enums.PrescriptionStatus;
import com.hms2.repository.PrescriptionRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@ApplicationScoped
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionServiceImpl.class);
    
    @Inject
    private PrescriptionRepository prescriptionRepository;
    
    @Inject
    private PatientRepository patientRepository;
    
    @Override
    public Prescription save(Prescription prescription) {
        try {
            System.out.println("Saving prescription for patient: " + prescription.getPatient().getPatientId());
            prescription.setCreatedAt(LocalDateTime.now());
            prescription.setUpdatedAt(LocalDateTime.now());
            return prescriptionRepository.save(prescription);
        } catch (Exception e) {
            System.err.println("Error saving prescription: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error saving prescription", e);
        }
    }
    
    @Override
    public Prescription findById(Long id) {
        try {
            Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
            return prescriptionOpt.orElse(null);
        } catch (Exception e) {
            logger.error("Error finding prescription by id: {}", id, e);
            throw new RuntimeException("Error finding prescription", e);
        }
    }
    
    @Override
    public List<Prescription> findAll() {
        try {
            return prescriptionRepository.findAll();
        } catch (Exception e) {
            logger.error("Error finding all prescriptions", e);
            throw new RuntimeException("Error finding prescriptions", e);
        }
    }
    
    @Override
    public Prescription update(Prescription prescription) {
        try {
            logger.info("Updating prescription: {}", prescription.getPrescriptionId());
            prescription.setUpdatedAt(LocalDateTime.now());
            return prescriptionRepository.update(prescription);
        } catch (Exception e) {
            logger.error("Error updating prescription", e);
            throw new RuntimeException("Error updating prescription", e);
        }
    }
    
    @Override
    public void delete(Long id) {
        try {
            logger.info("Deleting prescription: {}", id);
            Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
            if (prescriptionOpt.isPresent()) {
                prescriptionRepository.delete(prescriptionOpt.get());
            }
        } catch (Exception e) {
            logger.error("Error deleting prescription", e);
            throw new RuntimeException("Error deleting prescription", e);
        }
    }
    
    @Override
    public void softDelete(Long id) {
        try {
            logger.info("Soft deleting prescription: {}", id);
            Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
            if (prescriptionOpt.isPresent()) {
                Prescription prescription = prescriptionOpt.get();
                prescription.softDelete("SYSTEM");
                prescription.setUpdatedAt(LocalDateTime.now());
                prescriptionRepository.update(prescription);
            }
        } catch (Exception e) {
            logger.error("Error soft deleting prescription", e);
            throw new RuntimeException("Error soft deleting prescription", e);
        }
    }
    
    @Override
    public List<Prescription> findByPatient(Patient patient) {
        try {
            return prescriptionRepository.findByPatient(patient);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by patient", e);
            throw new RuntimeException("Error finding prescriptions by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                return prescriptionRepository.findByPatient(patientOpt.get());
            }
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by patient id: {}", patientId, e);
            throw new RuntimeException("Error finding prescriptions by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getActivePrescriptionsByPatient(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                return prescriptionRepository.findByPatientAndStatus(patientOpt.get(), PrescriptionStatus.ACTIVE);
            }
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding active prescriptions by patient id: {}", patientId, e);
            throw new RuntimeException("Error finding active prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findByDoctor(Doctor doctor) {
        try {
            return prescriptionRepository.findByDoctor(doctor);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by doctor", e);
            throw new RuntimeException("Error finding prescriptions by doctor", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        try {
            // This would need DoctorRepository injection to implement properly
            // For now, return empty list
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by doctor id: {}", doctorId, e);
            throw new RuntimeException("Error finding prescriptions by doctor", e);
        }
    }
    
    @Override
    public List<Prescription> findByStatus(PrescriptionStatus status) {
        try {
            return prescriptionRepository.findByStatus(status);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by status: {}", status, e);
            throw new RuntimeException("Error finding prescriptions by status", e);
        }
    }
    
    @Override
    public List<Prescription> findByStatus(String status) {
        try {
            // Convert string status to enum if needed
            PrescriptionStatus enumStatus = PrescriptionStatus.valueOf(status.toUpperCase());
            return prescriptionRepository.findByStatus(enumStatus);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by status: {}", status, e);
            throw new RuntimeException("Error finding prescriptions by status", e);
        }
    }
    
    @Override
    public List<Prescription> findByPatientAndStatus(Patient patient, PrescriptionStatus status) {
        try {
            return prescriptionRepository.findByPatientAndStatus(patient, status);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by patient and status", e);
            throw new RuntimeException("Error finding prescriptions by patient and status", e);
        }
    }
    
    @Override
    public List<Prescription> findByPatientAndStatus(Patient patient, String status) {
        try {
            // Convert string status to enum if needed
            PrescriptionStatus enumStatus = PrescriptionStatus.valueOf(status.toUpperCase());
            return prescriptionRepository.findByPatientAndStatus(patient, enumStatus);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by patient and status", e);
            throw new RuntimeException("Error finding prescriptions by patient and status", e);
        }
    }
    
    //@Override
    public List<Prescription> findByDoctorAndStatus(Doctor doctor, PrescriptionStatus status) {
        return prescriptionRepository.findByDoctorAndStatus(doctor, status);
    }

    @Override
    public List<Prescription> findByDoctorAndStatus(Doctor doctor, String status) {
        try {
            PrescriptionStatus enumStatus = PrescriptionStatus.valueOf(status.toUpperCase());
            return findByDoctorAndStatus(doctor, enumStatus);
        } catch (Exception e) {
            logger.error("Invalid status: {}", status, e);
            return List.of();
        }
    }
    
    @Override
    public List<Prescription> findByDateRange(LocalDate startDate, LocalDate endDate) {
        // Not implemented in repository, return empty list
        return List.of();
    }
    
    @Override
    public List<Prescription> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return prescriptionRepository.findByDoctorAndDateRange(doctor, startDate, endDate);
        } catch (Exception e) {
            logger.error("Error finding prescriptions by doctor and date range", e);
            throw new RuntimeException("Error finding prescriptions by doctor and date range", e);
        }
    }
    
    @Override
    public List<Prescription> findActivePrescriptions() {
        try {
            return prescriptionRepository.findActivePrescriptions();
        } catch (Exception e) {
            logger.error("Error finding active prescriptions", e);
            throw new RuntimeException("Error finding active prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findExpiredPrescriptions() {
        try {
            return prescriptionRepository.findExpiredPrescriptions();
        } catch (Exception e) {
            logger.error("Error finding expired prescriptions", e);
            throw new RuntimeException("Error finding expired prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findRecentPrescriptions(int limit) {
        try {
            return prescriptionRepository.findRecentPrescriptions(limit);
        } catch (Exception e) {
            logger.error("Error finding recent prescriptions", e);
            throw new RuntimeException("Error finding recent prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findDeletedPrescriptions() {
        try {
            return prescriptionRepository.findDeletedPrescriptions();
        } catch (Exception e) {
            logger.error("Error finding deleted prescriptions", e);
            throw new RuntimeException("Error finding deleted prescriptions", e);
        }
    }
    
    @Override
    public long countByStatus(PrescriptionStatus status) {
        try {
            return prescriptionRepository.countByStatus(status);
        } catch (Exception e) {
            logger.error("Error counting prescriptions by status: {}", status, e);
            throw new RuntimeException("Error counting prescriptions by status", e);
        }
    }
    
    @Override
    public long countByPatient(Patient patient) {
        try {
            return prescriptionRepository.countByPatient(patient);
        } catch (Exception e) {
            logger.error("Error counting prescriptions by patient", e);
            throw new RuntimeException("Error counting prescriptions by patient", e);
        }
    }
    
    @Override
    public long countByDoctor(Doctor doctor) {
        try {
            return prescriptionRepository.countByDoctor(doctor);
        } catch (Exception e) {
            logger.error("Error counting prescriptions by doctor", e);
            throw new RuntimeException("Error counting prescriptions by doctor", e);
        }
    }
    
    @Override
    public long getTotalPrescriptionsCount() {
        try {
            return prescriptionRepository.findAll().size();
        } catch (Exception e) {
            logger.error("Error getting total prescriptions count", e);
            throw new RuntimeException("Error getting total prescriptions count", e);
        }
    }
    
    @Override
    public long getPrescriptionsCountByPatient(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                return prescriptionRepository.countByPatient(patientOpt.get());
            }
            return 0;
        } catch (Exception e) {
            logger.error("Error getting prescriptions count by patient id: {}", patientId, e);
            throw new RuntimeException("Error getting prescriptions count by patient", e);
        }
    }
    
    @Override
    public boolean existsByPatientAndMedication(Patient patient, String medicationName) {
        try {
            return prescriptionRepository.existsByPatientAndMedication(patient, medicationName);
        } catch (Exception e) {
            logger.error("Error checking prescription existence by patient and medication", e);
            throw new RuntimeException("Error checking prescription existence", e);
        }
    }
    
    @Override
    public Prescription createPrescription(Prescription prescription) {
        try {
            logger.info("Creating new prescription");
            prescription.setStatus("ACTIVE");
            prescription.setPrescriptionDate(LocalDateTime.now());
            return save(prescription);
        } catch (Exception e) {
            logger.error("Error creating prescription", e);
            throw new RuntimeException("Error creating prescription", e);
        }
    }
    
    @Override
    public Prescription updatePrescription(Prescription prescription) {
        try {
            return update(prescription);
        } catch (Exception e) {
            logger.error("Error updating prescription", e);
            throw new RuntimeException("Error updating prescription", e);
        }
    }
    
    @Override
    public void completePrescription(Long prescriptionId) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.complete();
                update(prescription);
                logger.info("Prescription {} marked as completed", prescriptionId);
            }
        } catch (Exception e) {
            logger.error("Error completing prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error completing prescription", e);
        }
    }
    
//@Override
    public void cancelPrescription(Long prescriptionId) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.cancel();
                update(prescription);
                logger.info("Prescription {} cancelled", prescriptionId);
            }
        } catch (Exception e) {
            logger.error("Error cancelling prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error cancelling prescription", e);
        }
    }
    
    @Override
    public void expirePrescription(Long prescriptionId) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.expire();
                update(prescription);
                logger.info("Prescription {} expired", prescriptionId);
            }
        } catch (Exception e) {
            logger.error("Error expiring prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error expiring prescription", e);
        }
    }
    
    @Override
    public void renewPrescription(Long prescriptionId, LocalDateTime newExpiryDate) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.setExpiryDate(newExpiryDate);
                prescription.setStatus("ACTIVE");
                update(prescription);
                logger.info("Prescription {} renewed until {}", prescriptionId, newExpiryDate);
            }
        } catch (Exception e) {
            logger.error("Error renewing prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error renewing prescription", e);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        try {
            return findById(id) != null;
        } catch (Exception e) {
            logger.error("Error checking if prescription exists: {}", id, e);
            return false;
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsExpiringSoonByPatient(Long patientId, int days) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                // This would need more sophisticated logic to check expiry dates
                // For now, return empty list as placeholder
                logger.info("Getting prescriptions expiring soon for patient: {} within {} days", patientId, days);
                return List.of();
            }
            return List.of();
        } catch (Exception e) {
            logger.error("Error getting prescriptions expiring soon for patient: {}", patientId, e);
            throw new RuntimeException("Error getting prescriptions expiring soon", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsExpiringSoonByDoctor(Long doctorId, int days) {
        try {
            // This would need DoctorRepository injection to implement properly
            // For now, return empty list as placeholder
            logger.info("Getting prescriptions expiring soon for doctor: {} within {} days", doctorId, days);
            return List.of();
        } catch (Exception e) {
            logger.error("Error getting prescriptions expiring soon for doctor: {}", doctorId, e);
            throw new RuntimeException("Error getting prescriptions expiring soon", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsExpiringSoon(int days) {
        try {
            // This would need more sophisticated logic to check expiry dates
            // For now, return empty list as placeholder
            logger.info("Getting prescriptions expiring soon within {} days", days);
            return List.of();
        } catch (Exception e) {
            logger.error("Error getting prescriptions expiring soon within {} days", days, e);
            throw new RuntimeException("Error getting prescriptions expiring soon", e);
        }
    }
    
    @Override
    public List<Prescription> getExpiredPrescriptions() {
        return findExpiredPrescriptions();
    }
    
    // ==================== ADDITIONAL MISSING METHODS ====================
    
    @Override
    public Prescription findOne(Long id) {
        return findById(id);
    }
    
    @Override
    public void deletePrescription(Long prescriptionId) {
        delete(prescriptionId);
    }
    
    @Override
    public Prescription getPrescriptionById(Long prescriptionId) {
        return findById(prescriptionId);
    }
    
    @Override
    public List<Prescription> getAllPrescriptions() {
        return findAll();
    }
    
    @Override
    public List<Prescription> getCompletedPrescriptions() {
        try {
            return prescriptionRepository.findByStatus(PrescriptionStatus.COMPLETED);
        } catch (Exception e) {
            logger.error("Error finding completed prescriptions", e);
            throw new RuntimeException("Error finding completed prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> getOverduePrescriptions() {
        try {
            // This would need more sophisticated logic to check overdue status
            // For now, return empty list as placeholder
            logger.info("Getting overdue prescriptions");
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding overdue prescriptions", e);
            throw new RuntimeException("Error finding overdue prescriptions", e);
        }
    }
    
    @Override
    public void markAsCompleted(Long prescriptionId) {
        completePrescription(prescriptionId);
    }
    
    @Override
    public void markAsExpired(Long prescriptionId) {
        expirePrescription(prescriptionId);
    }
    
    @Override
    public void cancelPrescription(Long prescriptionId, String reason) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.cancel();
                prescription.setNotes(reason);
                update(prescription);
                logger.info("Prescription {} cancelled with reason: {}", prescriptionId, reason);
            }
        } catch (Exception e) {
            logger.error("Error cancelling prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error cancelling prescription", e);
        }
    }
    
    @Override
    public void refillPrescription(Long prescriptionId, String refilledBy) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.setStatus("ACTIVE");
                prescription.setNotes(null); // Removed setRefillDate
               // prescription.setRefilledBy(null); // Removed setRefilledBy
                update(prescription);
                logger.info("Prescription {} refilled by: {}", prescriptionId, refilledBy);
            }
        } catch (Exception e) {
            logger.error("Error refilling prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error refilling prescription", e);
        }
    }
    
    @Override
    public long countPrescriptionsByStatus(String status) {
        try {
            PrescriptionStatus enumStatus = PrescriptionStatus.valueOf(status.toUpperCase());
            return prescriptionRepository.countByStatus(enumStatus);
        } catch (Exception e) {
            logger.error("Error counting prescriptions by status: {}", status, e);
            throw new RuntimeException("Error counting prescriptions by status", e);
        }
    }
    
    @Override
    public long countPrescriptionsByPatient(Patient patient) {
        return countByPatient(patient);
    }
    
    @Override
    public long countPrescriptionsByDoctor(Doctor doctor) {
        return countByDoctor(doctor);
    }
    
    @Override
    public Map<String, Long> getPrescriptionStatistics() {
        try {
            Map<String, Long> stats = new HashMap<>();
            stats.put("total", Long.valueOf(getTotalPrescriptionsCount()));
            stats.put("active", Long.valueOf(countByStatus(PrescriptionStatus.ACTIVE)));
            stats.put("completed", Long.valueOf(countByStatus(PrescriptionStatus.COMPLETED)));
            stats.put("expired", Long.valueOf(countByStatus(PrescriptionStatus.EXPIRED)));
            stats.put("cancelled", Long.valueOf(countByStatus(PrescriptionStatus.CANCELLED)));
            return stats;
        } catch (Exception e) {
            logger.error("Error getting prescription statistics", e);
            throw new RuntimeException("Error getting prescription statistics", e);
        }
    }
    
    @Override
    public boolean isPrescriptionValid(Prescription prescription) {
        try {
            return prescription != null && 
                   prescription.getExpiryDate() != null && 
                   prescription.getExpiryDate().isAfter(LocalDateTime.now()) &&
                   "ACTIVE".equals(prescription.getStatus());
        } catch (Exception e) {
            logger.error("Error checking prescription validity", e);
            return false;
        }
    }
    
    @Override
    public boolean canBeRefilled(Prescription prescription) {
        try {
            return prescription != null && 
                   "ACTIVE".equals(prescription.getStatus()); // Removed getRefillCount and getMaxRefills
        } catch (Exception e) {
            logger.error("Error checking if prescription can be refilled", e);
            return false;
        }
    }
    
    @Override
    public LocalDate calculateExpiryDate(Prescription prescription) {
        try {
            if (prescription.getExpiryDate() != null) {
                return prescription.getExpiryDate().toLocalDate();
            }
            return LocalDate.now().plusDays(30); // Default 30 days
        } catch (Exception e) {
            logger.error("Error calculating expiry date", e);
            return LocalDate.now().plusDays(30);
        }
    }
    
    @Override
    public List<Prescription> getDeletedPrescriptions() {
        return findDeletedPrescriptions();
    }
    
    // Enhanced prescription management methods
    @Override
    public Prescription createPrescriptionWithMedications(com.hms2.dto.prescription.PrescriptionRequestDTO prescriptionRequest, String prescribedBy) {
        try {
            // This would need more sophisticated implementation with medications
            // For now, create basic prescription
            Prescription prescription = new Prescription();
            //prescription.setPrescribedBy(prescribedBy);
            prescription.setStatus("ACTIVE");
            prescription.setPrescriptionDate(LocalDateTime.now());
            return save(prescription);
        } catch (Exception e) {
            logger.error("Error creating prescription with medications", e);
            throw new RuntimeException("Error creating prescription with medications", e);
        }
    }
    
    @Override
    public Prescription updatePrescriptionWithMedications(Long prescriptionId, com.hms2.dto.prescription.PrescriptionRequestDTO prescriptionRequest) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                // Update prescription with new data
                prescription.setUpdatedAt(LocalDateTime.now());
                return update(prescription);
            }
            throw new RuntimeException("Prescription not found: " + prescriptionId);
        } catch (Exception e) {
            logger.error("Error updating prescription with medications", e);
            throw new RuntimeException("Error updating prescription with medications", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsByPatient(Patient patient) {
        return findByPatient(patient);
    }
    
    @Override
    public List<Prescription> getPrescriptionsByDoctor(Doctor doctor) {
        return findByDoctor(doctor);
    }

    @Override
    public List<Prescription> getRecentPrescriptionsByDoctor(Doctor doctor) {
        try {
            List<Prescription> all = prescriptionRepository.findByDoctor(doctor);
            return all.size() > 10 ? all.subList(0, 10) : all;
        } catch (Exception e) {
            logger.error("Error finding recent prescriptions by doctor", e);
            throw new RuntimeException("Error finding recent prescriptions by doctor", e);
        }
    }
    
    @Override
    public List<Prescription> getOverduePrescriptionsByDoctor(Doctor doctor) {
        try {
            // This would need more sophisticated logic to check overdue status
            // For now, return empty list as placeholder
            logger.info("Getting overdue prescriptions for doctor: {}", doctor.getDoctorId());
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding overdue prescriptions by doctor", e);
            throw new RuntimeException("Error finding overdue prescriptions by doctor", e);
        }
    }
    
    @Override
    public List<Prescription> getActivePrescriptionsByPatient(Patient patient) {
        try {
            return prescriptionRepository.findByPatientAndStatus(patient, PrescriptionStatus.ACTIVE);
        } catch (Exception e) {
            logger.error("Error finding active prescriptions by patient", e);
            throw new RuntimeException("Error finding active prescriptions by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsRequiringRefills() {
        try {
            // This would need more sophisticated logic to check refill requirements
            // For now, return empty list as placeholder
            logger.info("Getting prescriptions requiring refills");
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding prescriptions requiring refills", e);
            throw new RuntimeException("Error finding prescriptions requiring refills", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsRequiringRefillsByPatient(Patient patient) {
        try {
            
            // For now, return empty list as placeholder
            logger.info("Getting prescriptions requiring refills for patient: {}", patient.getPatientId());
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding prescriptions requiring refills by patient", e);
            throw new RuntimeException("Error finding prescriptions requiring refills by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionsRequiringRefillsByDoctor(Doctor doctor) {
        try {
            // This would need more sophisticated logic to check refill requirements
            // For now, return empty list as placeholder
            logger.info("Getting prescriptions requiring refills for doctor: {}", doctor.getDoctorId());
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding prescriptions requiring refills by doctor", e);
            throw new RuntimeException("Error finding prescriptions requiring refills by doctor", e);
        }
    }
    
    @Override
    public Prescription refillPrescription(Long prescriptionId, String refilledBy, String notes) {
        try {
            Prescription prescription = findById(prescriptionId);
            if (prescription != null) {
                prescription.setStatus("ACTIVE");
                prescription.setNotes(notes); // Removed setRefillDate
               // prescription.setRefilledBy(null); // Removed setRefilledBy
                update(prescription);
                logger.info("Prescription {} refilled by: {} with notes: {}", prescriptionId, refilledBy, notes);
                return prescription;
            }
            throw new RuntimeException("Prescription not found: " + prescriptionId);
        } catch (Exception e) {
            logger.error("Error refilling prescription: {}", prescriptionId, e);
            throw new RuntimeException("Error refilling prescription", e);
        }
    }
    
    @Override
    public Map<String, Long> getPrescriptionStatisticsByDoctor(Long doctorId) {
        try {
            Map<String, Long> stats = new HashMap<>();
            // This would need DoctorRepository injection to implement properly
            // For now, return empty stats
            logger.info("Getting prescription statistics for doctor: {}", doctorId);
            return stats;
        } catch (Exception e) {
            logger.error("Error getting prescription statistics by doctor: {}", doctorId, e);
            throw new RuntimeException("Error getting prescription statistics by doctor", e);
        }
    }
    
    @Override
    public Map<String, Long> getPrescriptionStatisticsByPatient(Long patientId) {
        try {
            Map<String, Long> stats = new HashMap<>();
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                stats.put("total", Long.valueOf(countByPatient(patientOpt.get())));
                stats.put("active", Long.valueOf(prescriptionRepository.findByPatientAndStatus(patientOpt.get(), PrescriptionStatus.ACTIVE).size()));
                stats.put("completed", Long.valueOf(prescriptionRepository.findByPatientAndStatus(patientOpt.get(), PrescriptionStatus.COMPLETED).size()));
                stats.put("expired", Long.valueOf(prescriptionRepository.findByPatientAndStatus(patientOpt.get(), PrescriptionStatus.EXPIRED).size()));
            }
            return stats;
        } catch (Exception e) {
            logger.error("Error getting prescription statistics by patient: {}", patientId, e);
            throw new RuntimeException("Error getting prescription statistics by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionHistoryByPatient(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                return findByPatient(patientOpt.get());
            }
            return List.of();
        } catch (Exception e) {
            logger.error("Error getting prescription history by patient: {}", patientId, e);
            throw new RuntimeException("Error getting prescription history by patient", e);
        }
    }
    
    @Override
    public List<Prescription> getPrescriptionHistoryByDoctor(Long doctorId) {
        try {
            // This would need DoctorRepository injection to implement properly
            // For now, return empty list
            logger.info("Getting prescription history for doctor: {}", doctorId);
            return List.of();
        } catch (Exception e) {
            logger.error("Error getting prescription history by doctor: {}", doctorId, e);
            throw new RuntimeException("Error getting prescription history by doctor", e);
        }
    }
    
    @Override
    public List<String> checkDrugInteractions(List<String> medications) {
        try {
            // This would need integration with drug interaction database
            // For now, return empty list
            logger.info("Checking drug interactions for medications: {}", medications);
            return List.of();
        } catch (Exception e) {
            logger.error("Error checking drug interactions", e);
            throw new RuntimeException("Error checking drug interactions", e);
        }
    }
    
    @Override
    public List<String> checkAllergies(Long patientId, List<String> medications) {
        try {
            // This would need integration with allergy database
            // For now, return empty list
            logger.info("Checking allergies for patient: {} with medications: {}", patientId, medications);
            return List.of();
        } catch (Exception e) {
            logger.error("Error checking allergies", e);
            throw new RuntimeException("Error checking allergies", e);
        }
    }
    
    @Override
    public Double calculatePrescriptionCost(Prescription prescription) {
        try {
            // This would need integration with pricing database
            // For now, return default cost
            logger.info("Calculating prescription cost for prescription: {}", prescription.getPrescriptionId());
            return 50.0; // Default cost
        } catch (Exception e) {
            logger.error("Error calculating prescription cost", e);
            throw new RuntimeException("Error calculating prescription cost", e);
        }
    }
    
    @Override
    public List<Prescription> getControlledSubstancePrescriptions() {
        try {
            // This would need more sophisticated logic to identify controlled substances
            // For now, return empty list
            logger.info("Getting controlled substance prescriptions");
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding controlled substance prescriptions", e);
            throw new RuntimeException("Error finding controlled substance prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> getControlledSubstancePrescriptionsByDoctor(Doctor doctor) {
        try {
            // This would need more sophisticated logic to identify controlled substances
            // For now, return empty list
            logger.info("Getting controlled substance prescriptions for doctor: {}", doctor.getDoctorId());
            return List.of();
        } catch (Exception e) {
            logger.error("Error finding controlled substance prescriptions by doctor", e);
            throw new RuntimeException("Error finding controlled substance prescriptions by doctor", e);
        }
    }
    
    @Override
    public boolean validateInsuranceCoverage(Prescription prescription, String insuranceProvider) {
        try {
            // This would need integration with insurance validation system
            // For now, return true as placeholder
            logger.info("Validating insurance coverage for prescription: {} with provider: {}", prescription.getPrescriptionId(), insuranceProvider);
            return true;
        } catch (Exception e) {
            logger.error("Error validating insurance coverage", e);
            return false;
        }
    }
    
    @Override
    public void sendPrescriptionRefillReminders() {
        try {
            // This would need integration with notification system
            // For now, just log the action
            logger.info("Sending prescription refill reminders");
        } catch (Exception e) {
            logger.error("Error sending prescription refill reminders", e);
            throw new RuntimeException("Error sending prescription refill reminders", e);
        }
    }

    @Override
    public List<Prescription> getActivePrescriptions() {
        return prescriptionRepository.findActivePrescriptions();
    }
}
