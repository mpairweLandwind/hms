package com.hms2.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DoctorRegistrationDTO {
    
    // User account fields
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    
    // Personal information
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    
       @NotBlank(message = "Phone number is required")
       private String phoneNumber;
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    // Doctor specific fields
    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;
    
    @NotBlank(message = "License number is required")
    @Size(max = 50, message = "License number must not exceed 50 characters")
    private String licenseNumber;
    
    @NotBlank(message = "Qualifications is required")
    @Size(max = 200, message = "Qualifications must not exceed 200 characters")
    private String qualifications;
    
    @Min(value = 0, message = "Experience must be non-negative")
    @Max(value = 99, message = "Experience cannot exceed 99 years")
    private Integer experience;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    // Terms and conditions
    private boolean acceptTerms;
    private boolean acceptPrivacy;
    private boolean professionalConsent;

    // Constructors
    public DoctorRegistrationDTO() {}
    
    // Validation methods
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        boolean match = password != null && password.equals(confirmPassword);
        System.err.println("isPasswordMatching: " + match);
        return match;
    }
    
    @AssertTrue(message = "You must accept the terms and conditions")
    public boolean isTermsAccepted() { 
        System.err.println("isTermsAccepted: " + acceptTerms);
        return acceptTerms; 
    }
    
    @AssertTrue(message = "You must accept the privacy policy")
    public boolean isPrivacyAccepted() { 
        System.err.println("isPrivacyAccepted: " + acceptPrivacy);
        return acceptPrivacy; 
    }
    
    @AssertTrue(message = "You must confirm the accuracy of professional information")
    public boolean isProfessionalConsentGiven() { 
        System.err.println("isProfessionalConsentGiven: " + professionalConsent);
        return professionalConsent; 
    }
    
    // Getters and setters for terms
    public boolean isAcceptTerms() { 
        return acceptTerms; 
    }
    
    public void setAcceptTerms(boolean acceptTerms) { 
        System.err.println("setAcceptTerms: " + acceptTerms);
        this.acceptTerms = acceptTerms; 
    }
    
    public boolean isAcceptPrivacy() { 
        return acceptPrivacy; 
    }
    
    public void setAcceptPrivacy(boolean acceptPrivacy) { 
        System.err.println("setAcceptPrivacy: " + acceptPrivacy);
        this.acceptPrivacy = acceptPrivacy; 
    }
    
    public boolean isProfessionalConsent() { 
        return professionalConsent; 
    }
    
    public void setProfessionalConsent(boolean professionalConsent) { 
        System.err.println("setProfessionalConsent: " + professionalConsent);
        this.professionalConsent = professionalConsent; 
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        System.err.println("setUsername: " + username);
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        System.err.println("setEmail: " + email);
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        System.err.println("setPassword: " + password);
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        System.err.println("setConfirmPassword: " + confirmPassword);
        this.confirmPassword = confirmPassword;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        System.err.println("setFirstName: " + firstName);
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        System.err.println("setLastName: " + lastName);
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        System.err.println("setPhoneNumber: " + phoneNumber);
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        System.err.println("setAddress: " + address);
        this.address = address;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        System.err.println("setSpecialization: " + specialization);
        this.specialization = specialization;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        System.err.println("setLicenseNumber: " + licenseNumber);
        this.licenseNumber = licenseNumber;
    }
    
    public String getQualifications() {
        return qualifications;
    }
    
    public void setQualifications(String qualifications) {
        System.err.println("setQualifications: " + qualifications);
        this.qualifications = qualifications;
    }
    
    public Integer getExperience() {
        return experience;
    }
    
    public void setExperience(Integer experience) {
        System.err.println("setExperience: " + experience);
        this.experience = experience;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        System.err.println("setDepartment: " + department);
        this.department = department;
    }
}
