package com.hms2.model;

import java.util.List;

import com.hms2.enums.DoctorStatus;
import com.hms2.converter.DoctorStatusConverter; // <--- Import your converter

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Convert; // <--- Import the @Convert annotation

// Jakarta Validation constraints (keep these as they are for bean validation)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity { // Assuming BaseEntity handles ID and timestamps (like createdAt, updatedAt)

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

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    @NotBlank(message = "License number is required")
    @Size(max = 50, message = "License number must not exceed 50 characters")
    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Min(value = 0, message = "Experience must be non-negative")
    @Column(name = "experience")
    private Integer experience;

    @Size(max = 500, message = "Qualifications must not exceed 500 characters")
    @Column(name = "qualifications", length = 500)
    private String qualifications;

 
    @Convert(converter = DoctorStatusConverter.class) // <--- Use your custom converter
    @Column(name = "status", nullable = false, length = 30) // <--- Ensure length is sufficient for display text
    private DoctorStatus status = DoctorStatus.PENDING_VERIFICATION; // Default value

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // Many-to-one relationship with Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // One-to-many relationships with optimized cascade
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    // Constructors
    public Doctor() {
        // Default constructor
    }

    // Ensure your constructors align with your actual needs
    public Doctor(String firstName, String lastName, String email, String specialization, String licenseNumber, User user, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.user = user; // Associate with User
        this.department = department; // Associate with Department
        this.status = DoctorStatus.PENDING_VERIFICATION; // Set default status for new Doctor
        this.active = true;
        // BaseEntity fields (createdAt, updatedAt) will be handled by lifecycle callbacks
    }


    // Business methods
    public String getFullName() {
        return "Dr. " + firstName + " " + lastName;
    }

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

    // Getter and Setter for status are crucial for Hibernate/JPA
    public DoctorStatus getStatus() {
        return status;
    }

    public void setStatus(DoctorStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active != null && active && super.isActive(); // Assuming BaseEntity has an isActive
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    // Backward compatibility method
    public Long getDoctorId() {
        return getId(); // Assuming getId() comes from BaseEntity
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() + // Assuming getId() from BaseEntity
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", status=" + status.getText() + // <--- Use getText() for readable output
                ", active=" + active +
                '}';
    }
}