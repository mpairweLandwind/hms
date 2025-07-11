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
import com.hms2.model.Billing;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Staff;
import com.hms2.service.AppointmentService;
import com.hms2.service.BillingService;
import com.hms2.service.DoctorService;
import com.hms2.service.PatientService;
import com.hms2.service.StaffService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("staffDashboardController")
@RequestScoped
public class StaffDashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffDashboardController.class);
    
    @Inject
    private StaffService staffService;
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private BillingService billingService;
    
    @Inject
    private DoctorService doctorService;
    
    private Staff currentStaff;
    private List<Appointment> todaysAppointments;
    private List<Patient> recentPatients;
    private List<Billing> pendingBills;
    private List<Doctor> availableDoctors;
    
    // Statistics
    private Map<String, Long> operationalStats = new HashMap<>();
    private Map<String, Long> patientStats = new HashMap<>();
    private Map<String, Long> appointmentStats = new HashMap<>();
    private Map<String, Double> financialStats = new HashMap<>();
    
    // Chart Data
    private Map<String, Object> dailyAppointmentsData = new HashMap<>();
    private Map<String, Object> patientRegistrationData = new HashMap<>();
    private Map<String, Object> billingStatusData = new HashMap<>();
    private Map<String, Object> doctorWorkloadData = new HashMap<>();
    
    // Quick Actions
    private List<QuickAction> quickActions;
    private List<DashboardAlert> alerts;
    private List<TaskItem> pendingTasks;
    
    // Performance Metrics
    private double patientSatisfaction;
    private double appointmentCompletionRate;
    private int patientsProcessedToday;
    private int billsProcessedToday;
    private double serviceQualityScore;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing staff dashboard controller");
        loadStaffData();
    }
    
    private void loadStaffData() {
        try {
            // Mock staff ID - in real app, get from session
            Long staffId = 1L; // This should come from authenticated user session
            
            currentStaff = staffService.getStaffById(staffId).orElse(null);
            if (currentStaff != null) {
                loadAppointments();
                loadPatients();
                loadBillings();
                loadDoctors();
                loadStatistics();
                loadChartData();
                loadQuickActions();
                loadAlerts();
                loadPendingTasks();
                calculatePerformanceMetrics();
                logger.info("Staff dashboard data loaded successfully for staff: {}", staffId);
            } else {
                logger.warn("Staff not found with id: {}", staffId);
                initializeEmptyData();
            }
            
        } catch (Exception e) {
            logger.error("Error loading staff dashboard data", e);
            initializeEmptyData();
        }
    }
    
    private void initializeEmptyData() {
        todaysAppointments = new ArrayList<>();
        recentPatients = new ArrayList<>();
        pendingBills = new ArrayList<>();
        availableDoctors = new ArrayList<>();
        operationalStats = new HashMap<>();
        patientStats = new HashMap<>();
        appointmentStats = new HashMap<>();
        financialStats = new HashMap<>();
        quickActions = new ArrayList<>();
        alerts = new ArrayList<>();
        pendingTasks = new ArrayList<>();
    }
    
    private void loadAppointments() {
        try {
            todaysAppointments = appointmentService.findTodaysAppointments();
        } catch (Exception e) {
            logger.error("Error loading appointments", e);
            todaysAppointments = new ArrayList<>();
        }
    }
    
    private void loadPatients() {
        try {
            recentPatients = patientService.getActivePatients().stream()
                .limit(10)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error loading patients", e);
            recentPatients = new ArrayList<>();
        }
    }
    
    private void loadBillings() {
        try {
            pendingBills = billingService.getPendingBillings();
        } catch (Exception e) {
            logger.error("Error loading billings", e);
            pendingBills = new ArrayList<>();
        }
    }
    
    private void loadDoctors() {
        try {
            availableDoctors = doctorService.getAllDoctors().stream()
                .filter(Doctor::isActive)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error loading doctors", e);
            availableDoctors = new ArrayList<>();
        }
    }
    
    private void loadStatistics() {
        operationalStats = new HashMap<>();
        patientStats = new HashMap<>();
        appointmentStats = new HashMap<>();
        financialStats = new HashMap<>();
        
        try {
            // Operational Statistics
            operationalStats.put("totalPatientsToday", (long) recentPatients.size());
            operationalStats.put("totalAppointmentsToday", (long) todaysAppointments.size());
            operationalStats.put("pendingBillsCount", (long) pendingBills.size());
            operationalStats.put("availableDoctors", (long) availableDoctors.size());
            
            // Patient Statistics
            patientStats.put("totalPatients", patientService.getTotalPatientCount());
            patientStats.put("newPatientsThisMonth", patientService.getNewPatientsThisMonth());
            patientStats.put("activePatients", patientService.getActivePatientCount());
            patientStats.put("patientsSeenToday", (long) todaysAppointments.size());
            
            // Appointment Statistics
            appointmentStats.put("todaysAppointments", (long) todaysAppointments.size());
            appointmentStats.put("completedToday", appointmentService.getCompletedAppointmentCount());
            appointmentStats.put("cancelledToday", appointmentService.getCancelledAppointmentCount());
            appointmentStats.put("noShowToday", calculateNoShowAppointments());
            
            // Financial Statistics
            financialStats.put("pendingAmount", calculatePendingAmount());
            financialStats.put("collectedToday", calculateCollectedToday());
            financialStats.put("averageBillAmount", calculateAverageBillAmount());
            financialStats.put("overdueAmount", calculateOverdueAmount());
            
        } catch (Exception e) {
            logger.error("Error loading statistics", e);
        }
    }
    
    private void loadChartData() {
        loadDailyAppointmentsData();
        loadPatientRegistrationData();
        loadBillingStatusData();
        loadDoctorWorkloadData();
    }
    
    private void loadDailyAppointmentsData() {
        dailyAppointmentsData = new HashMap<>();
        List<String> labels = Arrays.asList("9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM");
        List<Long> data = new ArrayList<>();
        
        // Generate hourly appointment data
        for (int i = 0; i < 9; i++) {
            data.add(calculateAppointmentsForHour(i + 9));
        }
        
        dailyAppointmentsData.put("labels", labels);
        dailyAppointmentsData.put("data", data);
    }
    
    private void loadPatientRegistrationData() {
        patientRegistrationData = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        // Generate last 7 days registration data
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM dd")));
            data.add(calculateRegistrationsForDay(date));
        }
        
        patientRegistrationData.put("labels", labels);
        patientRegistrationData.put("data", data);
    }
    
    private void loadBillingStatusData() {
        billingStatusData = new HashMap<>();
        List<String> labels = Arrays.asList("Pending", "Paid", "Partially Paid", "Overdue", "Cancelled");
        List<Long> data = Arrays.asList(
            (long) pendingBills.size(),
            calculatePaidBillsCount(),
            0L, // Partially paid
            calculateOverdueBillsCount(),
            0L  // Cancelled
        );
        
        billingStatusData.put("labels", labels);
        billingStatusData.put("data", data);
    }
    
    private void loadDoctorWorkloadData() {
        doctorWorkloadData = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        for (Doctor doctor : availableDoctors) {
            labels.add(doctor.getFirstName() + " " + doctor.getLastName());
            data.add(calculateDoctorWorkload(doctor.getDoctorId()));
        }
        
        doctorWorkloadData.put("labels", labels);
        doctorWorkloadData.put("data", data);
    }
    
    private void loadQuickActions() {
        quickActions = new ArrayList<>();
        quickActions.add(new QuickAction("Register Patient", "pi pi-user-plus", "register-patient", "primary"));
        quickActions.add(new QuickAction("Schedule Appointment", "pi pi-calendar-plus", "schedule-appointment", "success"));
        quickActions.add(new QuickAction("Process Payment", "pi pi-credit-card", "process-payment", "warning"));
        quickActions.add(new QuickAction("Patient Search", "pi pi-search", "search-patients", "info"));
        quickActions.add(new QuickAction("Generate Report", "pi pi-chart-bar", "generate-report", "secondary"));
        quickActions.add(new QuickAction("Update Records", "pi pi-pencil", "update-records", "info"));
    }
    
    private void loadAlerts() {
        alerts = new ArrayList<>();
        
        // Check for urgent appointments
        if (todaysAppointments.stream().anyMatch(apt -> "URGENT".equals(apt.getAppointmentType()))) {
            alerts.add(new DashboardAlert("Urgent Appointments", 
                "You have urgent appointments today", "danger", "high"));
        }
        
        // Check for pending bills
        if (pendingBills.size() > 10) {
            alerts.add(new DashboardAlert("High Pending Bills", 
                pendingBills.size() + " bills pending processing", "warning", "medium"));
        }
        
        // Check for no-show appointments
        if (appointmentStats.get("noShowToday") > 2) {
            alerts.add(new DashboardAlert("No-Show Appointments", 
                appointmentStats.get("noShowToday") + " no-show appointments today", "warning", "medium"));
        }
        
        // Check for overdue payments
        if (financialStats.get("overdueAmount") > 5000.0) {
            alerts.add(new DashboardAlert("Overdue Payments", 
                "Overdue amount exceeds $5,000", "danger", "high"));
        }
    }
    
    private void loadPendingTasks() {
        pendingTasks = new ArrayList<>();
        pendingTasks.add(new TaskItem("Process patient registrations", "Register new patients in the system", "high", "patient-registration", "pi pi-user-plus", LocalDate.now().plusDays(1)));
        pendingTasks.add(new TaskItem("Update medical records", "Ensure all records are up to date", "medium", "medical-records", "pi pi-file-edit", LocalDate.now().plusDays(2)));
        pendingTasks.add(new TaskItem("Schedule follow-up appointments", "Arrange follow-ups for patients", "medium", "appointments", "pi pi-calendar-plus", LocalDate.now().plusDays(3)));
        pendingTasks.add(new TaskItem("Verify insurance information", "Check and verify patient insurance", "low", "insurance", "pi pi-id-card", LocalDate.now().plusDays(4)));
    }
    
    private void calculatePerformanceMetrics() {
        patientSatisfaction = calculatePatientSatisfaction();
        appointmentCompletionRate = calculateAppointmentCompletionRate();
        patientsProcessedToday = calculatePatientsProcessedToday();
        billsProcessedToday = calculateBillsProcessedToday();
        serviceQualityScore = calculateServiceQualityScore();
    }
    
    // Helper calculation methods
    private long calculateNoShowAppointments() {
        try {
            return todaysAppointments.stream()
                .filter(apt -> "NO_SHOW".equals(apt.getStatus()))
                .count();
        } catch (Exception e) {
            logger.error("Error calculating no-show appointments", e);
            return 0;
        }
    }
    
    private double calculatePendingAmount() {
        try {
            return pendingBills.stream()
                .mapToDouble(bill -> bill.getTotalAmount() != null ? bill.getTotalAmount().doubleValue() : 0.0)
                .sum();
        } catch (Exception e) {
            logger.error("Error calculating pending amount", e);
            return 0.0;
        }
    }
    
    private double calculateCollectedToday() {
        // Mock calculation
        return new Random().nextDouble() * 2000.0;
    }
    
    private double calculateAverageBillAmount() {
        try {
            if (pendingBills.isEmpty()) return 0.0;
            return pendingBills.stream()
                .mapToDouble(bill -> bill.getTotalAmount() != null ? bill.getTotalAmount().doubleValue() : 0.0)
                .average()
                .orElse(0.0);
        } catch (Exception e) {
            logger.error("Error calculating average bill amount", e);
            return 0.0;
        }
    }
    
    private double calculateOverdueAmount() {
        try {
            return billingService.getOverdueBillings().stream()
                .mapToDouble(bill -> bill.getTotalAmount() != null ? bill.getTotalAmount().doubleValue() : 0.0)
                .sum();
        } catch (Exception e) {
            logger.error("Error calculating overdue amount", e);
            return 0.0;
        }
    }
    
    private long calculateAppointmentsForHour(int hour) {
        // Mock calculation
        return new Random().nextInt(5) + 1;
    }
    
    private long calculateRegistrationsForDay(LocalDate date) {
        // Mock calculation
        return new Random().nextInt(10) + 2;
    }
    
    private long calculatePaidBillsCount() {
        try {
            return billingService.getPaidBillings().size();
        } catch (Exception e) {
            logger.error("Error calculating paid bills count", e);
            return 0;
        }
    }
    
    private long calculateOverdueBillsCount() {
        try {
            return billingService.getOverdueBillings().size();
        } catch (Exception e) {
            logger.error("Error calculating overdue bills count", e);
            return 0;
        }
    }
    
    private long calculateDoctorWorkload(Long doctorId) {
        try {
            return todaysAppointments.stream()
                .filter(apt -> apt.getDoctor() != null && doctorId.equals(apt.getDoctor().getDoctorId()))
                .count();
        } catch (Exception e) {
            logger.error("Error calculating doctor workload", e);
            return 0;
        }
    }
    
    private double calculatePatientSatisfaction() {
        // Mock calculation
        return 4.0 + (new Random().nextDouble() * 1.0);
    }
    
    private double calculateAppointmentCompletionRate() {
        try {
            long completed = appointmentService.getCompletedAppointmentCount();
            long total = appointmentService.getAppointmentCount();
            return total > 0 ? (double) completed / total * 100 : 0.0;
        } catch (Exception e) {
            logger.error("Error calculating appointment completion rate", e);
            return 90.0; // Default value
        }
    }
    
    private int calculatePatientsProcessedToday() {
        return todaysAppointments.size();
    }
    
    private int calculateBillsProcessedToday() {
        // Mock calculation
        return new Random().nextInt(20) + 5;
    }
    
    private double calculateServiceQualityScore() {
        // Mock calculation - in real app, get from feedback/metrics
        return 4.2 + (new Random().nextDouble() * 0.8);
    }
    
    public void refreshDashboard() {
        logger.info("Refreshing staff dashboard");
        loadStaffData();
    }
    
    // Getters
    public Staff getCurrentStaff() {
        return currentStaff;
    }
    
    public List<Appointment> getTodaysAppointments() {
        return todaysAppointments;
    }
    
    public List<Patient> getRecentPatients() {
        return recentPatients;
    }
    
    public List<Billing> getPendingBills() {
        return pendingBills;
    }
    
    public List<Doctor> getAvailableDoctors() {
        return availableDoctors;
    }
    
    public Map<String, Long> getOperationalStats() {
        return operationalStats;
    }
    
    public Map<String, Long> getPatientStats() {
        return patientStats;
    }
    
    public Map<String, Long> getAppointmentStats() {
        return appointmentStats;
    }
    
    public Map<String, Double> getFinancialStats() {
        return financialStats;
    }
    
    public Map<String, Object> getDailyAppointmentsData() {
        return dailyAppointmentsData;
    }
    
    public Map<String, Object> getPatientRegistrationData() {
        return patientRegistrationData;
    }
    
    public Map<String, Object> getBillingStatusData() {
        return billingStatusData;
    }
    
    public Map<String, Object> getDoctorWorkloadData() {
        return doctorWorkloadData;
    }
    
    public List<QuickAction> getQuickActions() {
        return quickActions;
    }
    
    public List<DashboardAlert> getAlerts() {
        return alerts;
    }
    
    public List<TaskItem> getPendingTasks() {
        return pendingTasks;
    }
    
    public double getPatientSatisfaction() {
        return patientSatisfaction;
    }
    
    public double getAppointmentCompletionRate() {
        return appointmentCompletionRate;
    }
    
    public int getPatientsProcessedToday() {
        return patientsProcessedToday;
    }
    
    public int getBillsProcessedToday() {
        return billsProcessedToday;
    }
    
    public double getServiceQualityScore() {
        return serviceQualityScore;
    }
    
    // --- Safe Statistic Getters ---

    // Operational Stats
    public long getTotalPatientsToday() {
        return operationalStats.getOrDefault("totalPatientsToday", 0L);
    }
    
    public long getTotalAppointmentsToday() {
        return operationalStats.getOrDefault("totalAppointmentsToday", 0L);
    }
    
    public long getPendingBillsCount() {
        return operationalStats.getOrDefault("pendingBillsCount", 0L);
    }
    
    public long getAvailableDoctorsCount() {
        return operationalStats.getOrDefault("availableDoctors", 0L);
    }

    // Patient Stats
    public long getTotalPatientsCount() {
        return patientStats.getOrDefault("totalPatients", 0L);
    }

    public long getNewPatientsThisMonthCount() {
        return patientStats.getOrDefault("newPatientsThisMonth", 0L);
    }

    public long getActivePatientsCount() {
        return patientStats.getOrDefault("activePatients", 0L);
    }

    public long getPatientsSeenTodayCount() {
        return patientStats.getOrDefault("patientsSeenToday", 0L);
    }

    // Appointment Stats
    public long getCompletedAppointmentsToday() {
        return appointmentStats.getOrDefault("completedToday", 0L);
    }

    public long getCancelledAppointmentsToday() {
        return appointmentStats.getOrDefault("cancelledToday", 0L);
    }

    public long getNoShowAppointmentsToday() {
        return appointmentStats.getOrDefault("noShowToday", 0L);
    }

    // Financial Stats
    public double getTotalPendingAmount() {
        return financialStats.getOrDefault("pendingAmount", 0.0);
    }

    public double getAmountCollectedToday() {
        return financialStats.getOrDefault("collectedToday", 0.0);
    }

    public double getAverageBillAmount() {
        return financialStats.getOrDefault("averageBillAmount", 0.0);
    }

    public double getOverdueAmount() {
        return financialStats.getOrDefault("overdueAmount", 0.0);
    }
    
    public String getStatusColor(String status) {
        if (status == null) {
            return "info";
        }
        switch (status.toLowerCase()) {
            case "completed":
            case "paid":
                return "success";
            case "scheduled":
            case "pending":
                return "warning";
            case "cancelled":
            case "overdue":
            case "no_show":
                return "danger";
            default:
                return "info";
        }
    }
    
    public long getDoctorWorkload(Long doctorId) {
        if (doctorId == null) {
            return 0L;
        }
        return calculateDoctorWorkload(doctorId);
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
        
        public DashboardAlert(String title, String message, String severity, String priority) {
            this.title = title;
            this.message = message;
            this.severity = severity;
            this.priority = priority;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getSeverity() { return severity; }
        public String getPriority() { return priority; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class TaskItem {
        private String title;
        private String description;
        private String priority;
        private String category;
        private String icon;
        private LocalDate dueDate;
        
        public TaskItem(String title, String description, String priority, String category, String icon, LocalDate dueDate) {
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.category = category;
            this.icon = icon;
            this.dueDate = dueDate;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
        public String getCategory() { return category; }
        public String getIcon() { return icon; }
        public LocalDate getDueDate() { return dueDate; }
    }
}
