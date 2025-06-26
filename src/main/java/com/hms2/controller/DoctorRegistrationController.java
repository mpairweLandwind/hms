package com.hms2.controller;

import com.hms2.dto.user.DoctorRegistrationDTO;
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
import java.util.List;

@Named("doctorRegistrationController")
@ViewScoped
public class DoctorRegistrationController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorRegistrationController.class);
    
    @Inject
    private RegistrationService registrationService;
    
    @Inject
    private DepartmentService departmentService;
    
    private DoctorRegistrationDTO registrationDTO;
    private List<Department> departments;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing doctor registration controller");
        registrationDTO = new DoctorRegistrationDTO();
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
    
    public void registerDoctor() {
        try {
            logger.info("Registering doctor: {}", registrationDTO.getEmail());
            
            // Set role
            registrationDTO.setRole("DOCTOR");
            
            // Convert to UserRegistrationDTO and register
            var userRegistrationDTO = convertToUserRegistrationDTO();
            registrationService.registerUser(userRegistrationDTO);
            
            addSuccessMessage("Doctor application submitted successfully! We will review your credentials and contact you within 2-3 business days.");
            
            // Add success flag for JavaScript
            PrimeFaces.current().ajax().addCallbackParam("success", true);
            
            // Reset form
            registrationDTO = new DoctorRegistrationDTO();
            
        } catch (Exception e) {
            logger.error("Error registering doctor", e);
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
    
    public void validateLicenseNumber() {
        if (registrationDTO.getLicenseNumber() != null && !registrationDTO.getLicenseNumber().trim().isEmpty()) {
            if (!registrationService.isLicenseNumberUnique(registrationDTO.getLicenseNumber())) {
                addErrorMessage("License number already exists");
            }
        }
    }
    
    private com.hms2.dto.UserRegistrationDTO convertToUserRegistrationDTO() {
        var dto = new com.hms2.dto.UserRegistrationDTO();
        
        // Account info
        dto.setUsername(registrationDTO.getUsername());
        dto.setEmail(registrationDTO.getEmail());
        dto.setPassword(registrationDTO.getPassword());
        dto.setRole("DOCTOR");
        
        // Personal info
        dto.setFirstName(registrationDTO.getFirstName());
        dto.setLastName(registrationDTO.getLastName());
        dto.setPhoneNumber(registrationDTO.getPhoneNumber());
        dto.setAddress(registrationDTO.getAddress());
        
        // Doctor specific
        dto.setSpecialization(registrationDTO.getSpecialization());
        dto.setLicenseNumber(registrationDTO.getLicenseNumber());
        dto.setExperience(registrationDTO.getExperience());
        dto.setQualifications(registrationDTO.getQualifications());
        dto.setDepartmentId(registrationDTO.getDepartmentId());
        
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
    public DoctorRegistrationDTO getRegistrationDTO() {
        return registrationDTO;
    }
    
    public void setRegistrationDTO(DoctorRegistrationDTO registrationDTO) {
        this.registrationDTO = registrationDTO;
    }
    
    public List<Department> getDepartments() {
        return departments;
    }
    
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
