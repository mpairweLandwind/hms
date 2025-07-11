package com.hms2.dto.user;

import com.hms2.enums.StaffStatus;

public class StaffTableDTO {
    private Long staffId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String position;
    private StaffStatus status;
    private Boolean isDeleted;
    // Add more fields as needed for display/editing

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public StaffStatus getStatus() { return status; }
    public void setStatus(StaffStatus status) { this.status = status; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
} 