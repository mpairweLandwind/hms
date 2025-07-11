package com.hms2.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.dto.dashboard.DashboardAlertDTO;
import com.hms2.dto.dashboard.QuickActionDTO;
import com.hms2.dto.dashboard.TaskItemDTO;
import com.hms2.enums.StaffStatus;
import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Department;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Staff;
import com.hms2.repository.StaffRepository;
import com.hms2.service.AppointmentService;
import com.hms2.service.BillingService;
import com.hms2.service.DoctorService;
import com.hms2.service.PatientService;
import com.hms2.service.StaffService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StaffServiceImpl implements StaffService {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
    
    @Inject
    private StaffRepository staffRepository;
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private BillingService billingService;
    
    @Inject
    private DoctorService doctorService;
    
    @Override
    public Staff createStaff(Staff staff) {
        logger.info("[CREATE] Staff received from form: {}", staff);
        try {
        logger.info("Creating new staff: {}", staff.getFullName());
        
        // Validate unique constraints
        if (!isEmployeeIdUnique(staff.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        if (!isEmailUnique(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
            logger.info("[SUCCESS] Staff created: {}", staff);
        return staffRepository.save(staff);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to create staff: {}", staff, e);
            throw e;
        }
    }
    
    @Override
    public Staff updateStaff(Staff staff) {
        logger.info("[UPDATE] Staff received from form: {}", staff);
        try {
        logger.info("Updating staff: {}", staff.getFullName());
        
        Optional<Staff> existingStaff = staffRepository.findById(staff.getStaffId());
        if (existingStaff.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }
        
        // Check unique constraints only if values changed
        Staff existing = existingStaff.get();
        if (!existing.getEmployeeId().equals(staff.getEmployeeId()) && 
            !isEmployeeIdUnique(staff.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        if (!existing.getEmail().equals(staff.getEmail()) && 
            !isEmailUnique(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
            logger.info("[SUCCESS] Staff updated: {}", staff);
        return staffRepository.update(staff);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to update staff: {}", staff, e);
            throw e;
        }
    }

    @Override
    public long getActiveStaffCount() {
        return staffRepository.countByStatus(StaffStatus.ACTIVE);
    }

    @Override
    public void deleteStaff(Long staffId) {
        logger.info("[DELETE] Staff delete requested for ID: {}", staffId);
        try {
        logger.info("Soft deleting staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.softDelete("SYSTEM"); // In real app, get current user
            staffRepository.update(staff);
                logger.info("[SUCCESS] Staff soft deleted: {}", staffId);
        } else {
            throw new IllegalArgumentException("Staff not found");
            }
        } catch (Exception e) {
            logger.error("[ERROR] Failed to soft delete staff: {}", staffId, e);
            throw e;
        }
    }

    public void restoreStaff(Long staffId) {
        logger.info("Restoring staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.restore(); // In real app, get current user
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }

    public List<Staff> getDeletedStaff() {
        return staffRepository.findDeletedStaff();
    }

    public void permanentlyDeleteStaff(Long staffId) {
        logger.info("Permanently deleting staff with ID: {}", staffId);
        staffRepository.deleteById(staffId);
    }
    
    @Override
    public Optional<Staff> getStaffById(Long staffId) {
        return staffRepository.findById(staffId);
    }
    
    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
    
    @Override
    public List<Staff> getStaffByDepartment(Department department) {
        return staffRepository.findByDepartment(department);
    }
    
    public List<Staff> getStaffByStatus(StaffStatus status) {
        return staffRepository.findByStatus(status);
    }
    
    @Override
    public List<Staff> getActiveStaff() {
        return staffRepository.findByActive(true);
    }
    
    @Override
    public Optional<Staff> getStaffByEmployeeId(String employeeId) {
        return staffRepository.findByEmployeeId(employeeId);
    }
    
    @Override
    public Optional<Staff> getStaffByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
    
    @Override
    public List<Staff> getStaffByPosition(String position) {
        return staffRepository.findByPosition(position);
    }
    
    @Override
    public List<Staff> searchStaffByName(String name) {
        return staffRepository.searchByName(name);
    }
    
    @Override
    public void verifyStaff(Long staffId) {
        logger.info("Verifying staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.verify();
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void rejectStaff(Long staffId) {
        logger.info("Rejecting staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.reject();
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void deactivateStaff(Long staffId) {
        logger.info("Deactivating staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setActive(false);
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void activateStaff(Long staffId) {
        logger.info("Activating staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setActive(true);
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public long getStaffCountByDepartment(Department department) {
        return staffRepository.countByDepartment(department);
    }
    
    public long getStaffCountByStatus(StaffStatus status) {
        return staffRepository.countByStatus(status);
    }
    
    @Override
    public boolean isEmployeeIdUnique(String employeeId) {
        return staffRepository.findByEmployeeId(employeeId).isEmpty();
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        return staffRepository.findByEmail(email).isEmpty();
    }
    
    // ==================== DASHBOARD ENHANCED FUNCTIONALITY ====================
    
    /**
     * Get dashboard statistics for staff operations
     */
    public Map<String, Long> getDashboardStatistics() {
        logger.info("Getting dashboard statistics for staff operations");
        Map<String, Long> stats = new HashMap<>();
        
        try {
            // Get patient statistics
            List<Patient> allPatients = patientService.findAll();
            List<Patient> activePatients = patientService.getActivePatients();
            long newPatientsThisMonth = patientService.getNewPatientsThisMonth();
            
            stats.put("totalPatients", (long) allPatients.size());
            stats.put("activePatients", (long) activePatients.size());
            stats.put("newPatientsThisMonth", newPatientsThisMonth);
            
            // Get appointment statistics
            List<Appointment> todaysAppointments = appointmentService.findTodaysAppointments();
            List<Appointment> upcomingAppointments = appointmentService.findUpcomingAppointments();
            long pendingAppointments = appointmentService.getPendingAppointmentCount();
            
            stats.put("todaysAppointments", (long) todaysAppointments.size());
            stats.put("upcomingAppointments", (long) upcomingAppointments.size());
            stats.put("pendingAppointments", pendingAppointments);
            
            // Get billing statistics
            List<Billing> allBills = billingService.findAll();
            long pendingBills = billingService.countBillingsByStatus(com.hms2.enums.BillingStatus.PENDING);
            
            stats.put("totalBills", (long) allBills.size());
            stats.put("pendingBills", pendingBills);
            
            // Get doctor statistics
            List<Doctor> allDoctors = doctorService.findAll();
            List<Doctor> activeDoctors = doctorService.getActiveDoctors();
            
            stats.put("totalDoctors", (long) allDoctors.size());
            stats.put("activeDoctors", (long) activeDoctors.size());
            
            logger.info("Dashboard statistics calculated: {}", stats);
        } catch (Exception e) {
            logger.error("Error getting dashboard statistics", e);
        }
        
        return stats;
    }
    
    /**
     * Get dashboard metrics for staff operations
     */
    public Map<String, Double> getDashboardMetrics() {
        logger.info("Getting dashboard metrics for staff operations");
        Map<String, Double> metrics = new HashMap<>();
        
        try {
            // Calculate patient satisfaction (mock data)
            metrics.put("patientSatisfaction", 4.2);
            
            // Calculate appointment completion rate
            long totalAppointments = appointmentService.getAppointmentCount();
            long completedAppointments = appointmentService.getCompletedAppointmentCount();
            double completionRate = totalAppointments > 0 ? (double) completedAppointments / totalAppointments * 100 : 0;
            metrics.put("appointmentCompletionRate", completionRate);
            
            // Calculate average patients per day
            double avgPatientsPerDay = calculateAveragePatientsPerDay();
            metrics.put("averagePatientsPerDay", avgPatientsPerDay);
            
            // Calculate service quality score
            metrics.put("serviceQualityScore", 85.5);
            
            // Calculate bills processed today
            metrics.put("billsProcessedToday", (double) calculateBillsProcessedToday());
            
            logger.info("Dashboard metrics calculated: {}", metrics);
        } catch (Exception e) {
            logger.error("Error getting dashboard metrics", e);
        }
        
        return metrics;
    }
    
    /**
     * Get chart data for staff dashboard
     */
    public Map<String, Object> getChartData() {
        logger.info("Getting chart data for staff dashboard");
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // Daily appointments chart data
            chartData.put("dailyAppointmentsData", getDailyAppointmentsData());
            
            // Patient registration chart data
            chartData.put("patientRegistrationData", getPatientRegistrationData());
            
            // Billing status chart data
            chartData.put("billingStatusData", getBillingStatusData());
            
            // Doctor workload chart data
            chartData.put("doctorWorkloadData", getDoctorWorkloadData());
            
            logger.info("Chart data prepared: {}", chartData.keySet());
        } catch (Exception e) {
            logger.error("Error getting chart data", e);
        }
        
        return chartData;
    }
    
    /**
     * Get quick actions for staff dashboard
     */
    public List<QuickActionDTO> getQuickActions() {
        logger.info("Getting quick actions for staff dashboard");
        List<QuickActionDTO> quickActions = new ArrayList<>();
        
        try {
            quickActions.add(new QuickActionDTO("Register Patient", "pi pi-user-plus", "register-patient", "primary", 
                "Register a new patient in the system", "/views/registration/patient-registration.xhtml"));
            quickActions.add(new QuickActionDTO("Schedule Appointment", "pi pi-calendar-plus", "schedule-appointment", "success", 
                "Schedule an appointment for a patient", "/views/appointments/create-appointment.xhtml"));
            quickActions.add(new QuickActionDTO("Process Payment", "pi pi-credit-card", "process-payment", "warning", 
                "Process patient payments", "/views/billing/process-payment.xhtml"));
            quickActions.add(new QuickActionDTO("Patient Search", "pi pi-search", "search-patients", "info", 
                "Search for patient records", "/views/patients/patient-search.xhtml"));
            quickActions.add(new QuickActionDTO("Generate Report", "pi pi-chart-bar", "generate-report", "secondary", 
                "Generate operational reports", "/views/reports/generate-report.xhtml"));
            quickActions.add(new QuickActionDTO("Update Records", "pi pi-pencil", "update-records", "info", 
                "Update patient and medical records", "/views/records/update-records.xhtml"));
            
            logger.info("Quick actions prepared: {} actions", quickActions.size());
        } catch (Exception e) {
            logger.error("Error getting quick actions", e);
        }
        
        return quickActions;
    }
    
    /**
     * Get alerts for staff dashboard
     */
    public List<DashboardAlertDTO> getAlerts() {
        logger.info("Getting alerts for staff dashboard");
        List<DashboardAlertDTO> alerts = new ArrayList<>();
        
        try {
            // Check for urgent appointments
            List<Appointment> urgentAppointments = appointmentService.findTodaysAppointments().stream()
                .filter(apt -> "URGENT".equals(apt.getAppointmentType()))
                .collect(java.util.stream.Collectors.toList());
            if (!urgentAppointments.isEmpty()) {
                alerts.add(new DashboardAlertDTO("Urgent Appointments", 
                    "You have " + urgentAppointments.size() + " urgent appointments today", "error", "high", "pi pi-exclamation-triangle"));
            }
            
            // Check for pending bills
            long pendingBills = billingService.countBillingsByStatus(com.hms2.enums.BillingStatus.PENDING);
            if (pendingBills > 10) {
                alerts.add(new DashboardAlertDTO("High Pending Bills", 
                    pendingBills + " bills pending processing", "warning", "medium", "pi pi-exclamation-circle"));
            }
            
            // Check for no-show appointments
            List<Appointment> noShowAppointments = appointmentService.findByStatus("NO_SHOW");
            if (noShowAppointments.size() > 2) {
                alerts.add(new DashboardAlertDTO("No-Show Appointments", 
                    noShowAppointments.size() + " no-show appointments today", "warning", "medium", "pi pi-calendar-times"));
            }
            
            // Check for overdue payments
            List<Billing> overdueBills = billingService.getOverdueBillings();
            if (!overdueBills.isEmpty() && overdueBills.size() > 5) {
                alerts.add(new DashboardAlertDTO("Overdue Payments", 
                    "Overdue amount exceeds $5,000", "error", "high", "pi pi-credit-card"));
            }
            
            logger.info("Alerts prepared: {} alerts", alerts.size());
        } catch (Exception e) {
            logger.error("Error getting alerts", e);
        }
        
        return alerts;
    }
    
    /**
     * Get pending tasks for staff dashboard
     */
    public List<TaskItemDTO> getPendingTasks() {
        logger.info("Getting pending tasks for staff dashboard");
        List<TaskItemDTO> pendingTasks = new ArrayList<>();
        
        try {
            pendingTasks.add(new TaskItemDTO("Process patient registrations", 
                "Register new patients in the system", "high", "patient-registration", 
                "pi pi-user-plus", LocalDateTime.now().plusDays(1)));
            pendingTasks.add(new TaskItemDTO("Update medical records", 
                "Ensure all records are up to date", "medium", "medical-records", 
                "pi pi-file-edit", LocalDateTime.now().plusDays(2)));
            pendingTasks.add(new TaskItemDTO("Schedule follow-up appointments", 
                "Arrange follow-ups for patients", "medium", "appointments", 
                "pi pi-calendar-plus", LocalDateTime.now().plusDays(3)));
            pendingTasks.add(new TaskItemDTO("Verify insurance information", 
                "Check and verify patient insurance", "low", "insurance", 
                "pi pi-id-card", LocalDateTime.now().plusDays(4)));
            pendingTasks.add(new TaskItemDTO("Process billing payments", 
                "Process outstanding payments", "high", "billing", 
                "pi pi-credit-card", LocalDateTime.now().plusDays(1)));
            
            logger.info("Pending tasks prepared: {} tasks", pendingTasks.size());
        } catch (Exception e) {
            logger.error("Error getting pending tasks", e);
        }
        
        return pendingTasks;
    }
    
    /**
     * Get today's appointments
     */
    public List<Appointment> getTodaysAppointments() {
        logger.info("Getting today's appointments");
        try {
            return appointmentService.findTodaysAppointments();
        } catch (Exception e) {
            logger.error("Error getting today's appointments", e);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get recent patients
     */
    public List<Patient> getRecentPatients() {
        logger.info("Getting recent patients");
        try {
            return patientService.findAll().stream()
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting recent patients", e);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get pending bills
     */
    public List<Billing> getPendingBills() {
        logger.info("Getting pending bills");
        try {
            return billingService.getPendingBillings();
        } catch (Exception e) {
            logger.error("Error getting pending bills", e);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get available doctors
     */
    public List<Doctor> getAvailableDoctors() {
        logger.info("Getting available doctors");
        try {
            return doctorService.getActiveDoctors();
        } catch (Exception e) {
            logger.error("Error getting available doctors", e);
        }
        return new ArrayList<>();
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    private double calculateAveragePatientsPerDay() {
        try {
            List<Patient> patients = patientService.findAll();
            if (patients.isEmpty()) return 0.0;
            
            // Calculate average over the last 30 days
            LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
            long patientsInLast30Days = patients.stream()
                .filter(patient -> patient.getCreatedAt() != null && 
                    patient.getCreatedAt().toLocalDate().isAfter(thirtyDaysAgo))
                .count();
            
            return (double) patientsInLast30Days / 30.0;
        } catch (Exception e) {
            logger.error("Error calculating average patients per day", e);
            return 0.0;
        }
    }
    
    private long calculateBillsProcessedToday() {
        try {
            List<Billing> bills = billingService.findAll();
            LocalDate today = LocalDate.now();
            
            return bills.stream()
                .filter(bill -> bill.getPaymentDate() != null && 
                    bill.getPaymentDate().toLocalDate().equals(today))
                .count();
        } catch (Exception e) {
            logger.error("Error calculating bills processed today", e);
            return 0;
        }
    }
    
    private Map<String, Object> getDailyAppointmentsData() {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        
        // Generate last 7 days data
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM dd")));
            
            // Mock data - in real implementation, get from appointment service
            values.add((long) (Math.random() * 15 + 8));
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getPatientRegistrationData() {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        
        // Generate last 6 months data
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            labels.add(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            
            // Mock data - in real implementation, get from patient service
            values.add((long) (Math.random() * 30 + 15));
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getBillingStatusData() {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = Arrays.asList("Paid", "Pending", "Overdue", "Cancelled");
        List<Long> values = Arrays.asList(
            (long) (Math.random() * 100 + 50),
            (long) (Math.random() * 30 + 10),
            (long) (Math.random() * 20 + 5),
            (long) (Math.random() * 10 + 2)
        );
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getDoctorWorkloadData() {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        
        try {
            List<Doctor> doctors = doctorService.findAll();
            for (Doctor doctor : doctors) {
                labels.add(doctor.getFirstName() + " " + doctor.getLastName());
                // Mock data - in real implementation, calculate actual workload
                values.add((long) (Math.random() * 20 + 10));
            }
        } catch (Exception e) {
            logger.error("Error getting doctor workload data", e);
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
}
