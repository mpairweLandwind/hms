package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

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

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE, or OTHER")
    @Column(name = "gender", length = 10)
    private String gender;

    @Size(max = 100, message = "Emergency contact must not exceed 100 characters")
    @Column(name = "emergency_contact", length = 100)
    private String emergencyContact;

    @Pattern(regexp = "\\d{10,15}", message = "Emergency phone must be 10-15 digits")
    @Column(name = "emergency_phone", length = 15)
    private String emergencyPhone;

    @Size(max = 50, message = "Insurance number must not exceed 50 characters")
    @Column(name = "insurance_number", length = 50)
    private String insuranceNumber;

    @Pattern(regexp = "A\\+|A-|B\\+|B-|AB\\+|AB-|O\\+|O-", message = "Invalid blood type")
    @Column(name = "blood_type", length = 5)
    private String bloodType;

    @Size(max = 500, message = "Allergies must not exceed 500 characters")
    @Column(name = "allergies", length = 500)
    private String allergies;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // One-to-many relationships with optimized cascade
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Billing> billings;

    // Constructors
    public Patient() {}

    public Patient(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return java.time.Period.between(
            dateOfBirth.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
            java.time.LocalDate.now()
        ).getYears();
    }

    public boolean hasAllergies() {
        return allergies != null && !allergies.trim().isEmpty();
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Backward compatibility method
    public Long getPatientId() {
        return getId();
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
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

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
    }

    public Doctor getPrimaryDoctor() {
        if (appointments != null && !appointments.isEmpty()) {
            // Sort appointments by date in descending order to find the most recent one
            appointments.sort((a1, a2) -> a2.getAppointmentDateTime().compareTo(a1.getAppointmentDateTime()));
            // Return the doctor from the most recent appointment
            return appointments.get(0).getDoctor();
        }
        return null; // Or handle as needed if there are no appointments
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}
