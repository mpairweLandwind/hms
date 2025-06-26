package com.hms2.dto.user;

import jakarta.validation.constraints.*;
import java.util.Date;

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
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be 10-15 digits")
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
    
    @Min(value = 0, message = "Experience must be non-negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experience;
    
    @Size(max = 500, message = "Qualifications must not exceed 500 characters")
    private String qualifications;
    
    private Long departmentId;
    
    // Professional documents (file uploads would be handled separately)
    private String medicalLicensePath;
    private String degreeCertificatePath;
    private String cvPath;
    
    // Terms and conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean acceptTerms = false;
    
    @AssertTrue(message = "You must accept the privacy policy")
    private boolean acceptPrivacy = false;
    
    @AssertTrue(message = "You must confirm the accuracy of professional information")
    private boolean confirmAccuracy = false;
    
    // Constructors
    public DoctorRegistrationDTO() {}
    
    // Validation methods
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public Integer getExperience() {
        return experience;
    }
    
    public void setExperience(Integer experience) {
        this.experience = experience;
    }
    
    public String getQualifications() {
        return qualifications;
    }
    
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    
    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getMedicalLicensePath() {
        return medicalLicensePath;
    }
    
    public void setMedicalLicensePath(String medicalLicensePath) {
        this.medicalLicensePath = medicalLicensePath;
    }
    
    public String getDegreeCertificatePath() {
        return degreeCertificatePath;
    }
    
    public void setDegreeCertificatePath(String degreeCertificatePath) {
        this.degreeCertificatePath = degreeCertificatePath;
    }
    
    public String getCvPath() {
        return cvPath;
    }
    
    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }
    
    public boolean isAcceptTerms() {
        return acceptTerms;
    }
    
    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }
    
    public boolean isAcceptPrivacy() {
        return acceptPrivacy;
    }
    
    public void setAcceptPrivacy(boolean acceptPrivacy) {
        this.acceptPrivacy = acceptPrivacy;
    }
    
    public boolean isConfirmAccuracy() {
        return confirmAccuracy;
    }
    
    public void setConfirmAccuracy(boolean confirmAccuracy) {
        this.confirmAccuracy = confirmAccuracy;
    }
}
