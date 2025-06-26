package com.hms2.dto.user;

import com.hms2.enums.StaffStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

public class StaffRegistrationDTO {
    
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
    
    // Staff specific fields
    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position must not exceed 100 characters")
    private String position;
    
    @NotBlank(message = "Employee ID is required")
    @Size(max = 20, message = "Employee ID must not exceed 20 characters")
    private String employeeId;
    
    @NotNull(message = "Hire date is required")
    private Date hireDate;
    
    @DecimalMin(value = "0.0", message = "Salary must be positive")
    @DecimalMax(value = "999999.99", message = "Salary cannot exceed 999,999.99")
    private BigDecimal salary;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    private Long departmentId;
    
    private StaffStatus status = StaffStatus.PENDING_VERIFICATION;
    
    // Employment documents (file uploads would be handled separately)
    private String resumePath;
    private String certificatesPath;
    private String referencesPath;
    
    // Terms and conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean acceptTerms = false;
    
    @AssertTrue(message = "You must accept the privacy policy")
    private boolean acceptPrivacy = false;
    
    @AssertTrue(message = "You must accept the employment agreement")
    private boolean acceptEmployment = false;
    
    // Constructors
    public StaffRegistrationDTO() {}
    
    // Validation methods
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
    
    @AssertTrue(message = "Hire date cannot be in the future")
    public boolean isHireDateValid() {
        return hireDate == null || !hireDate.after(new Date());
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
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public Date getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
    public StaffStatus getStatus() {
        return status;
    }
    
    public void setStatus(StaffStatus status) {
        this.status = status;
    }
    
    public String getResumePath() {
        return resumePath;
    }
    
    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
    
    public String getCertificatesPath() {
        return certificatesPath;
    }
    
    public void setCertificatesPath(String certificatesPath) {
        this.certificatesPath = certificatesPath;
    }
    
    public String getReferencesPath() {
        return referencesPath;
    }
    
    public void setReferencesPath(String referencesPath) {
        this.referencesPath = referencesPath;
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
    
    public boolean isAcceptEmployment() {
        return acceptEmployment;
    }
    
    public void setAcceptEmployment(boolean acceptEmployment) {
        this.acceptEmployment = acceptEmployment;
    }
}
