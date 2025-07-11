package com.hms2.repository;

import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Prescription;
import com.hms2.model.Medication;
import java.util.List;
import java.util.Optional;

public interface PrescriptionMedicationRepository extends GenericRepository<PrescriptionMedication, Long> {

    List<PrescriptionMedication> findByPrescription(Prescription prescription);

    List<PrescriptionMedication> findByMedication(Medication medication);

    Optional<PrescriptionMedication> findByPrescriptionAndMedication(Prescription prescription, Medication medication);

    List<PrescriptionMedication> findByDosage(String dosage);

    List<PrescriptionMedication> findByFrequency(String frequency);

    List<PrescriptionMedication> findActiveMedications();

    List<PrescriptionMedication> findDeletedMedications();

    long countByPrescription(Prescription prescription);

    long countByMedication(Medication medication);

    boolean existsByPrescriptionAndMedication(Prescription prescription, Medication medication);
}
