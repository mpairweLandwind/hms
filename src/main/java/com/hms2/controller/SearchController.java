package com.hms2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Appointment;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Staff;
import com.hms2.service.AppointmentService;
import com.hms2.service.DoctorService;
import com.hms2.service.PatientService;
import com.hms2.service.StaffService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("searchController")
@RequestScoped
public class SearchController {
    
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private StaffService staffService;
    
    @Inject
    private AppointmentService appointmentService;
    
    // Search properties
    private String searchQuery;
    private List<SearchResult> searchResults;
    private boolean isSearching;
    private String searchType;
    
    // Search filters
    private boolean includePatients = true;
    private boolean includeDoctors = true;
    private boolean includeStaff = true;
    private boolean includeAppointments = true;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing SearchController");
        searchResults = new ArrayList<>();
        searchType = "all";
    }
    
    /**
     * Perform global search across all entities
     */
    public void performSearch() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            searchResults.clear();
            return;
        }
        
        logger.info("Performing search for query: {}", searchQuery);
        isSearching = true;
        
        try {
            searchResults.clear();
            String query = searchQuery.trim().toLowerCase();
            
            // Search based on selected type or all if not specified
            if ("all".equals(searchType) || includePatients) {
                searchPatients(query);
            }
            
            if ("all".equals(searchType) || includeDoctors) {
                searchDoctors(query);
            }
            
            if ("all".equals(searchType) || includeStaff) {
                searchStaff(query);
            }
            
            if ("all".equals(searchType) || includeAppointments) {
                searchAppointments(query);
            }
            
            logger.info("Search completed. Found {} results", searchResults.size());
            
        } catch (Exception e) {
            logger.error("Error performing search", e);
            addErrorMessage("Error performing search: " + e.getMessage());
        } finally {
            isSearching = false;
        }
    }
    
    /**
     * Search patients by name, email, or phone number
     */
    private void searchPatients(String query) {
        try {
            List<Patient> patients = patientService.searchPatientsByName(query);
            
            // Also search by email and phone if name search doesn't yield results
            if (patients.isEmpty()) {
                patients = patientService.getAllPatients().stream()
                    .filter(p -> (p.getEmail() != null && p.getEmail().toLowerCase().contains(query)) ||
                                (p.getPhoneNumber() != null && p.getPhoneNumber().contains(query)))
                    .collect(Collectors.toList());
            }
            
            for (Patient patient : patients) {
                SearchResult result = new SearchResult();
                result.setId(patient.getPatientId());
                result.setTitle(patient.getFirstName() + " " + patient.getLastName());
                result.setDescription("Patient - " + (patient.getEmail() != null ? patient.getEmail() : "No email"));
                result.setType("patient");
                result.setIcon("pi pi-user");
                result.setUrl("/views/patients/patient-details?patientId=" + patient.getPatientId());
                result.setPriority(1);
                searchResults.add(result);
            }
        } catch (Exception e) {
            logger.error("Error searching patients", e);
        }
    }
    
    /**
     * Search doctors by name, specialization, or license number
     */
    private void searchDoctors(String query) {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors().stream()
                .filter(d -> (d.getFirstName() != null && d.getFirstName().toLowerCase().contains(query)) ||
                            (d.getLastName() != null && d.getLastName().toLowerCase().contains(query)) ||
                            (d.getSpecialization() != null && d.getSpecialization().toLowerCase().contains(query)) ||
                            (d.getLicenseNumber() != null && d.getLicenseNumber().toLowerCase().contains(query)))
                .collect(Collectors.toList());
            
            for (Doctor doctor : doctors) {
                SearchResult result = new SearchResult();
                result.setId(doctor.getDoctorId());
                result.setTitle(doctor.getFirstName() + " " + doctor.getLastName());
                result.setDescription("Doctor - " + (doctor.getSpecialization() != null ? doctor.getSpecialization() : "No specialization"));
                result.setType("doctor");
                result.setIcon("pi pi-user-edit");
                result.setUrl("/views/doctors/doctor-details?doctorId=" + doctor.getDoctorId());
                result.setPriority(2);
                searchResults.add(result);
            }
        } catch (Exception e) {
            logger.error("Error searching doctors", e);
        }
    }
    
    /**
     * Search staff by name, position, or employee ID
     */
    private void searchStaff(String query) {
        try {
            List<Staff> staffList = staffService.getAllStaff().stream()
                .filter(s -> (s.getFirstName() != null && s.getFirstName().toLowerCase().contains(query)) ||
                            (s.getLastName() != null && s.getLastName().toLowerCase().contains(query)) ||
                            (s.getPosition() != null && s.getPosition().toLowerCase().contains(query)) ||
                            (s.getEmployeeId() != null && s.getEmployeeId().toLowerCase().contains(query)))
                .collect(Collectors.toList());
            
            for (Staff staff : staffList) {
                SearchResult result = new SearchResult();
                result.setId(staff.getStaffId());
                result.setTitle(staff.getFirstName() + " " + staff.getLastName());
                result.setDescription("Staff - " + (staff.getPosition() != null ? staff.getPosition() : "No position"));
                result.setType("staff");
                result.setIcon("pi pi-users");
                result.setUrl("/views/staff/staff-details?staffId=" + staff.getStaffId());
                result.setPriority(3);
                searchResults.add(result);
            }
        } catch (Exception e) {
            logger.error("Error searching staff", e);
        }
    }
    
    /**
     * Search appointments by patient name, doctor name, or reason
     */
    private void searchAppointments(String query) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments().stream()
                .filter(a -> (a.getPatient() != null && 
                             (a.getPatient().getFirstName() != null && a.getPatient().getFirstName().toLowerCase().contains(query)) ||
                             (a.getPatient().getLastName() != null && a.getPatient().getLastName().toLowerCase().contains(query))) ||
                            (a.getDoctor() != null && 
                             (a.getDoctor().getFirstName() != null && a.getDoctor().getFirstName().toLowerCase().contains(query)) ||
                             (a.getDoctor().getLastName() != null && a.getDoctor().getLastName().toLowerCase().contains(query))) ||
                            (a.getReason() != null && a.getReason().toLowerCase().contains(query)))
                .collect(Collectors.toList());
            
            for (Appointment appointment : appointments) {
                SearchResult result = new SearchResult();
                result.setId(appointment.getAppointmentId());
                result.setTitle("Appointment #" + appointment.getAppointmentId());
                result.setDescription("Patient: " + (appointment.getPatient() != null ? 
                    appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() : "Unknown") +
                    " | Doctor: " + (appointment.getDoctor() != null ? 
                    appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() : "Unknown"));
                result.setType("appointment");
                result.setIcon("pi pi-calendar");
                result.setUrl("/views/appointments/appointment-details?appointmentId=" + appointment.getAppointmentId());
                result.setPriority(4);
                searchResults.add(result);
            }
        } catch (Exception e) {
            logger.error("Error searching appointments", e);
        }
    }
    
    /**
     * Clear search results
     */
    public void clearSearch() {
        searchQuery = null;
        searchResults.clear();
        searchType = "all";
        logger.info("Search cleared");
    }
    
    /**
     * Get search results count
     */
    public int getSearchResultsCount() {
        return searchResults != null ? searchResults.size() : 0;
    }
    
    /**
     * Check if search has results
     */
    public boolean hasSearchResults() {
        return searchResults != null && !searchResults.isEmpty();
    }
    
    /**
     * Get search results by type
     */
    public List<SearchResult> getSearchResultsByType(String type) {
        if (searchResults == null) {
            return new ArrayList<>();
        }
        return searchResults.stream()
            .filter(result -> type.equals(result.getType()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get recent search suggestions (placeholder for future implementation)
     */
    public List<String> getRecentSearches() {
        // This could be implemented with a database table to store recent searches
        return new ArrayList<>();
    }
    
    /**
     * Get popular search terms (placeholder for future implementation)
     */
    public List<String> getPopularSearches() {
        // This could be implemented with analytics data
        return new ArrayList<>();
    }
    
    /**
     * Navigate to search result
     */
    public String navigateToResult(SearchResult result) {
        logger.info("Navigating to search result: {}", result.getTitle());
        return result.getUrl();
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search Error", message));
    }
    
    // Getters and setters
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    
    public List<SearchResult> getSearchResults() { return searchResults; }
    public void setSearchResults(List<SearchResult> searchResults) { this.searchResults = searchResults; }
    
    public boolean isSearching() { return isSearching; }
    public void setSearching(boolean searching) { isSearching = searching; }
    
    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }
    
    public boolean isIncludePatients() { return includePatients; }
    public void setIncludePatients(boolean includePatients) { this.includePatients = includePatients; }
    
    public boolean isIncludeDoctors() { return includeDoctors; }
    public void setIncludeDoctors(boolean includeDoctors) { this.includeDoctors = includeDoctors; }
    
    public boolean isIncludeStaff() { return includeStaff; }
    public void setIncludeStaff(boolean includeStaff) { this.includeStaff = includeStaff; }
    
    public boolean isIncludeAppointments() { return includeAppointments; }
    public void setIncludeAppointments(boolean includeAppointments) { this.includeAppointments = includeAppointments; }
    
    /**
     * Inner class to represent search results
     */
    public static class SearchResult {
        private Long id;
        private String title;
        private String description;
        private String type;
        private String icon;
        private String url;
        private int priority;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
} 