package com.hms2.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.hms2.model.Department;
import com.hms2.model.Doctor;

public interface DoctorService {
    
    Doctor createDoctor(Doctor doctor);
    
    Doctor updateDoctor(Doctor doctor);
    
    void deleteDoctor(Long doctorId);
    
    void restoreDoctor(Long doctorId);
    
    void permanentlyDeleteDoctor(Long doctorId);

    Doctor findById(Long doctorId);
    
    Optional<Doctor> getDoctorById(Long doctorId);
    
    List<Doctor> getAllDoctors();
    
    List<Doctor> getDeletedDoctors();
    
    List<Doctor> getAllDoctorsWithDepartments();
    
    List<Doctor> getDeletedDoctorsWithDepartments();
    
    List<Doctor> getActiveDoctors();
    
    List<Doctor> getDoctorsBySpecialization(String specialization);
    
    List<Doctor> getDoctorsByStatus(String status);
    
    Optional<Doctor> getDoctorByEmail(String email);
    
    Optional<Doctor> getDoctorByLicenseNumber(String licenseNumber);
    
    List<Doctor> searchDoctorsByName(String name);    
    
    
    long getDoctorCountBySpecialization(String specialization);
    
    long getDoctorCountByStatus(String status);

    
    boolean isEmailUnique(String email);
    
    boolean isLicenseNumberUnique(String licenseNumber);

    long getActiveDoctorCount();
    
    // ==================== DASHBOARD ENHANCED FUNCTIONALITY ====================
    
    /**
     * Get dashboard statistics for a specific doctor
     */
    Map<String, Long> getDashboardStatistics(Long doctorId);
    
    /**
     * Get dashboard metrics for a specific doctor
     */
    Map<String, Double> getDashboardMetrics(Long doctorId);
    
    /**
     * Get chart data for doctor dashboard
     */
    Map<String, Object> getChartData(Long doctorId);
    
    /**
     * Get quick actions for doctor dashboard
     */
    List<com.hms2.dto.dashboard.QuickActionDTO> getQuickActions(Long doctorId);
    
    /**
     * Get alerts for doctor dashboard
     */
    List<com.hms2.dto.dashboard.DashboardAlertDTO> getAlerts(Long doctorId);
    
    /**
     * Get today's appointments for a doctor
     */
    List<com.hms2.model.Appointment> getTodaysAppointments(Long doctorId);
    
    /**
     * Get upcoming appointments for a doctor
     */
    List<com.hms2.model.Appointment> getUpcomingAppointments(Long doctorId);
    
    /**
     * Get recent patients for a doctor
     */
    List<com.hms2.model.Patient> getRecentPatients(Long doctorId);
    
    /**
     * Get recent prescriptions for a doctor
     */
    List<com.hms2.model.Prescription> getRecentPrescriptions(Long doctorId);

    List<Doctor> findAll();
    
    /**
     * Find doctor by user ID
     */
    Doctor findByUserId(Long userId);
}
