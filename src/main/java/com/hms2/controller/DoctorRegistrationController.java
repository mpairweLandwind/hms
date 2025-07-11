package com.hms2.controller;

import java.util.List;

import java.io.Serializable;

import com.hms2.dto.user.DoctorRegistrationDTO;
import com.hms2.model.Department;
import com.hms2.service.DepartmentService;
import com.hms2.service.RegistrationService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("doctorRegistrationController")
@ViewScoped
public class DoctorRegistrationController implements Serializable {

    
    @Inject
    private RegistrationService registrationService;

    @Inject
    private DepartmentService departmentService;

    private DoctorRegistrationDTO registrationDTO;
    private List<Department> departments;
    private String formToken = java.util.UUID.randomUUID().toString();

    // Remove multi-step navigation logic (currentStep, nextStep, previousStep, isStepXActive, showXButton, etc.)

    @PostConstruct
    public void init() {
        System.err.println("Initializing doctor registration controller");
        registrationDTO = new DoctorRegistrationDTO();
        loadDepartments();
    }

    private void loadDepartments() {
        try {
            departments = departmentService.getAllDepartments();
            System.err.println("[DoctorRegistrationController] Loaded " + departments.size() + " departments");
        } catch (Exception e) {
            System.err.println("[DoctorRegistrationController] Error loading departments: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Error loading departments: " + e.getMessage());
        }
    }

    public String registerDoctor() {
        System.err.println("[DoctorRegistrationController] Doctor registration started for username: " + registrationDTO.getUsername());
        try {
            System.err.println("[DoctorRegistrationController] Registering doctor: " + registrationDTO.getEmail());

            // Complete registration process - creates both User and Doctor simultaneously
            registrationService.registerDoctor(registrationDTO);
            System.err.println("[DoctorRegistrationController] Doctor registration successful for username: " + registrationDTO.getUsername());

            addSuccessMessage("Doctor application submitted successfully! We will review your credentials and contact you within 2-3 business days.");

            // Redirect to index page on success
            return "/index.xhtml?faces-redirect=true";

        } catch (Exception e) {
            System.err.println("[DoctorRegistrationController] Doctor registration failed for username: " + registrationDTO.getUsername() + ", error: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Registration failed: " + e.getMessage());

            // Stay on the same page if there's an error
            return null;
        }
    }

    public String processDoctorRegistration() {
        System.err.println("[DoctorRegistrationController] Processing doctor registration DTO for username: " + registrationDTO.getUsername());
        try {
            // Validate the DTO
            if (!validateRegistrationDTO()) {
                System.err.println("[DoctorRegistrationController] DTO validation failed for username: " + registrationDTO.getUsername());
                addErrorMessage("Please correct the validation errors before submitting.");
                return null;
            }
            
            // Process the registration using the DTO
            return registerDoctor();
            
        } catch (Exception e) {
            System.err.println("[DoctorRegistrationController] Error processing doctor registration DTO: " + e.getMessage());
            addErrorMessage("Error processing registration: " + e.getMessage());
            return null;
        }
    }
    
    private boolean validateRegistrationDTO() {
        // Add custom validation logic for the DTO
        if (registrationDTO == null) {
            addErrorMessage("Registration data is missing.");
            return false;
        }
        
        // Check required fields
        if (registrationDTO.getUsername() == null || registrationDTO.getUsername().trim().isEmpty()) {
            addErrorMessage("Username is required.");
            return false;
        }
        
        if (registrationDTO.getEmail() == null || registrationDTO.getEmail().trim().isEmpty()) {
            addErrorMessage("Email is required.");
            return false;
        }
        
        if (registrationDTO.getPassword() == null || registrationDTO.getPassword().trim().isEmpty()) {
            addErrorMessage("Password is required.");
            return false;
        }
        
        if (registrationDTO.getFirstName() == null || registrationDTO.getFirstName().trim().isEmpty()) {
            addErrorMessage("First name is required.");
            return false;
        }
        
        if (registrationDTO.getLastName() == null || registrationDTO.getLastName().trim().isEmpty()) {
            addErrorMessage("Last name is required.");
            return false;
        }
        
        if (registrationDTO.getSpecialization() == null || registrationDTO.getSpecialization().trim().isEmpty()) {
            addErrorMessage("Specialization is required.");
            return false;
        }
        
        if (registrationDTO.getLicenseNumber() == null || registrationDTO.getLicenseNumber().trim().isEmpty()) {
            addErrorMessage("License number is required.");
            return false;
        }
        
        if (registrationDTO.getDepartment() == null || registrationDTO.getDepartment().trim().isEmpty()) {
            addErrorMessage("Department is required.");
            return false;
        }
        
        // Check terms and conditions
        if (!registrationDTO.isAcceptTerms()) {
            addErrorMessage("You must accept the terms and conditions.");
            return false;
        }
        
        if (!registrationDTO.isAcceptPrivacy()) {
            addErrorMessage("You must accept the privacy policy.");
            return false;
        }
        
        if (!registrationDTO.isProfessionalConsent()) {
            addErrorMessage("You must confirm the accuracy of your professional information.");
            return false;
        }
        
        return true;
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

    public String getFormToken() {
        return formToken;
    }

    public void setFormToken(String formToken) {
        this.formToken = formToken;
    }
}
