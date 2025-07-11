package com.hms2.controller;

import com.hms2.enums.StaffStatus;
import com.hms2.service.StaffService;
import com.hms2.service.PrescriptionMedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named("dashboardController")
@RequestScoped
public class DashboardController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @Inject
    private StaffService staffService;
    
    @Inject
    private PrescriptionMedicationService prescriptionMedicationService;
    
    private Map<String, Long> staffStatistics;
    private Map<String, Long> medicationStatistics;
    private long totalStaff;
    private long activeStaff;
    private long pendingVerificationStaff;
    private long pendingDispensals;
    private long dispensedMedications;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing dashboard controller");
        loadStatistics();
    }
    
    private void loadStatistics() {
        try {
            // Staff statistics
            totalStaff = staffService.getAllStaff().size();
            activeStaff = staffService.getActiveStaff().size();
            pendingVerificationStaff = staffService.getStaffCountByStatus(StaffStatus.PENDING_VERIFICATION);
            
            staffStatistics = new HashMap<>();
            staffStatistics.put("Total", totalStaff);
            staffStatistics.put("Active", activeStaff);
            staffStatistics.put("Pending Verification", pendingVerificationStaff);
            staffStatistics.put("Verified", staffService.getStaffCountByStatus(StaffStatus.VERIFIED));
            staffStatistics.put("Rejected", staffService.getStaffCountByStatus(StaffStatus.REJECTED));
            
            // Medication statistics
            pendingDispensals = prescriptionMedicationService.getCountByStatus("PENDING");
            dispensedMedications = prescriptionMedicationService.getCountByStatus("DISPENSED");
            
            medicationStatistics = new HashMap<>();
            medicationStatistics.put("Pending Dispensals", pendingDispensals);
            medicationStatistics.put("Dispensed", dispensedMedications);
            medicationStatistics.put("Cancelled", prescriptionMedicationService.getCountByStatus("CANCELLED"));
            
            logger.info("Dashboard statistics loaded successfully");
            
        } catch (Exception e) {
            logger.error("Error loading dashboard statistics", e);
        }
    }
    
    public void refreshStatistics() {
        logger.info("Refreshing dashboard statistics");
        loadStatistics();
    }
    
    // Getters
    public Map<String, Long> getStaffStatistics() {
        return staffStatistics;
    }
    
    public Map<String, Long> getMedicationStatistics() {
        return medicationStatistics;
    }
    
    public long getTotalStaff() {
        return totalStaff;
    }
    
    public long getActiveStaff() {
        return activeStaff;
    }
    
    public long getPendingVerificationStaff() {
        return pendingVerificationStaff;
    }
    
    public long getPendingDispensals() {
        return pendingDispensals;
    }
    
    public long getDispensedMedications() {
        return dispensedMedications;
    }
    
    public String getStaffStatusColor(String status) {
        switch (status) {
            case "Active":
                return "success";
            case "Pending Verification":
                return "warning";
            case "Rejected":
                return "danger";
            default:
                return "info";
        }
    }
    
    public String getMedicationStatusColor(String status) {
        switch (status) {
            case "Dispensed":
                return "success";
            case "Pending Dispensals":
                return "warning";
            case "Cancelled":
                return "danger";
            default:
                return "info";
        }
    }
}
