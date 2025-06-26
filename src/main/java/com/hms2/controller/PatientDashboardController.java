package com.hms2.controller;

import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Prescription;
import com.hms2.model.Billing;
import com.hms2.service.PatientService;
import com.hms2.service.AppointmentService;
import com.hms2.service.MedicalRecordService;
import com.hms2.service.PrescriptionService;
import com.hms2.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Named("patientDashboardController")
@ViewScoped
public class PatientDashboardController implements Serializable {
    
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
    private List<Appointment> upcomingAppointments;
    private List<MedicalRecord> recentMedicalRecords;
    private List<Prescription> activePrescriptions;
    private List<Billing> pendingBills;
    
    private long totalAppointments;
    private long completedAppointments;
    private long pendingBillsCount;
    private double totalOutstanding;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing patient dashboard controller");
        // In a real application, get current patient from session
        loadPatientData();
    }
    
    private void loadPatientData() {
        try {
            // Mock patient ID - in real app, get from session
            Long patientId = 1L; // This should come from authenticated user session
            
            currentPatient = patientService.getPatientById(patientId);
            if (currentPatient != null) {
                loadAppointments();
                loadMedicalRecords();
                loadPrescriptions();
                loadBillings();
                calculateStatistics();
            }
            
            logger.info("Patient dashboard data loaded successfully");
            
        } catch (Exception e) {
            logger.error("Error loading patient dashboard data", e);
        }
    }
    
    private void loadAppointments() {
        upcomingAppointments = appointmentService.getUpcomingAppointmentsByPatient(currentPatient.getPatientId());
        totalAppointments = appointmentService.getAppointmentCountByPatient(currentPatient.getPatientId());
        completedAppointments = appointmentService.getCompletedAppointmentCountByPatient(currentPatient.getPatientId());
    }
    
    private void loadMedicalRecords() {
        recentMedicalRecords = medicalRecordService.getRecentRecordsByPatient(currentPatient.getPatientId(), 5);
    }
    
    private void loadPrescriptions() {
        activePrescriptions = prescriptionService.getActivePrescriptionsByPatient(currentPatient.getPatientId());
    }
    
    private void loadBillings() {
        pendingBills = billingService.getPendingBillsByPatient(currentPatient.getPatientId());
        pendingBillsCount = pendingBills.size();
        totalOutstanding = pendingBills.stream()
            .mapToDouble(bill -> bill.getTotalAmount().doubleValue())
            .sum();
    }
    
    private void calculateStatistics() {
        // Additional statistics calculations can be added here
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
        return upcomingAppointments;
    }
    
    public List<MedicalRecord> getRecentMedicalRecords() {
        return recentMedicalRecords;
    }
    
    public List<Prescription> getActivePrescriptions() {
        return activePrescriptions;
    }
    
    public List<Billing> getPendingBills() {
        return pendingBills;
    }
    
    public long getTotalAppointments() {
        return totalAppointments;
    }
    
    public long getCompletedAppointments() {
        return completedAppointments;
    }
    
    public long getPendingBillsCount() {
        return pendingBillsCount;
    }
    
    public double getTotalOutstanding() {
        return totalOutstanding;
    }
    
    public String getNextAppointmentDate() {
        if (upcomingAppointments != null && !upcomingAppointments.isEmpty()) {
            return upcomingAppointments.get(0).getAppointmentDate().toString();
        }
        return "No upcoming appointments";
    }
}
