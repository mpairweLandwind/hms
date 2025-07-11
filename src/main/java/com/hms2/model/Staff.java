package com.hms2.model;

import java.math.BigDecimal;
import java.util.Date;

import com.hms2.enums.StaffStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "staff")
public class Staff extends BaseEntity {

    // One-to-one relationship with User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be 10-15 digits")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", length = 255)
    private String address;

    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position must not exceed 100 characters")
    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @NotBlank(message = "Employee ID is required")
    @Size(max = 20, message = "Employee ID must not exceed 20 characters")
    @Column(name = "employee_id", nullable = false, unique = true, length = 20)
    private String employeeId;

    @Temporal(TemporalType.DATE)
    @Column(name = "hire_date")
    private Date hireDate;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StaffStatus status = StaffStatus.PENDING_VERIFICATION;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // Many-to-one relationship with Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // Constructors
    public Staff() {}

    public Staff(String firstName, String lastName, String email, String position, String employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.employeeId = employeeId;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isVerified() {
        return StaffStatus.VERIFIED.equals(status);
    }

    public boolean isPendingVerification() {
        return StaffStatus.PENDING_VERIFICATION.equals(status);
    }

    public void verify() {
        this.status = StaffStatus.VERIFIED;
        this.active = true;
    }

    public void reject() {
        this.status = StaffStatus.REJECTED;
        this.active = false;
    }

    public void suspend() {
        this.status = StaffStatus.SUSPENDED;
        this.active = false;
    }

    public void terminate() {
        this.status = StaffStatus.TERMINATED;
        this.active = false;
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public StaffStatus getStatus() {
        return status;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active != null && active && super.isActive();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // Backward compatibility method
    public Long getStaffId() {
        return getId();
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", status=" + status +
                ", active=" + active +
                '}';
    }
}
