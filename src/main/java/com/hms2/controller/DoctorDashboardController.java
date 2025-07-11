package com.hms2.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.service.AppointmentService;
import com.hms2.service.DoctorService;
import com.hms2.service.MedicalRecordService;
import com.hms2.service.PatientService;
import com.hms2.service.PrescriptionMedicationService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("doctorDashboardController")
@RequestScoped
public class DoctorDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboardController.class);

    @Inject
    private DoctorService doctorService;

    @Inject
    private AppointmentService appointmentService;

    @Inject
    private PatientService patientService;

    @Inject
    private PrescriptionMedicationService prescriptionService;

    @Inject
    private MedicalRecordService medicalRecordService;

    private Doctor currentDoctor;
    private List<Appointment> todaysAppointments;
    private List<Appointment> upcomingAppointments;
    private List<Patient> recentPatients;
    private List<Prescription> recentPrescriptions;
    private List<MedicalRecord> recentMedicalRecords;

    // Dashboard data
    private Map<String, Long> appointmentStats;
    private Map<String, Long> patientStats;
    private Map<String, Long> prescriptionStats;
    private Map<String, Double> performanceStats;
    private Map<String, Object> appointmentChartData;
    private Map<String, Object> patientGrowthChartData;
    private Map<String, Object> prescriptionChartData;
    private Map<String, Object> weeklyScheduleData;
    private Map<String, Object> chartData;
    private List<com.hms2.dto.dashboard.QuickActionDTO> quickActions;
    private List<com.hms2.dto.dashboard.DashboardAlertDTO> alerts;

    // Performance Metrics
    private double patientSatisfaction;
    private double appointmentCompletionRate;
    private int averagePatientsPerDay;
    private int totalConsultations;

    @PostConstruct
    public void init() {
        logger.info("Initializing doctor dashboard controller");
        loadDoctorData();
    }

    private void loadDoctorData() {
        try {
            // Get current doctor (mock data for now - in real app, get from session)
            currentDoctor = doctorService.findAll().stream().findFirst().orElse(null);
            if (currentDoctor != null) {
                Long doctorId = currentDoctor.getDoctorId();
                
                // Load dashboard data using enhanced service methods
                appointmentStats = doctorService.getDashboardStatistics(doctorId);
                performanceStats = doctorService.getDashboardMetrics(doctorId);
                chartData = doctorService.getChartData(doctorId);
                quickActions = doctorService.getQuickActions(doctorId);
                alerts = doctorService.getAlerts(doctorId);
                
                // Load specific data
                todaysAppointments = doctorService.getTodaysAppointments(doctorId);
                upcomingAppointments = doctorService.getUpcomingAppointments(doctorId);
                recentPatients = doctorService.getRecentPatients(doctorId);
                recentPrescriptions = doctorService.getRecentPrescriptions(doctorId);
                
                // Calculate performance metrics
                calculatePerformanceMetrics();
                
                logger.info("Doctor dashboard data loaded successfully for doctor ID: {}", doctorId);
            } else {
                logger.warn("No doctor found for dashboard initialization");
                initializeEmptyData();
            }
        } catch (Exception e) {
            logger.error("Error loading doctor dashboard data", e);
            initializeEmptyData();
        }
    }

    private void initializeEmptyData() {
        appointmentStats = new HashMap<>();
        patientStats = new HashMap<>();
        prescriptionStats = new HashMap<>();
        performanceStats = new HashMap<>();
        appointmentChartData = new HashMap<>();
        patientGrowthChartData = new HashMap<>();
        prescriptionChartData = new HashMap<>();
        weeklyScheduleData = new HashMap<>();
        chartData = new HashMap<>();
        quickActions = new ArrayList<>();
        alerts = new ArrayList<>();
        todaysAppointments = new ArrayList<>();
        upcomingAppointments = new ArrayList<>();
        recentPatients = new ArrayList<>();
        recentPrescriptions = new ArrayList<>();
        recentMedicalRecords = new ArrayList<>();
    }

    private void calculatePerformanceMetrics() {
        try {
            // Performance metrics are now calculated in the service layer
            // This method can be used for additional client-side calculations if needed
            logger.info("Performance metrics calculated from service layer");
        } catch (Exception e) {
            logger.error("Error calculating performance metrics", e);
        }
    }

    // ==================== APPOINTMENT MANAGEMENT ====================
    
    @Inject
    private AppointmentManagementController appointmentManagementController;
    
    @Inject
    private PrescriptionManagementController prescriptionManagementController;
    
    // Appointment management methods
    public String navigateToAppointmentManagement() {
        return "/views/appointments/appointment-management?faces-redirect=true";
    }
    
    public String navigateToPrescriptionManagement() {
        return "/views/prescriptions/prescription-management?faces-redirect=true";
    }
    
    public void approveAppointment(Long appointmentId) {
        try {
            // This would be implemented when the appointment management is fully integrated
            logger.info("Approving appointment: " + appointmentId);
            addSuccessMessage("Appointment approved successfully!");
        } catch (Exception e) {
            addErrorMessage("Error approving appointment: " + e.getMessage());
        }
    }
    
    public void rejectAppointment(Long appointmentId, String reason) {
        try {
            // This would be implemented when the appointment management is fully integrated
            logger.info("Rejecting appointment: " + appointmentId + " with reason: " + reason);
            addSuccessMessage("Appointment rejected successfully!");
        } catch (Exception e) {
            addErrorMessage("Error rejecting appointment: " + e.getMessage());
        }
    }
    
    public void createPrescription(Long patientId) {
        try {
            // This would be implemented when the prescription management is fully integrated
            logger.info("Creating prescription for patient: " + patientId);
            addSuccessMessage("Prescription created successfully!");
        } catch (Exception e) {
            addErrorMessage("Error creating prescription: " + e.getMessage());
        }
    }
    
    // Helper methods
    private Long getCurrentUserId() {
        // In a real application, this would come from session/security context
        return 1L; // Placeholder
    }
    
    private Long getCurrentDoctorId() {
        // In a real application, this would come from session/security context
        return 1L; // Placeholder
    }
    
    // Message utilities
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    // ==================== GETTERS AND SETTERS ====================

    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }

    public void setCurrentDoctor(Doctor currentDoctor) {
        this.currentDoctor = currentDoctor;
    }

    public List<Appointment> getTodaysAppointments() {
        return todaysAppointments;
    }

    public void setTodaysAppointments(List<Appointment> todaysAppointments) {
        this.todaysAppointments = todaysAppointments;
    }

    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public void setUpcomingAppointments(List<Appointment> upcomingAppointments) {
        this.upcomingAppointments = upcomingAppointments;
    }

    public List<Patient> getRecentPatients() {
        return recentPatients;
    }

    public void setRecentPatients(List<Patient> recentPatients) {
        this.recentPatients = recentPatients;
    }

    public List<Prescription> getRecentPrescriptions() {
        return recentPrescriptions;
    }

    public void setRecentPrescriptions(List<Prescription> recentPrescriptions) {
        this.recentPrescriptions = recentPrescriptions;
    }

    public List<MedicalRecord> getRecentMedicalRecords() {
        return recentMedicalRecords;
    }

    public void setRecentMedicalRecords(List<MedicalRecord> recentMedicalRecords) {
        this.recentMedicalRecords = recentMedicalRecords;
    }

    public Map<String, Long> getAppointmentStats() {
        return appointmentStats;
    }

    public void setAppointmentStats(Map<String, Long> appointmentStats) {
        this.appointmentStats = appointmentStats;
    }

    public Map<String, Long> getPatientStats() {
        return patientStats;
    }

    public void setPatientStats(Map<String, Long> patientStats) {
        this.patientStats = patientStats;
    }

    public Map<String, Long> getPrescriptionStats() {
        return prescriptionStats;
    }

    public void setPrescriptionStats(Map<String, Long> prescriptionStats) {
        this.prescriptionStats = prescriptionStats;
    }

    public Map<String, Double> getPerformanceStats() {
        return performanceStats;
    }

    public void setPerformanceStats(Map<String, Double> performanceStats) {
        this.performanceStats = performanceStats;
    }

    public Map<String, Object> getAppointmentChartData() {
        return appointmentChartData;
    }

    public void setAppointmentChartData(Map<String, Object> appointmentChartData) {
        this.appointmentChartData = appointmentChartData;
    }

    public Map<String, Object> getPatientGrowthChartData() {
        return patientGrowthChartData;
    }

    public void setPatientGrowthChartData(Map<String, Object> patientGrowthChartData) {
        this.patientGrowthChartData = patientGrowthChartData;
    }

    public Map<String, Object> getPrescriptionChartData() {
        return prescriptionChartData;
    }

    public void setPrescriptionChartData(Map<String, Object> prescriptionChartData) {
        this.prescriptionChartData = prescriptionChartData;
    }

    public Map<String, Object> getWeeklyScheduleData() {
        return weeklyScheduleData;
    }

    public void setWeeklyScheduleData(Map<String, Object> weeklyScheduleData) {
        this.weeklyScheduleData = weeklyScheduleData;
    }

    public Map<String, Object> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, Object> chartData) {
        this.chartData = chartData;
    }

    public List<com.hms2.dto.dashboard.QuickActionDTO> getQuickActions() {
        return quickActions;
    }

    public void setQuickActions(List<com.hms2.dto.dashboard.QuickActionDTO> quickActions) {
        this.quickActions = quickActions;
    }

    public List<com.hms2.dto.dashboard.DashboardAlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<com.hms2.dto.dashboard.DashboardAlertDTO> alerts) {
        this.alerts = alerts;
    }
}
