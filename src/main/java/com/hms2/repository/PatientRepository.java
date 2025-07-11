package com.hms2.repository;

import com.hms2.model.Patient;
import com.hms2.enums.BloodType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends GenericRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPatientId(String patientId);

    List<Patient> findByBloodType(BloodType bloodType);

    List<Patient> findByStatus(String status);

    List<Patient> findByDateOfBirthRange(LocalDate startDate, LocalDate endDate);

    List<Patient> findByGender(String gender);

    List<Patient> searchByName(String name);

    List<Patient> findActivePatients();

    List<Patient> findDeletedPatients();

    long countByStatus(String status);

    long countByBloodType(BloodType bloodType);

    long countByGender(String gender);

    boolean existsByEmail(String email);

    boolean existsByPatientId(String patientId);
    
    /**
     * Find patient by user ID
     */
    Optional<Patient> findByUserId(Long userId);
}
