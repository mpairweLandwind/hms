package com.hms2.controller;

import com.hms2.model.PrescriptionMedication;
import com.hms2.service.PrescriptionMedicationService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named("prescriptionMedicationController")
@RequestScoped
public class PrescriptionMedicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionMedicationController.class);
    
    @Inject
    private PrescriptionMedicationService prescriptionMedicationService;
    
    private List<PrescriptionMedication> prescriptionMedicationList;
    private List<PrescriptionMedication> filteredPrescriptionMedications;
    private PrescriptionMedication selectedPrescriptionMedication;
    private PrescriptionMedication newPrescriptionMedication;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing prescription medication controller");
        loadPrescriptionMedicationList();
        newPrescriptionMedication = new PrescriptionMedication();
    }
    
    private void loadPrescriptionMedicationList() {
        try {
            prescriptionMedicationList = prescriptionMedicationService.getAllPrescriptionMedications();
            logger.info("Loaded {} prescription medications", prescriptionMedicationList.size());
        } catch (Exception e) {
            logger.error("Error loading prescription medication list", e);
            addErrorMessage("Error loading prescription medications: " + e.getMessage());
        }
    }
    
    public void loadPendingDispensals() {
        try {
            prescriptionMedicationList = prescriptionMedicationService.getPendingDispensals();
            logger.info("Loaded {} pending dispensals", prescriptionMedicationList.size());
        } catch (Exception e) {
            logger.error("Error loading pending dispensals", e);
            addErrorMessage("Error loading pending dispensals: " + e.getMessage());
        }
    }
    
    public void createPrescriptionMedication() {
        try {
            prescriptionMedicationService.createPrescriptionMedication(newPrescriptionMedication);
            addSuccessMessage("Prescription medication created successfully");
            loadPrescriptionMedicationList();
            newPrescriptionMedication = new PrescriptionMedication();
            PrimeFaces.current().executeScript("PF('prescriptionMedicationDialog').hide()");
        } catch (Exception e) {
            logger.error("Error creating prescription medication", e);
            addErrorMessage("Error creating prescription medication: " + e.getMessage());
        }
    }
    
    public void updatePrescriptionMedication() {
        try {
            prescriptionMedicationService.updatePrescriptionMedication(selectedPrescriptionMedication);
            addSuccessMessage("Prescription medication updated successfully");
            loadPrescriptionMedicationList();
            PrimeFaces.current().executeScript("PF('editPrescriptionMedicationDialog').hide()");
        } catch (Exception e) {
            logger.error("Error updating prescription medication", e);
            addErrorMessage("Error updating prescription medication: " + e.getMessage());
        }
    }
    
    public void dispenseMedication(PrescriptionMedication pm) {
        try {
            prescriptionMedicationService.dispenseMedication(pm.getPrescriptionMedicationId());
            addSuccessMessage("Medication dispensed successfully");
            loadPrescriptionMedicationList();
        } catch (Exception e) {
            logger.error("Error dispensing medication", e);
            addErrorMessage("Error dispensing medication: " + e.getMessage());
        }
    }
    
    public void cancelMedication(PrescriptionMedication pm) {
        try {
            prescriptionMedicationService.cancelMedication(pm.getPrescriptionMedicationId());
            addSuccessMessage("Medication cancelled successfully");
            loadPrescriptionMedicationList();
        } catch (Exception e) {
            logger.error("Error cancelling medication", e);
            addErrorMessage("Error cancelling medication: " + e.getMessage());
        }
    }
    
    public void prepareNewPrescriptionMedication() {
        newPrescriptionMedication = new PrescriptionMedication();
    }
    
    public void prepareEditPrescriptionMedication(PrescriptionMedication pm) {
        selectedPrescriptionMedication = pm;
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and setters
    public List<PrescriptionMedication> getPrescriptionMedicationList() {
        return prescriptionMedicationList;
    }
    
    public void setPrescriptionMedicationList(List<PrescriptionMedication> prescriptionMedicationList) {
        this.prescriptionMedicationList = prescriptionMedicationList;
    }
    
    public List<PrescriptionMedication> getFilteredPrescriptionMedications() {
        return filteredPrescriptionMedications;
    }
    
    public void setFilteredPrescriptionMedications(List<PrescriptionMedication> filteredPrescriptionMedications) {
        this.filteredPrescriptionMedications = filteredPrescriptionMedications;
    }
    
    public PrescriptionMedication getSelectedPrescriptionMedication() {
        return selectedPrescriptionMedication;
    }
    
    public void setSelectedPrescriptionMedication(PrescriptionMedication selectedPrescriptionMedication) {
        this.selectedPrescriptionMedication = selectedPrescriptionMedication;
    }
    
    public PrescriptionMedication getNewPrescriptionMedication() {
        return newPrescriptionMedication;
    }
    
    public void setNewPrescriptionMedication(PrescriptionMedication newPrescriptionMedication) {
        this.newPrescriptionMedication = newPrescriptionMedication;
    }
}
