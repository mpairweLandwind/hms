package com.hms2.service;

import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Prescription;
import com.hms2.model.Medication;
import java.util.List;
import java.util.Optional;

public interface PrescriptionMedicationService {
    
    PrescriptionMedication createPrescriptionMedication(PrescriptionMedication prescriptionMedication);
    
    PrescriptionMedication updatePrescriptionMedication(PrescriptionMedication prescriptionMedication);
    
    void deletePrescriptionMedication(Long id);
    
    Optional<PrescriptionMedication> getPrescriptionMedicationById(Long id);
    
    List<PrescriptionMedication> getAllPrescriptionMedications();
    
    List<PrescriptionMedication> getPrescriptionMedicationsByPrescription(Prescription prescription);
    
    List<PrescriptionMedication> getPrescriptionMedicationsByMedication(Medication medication);
    
    List<PrescriptionMedication> getPrescriptionMedicationsByStatus(String status);
    
    List<PrescriptionMedication> getPendingDispensals();
    
    void dispenseMedication(Long id);
    
    void cancelMedication(Long id);
    
    long getCountByStatus(String status);
}
