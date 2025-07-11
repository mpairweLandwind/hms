package com.hms2.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.hms2.dto.prescription.MedicationDTO;
import com.hms2.dto.prescription.PrescriptionRequestDTO;
import com.hms2.enums.UserRole;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.model.User;
import com.hms2.service.DoctorService;
import com.hms2.service.PatientService;
import com.hms2.service.PrescriptionService;
import com.hms2.service.UserService;

@Named
@ViewScoped
public class PrescriptionManagementController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PrescriptionManagementController.class.getName());
    
    @Inject
    private PrescriptionService prescriptionService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private UserService userService;
    
    // Prescription management data
    private PrescriptionRequestDTO newPrescription;
    private List<Prescription> activePrescriptions;
    private List<Prescription> completedPrescriptions;
    private List<Prescription> expiredPrescriptions;
    private List<Prescription> overduePrescriptions;
    private List<Prescription> recentPrescriptions;
    private List<Prescription> controlledSubstancePrescriptions;
    
    // Patient and doctor lists
    private List<Patient> allPatients;
    private List<Doctor> allDoctors;
    
    // Filtering and search
    private String selectedStatus;
    private Long selectedDoctorId;
    private Long selectedPatientId;
    private Date selectedDate;
    private String searchTerm;
    
    // Statistics
    private Map<String, Long> prescriptionStats;
    private Map<String, Long> doctorStats;
    private Map<String, Long> patientStats;
    
    // Current user context
    private User currentUser;
    private UserRole currentUserRole;
    private Doctor currentDoctor;
    
    // UI state
    private boolean showPrescriptionDialog;
    private boolean showRefillDialog;
    private boolean showDetailsDialog;
    private Prescription selectedPrescription;
    private String refillNotes;
    
    // Medication management
    private List<MedicationDTO> medications;
    private MedicationDTO newMedication;
    private boolean showMedicationDialog;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing PrescriptionManagementController");
        
        try {
            // Initialize prescription request
            newPrescription = new PrescriptionRequestDTO();
            medications = new ArrayList<>();
            newMedication = new MedicationDTO();
            
            // Load current user context
            loadCurrentUserContext();
            
            // Load initial data
            loadAllPatients();
            loadAllDoctors();
            loadPrescriptions();
            loadStatistics();
            
            logger.info("PrescriptionManagementController initialized successfully");
            
        } catch (Exception e) {
            logger.severe("Error initializing PrescriptionManagementController: " + e.getMessage());
            addErrorMessage("Error initializing prescription management: " + e.getMessage());
        }
    }
    
    // ==================== INITIALIZATION METHODS ====================
    
    private void loadCurrentUserContext() {
        // In a real application, this would come from session/security context
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("doctor");
        currentUser.setRole(UserRole.DOCTOR);
        currentUserRole = UserRole.DOCTOR;
        
        // Load current doctor if user is a doctor
        if (currentUserRole == UserRole.DOCTOR) {
            currentDoctor = doctorService.findByUserId(currentUser.getId());
        }
        
        logger.info("Loaded user context: " + currentUser.getUsername() + " with role: " + currentUserRole);
    }
    
    private void loadAllPatients() {
        try {
            allPatients = patientService.findAll();
            logger.info("Loaded " + allPatients.size() + " patients");
        } catch (Exception e) {
            logger.severe("Error loading patients: " + e.getMessage());
            allPatients = new ArrayList<>();
        }
    }
    
    private void loadAllDoctors() {
        try {
            allDoctors = doctorService.findAll();
            logger.info("Loaded " + allDoctors.size() + " doctors");
        } catch (Exception e) {
            logger.severe("Error loading doctors: " + e.getMessage());
            allDoctors = new ArrayList<>();
        }
    }
    
    private void loadPrescriptions() {
        try {
            // Load prescriptions based on user role
            if (currentUserRole == UserRole.ADMIN) {
                loadAllPrescriptions();
            } else if (currentUserRole == UserRole.DOCTOR) {
                loadDoctorPrescriptions();
            } else if (currentUserRole == UserRole.PATIENT) {
                loadPatientPrescriptions();
            }
            
            logger.info("Loaded prescriptions successfully");
        } catch (Exception e) {
            logger.severe("Error loading prescriptions: " + e.getMessage());
            addErrorMessage("Error loading prescriptions: " + e.getMessage());
        }
    }
    
    private void loadAllPrescriptions() {
        activePrescriptions = prescriptionService.getActivePrescriptions();
        completedPrescriptions = prescriptionService.getCompletedPrescriptions();
        expiredPrescriptions = prescriptionService.getExpiredPrescriptions();
        overduePrescriptions = prescriptionService.getOverduePrescriptions();
        //recentPrescriptions = prescriptionService.findRecentPrescriptions(10);
        controlledSubstancePrescriptions = prescriptionService.getControlledSubstancePrescriptions();
    }
    
    private void loadDoctorPrescriptions() {
        if (currentDoctor != null) {
            activePrescriptions = prescriptionService.getPrescriptionsByDoctor(currentDoctor);
            completedPrescriptions = prescriptionService.getCompletedPrescriptions().stream()
                .filter(p -> p.getDoctor().getDoctorId().equals(currentDoctor.getDoctorId()))
                .collect(java.util.stream.Collectors.toList());
            expiredPrescriptions = prescriptionService.getExpiredPrescriptions().stream()
                .filter(p -> p.getDoctor().getDoctorId().equals(currentDoctor.getDoctorId()))
                .collect(java.util.stream.Collectors.toList());
            overduePrescriptions = prescriptionService.getOverduePrescriptionsByDoctor(currentDoctor);
            recentPrescriptions = prescriptionService.getRecentPrescriptionsByDoctor(currentDoctor);
            controlledSubstancePrescriptions = prescriptionService.getControlledSubstancePrescriptionsByDoctor(currentDoctor);
        }
    }
    
    private void loadPatientPrescriptions() {
        // Load prescriptions for the current patient
        Patient currentPatient = patientService.findByUserId(currentUser.getId());
        if (currentPatient != null) {
            activePrescriptions = prescriptionService.getActivePrescriptionsByPatient(currentPatient);
            completedPrescriptions = prescriptionService.getCompletedPrescriptions().stream()
                .filter(p -> p.getPatient().getPatientId().equals(currentPatient.getPatientId()))
                .collect(java.util.stream.Collectors.toList());
            expiredPrescriptions = prescriptionService.getExpiredPrescriptions().stream()
                .filter(p -> p.getPatient().getPatientId().equals(currentPatient.getPatientId()))
                .collect(java.util.stream.Collectors.toList());
            overduePrescriptions = prescriptionService.getPrescriptionsRequiringRefillsByPatient(currentPatient);
            recentPrescriptions = new ArrayList<>(); // Patients don't see recent prescriptions list
            controlledSubstancePrescriptions = new ArrayList<>(); // Patients don't see controlled substances list
        }
    }
    
    private void loadStatistics() {
        try {
            prescriptionStats = prescriptionService.getPrescriptionStatistics();
            
            if (currentUserRole == UserRole.DOCTOR && currentDoctor != null) {
                doctorStats = prescriptionService.getPrescriptionStatisticsByDoctor(currentDoctor.getDoctorId());
            } else if (currentUserRole == UserRole.PATIENT) {
                Patient currentPatient = patientService.findByUserId(currentUser.getId());
                if (currentPatient != null) {
                    patientStats = prescriptionService.getPrescriptionStatisticsByPatient(currentPatient.getPatientId());
                }
            }
            
            logger.info("Loaded prescription statistics successfully");
        } catch (Exception e) {
            logger.severe("Error loading statistics: " + e.getMessage());
        }
    }
    
    // ==================== PRESCRIPTION CREATION ====================
    
    public void createPrescription() {
        logger.info("Creating new prescription");
        
        try {
            // Validate prescription request
            if (!validatePrescriptionRequest(newPrescription)) {
                return;
            }
            
            // Set medications
            newPrescription.setMedications(medications);
            
            // Create prescription
            String prescribedBy = currentUserRole.name();
            Prescription prescription = prescriptionService.createPrescriptionWithMedications(newPrescription, prescribedBy);
            
            // Reset form and close dialog
            newPrescription = new PrescriptionRequestDTO();
            medications = new ArrayList<>();
            showPrescriptionDialog = false;
            
            // Reload prescriptions
            loadPrescriptions();
            loadStatistics();
            
            addSuccessMessage("Prescription created successfully! Prescription ID: " + prescription.getPrescriptionId());
            logger.info("Prescription created successfully: " + prescription.getPrescriptionId());
            
        } catch (Exception e) {
            logger.severe("Error creating prescription: " + e.getMessage());
            addErrorMessage("Error creating prescription: " + e.getMessage());
        }
    }
    
    private boolean validatePrescriptionRequest(PrescriptionRequestDTO request) {
        if (request.getPatientId() == null) {
            addErrorMessage("Please select a patient");
            return false;
        }
        
        if (request.getDoctorId() == null) {
            addErrorMessage("Please select a doctor");
            return false;
        }
        
        if (request.getDiagnosis() == null || request.getDiagnosis().trim().isEmpty()) {
            addErrorMessage("Please provide a diagnosis");
            return false;
        }
        
        if (medications == null || medications.isEmpty()) {
            addErrorMessage("Please add at least one medication");
            return false;
        }
        
        // Validate each medication
        for (MedicationDTO medication : medications) {
            if (!validateMedication(medication)) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean validateMedication(MedicationDTO medication) {
        if (medication.getMedicationName() == null || medication.getMedicationName().trim().isEmpty()) {
            addErrorMessage("Please provide medication name for all medications");
            return false;
        }
        
        if (medication.getDosage() == null || medication.getDosage().trim().isEmpty()) {
            addErrorMessage("Please provide dosage for medication: " + medication.getMedicationName());
            return false;
        }
        
        if (medication.getFrequency() == null || medication.getFrequency().trim().isEmpty()) {
            addErrorMessage("Please provide frequency for medication: " + medication.getMedicationName());
            return false;
        }
        
        if (medication.getDuration() == null || medication.getDuration().trim().isEmpty()) {
            addErrorMessage("Please provide duration for medication: " + medication.getMedicationName());
            return false;
        }
        
        return true;
    }
    
    // ==================== MEDICATION MANAGEMENT ====================
    
    public void addMedication() {
        logger.info("Adding medication to prescription");
        
        try {
            if (!validateMedication(newMedication)) {
                return;
            }
            
            // Set medication ID for tracking
            newMedication.setMedicationId((long) (medications.size() + 1));
            
            // Add medication to list
            medications.add(newMedication);
            
            // Reset medication form
            newMedication = new MedicationDTO();
            
            addSuccessMessage("Medication added successfully!");
            logger.info("Medication added to prescription");
            
        } catch (Exception e) {
            logger.severe("Error adding medication: " + e.getMessage());
            addErrorMessage("Error adding medication: " + e.getMessage());
        }
    }
    
    public void removeMedication(MedicationDTO medication) {
        logger.info("Removing medication from prescription: " + medication.getMedicationName());
        
        try {
            medications.remove(medication);
            addSuccessMessage("Medication removed successfully!");
            logger.info("Medication removed from prescription");
            
        } catch (Exception e) {
            logger.severe("Error removing medication: " + e.getMessage());
            addErrorMessage("Error removing medication: " + e.getMessage());
        }
    }
    
    public void editMedication(MedicationDTO medication) {
        logger.info("Editing medication: " + medication.getMedicationName());
        
        try {
            newMedication = medication;
            medications.remove(medication);
            showMedicationDialog = true;
            
        } catch (Exception e) {
            logger.severe("Error editing medication: " + e.getMessage());
            addErrorMessage("Error editing medication: " + e.getMessage());
        }
    }
    
    // ==================== PRESCRIPTION ACTIONS ====================
    
    public void refillPrescription() {
        logger.info("Refilling prescription: " + selectedPrescription.getPrescriptionId());
        
        try {
            Prescription prescription = prescriptionService.refillPrescription(
                selectedPrescription.getPrescriptionId(),
                currentUser.getUsername(),
                refillNotes
            );
            
            // Reset form and close dialog
            selectedPrescription = null;
            refillNotes = null;
            showRefillDialog = false;
            
            // Reload prescriptions
            loadPrescriptions();
            loadStatistics();
            
            addSuccessMessage("Prescription refilled successfully!");
            logger.info("Prescription refilled successfully: " + prescription.getPrescriptionId());
            
        } catch (Exception e) {
            logger.severe("Error refilling prescription: " + e.getMessage());
            addErrorMessage("Error refilling prescription: " + e.getMessage());
        }
    }
    
    public void markAsCompleted() {
        logger.info("Marking prescription as completed: " + selectedPrescription.getPrescriptionId());
        
        try {
            prescriptionService.markAsCompleted(selectedPrescription.getPrescriptionId());
            
            // Reload prescriptions
            loadPrescriptions();
            loadStatistics();
            
            addSuccessMessage("Prescription marked as completed successfully!");
            logger.info("Prescription marked as completed: " + selectedPrescription.getPrescriptionId());
            
        } catch (Exception e) {
            logger.severe("Error marking prescription as completed: " + e.getMessage());
            addErrorMessage("Error marking prescription as completed: " + e.getMessage());
        }
    }
    
    public void cancelPrescription() {
        logger.info("Cancelling prescription: " + selectedPrescription.getPrescriptionId());
        
        try {
            prescriptionService.cancelPrescription(selectedPrescription.getPrescriptionId(), "Cancelled by " + currentUserRole.name());
            
            // Reload prescriptions
            loadPrescriptions();
            loadStatistics();
            
            addSuccessMessage("Prescription cancelled successfully!");
            logger.info("Prescription cancelled: " + selectedPrescription.getPrescriptionId());
            
        } catch (Exception e) {
            logger.severe("Error cancelling prescription: " + e.getMessage());
            addErrorMessage("Error cancelling prescription: " + e.getMessage());
        }
    }
    
    // ==================== DIALOG MANAGEMENT ====================
    
    public void openPrescriptionDialog() {
        newPrescription = new PrescriptionRequestDTO();
        medications = new ArrayList<>();
        newMedication = new MedicationDTO();
        showPrescriptionDialog = true;
    }
    
    public void openRefillDialog(Prescription prescription) {
        selectedPrescription = prescription;
        refillNotes = null;
        showRefillDialog = true;
    }
    
    public void openDetailsDialog(Prescription prescription) {
        selectedPrescription = prescription;
        showDetailsDialog = true;
    }
    
    public void openMedicationDialog() {
        newMedication = new MedicationDTO();
        showMedicationDialog = true;
    }
    
    public void closePrescriptionDialog() {
        showPrescriptionDialog = false;
        newPrescription = new PrescriptionRequestDTO();
        medications = new ArrayList<>();
    }
    
    public void closeRefillDialog() {
        showRefillDialog = false;
        selectedPrescription = null;
        refillNotes = null;
    }
    
    public void closeDetailsDialog() {
        showDetailsDialog = false;
        selectedPrescription = null;
    }
    
    public void closeMedicationDialog() {
        showMedicationDialog = false;
        newMedication = new MedicationDTO();
    }
    
    // ==================== FILTERING AND SEARCH ====================
    
    public void filterPrescriptions() {
        logger.info("Filtering prescriptions with status: " + selectedStatus + ", doctor: " + selectedDoctorId + ", patient: " + selectedPatientId);
        
        try {
            // This would implement filtering logic based on selected criteria
            loadPrescriptions();
            logger.info("Prescriptions filtered successfully");
        } catch (Exception e) {
            logger.severe("Error filtering prescriptions: " + e.getMessage());
            addErrorMessage("Error filtering prescriptions: " + e.getMessage());
        }
    }
    
    public void searchPrescriptions() {
        logger.info("Searching prescriptions with term: " + searchTerm);
        
        try {
            // This would implement search logic
            loadPrescriptions();
            logger.info("Prescriptions searched successfully");
        } catch (Exception e) {
            logger.severe("Error searching prescriptions: " + e.getMessage());
            addErrorMessage("Error searching prescriptions: " + e.getMessage());
        }
    }
    
    public void clearFilters() {
        selectedStatus = null;
        selectedDoctorId = null;
        selectedPatientId = null;
        selectedDate = null;
        searchTerm = null;
        
        loadPrescriptions();
        addInfoMessage("Filters cleared");
    }
    
    // ==================== UTILITY METHODS ====================
    
    public boolean canCreatePrescriptions() {
        return currentUserRole == UserRole.DOCTOR || currentUserRole == UserRole.ADMIN;
    }
    
    public boolean canRefillPrescriptions() {
        return currentUserRole == UserRole.DOCTOR || currentUserRole == UserRole.ADMIN;
    }
    
    public boolean canCancelPrescriptions() {
        return currentUserRole == UserRole.DOCTOR || currentUserRole == UserRole.ADMIN;
    }
    
    public boolean canViewControlledSubstances() {
        return currentUserRole == UserRole.DOCTOR || currentUserRole == UserRole.ADMIN;
    }
    
    public String getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "ACTIVE": return "success";
            case "COMPLETED": return "info";
            case "EXPIRED": return "warning";
            case "CANCELLED": return "danger";
            case "OVERDUE": return "secondary";
            default: return "primary";
        }
    }
    
    public boolean isPrescriptionExpiringSoon(Prescription prescription) {
        if (prescription.getExpiryDate() == null) {
            return false;
        }
        
        LocalDate expiryDate = prescription.getExpiryDate().toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);
        
        return expiryDate.isBefore(weekFromNow) && expiryDate.isAfter(today);
    }
    
    public boolean isPrescriptionOverdue(Prescription prescription) {
        if (prescription.getExpiryDate() == null) {
            return false;
        }
        
        LocalDate expiryDate = prescription.getExpiryDate().toLocalDate();
        LocalDate today = LocalDate.now();
        
        return expiryDate.isBefore(today);
    }
    
    // ==================== MESSAGE UTILITIES ====================
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    public PrescriptionRequestDTO getNewPrescription() {
        return newPrescription;
    }
    
    public void setNewPrescription(PrescriptionRequestDTO newPrescription) {
        this.newPrescription = newPrescription;
    }
    
    public List<Prescription> getActivePrescriptions() {
        return activePrescriptions;
    }
    
    public void setActivePrescriptions(List<Prescription> activePrescriptions) {
        this.activePrescriptions = activePrescriptions;
    }
    
    public List<Prescription> getCompletedPrescriptions() {
        return completedPrescriptions;
    }
    
    public void setCompletedPrescriptions(List<Prescription> completedPrescriptions) {
        this.completedPrescriptions = completedPrescriptions;
    }
    
    public List<Prescription> getExpiredPrescriptions() {
        return expiredPrescriptions;
    }
    
    public void setExpiredPrescriptions(List<Prescription> expiredPrescriptions) {
        this.expiredPrescriptions = expiredPrescriptions;
    }
    
    public List<Prescription> getOverduePrescriptions() {
        return overduePrescriptions;
    }
    
    public void setOverduePrescriptions(List<Prescription> overduePrescriptions) {
        this.overduePrescriptions = overduePrescriptions;
    }
    
    public List<Prescription> getRecentPrescriptions() {
        return recentPrescriptions;
    }
    
    public void setRecentPrescriptions(List<Prescription> recentPrescriptions) {
        this.recentPrescriptions = recentPrescriptions;
    }
    
    public List<Prescription> getControlledSubstancePrescriptions() {
        return controlledSubstancePrescriptions;
    }
    
    public void setControlledSubstancePrescriptions(List<Prescription> controlledSubstancePrescriptions) {
        this.controlledSubstancePrescriptions = controlledSubstancePrescriptions;
    }
    
    public List<Patient> getAllPatients() {
        return allPatients;
    }
    
    public void setAllPatients(List<Patient> allPatients) {
        this.allPatients = allPatients;
    }
    
    public List<Doctor> getAllDoctors() {
        return allDoctors;
    }
    
    public void setAllDoctors(List<Doctor> allDoctors) {
        this.allDoctors = allDoctors;
    }
    
    public String getSelectedStatus() {
        return selectedStatus;
    }
    
    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }
    
    public Long getSelectedDoctorId() {
        return selectedDoctorId;
    }
    
    public void setSelectedDoctorId(Long selectedDoctorId) {
        this.selectedDoctorId = selectedDoctorId;
    }
    
    public Long getSelectedPatientId() {
        return selectedPatientId;
    }
    
    public void setSelectedPatientId(Long selectedPatientId) {
        this.selectedPatientId = selectedPatientId;
    }
    
    public Date getSelectedDate() {
        return selectedDate;
    }
    
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public Map<String, Long> getPrescriptionStats() {
        return prescriptionStats;
    }
    
    public void setPrescriptionStats(Map<String, Long> prescriptionStats) {
        this.prescriptionStats = prescriptionStats;
    }
    
    public Map<String, Long> getDoctorStats() {
        return doctorStats;
    }
    
    public void setDoctorStats(Map<String, Long> doctorStats) {
        this.doctorStats = doctorStats;
    }
    
    public Map<String, Long> getPatientStats() {
        return patientStats;
    }
    
    public void setPatientStats(Map<String, Long> patientStats) {
        this.patientStats = patientStats;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    public UserRole getCurrentUserRole() {
        return currentUserRole;
    }
    
    public void setCurrentUserRole(UserRole currentUserRole) {
        this.currentUserRole = currentUserRole;
    }
    
    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }
    
    public void setCurrentDoctor(Doctor currentDoctor) {
        this.currentDoctor = currentDoctor;
    }
    
    public boolean isShowPrescriptionDialog() {
        return showPrescriptionDialog;
    }
    
    public void setShowPrescriptionDialog(boolean showPrescriptionDialog) {
        this.showPrescriptionDialog = showPrescriptionDialog;
    }
    
    public boolean isShowRefillDialog() {
        return showRefillDialog;
    }
    
    public void setShowRefillDialog(boolean showRefillDialog) {
        this.showRefillDialog = showRefillDialog;
    }
    
    public boolean isShowDetailsDialog() {
        return showDetailsDialog;
    }
    
    public void setShowDetailsDialog(boolean showDetailsDialog) {
        this.showDetailsDialog = showDetailsDialog;
    }
    
    public Prescription getSelectedPrescription() {
        return selectedPrescription;
    }
    
    public void setSelectedPrescription(Prescription selectedPrescription) {
        this.selectedPrescription = selectedPrescription;
    }
    
    public String getRefillNotes() {
        return refillNotes;
    }
    
    public void setRefillNotes(String refillNotes) {
        this.refillNotes = refillNotes;
    }
    
    public List<MedicationDTO> getMedications() {
        return medications;
    }
    
    public void setMedications(List<MedicationDTO> medications) {
        this.medications = medications;
    }
    
    public MedicationDTO getNewMedication() {
        return newMedication;
    }
    
    public void setNewMedication(MedicationDTO newMedication) {
        this.newMedication = newMedication;
    }
    
    public boolean isShowMedicationDialog() {
        return showMedicationDialog;
    }
    
    public void setShowMedicationDialog(boolean showMedicationDialog) {
        this.showMedicationDialog = showMedicationDialog;
    }
} 