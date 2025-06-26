package com.hms2.service.impl;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.appointment.AppointmentResponseDTO;
import com.hms2.repository.AppointmentRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.repository.DoctorRepository;
import com.hms2.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Override
    public Appointment createAppointment(AppointmentRequestDTO appointmentRequest) {
        try {
            // Validate patient and doctor exist
            Patient patient = patientRepository.findById(appointmentRequest.getPatientId());
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + appointmentRequest.getPatientId());
            }
            
            Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId());
            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with ID: " + appointmentRequest.getDoctorId());
            }
            
            // Convert Date to LocalDateTime
            LocalDateTime appointmentDateTime = convertToLocalDateTime(appointmentRequest.getAppointmentDate());
            
            // Check for conflicts
            LocalDateTime endTime = appointmentDateTime.plusMinutes(30); // Default 30 minutes
            if (!isSlotAvailable(doctor, appointmentDateTime, endTime)) {
                throw new IllegalStateException("The selected time slot is not available");
            }
            
            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDateTime(appointmentDateTime);
            appointment.setReason(appointmentRequest.getReason());
            appointment.setAppointmentType(determineAppointmentType(appointmentRequest.getPriority()));
            appointment.setStatus("SCHEDULED");
            appointment.setNotes(appointmentRequest.getNotes());
            appointment.setDurationMinutes(30);
            
            return appointmentRepository.save(appointment);
            
        } catch (Exception e) {
            logger.error("Error creating appointment", e);
            throw new RuntimeException("Failed to create appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment updateAppointment(Long appointmentId, AppointmentRequestDTO appointmentRequest) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            // Update fields
            appointment.setReason(appointmentRequest.getReason());
            appointment.setNotes(appointmentRequest.getNotes());
            appointment.setAppointmentType(determineAppointmentType(appointmentRequest.getPriority()));
            
            return appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error updating appointment", e);
            throw new RuntimeException("Failed to update appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void cancelAppointment(Long appointmentId, String reason) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.cancel();
            appointment.setNotes(appointment.getNotes() + "\nCancellation reason: " + reason);
            appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error cancelling appointment", e);
            throw new RuntimeException("Failed to cancel appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void confirmAppointment(Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.confirm();
            appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error confirming appointment", e);
            throw new RuntimeException("Failed to confirm appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void completeAppointment(Long appointmentId, String notes) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.complete();
            if (notes != null && !notes.trim().isEmpty()) {
                appointment.setNotes(appointment.getNotes() + "\nCompletion notes: " + notes);
            }
            appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error completing appointment", e);
            throw new RuntimeException("Failed to complete appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void markNoShow(Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.markNoShow();
            appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error marking appointment as no-show", e);
            throw new RuntimeException("Failed to mark appointment as no-show: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }
    
    @Override
    public List<Appointment> findByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }
    
    @Override
    public List<Appointment> findByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }
    
    @Override
    public List<Appointment> findByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }
    
    @Override
    public List<Appointment> findTodaysAppointments() {
        return appointmentRepository.findTodaysAppointments();
    }
    
    @Override
    public List<Appointment> findUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments();
    }
    
    @Override
    public List<Appointment> findAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Appointment> findAvailableSlots(Doctor doctor, LocalDateTime date) {
        // Get doctor's working hours (assuming 9 AM to 5 PM)
        LocalDateTime startOfDay = date.toLocalDate().atTime(9, 0);
        LocalDateTime endOfDay = date.toLocalDate().atTime(17, 0);
        
        // Get existing appointments for the doctor on this date
        List<Appointment> existingAppointments = appointmentRepository.findByDateRange(startOfDay, endOfDay)
            .stream()
            .filter(apt -> apt.getDoctor().getDoctorId().equals(doctor.getDoctorId()))
            .collect(Collectors.toList());
        
        // Generate available slots (30-minute intervals)
        List<Appointment> availableSlots = new ArrayList<>();
        LocalDateTime currentSlot = startOfDay;
        
        while (currentSlot.isBefore(endOfDay)) {
            LocalDateTime slotEnd = currentSlot.plusMinutes(30);
            
            // Check if this slot conflicts with existing appointments
            boolean isAvailable = existingAppointments.stream()
                .noneMatch(apt -> isTimeOverlapping(currentSlot, slotEnd, 
                    apt.getAppointmentDateTime(), apt.getEndDateTime()));
            
            if (isAvailable) {
                Appointment slot = new Appointment();
                slot.setDoctor(doctor);
                slot.setAppointmentDateTime(currentSlot);
                slot.setDurationMinutes(30);
                availableSlots.add(slot);
            }
            
            currentSlot = currentSlot.plusMinutes(30);
        }
        
        return availableSlots;
    }
    
    @Override
    public boolean isSlotAvailable(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        List<Appointment> conflicts = findConflictingAppointments(doctor, startTime, endTime);
        return conflicts.isEmpty();
    }
    
    @Override
    public List<AppointmentResponseDTO> getAppointmentHistory(Long patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            return new ArrayList<>();
        }
        
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return appointments.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentResponseDTO> getDoctorSchedule(Long doctorId, LocalDateTime date) {
        Doctor doctor = doctorRepository.findById(doctorId);
        if (doctor == null) {
            return new ArrayList<>();
        }
        
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        
        List<Appointment> appointments = appointmentRepository.findByDateRange(startOfDay, endOfDay)
            .stream()
            .filter(apt -> apt.getDoctor().getDoctorId().equals(doctorId))
            .collect(Collectors.toList());
        
        return appointments.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            // Check if new slot is available
            LocalDateTime newEndTime = newDateTime.plusMinutes(appointment.getDurationMinutes());
            if (!isSlotAvailable(appointment.getDoctor(), newDateTime, newEndTime)) {
                throw new IllegalStateException("The new time slot is not available");
            }
            
            appointment.setAppointmentDateTime(newDateTime);
            appointmentRepository.update(appointment);
            
        } catch (Exception e) {
            logger.error("Error rescheduling appointment", e);
            throw new RuntimeException("Failed to reschedule appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Appointment> findConflictingAppointments(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository.findConflictingAppointments(doctor, startTime, endTime);
    }
    
    @Override
    public long getAppointmentCount() {
        return appointmentRepository.count();
    }
    
    @Override
    public long getTodaysAppointmentCount() {
        return appointmentRepository.findTodaysAppointments().size();
    }
    
    @Override
    public long getPendingAppointmentCount() {
        return appointmentRepository.countByStatus("SCHEDULED") + appointmentRepository.countByStatus("CONFIRMED");
    }
    
    // Helper methods
    private LocalDateTime convertToLocalDateTime(java.util.Date date) {
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
    
    private String determineAppointmentType(String priority) {
        if (priority == null) return "ROUTINE";
        switch (priority.toUpperCase()) {
            case "URGENT": return "URGENT";
            case "EMERGENCY": return "EMERGENCY";
            default: return "ROUTINE";
        }
    }
    
    private boolean isTimeOverlapping(LocalDateTime start1, LocalDateTime end1, 
                                    LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    private AppointmentResponseDTO convertToResponseDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        dto.setDoctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
        dto.setDepartment(appointment.getDoctor().getDepartment().getName());
        dto.setAppointmentDate(java.sql.Timestamp.valueOf(appointment.getAppointmentDateTime()));
        dto.setStatus(appointment.getStatus());
        dto.setReason(appointment.getReason());
        dto.setPriority(appointment.getAppointmentType());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedDate(java.sql.Timestamp.valueOf(appointment.getCreatedAt()));
        return dto;
    }
}
