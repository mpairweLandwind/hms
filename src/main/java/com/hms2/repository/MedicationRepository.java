package com.hms2.repository;

import com.hms2.model.Medication;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends GenericRepository<Medication, Long> {
    
    Optional<Medication> findByMedicationName(String medicationName);
    
    List<Medication> findByGenericName(String genericName);
    
    List<Medication> findByBrandName(String brandName);
    
    List<Medication> findByManufacturer(String manufacturer);
    
    List<Medication> findByStatus(String status);
    
    List<Medication> findAvailableMedications();
    
    List<Medication> findLowStockMedications();
    
    List<Medication> findDeletedMedications();
    
    List<Medication> searchByName(String name);
    
    long countByStatus(String status);
}
