package com.hms2.repository;

import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Prescription;
import com.hms2.model.Medication;
import java.util.List;

public interface PrescriptionMedicationRepository extends GenericRepository<PrescriptionMedication, Long> {
    
    List<PrescriptionMedication> findByPrescription(Prescription prescription);
    
    List<PrescriptionMedication> findByMedication(Medication medication);
    
    List<PrescriptionMedication> findByStatus(String status);
    
    List<PrescriptionMedication> findPendingDispensals();
    
    long countByStatus(String status);
}
