package com.hms2.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import com.hms2.enums.BloodType;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.repository.PatientRepository;
import com.hms2.service.PatientService;
import com.hms2.model.Appointment;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Prescription;
import com.hms2.model.Billing;
import com.hms2.dto.dashboard.QuickActionDTO;
import com.hms2.dto.dashboard.DashboardAlertDTO;
import com.hms2.dto.dashboard.HealthTipDTO;
import com.hms2.service.AppointmentService;
import com.hms2.service.MedicalRecordService;
import com.hms2.service.PrescriptionService;
import com.hms2.service.BillingService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PatientServiceImpl implements PatientService {

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private AppointmentService appointmentService;

    @Inject
    private MedicalRecordService medicalRecordService;

    @Inject
    private PrescriptionService prescriptionService;

    @Inject
    private BillingService billingService;

    // Basic CRUD operations
    @Override
    public Patient save(Patient patient) {
        System.err.println("Request to save Patient : " + patient);
        try {
            validatePatient(patient);
            return patientRepository.save(patient);
        } catch (Exception e) {
            System.err.println("Error saving patient");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error saving patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient update(Patient patient) {
        System.err.println("Request to update Patient : " + patient);
        try {
            validatePatient(patient);
            return patientRepository.update(patient);
        } catch (Exception e) {
            System.err.println("Error updating patient");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error updating patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        System.err.println("Request to delete Patient : " + id);
        try {
            patientRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error deleting patient");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient findOne(Long id) {
        System.err.println("Request to get Patient : " + id);
        try {
            Patient patient = patientRepository.findById(id).orElse(null);
            if (patient != null) {
                patient.setActive(true);
                patientRepository.update(patient);
            }
            return patient;
        } catch (Exception e) {
            System.err.println("Error finding patient by ID");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patient by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findAll() {
        System.err.println("Request to get all Patients");
        try {
            return patientRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error finding all patients");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding all patients: " + e.getMessage(), e);
        }
    }

    // Additional methods for compatibility
    @Override
    public Patient createPatient(Patient patient) {
        System.err.println("[CREATE] Patient received from form: " + patient);
        try {
            validatePatient(patient);
            Patient savedPatient = save(patient);
            System.err.println("[SUCCESS] Patient created: " + savedPatient);
            return savedPatient;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to create patient: " + patient);
            throw e;
        }
    }

    @Override
    public Patient updatePatient(Patient patient) {
        System.err.println("[UPDATE] Patient received from form: " + patient);
        try {
            validatePatient(patient);
            Patient updatedPatient = update(patient);
            System.err.println("[SUCCESS] Patient updated: " + updatedPatient);
            return updatedPatient;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update patient: " + patient);
            throw e;
        }
    }

    @Override
    public void deletePatient(Long patientId) {
        System.err.println("[DELETE] Patient delete requested for ID: " + patientId);
        try {
        softDelete(patientId);
            System.err.println("[SUCCESS] Patient soft deleted: " + patientId);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to soft delete patient: " + patientId);
            throw e;
        }
    }

    @Override
    public Patient getPatientById(Long patientId) {
        return findOne(patientId);
    }

    @Override
    public List<Patient> getAllPatients() {
        return findAll();
    }

    @Override
    public Patient findById(Long patientId) {
        return findOne(patientId);
    }

    @Override
    public Patient getPatientByEmail(String email) {
        System.err.println("Request to get Patient by email : " + email);
        try {
            return patientRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            System.err.println("Error finding patient by email");
            throw new RuntimeException("Error finding patient by email: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> searchPatientsByName(String name) {
        System.err.println("Request to search Patients by name : " + name);
        try {
            return patientRepository.searchByName(name);
        } catch (Exception e) {
            System.err.println("Error searching patients by name");
            throw new RuntimeException("Error searching patients by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getPatientsByBloodType(String bloodType) {
        System.err.println("Request to get Patients by blood type : " + bloodType);
        try {
            return patientRepository.findByBloodType(BloodType.valueOf(bloodType));
        } catch (Exception e) {
            System.err.println("Error finding patients by blood type");
            throw new RuntimeException("Error finding patients by blood type: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getRecentPatientsByDoctor(Doctor doctor) {
        System.err.println("Getting recent patients for doctor ID: " + doctor.getDoctorId());
        try {
            return patientRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error finding recent patients by doctor");
            throw new RuntimeException("Error finding recent patients by doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public long getTotalPatientCountByDoctor(Doctor doctor) {
        System.err.println("Getting total patient count for doctor ID: " + doctor.getDoctorId());
        try {
            return patientRepository.count();
        } catch (Exception e) {
            System.err.println("Error counting patients by doctor");
            throw new RuntimeException("Error counting patients by doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getActivePatients() {
        System.err.println("Request to get active Patients");
        try {
            return patientRepository.findActivePatients();
        } catch (Exception e) {
            System.err.println("Error finding active patients");
            throw new RuntimeException("Error finding active patients: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getInactivePatients() {
        System.err.println("Request to get inactive Patients");
        try {
            return patientRepository.findByStatus("inactive");
        } catch (Exception e) {
            System.err.println("Error finding inactive patients");
            throw new RuntimeException("Error finding inactive patients: " + e.getMessage(), e);
        }
    }

    @Override
    public void activatePatient(Long patientId) {
        System.err.println("Request to activate Patient : " + patientId);
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                patient.setActive(true);
            patientRepository.update(patient);
            }
        } catch (Exception e) {
            System.err.println("Error activating patient");
            throw new RuntimeException("Error activating patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void deactivatePatient(Long patientId) {
        System.err.println("Request to deactivate Patient : " + patientId);
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                patient.setActive(true);
                patientRepository.update(patient);
            }
        } catch (Exception e) {
            System.err.println("Error deactivating patient");
            throw new RuntimeException("Error deactivating patient: " + e.getMessage(), e);
        }
    }

    @Override
    public long getTotalPatientCount() {
        System.err.println("Request to get total patient count");
        try {
            return patientRepository.count();
        } catch (Exception e) {
            System.err.println("Error counting total patients");
            throw new RuntimeException("Error counting total patients: " + e.getMessage(), e);
        }
    }

    @Override
    public long getActivePatientCount() {
        System.err.println("Request to get active patient count");
        try {
            return patientRepository.countByStatus("active");
        } catch (Exception e) {
            System.err.println("Error counting active patients");
            throw new RuntimeException("Error counting active patients: " + e.getMessage(), e);
        }
    }

    @Override
    public long getNewPatientsThisMonth() {
        System.err.println("Request to get new patients this month");
        try {
            // This would need a specific repository method to count patients created this month
            // For now, returning total count as placeholder
            return getTotalPatientCount();
        } catch (Exception e) {
            System.err.println("Error counting new patients this month");
            throw new RuntimeException("Error counting new patients this month: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasAllergies(Long patientId) {
        System.err.println("Request to check if patient has allergies : " + patientId);
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                patient.setActive(true);
                patientRepository.update(patient);
            }
            return patient.getAllergies() != null && !patient.getAllergies().trim().isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking patient allergies");
            throw new RuntimeException("Error checking patient allergies: " + e.getMessage(), e);
        }
    }

    @Override
    public int calculateAge(Long patientId) {
        System.err.println("Request to calculate age for patient : " + patientId);
        try {
            Optional<Patient> patient = patientRepository.findByPatientId(String.valueOf(patientId));
            if (patient.isEmpty()) {
                throw new RuntimeException("Patient not found");
            }
            if (patient.get().getDateOfBirth() != null) {
                return Period.between(patient.get().getDateOfBirth().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears();
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error calculating patient age");
            throw new RuntimeException("Error calculating patient age: " + e.getMessage(), e);
        }
    }

    @Override
    public void softDelete(Long id) {
        System.err.println("Request to soft delete Patient : " + id);
        try {
            Optional<Patient> patientOpt = patientRepository.findById(id);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                patient.softDelete("SYSTEM"); // In real app, get current user
                patientRepository.update(patient);
            } else {
                throw new IllegalArgumentException("Patient not found");
            }
        } catch (Exception e) {
            System.err.println("Error soft deleting patient");
            throw new RuntimeException("Error soft deleting patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void restore(Long id) {
        System.err.println("Request to restore Patient : " + id);
        try {
            Optional<Patient> patientOpt = patientRepository.findById(id);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                patient.restore();
                patientRepository.update(patient);
            } else {
                throw new IllegalArgumentException("Patient not found");
            }
        } catch (Exception e) {
            System.err.println("Error restoring patient");
            throw new RuntimeException("Error restoring patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getDeletedPatients() {
        System.err.println("Request to get deleted Patients");
        try {
            return patientRepository.findDeletedPatients();
        } catch (Exception e) {
            System.err.println("Error finding deleted patients");
            throw new RuntimeException("Error finding deleted patients: " + e.getMessage(), e);
        }
    }

    @Override
    public void permanentlyDelete(Long id) {
        System.err.println("Request to permanently delete Patient : " + id);
        try {
            patientRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error permanently deleting patient");
            throw new RuntimeException("Error permanently deleting patient: " + e.getMessage(), e);
        }
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (patient.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
    }
    
    // ==================== DASHBOARD ENHANCED FUNCTIONALITY ====================
    
    /**
     * Get dashboard statistics for a specific patient
     */
    public Map<String, Long> getDashboardStatistics(Long patientId) {
        System.err.println("Getting dashboard statistics for patient ID: " + patientId);
        Map<String, Long> stats = new HashMap<>();
        
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                // Get appointment statistics
                List<Appointment> totalAppointments = appointmentService.findByPatient(patient);
                List<Appointment> completedAppointments = appointmentService.findByStatus("COMPLETED").stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .collect(java.util.stream.Collectors.toList());
                List<Appointment> upcomingAppointments = appointmentService.findUpcomingAppointments().stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .collect(java.util.stream.Collectors.toList());
                
                stats.put("totalAppointments", (long) totalAppointments.size());
                stats.put("completedAppointments", (long) completedAppointments.size());
                stats.put("upcomingAppointments", (long) upcomingAppointments.size());
                
                // Get medical record statistics
                List<MedicalRecord> medicalRecords = medicalRecordService.findByPatient(patient);
                stats.put("totalMedicalRecords", (long) medicalRecords.size());
                
                // Get prescription statistics
                List<Prescription> prescriptions = prescriptionService.findByPatient(patient);
                stats.put("totalPrescriptions", (long) prescriptions.size());
                
                // Get billing statistics
                List<Billing> bills = billingService.getBillingsByPatient(patient);
                stats.put("totalBills", (long) bills.size());
                stats.put("pendingBills", bills.stream().filter(bill -> "PENDING".equals(bill.getStatus())).count());
                
                System.err.println("Dashboard statistics calculated for patient " + patientId + ": " + stats);
            }
        } catch (Exception e) {
            System.err.println("Error getting dashboard statistics for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return stats;
    }
    
    /**
     * Get dashboard metrics for a specific patient
     */
    public Map<String, Double> getDashboardMetrics(Long patientId) {
        System.err.println("Getting dashboard metrics for patient ID: " + patientId);
        Map<String, Double> metrics = new HashMap<>();
        
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                // Calculate appointment completion rate
                List<Appointment> totalAppointments = appointmentService.findByPatient(patient);
                long completedAppointments = appointmentService.findByStatus("COMPLETED").stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .count();
                double completionRate = totalAppointments.size() > 0 ? (double) completedAppointments / totalAppointments.size() * 100 : 0;
                metrics.put("appointmentCompletionRate", completionRate);
                
                // Calculate medication compliance (mock data)
                metrics.put("medicationCompliance", 85.5);
                
                // Calculate health score (mock data)
                metrics.put("healthScore", 78.0);
                
                // Calculate outstanding balance
                List<Billing> bills = billingService.getBillingsByPatient(patient);
                double outstandingBalance = bills.stream()
                    .filter(bill -> "PENDING".equals(bill.getStatus()))
                    .mapToDouble(bill -> bill.getTotalAmount().doubleValue())
                    .sum();
                metrics.put("outstandingBalance", outstandingBalance);
                
                System.err.println("Dashboard metrics calculated for patient " + patientId + ": " + metrics);
            }
        } catch (Exception e) {
            System.err.println("Error getting dashboard metrics for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return metrics;
    }
    
    /**
     * Get chart data for patient dashboard
     */
    public Map<String, Object> getChartData(Long patientId) {
        System.err.println("Getting chart data for patient ID: " + patientId);
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                // Appointment history chart data
                chartData.put("appointmentHistoryData", getAppointmentHistoryData(patient));
                
                // Health trend chart data
                chartData.put("healthTrendData", getHealthTrendData(patient));
                
                // Billing history chart data
                chartData.put("billingHistoryData", getBillingHistoryData(patient));
                
                // Medication compliance chart data
                chartData.put("medicationComplianceData", getMedicationComplianceData(patient));
                
                System.err.println("Chart data prepared for patient " + patientId + ": " + chartData.keySet());
            }
        } catch (Exception e) {
            System.err.println("Error getting chart data for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return chartData;
    }
    
    /**
     * Get quick actions for patient dashboard
     */
    public List<QuickActionDTO> getQuickActions(Long patientId) {
        System.err.println("Getting quick actions for patient ID: " + patientId);
        List<QuickActionDTO> quickActions = new ArrayList<>();
        
        try {
            quickActions.add(new QuickActionDTO("Book Appointment", "pi pi-calendar-plus", "book-appointment", "primary", 
                "Schedule a new appointment with a doctor", "/views/appointments/book-appointment.xhtml"));
            quickActions.add(new QuickActionDTO("View Medical Records", "pi pi-folder-open", "medical-records", "info", 
                "View your medical history and records", "/views/medical-records/view-records.xhtml"));
            quickActions.add(new QuickActionDTO("Request Prescription", "pi pi-file-edit", "request-prescription", "success", 
                "Request a prescription refill", "/views/prescriptions/request-prescription.xhtml"));
            quickActions.add(new QuickActionDTO("Pay Bills", "pi pi-credit-card", "pay-bills", "warning", 
                "View and pay outstanding bills", "/views/billing/pay-bills.xhtml"));
            quickActions.add(new QuickActionDTO("Update Profile", "pi pi-user-edit", "update-profile", "secondary", 
                "Update your personal information", "/views/profile/update-profile.xhtml"));
            quickActions.add(new QuickActionDTO("Contact Doctor", "pi pi-comments", "contact-doctor", "info", 
                "Send a message to your doctor", "/views/messages/contact-doctor.xhtml"));
            
            System.err.println("Quick actions prepared for patient " + patientId + ": " + quickActions.size() + " actions");
        } catch (Exception e) {
            System.err.println("Error getting quick actions for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return quickActions;
    }
    
    /**
     * Get alerts for patient dashboard
     */
    public List<DashboardAlertDTO> getAlerts(Long patientId) {
        System.err.println("Getting alerts for patient ID: " + patientId);
        List<DashboardAlertDTO> alerts = new ArrayList<>();
        
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                // Check for upcoming appointments
                List<Appointment> upcomingAppointments = appointmentService.findUpcomingAppointments().stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .collect(java.util.stream.Collectors.toList());
                
                if (!upcomingAppointments.isEmpty()) {
                    long daysUntilNext = java.time.temporal.ChronoUnit.DAYS.between(
                        java.time.LocalDate.now(), 
                        upcomingAppointments.get(0).getAppointmentDateTime().toLocalDate()
                    );
                    
                    if (daysUntilNext <= 2) {
                        alerts.add(new DashboardAlertDTO("Upcoming Appointment", 
                            "Your " + upcomingAppointments.get(0).getAppointmentType() + " is in " + daysUntilNext + " days.",
                            "warn", "medium", "pi pi-calendar"));
                    }
                }
                
                // Check for pending bills
                List<Billing> pendingBills = billingService.getBillingsByPatient(patient).stream()
                    .filter(bill -> "PENDING".equals(bill.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
                
                if (!pendingBills.isEmpty()) {
                    alerts.add(new DashboardAlertDTO("Pending Bill", 
                        "You have " + pendingBills.size() + " pending bills.", 
                        "error", "high", "pi pi-credit-card"));
                }
                
                // Check for medication reminders (mock data)
                if (Math.random() > 0.6) {
                    alerts.add(new DashboardAlertDTO("Medication Reminder", 
                        "Time to take your medication: Aspirin", "info", "low", "pi pi-pills"));
                }
                
                // Check for health status
                Map<String, Double> metrics = getDashboardMetrics(patientId);
                if (metrics.get("healthScore") < 70.0) {
                    alerts.add(new DashboardAlertDTO("Health Status Alert", 
                        "Your health status requires attention. Please review your recent records.", 
                        "warn", "medium", "pi pi-exclamation-triangle"));
                }
                
                System.err.println("Alerts prepared for patient " + patientId + ": " + alerts.size() + " alerts");
            }
        } catch (Exception e) {
            System.err.println("Error getting alerts for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return alerts;
    }
    
    /**
     * Get health tips for patient dashboard
     */
    public List<HealthTipDTO> getHealthTips(Long patientId) {
        System.err.println("Getting health tips for patient ID: " + patientId);
        List<HealthTipDTO> healthTips = new ArrayList<>();
        
        try {
            healthTips.add(new HealthTipDTO("Stay Hydrated", 
                "Drink at least 8 glasses of water daily to maintain good health.", 
                "pi pi-tint", "General Health", "Medical Guidelines"));
            healthTips.add(new HealthTipDTO("Regular Exercise", 
                "Aim for 30 minutes of moderate exercise most days of the week.", 
                "pi pi-heart", "Fitness", "Health & Wellness"));
            healthTips.add(new HealthTipDTO("Healthy Diet", 
                "Include plenty of fruits, vegetables, and whole grains in your diet.", 
                "pi pi-apple", "Nutrition", "Dietary Guidelines"));
            healthTips.add(new HealthTipDTO("Adequate Sleep", 
                "Get 7-9 hours of quality sleep each night for optimal health.", 
                "pi pi-moon", "Sleep", "Sleep Foundation"));
            healthTips.add(new HealthTipDTO("Stress Management", 
                "Practice relaxation techniques like deep breathing and meditation.", 
                "pi pi-leaf", "Mental Health", "Mental Health Association"));
            
            System.err.println("Health tips prepared for patient " + patientId + ": " + healthTips.size() + " tips");
        } catch (Exception e) {
            System.err.println("Error getting health tips for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        
        return healthTips;
    }
    
    /**
     * Get upcoming appointments for a patient
     */
    public List<Appointment> getUpcomingAppointments(Long patientId) {
        System.err.println("Getting upcoming appointments for patient ID: " + patientId);
        try {
            return appointmentService.findUpcomingAppointments().stream()
                .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting upcoming appointments for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get recent medical records for a patient
     */
    public List<MedicalRecord> getRecentMedicalRecords(Long patientId) {
        System.err.println("Getting recent medical records for patient ID: " + patientId);
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                return medicalRecordService.findByPatient(patient).stream()
                    .limit(5)
                    .collect(java.util.stream.Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error getting recent medical records for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get active prescriptions for a patient
     */
    public List<Prescription> getActivePrescriptions(Long patientId) {
        System.err.println("Getting active prescriptions for patient ID: " + patientId);
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                return prescriptionService.findByPatient(patient).stream()
                    .filter(prescription -> "ACTIVE".equals(prescription.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error getting active prescriptions for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get pending bills for a patient
     */
    public List<Billing> getPendingBills(Long patientId) {
        System.err.println("Getting pending bills for patient ID: " + patientId);
        try {
            Patient patient = findById(patientId);
            if (patient != null) {
                return billingService.getBillingsByPatient(patient).stream()
                    .filter(bill -> "PENDING".equals(bill.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error getting pending bills for patient ID: " + patientId);
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    private Map<String, Object> getAppointmentHistoryData(Patient patient) {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        
        // Generate last 6 months data
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate date = java.time.LocalDate.now().minusMonths(i);
            labels.add(date.format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")));
            
            // Mock data - in real implementation, get from appointment service
            values.add((long) (Math.random() * 5 + 1));
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getHealthTrendData(Patient patient) {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        
        // Generate last 12 months health score data
        for (int i = 11; i >= 0; i--) {
            java.time.LocalDate date = java.time.LocalDate.now().minusMonths(i);
            labels.add(date.format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")));
            
            // Mock data - in real implementation, get from medical records
            values.add(70.0 + Math.random() * 20.0);
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getBillingHistoryData(Patient patient) {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        
        // Generate last 6 months billing data
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate date = java.time.LocalDate.now().minusMonths(i);
            labels.add(date.format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")));
            
            // Mock data - in real implementation, get from billing service
            values.add(Math.random() * 500.0 + 100.0);
        }
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    private Map<String, Object> getMedicationComplianceData(Patient patient) {
        Map<String, Object> data = new HashMap<>();
        List<String> labels = Arrays.asList("On Time", "Late", "Missed", "Not Taken");
        List<Long> values = Arrays.asList(
            (long) (Math.random() * 20 + 15), // 80% compliance
            (long) (Math.random() * 5 + 2),   // 15% late
            (long) (Math.random() * 3 + 1),   // 5% missed
            0L
        );
        
        data.put("labels", labels);
        data.put("data", values);
        return data;
    }
    
    @Override
    public Patient findByUserId(Long userId) {
        System.err.println("Finding patient by user ID: " + userId);
        try {
            Optional<Patient> patientOpt = patientRepository.findByUserId(userId);
            return patientOpt.orElse(null);
        } catch (Exception e) {
            System.err.println("Error finding patient by user ID: " + userId);
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patient by user ID", e);
        }
    }
}
