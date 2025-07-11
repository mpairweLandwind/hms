package com.hms2.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.service.AppointmentService;
import com.hms2.service.BillingService;
import com.hms2.service.MedicalRecordService;
import com.hms2.service.PatientService;
import com.hms2.service.PrescriptionService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named("patientDashboardController")
@RequestScoped
public class PatientDashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientDashboardController.class);
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private MedicalRecordService medicalRecordService;
    
    @Inject
    private PrescriptionService prescriptionService;
    
    @Inject
    private BillingService billingService;
    
    private Patient currentPatient;
    private List<Appointment> upcomingAppointments = new ArrayList<>();
    private List<MedicalRecord> recentMedicalRecords = new ArrayList<>();
    private List<Prescription> activePrescriptions = new ArrayList<>();
    private List<Billing> pendingBills = new ArrayList<>();
    
    // Statistics
    private Map<String, Long> appointmentStats;
    private Map<String, Long> healthStats;
    private Map<String, Double> billingStats;
    private Map<String, Object> healthMetrics;
    
    // Chart Data
    private Map<String, Object> appointmentHistoryData;
    private Map<String, Object> healthTrendData;
    private Map<String, Object> billingHistoryData;
    private Map<String, Object> medicationComplianceData;
    
    // Quick Actions
    private List<QuickAction> quickActions;
    private List<DashboardAlert> alerts;
    private List<HealthTip> healthTips;
    
    // Health Summary
    private String nextAppointmentDate;
    private String lastVisitDate;
    private String primaryDoctor;
    private String primaryDoctorDepartment;
    private String lastHealthUpdate;
    private String bloodType;
    private int age;
    private String healthStatus;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing patient dashboard controller");
        loadPatientData();
    }
    
    private void loadPatientData() {
        try {
            // Mock patient ID - in real app, get from session
            Long patientId = 1L; // This should come from authenticated user session
            
            currentPatient = patientService.findById(patientId);
            if (currentPatient != null) {
                loadAppointments();
                loadMedicalRecords();
                loadPrescriptions();
                loadBillings();
                loadStatistics();
                loadChartData();
                loadQuickActions();
                loadAlerts();
                loadHealthTips();
                calculateHealthSummary();
                logger.info("Patient dashboard data loaded successfully for patient: {}", patientId);
            } else {
                logger.warn("Patient not found with id: {}", patientId);
                initializeEmptyData();
            }
            
        } catch (Exception e) {
            logger.error("Error loading patient dashboard data", e);
            initializeEmptyData();
        }
    }
    
    private void initializeEmptyData() {
        upcomingAppointments = new ArrayList<>();
        recentMedicalRecords = new ArrayList<>();
        activePrescriptions = new ArrayList<>();
        pendingBills = new ArrayList<>();
        appointmentStats = new HashMap<>();
        healthStats = new HashMap<>();
        billingStats = new HashMap<>();
        healthMetrics = new HashMap<>();
        quickActions = new ArrayList<>();
        alerts = new ArrayList<>();
        healthTips = new ArrayList<>();
    }
    
    private void loadAppointments() {
        try {
            if (appointmentService != null) {
                upcomingAppointments = appointmentService.findUpcomingAppointments();
            }
        } catch (Exception e) {
            logger.error("Error loading appointments", e);
            upcomingAppointments = new ArrayList<>();
        }
    }
    
    private void loadMedicalRecords() {
        try {
            if (medicalRecordService != null) {
                recentMedicalRecords = medicalRecordService.getRecentRecordsByPatient(currentPatient.getPatientId(), 5);
            }
        } catch (Exception e) {
            logger.error("Error loading medical records", e);
            recentMedicalRecords = new ArrayList<>();
        }
    }
    
    private void loadPrescriptions() {
        try {
            if (prescriptionService != null) {
                //activePrescriptions = prescriptionService.getActivePrescriptionsByPatient(currentPatient.getPatientId());
            }
        } catch (Exception e) {
            logger.error("Error loading prescriptions", e);
            activePrescriptions = new ArrayList<>();
        }
    }
    
    private void loadBillings() {
        try {
            if (billingService != null) {
                pendingBills = billingService.getAllBillings();
            }
        } catch (Exception e) {
            logger.error("Error loading billing information", e);
            pendingBills = new ArrayList<>();
        }
    }
    
    private void loadStatistics() {
        appointmentStats = new HashMap<>();
        healthStats = new HashMap<>();
        billingStats = new HashMap<>();
        healthMetrics = new HashMap<>();
        
        try {
            // Appointment Statistics
            appointmentStats.put("totalAppointments", appointmentService.getAppointmentCount());
            appointmentStats.put("completedAppointments", appointmentService.getCompletedAppointmentCount());
            appointmentStats.put("upcomingAppointments", (long) upcomingAppointments.size());
            appointmentStats.put("cancelledAppointments", appointmentService.getCancelledAppointmentCount());
            
            // Health Statistics
            healthStats.put("medicalRecords", (long) recentMedicalRecords.size());
            healthStats.put("activePrescriptions", (long) activePrescriptions.size());
            healthStats.put("allergies", currentPatient.getAllergies() != null && !currentPatient.getAllergies().isEmpty() ? 1L : 0L);
            healthStats.put("emergencyContacts", currentPatient.getEmergencyContact() != null ? 1L : 0L);
            
            // Billing Statistics
            billingStats.put("pendingBills", (double) pendingBills.size());
            billingStats.put("totalOutstanding", calculateTotalOutstanding());
            billingStats.put("paidThisMonth", calculatePaidThisMonth());
            billingStats.put("averageBillAmount", calculateAverageBillAmount());
            
            // Health Metrics
            healthMetrics.put("bloodPressure", calculateBloodPressure());
            healthMetrics.put("heartRate", calculateHeartRate());
            healthMetrics.put("temperature", calculateTemperature());
            healthMetrics.put("weight", calculateWeight());
            
        } catch (Exception e) {
            logger.error("Error loading statistics", e);
        }
    }
    
    private void loadChartData() {
        loadAppointmentHistoryData();
        loadHealthTrendData();
        loadBillingHistoryData();
        loadMedicationComplianceData();
    }
    
    private void loadAppointmentHistoryData() {
        appointmentHistoryData = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        // Generate last 6 months appointment data
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            data.add(calculateAppointmentsForMonth(date));
        }
        
        appointmentHistoryData.put("labels", labels);
        appointmentHistoryData.put("data", data);
    }
    
    private void loadHealthTrendData() {
        healthTrendData = new HashMap<>();
        List<String> labels = Arrays.asList("Blood Pressure", "Heart Rate", "Temperature", "Weight");
        
        // Defensively get values from the map, providing a default of 0.0 if null
        Double bloodPressure = (Double) healthMetrics.getOrDefault("bloodPressure", 0.0);
        Double heartRate = (Double) healthMetrics.getOrDefault("heartRate", 0.0);
        Double temperature = (Double) healthMetrics.getOrDefault("temperature", 0.0);
        Double weight = (Double) healthMetrics.getOrDefault("weight", 0.0);

        List<Double> data = Arrays.asList(
            bloodPressure,
            heartRate,
            temperature,
            weight
        );
        
        healthTrendData.put("labels", labels);
        healthTrendData.put("data", data);
    }
    
    private void loadBillingHistoryData() {
        billingHistoryData = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        // Generate last 6 months billing data
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            data.add(calculateBillingForMonth(date));
        }
        
        billingHistoryData.put("labels", labels);
        billingHistoryData.put("data", data);
    }
    
    private void loadMedicationComplianceData() {
        medicationComplianceData = new HashMap<>();
        List<String> labels = Arrays.asList("On Time", "Late", "Missed", "Not Taken");
        List<Long> data = Arrays.asList(
            (long) (activePrescriptions.size() * 0.8), // 80% compliance
            (long) (activePrescriptions.size() * 0.15), // 15% late
            (long) (activePrescriptions.size() * 0.05), // 5% missed
            0L
        );
        
        medicationComplianceData.put("labels", labels);
        medicationComplianceData.put("data", data);
    }
    
    private void loadQuickActions() {
        quickActions = new ArrayList<>();
        quickActions.add(new QuickAction("Book Appointment", "pi pi-calendar-plus", "book-appointment", "primary"));
        quickActions.add(new QuickAction("View Medical Records", "pi pi-folder-open", "medical-records", "info"));
        quickActions.add(new QuickAction("Request Prescription", "pi pi-file-edit", "request-prescription", "success"));
        quickActions.add(new QuickAction("Pay Bills", "pi pi-credit-card", "pay-bills", "warning"));
        quickActions.add(new QuickAction("Update Profile", "pi pi-user-edit", "update-profile", "secondary"));
        quickActions.add(new QuickAction("Contact Doctor", "pi pi-comments", "contact-doctor", "info"));
    }
    
    private void loadAlerts() {
        alerts = new ArrayList<>();
        try {
            // Medication Alerts
            if (!activePrescriptions.isEmpty() && activePrescriptions.get(0).getMedication() != null) {
                alerts.add(new DashboardAlert(
                    "Medication Reminder", 
                    "Time to take your medication: " + activePrescriptions.get(0).getMedication().getMedicationName(),
                    "info", 
                    "low",
                    "pi pi-pills"
                ));
            }
            
            // Appointment Alerts
            if (!upcomingAppointments.isEmpty() && getDaysUntilNextAppointment() <= 2) {
                alerts.add(new DashboardAlert(
                    "Upcoming Appointment", 
                    "Your " + upcomingAppointments.get(0).getAppointmentType() + " is in " + getDaysUntilNextAppointment() + " days.",
                    "warn", 
                    "medium",
                    "pi pi-calendar"
                ));
        }
        
            // Billing Alerts
            if (hasPendingBills()) {
                alerts.add(new DashboardAlert(
                    "Pending Bill", 
                    "You have " + getPendingBillsCount() + " pending bills.", 
                    "error", 
                    "high",
                    "pi pi-credit-card"
                ));
        }
        
            // General Health Alerts
            if ("Needs Attention".equals(healthStatus)) {
                alerts.add(new DashboardAlert(
                    "Health Status Alert", 
                    "Your health status requires attention. Please review your recent records.", 
                    "warn", 
                    "medium",
                    "pi pi-exclamation-triangle"
                ));
            }
        } catch (Exception e) {
            logger.error("Error loading alerts", e);
        }
    }
    
    private void loadHealthTips() {
        healthTips = new ArrayList<>();
        healthTips.add(new HealthTip("Stay Hydrated", "Drink at least 8 glasses of water daily", "pi pi-tint"));
        healthTips.add(new HealthTip("Regular Exercise", "Aim for 30 minutes of physical activity daily", "pi pi-heart"));
        healthTips.add(new HealthTip("Healthy Diet", "Include fruits and vegetables in your meals", "pi pi-apple"));
        healthTips.add(new HealthTip("Get Enough Sleep", "Aim for 7-9 hours of sleep per night", "pi pi-moon"));
    }
    
    private void calculateHealthSummary() {
        if (currentPatient == null) {
            return;
        }
        
        // Next Appointment
            if (!upcomingAppointments.isEmpty()) {
            Appointment nextAppointment = upcomingAppointments.get(0);
            nextAppointmentDate = nextAppointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a"));
            } else {
                nextAppointmentDate = "No upcoming appointments";
            }
            
        // Last Visit
            if (!recentMedicalRecords.isEmpty()) {
            lastVisitDate = recentMedicalRecords.get(0).getVisitDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
            lastHealthUpdate = lastVisitDate;
            } else {
                lastVisitDate = "No recent visits";
            lastHealthUpdate = "N/A";
            }
            
        // Primary Doctor
        if (currentPatient.getPrimaryDoctor() != null) {
            primaryDoctor = currentPatient.getPrimaryDoctor().getFirstName() + " " + currentPatient.getPrimaryDoctor().getLastName();
            primaryDoctorDepartment = currentPatient.getPrimaryDoctor().getDepartment().getDepartmentName();
        } else {
            primaryDoctor = "Not assigned";
            primaryDoctorDepartment = "N/A";
        }
        
        // Blood Type
        bloodType = currentPatient.getBloodType() != null ? currentPatient.getBloodType().toString() : "N/A";
        
        // Age
        if (currentPatient.getDateOfBirth() != null) {
            age = (int) ChronoUnit.YEARS.between(currentPatient.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
        }
        
        // Health Status
        healthStatus = determineHealthStatus();
    }
    
    // Helper calculation methods
    private double calculateTotalOutstanding() {
        try {
            return pendingBills.stream()
                .mapToDouble(bill -> bill.getTotalAmount() != null ? bill.getTotalAmount().doubleValue() : 0.0)
                .sum();
        } catch (Exception e) {
            logger.error("Error calculating total outstanding", e);
            return 0.0;
        }
    }
    
    private double calculatePaidThisMonth() {
        // Mock calculation
        return new Random().nextDouble() * 1000.0;
    }
    
    private double calculateAverageBillAmount() {
            if (pendingBills.isEmpty()) return 0.0;
            return pendingBills.stream()
            .mapToDouble(b -> b.getTotalAmount().doubleValue())
                .average()
                .orElse(0.0);
    }
    
    private double calculateBloodPressure() {
        // Mock data
        return 120.0;
    }
    
    private double calculateHeartRate() {
        // Mock data
        return 75.0;
    }
    
    private double calculateTemperature() {
        // Mock data
        return 98.6;
    }
    
    private double calculateWeight() {
        if (currentPatient != null && !recentMedicalRecords.isEmpty()) {
            // Find the most recent weight from medical records
            return recentMedicalRecords.stream()
                .filter(r -> r.getWeight() != null)
                .mapToDouble(MedicalRecord::getWeight)
                .findFirst()
                .orElse(0.0);
        }
        return 0.0;
    }
    
    private long calculateAppointmentsForMonth(LocalDate month) {
        // Mock data - replace with actual query
        return new Random().nextInt(5) + 1;
    }
    
    private double calculateBillingForMonth(LocalDate month) {
        // Mock calculation
        return new Random().nextDouble() * 500.0;
    }
    
    private String determineHealthStatus() {
        // Mock health status determination
        String[] statuses = {"Excellent", "Good", "Fair", "Needs Attention"};
        return statuses[new Random().nextInt(statuses.length)];
    }
    
    public void refreshDashboard() {
        logger.info("Refreshing patient dashboard");
        loadPatientData();
    }
    
    // Getters
    public Patient getCurrentPatient() {
        return currentPatient;
    }
    
    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments != null ? upcomingAppointments : new ArrayList<>();
    }
    
    public List<MedicalRecord> getRecentMedicalRecords() {
        return recentMedicalRecords != null ? recentMedicalRecords : new ArrayList<>();
    }
    
    public List<Prescription> getActivePrescriptions() {
        return activePrescriptions != null ? activePrescriptions : new ArrayList<>();
    }
    
    public List<Billing> getPendingBills() {
        return pendingBills != null ? pendingBills : new ArrayList<>();
    }
    
    public Map<String, Long> getAppointmentStats() {
        return appointmentStats;
    }
    
    public Map<String, Long> getHealthStats() {
        return healthStats;
    }
    
    public Map<String, Double> getBillingStats() {
        return billingStats;
    }
    
    public Map<String, Object> getHealthMetrics() {
        return healthMetrics;
    }
    
    public Map<String, Object> getAppointmentHistoryData() {
        return appointmentHistoryData;
    }
    
    public Map<String, Object> getHealthTrendData() {
        return healthTrendData;
    }
    
    public Map<String, Object> getBillingHistoryData() {
        return billingHistoryData;
    }
    
    public Map<String, Object> getMedicationComplianceData() {
        return medicationComplianceData;
    }
    
    public List<QuickAction> getQuickActions() {
        return quickActions;
    }
    
    public List<DashboardAlert> getAlerts() {
        return alerts;
    }
    
    public List<HealthTip> getHealthTips() {
        return healthTips;
    }
    
    public String getNextAppointmentDate() {
        return nextAppointmentDate;
    }
    
    public String getLastVisitDate() {
        return lastVisitDate;
    }
    
    public String getPrimaryDoctor() {
        return primaryDoctor;
    }
    
    public String getPrimaryDoctorDepartment() {
        return primaryDoctorDepartment;
    }
    
    public String getLastHealthUpdate() {
        return lastHealthUpdate;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getHealthStatus() {
        return healthStatus;
    }
    
    public long getTotalAppointments() {
        return appointmentStats.getOrDefault("totalAppointments", 0L);
    }
    
    public long getCompletedAppointments() {
        return appointmentStats.getOrDefault("completedAppointments", 0L);
    }
    
    public long getPendingBillsCount() {
        return billingStats.getOrDefault("pendingBills", 0.0).longValue();
    }
    
    public double getTotalOutstanding() {
        return billingStats.getOrDefault("totalOutstanding", 0.0);
    }
    
    public boolean hasUpcomingAppointments() {
        return upcomingAppointments != null && !upcomingAppointments.isEmpty();
    }
    
    public boolean hasRecentMedicalRecords() {
        return recentMedicalRecords != null && !recentMedicalRecords.isEmpty();
    }
    
    public boolean hasActivePrescriptions() {
        return activePrescriptions != null && !activePrescriptions.isEmpty();
    }
    
    public boolean hasPendingBills() {
        return pendingBills != null && !pendingBills.isEmpty();
    }
    
    public String getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "completed":
            case "paid":
                return "success";
            case "scheduled":
            case "pending":
                return "warning";
            case "cancelled":
            case "overdue":
                return "danger";
            default:
                return "info";
        }
    }
    
    public long getDaysUntilNextAppointment() {
        if (upcomingAppointments != null && !upcomingAppointments.isEmpty()) {
            LocalDateTime nextAppointmentDateTime = upcomingAppointments.get(0).getAppointmentDateTime();
            return ChronoUnit.DAYS.between(LocalDate.now(), nextAppointmentDateTime.toLocalDate());
        }
        return 0;
    }
    
    public String getAllergies() {
        return currentPatient != null && currentPatient.getAllergies() != null ? currentPatient.getAllergies() : "None reported";
    }

    public String getChronicConditions() {
        // This is a placeholder as the model doesn't have a chronic conditions field.
        return "None reported";
    }

    public String getCurrentMedications() {
        if (activePrescriptions != null && !activePrescriptions.isEmpty()) {
            return String.valueOf(activePrescriptions.size()) + " active prescriptions";
        }
        return "None";
    }

    public List<Object> getRecentActivity() {
        // Placeholder implementation
        return Collections.emptyList();
    }

    public List<Object> getImportantNotices() {
        // Placeholder implementation
        return Collections.emptyList();
    }
    
    // --- Logout functionality ---
    public String logout() {
        logger.info("Patient logging out");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "You have been logged out successfully"));
        return "/index.xhtml?faces-redirect=true";
    }
    
    // Inner classes
    public static class QuickAction {
        private String title;
        private String icon;
        private String action;
        private String color;
        
        public QuickAction(String title, String icon, String action, String color) {
            this.title = title;
            this.icon = icon;
            this.action = action;
            this.color = color;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getIcon() { return icon; }
        public String getAction() { return action; }
        public String getColor() { return color; }
    }
    
    public static class DashboardAlert {
        private String title;
        private String message;
        private String severity;
        private String priority;
        private LocalDateTime timestamp;
        private String icon;
        
        public DashboardAlert(String title, String message, String severity, String priority, String icon) {
            this.title = title;
            this.message = message;
            this.severity = severity;
            this.priority = priority;
            this.timestamp = LocalDateTime.now();
            this.icon = icon;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getSeverity() { return severity; }
        public String getPriority() { return priority; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getIcon() { return icon; }
    }
    
    public static class HealthTip {
        private String title;
        private String description;
        private String icon;
        
        public HealthTip(String title, String description, String icon) {
            this.title = title;
            this.description = description;
            this.icon = icon;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }
}
