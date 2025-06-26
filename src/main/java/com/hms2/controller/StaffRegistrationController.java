package com.hms2.controller;

import com.hms2.dto.user.StaffRegistrationDTO;
import com.hms2.model.Department;
import com.hms2.service.RegistrationService;
import com.hms2.service.DepartmentService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named("staffRegistrationController")
@ViewScoped
public class StaffRegistrationController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffRegistrationController.class);
    
    @Inject
    private RegistrationService registrationService;
    
    @Inject
    private DepartmentService departmentService;
    
    private StaffRegistrationDTO registrationDTO;
    private List<Department> departments;
    private Date today = new Date();
    
    @PostConstruct
    public void init() {
        logger.info("Initializing staff registration controller");
        registrationDTO = new StaffRegistrationDTO();
        loadDepartments();
    }
    
    private void loadDepartments() {
        try {
            departments = departmentService.getAllDepartments();
            logger.info("Loaded {} departments", departments.size());
        } catch (Exception e) {
            logger.error("Error loading departments", e);
            addErrorMessage("Error loading departments: " + e.getMessage());
        }
    }
    
    public void registerStaff() {
        try {
            logger.info("Registering staff: {}", registrationDTO.getEmail());
            
            // Set role
            registrationDTO.setRole("STAFF");
            
            // Convert to UserRegistrationDTO and register
            var userRegistrationDTO = convertToUserRegistrationDTO();
            registrationService.registerUser(userRegistrationDTO);
            
            addSuccessMessage("Staff application submitted successfully! HR will review your application and contact you soon.");
            
            // Add success flag for JavaScript
            PrimeFaces.current().ajax().addCallbackParam("success", true);
            
            // Reset form
            registrationDTO = new StaffRegistrationDTO();
            
        } catch (Exception e) {
            logger.error("Error registering staff", e);
            addErrorMessage("Registration failed: " + e.getMessage());
            PrimeFaces.current().ajax().addCallbackParam("success", false);
        }
    }
    
    public void validateUsername() {
        if (registrationDTO.getUsername() != null && !registrationDTO.getUsername().trim().isEmpty()) {
            if (!registrationService.isUsernameUnique(registrationDTO.getUsername())) {
                addErrorMessage("Username already exists");
            }
        }
    }
    
    public void validateEmail() {
        if (registrationDTO.getEmail() != null && !registrationDTO.getEmail().trim().isEmpty()) {
            if (!registrationService.isEmailUnique(registrationDTO.getEmail())) {
                addErrorMessage("Email already exists");
            }
        }
    }
    
    public void validateEmployeeId() {
        if (registrationDTO.getEmployeeId() != null && !registrationDTO.getEmployeeId().trim().isEmpty()) {
            if (!registrationService.isEmployeeIdUnique(registrationDTO.getEmployeeId())) {
                addErrorMessage("Employee ID already exists");
            }
        }
    }
    
    private com.hms2.dto.UserRegistrationDTO convertToUserRegistrationDTO() {
        var dto = new com.hms2.dto.UserRegistrationDTO();
        
        // Account info
        dto.setUsername(registrationDTO.getUsername());
        dto.setEmail(registrationDTO.getEmail());
        dto.setPassword(registrationDTO.getPassword());
        dto.setRole("STAFF");
        
        // Personal info
        dto.setFirstName(registrationDTO.getFirstName());
        dto.setLastName(registrationDTO.getLastName());
        dto.setPhoneNumber(registrationDTO.getPhoneNumber());
        dto.setAddress(registrationDTO.getAddress());
        
        // Staff specific
        dto.setPosition(registrationDTO.getPosition());
        dto.setEmployeeId(registrationDTO.getEmployeeId());
        dto.setHireDate(registrationDTO.getHireDate());
        dto.setSalary(registrationDTO.getSalary());
        dto.setNotes(registrationDTO.getNotes());
        dto.setDepartmentId(registrationDTO.getDepartmentId());
        dto.setStatus(registrationDTO.getStatus());
        
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
    public StaffRegistrationDTO getRegistrationDTO() {
        return registrationDTO;
    }
    
    public void setRegistrationDTO(StaffRegistrationDTO registrationDTO) {
        this.registrationDTO = registrationDTO;
    }
    
    public List<Department> getDepartments() {
        return departments;
    }
    
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    
    public Date getToday() {
        return today;
    }
}
