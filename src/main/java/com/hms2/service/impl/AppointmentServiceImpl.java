package com.hms2.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.appointment.AppointmentResponseDTO;
import com.hms2.enums.AppointmentStatus;
import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.repository.AppointmentRepository;
import com.hms2.repository.DoctorRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.service.AppointmentService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
            Patient patient = patientRepository.findById(appointmentRequest.getPatientId()).orElse(null);
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + appointmentRequest.getPatientId());
            }
            
            Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId()).orElse(null);
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
            appointment.setStatus(AppointmentStatus.SCHEDULED);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.cancel(reason, reason);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
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
        return appointmentRepository.findById(appointmentId).orElse(null);
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
        return appointmentRepository.findByStatus(AppointmentStatus.valueOf(status.toUpperCase()));
    }
    
    @Override
    public List<Appointment> findTodaysAppointments() {
        return appointmentRepository.findTodaysAppointments();
    }

    @Override
    public List<Appointment> getTodaysAppointmentsByDoctor(Doctor doctor) {
        return List.of();
    }

    @Override
    public List<Appointment> findUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments();
    }

    @Override
    public List<Appointment> getUpcomingAppointmentsByDoctor(Doctor doctor) {
        return List.of();
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
            final LocalDateTime slotStart = currentSlot;
            LocalDateTime slotEnd = currentSlot.plusMinutes(30);
            
            // Check if this slot conflicts with existing appointments
            boolean isAvailable = existingAppointments.stream()
                .noneMatch(apt -> isTimeOverlapping(slotStart, slotEnd, 
                    apt.getAppointmentDateTime(), apt.getEndTime()));
            
            if (isAvailable) {
                Appointment slot = new Appointment();
                slot.setDoctor(doctor);
                slot.setAppointmentDateTime(slotStart);
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
        Patient patient = patientRepository.findById(patientId).orElse(null);
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
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
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
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
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
        return new ArrayList<>(); // Placeholder implementation
    }
    
    @Override
    public long getAppointmentCount() {
        return appointmentRepository.count();
    }
    
    @Override
    public int getTodaysAppointmentCount() {
        return appointmentRepository.findTodaysAppointments().size();
    }
    
    @Override
    public long getPendingAppointmentCount() {
        return appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED) + appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return List.of();
    }

    @Override
    public long getWeeklyAppointmentCount() {
        return 0;
    }

    @Override
    public long getMonthlyAppointmentCount() {
        return 0;
    }

    @Override
    public long getCompletedAppointmentCount() {
        return 0;
    }

    @Override
    public long getCancelledAppointmentCount() {
        return 0;
    }

    @Override
    public long getWeeklyAppointmentCountByDoctor(Doctor doctor) {
        return 0;
    }

    @Override
    public List<Appointment> findDeletedAppointments() {
        try {
            return appointmentRepository.findDeletedAppointments();
        } catch (Exception e) {
            logger.error("Error finding deleted appointments", e);
            throw new RuntimeException("Error finding deleted appointments: " + e.getMessage(), e);
        }
    }

    // ==================== ENHANCED APPOINTMENT MANAGEMENT ====================
    
    @Override
    public Appointment scheduleAppointment(AppointmentRequestDTO appointmentRequest, String requestedBy) {
        logger.info("Scheduling appointment for patient: {}, doctor: {}, requested by: {}", 
            appointmentRequest.getPatientId(), appointmentRequest.getDoctorId(), requestedBy);
        
        try {
            // Validate patient and doctor exist
            Patient patient = patientRepository.findById(appointmentRequest.getPatientId()).orElse(null);
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + appointmentRequest.getPatientId());
            }
            
            Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId()).orElse(null);
            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with ID: " + appointmentRequest.getDoctorId());
            }
            
            // Convert Date to LocalDateTime
            LocalDateTime appointmentDateTime = convertToLocalDateTime(appointmentRequest.getAppointmentDate());
            
            // Check for conflicts
            LocalDateTime endTime = appointmentDateTime.plusMinutes(Integer.parseInt(appointmentRequest.getDuration()));
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
            appointment.setPriority(appointmentRequest.getPriority());
            appointment.setSymptoms(appointmentRequest.getSymptoms());
            appointment.setDurationMinutes(Integer.parseInt(appointmentRequest.getDuration()));
            appointment.setFollowUpRequired(appointmentRequest.getFollowUpRequired());
            appointment.setNotes(appointmentRequest.getNotes());
            
            // Set status based on who requested
            if ("STAFF".equals(requestedBy) || "ADMIN".equals(requestedBy)) {
                appointment.setStatus(com.hms2.enums.AppointmentStatus.CONFIRMED);
            } else {
                appointment.setStatus(com.hms2.enums.AppointmentStatus.SCHEDULED);
            }
            
            // Set follow-up date if required
            if (appointmentRequest.getFollowUpRequired() && appointmentRequest.getFollowUpDate() != null) {
                appointment.setFollowUpDate(convertToLocalDateTime(appointmentRequest.getFollowUpDate()));
            }
            
            appointment = appointmentRepository.save(appointment);
            
            logger.info("Appointment scheduled successfully: {}", appointment.getAppointmentId());
            return appointment;
            
        } catch (Exception e) {
            logger.error("Error scheduling appointment", e);
            throw new RuntimeException("Failed to schedule appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment approveAppointment(Long appointmentId, Long approvedBy, String approvedByRole, String notes) {
        logger.info("Approving appointment: {}, approved by: {} ({})", appointmentId, approvedBy, approvedByRole);
        
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.setStatus(com.hms2.enums.AppointmentStatus.CONFIRMED);
            appointment.setNotes(appointment.getNotes() + "\nApproved by " + approvedByRole + " on " + new Date() + ": " + notes);
            
            appointment = appointmentRepository.update(appointment);
            
            logger.info("Appointment approved successfully: {}", appointmentId);
            return appointment;
            
        } catch (Exception e) {
            logger.error("Error approving appointment", e);
            throw new RuntimeException("Failed to approve appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment rejectAppointment(Long appointmentId, Long rejectedBy, String rejectedByRole, String rejectionReason) {
        logger.info("Rejecting appointment: {}, rejected by: {} ({}), reason: {}", 
            appointmentId, rejectedBy, rejectedByRole, rejectionReason);
        
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.setStatus(com.hms2.enums.AppointmentStatus.CANCELLED);
            appointment.setNotes(appointment.getNotes() + "\nRejected by " + rejectedByRole + " on " + new Date() + ": " + rejectionReason);
            
            appointment = appointmentRepository.update(appointment);
            
            logger.info("Appointment rejected successfully: {}", appointmentId);
            return appointment;
            
        } catch (Exception e) {
            logger.error("Error rejecting appointment", e);
            throw new RuntimeException("Failed to reject appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment confirmAppointment(Long appointmentId, String notes) {
        logger.info("Confirming appointment: {}", appointmentId);
        
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.setStatus(com.hms2.enums.AppointmentStatus.CONFIRMED);
            if (notes != null && !notes.trim().isEmpty()) {
                appointment.setNotes(appointment.getNotes() + "\nConfirmed on " + new Date() + ": " + notes);
            }
            
            appointment = appointmentRepository.update(appointment);
            
            logger.info("Appointment confirmed successfully: {}", appointmentId);
            return appointment;
            
        } catch (Exception e) {
            logger.error("Error confirming appointment", e);
            throw new RuntimeException("Failed to confirm appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Appointment rescheduleAppointment(Long appointmentId, Date newDateTime, String reason) {
        logger.info("Rescheduling appointment: {} to: {}", appointmentId, newDateTime);
        
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            LocalDateTime newAppointmentDateTime = convertToLocalDateTime(newDateTime);
            LocalDateTime endTime = newAppointmentDateTime.plusMinutes(appointment.getDurationMinutes());
            
            // Check if new slot is available
            if (!isSlotAvailable(appointment.getDoctor(), newAppointmentDateTime, endTime)) {
                throw new IllegalStateException("The new time slot is not available");
            }
            
            appointment.setAppointmentDateTime(newAppointmentDateTime);
            appointment.setNotes(appointment.getNotes() + "\nRescheduled on " + new Date() + ": " + reason);
            
            appointment = appointmentRepository.update(appointment);
            
            logger.info("Appointment rescheduled successfully: {}", appointmentId);
            return appointment;
            
        } catch (Exception e) {
            logger.error("Error rescheduling appointment", e);
            throw new RuntimeException("Failed to reschedule appointment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Appointment> getPendingAppointments() {
        logger.info("Getting pending appointments");
        try {
            return appointmentRepository.findByStatus(com.hms2.enums.AppointmentStatus.SCHEDULED);
        } catch (Exception e) {
            logger.error("Error getting pending appointments", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getPendingAppointmentsByDoctor(Doctor doctor) {
        logger.info("Getting pending appointments for doctor: {}", doctor.getDoctorId());
        try {
            return appointmentRepository.findByDoctorAndStatus(doctor, com.hms2.enums.AppointmentStatus.SCHEDULED);
        } catch (Exception e) {
            logger.error("Error getting pending appointments by doctor", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        logger.info("Getting appointments by status: {}", status);
        try {
            return appointmentRepository.findByStatus(com.hms2.enums.AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            logger.error("Error getting appointments by status", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getAppointmentsByPatientAndStatus(Patient patient, String status) {
        logger.info("Getting appointments by patient and status: patient={}, status={}", patient.getPatientId(), status);
        try {
            return appointmentRepository.findByPatientAndStatus(patient, com.hms2.enums.AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            logger.error("Error getting appointments by patient and status", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getAppointmentsByDoctorAndStatus(Doctor doctor, String status) {
        logger.info("Getting appointments by doctor and status: doctor={}, status={}", doctor.getDoctorId(), status);
        try {
            return appointmentRepository.findByDoctorAndStatus(doctor, com.hms2.enums.AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            logger.error("Error getting appointments by doctor and status", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<String> getAvailableTimeSlots(Long doctorId, Date date) {
        logger.info("Getting available time slots for doctor: {} on date: {}", doctorId, date);
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
            }
            
            LocalDateTime dateTime = convertToLocalDateTime(date);
            List<Appointment> availableSlots = findAvailableSlots(doctor, dateTime);
            
            List<String> timeSlots = new ArrayList<>();
            for (Appointment slot : availableSlots) {
                timeSlots.add(slot.getAppointmentDateTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
            }
            
            return timeSlots;
        } catch (Exception e) {
            logger.error("Error getting available time slots", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean isTimeSlotAvailable(Long doctorId, Date dateTime) {
        logger.info("Checking if time slot is available for doctor: {} at: {}", doctorId, dateTime);
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                return false;
            }
            
            LocalDateTime appointmentDateTime = convertToLocalDateTime(dateTime);
            LocalDateTime endTime = appointmentDateTime.plusMinutes(30); // Default 30 minutes
            
            return isSlotAvailable(doctor, appointmentDateTime, endTime);
        } catch (Exception e) {
            logger.error("Error checking time slot availability", e);
            return false;
        }
    }
    
    @Override
    public Map<String, Long> getAppointmentStatisticsByStatus() {
        logger.info("Getting appointment statistics by status");
        Map<String, Long> stats = new HashMap<>();
        
        try {
            stats.put("SCHEDULED", appointmentRepository.countByStatus(com.hms2.enums.AppointmentStatus.SCHEDULED));
            stats.put("CONFIRMED", appointmentRepository.countByStatus(com.hms2.enums.AppointmentStatus.CONFIRMED));
            stats.put("COMPLETED", appointmentRepository.countByStatus(com.hms2.enums.AppointmentStatus.COMPLETED));
            stats.put("CANCELLED", appointmentRepository.countByStatus(com.hms2.enums.AppointmentStatus.CANCELLED));
            stats.put("NO_SHOW", appointmentRepository.countByStatus(com.hms2.enums.AppointmentStatus.NO_SHOW));
        } catch (Exception e) {
            logger.error("Error getting appointment statistics by status", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Long> getAppointmentStatisticsByDoctor(Long doctorId) {
        logger.info("Getting appointment statistics for doctor: {}", doctorId);
        Map<String, Long> stats = new HashMap<>();
        
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor != null) {
                stats.put("total", appointmentRepository.countByDoctor(doctor));
                stats.put("today", (long) findTodaysAppointments().stream()
                    .filter(apt -> apt.getDoctor().getDoctorId().equals(doctorId))
                    .count());
                stats.put("upcoming", (long) findUpcomingAppointments().stream()
                    .filter(apt -> apt.getDoctor().getDoctorId().equals(doctorId))
                    .count());
            }
        } catch (Exception e) {
            logger.error("Error getting appointment statistics by doctor", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Long> getAppointmentStatisticsByPatient(Long patientId) {
        logger.info("Getting appointment statistics for patient: {}", patientId);
        Map<String, Long> stats = new HashMap<>();
        
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                stats.put("total", appointmentRepository.countByPatient(patient));
                stats.put("completed", (long) findByStatus("COMPLETED").stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .count());
                stats.put("upcoming", (long) findUpcomingAppointments().stream()
                    .filter(apt -> apt.getPatient().getPatientId().equals(patientId))
                    .count());
            }
        } catch (Exception e) {
            logger.error("Error getting appointment statistics by patient", e);
        }
        
        return stats;
    }
    
    @Override
    public void sendAppointmentReminders() {
        logger.info("Sending appointment reminders");
        try {
            List<Appointment> upcomingAppointments = getUpcomingAppointmentsForReminders();
            for (Appointment appointment : upcomingAppointments) {
                // In a real implementation, send email/SMS reminders
                logger.info("Sending reminder for appointment: {} to patient: {}", 
                    appointment.getAppointmentId(), appointment.getPatient().getEmail());
            }
        } catch (Exception e) {
            logger.error("Error sending appointment reminders", e);
        }
    }
    
    @Override
    public List<Appointment> getUpcomingAppointmentsForReminders() {
        logger.info("Getting upcoming appointments for reminders");
        try {
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            LocalDateTime dayAfterTomorrow = LocalDateTime.now().plusDays(2);
            
            return appointmentRepository.findByDateRange(tomorrow, dayAfterTomorrow).stream()
                .filter(apt -> apt.getStatus() == com.hms2.enums.AppointmentStatus.CONFIRMED)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting upcoming appointments for reminders", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public void markAsNoShow(Long appointmentId, String notes) {
        logger.info("Marking appointment as no-show: {}", appointmentId);
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
            }
            
            appointment.markNoShow();
            if (notes != null && !notes.trim().isEmpty()) {
                appointment.setNotes(appointment.getNotes() + "\nNo-show marked on " + new Date() + ": " + notes);
            }
            
            appointmentRepository.update(appointment);
            logger.info("Appointment marked as no-show successfully: {}", appointmentId);
            
        } catch (Exception e) {
            logger.error("Error marking appointment as no-show", e);
            throw new RuntimeException("Failed to mark appointment as no-show: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Appointment> getNoShowAppointments() {
        logger.info("Getting no-show appointments");
        try {
            return appointmentRepository.findByStatus(com.hms2.enums.AppointmentStatus.NO_SHOW);
        } catch (Exception e) {
            logger.error("Error getting no-show appointments", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getNoShowAppointmentsByDoctor(Doctor doctor) {
        logger.info("Getting no-show appointments for doctor: {}", doctor.getDoctorId());
        try {
            return appointmentRepository.findByDoctorAndStatus(doctor, com.hms2.enums.AppointmentStatus.NO_SHOW);
        } catch (Exception e) {
            logger.error("Error getting no-show appointments by doctor", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getUrgentAppointments() {
        logger.info("Getting urgent appointments");
        try {
            return appointmentRepository.findAll().stream()
                .filter(apt -> "URGENT".equals(apt.getAppointmentType()) || "EMERGENCY".equals(apt.getAppointmentType()))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting urgent appointments", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getUrgentAppointmentsByDoctor(Doctor doctor) {
        logger.info("Getting urgent appointments for doctor: {}", doctor.getDoctorId());
        try {
            return appointmentRepository.findByDoctor(doctor).stream()
                .filter(apt -> "URGENT".equals(apt.getAppointmentType()) || "EMERGENCY".equals(apt.getAppointmentType()))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting urgent appointments by doctor", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Appointment> getCompletedAppointmentsByDoctor(Doctor doctor) {
        logger.info("Getting completed appointments for doctor: {}", doctor.getDoctorId());
        try {
            return appointmentRepository.findByDoctorAndStatus(doctor, com.hms2.enums.AppointmentStatus.COMPLETED);
        } catch (Exception e) {
            logger.error("Error getting completed appointments by doctor", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public long getTotalAppointmentsByDoctor(Doctor doctor) {
        logger.info("Getting total appointments for doctor: {}", doctor.getDoctorId());
        try {
            return appointmentRepository.countByDoctor(doctor);
        } catch (Exception e) {
            logger.error("Error getting total appointments by doctor", e);
            return 0;
        }
    }
    
    @Override
    public List<com.hms2.model.Prescription> getRecentPrescriptionsByDoctor(Doctor doctor) {
        logger.info("Getting recent prescriptions for doctor: {}", doctor.getDoctorId());
        try {
            // This would require prescription service integration
            // For now, return empty list
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting recent prescriptions by doctor", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<com.hms2.model.Prescription> getOverduePrescriptionsByDoctor(Doctor doctor) {
        logger.info("Getting overdue prescriptions for doctor: {}", doctor.getDoctorId());
        try {
            // This would require prescription service integration
            // For now, return empty list
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting overdue prescriptions by doctor", e);
            return new ArrayList<>();
        }
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
        dto.setDepartment(appointment.getDoctor().getDepartment().getDepartmentName());
        dto.setAppointmentDate(java.sql.Timestamp.valueOf(appointment.getAppointmentDateTime()));
        dto.setStatus(appointment.getStatus().name());
        dto.setReason(appointment.getReason());
        dto.setPriority(appointment.getAppointmentType());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedDate(java.sql.Timestamp.valueOf(appointment.getCreatedAt()));
        return dto;
    }
}
