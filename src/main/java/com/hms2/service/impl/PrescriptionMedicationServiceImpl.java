package com.hms2.service.impl;

import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Prescription;
import com.hms2.enums.PrescriptionStatus;
import com.hms2.model.Medication;
import com.hms2.model.Doctor;
import com.hms2.repository.PrescriptionMedicationRepository;
import com.hms2.service.PrescriptionMedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PrescriptionMedicationServiceImpl implements PrescriptionMedicationService {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionMedicationServiceImpl.class);

    @Inject
    private PrescriptionMedicationRepository prescriptionMedicationRepository;

    @Override
    public PrescriptionMedication createPrescriptionMedication(PrescriptionMedication prescriptionMedication) {
        logger.info("Creating new prescription medication");

        // Set unit price from medication if not set
        if (prescriptionMedication.getUnitPrice() == null &&
                prescriptionMedication.getMedication() != null) {
            prescriptionMedication.setUnitPrice(prescriptionMedication.getMedication().getUnitPrice());
        }

        // Calculate total price
        prescriptionMedication.calculateTotalPrice();

        return prescriptionMedicationRepository.save(prescriptionMedication);
    }

    @Override
    public PrescriptionMedication updatePrescriptionMedication(PrescriptionMedication prescriptionMedication) {
        logger.info("Updating prescription medication with ID: {}",
                prescriptionMedication.getPrescriptionMedicationId());

        // Recalculate total price
        prescriptionMedication.calculateTotalPrice();

        return prescriptionMedicationRepository.update(prescriptionMedication);
    }

    @Override
    public void deletePrescriptionMedication(Long id) {
        logger.info("Deleting prescription medication with ID: {}", id);
        prescriptionMedicationRepository.deleteById(id);
    }

    @Override
    public Optional<PrescriptionMedication> getPrescriptionMedicationById(Long id) {
        return prescriptionMedicationRepository.findById(id);
    }

    @Override
    public List<PrescriptionMedication> getAllPrescriptionMedications() {
        return prescriptionMedicationRepository.findAll();
    }

    @Override
    public List<PrescriptionMedication> getPrescriptionMedicationsByPrescription(Prescription prescription) {
        return prescriptionMedicationRepository.findByPrescription(prescription);
    }

    @Override
    public List<PrescriptionMedication> getPrescriptionMedicationsByMedication(Medication medication) {
        return prescriptionMedicationRepository.findByMedication(medication);
    }

    @Override
    public List<PrescriptionMedication> getPrescriptionMedicationsByStatus(String status) {
        return prescriptionMedicationRepository.findAll();
    }

    @Override
    public List<PrescriptionMedication> getPendingDispensals() {
        return prescriptionMedicationRepository.findAll();
    }

    @Override
    public void dispenseMedication(Long id) {
        logger.info("Dispensing medication with ID: {}", id);
        Optional<PrescriptionMedication> pmOpt = prescriptionMedicationRepository.findById(id);
        if (pmOpt.isPresent()) {
            PrescriptionMedication pm = pmOpt.get();
            pm.setStatus("DISPENSED");
            prescriptionMedicationRepository.update(pm);
        } else {
            throw new IllegalArgumentException("Prescription medication not found");
        }
    }

    @Override
    public void cancelMedication(Long id) {
        logger.info("Cancelling medication with ID: {}", id);
        Optional<PrescriptionMedication> pmOpt = prescriptionMedicationRepository.findById(id);
        if (pmOpt.isPresent()) {
            PrescriptionMedication pm = pmOpt.get();
            pm.setStatus("CANCELLED");
            prescriptionMedicationRepository.update(pm);
        } else {
            throw new IllegalArgumentException("Prescription medication not found");
        }
    }

    @Override
    public long getCountByStatus(String status) {
        return prescriptionMedicationRepository.count();
    }

    @Override
    public List<PrescriptionMedication> getRecentPrescriptionsByDoctor(Doctor doctor) {
        logger.info("Getting recent prescriptions for doctor ID: {}", doctor.getDoctorId());
        return prescriptionMedicationRepository.findActiveMedications();
    }

    @Override
    public long getActivePrescriptionCountByDoctor(Doctor doctor) {
        logger.info("Getting active prescription count for doctor ID: {}", doctor.getDoctorId());
        return prescriptionMedicationRepository.count();
    }
}
