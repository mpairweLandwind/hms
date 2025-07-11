package com.hms2.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;
import java.util.Map;

import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.appointment.AppointmentResponseDTO;
import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;

public interface AppointmentService {

    Appointment createAppointment(AppointmentRequestDTO appointmentRequest);

    Appointment updateAppointment(Long appointmentId, AppointmentRequestDTO appointmentRequest);

    void cancelAppointment(Long appointmentId, String reason);

    void confirmAppointment(Long appointmentId);

    void completeAppointment(Long appointmentId, String notes);

    void markNoShow(Long appointmentId);

    Appointment findById(Long appointmentId);

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByStatus(String status);

    List<Appointment> findTodaysAppointments();

    List<Appointment> getTodaysAppointmentsByDoctor(Doctor doctor);

    List<Appointment> findUpcomingAppointments();

    List<Appointment> getUpcomingAppointmentsByDoctor(Doctor doctor);

    List<Appointment> findAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findAvailableSlots(Doctor doctor, LocalDateTime date);

    boolean isSlotAvailable(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);

    List<AppointmentResponseDTO> getAppointmentHistory(Long patientId);

    List<AppointmentResponseDTO> getDoctorSchedule(Long doctorId, LocalDateTime date);

    void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime);

    List<Appointment> findConflictingAppointments(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);

    long getAppointmentCount();

    int getTodaysAppointmentCount();

    long getPendingAppointmentCount();

    List<Appointment> getAllAppointments();

    long getWeeklyAppointmentCount();

    long getMonthlyAppointmentCount();

    long getCompletedAppointmentCount();

    long getCancelledAppointmentCount();

    long getWeeklyAppointmentCountByDoctor(Doctor doctor);

    List<Appointment> findDeletedAppointments();
    
    // ==================== ENHANCED APPOINTMENT MANAGEMENT ====================
    
    /**
     * Schedule appointment (for staff and patients)
     */
    Appointment scheduleAppointment(AppointmentRequestDTO appointmentRequest, String requestedBy);
    
    /**
     * Approve appointment (for admin and doctors)
     */
    Appointment approveAppointment(Long appointmentId, Long approvedBy, String approvedByRole, String notes);
    
    /**
     * Reject appointment (for admin and doctors)
     */
    Appointment rejectAppointment(Long appointmentId, Long rejectedBy, String rejectedByRole, String rejectionReason);
    
    /**
     * Confirm appointment (for staff)
     */
    Appointment confirmAppointment(Long appointmentId, String notes);
    
    /**
     * Reschedule appointment
     */
    Appointment rescheduleAppointment(Long appointmentId, Date newDateTime, String reason);
    
    /**
     * Get pending appointments for approval
     */
    List<Appointment> getPendingAppointments();
    
    /**
     * Get pending appointments by doctor
     */
    List<Appointment> getPendingAppointmentsByDoctor(Doctor doctor);
    
    /**
     * Get appointments by status
     */
    List<Appointment> getAppointmentsByStatus(String status);
    
    /**
     * Get appointments by patient and status
     */
    List<Appointment> getAppointmentsByPatientAndStatus(Patient patient, String status);
    
    /**
     * Get appointments by doctor and status
     */
    List<Appointment> getAppointmentsByDoctorAndStatus(Doctor doctor, String status);
    
    /**
     * Get available time slots for a doctor on a specific date
     */
    List<String> getAvailableTimeSlots(Long doctorId, Date date);
    
    /**
     * Check if time slot is available
     */
    boolean isTimeSlotAvailable(Long doctorId, Date dateTime);
    
    /**
     * Get appointment statistics by status
     */
    Map<String, Long> getAppointmentStatisticsByStatus();
    
    /**
     * Get appointment statistics by doctor
     */
    Map<String, Long> getAppointmentStatisticsByDoctor(Long doctorId);
    
    /**
     * Get appointment statistics by patient
     */
    Map<String, Long> getAppointmentStatisticsByPatient(Long patientId);
    
    /**
     * Send appointment reminders
     */
    void sendAppointmentReminders();
    
    /**
     * Get upcoming appointments for reminders
     */
    List<Appointment> getUpcomingAppointmentsForReminders();
    
    /**
     * Mark appointment as no-show
     */
    void markAsNoShow(Long appointmentId, String notes);
    
    /**
     * Get no-show appointments
     */
    List<Appointment> getNoShowAppointments();
    
    /**
     * Get no-show appointments by doctor
     */
    List<Appointment> getNoShowAppointmentsByDoctor(Doctor doctor);
    
    /**
     * Get urgent appointments
     */
    List<Appointment> getUrgentAppointments();
    
    /**
     * Get urgent appointments by doctor
     */
    List<Appointment> getUrgentAppointmentsByDoctor(Doctor doctor);
    
    /**
     * Get completed appointments by doctor
     */
    List<Appointment> getCompletedAppointmentsByDoctor(Doctor doctor);
    
    /**
     * Get total appointments by doctor
     */
    long getTotalAppointmentsByDoctor(Doctor doctor);
    
    /**
     * Get recent prescriptions by doctor
     */
    List<com.hms2.model.Prescription> getRecentPrescriptionsByDoctor(Doctor doctor);
    
    /**
     * Get overdue prescriptions by doctor
     */
    List<com.hms2.model.Prescription> getOverduePrescriptionsByDoctor(Doctor doctor);
}
