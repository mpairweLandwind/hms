package com.hms2.dto.dashboard;

import java.util.Date;

public class RecentActivityDTO {
    
    private Long activityId;
    private String activityType; // LOGIN, LOGOUT, CREATE, UPDATE, DELETE, APPROVE, REJECT, BILLING, APPOINTMENT, PRESCRIPTION
    private String entityType; // USER, PATIENT, DOCTOR, STAFF, APPOINTMENT, BILLING, PRESCRIPTION, MEDICAL_RECORD
    private Long entityId;
    private String entityName;
    private String description;
    private String performedBy;
    private String performedByRole;
    private Date performedDate;
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    
    // Additional context
    private String oldValue;
    private String newValue;
    private String changes;
    private String department;
    private String category;
    private String subCategory;
    private String tags;
    
    // Status and priority
    private String status; // SUCCESS, FAILED, PENDING, CANCELLED
    private String priority; // HIGH, NORMAL, LOW
    private Boolean isImportant;
    private Boolean isSensitive;
    
    // Constructors
    public RecentActivityDTO() {}
    
    public RecentActivityDTO(String activityType, String entityType, Long entityId, String description, String performedBy) {
        this.activityType = activityType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.performedBy = performedBy;
        this.performedDate = new Date();
    }
    
    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public String getPerformedByRole() {
        return performedByRole;
    }
    
    public void setPerformedByRole(String performedByRole) {
        this.performedByRole = performedByRole;
    }
    
    public Date getPerformedDate() {
        return performedDate;
    }
    
    public void setPerformedDate(Date performedDate) {
        this.performedDate = performedDate;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getChanges() {
        return changes;
    }
    
    public void setChanges(String changes) {
        this.changes = changes;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Boolean getIsImportant() {
        return isImportant;
    }
    
    public void setIsImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }
    
    public Boolean getIsSensitive() {
        return isSensitive;
    }
    
    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }
    
    // Utility methods
    public String getActivityTypeIcon() {
        if (activityType != null) {
            switch (activityType.toUpperCase()) {
                case "LOGIN":
                    return "pi pi-sign-in";
                case "LOGOUT":
                    return "pi pi-sign-out";
                case "CREATE":
                    return "pi pi-plus-circle";
                case "UPDATE":
                    return "pi pi-pencil";
                case "DELETE":
                    return "pi pi-trash";
                case "APPROVE":
                    return "pi pi-check-circle";
                case "REJECT":
                    return "pi pi-times-circle";
                case "BILLING":
                    return "pi pi-dollar";
                case "APPOINTMENT":
                    return "pi pi-calendar";
                case "PRESCRIPTION":
                    return "pi pi-file-text";
                default:
                    return "pi pi-info-circle";
            }
        }
        return "pi pi-info-circle";
    }
    
    public String getActivityTypeColor() {
        if (activityType != null) {
            switch (activityType.toUpperCase()) {
                case "LOGIN":
                    return "success";
                case "LOGOUT":
                    return "secondary";
                case "CREATE":
                    return "success";
                case "UPDATE":
                    return "info";
                case "DELETE":
                    return "danger";
                case "APPROVE":
                    return "success";
                case "REJECT":
                    return "danger";
                case "BILLING":
                    return "warning";
                case "APPOINTMENT":
                    return "primary";
                case "PRESCRIPTION":
                    return "dark";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public String getStatusColor() {
        if (status != null) {
            switch (status.toUpperCase()) {
                case "SUCCESS":
                    return "success";
                case "FAILED":
                    return "danger";
                case "PENDING":
                    return "warning";
                case "CANCELLED":
                    return "secondary";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public String getTimeAgo() {
        if (performedDate != null) {
            long diffInMillies = System.currentTimeMillis() - performedDate.getTime();
            long diffInMinutes = diffInMillies / (60 * 1000);
            long diffInHours = diffInMillies / (60 * 60 * 1000);
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            
            if (diffInMinutes < 1) {
                return "Just now";
            } else if (diffInMinutes < 60) {
                return diffInMinutes + " minute" + (diffInMinutes > 1 ? "s" : "") + " ago";
            } else if (diffInHours < 24) {
                return diffInHours + " hour" + (diffInHours > 1 ? "s" : "") + " ago";
            } else {
                return diffInDays + " day" + (diffInDays > 1 ? "s" : "") + " ago";
            }
        }
        return "Unknown";
    }
    
    public Boolean isRecent() {
        if (performedDate != null) {
            long diffInMillies = System.currentTimeMillis() - performedDate.getTime();
            long diffInHours = diffInMillies / (60 * 60 * 1000);
            return diffInHours < 24; // Consider recent if less than 24 hours
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "RecentActivityDTO{" +
                "activityId=" + activityId +
                ", activityType='" + activityType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityName='" + entityName + '\'' +
                ", description='" + description + '\'' +
                ", performedBy='" + performedBy + '\'' +
                ", performedDate=" + performedDate +
                ", status='" + status + '\'' +
                '}';
    }
} 