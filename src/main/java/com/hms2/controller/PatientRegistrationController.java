package com.hms2.controller;

import com.hms2.dto.user.PatientRegistrationDTO;
import com.hms2.service.RegistrationService;
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

@Named("patientRegistrationController")
@ViewScoped
public class PatientRegistrationController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientRegistrationController.class);
    
    @Inject
    private RegistrationService registrationService;
    
    private PatientRegistrationDTO registrationDTO;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing patient registration controller");
        registrationDTO = new PatientRegistrationDTO();
    }
    
    public void registerPatient() {
        try {
            logger.info("Registering patient: {}", registrationDTO.getEmail());
            
            // Set role
            registrationDTO.setRole("PATIENT");
            
            // Convert to UserRegistrationDTO and register
            var userRegistrationDTO = convertToUserRegistrationDTO();
            registrationService.registerUser(userRegistrationDTO);
            
            addSuccessMessage("Patient account created successfully! Please check your email for verification.");
            
            // Add success flag for JavaScript
            PrimeFaces.current().ajax().addCallbackParam("success", true);
            
            // Reset form
            registrationDTO = new PatientRegistrationDTO();
            
        } catch (Exception e) {
            logger.error("Error registering patient", e);
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
    
    private com.hms2.dto.UserRegistrationDTO convertToUserRegistrationDTO() {
        var dto = new com.hms2.dto.UserRegistrationDTO();
        
        // Account info
        dto.setUsername(registrationDTO.getUsername());
        dto.setEmail(registrationDTO.getEmail());
        dto.setPassword(registrationDTO.getPassword());
        dto.setRole("PATIENT");
        
        // Personal info
        dto.setFirstName(registrationDTO.getFirstName());
        dto.setLastName(registrationDTO.getLastName());
        dto.setPhoneNumber(registrationDTO.getPhoneNumber());
        dto.setAddress(registrationDTO.getAddress());
        
        // Patient specific
        dto.setDateOfBirth(registrationDTO.getDateOfBirth());
        dto.setGender(registrationDTO.getGender());
        dto.setEmergencyContact(registrationDTO.getEmergencyContact());
        dto.setEmergencyPhone(registrationDTO.getEmergencyPhone());
        dto.setInsuranceNumber(registrationDTO.getInsuranceNumber());
        dto.setBloodType(registrationDTO.getBloodType());
        dto.setAllergies(registrationDTO.getAllergies());
        
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
    public PatientRegistrationDTO getRegistrationDTO() {
        return registrationDTO;
    }
    
    public void setRegistrationDTO(PatientRegistrationDTO registrationDTO) {
        this.registrationDTO = registrationDTO;
    }
}
