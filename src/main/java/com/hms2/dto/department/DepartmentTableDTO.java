package com.hms2.dto.department;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DepartmentTableDTO {
    private Long departmentId;
    
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String departmentName;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits (can start with + for international numbers)")
    private String phoneNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Pattern(regexp = "ACTIVE|INACTIVE|UNDER_MAINTENANCE", 
             message = "Status must be ACTIVE, INACTIVE, or UNDER_MAINTENANCE")
    private String status = "ACTIVE";
    
    private Boolean isDeleted = false;
    
    private Long version;

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public Long getId() { return getDepartmentId(); }
    public void setId(Long id) { setDepartmentId(id); }

    public String getName() {
        return getDepartmentName();
    }

    public void setName(String name) {
        setDepartmentName(name);
    }
} 