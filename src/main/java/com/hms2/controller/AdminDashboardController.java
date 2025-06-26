package com.hms2.controller;

import com.hms2.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Named("adminDashboardController")
@ViewScoped
public class AdminDashboardController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
    
    @Inject private PatientService patientService;
    @Inject private DoctorService doctorService;
    @Inject private StaffService staffService;
    @Inject private AppointmentService appointmentService;
    @Inject private BillingService billingService;
    @Inject private DepartmentService departmentService;
    
    // Dashboard Statistics
    private Map<String, Long> systemStats;
    private Map<String, BigDecimal> financialStats;
    private Map<String, Long> appointmentStats;
    private Map<String, Long> userStats;
    
    // Recent Activities
    private int recentPatientsCount;
    private int todaysAppointmentsCount;
    private int pendingBillsCount;
    private int activeStaffCount;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing admin dashboard controller");
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        try {
            loadSystemStatistics();
            loadFinancialStatistics();
            loadAppointmentStatistics();
            loadUserStatistics();
            loadRecentActivities();
            
            logger.info("Admin dashboard data loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading admin dashboard data", e);
        }
    }
    
    private void loadSystemStatistics() {
        systemStats = new HashMap<>();
        systemStats.put("totalPatients", (long) patientService.getAllPatients().size());
        systemStats.put("totalDoctors", (long) doctorService.getAllDoctors().size());
        systemStats.put("totalStaff", (long) staffService.getAllStaff().size());
        systemStats.put("totalDepartments", (long) departmentService.getAllDepartments().size());
        systemStats.put("totalAppointments", (long) appointmentService.getAllAppointments().size());
        systemStats.put("totalBillings", (long) billingService.getAllBillings().size());
    }
    
    private void loadFinancialStatistics() {
        financialStats = new HashMap<>();
        financialStats.put("totalRevenue", billingService.calculateTotalRevenue());
        financialStats.put("pendingAmount", billingService.calculatePendingAmount());
        financialStats.put("monthlyRevenue", calculateMonthlyRevenue());
        financialStats.put("averageBillAmount", calculateAverageBillAmount());
    }
    
    private void loadAppointmentStatistics() {
        appointmentStats = new HashMap<>();
        appointmentStats.put("todaysAppointments", appointmentService.getTodaysAppointmentCount());
        appointmentStats.put("weeklyAppointments", appointmentService.getWeeklyAppointmentCount());
        appointmentStats.put("monthlyAppointments", appointmentService.getMonthlyAppointmentCount());
        appointmentStats.put("completedAppointments", appointmentService.getCompletedAppointmentCount());
        appointmentStats.put("cancelledAppointments", appointmentService.getCancelledAppointmentCount());
    }
    
    private void loadUserStatistics() {
        userStats = new HashMap<>();
        userStats.put("activePatients", patientService.getActivePatientCount());
        userStats.put("activeDoctors", doctorService.getActiveDoctorCount());
        userStats.put("activeStaff", staffService.getActiveStaffCount());
        userStats.put("newPatientsThisMonth", patientService.getNewPatientsThisMonthCount());
    }
    
    private void loadRecentActivities() {
        recentPatientsCount = patientService.getRecentPatients(7).size();
        todaysAppointmentsCount = appointmentService.getTodaysAppointments().size();
        pendingBillsCount = billingService.getPendingBills().size();
        activeStaffCount = staffService.getActiveStaff().size();
    }
    
    private BigDecimal calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        
        return billingService.getBillingsByDateRange(startOfMonth, endOfMonth)
            .stream()
            .filter(billing -> "PAID".equals(billing.getStatus()))
            .map(billing -> billing.getTotalAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateAverageBillAmount() {
        var billings = billingService.getAllBillings();
        if (billings.isEmpty()) return BigDecimal.ZERO;
        
        BigDecimal total = billings.stream()
            .map(billing -> billing.getTotalAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(BigDecimal.valueOf(billings.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public void refreshDashboard() {
        logger.info("Refreshing admin dashboard");
        loadDashboardData();
    }
    
    // Getters
    public Map<String, Long> getSystemStats() { return systemStats; }
    public Map<String, BigDecimal> getFinancialStats() { return financialStats; }
    public Map<String, Long> getAppointmentStats() { return appointmentStats; }
    public Map<String, Long> getUserStats() { return userStats; }
    
    public int getRecentPatientsCount() { return recentPatientsCount; }
    public int getTodaysAppointmentsCount() { return todaysAppointmentsCount; }
    public int getPendingBillsCount() { return pendingBillsCount; }
    public int getActiveStaffCount() { return activeStaffCount; }
    
    // Utility methods for dashboard
    public String getGrowthPercentage(String metric) {
        // Mock calculation - in real app, compare with previous period
        return "+12.5%";
    }
    
    public String getGrowthTrend(String metric) {
        // Mock trend - in real app, calculate actual trend
        return "positive";
    }
}
