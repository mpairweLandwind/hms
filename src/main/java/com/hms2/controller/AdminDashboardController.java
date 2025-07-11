package com.hms2.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.hms2.config.DatabaseInitializer;
import com.hms2.dto.department.DepartmentTableDTO;
import com.hms2.dto.user.DoctorTableDTO;
import com.hms2.dto.user.PatientTableDTO;
import com.hms2.dto.user.StaffTableDTO;
import com.hms2.dto.user.UserTableDTO;
import com.hms2.enums.UserRole;
import com.hms2.enums.DoctorStatus;
import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Department;
import com.hms2.model.Doctor;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.model.Staff;
import com.hms2.model.User;
import com.hms2.service.AppointmentService;
import com.hms2.service.AuthenticationService;
import com.hms2.service.BillingService;
import com.hms2.service.DepartmentService;
import com.hms2.service.DoctorService;
import com.hms2.service.MedicalRecordService;
import com.hms2.service.PatientService;
import com.hms2.service.PrescriptionService;
import com.hms2.service.StaffService;
import com.hms2.service.UserService;
import com.hms2.util.PasswordUtil;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.primefaces.event.SelectEvent;
import java.io.Serializable;

// --- Doctor Registration for Admin ---
import com.hms2.dto.user.DoctorRegistrationDTO;
import com.hms2.service.RegistrationService;

@Named("adminDashboardController")
@ViewScoped
public class AdminDashboardController implements Serializable {
    
    @Inject private PatientService patientService;
    @Inject private DoctorService doctorService;
    @Inject private StaffService staffService;
    @Inject private AppointmentService appointmentService;
    @Inject private BillingService billingService;
    @Inject private DepartmentService departmentService;
    @Inject private MedicalRecordService medicalRecordService;
    @Inject private PrescriptionService prescriptionService;
    @Inject private DatabaseInitializer databaseInitializer;
    @Inject private UserService userService;
    @Inject private AuthenticationService authenticationService;
    @Inject private RegistrationService registrationService;
    
    // Dashboard Statistics
    private Map<String, Long> systemStats = new HashMap<>();
    private Map<String, BigDecimal> financialStats = new HashMap<>();
    private Map<String, Long> appointmentStats = new HashMap<>();
    private Map<String, Long> userStats = new HashMap<>();
    private Map<String, Long> departmentStats = new HashMap<>();
    
    // Recent Activities
    private List<Patient> recentPatients;
    private List<Appointment> todaysAppointments;
    private List<Billing> pendingBills;
    private List<Staff> pendingVerifications;
    
    // Soft Deleted Data Management
    private List<PatientTableDTO> deletedPatients;
    private List<DoctorTableDTO> deletedDoctors;
    private List<StaffTableDTO> deletedStaff;
    private List<DepartmentTableDTO> deletedDepartments;
    private List<Appointment> deletedAppointments;
    private List<Billing> deletedBillings;
    private List<MedicalRecord> deletedMedicalRecords;
    private List<Prescription> deletedPrescriptions;
    
    // Soft Delete Statistics
    private Map<String, Long> softDeleteStats = new HashMap<>();
    
    // Chart Data
    private String revenueChartPeriod = "6m";
    private String appointmentChartPeriod = "6m";
    private Map<String, Object> revenueChartData = new HashMap<>();
    private Map<String, Object> appointmentChartData = new HashMap<>();
    private Map<String, Object> departmentChartData = new HashMap<>();
    private Map<String, Object> patientGrowthChartData = new HashMap<>();
    
    // Alerts and Notifications
    private List<DashboardAlert> systemAlerts;
    private List<DashboardAlert> financialAlerts;
    private List<DashboardAlert> operationalAlerts;
    
    // Performance Metrics
    private double systemUptime;
    private double averageResponseTime;
    private int activeUsers;
    private int criticalAlerts;
    
    // --- Department Management ---
    private List<Department> activeDepartments;
    private DepartmentTableDTO newDepartment = new DepartmentTableDTO();
    
    // --- User Management ---
    private List<UserTableDTO> allUsers;
    private List<UserTableDTO> deletedUsers;
    private UserTableDTO newUser = new UserTableDTO();
    private UserTableDTO editingUser = new UserTableDTO();
    // Add a transient field for the plain password used only for user creation
    private transient String newUserPassword;
    
    // --- Backing lists for inline editing refresh ---
    private List<DepartmentTableDTO> allDepartments;
    private List<PatientTableDTO> allPatients;
    private List<DoctorTableDTO> allDoctors;
    private List<StaffTableDTO> allStaff;
    
    // --- Selected DTOs for editing ---
    private DepartmentTableDTO selectedDepartment = new DepartmentTableDTO();
    private UserTableDTO selectedUser;
    private StaffTableDTO selectedStaff;
    private PatientTableDTO selectedPatient;
    private DoctorTableDTO selectedDoctor = createDefaultDoctorDTO();
    
    // --- Doctor Management ---
    private List<DoctorTableDTO> selectedDoctors = new ArrayList<>();
    public List<DoctorTableDTO> getSelectedDoctors() { return selectedDoctors; }
    public void setSelectedDoctors(List<DoctorTableDTO> selectedDoctors) { this.selectedDoctors = selectedDoctors; }

    public void deleteSelectedDoctors() {
        if (selectedDoctors != null && !selectedDoctors.isEmpty()) {
            for (DoctorTableDTO dto : new ArrayList<>(selectedDoctors)) {
                if (dto.getDoctorId() != null) {
                    doctorService.deleteDoctor(dto.getDoctorId());
                }
            }
            addSuccessMessage("Selected doctors deleted (soft) successfully.");
            loadAllDoctors();
            loadDeletedDoctors();
            selectedDoctors.clear();
        } else {
            addErrorMessage("No doctors selected for deletion.");
        }
    }

    // Optionally, add import/export stubs for future implementation
    public void importDoctors() {
        // TODO: Implement import logic
        addSuccessMessage("Import not implemented yet.");
    }
    public void exportDoctors() {
        // TODO: Implement export logic
        addSuccessMessage("Export not implemented yet.");
    }

    public void openNewDoctor() {
        selectedDoctor = createDefaultDoctorDTO();
        loadAllDepartments();
    }

    public void prepareEditDoctor(DoctorTableDTO doctor) {
        System.err.println("[DEBUG] prepareEditDoctor called for: " + doctor);
        selectedDoctor = doctor;
        loadAllDepartments();
    }

