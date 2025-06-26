package com.hms2.controller;

import com.hms2.model.Staff;
import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Billing;
import com.hms2.service.StaffService;
import com.hms2.service.AppointmentService;
import com.hms2.service.PatientService;
import com.hms2.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("staffDashboardController")
@ViewScoped
public class StaffDashboardController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffDashboardController.class);
    
    @Inject
    private StaffService staffService;
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private BillingService billingService;
    
    private Staff currentStaff;
    private List<Appointment> todaysAppointments;
    private List<Patient> recentPatients;
    private List<Billing> pendingBills;
    
    private long totalPatientsToday;
    private long totalAppointmentsToday;
    private long pendingBillsCount;
    private double totalPendingAmount;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing staff dashboard controller");
        loadStaffData();
    }
    
    private void loadStaffData() {
        try {
            // Mock staff ID - in real app, get from session
            Long staffId = 1L; // This should come from authenticated user session
            
            currentStaff = staffService.getStaffById(staffId);
            if (currentStaff != null) {
                loadAppointments();
                loadPatients();
                loadBillings();
                calculateStatistics();
            }
            
            logger.info("Staff dashboard data loaded successfully");
            
        } catch (Exception e) {
            logger.error("Error loading staff dashboard data", e);
        }
    }
    
    private void loadAppointments() {
        todaysAppointments = appointmentService.getTodaysAppointments();
        totalAppointmentsToday = todaysAppointments.size();
    }
    
    private void loadPatients() {
        recentPatients = patientService.getRecentPatients(10);
        totalPatientsToday = patientService.getTodaysPatientCount();
    }
    
    private void loadBillings() {
        pendingBills = billingService.getPendingBills();
        pendingBillsCount = pendingBills.size();
        totalPendingAmount = pendingBills.stream()
            .mapToDouble(bill -> bill.getTotalAmount().doubleValue())
            .sum();
    }
    
    private void calculateStatistics() {
        // Additional statistics calculations can be added here
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
    
    public long getTotalPatientsToday() {
        return totalPatientsToday;
    }
    
    public long getTotalAppointmentsToday() {
        return totalAppointmentsToday;
    }
    
    public long getPendingBillsCount() {
        return pendingBillsCount;
    }
    
    public double getTotalPendingAmount() {
        return totalPendingAmount;
    }
}
