package com.hms2.controller;

import com.hms2.model.Doctor;
import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.service.DoctorService;
import com.hms2.service.AppointmentService;
import com.hms2.service.PatientService;
import com.hms2.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Named("doctorDashboardController")
@ViewScoped
public class DoctorDashboardController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboardController.class);
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private PrescriptionService prescriptionService;
    
    private Doctor currentDoctor;
    private List<Appointment> todaysAppointments;
    private List<Appointment> upcomingAppointments;
    private List<Patient> recentPatients;
    private List<Prescription> recentPrescriptions;
    
    private long totalPatients;
    private long todaysAppointmentCount;
    private long weeklyAppointmentCount;
    private long activePrescriptionsCount;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing doctor dashboard controller");
        loadDoctorData();
    }
    
    private void loadDoctorData() {
        try {
            // Mock doctor ID - in real app, get from session
            Long doctorId = 1L; // This should come from authenticated user session
            
            currentDoctor = doctorService.getDoctorById(doctorId);
            if (currentDoctor != null) {
                loadAppointments();
                loadPatients();
                loadPrescriptions();
                calculateStatistics();
            }
            
            logger.info("Doctor dashboard data loaded successfully");
            
        } catch (Exception e) {
            logger.error("Error loading doctor dashboard data", e);
        }
    }
    
    private void loadAppointments() {
        todaysAppointments = appointmentService.getTodaysAppointmentsByDoctor(currentDoctor.getDoctorId());
        upcomingAppointments = appointmentService.getUpcomingAppointmentsByDoctor(currentDoctor.getDoctorId());
        todaysAppointmentCount = todaysAppointments.size();
        weeklyAppointmentCount = appointmentService.getWeeklyAppointmentCountByDoctor(currentDoctor.getDoctorId());
    }
    
    private void loadPatients() {
        recentPatients = patientService.getRecentPatientsByDoctor(currentDoctor.getDoctorId(), 10);
        totalPatients = patientService.getTotalPatientCountByDoctor(currentDoctor.getDoctorId());
    }
    
    private void loadPrescriptions() {
        recentPrescriptions = prescriptionService.getRecentPrescriptionsByDoctor(currentDoctor.getDoctorId(), 5);
        activePrescriptionsCount = prescriptionService.getActivePrescriptionCountByDoctor(currentDoctor.getDoctorId());
    }
    
    private void calculateStatistics() {
        // Additional statistics calculations can be added here
    }
    
    public void refreshDashboard() {
        logger.info("Refreshing doctor dashboard");
        loadDoctorData();
    }
    
    // Getters
    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }
    
    public List<Appointment> getTodaysAppointments() {
        return todaysAppointments;
    }
    
    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }
    
    public List<Patient> getRecentPatients() {
        return recentPatients;
    }
    
    public List<Prescription> getRecentPrescriptions() {
        return recentPrescriptions;
    }
    
    public long getTotalPatients() {
        return totalPatients;
    }
    
    public long getTodaysAppointmentCount() {
        return todaysAppointmentCount;
    }
    
    public long getWeeklyAppointmentCount() {
        return weeklyAppointmentCount;
    }
    
    public long getActivePrescriptionsCount() {
        return activePrescriptionsCount;
    }
    
    public String getNextAppointmentTime() {
        if (todaysAppointments != null && !todaysAppointments.isEmpty()) {
            return todaysAppointments.get(0).getAppointmentTime().toString();
        }
        return "No appointments today";
    }
}