    private boolean validateDoctorDTO(DoctorTableDTO dto) {
        if (dto == null) {
            addErrorMessage("Doctor data is missing.");
            return false;
        }
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            addErrorMessage("First name is required.");
            return false;
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            addErrorMessage("Last name is required.");
            return false;
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            addErrorMessage("Email is required.");
            return false;
        }
        if (dto.getSpecialization() == null || dto.getSpecialization().trim().isEmpty()) {
            addErrorMessage("Specialization is required.");
            return false;
        }
        if (dto.getLicenseNumber() == null || dto.getLicenseNumber().trim().isEmpty()) {
            addErrorMessage("License number is required.");
            return false;
        }
        if (dto.getStatus() == null) {
            addErrorMessage("Status is required.");
            return false;
        }
        return true;
    }

    public void saveDoctor() {
        System.err.println("[DEBUG] AdminDashboardController.saveDoctor: START");
        if (selectedDoctor == null) {
            System.err.println("[DEBUG] saveDoctor: selectedDoctor is null");
            selectedDoctor = new DoctorTableDTO();
        }
        System.err.println("[DEBUG] saveDoctor: status value = " + selectedDoctor.getStatus() + ", type = " + (selectedDoctor.getStatus() != null ? selectedDoctor.getStatus().getClass().getName() : "null"));
        if (!validateDoctorDTO(selectedDoctor)) {
            System.err.println("[DEBUG] saveDoctor: validation failed");
            FacesContext.getCurrentInstance().validationFailed(); // Mark validation failed so dialog stays open
            return;
        }
        try {
            if (selectedDoctor.getDoctorId() == null) {
                System.err.println("[DEBUG] saveDoctor: creating new doctor");
                doctorService.createDoctor(toDoctorEntity(selectedDoctor));
                addSuccessMessage("Doctor created successfully.");
            } else {
                System.err.println("[DEBUG] saveDoctor: updating existing doctor, id = " + selectedDoctor.getDoctorId());
                doctorService.updateDoctor(toDoctorEntity(selectedDoctor));
                addSuccessMessage("Doctor updated successfully.");
            }
            loadAllDoctors();
            loadDeletedDoctors();
            selectedDoctor = null;
        } catch (Exception e) {
            System.err.println("[ERROR] saveDoctor: " + e.getMessage());
            addErrorMessage("Failed to save doctor: " + e.getMessage());
            FacesContext.getCurrentInstance().validationFailed(); // Mark validation failed on error
        }
        System.err.println("[DEBUG] AdminDashboardController.saveDoctor: END");
    }

    public void deleteDoctor() {
        if (selectedDoctor != null && selectedDoctor.getDoctorId() != null) {
            try {
                doctorService.deleteDoctor(selectedDoctor.getDoctorId());
                addSuccessMessage("Doctor deleted (soft) successfully.");
                loadAllDoctors();
                loadDeletedDoctors();
                selectedDoctor = null;
            } catch (Exception e) {
                addErrorMessage("Failed to delete doctor: " + e.getMessage());
            }
        } else {
            addErrorMessage("No doctor selected for deletion.");
        }
    }

    public void restoreDoctor(DoctorTableDTO dto) {
        doctorService.restoreDoctor(dto.getDoctorId());
        loadAllDoctors();
        loadDeletedDoctors();
        addSuccessMessage("Doctor restored successfully.");
        selectedDoctor = null;
    }
    
    // --- Helper methods for refreshing lists after inline editing ---
    private void loadAllDepartments() {
        allDepartments = departmentService.getAllDepartments().stream()
            .map(this::toDepartmentTableDTO)
            .collect(Collectors.toList());
        System.err.println("[DEBUG] loadAllDepartments: loaded " + allDepartments.size() + " departments");
        for (DepartmentTableDTO dept : allDepartments) {
            System.err.println("[DEBUG] Department: id=" + dept.getDepartmentId() + ", name=" + dept.getDepartmentName());
        }
    }
    private void loadAllPatients() {
        allPatients = patientService.getAllPatients().stream()
            .map(this::toPatientTableDTO)
            .collect(Collectors.toList());
    }
    private void loadAllDoctors() {
        allDoctors = doctorService.getAllDoctorsWithDepartments().stream()
            .map(this::toDoctorTableDTO)
            .collect(Collectors.toList());
        allDoctors.forEach(d -> System.err.println("[DEBUG] loadAllDoctors: doctorId=" + d.getDoctorId() + ", status=" + d.getStatus()));
    }
    private void loadAllStaff() {
        allStaff = staffService.getAllStaff().stream()
            .map(this::toStaffTableDTO)
            .collect(Collectors.toList());
    }
    private void loadAllUsers() {
        allUsers = userService.getAllUsers().stream()
            .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
            .map(this::toUserTableDTO)
            .collect(Collectors.toList());
        allUsers.forEach(u -> System.err.println("[DEBUG] Loaded user DTO: username=" + u.getUsername() + ", id=" + u.getId()));
    }
    private void loadDeletedUsers() {
        deletedUsers = userService.getDeletedUsers().stream()
            .map(this::toUserTableDTO)
            .collect(Collectors.toList());
    }
    
    @PostConstruct
    public void init() {
        System.err.println("Initializing admin dashboard controller");
        loadDashboardData();
        loadAllUsers();
        loadAllPatients();
        loadAllStaff();
        loadAllDoctors();
        loadAllDepartments();
        loadDeletedUsers();
        loadDeletedDepartments();
        loadDeletedStaff();
        loadDeletedPatients();
        loadDeletedDoctors();
      
    }
    
    private void loadDashboardData() {
        try {
            loadSystemStatistics();
            loadFinancialStatistics();
            loadAppointmentStatistics();
            loadUserStatistics();
            loadDepartmentStatistics();
            loadRecentActivities();
            loadSoftDeletedData();
            loadSoftDeleteStatistics();
            loadChartData();
            loadAlerts();
            loadPerformanceMetrics();
            loadAllUsers();
            loadDeletedUsers();
            System.err.println("Admin dashboard data loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard data");
            e.printStackTrace(System.err);
        }
    }
    
    private void loadSystemStatistics() {
        systemStats.clear();
        systemStats.put("totalPatients", patientService.getTotalPatientCount());
        systemStats.put("totalDoctors", (long) doctorService.getAllDoctors().size());
        systemStats.put("totalStaff", (long) staffService.getAllStaff().size());
        systemStats.put("totalDepartments", (long) departmentService.getAllDepartments().size());
        systemStats.put("totalAppointments", appointmentService.getAppointmentCount());
        systemStats.put("totalBillings", (long) billingService.getAllBillings().size());
        systemStats.put("activePatients", patientService.getActivePatientCount());
        systemStats.put("newPatientsThisMonth", patientService.getNewPatientsThisMonth());
    }
    
    private void loadFinancialStatistics() {
        financialStats.clear();
        financialStats.put("totalRevenue", billingService.getTotalRevenue());
        financialStats.put("pendingAmount", calculatePendingAmount());
        financialStats.put("monthlyRevenue", calculateMonthlyRevenue());
        financialStats.put("overdueAmount", calculateOverdueAmount());
        financialStats.put("thisMonthRevenue", calculateThisMonthRevenue());
        financialStats.put("lastMonthRevenue", calculateLastMonthRevenue());
    }
    
    private void loadAppointmentStatistics() {
        appointmentStats.clear();
        appointmentStats.put("todaysAppointments", (long) appointmentService.getTodaysAppointmentCount());
        appointmentStats.put("weeklyAppointments", appointmentService.getWeeklyAppointmentCount());
        appointmentStats.put("monthlyAppointments", appointmentService.getMonthlyAppointmentCount());
        appointmentStats.put("completedAppointments", appointmentService.getCompletedAppointmentCount());
        appointmentStats.put("cancelledAppointments", appointmentService.getCancelledAppointmentCount());
        appointmentStats.put("pendingAppointments", appointmentService.getPendingAppointmentCount());
        appointmentStats.put("noShowAppointments", calculateNoShowAppointments());
    }
    
    private void loadUserStatistics() {
        userStats.clear();
        userStats.put("activePatients", patientService.getActivePatientCount());
        userStats.put("activeDoctors", calculateActiveDoctors());
        userStats.put("activeStaff", (long) staffService.getActiveStaff().size());
        userStats.put("newPatientsThisMonth", patientService.getNewPatientsThisMonth());
        userStats.put("newDoctorsThisMonth", calculateNewDoctorsThisMonth());
        userStats.put("pendingVerifications", calculatePendingVerifications());
    }
    
    private void loadDepartmentStatistics() {
        departmentStats.clear();
        List<Department> departments = departmentService.getAllDepartments();
        for (Department dept : departments) {
            long appointmentCount = calculateAppointmentsByDepartment(dept.getId());
            departmentStats.put(dept.getDepartmentName(), appointmentCount);
        }
    }
    
    private void loadRecentActivities() {
        try {
            recentPatients = patientService.getActivePatients().stream().limit(10).collect(Collectors.toList());
            todaysAppointments = appointmentService.findTodaysAppointments();
            pendingBills = billingService.getPendingBillings();
            pendingVerifications = staffService.getAllStaff().stream()
                .filter(staff -> "PENDING_VERIFICATION".equals(staff.getStatus()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error loading recent activities");
            e.printStackTrace(System.err);
            recentPatients = new ArrayList<>();
            todaysAppointments = new ArrayList<>();
            pendingBills = new ArrayList<>();
            pendingVerifications = new ArrayList<>();
        }
    }
    
    private void loadSoftDeletedData() {
        try {
            System.err.println("Loading soft deleted data for admin dashboard");
            
            // Load soft deleted data from all services
            deletedPatients = patientService.getDeletedPatients().stream()
                .map(this::toPatientTableDTO)
                .collect(Collectors.toList());
            deletedDoctors = doctorService.getDeletedDoctorsWithDepartments().stream()
                .map(this::toDoctorTableDTO)
                .collect(Collectors.toList());
            deletedStaff = staffService.getDeletedStaff().stream()
                .map(this::toStaffTableDTO)
                .collect(Collectors.toList());
            deletedDepartments = departmentService.getDeletedDepartments().stream()
                .map(this::toDepartmentTableDTO)
                .collect(Collectors.toList());
            deletedAppointments = appointmentService.findDeletedAppointments();
            deletedBillings = billingService.getDeletedBillings();
            deletedMedicalRecords = medicalRecordService.findDeletedRecords();

            
            System.err.println("Soft deleted data loaded successfully - Patients: " + deletedPatients.size() + ", Doctors: " + deletedDoctors.size() + ", Staff: " + deletedStaff.size() + ", Departments: " + deletedDepartments.size() + ", Appointments: " + deletedAppointments.size() + ", Billings: " + deletedBillings.size() + ", Records: " + deletedMedicalRecords.size() + ", Prescriptions: " + deletedPrescriptions.size());
                
        } catch (Exception e) {
            System.err.println("Error loading soft deleted data");
            e.printStackTrace(System.err);
            // Initialize empty lists on error
            deletedPatients = new ArrayList<>();
            deletedDoctors = new ArrayList<>();
            deletedStaff = new ArrayList<>();
            deletedDepartments = new ArrayList<>();
            deletedAppointments = new ArrayList<>();
            deletedBillings = new ArrayList<>();
            deletedMedicalRecords = new ArrayList<>();
            deletedPrescriptions = new ArrayList<>();
        }
    }
    
    private void loadSoftDeleteStatistics() {
        try {
            System.err.println("Loading soft delete statistics for admin dashboard");
            
            softDeleteStats.clear();
            softDeleteStats.put("deletedPatients", (long) deletedPatients.size());
            softDeleteStats.put("deletedDoctors", (long) deletedDoctors.size());
            softDeleteStats.put("deletedStaff", (long) deletedStaff.size());
            softDeleteStats.put("deletedDepartments", (long) deletedDepartments.size());
            softDeleteStats.put("deletedAppointments", (long) deletedAppointments.size());
            softDeleteStats.put("deletedBillings", (long) deletedBillings.size());
            softDeleteStats.put("deletedMedicalRecords", (long) deletedMedicalRecords.size());
            softDeleteStats.put("deletedPrescriptions", (long) deletedPrescriptions.size());
            
            // Calculate total deleted items
            long totalDeleted = softDeleteStats.values().stream().mapToLong(Long::longValue).sum();
            softDeleteStats.put("totalDeleted", totalDeleted);
            
            System.err.println("Soft delete statistics loaded successfully - Total deleted items: " + totalDeleted);
            
        } catch (Exception e) {
            System.err.println("Error loading soft delete statistics");
            e.printStackTrace(System.err);
            softDeleteStats.clear();
        }
    }
    
    private void loadChartData() {
        loadRevenueChartData();
        loadAppointmentChartData();
        loadDepartmentChartData();
        loadPatientGrowthChartData();
    }
    
    private void loadRevenueChartData() {
        revenueChartData.clear();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        
        // Generate last 6 months data
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            data.add(calculateRevenueForMonth(date));
        }
        
        revenueChartData.put("labels", labels);
        revenueChartData.put("data", data);
    }
    
    private void loadAppointmentChartData() {
        appointmentChartData.clear();
        List<String> labels = Arrays.asList("Scheduled", "Confirmed", "Completed", "Cancelled", "No Show");
        List<Long> data = Arrays.asList(
            appointmentStats.get("pendingAppointments"),
            appointmentStats.get("todaysAppointments"),
            appointmentStats.get("completedAppointments"),
            appointmentStats.get("cancelledAppointments"),
            appointmentStats.get("noShowAppointments")
        );
        
        appointmentChartData.put("labels", labels);
        appointmentChartData.put("data", data);
    }
    
    private void loadDepartmentChartData() {
        departmentChartData.clear();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : departmentStats.entrySet()) {
            labels.add(entry.getKey());
            data.add(entry.getValue());
        }
        
        departmentChartData.put("labels", labels);
        departmentChartData.put("data", data);
    }
    
    private void loadPatientGrowthChartData() {
        patientGrowthChartData.clear();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        // Generate last 12 months patient growth
        for (int i = 11; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            data.add(calculatePatientGrowthForMonth(date));
        }
        
        patientGrowthChartData.put("labels", labels);
        patientGrowthChartData.put("data", data);
    }
    
    private void loadAlerts() {
        systemAlerts = new ArrayList<>();
        financialAlerts = new ArrayList<>();
        operationalAlerts = new ArrayList<>();
        
        // System Alerts
        if (systemUptime < 99.0) {
            systemAlerts.add(new DashboardAlert("System Uptime", "System uptime is below 99%", "warning", "high"));
        }
        
        // Financial Alerts
        if (financialStats.get("overdueAmount").compareTo(BigDecimal.valueOf(10000)) > 0) {
            financialAlerts.add(new DashboardAlert("Overdue Payments", 
                "Overdue amount exceeds $10,000", "danger", "high"));
        }
        
        if (financialStats.get("pendingAmount").compareTo(BigDecimal.valueOf(50000)) > 0) {
            financialAlerts.add(new DashboardAlert("High Pending Amount", 
                "Pending amount is high", "warning", "medium"));
        }
        
        // Operational Alerts
        if (pendingBills.size() > 20) {
            operationalAlerts.add(new DashboardAlert("Pending Bills", 
                pendingBills.size() + " bills pending processing", "warning", "medium"));
        }
        
        if (pendingVerifications.size() > 5) {
            operationalAlerts.add(new DashboardAlert("Staff Verifications", 
                pendingVerifications.size() + " staff members pending verification", "info", "low"));
        }
    }
    
    private void loadPerformanceMetrics() {
        systemUptime = 99.8; // Mock data - in real app, get from monitoring system
        averageResponseTime = 245.5; // milliseconds
        activeUsers = calculateActiveUsers();
        criticalAlerts = systemAlerts.stream()
            .filter(alert -> "high".equals(alert.getPriority()))
            .mapToInt(alert -> 1)
            .sum();
    }
    
    // Helper methods for calculations
    private BigDecimal calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        
        return billingService.getBillingsByDateRange(startOfMonth, endOfMonth)
            .stream()
            .filter(billing -> "PAID".equals(billing.getStatus()))
            .map(Billing::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateThisMonthRevenue() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        return calculateRevenueForDateRange(startOfMonth, endOfMonth);
    }
    
    private BigDecimal calculateLastMonthRevenue() {
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(
            LocalDate.now().minusMonths(1).lengthOfMonth());
        return calculateRevenueForDateRange(startOfLastMonth, endOfLastMonth);
    }
    
    private BigDecimal calculateRevenueForDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return billingService.getBillingsByDateRange(startDateTime, endDateTime)
            .stream()
            .filter(billing -> "PAID".equals(billing.getStatus()))
            .map(Billing::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateRevenueForMonth(LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        return calculateRevenueForDateRange(startOfMonth, endOfMonth);
    }
    
    private long calculatePatientGrowthForMonth(LocalDate month) {
        // Mock calculation - in real app, calculate actual patient growth
        return new Random().nextInt(50) + 10;
    }
    
    private int calculateActiveUsers() {
        // Mock calculation - in real app, get from session management
        return new Random().nextInt(50) + 20;
    }
    
    public void refreshDashboard() {
        System.err.println("Refreshing admin dashboard");
        loadDashboardData();
    }
    
    public void setRevenueChartPeriod(String period) {
        this.revenueChartPeriod = period;
        loadRevenueChartData();
    }
    
    public void setAppointmentChartPeriod(String appointmentChartPeriod) {
        this.appointmentChartPeriod = appointmentChartPeriod;
        // Optionally reload chart data here if needed
        // loadAppointmentChartData();
    }
    
    // Getters
    public Map<String, Long> getSystemStats() { return systemStats; }
    public Map<String, BigDecimal> getFinancialStats() { return financialStats; }
    public Map<String, Long> getAppointmentStats() { return appointmentStats; }
    public Map<String, Long> getUserStats() { return userStats; }
    public Map<String, Long> getDepartmentStats() { return departmentStats; }
    
    public List<Patient> getRecentPatients() { return recentPatients != null ? recentPatients : java.util.Collections.emptyList(); }
    public List<Appointment> getTodaysAppointments() { return todaysAppointments != null ? todaysAppointments : java.util.Collections.emptyList(); }
    public List<Billing> getPendingBills() { return pendingBills != null ? pendingBills : java.util.Collections.emptyList(); }
    public List<Staff> getPendingVerifications() { return pendingVerifications != null ? pendingVerifications : java.util.Collections.emptyList(); }
    
    public Map<String, Object> getRevenueChartData() { return revenueChartData; }
    public Map<String, Object> getAppointmentChartData() { return appointmentChartData; }
    public Map<String, Object> getDepartmentChartData() { return departmentChartData; }
    public Map<String, Object> getPatientGrowthChartData() { return patientGrowthChartData; }
    
    public List<DashboardAlert> getSystemAlerts() { return systemAlerts; }
    public List<DashboardAlert> getFinancialAlerts() { return financialAlerts; }
    public List<DashboardAlert> getOperationalAlerts() { return operationalAlerts; }
    
    public double getSystemUptime() { return systemUptime; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public int getActiveUsers() { return activeUsers; }
    public int getCriticalAlerts() { return criticalAlerts; }
    
    public String getRevenueChartPeriod() { return revenueChartPeriod; }
    public String getAppointmentChartPeriod() {
        return appointmentChartPeriod;
    }
    
    // Soft Deleted Data Getters
    public List<PatientTableDTO> getDeletedPatients() { return deletedPatients != null ? deletedPatients : java.util.Collections.emptyList(); }
    public List<DoctorTableDTO> getDeletedDoctors() { return deletedDoctors; }
    public List<StaffTableDTO> getDeletedStaff() { return deletedStaff != null ? deletedStaff : java.util.Collections.emptyList(); }
    public List<DepartmentTableDTO> getDeletedDepartments() { return deletedDepartments != null ? deletedDepartments : java.util.Collections.emptyList(); }
    public List<Appointment> getDeletedAppointments() { return deletedAppointments != null ? deletedAppointments : java.util.Collections.emptyList(); }
    public List<Billing> getDeletedBillings() { return deletedBillings != null ? deletedBillings : java.util.Collections.emptyList(); }
    public List<MedicalRecord> getDeletedMedicalRecords() { return deletedMedicalRecords != null ? deletedMedicalRecords : java.util.Collections.emptyList(); }
    public List<Prescription> getDeletedPrescriptions() { return deletedPrescriptions != null ? deletedPrescriptions : java.util.Collections.emptyList(); }
    public Map<String, Long> getSoftDeleteStats() { return softDeleteStats; }
    
    // Soft Delete Management Actions
    public void restorePatient(Patient patient) {
        try {
            patientService.restore(patient.getId());
            addSuccessMessage("Patient restored successfully.");
            deletedPatients = null;
        } catch (Exception e) {
            addErrorMessage("Failed to restore patient: " + e.getMessage());
        }
    }
    
    public void restoreStaff(Staff staff) {
        try {
            staffService.restoreStaff(staff.getId());
            addSuccessMessage("Staff restored successfully.");
            deletedStaff = null;
        } catch (Exception e) {
            addErrorMessage("Failed to restore staff: " + e.getMessage());
        }
    }
    
    public void restoreDepartment(Department dept) {
        try {
            departmentService.restoreDepartment(dept.getId());
            addSuccessMessage("Department restored successfully.");
            deletedDepartments = null;
        } catch (Exception e) {
            addErrorMessage("Failed to restore department: " + e.getMessage());
        }
    }
    
    public void permanentlyDeletePatient(Long patientId) {
        try {
            patientService.permanentlyDelete(patientId);
            addSuccessMessage("Patient permanently deleted");
            loadSoftDeletedData();
            loadSoftDeleteStatistics();
        } catch (Exception e) {
            System.err.println("Error permanently deleting patient");
            e.printStackTrace(System.err);
            addErrorMessage("Error permanently deleting patient: " + e.getMessage());
        }
    }
    
    public void permanentlyDeleteStaff(Long staffId) {
        try {
            staffService.permanentlyDeleteStaff(staffId);
            addSuccessMessage("Staff permanently deleted");
            loadSoftDeletedData();
            loadSoftDeleteStatistics();
        } catch (Exception e) {
            System.err.println("Error permanently deleting staff");
            e.printStackTrace(System.err);
            addErrorMessage("Error permanently deleting staff: " + e.getMessage());
        }
    }
    
    public void permanentlyDeleteDepartment(Long departmentId) {
        try {
            departmentService.permanentlyDeleteDepartment(departmentId);
            addSuccessMessage("Department permanently deleted");
            loadSoftDeletedData();
            loadSoftDeleteStatistics();
        } catch (Exception e) {
            System.err.println("Error permanently deleting department");
            e.printStackTrace(System.err);
            addErrorMessage("Error permanently deleting department: " + e.getMessage());
        }
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Utility methods for dashboard
    public String getGrowthPercentage(String metric) {
        switch (metric) {
            case "patients":
                return "+" + (new Random().nextInt(20) + 5) + "%";
            case "revenue":
                return "+" + (new Random().nextInt(15) + 8) + "%";
            case "appointments":
                return "+" + (new Random().nextInt(25) + 10) + "%";
            default:
                return "+12.5%";
        }
    }
    
    public String getGrowthTrend(String metric) {
        return "positive"; // Mock - in real app, calculate actual trend
    }
    
    public String getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "active":
            case "completed":
            case "paid":
                return "success";
            case "pending":
            case "scheduled":
                return "warning";
            case "cancelled":
            case "overdue":
                return "danger";
            default:
                return "info";
        }
    }
    
    // Helper calculation methods
    private BigDecimal calculatePendingAmount() {
        try {
            return billingService.getPendingBillings().stream()
                .map(Billing::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("Error calculating pending amount");
            e.printStackTrace(System.err);
            return BigDecimal.ZERO;
        }
    }
    
    private BigDecimal calculateOverdueAmount() {
        try {
            return billingService.getOverdueBillings().stream()
                .map(Billing::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("Error calculating overdue amount");
            e.printStackTrace(System.err);
            return BigDecimal.ZERO;
        }
    }
    
    private long calculateNoShowAppointments() {
        try {
            return appointmentService.getAllAppointments().stream()
                .filter(apt -> "NO_SHOW".equals(apt.getStatus()))
                .count();
        } catch (Exception e) {
            System.err.println("Error calculating no-show appointments");
            e.printStackTrace(System.err);
            return 0;
        }
    }
    
    private long calculateActiveDoctors() {
        try {
            return doctorService.getAllDoctors().stream()
                .filter(doctor -> doctor.isActive())
                .count();
        } catch (Exception e) {
            System.err.println("Error calculating active doctors");
            e.printStackTrace(System.err);
            return 0;
        }
    }
    
    private long calculateNewDoctorsThisMonth() {
        try {
            // Mock calculation - in real app, filter by creation date
            return doctorService.getAllDoctors().size() / 12; // Rough estimate
        } catch (Exception e) {
            System.err.println("Error calculating new doctors this month");
            e.printStackTrace(System.err);
            return 0;
        }
    }
    
    private long calculatePendingVerifications() {
        try {
            return staffService.getAllStaff().stream()
                .filter(staff -> "PENDING_VERIFICATION".equals(staff.getStatus()))
                .count();
        } catch (Exception e) {
            System.err.println("Error calculating pending verifications");
            e.printStackTrace(System.err);
            return 0;
        }
    }
    
    private long calculateAppointmentsByDepartment(Long departmentId) {
        try {
            return appointmentService.getAllAppointments().stream()
                .filter(apt -> apt.getDoctor() != null && 
                              apt.getDoctor().getDepartment() != null &&
                              departmentId.equals(apt.getDoctor().getDepartment().getId()))
                .count();
        } catch (Exception e) {
            System.err.println("Error calculating appointments by department");
            e.printStackTrace(System.err);
            return 0;
        }
    }
    
    // Inner class for alerts
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

    // --- Safe Statistic Getters ---

    // System Stats
    public long getSystemTotalPatients() { return systemStats.getOrDefault("totalPatients", 0L); }
    public long getSystemTotalDoctors() { return systemStats.getOrDefault("totalDoctors", 0L); }
    public long getSystemTotalStaff() { return systemStats.getOrDefault("totalStaff", 0L); }
    public long getSystemTotalDepartments() { return systemStats.getOrDefault("totalDepartments", 0L); }
    public long getSystemTotalAppointments() { return systemStats.getOrDefault("totalAppointments", 0L); }
    public long getSystemTotalBillings() { return systemStats.getOrDefault("totalBillings", 0L); }
    public long getSystemActivePatients() { return systemStats.getOrDefault("activePatients", 0L); }
    public long getSystemNewPatientsThisMonth() { return systemStats.getOrDefault("newPatientsThisMonth", 0L); }

    // Financial Stats
    public BigDecimal getFinancialTotalRevenue() { return financialStats.getOrDefault("totalRevenue", BigDecimal.ZERO); }
    public BigDecimal getFinancialPendingAmount() { return financialStats.getOrDefault("pendingAmount", BigDecimal.ZERO); }
    public BigDecimal getFinancialMonthlyRevenue() { return financialStats.getOrDefault("monthlyRevenue", BigDecimal.ZERO); }
    public BigDecimal getFinancialOverdueAmount() { return financialStats.getOrDefault("overdueAmount", BigDecimal.ZERO); }
    public BigDecimal getFinancialThisMonthRevenue() { return financialStats.getOrDefault("thisMonthRevenue", BigDecimal.ZERO); }
    public BigDecimal getFinancialLastMonthRevenue() { return financialStats.getOrDefault("lastMonthRevenue", BigDecimal.ZERO); }

    // Appointment Stats
    public long getAppointmentTodays() { return appointmentStats.getOrDefault("todaysAppointments", 0L); }
    public long getAppointmentWeekly() { return appointmentStats.getOrDefault("weeklyAppointments", 0L); }
    public long getAppointmentMonthly() { return appointmentStats.getOrDefault("monthlyAppointments", 0L); }
    public long getAppointmentCompleted() { return appointmentStats.getOrDefault("completedAppointments", 0L); }
    public long getAppointmentCancelled() { return appointmentStats.getOrDefault("cancelledAppointments", 0L); }
    public long getAppointmentPending() { return appointmentStats.getOrDefault("pendingAppointments", 0L); }
    public long getAppointmentNoShow() { return appointmentStats.getOrDefault("noShowAppointments", 0L); }

    // User Stats
    public long getUserActivePatients() { return userStats.getOrDefault("activePatients", 0L); }
    public long getUserActiveDoctors() { return userStats.getOrDefault("activeDoctors", 0L); }
    public long getUserActiveStaff() { return userStats.getOrDefault("activeStaff", 0L); }
    public long getUserNewPatientsThisMonth() { return userStats.getOrDefault("newPatientsThisMonth", 0L); }
    public long getUserNewDoctorsThisMonth() { return userStats.getOrDefault("newDoctorsThisMonth", 0L); }
    public long getUserPendingVerifications() { return userStats.getOrDefault("pendingVerifications", 0L); }

    // Soft Delete Stats
    public long getDeletedPatientsCount() { return softDeleteStats.getOrDefault("deletedPatients", 0L); }
    public long getDeletedDoctorsCount() { return softDeleteStats.getOrDefault("deletedDoctors", 0L); }
    public long getDeletedStaffCount() { return softDeleteStats.getOrDefault("deletedStaff", 0L); }
    public long getDeletedDepartmentsCount() { return softDeleteStats.getOrDefault("deletedDepartments", 0L); }
    public long getDeletedAppointmentsCount() { return softDeleteStats.getOrDefault("deletedAppointments", 0L); }
    public long getDeletedBillingsCount() { return softDeleteStats.getOrDefault("deletedBillings", 0L); }
    public long getDeletedMedicalRecordsCount() { return softDeleteStats.getOrDefault("deletedMedicalRecords", 0L); }
    public long getDeletedPrescriptionsCount() { return softDeleteStats.getOrDefault("deletedPrescriptions", 0L); }
    public long getTotalDeletedItemsCount() { return softDeleteStats.getOrDefault("totalDeleted", 0L); }

    public com.hms2.enums.DoctorStatus[] getAllDoctorStatuses() {
        return com.hms2.enums.DoctorStatus.values();
    }

    public com.hms2.enums.UserRole[] getUserRoles() {
        return com.hms2.enums.UserRole.values();
    }

    public void triggerDatabaseInit() {
        try {
            databaseInitializer.initializeDatabase();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Database initialized!", "Sample data has been reloaded."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Initialization failed", e.getMessage()));
        }
    }

    public void addSampleDepartments() {
        try {
            List<Department> departments = Arrays.asList(
                createDepartment("Cardiology", "Heart and vascular care", "Building A, Floor 2", "1234567890", "cardio@hospital.com"),
                createDepartment("Neurology", "Brain and nervous system", "Building B, Floor 3", "1234567891", "neuro@hospital.com"),
                createDepartment("Orthopedics", "Bones and joints", "Building C, Floor 1", "1234567892", "ortho@hospital.com"),
                createDepartment("Pediatrics", "Child healthcare", "Building D, Floor 2", "1234567893", "pediatrics@hospital.com"),
                createDepartment("Oncology", "Cancer treatment", "Building E, Floor 4", "1234567894", "oncology@hospital.com"),
                createDepartment("Radiology", "Imaging and diagnostics", "Building F, Floor 1", "1234567895", "radiology@hospital.com"),
                createDepartment("Emergency", "Emergency care", "Building G, Ground Floor", "1234567896", "emergency@hospital.com"),
                createDepartment("Dermatology", "Skin care", "Building H, Floor 2", "1234567897", "derma@hospital.com"),
                createDepartment("Gastroenterology", "Digestive system", "Building I, Floor 3", "1234567898", "gastro@hospital.com"),
                createDepartment("Urology", "Urinary tract and male health", "Building J, Floor 2", "1234567899", "urology@hospital.com")
            );
            for (Department dept : departments) {
                departmentService.createDepartment(dept);
            }
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Departments added!", "Sample departments have been inserted."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add departments", e.getMessage()));
        }
    }

    private Department createDepartment(String name, String description, String location, String phone, String email) {
        Department department = new Department();
        department.setDepartmentName(name);
        department.setDescription(description);
        department.setLocation(location);
        department.setPhoneNumber(phone);
        department.setEmail(email);
        department.setStatus("ACTIVE");
        department.setCreatedBy("SYSTEM");
        return department;
    }

    public List<Department> getActiveDepartments() {
        return activeDepartments != null ? activeDepartments : java.util.Collections.emptyList();
    }

    public DepartmentTableDTO getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(DepartmentTableDTO newDepartment) {
        this.newDepartment = newDepartment;
    }

    public String createDepartment() {
        try {
            // Trim all string values
            if (newDepartment.getDepartmentName() != null) {
                newDepartment.setDepartmentName(newDepartment.getDepartmentName().trim());
            }
            if (newDepartment.getDescription() != null) {
                newDepartment.setDescription(newDepartment.getDescription().trim());
            }
            if (newDepartment.getLocation() != null) {
                newDepartment.setLocation(newDepartment.getLocation().trim());
            }
            if (newDepartment.getPhoneNumber() != null) {
                newDepartment.setPhoneNumber(newDepartment.getPhoneNumber().trim());
            }
            if (newDepartment.getEmail() != null) {
                newDepartment.setEmail(newDepartment.getEmail().trim());
            }
            
            // Debug: Log phone number validation
            System.out.println("Creating department - Phone number being validated: '" + newDepartment.getPhoneNumber() + "'");
            System.out.println("Phone number length: " + (newDepartment.getPhoneNumber() != null ? newDepartment.getPhoneNumber().length() : "null"));
            System.out.println("Phone number matches pattern: " + (newDepartment.getPhoneNumber() != null ? newDepartment.getPhoneNumber().matches("^[+]?[0-9]{10,15}$") : "null"));
            
            // Bean Validation
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<DepartmentTableDTO>> violations = validator.validate(newDepartment);
            
            if (!violations.isEmpty()) {
                for (ConstraintViolation<DepartmentTableDTO> violation : violations) {
                    System.out.println("Validation violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                    addErrorMessage(violation.getMessage());
                }
                return null;
            }
            
            // Check if department name already exists
            if (!departmentService.isDepartmentNameUnique(newDepartment.getDepartmentName())) {
                addErrorMessage("Department name '" + newDepartment.getDepartmentName() + "' already exists. Please choose a different name.");
                return null;
            }
            
            // Set default values
            newDepartment.setStatus("ACTIVE");
            newDepartment.setIsDeleted(false);
            
            // Create the department
            Department department = toDepartmentEntity(newDepartment);
            department.setCreatedBy("ADMIN"); // Set the creator
            departmentService.createDepartment(department);
            
            // Success feedback
            addSuccessMessage("Department '" + newDepartment.getDepartmentName() + "' created successfully!");
            
            // Reset form and refresh data
            newDepartment = new DepartmentTableDTO();
            loadAllDepartments();
            loadDeletedDepartments();
            
            return "/views/dashboard/sections/department-management.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("Exception in createDepartment: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Error creating department: " + e.getMessage());
            return null;
        }
    }
    
    public void refreshDepartmentData() {
        loadAllDepartments();
        loadDeletedDepartments();
    }
    
    public boolean validateDepartmentForm() {
        try {
            // Test phone number validation
            String testPhone = "256756896124";
            System.out.println("Testing phone number: " + testPhone);
            System.out.println("Length: " + testPhone.length());
            System.out.println("Matches pattern: " + testPhone.matches("^[+]?[0-9]{10,15}$"));
            
            // Validate the new department
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<DepartmentTableDTO>> violations = validator.validate(newDepartment);
            
            if (!violations.isEmpty()) {
                for (ConstraintViolation<DepartmentTableDTO> violation : violations) {
                    addErrorMessage("Validation error: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
                return false;
            }
            
            addSuccessMessage("Form validation passed successfully!");
            return true;
        } catch (Exception e) {
            addErrorMessage("Validation error: " + e.getMessage());
            return false;
        }
    }

    // --- Department CRUD for Tab Layout ---
    public void onRowEditDepartment(org.primefaces.event.RowEditEvent<DepartmentTableDTO> event) {
        DepartmentTableDTO dto = event.getObject();
        try {
            // Trim all string values
            if (dto.getDepartmentName() != null) {
                dto.setDepartmentName(dto.getDepartmentName().trim());
            }
            if (dto.getDescription() != null) {
                dto.setDescription(dto.getDescription().trim());
            }
            if (dto.getLocation() != null) {
                dto.setLocation(dto.getLocation().trim());
            }
            if (dto.getPhoneNumber() != null) {
                dto.setPhoneNumber(dto.getPhoneNumber().trim());
            }
            if (dto.getEmail() != null) {
                dto.setEmail(dto.getEmail().trim());
            }
            // Bean Validation
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<DepartmentTableDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<DepartmentTableDTO> violation : violations) {
                    addErrorMessage("Update failed: " + violation.getMessage());
                }
                return;
            }
            // Check if department name already exists (excluding current department)
            Department existingDept = departmentService.getDepartmentById(dto.getDepartmentId()).orElse(null);
            if (existingDept != null && !existingDept.getDepartmentName().equals(dto.getDepartmentName())) {
                if (!departmentService.isDepartmentNameUnique(dto.getDepartmentName())) {
                    addErrorMessage("Department name '" + dto.getDepartmentName() + "' already exists. Please choose a different name.");
                    return;
                }
            }
            departmentService.updateDepartment(toDepartmentEntity(dto));
            addSuccessMessage("Department '" + dto.getDepartmentName() + "' updated successfully.");
            loadAllDepartments();
            selectedDepartment = null;
        } catch (Exception e) {
            addErrorMessage("Failed to update department: " + e.getMessage());
        }
    }

    public void onRowEditCancelDepartment(org.primefaces.event.RowEditEvent<DepartmentTableDTO> event) {
        addSuccessMessage("Edit cancelled for department: " + event.getObject().getDepartmentName());
    }

    public void deleteDepartmentById(Long departmentId) {
        try {
            departmentService.deleteDepartment(departmentId);
            addSuccessMessage("Department deleted (soft) successfully.");
            loadAllDepartments();
            loadDeletedDepartments();
        } catch (Exception e) {
            addErrorMessage("Failed to delete department: " + e.getMessage());
        }
    }

    public void restoreDepartment(DepartmentTableDTO dto) {
        try {
            departmentService.restoreDepartment(dto.getDepartmentId());
            addSuccessMessage("Department restored successfully.");
            loadAllDepartments();
            loadDeletedDepartments();
        } catch (Exception e) {
            addErrorMessage("Failed to restore department: " + e.getMessage());
        }
    }

    // --- CRUD for Patient, Doctor, Staff for Tab Layouts ---
    public void onRowEditPatient(org.primefaces.event.RowEditEvent<PatientTableDTO> event) {
        PatientTableDTO dto = event.getObject();
        try {
            patientService.updatePatient(toPatientEntity(dto));
            addSuccessMessage("Patient updated successfully.");
            loadAllPatients();
            selectedPatient = null;
        } catch (Exception e) {
            addErrorMessage("Failed to update patient: " + e.getMessage());
        }
    }
    public void deletePatient(PatientTableDTO dto) {
        try {
            patientService.deletePatient(dto.getPatientId());
            addSuccessMessage("Patient deleted (soft) successfully.");
            loadAllPatients();
            loadDeletedPatients();
        } catch (Exception e) {
            addErrorMessage("Failed to delete patient: " + e.getMessage());
        }
    }
    public void onRowEditDoctor(org.primefaces.event.RowEditEvent<DoctorTableDTO> event) {
        DoctorTableDTO dto = event.getObject();
        try {
            doctorService.updateDoctor(toDoctorEntity(dto));
            addSuccessMessage("Doctor updated successfully.");
            loadAllDoctors();
            loadDeletedDoctors();
            selectedDoctor = null;
        } catch (Exception e) {
            addErrorMessage("Failed to update doctor: " + e.getMessage());
        }
    }

    public void onRowEditStaff(org.primefaces.event.RowEditEvent<StaffTableDTO> event) {
        StaffTableDTO dto = event.getObject();
        try {
            staffService.updateStaff(toStaffEntity(dto));
            addSuccessMessage("Staff updated successfully.");
            loadAllStaff();
            selectedStaff = null;
        } catch (Exception e) {
            addErrorMessage("Failed to update staff: " + e.getMessage());
        }
    }
    public void deleteStaff(StaffTableDTO dto) {
        try {
            staffService.deleteStaff(dto.getStaffId());
            addSuccessMessage("Staff deleted (soft) successfully.");
            loadAllStaff();
            loadDeletedStaff();
        } catch (Exception e) {
            addErrorMessage("Failed to delete staff: " + e.getMessage());
        }
    }

    public StaffService getStaffService() {
        return staffService;
    }

    // --- Logout functionality ---
    public String logout() {
        System.err.println("Admin logging out");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "You have been logged out successfully"));
        return "/index.xhtml?faces-redirect=true";
    }

    // --- Entity to DTO mapping helpers ---
    private DepartmentTableDTO toDepartmentTableDTO(Department dept) {
        DepartmentTableDTO dto = new DepartmentTableDTO();
        dto.setDepartmentId(dept.getId());
        dto.setDepartmentName(dept.getDepartmentName());
        dto.setDescription(dept.getDescription());
        dto.setLocation(dept.getLocation());
        dto.setPhoneNumber(dept.getPhoneNumber());
        dto.setEmail(dept.getEmail());
        dto.setStatus(dept.getStatus());
        dto.setIsDeleted(dept.getIsDeleted());
        return dto;
    }
    private PatientTableDTO toPatientTableDTO(Patient patient) {
        PatientTableDTO dto = new PatientTableDTO();
        dto.setPatientId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setEmergencyContact(patient.getEmergencyContact());
        dto.setEmergencyPhone(patient.getEmergencyPhone());
        dto.setInsuranceNumber(patient.getInsuranceNumber());
        dto.setBloodType(patient.getBloodType());
        dto.setAllergies(patient.getAllergies());
        dto.setActive(patient.getActive());
        dto.setIsDeleted(patient.getIsDeleted());
        // Optionally compute age
        return dto;
    }
    private DoctorTableDTO toDoctorTableDTO(Doctor doctor) {
        System.err.println("[DEBUG] AdminDashboardController.toDoctorTableDTO: START");
        DoctorTableDTO dto = new DoctorTableDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setExperience(doctor.getExperience());
        dto.setQualifications(doctor.getQualifications()); // Ensure qualifications is mapped
        dto.setStatus(doctor.getStatus());
        dto.setIsDeleted(doctor.getIsDeleted());
        // if (doctor.getDepartment() != null) {
        //     dto.setDepartment(toDepartmentTableDTO(doctor.getDepartment()));
        // } else {
        //     dto.setDepartment(null);
        // }
        System.err.println("[DEBUG] AdminDashboardController.toDoctorTableDTO: END");
        return dto;
    }
    private StaffTableDTO toStaffTableDTO(Staff staff) {
        StaffTableDTO dto = new StaffTableDTO();
        dto.setStaffId(staff.getId());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        dto.setPhoneNumber(staff.getPhoneNumber());
        dto.setPosition(staff.getPosition());
        dto.setStatus(staff.getStatus());
        dto.setIsDeleted(staff.getIsDeleted());
        return dto;
    }
    private UserTableDTO toUserTableDTO(User user) {
        UserTableDTO dto = new UserTableDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setIsDeleted(user.getIsDeleted());
        dto.setVersion(user.getVersion());
        return dto;
    }

    // --- DTO to Entity mapping helpers (for save/update) ---
    private Department toDepartmentEntity(DepartmentTableDTO dto) {
        Department dept = new Department();
        dept.setId(dto.getDepartmentId());
        dept.setDepartmentName(dto.getDepartmentName());
        dept.setDescription(dto.getDescription());
        dept.setLocation(dto.getLocation());
        dept.setPhoneNumber(dto.getPhoneNumber());
        dept.setEmail(dto.getEmail());
        dept.setStatus(dto.getStatus());
        dept.setVersion(dto.getVersion());
        return dept;
    }
    private Patient toPatientEntity(PatientTableDTO dto) {
        Patient patient = new Patient();
        patient.setId(dto.getPatientId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setEmergencyPhone(dto.getEmergencyPhone());
        patient.setInsuranceNumber(dto.getInsuranceNumber());
        patient.setBloodType(dto.getBloodType());
        patient.setAllergies(dto.getAllergies());
        patient.setActive(dto.getActive());
        patient.setIsDeleted(dto.getIsDeleted());
        return patient;
    }
    private Doctor toDoctorEntity(DoctorTableDTO dto) {
        System.err.println("[DEBUG] AdminDashboardController.toDoctorEntity: START");
        Doctor doctor = new Doctor();
        doctor.setId(dto.getDoctorId());
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setExperience(dto.getExperience());
        doctor.setQualifications(dto.getQualifications()); // Ensure qualifications is mapped
        // Defensive: ensure status is always enum
        DoctorStatus status = dto.getStatus();
        if (status == null) {
            status = DoctorStatus.PENDING_VERIFICATION;
            System.err.println("[DEBUG] toDoctorEntity: dto status was null, set to PENDING_VERIFICATION");
        }
        doctor.setStatus(status);
        doctor.setIsDeleted(dto.getIsDeleted());
        // Map department by departmentId if present
        // if (dto.getDepartment() != null && dto.getDepartment().getId() != null) {
        //     Department department = departmentService.getDepartmentById(dto.getDepartment().getId()).orElse(null);
        //     if (department == null) {
        //         System.err.println("[ERROR] toDoctorEntity: Department not found for ID: " + dto.getDepartment().getId());
        //         throw new IllegalArgumentException("Selected department does not exist. Please select a valid department.");
        //     }
        //     doctor.setDepartment(department);
        // } else {
        //     System.err.println("[ERROR] toDoctorEntity: Department or departmentId is null in DTO");
        //     throw new IllegalArgumentException("Department is required. Please select a department.");
        // }
        System.err.println("[DEBUG] toDoctorEntity: mapped status = " + status);
        System.err.println("[DEBUG] toDoctorEntity: mapped qualifications = " + dto.getQualifications());
        System.err.println("[DEBUG] toDoctorEntity: mapped department = " + (doctor.getDepartment() != null ? doctor.getDepartment().getDepartmentName() : "null"));
        System.err.println("[DEBUG] AdminDashboardController.toDoctorEntity: END");
        return doctor;
    }
    private Staff toStaffEntity(StaffTableDTO dto) {
        Staff staff = new Staff();
        staff.setId(dto.getStaffId());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setPosition(dto.getPosition());
        staff.setStatus(dto.getStatus());
        staff.setIsDeleted(dto.getIsDeleted());
        return staff;
    }
    private User toUserEntity(UserTableDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setIsDeleted(dto.getIsDeleted());
        user.setVersion(dto.getVersion());
        return user;
    }

    public DepartmentTableDTO getSelectedDepartment() { return selectedDepartment; }
    public void setSelectedDepartment(DepartmentTableDTO selectedDepartment) { this.selectedDepartment = selectedDepartment; }
    public DoctorTableDTO getSelectedDoctor() { return selectedDoctor; }
    public void setSelectedDoctor(DoctorTableDTO selectedDoctor) {
        System.err.println("[DEBUG] setSelectedDoctor: doctorId=" + (selectedDoctor != null ? selectedDoctor.getDoctorId() : "null") + ", status=" + (selectedDoctor != null ? selectedDoctor.getStatus() : "null"));
        if (selectedDoctor != null && selectedDoctor.getStatus() == null) {
            selectedDoctor.setStatus(DoctorStatus.PENDING_VERIFICATION);
            System.err.println("[DEBUG] setSelectedDoctor: status was null, set to PENDING_VERIFICATION");
        }
        this.selectedDoctor = selectedDoctor;
    }
    public StaffTableDTO getSelectedStaff() { return selectedStaff; }
    public void setSelectedStaff(StaffTableDTO selectedStaff) { this.selectedStaff = selectedStaff; }
    public PatientTableDTO getSelectedPatient() { return selectedPatient; }
    public void setSelectedPatient(PatientTableDTO selectedPatient) { this.selectedPatient = selectedPatient; }
    public UserTableDTO getSelectedUser() { return selectedUser; }
    public void setSelectedUser(UserTableDTO selectedUser) { this.selectedUser = selectedUser; }

    private void loadDeletedDepartments() {
        deletedDepartments = departmentService.getDeletedDepartments().stream()
            .map(this::toDepartmentTableDTO)
            .collect(Collectors.toList());
    }
    private void loadDeletedStaff() {
        deletedStaff = staffService.getDeletedStaff().stream()
            .map(this::toStaffTableDTO)
            .collect(Collectors.toList());
    }
    private void loadDeletedDoctors() {
        deletedDoctors = doctorService.getDeletedDoctorsWithDepartments().stream()
            .map(this::toDoctorTableDTO)
            .collect(Collectors.toList());
    }
    private void loadDeletedPatients() {
        deletedPatients = patientService.getDeletedPatients().stream()
            .map(this::toPatientTableDTO)
            .collect(Collectors.toList());
    }

    public void restoreUser(UserTableDTO dto) {
        System.err.println("[DEBUG] restoreUser called with DTO: " + dto);
        System.err.println("[DEBUG] User ID to restore: " + dto.getId());
        try {
            userService.restoreUser(dto.getId());
            addSuccessMessage("User restored successfully.");
            loadAllUsers();
            loadDeletedUsers();
            System.err.println("[SUCCESS] User restored successfully: " + dto.getId());
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to restore user: " + e.getMessage());
            e.printStackTrace(System.err);
            addErrorMessage("Error restoring user: " + e.getMessage());
        }
    }

    public void editUser(UserTableDTO user) {
        System.err.println("[DEBUG] editUser called for: " + user);
        if (user != null) {
            this.editingUser = new UserTableDTO();
            this.editingUser.setId(user.getId());
            this.editingUser.setUsername(user.getUsername());
            this.editingUser.setEmail(user.getEmail());
            this.editingUser.setRole(user.getRole());
            this.editingUser.setIsDeleted(user.getIsDeleted());
            this.editingUser.setVersion(user.getVersion());
            // Clear the password field for security
            this.newUserPassword = null;
        }
    }

    public void updateUser() {
        System.err.println("[DEBUG] updateUser called for: " + editingUser);
        try {
            if (editingUser == null) {
                addErrorMessage("No user selected for update.");
                return;
            }

            // Get the existing user from the database
            User existingUser = userService.getUserById(editingUser.getId()).orElse(null);
            if (existingUser == null) {
                addErrorMessage("User not found in database.");
                return;
            }

            // Update the user fields
            existingUser.setUsername(editingUser.getUsername());
            existingUser.setEmail(editingUser.getEmail());
            existingUser.setRole(editingUser.getRole());

            // Only update password if a new one is provided
            if (newUserPassword != null && !newUserPassword.trim().isEmpty()) {
                existingUser.setPasswordHash(PasswordUtil.hashPassword(newUserPassword));
                System.err.println("[DEBUG] Password updated for user: " + editingUser.getUsername());
            }

            // Save the updated user
            userService.updateUser(existingUser);
            addSuccessMessage("User '" + editingUser.getUsername() + "' updated successfully.");
            
            // Refresh the user lists
            loadAllUsers();
            loadDeletedUsers();
            
            // Clear the editing data
            editingUser = null;
            newUserPassword = null;
            
            System.err.println("[SUCCESS] User updated: " + existingUser.getId());
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update user: " + e.getMessage());
            e.printStackTrace(System.err);
            addErrorMessage("Failed to update user: " + e.getMessage());
        }
    }

    public void onUserRowSelect(org.primefaces.event.SelectEvent<UserTableDTO> event) {
        UserTableDTO selectedUser = event.getObject();
        editUser(selectedUser);
    }

    public String createUser() {
        try {
            if (newUser == null) {
                addErrorMessage("User data is missing.");
                return null;
            }

            // Trim all string values
            if (newUser.getUsername() != null) {
                newUser.setUsername(newUser.getUsername().trim());
            }
            if (newUser.getEmail() != null) {
                newUser.setEmail(newUser.getEmail().trim());
            }

            // Validate required fields
            if (newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
                addErrorMessage("Username is required.");
                return null;
            }
            if (newUser.getEmail() == null || newUser.getEmail().isEmpty()) {
                addErrorMessage("Email is required.");
                return null;
            }
            if (newUserPassword == null || newUserPassword.trim().isEmpty()) {
                addErrorMessage("Password is required.");
                return null;
            }
            if (newUser.getRole() == null) {
                addErrorMessage("Role is required.");
                return null;
            }

            // Check if username or email already exists
            if (!authenticationService.isUsernameAvailable(newUser.getUsername())) {
                addErrorMessage("Username '" + newUser.getUsername() + "' is already taken.");
                return null;
            }
            if (!registrationService.isEmailUnique(newUser.getEmail())) {
                addErrorMessage("Email '" + newUser.getEmail() + "' is already registered.");
                return null;
            }

            // Create the user entity
            User user = new User();
            user.setUsername(newUser.getUsername());
            user.setEmail(newUser.getEmail());
            user.setPasswordHash(PasswordUtil.hashPassword(newUserPassword));
            user.setRole(newUser.getRole());
            user.setStatus("ACTIVE");
            user.setIsDeleted(false);

            userService.createUser(user);
            addSuccessMessage("User '" + newUser.getUsername() + "' created successfully!");

            // Reset form and refresh data
            newUser = new UserTableDTO();
            newUserPassword = null;
            loadAllUsers();
            loadDeletedUsers();

            return "/views/dashboard/sections/user-management.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("Exception in createUser: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Error creating user: " + e.getMessage());
            return null;
        }
    }

    public void deleteUserById(Long userId) {
        try {
            if (userId == null) {
                addErrorMessage("User ID is required for deletion.");
                return;
            }

            userService.deleteUser(userId);
            addSuccessMessage("User deleted successfully.");
            loadAllUsers();
            loadDeletedUsers();
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Error deleting user: " + e.getMessage());
        }
    }

    public boolean isEmailAvailable(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return registrationService.isEmailUnique(email);
    }

    private DepartmentTableDTO editingDepartment = new DepartmentTableDTO();
    public DepartmentTableDTO getEditingDepartment() { return editingDepartment; }
    public void setEditingDepartment(DepartmentTableDTO editingDepartment) { this.editingDepartment = editingDepartment; }

    public void editDepartment(DepartmentTableDTO dept) {
        System.err.println("[DEBUG] editDepartment called for: " + dept);
        if (dept != null) {
            this.editingDepartment = new DepartmentTableDTO();
            this.editingDepartment.setId(dept.getDepartmentId());
            this.editingDepartment.setDepartmentName(dept.getDepartmentName());
            this.editingDepartment.setDescription(dept.getDescription());
            this.editingDepartment.setLocation(dept.getLocation());
            this.editingDepartment.setPhoneNumber(dept.getPhoneNumber());
            this.editingDepartment.setEmail(dept.getEmail());
            this.editingDepartment.setStatus(dept.getStatus());
            this.editingDepartment.setVersion(dept.getVersion());
        }
    }

    public void updateDepartment() {
        System.err.println("[DEBUG] updateDepartment called for: " + editingDepartment);
        try {
            departmentService.updateDepartment(toDepartmentEntity(editingDepartment));
            addSuccessMessage("Department updated successfully.");
            loadAllDepartments();
            loadDeletedDepartments();
            System.err.println("[SUCCESS] Department updated: " + editingDepartment.getDepartmentId());
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update department: " + e.getMessage());
            e.printStackTrace(System.err);
            addErrorMessage("Failed to update department: " + e.getMessage());
        }
    }

    private DoctorTableDTO createDefaultDoctorDTO() {
        DoctorTableDTO dto = new DoctorTableDTO();
        dto.setStatus(DoctorStatus.values()[0]);
        return dto;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public UserTableDTO getEditingUser() {
        return editingUser;
    }

    public void setEditingUser(UserTableDTO editingUser) {
        this.editingUser = editingUser;
    }



    // --- Doctor Registration for Admin ---
    private com.hms2.dto.user.DoctorRegistrationDTO newDoctorRegistration = new com.hms2.dto.user.DoctorRegistrationDTO();
    public com.hms2.dto.user.DoctorRegistrationDTO getNewDoctorRegistration() { return newDoctorRegistration; }
    public void setNewDoctorRegistration(com.hms2.dto.user.DoctorRegistrationDTO newDoctorRegistration) { this.newDoctorRegistration = newDoctorRegistration; }

    public String createDoctorAccount() {
        try {
            // Trim all string values
            if (newDoctorRegistration.getUsername() != null) newDoctorRegistration.setUsername(newDoctorRegistration.getUsername().trim());
            if (newDoctorRegistration.getEmail() != null) newDoctorRegistration.setEmail(newDoctorRegistration.getEmail().trim());
            if (newDoctorRegistration.getFirstName() != null) newDoctorRegistration.setFirstName(newDoctorRegistration.getFirstName().trim());
            if (newDoctorRegistration.getLastName() != null) newDoctorRegistration.setLastName(newDoctorRegistration.getLastName().trim());
            if (newDoctorRegistration.getPhoneNumber() != null) newDoctorRegistration.setPhoneNumber(newDoctorRegistration.getPhoneNumber().trim());
            if (newDoctorRegistration.getAddress() != null) newDoctorRegistration.setAddress(newDoctorRegistration.getAddress().trim());
            if (newDoctorRegistration.getSpecialization() != null) newDoctorRegistration.setSpecialization(newDoctorRegistration.getSpecialization().trim());
            if (newDoctorRegistration.getLicenseNumber() != null) newDoctorRegistration.setLicenseNumber(newDoctorRegistration.getLicenseNumber().trim());
            if (newDoctorRegistration.getQualifications() != null) newDoctorRegistration.setQualifications(newDoctorRegistration.getQualifications().trim());
            if (newDoctorRegistration.getDepartment() != null) newDoctorRegistration.setDepartment(newDoctorRegistration.getDepartment().trim());

            // Bean Validation
            jakarta.validation.ValidatorFactory factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
            jakarta.validation.Validator validator = factory.getValidator();
            java.util.Set<jakarta.validation.ConstraintViolation<com.hms2.dto.user.DoctorRegistrationDTO>> violations = validator.validate(newDoctorRegistration);
            if (!violations.isEmpty()) {
                for (jakarta.validation.ConstraintViolation<com.hms2.dto.user.DoctorRegistrationDTO> violation : violations) {
                    addErrorMessage(violation.getMessage());
                }
                return null;
            }

            // Check if username or email already exists
            if (!authenticationService.isUsernameAvailable(newDoctorRegistration.getUsername())) {
                addErrorMessage("Username '" + newDoctorRegistration.getUsername() + "' is already taken. Please choose a different username.");
                return null;
            }
            if (!registrationService.isEmailUnique(newDoctorRegistration.getEmail())) {
                addErrorMessage("Email '" + newDoctorRegistration.getEmail() + "' is already registered. Please use a different email.");
                return null;
            }

            // Set default values
            // (status and active are set below)

            // Create User entity
            com.hms2.model.User user = new com.hms2.model.User();
            user.setUsername(newDoctorRegistration.getUsername());
            user.setEmail(newDoctorRegistration.getEmail());
            user.setPasswordHash(PasswordUtil.hashPassword(newDoctorRegistration.getPassword()));
            user.setRole(com.hms2.enums.UserRole.DOCTOR);
            user.setStatus("ACTIVE");
            userService.createUser(user);

            // Find department entity by name
            com.hms2.model.Department department = null;
            java.util.Optional<com.hms2.model.Department> deptOpt = departmentService.getDepartmentByName(newDoctorRegistration.getDepartment());
            if (deptOpt.isPresent()) {
                department = deptOpt.get();
            } else {
                addErrorMessage("Selected department not found.");
                return null;
            }

            // Create Doctor entity
            com.hms2.model.Doctor doctor = new com.hms2.model.Doctor();
            doctor.setUser(user);
            doctor.setFirstName(newDoctorRegistration.getFirstName());
            doctor.setLastName(newDoctorRegistration.getLastName());
            doctor.setEmail(newDoctorRegistration.getEmail());
            doctor.setPhoneNumber(newDoctorRegistration.getPhoneNumber());
            doctor.setAddress(newDoctorRegistration.getAddress());
            doctor.setSpecialization(newDoctorRegistration.getSpecialization());
            doctor.setLicenseNumber(newDoctorRegistration.getLicenseNumber());
            doctor.setQualifications(newDoctorRegistration.getQualifications());
            doctor.setExperience(newDoctorRegistration.getExperience());
            doctor.setDepartment(department);
            doctor.setStatus(com.hms2.enums.DoctorStatus.PENDING_VERIFICATION);
            doctor.setActive(true);
            doctorService.createDoctor(doctor);

            addSuccessMessage("Doctor account for '" + newDoctorRegistration.getUsername() + "' created successfully!");
            newDoctorRegistration = new com.hms2.dto.user.DoctorRegistrationDTO();
            loadAllDoctors();
            loadDeletedDoctors();

            return "/views/dashboard/sections/doctor-management.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("Exception in createDoctorAccount: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Error creating doctor account: " + e.getMessage());
            return null;
        }
    }

    public boolean validateDoctorForm() {
        try {
            jakarta.validation.ValidatorFactory factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
            jakarta.validation.Validator validator = factory.getValidator();
            java.util.Set<jakarta.validation.ConstraintViolation<com.hms2.dto.user.DoctorRegistrationDTO>> violations = validator.validate(newDoctorRegistration);
            if (!violations.isEmpty()) {
                for (jakarta.validation.ConstraintViolation<com.hms2.dto.user.DoctorRegistrationDTO> violation : violations) {
                    addErrorMessage("Validation error: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
                return false;
            }
            addSuccessMessage("Form validation passed successfully!");
            return true;
        } catch (Exception e) {
            addErrorMessage("Validation error: " + e.getMessage());
            return false;
        }
    }

    public List<DoctorTableDTO> getAllDoctors() {
        return allDoctors != null ? allDoctors : java.util.Collections.emptyList();
    }

    public List<UserTableDTO> getAllUsers() {
        return allUsers != null ? allUsers : java.util.Collections.emptyList();
    }

    public List<DepartmentTableDTO> getAllDepartments() {
        return allDepartments != null ? allDepartments : java.util.Collections.emptyList();
    }

    public List<StaffTableDTO> getAllStaff() {
        return allStaff != null ? allStaff : java.util.Collections.emptyList();
    }

    public List<PatientTableDTO> getAllPatients() {
        return allPatients != null ? allPatients : java.util.Collections.emptyList();
    }

    public UserTableDTO getNewUser() {
        return newUser;
    }

    public void setNewUser(UserTableDTO newUser) {
        this.newUser = newUser;
    }

    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return authenticationService.isUsernameAvailable(username);
    }

    public List<UserTableDTO> getDeletedUsers() {
        return deletedUsers != null ? deletedUsers : java.util.Collections.emptyList();
    }

    public void updateDoctor() {
        System.err.println("[DEBUG] AdminDashboardController.updateDoctor: START");
        if (selectedDoctor == null) {
            System.err.println("[DEBUG] updateDoctor: selectedDoctor is null");
            addErrorMessage("No doctor selected for update.");
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }
        if (!validateDoctorDTO(selectedDoctor)) {
            System.err.println("[DEBUG] updateDoctor: validation failed");
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }
        try {
            doctorService.updateDoctor(toDoctorEntity(selectedDoctor));
            addSuccessMessage("Doctor updated successfully.");
            loadAllDoctors();
            loadDeletedDoctors();
            selectedDoctor = null;
        } catch (Exception e) {
            System.err.println("[ERROR] updateDoctor: " + e.getMessage());
            e.printStackTrace(System.err);
            addErrorMessage("Failed to update doctor: " + e.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
        }
        System.err.println("[DEBUG] AdminDashboardController.updateDoctor: END");
    }
}

