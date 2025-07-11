package com.hms2.controller;

import java.io.Serializable;

import com.hms2.dto.user.PatientRegistrationDTO;
import com.hms2.service.RegistrationService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("patientRegistrationController")
@ViewScoped
public class PatientRegistrationController implements Serializable {

    @Inject
    private RegistrationService registrationService;

    private PatientRegistrationDTO registrationDTO;

    @PostConstruct
    public void init() {
        System.err.println("Initializing patient registration controller");
        registrationDTO = new PatientRegistrationDTO();
    }

    public String registerPatient() {
        try {
            System.err.println("Registering patient: " + registrationDTO.getEmail());

            // Complete registration process - creates both User and Patient simultaneously
            registrationService.registerPatient(registrationDTO);

            addSuccessMessage("Patient account created successfully! Please log in.");
            
            // Redirect to the same registration page on success
            return "patient-registration?faces-redirect=true";

        } catch (Exception e) {
            System.err.println("Error registering patient: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Registration failed: " + e.getMessage());
            
            // Stay on the same page if there's an error
            return null;
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

    // Helper methods for UI
    public String[] getGenders() {
        return new String[]{"MALE", "FEMALE", "OTHER"};
    }

    public String[] getBloodTypes() {
        return new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    }
}
