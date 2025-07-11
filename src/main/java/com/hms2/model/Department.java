package com.hms2.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    @Column(name = "department_name", nullable = false, unique = true, length = 100)
    private String departmentName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    @Column(name = "location", length = 100)
    private String location;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits (can start with + for international numbers)")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Pattern(regexp = "ACTIVE|INACTIVE|UNDER_MAINTENANCE", 
             message = "Status must be ACTIVE, INACTIVE, or UNDER_MAINTENANCE")
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    // One-to-many relationships
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Staff> staff;

    // Constructors
    public Department() {}

    public Department(String departmentName, String description, String location) {
        this.departmentName = departmentName;
        this.description = description;
        this.location = location;
    }

    // Business methods
    public boolean isActive() {
        return "ACTIVE".equals(status);  //&& !isDeleted();
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }

    public void setUnderMaintenance() {
        this.status = "UNDER_MAINTENANCE";
    }

    // Getters and setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public void setStaff(List<Staff> staff) {
        this.staff = staff;
    }
}
