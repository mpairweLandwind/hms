package com.hms2.controller;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.Department;
import com.hms2.dto.appointment.AppointmentRequestDTO;
import com.hms2.dto.appointment.AppointmentResponseDTO;
import com.hms2.service.AppointmentService;
import com.hms2.service.PatientService;
import com.hms2.service.DoctorService;
import com.hms2.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Named("appointmentController")
@SessionScoped
public class AppointmentController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private DepartmentService departmentService;
    
    // Form fields
    private AppointmentRequestDTO appointmentRequest;
    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Doctor> filteredDoctors;
    private List<Department> departments;
    private Long selectedDepartmentId;
    private Date selectedDate;
    private String selectedTimeSlot;
    private List<String> availableTimeSlots;
    
    // Display fields
    private List<AppointmentResponseDTO> appointments;
    private List<AppointmentResponseDTO> todaysAppointments;
    private List<AppointmentResponseDTO> upcomingAppointments;
    private AppointmentResponseDTO selectedAppointment;
    
    // Search and filter
    private String searchTerm;
    private String statusFilter;
    private Date dateFilter;
    
    // Dialog states
    private boolean showCreateDialog;
    private boolean showEditDialog;
    private boolean showCancelDialog;
    private boolean showRescheduleDialog;
    
    @PostConstruct
    public void init() {
        appointmentRequest = new AppointmentRequestDTO();
        loadInitialData();
        loadAppointments();
        availableTimeSlots = new ArrayList<>();
    }
    
    private void loadInitialData() {
        try {
            patients = patientService.findAll();
            doctors = doctorService.findAll();
            departments = departmentService.findAll();
            filteredDoctors = new ArrayList<>(doctors);
        } catch (Exception e) {
            logger.error("Error loading initial data", e);
            addErrorMessage("Error loading data: " + e.getMessage());
        }
    }
    
    private void loadAppointments() {
        try {
            appointments = new ArrayList<>();
            List<Appointment> allAppointments = appointmentService.findUpcomingAppointments();
            for (Appointment apt : allAppointments) {
                appointments.add(convertToResponseDTO(apt));
            }
            
            todaysAppointments = new ArrayList<>();
            List<Appointment> todayApts = appointmentService.findTodaysAppointments();
            for (Appointment apt : todayApts) {
                todaysAppointments.add(convertToResponseDTO(apt));
            }
            
        } catch (Exception e) {
            logger.error("Error loading appointments", e);
            addErrorMessage("Error loading appointments: " + e.getMessage());
        }
    }
    
    public void onDepartmentChange() {
        if (selectedDepartmentId != null) {
            filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getDepartment().getDepartmentId().equals(selectedDepartmentId))
                .collect(java.util.stream.Collectors.toList());
        } else {
            filteredDoctors = new ArrayList<>(doctors);
        }
        
        // Clear doctor selection when department changes
        appointmentRequest.setDoctorId(null);
        availableTimeSlots.clear();
    }
    
    public void onDoctorChange() {
        if (appointmentRequest.getDoctorId() != null && selectedDate != null) {
            loadAvailableTimeSlots();
        }
    }
    
    public void onDateChange() {
        if (appointmentRequest.getDoctorId() != null && selectedDate != null) {
            loadAvailableTimeSlots();
        }
    }
    
    private void loadAvailableTimeSlots() {
        try {
            availableTimeSlots.clear();
            
            if (appointmentRequest.getDoctorId() != null && selectedDate != null) {
                Doctor doctor = doctorService.findById(appointmentRequest.getDoctorId());
                LocalDateTime dateTime = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                List<Appointment> availableSlots = appointmentService.findAvailableSlots(doctor, dateTime);
                
                for (Appointment slot : availableSlots) {
                    String timeSlot = slot.getAppointmentDateTime().toLocalTime().toString();
                    availableTimeSlots.add(timeSlot);
                }
            }
        } catch (Exception e) {
            logger.error("Error loading available time slots", e);
            addErrorMessage("Error loading available time slots: " + e.getMessage());
        }
    }
    
    public void createAppointment() {
        try {
            if (validateAppointmentRequest()) {
                // Combine date and time
                LocalDateTime appointmentDateTime = combineDateTime(selectedDate, selectedTimeSlot);
                appointmentRequest.setAppointmentDate(Date.from(appointmentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                
                appointmentService.createAppointment(appointmentRequest);
                
                addSuccessMessage("Appointment created successfully!");
                resetForm();
                loadAppointments();
                showCreateDialog = false;
            }
        } catch (Exception e) {
            logger.error("Error creating appointment", e);
            addErrorMessage("Error creating appointment: " + e.getMessage());
        }
    }
    
    public void updateAppointment() {
        try {
            if (selectedAppointment != null && validateAppointmentRequest()) {
                appointmentService.updateAppointment(selectedAppointment.getAppointmentId(), appointmentRequest);
                
                addSuccessMessage("Appointment updated successfully!");
                loadAppointments();
                showEditDialog = false;
            }
        } catch (Exception e) {
            logger.error("Error updating appointment", e);
            addErrorMessage("Error updating appointment: " + e.getMessage());
        }
    }
    
    public void cancelAppointment() {
        try {
            if (selectedAppointment != null) {
                appointmentService.cancelAppointment(selectedAppointment.getAppointmentId(), "Cancelled by user");
                
                addSuccessMessage("Appointment cancelled successfully!");
                loadAppointments();
                showCancelDialog = false;
            }
        } catch (Exception e) {
            logger.error("Error cancelling appointment", e);
            addErrorMessage("Error cancelling appointment: " + e.getMessage());
        }
    }
    
    public void confirmAppointment(Long appointmentId) {
        try {
            appointmentService.confirmAppointment(appointmentId);
            addSuccessMessage("Appointment confirmed successfully!");
            loadAppointments();
        } catch (Exception e) {
            logger.error("Error confirming appointment", e);
            addErrorMessage("Error confirming appointment: " + e.getMessage());
        }
    }
    
    public void completeAppointment(Long appointmentId) {
        try {
            appointmentService.completeAppointment(appointmentId, "Appointment completed");
            addSuccessMessage("Appointment completed successfully!");
            loadAppointments();
        } catch (Exception e) {
            logger.error("Error completing appointment", e);
            addErrorMessage("Error completing appointment: " + e.getMessage());
        }
    }
    
    public void rescheduleAppointment() {
        try {
            if (selectedAppointment != null && selectedDate != null && selectedTimeSlot != null) {
                LocalDateTime newDateTime = combineDateTime(selectedDate, selectedTimeSlot);
                appointmentService.rescheduleAppointment(selectedAppointment.getAppointmentId(), newDateTime);
                
                addSuccessMessage("Appointment rescheduled successfully!");
                loadAppointments();
                showRescheduleDialog = false;
            }
        } catch (Exception e) {
            logger.error("Error rescheduling appointment", e);
            addErrorMessage("Error rescheduling appointment: " + e.getMessage());
        }
    }
    
    public void searchAppointments() {
        // Implement search logic based on searchTerm, statusFilter, dateFilter
        loadAppointments();
    }
    
    public void openCreateDialog() {
        resetForm();
        showCreateDialog = true;
    }
    
    public void openEditDialog(AppointmentResponseDTO appointment) {
        selectedAppointment = appointment;
        populateFormFromAppointment(appointment);
        showEditDialog = true;
    }
    
    public void openCancelDialog(AppointmentResponseDTO appointment) {
        selectedAppointment = appointment;
        showCancelDialog = true;
    }
    
    public void openRescheduleDialog(AppointmentResponseDTO appointment) {
        selectedAppointment = appointment;
        selectedDate = appointment.getAppointmentDate();
        loadAvailableTimeSlots();
        showRescheduleDialog = true;
    }
    
    private boolean validateAppointmentRequest() {
        if (appointmentRequest.getPatientId() == null) {
            addErrorMessage("Please select a patient");
            return false;
        }
        
        if (appointmentRequest.getDoctorId() == null) {
            addErrorMessage("Please select a doctor");
            return false;
        }
        
        if (selectedDate == null) {
            addErrorMessage("Please select a date");
            return false;
        }
        
        if (selectedTimeSlot == null || selectedTimeSlot.isEmpty()) {
            addErrorMessage("Please select a time slot");
            return false;
        }
        
        if (appointmentRequest.getReason() == null || appointmentRequest.getReason().trim().isEmpty()) {
            addErrorMessage("Please provide a reason for the appointment");
            return false;
        }
        
        return true;
    }
    
    private LocalDateTime combineDateTime(Date date, String timeSlot) {
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String[] timeParts = timeSlot.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        return dateTime.withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    }
    
    private void resetForm() {
        appointmentRequest = new AppointmentRequestDTO();
        selectedDepartmentId = null;
        selectedDate = null;
        selectedTimeSlot = null;
        availableTimeSlots.clear();
        filteredDoctors = new ArrayList<>(doctors);
    }
    
    private void populateFormFromAppointment(AppointmentResponseDTO appointment) {
        appointmentRequest.setPatientId(getPatientIdByName(appointment.getPatientName()));
        appointmentRequest.setDoctorId(getDoctorIdByName(appointment.getDoctorName()));
        appointmentRequest.setReason(appointment.getReason());
        appointmentRequest.setPriority(appointment.getPriority());
        appointmentRequest.setNotes(appointment.getNotes());
        selectedDate = appointment.getAppointmentDate();
    }
    
    private Long getPatientIdByName(String patientName) {
        return patients.stream()
            .filter(p -> (p.getFirstName() + " " + p.getLastName()).equals(patientName))
            .findFirst()
            .map(Patient::getPatientId)
            .orElse(null);
    }
    
    private Long getDoctorIdByName(String doctorName) {
        return doctors.stream()
            .filter(d -> (d.getFirstName() + " " + d.getLastName()).equals(doctorName))
            .findFirst()
            .map(Doctor::getDoctorId)
            .orElse(null);
    }
    
    private AppointmentResponseDTO convertToResponseDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        dto.setDoctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
        dto.setDepartment(appointment.getDoctor().getDepartment().getName());
        dto.setAppointmentDate(Date.from(appointment.getAppointmentDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setStatus(appointment.getStatus());
        dto.setReason(appointment.getReason());
        dto.setPriority(appointment.getAppointmentType());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedDate(Date.from(appointment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        return dto;
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and setters
    public AppointmentRequestDTO getAppointmentRequest() { return appointmentRequest; }
    public void setAppointmentRequest(AppointmentRequestDTO appointmentRequest) { this.appointmentRequest = appointmentRequest; }
    
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }
    
    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }
    
    public List<Doctor> getFilteredDoctors() { return filteredDoctors; }
    public void setFilteredDoctors(List<Doctor> filteredDoctors) { this.filteredDoctors = filteredDoctors; }
    
    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }
    
    public Long getSelectedDepartmentId() { return selectedDepartmentId; }
    public void setSelectedDepartmentId(Long selectedDepartmentId) { this.selectedDepartmentId = selectedDepartmentId; }
    
    public Date getSelectedDate() { return selectedDate; }
    public void setSelectedDate(Date selectedDate) { this.selectedDate = selectedDate; }
    
    public String getSelectedTimeSlot() { return selectedTimeSlot; }
    public void setSelectedTimeSlot(String selectedTimeSlot) { this.selectedTimeSlot = selectedTimeSlot; }
    
    public List<String> getAvailableTimeSlots() { return availableTimeSlots; }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { this.availableTimeSlots = availableTimeSlots; }
    
    public List<AppointmentResponseDTO> getAppointments() { return appointments; }
    public void setAppointments(List<AppointmentResponseDTO> appointments) { this.appointments = appointments; }
    
    public List<AppointmentResponseDTO> getTodaysAppointments() { return todaysAppointments; }
    public void setTodaysAppointments(List<AppointmentResponseDTO> todaysAppointments) { this.todaysAppointments = todaysAppointments; }
    
    public List<AppointmentResponseDTO> getUpcomingAppointments() { return upcomingAppointments; }
    public void setUpcomingAppointments(List<AppointmentResponseDTO> upcomingAppointments) { this.upcomingAppointments = upcomingAppointments; }
    
    public AppointmentResponseDTO getSelectedAppointment() { return selectedAppointment; }
    public void setSelectedAppointment(AppointmentResponseDTO selectedAppointment) { this.selectedAppointment = selectedAppointment; }
    
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
    
    public String getStatusFilter() { return statusFilter; }
    public void setStatusFilter(String statusFilter) { this.statusFilter = statusFilter; }
    
    public Date getDateFilter() { return dateFilter; }
    public void setDateFilter(Date dateFilter) { this.dateFilter = dateFilter; }
    
    public boolean isShowCreateDialog() { return showCreateDialog; }
    public void setShowCreateDialog(boolean showCreateDialog) { this.showCreateDialog = showCreateDialog; }
    
    public boolean isShowEditDialog() { return showEditDialog; }
    public void setShowEditDialog(boolean showEditDialog) { this.showEditDialog = showEditDialog; }
    
    public boolean isShowCancelDialog() { return showCancelDialog; }
    public void setShowCancelDialog(boolean showCancelDialog) { this.showCancelDialog = showCancelDialog; }
    
    public boolean isShowRescheduleDialog() { return showRescheduleDialog; }
    public void setShowRescheduleDialog(boolean showRescheduleDialog) { this.showRescheduleDialog = showRescheduleDialog; }
}
