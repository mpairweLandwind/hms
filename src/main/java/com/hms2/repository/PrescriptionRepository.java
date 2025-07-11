package com.hms2.repository;

import com.hms2.model.Prescription;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.enums.PrescriptionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends GenericRepository<Prescription, Long> {

    List<Prescription> findByPatient(Patient patient);

    List<Prescription> findByDoctor(Doctor doctor);

    List<Prescription> findByStatus(PrescriptionStatus status);

    List<Prescription> findByPatientAndStatus(Patient patient, PrescriptionStatus status);

    List<Prescription> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    List<Prescription> findActivePrescriptions();

    List<Prescription> findExpiredPrescriptions();

    List<Prescription> findRecentPrescriptions(int limit);

    List<Prescription> findDeletedPrescriptions();

    long countByStatus(PrescriptionStatus status);

    long countByPatient(Patient patient);

    long countByDoctor(Doctor doctor);

    boolean existsByPatientAndMedication(Patient patient, String medicationName);

    // Add stubs for service compatibility
    List<Prescription> findByDoctorAndStatus(Doctor doctor, PrescriptionStatus status);
    List<Prescription> findByDoctorAndStatus(Doctor doctor, String status);
}
