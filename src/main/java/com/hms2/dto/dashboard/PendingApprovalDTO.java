package com.hms2.dto.dashboard;

import java.util.Date;

public class PendingApprovalDTO {
    
    private Long approvalId;
    private String entityType; // USER, PATIENT, DOCTOR, STAFF, APPOINTMENT, BILLING, PRESCRIPTION, MEDICAL_RECORD
    private Long entityId;
    private String entityName;
    private String entityDescription;
    private String requestType; // REGISTRATION, STATUS_CHANGE, BILLING_APPROVAL, APPOINTMENT_APPROVAL, PRESCRIPTION_APPROVAL
    private String currentStatus;
    private String requestedStatus;
    private String priority; // URGENT, HIGH, NORMAL, LOW
    private String requestedBy;
    private Date requestedDate;
    private String requestedByRole;
    private String approvalNotes;
    private String rejectionReason;
    private Boolean isUrgent;
    private Boolean isDisputed;
    private String disputeReason;
    private Date disputeDate;
    private String disputeStatus;
    
    // Additional context
    private String department;
    private String category;
    private String subCategory;
    private String tags;
    
    // Constructors
    public PendingApprovalDTO() {}
    
    public PendingApprovalDTO(String entityType, Long entityId, String entityName, String requestType) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
        this.requestType = requestType;
        this.requestedDate = new Date();
    }
    
    // Getters and Setters
    public Long getApprovalId() {
        return approvalId;
    }
    
    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public String getEntityName() {
        return entityName;
    }
    
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
    
    public String getEntityDescription() {
        return entityDescription;
    }
    
    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }
    
    public String getRequestType() {
        return requestType;
    }
    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    public String getRequestedStatus() {
        return requestedStatus;
    }
    
    public void setRequestedStatus(String requestedStatus) {
        this.requestedStatus = requestedStatus;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getRequestedBy() {
        return requestedBy;
    }
    
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
    
    public Date getRequestedDate() {
        return requestedDate;
    }
    
    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    public String getRequestedByRole() {
        return requestedByRole;
    }
    
    public void setRequestedByRole(String requestedByRole) {
        this.requestedByRole = requestedByRole;
    }
    
    public String getApprovalNotes() {
        return approvalNotes;
    }
    
    public void setApprovalNotes(String approvalNotes) {
        this.approvalNotes = approvalNotes;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public Boolean getIsUrgent() {
        return isUrgent;
    }
    
    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }
    
    public Boolean getIsDisputed() {
        return isDisputed;
    }
    
    public void setIsDisputed(Boolean isDisputed) {
        this.isDisputed = isDisputed;
    }
    
    public String getDisputeReason() {
        return disputeReason;
    }
    
    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }
    
    public Date getDisputeDate() {
        return disputeDate;
    }
    
    public void setDisputeDate(Date disputeDate) {
        this.disputeDate = disputeDate;
    }
    
    public String getDisputeStatus() {
        return disputeStatus;
    }
    
    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSubCategory() {
        return subCategory;
    }
    
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    // Utility methods
    public String getPriorityColor() {
        if (priority != null) {
            switch (priority.toUpperCase()) {
                case "URGENT":
                    return "danger";
                case "HIGH":
                    return "warning";
                case "NORMAL":
                    return "info";
                case "LOW":
                    return "success";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public String getPriorityIcon() {
        if (priority != null) {
            switch (priority.toUpperCase()) {
                case "URGENT":
                    return "pi pi-exclamation-triangle";
                case "HIGH":
                    return "pi pi-exclamation-circle";
                case "NORMAL":
                    return "pi pi-info-circle";
                case "LOW":
                    return "pi pi-check-circle";
                default:
                    return "pi pi-clock";
            }
        }
        return "pi pi-clock";
    }
    
    public String getEntityTypeIcon() {
        if (entityType != null) {
            switch (entityType.toUpperCase()) {
                case "USER":
                    return "pi pi-user";
                case "PATIENT":
                    return "pi pi-user-plus";
                case "DOCTOR":
                    return "pi pi-user-md";
                case "STAFF":
                    return "pi pi-users";
                case "APPOINTMENT":
                    return "pi pi-calendar";
                case "BILLING":
                    return "pi pi-dollar";
                case "PRESCRIPTION":
                    return "pi pi-file-text";
                case "MEDICAL_RECORD":
                    return "pi pi-file";
                default:
                    return "pi pi-file";
            }
        }
        return "pi pi-file";
    }
    
    public String getEntityTypeColor() {
        if (entityType != null) {
            switch (entityType.toUpperCase()) {
                case "USER":
                    return "primary";
                case "PATIENT":
                    return "success";
                case "DOCTOR":
                    return "info";
                case "STAFF":
                    return "warning";
                case "APPOINTMENT":
                    return "secondary";
                case "BILLING":
                    return "danger";
                case "PRESCRIPTION":
                    return "dark";
                case "MEDICAL_RECORD":
                    return "light";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public Boolean isOverdue() {
        if (requestedDate != null && isUrgent != null && isUrgent) {
            // Check if urgent request is older than 24 hours
            long diffInMillies = System.currentTimeMillis() - requestedDate.getTime();
            long diffInHours = diffInMillies / (60 * 60 * 1000);
            return diffInHours > 24;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "PendingApprovalDTO{" +
                "approvalId=" + approvalId +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", entityName='" + entityName + '\'' +
                ", requestType='" + requestType + '\'' +
                ", priority='" + priority + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                ", requestedDate=" + requestedDate +
                ", isUrgent=" + isUrgent +
                '}';
    }
} 