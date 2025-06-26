package com.hms2.service;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.appointment.AppointmentResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

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
    
    List<Appointment> findUpcomingAppointments();
    
    List<Appointment> findAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Appointment> findAvailableSlots(Doctor doctor, LocalDateTime date);
    
    boolean isSlotAvailable(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    
    List<AppointmentResponseDTO> getAppointmentHistory(Long patientId);
    
    List<AppointmentResponseDTO> getDoctorSchedule(Long doctorId, LocalDateTime date);
    
    void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime);
    
    List<Appointment> findConflictingAppointments(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    
    long getAppointmentCount();
    
    long getTodaysAppointmentCount();
    
    long getPendingAppointmentCount();
}
