package com.hms2.controller;

import java.io.Serializable;
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

import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.dashboard.DashboardDTO;
import com.hms2.enums.AppointmentStatus;
import com.hms2.enums.UserRole;
import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.User;
import com.hms2.service.AppointmentService;
import com.hms2.service.DoctorService;
import com.hms2.service.PatientService;
import com.hms2.service.UserService;

@Named
@ViewScoped
public class AppointmentManagementController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AppointmentManagementController.class.getName());
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private UserService userService;
    
    // Appointment management data
    private AppointmentRequestDTO newAppointment;
    private List<Appointment> pendingAppointments;
    private List<Appointment> confirmedAppointments;
    private List<Appointment> completedAppointments;
    private List<Appointment> cancelledAppointments;
    private List<Appointment> noShowAppointments;
    private List<Appointment> urgentAppointments;
    
    // Doctor and patient lists
    private List<Doctor> availableDoctors;
    private List<Patient> allPatients;
    
    // Filtering and search
    private String selectedStatus;
    private Long selectedDoctorId;
    private Long selectedPatientId;
    private Date selectedDate;
    private String searchTerm;
    
    // Statistics
    private Map<String, Long> appointmentStats;
    private Map<String, Long> doctorStats;
    private Map<String, Long> patientStats;
    
    // Current user context
    private User currentUser;
    private UserRole currentUserRole;
    
    // UI state
    private boolean showScheduleDialog;
    private boolean showApprovalDialog;
    private boolean showRescheduleDialog;
    private Appointment selectedAppointment;
    private String approvalNotes;
    private String rejectionReason;
    private Date rescheduleDate;
    private String rescheduleReason;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing AppointmentManagementController");
        
        try {
            // Initialize appointment request
            newAppointment = new AppointmentRequestDTO();
            
            // Load current user context (this would come from session in real app)
            loadCurrentUserContext();
            
            // Load initial data
            loadAvailableDoctors();
            loadAllPatients();
            loadAppointments();
            loadStatistics();
            
            logger.info("AppointmentManagementController initialized successfully");
            
        } catch (Exception e) {
            logger.severe("Error initializing AppointmentManagementController: " + e.getMessage());
            addErrorMessage("Error initializing appointment management: " + e.getMessage());
        }
    }
    
    // ==================== INITIALIZATION METHODS ====================
    
    private void loadCurrentUserContext() {
        // In a real application, this would come from session/security context
        // For now, we'll simulate different user roles
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("admin");
        currentUser.setRole(UserRole.ADMIN);
        currentUserRole = UserRole.ADMIN;
        
        logger.info("Loaded user context: " + currentUser.getUsername() + " with role: " + currentUserRole);
    }
    
    private void loadAvailableDoctors() {
        try {
            availableDoctors = doctorService.findAll();
            logger.info("Loaded " + availableDoctors.size() + " available doctors");
        } catch (Exception e) {
            logger.severe("Error loading available doctors: " + e.getMessage());
            availableDoctors = new ArrayList<>();
        }
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
    
    private void loadAppointments() {
        try {
            // Load appointments based on user role
            if (currentUserRole == UserRole.ADMIN) {
                loadAllAppointments();
            } else if (currentUserRole == UserRole.DOCTOR) {
                loadDoctorAppointments();
            } else if (currentUserRole == UserRole.STAFF) {
                loadStaffAppointments();
            } else if (currentUserRole == UserRole.PATIENT) {
                loadPatientAppointments();
            }
            
            logger.info("Loaded appointments successfully");
        } catch (Exception e) {
            logger.severe("Error loading appointments: " + e.getMessage());
            addErrorMessage("Error loading appointments: " + e.getMessage());
        }
    }
    
    private void loadAllAppointments() {
        pendingAppointments = appointmentService.getPendingAppointments();
        confirmedAppointments = appointmentService.getAppointmentsByStatus("CONFIRMED");
        completedAppointments = appointmentService.getAppointmentsByStatus("COMPLETED");
        cancelledAppointments = appointmentService.getAppointmentsByStatus("CANCELLED");
        noShowAppointments = appointmentService.getNoShowAppointments();
        urgentAppointments = appointmentService.getUrgentAppointments();
    }
    
    private void loadDoctorAppointments() {
        // Load appointments for the current doctor
        Doctor currentDoctor = doctorService.findByUserId(currentUser.getId());
        if (currentDoctor != null) {
            pendingAppointments = appointmentService.getPendingAppointmentsByDoctor(currentDoctor);
            confirmedAppointments = appointmentService.getAppointmentsByDoctorAndStatus(currentDoctor, "CONFIRMED");
            completedAppointments = appointmentService.getAppointmentsByDoctorAndStatus(currentDoctor, "COMPLETED");
            cancelledAppointments = appointmentService.getAppointmentsByDoctorAndStatus(currentDoctor, "CANCELLED");
            noShowAppointments = appointmentService.getNoShowAppointmentsByDoctor(currentDoctor);
            urgentAppointments = appointmentService.getUrgentAppointmentsByDoctor(currentDoctor);
        }
    }
    
    private void loadStaffAppointments() {
        // Staff can see all appointments but with limited actions
        loadAllAppointments();
    }
    
    private void loadPatientAppointments() {
        // Load appointments for the current patient
        Patient currentPatient = patientService.findByUserId(currentUser.getId());
        if (currentPatient != null) {
            pendingAppointments = appointmentService.getAppointmentsByPatientAndStatus(currentPatient, "SCHEDULED");
            confirmedAppointments = appointmentService.getAppointmentsByPatientAndStatus(currentPatient, "CONFIRMED");
            completedAppointments = appointmentService.getAppointmentsByPatientAndStatus(currentPatient, "COMPLETED");
            cancelledAppointments = appointmentService.getAppointmentsByPatientAndStatus(currentPatient, "CANCELLED");
            noShowAppointments = appointmentService.getAppointmentsByPatientAndStatus(currentPatient, "NO_SHOW");
            urgentAppointments = new ArrayList<>(); // Patients don't see urgent appointments list
        }
    }
    
    private void loadStatistics() {
        try {
            appointmentStats = appointmentService.getAppointmentStatisticsByStatus();
            
            if (currentUserRole == UserRole.DOCTOR) {
                Doctor currentDoctor = doctorService.findByUserId(currentUser.getId());
                if (currentDoctor != null) {
                    doctorStats = appointmentService.getAppointmentStatisticsByDoctor(currentDoctor.getDoctorId());
                }
            } else if (currentUserRole == UserRole.PATIENT) {
                Patient currentPatient = patientService.findByUserId(currentUser.getId());
                if (currentPatient != null) {
                    patientStats = appointmentService.getAppointmentStatisticsByPatient(currentPatient.getPatientId());
                }
            }
            
            logger.info("Loaded appointment statistics successfully");
        } catch (Exception e) {
            logger.severe("Error loading statistics: " + e.getMessage());
        }
    }
    
    // ==================== APPOINTMENT SCHEDULING ====================
    
    public void scheduleAppointment() {
        logger.info("Scheduling new appointment");
        
        try {
            // Validate appointment request
            if (!validateAppointmentRequest(newAppointment)) {
                return;
            }
            
            // Schedule appointment
            String requestedBy = currentUserRole.name();
            Appointment appointment = appointmentService.scheduleAppointment(newAppointment, requestedBy);
            
            // Reset form and close dialog
            newAppointment = new AppointmentRequestDTO();
            showScheduleDialog = false;
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment scheduled successfully! Appointment ID: " + appointment.getAppointmentId());
            logger.info("Appointment scheduled successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error scheduling appointment: " + e.getMessage());
            addErrorMessage("Error scheduling appointment: " + e.getMessage());
        }
    }
    
    private boolean validateAppointmentRequest(AppointmentRequestDTO request) {
        if (request.getPatientId() == null) {
            addErrorMessage("Please select a patient");
            return false;
        }
        
        if (request.getDoctorId() == null) {
            addErrorMessage("Please select a doctor");
            return false;
        }
        
        if (request.getAppointmentDate() == null) {
            addErrorMessage("Please select appointment date and time");
            return false;
        }
        
        if (request.getAppointmentDate().before(new Date())) {
            addErrorMessage("Appointment date must be in the future");
            return false;
        }
        
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            addErrorMessage("Please provide a reason for the appointment");
            return false;
        }
        
        // Check if time slot is available
        if (!appointmentService.isTimeSlotAvailable(request.getDoctorId(), request.getAppointmentDate())) {
            addErrorMessage("The selected time slot is not available. Please choose another time.");
            return false;
        }
        
        return true;
    }
    
    // ==================== APPOINTMENT APPROVAL ====================
    
    public void approveAppointment() {
        logger.info("Approving appointment: " + selectedAppointment.getAppointmentId());
        
        try {
            Appointment appointment = appointmentService.approveAppointment(
                selectedAppointment.getAppointmentId(),
                currentUser.getId(),
                currentUserRole.name(),
                approvalNotes
            );
            
            // Reset form and close dialog
            selectedAppointment = null;
            approvalNotes = null;
            showApprovalDialog = false;
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment approved successfully!");
            logger.info("Appointment approved successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error approving appointment: " + e.getMessage());
            addErrorMessage("Error approving appointment: " + e.getMessage());
        }
    }
    
    public void rejectAppointment() {
        logger.info("Rejecting appointment: " + selectedAppointment.getAppointmentId());
        
        try {
            Appointment appointment = appointmentService.rejectAppointment(
                selectedAppointment.getAppointmentId(),
                currentUser.getId(),
                currentUserRole.name(),
                rejectionReason
            );
            
            // Reset form and close dialog
            selectedAppointment = null;
            rejectionReason = null;
            showApprovalDialog = false;
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment rejected successfully!");
            logger.info("Appointment rejected successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error rejecting appointment: " + e.getMessage());
            addErrorMessage("Error rejecting appointment: " + e.getMessage());
        }
    }
    
    public void confirmAppointment() {
        logger.info("Confirming appointment: " + selectedAppointment.getAppointmentId());
        
        try {
            Appointment appointment = appointmentService.confirmAppointment(
                selectedAppointment.getAppointmentId(),
                approvalNotes
            );
            
            // Reset form and close dialog
            selectedAppointment = null;
            approvalNotes = null;
            showApprovalDialog = false;
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment confirmed successfully!");
            logger.info("Appointment confirmed successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error confirming appointment: " + e.getMessage());
            addErrorMessage("Error confirming appointment: " + e.getMessage());
        }
    }
    
    // ==================== APPOINTMENT RESCHEDULING ====================
    
    public void rescheduleAppointment() {
        logger.info("Rescheduling appointment: " + selectedAppointment.getAppointmentId());
        
        try {
            if (rescheduleDate == null) {
                addErrorMessage("Please select a new date and time");
                return;
            }
            
            if (rescheduleDate.before(new Date())) {
                addErrorMessage("New appointment date must be in the future");
                return;
            }
            
            Appointment appointment = appointmentService.rescheduleAppointment(
                selectedAppointment.getAppointmentId(),
                rescheduleDate,
                rescheduleReason
            );
            
            // Reset form and close dialog
            selectedAppointment = null;
            rescheduleDate = null;
            rescheduleReason = null;
            showRescheduleDialog = false;
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment rescheduled successfully!");
            logger.info("Appointment rescheduled successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error rescheduling appointment: " + e.getMessage());
            addErrorMessage("Error rescheduling appointment: " + e.getMessage());
        }
    }
    
    // ==================== APPOINTMENT ACTIONS ====================
    
    public void markAsNoShow() {
        logger.info("Marking appointment as no-show: " + selectedAppointment.getAppointmentId());
        
        try {
            appointmentService.markAsNoShow(selectedAppointment.getAppointmentId(), "Marked as no-show by " + currentUserRole.name());
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment marked as no-show successfully!");
            logger.info("Appointment marked as no-show successfully: " + selectedAppointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error marking appointment as no-show: " + e.getMessage());
            addErrorMessage("Error marking appointment as no-show: " + e.getMessage());
        }
    }
    
    public void cancelAppointment() {
        logger.info("Cancelling appointment: " + selectedAppointment.getAppointmentId());
        
        try {
            Appointment appointment = appointmentService.rejectAppointment(
                selectedAppointment.getAppointmentId(),
                currentUser.getId(),
                currentUserRole.name(),
                "Cancelled by " + currentUserRole.name()
            );
            
            // Reload appointments
            loadAppointments();
            loadStatistics();
            
            addSuccessMessage("Appointment cancelled successfully!");
            logger.info("Appointment cancelled successfully: " + appointment.getAppointmentId());
            
        } catch (Exception e) {
            logger.severe("Error cancelling appointment: " + e.getMessage());
            addErrorMessage("Error cancelling appointment: " + e.getMessage());
        }
    }
    
    // ==================== DIALOG MANAGEMENT ====================
    
    public void openScheduleDialog() {
        newAppointment = new AppointmentRequestDTO();
        showScheduleDialog = true;
    }
    
    public void openApprovalDialog(Appointment appointment) {
        selectedAppointment = appointment;
        approvalNotes = null;
        rejectionReason = null;
        showApprovalDialog = true;
    }
    
    public void openRescheduleDialog(Appointment appointment) {
        selectedAppointment = appointment;
        rescheduleDate = null;
        rescheduleReason = null;
        showRescheduleDialog = true;
    }
    
    public void closeScheduleDialog() {
        showScheduleDialog = false;
        newAppointment = new AppointmentRequestDTO();
    }
    
    public void closeApprovalDialog() {
        showApprovalDialog = false;
        selectedAppointment = null;
        approvalNotes = null;
        rejectionReason = null;
    }
    
    public void closeRescheduleDialog() {
        showRescheduleDialog = false;
        selectedAppointment = null;
        rescheduleDate = null;
        rescheduleReason = null;
    }
    
    // ==================== FILTERING AND SEARCH ====================
    
    public void filterAppointments() {
        logger.info("Filtering appointments with status: " + selectedStatus + ", doctor: " + selectedDoctorId + ", patient: " + selectedPatientId);
        
        try {
            // This would implement filtering logic based on selected criteria
            loadAppointments();
            logger.info("Appointments filtered successfully");
        } catch (Exception e) {
            logger.severe("Error filtering appointments: " + e.getMessage());
            addErrorMessage("Error filtering appointments: " + e.getMessage());
        }
    }
    
    public void searchAppointments() {
        logger.info("Searching appointments with term: " + searchTerm);
        
        try {
            // This would implement search logic
            loadAppointments();
            logger.info("Appointments searched successfully");
        } catch (Exception e) {
            logger.severe("Error searching appointments: " + e.getMessage());
            addErrorMessage("Error searching appointments: " + e.getMessage());
        }
    }
    
    public void clearFilters() {
        selectedStatus = null;
        selectedDoctorId = null;
        selectedPatientId = null;
        selectedDate = null;
        searchTerm = null;
        
        loadAppointments();
        addInfoMessage("Filters cleared");
    }
    
    // ==================== UTILITY METHODS ====================
    
    public boolean canScheduleAppointments() {
        return currentUserRole == UserRole.STAFF || currentUserRole == UserRole.PATIENT || currentUserRole == UserRole.ADMIN;
    }
    
    public boolean canApproveAppointments() {
        return currentUserRole == UserRole.ADMIN || currentUserRole == UserRole.DOCTOR;
    }
    
    public boolean canConfirmAppointments() {
        return currentUserRole == UserRole.STAFF || currentUserRole == UserRole.ADMIN;
    }
    
    public boolean canRescheduleAppointments() {
        return currentUserRole == UserRole.STAFF || currentUserRole == UserRole.ADMIN || currentUserRole == UserRole.DOCTOR;
    }
    
    public boolean canCancelAppointments() {
        return currentUserRole == UserRole.STAFF || currentUserRole == UserRole.ADMIN || currentUserRole == UserRole.DOCTOR || currentUserRole == UserRole.PATIENT;
    }
    
    public String getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "SCHEDULED": return "warning";
            case "CONFIRMED": return "info";
            case "COMPLETED": return "success";
            case "CANCELLED": return "danger";
            case "NO_SHOW": return "secondary";
            default: return "primary";
        }
    }
    
    public String getPriorityColor(String priority) {
        switch (priority.toUpperCase()) {
            case "LOW": return "success";
            case "NORMAL": return "info";
            case "HIGH": return "warning";
            case "URGENT": return "danger";
            default: return "primary";
        }
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
    
    public AppointmentRequestDTO getNewAppointment() {
        return newAppointment;
    }
    
    public void setNewAppointment(AppointmentRequestDTO newAppointment) {
        this.newAppointment = newAppointment;
    }
    
    public List<Appointment> getPendingAppointments() {
        return pendingAppointments;
    }
    
    public void setPendingAppointments(List<Appointment> pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }
    
    public List<Appointment> getConfirmedAppointments() {
        return confirmedAppointments;
    }
    
    public void setConfirmedAppointments(List<Appointment> confirmedAppointments) {
        this.confirmedAppointments = confirmedAppointments;
    }
    
    public List<Appointment> getCompletedAppointments() {
        return completedAppointments;
    }
    
    public void setCompletedAppointments(List<Appointment> completedAppointments) {
        this.completedAppointments = completedAppointments;
    }
    
    public List<Appointment> getCancelledAppointments() {
        return cancelledAppointments;
    }
    
    public void setCancelledAppointments(List<Appointment> cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }
    
    public List<Appointment> getNoShowAppointments() {
        return noShowAppointments;
    }
    
    public void setNoShowAppointments(List<Appointment> noShowAppointments) {
        this.noShowAppointments = noShowAppointments;
    }
    
    public List<Appointment> getUrgentAppointments() {
        return urgentAppointments;
    }
    
    public void setUrgentAppointments(List<Appointment> urgentAppointments) {
        this.urgentAppointments = urgentAppointments;
    }
    
    public List<Doctor> getAvailableDoctors() {
        return availableDoctors;
    }
    
    public void setAvailableDoctors(List<Doctor> availableDoctors) {
        this.availableDoctors = availableDoctors;
    }
    
    public List<Patient> getAllPatients() {
        return allPatients;
    }
    
    public void setAllPatients(List<Patient> allPatients) {
        this.allPatients = allPatients;
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
    
    public Map<String, Long> getAppointmentStats() {
        return appointmentStats;
    }
    
    public void setAppointmentStats(Map<String, Long> appointmentStats) {
        this.appointmentStats = appointmentStats;
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
    
    public boolean isShowScheduleDialog() {
        return showScheduleDialog;
    }
    
    public void setShowScheduleDialog(boolean showScheduleDialog) {
        this.showScheduleDialog = showScheduleDialog;
    }
    
    public boolean isShowApprovalDialog() {
        return showApprovalDialog;
    }
    
    public void setShowApprovalDialog(boolean showApprovalDialog) {
        this.showApprovalDialog = showApprovalDialog;
    }
    
    public boolean isShowRescheduleDialog() {
        return showRescheduleDialog;
    }
    
    public void setShowRescheduleDialog(boolean showRescheduleDialog) {
        this.showRescheduleDialog = showRescheduleDialog;
    }
    
    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }
    
    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }
    
    public String getApprovalNotes() {
        return approvalNotes;
    }
    
    public void setApprovalNotes(String approvalNotes) {
        this.approvalNotes = approvalNotes;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public Date getRescheduleDate() {
        return rescheduleDate;
    }
    
    public void setRescheduleDate(Date rescheduleDate) {
        this.rescheduleDate = rescheduleDate;
    }
    
    public String getRescheduleReason() {
        return rescheduleReason;
    }
    
    public void setRescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
    }
} 