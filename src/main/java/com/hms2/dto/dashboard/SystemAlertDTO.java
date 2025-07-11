package com.hms2.dto.dashboard;

import java.util.Date;

public class SystemAlertDTO {
    
    private Long alertId;
    private String alertType; // SYSTEM, SECURITY, PERFORMANCE, MAINTENANCE, BILLING, APPOINTMENT, MEDICAL
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW, INFO
    private String title;
    private String message;
    private String description;
    private String category; // SYSTEM_HEALTH, SECURITY_BREACH, PERFORMANCE_ISSUE, MAINTENANCE_SCHEDULED, BILLING_OVERDUE, APPOINTMENT_CONFLICT
    private String subCategory;
    private String source; // SYSTEM, USER, DOCTOR, STAFF, PATIENT, EXTERNAL
    private String sourceId;
    private String sourceName;
    
    // Alert status and management
    private String status; // ACTIVE, ACKNOWLEDGED, RESOLVED, DISMISSED, ESCALATED
    private Boolean isAcknowledged;
    private String acknowledgedBy;
    private Date acknowledgedDate;
    private String resolvedBy;
    private Date resolvedDate;
    private String resolutionNotes;
    private String dismissedBy;
    private Date dismissedDate;
    private String dismissalReason;
    
    // Timing and scheduling
    private Date createdDate;
    private Date lastModifiedDate;
    private Date expiryDate;
    private Boolean isRecurring;
    private String recurrencePattern; // DAILY, WEEKLY, MONTHLY, CUSTOM
    private Integer recurrenceInterval;
    private Date nextOccurrence;
    
    // Action and response
    private String actionRequired; // NONE, ACKNOWLEDGE, RESOLVE, ESCALATE, NOTIFY
    private String actionUrl;
    private String actionMethod; // GET, POST, PUT, DELETE
    private String actionParameters;
    private Boolean requiresConfirmation;
    private String confirmationMessage;
    
    // Notification settings
    private Boolean sendEmail;
    private Boolean sendSMS;
    private Boolean sendPush;
    private Boolean showInDashboard;
    private Boolean showInSidebar;
    private String notificationRecipients;
    private String notificationTemplate;
    
    // Additional context
    private String department;
    private String tags;
    private String metadata;
    private String externalReference;
    private String externalUrl;
    
    // Constructors
    public SystemAlertDTO() {}
    
    public SystemAlertDTO(String alertType, String severity, String title, String message) {
        this.alertType = alertType;
        this.severity = severity;
        this.title = title;
        this.message = message;
        this.createdDate = new Date();
        this.status = "ACTIVE";
        this.isAcknowledged = false;
    }
    
    // Getters and Setters
    public Long getAlertId() {
        return alertId;
    }
    
    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }
    
    public String getAlertType() {
        return alertType;
    }
    
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public String getSourceName() {
        return sourceName;
    }
    
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Boolean getIsAcknowledged() {
        return isAcknowledged;
    }
    
    public void setIsAcknowledged(Boolean isAcknowledged) {
        this.isAcknowledged = isAcknowledged;
    }
    
    public String getAcknowledgedBy() {
        return acknowledgedBy;
    }
    
    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }
    
    public Date getAcknowledgedDate() {
        return acknowledgedDate;
    }
    
    public void setAcknowledgedDate(Date acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }
    
    public String getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
    
    public Date getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public String getResolutionNotes() {
        return resolutionNotes;
    }
    
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
    
    public String getDismissedBy() {
        return dismissedBy;
    }
    
    public void setDismissedBy(String dismissedBy) {
        this.dismissedBy = dismissedBy;
    }
    
    public Date getDismissedDate() {
        return dismissedDate;
    }
    
    public void setDismissedDate(Date dismissedDate) {
        this.dismissedDate = dismissedDate;
    }
    
    public String getDismissalReason() {
        return dismissalReason;
    }
    
    public void setDismissalReason(String dismissalReason) {
        this.dismissalReason = dismissalReason;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public Boolean getIsRecurring() {
        return isRecurring;
    }
    
    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }
    
    public String getRecurrencePattern() {
        return recurrencePattern;
    }
    
    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
    
    public Integer getRecurrenceInterval() {
        return recurrenceInterval;
    }
    
    public void setRecurrenceInterval(Integer recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }
    
    public Date getNextOccurrence() {
        return nextOccurrence;
    }
    
    public void setNextOccurrence(Date nextOccurrence) {
        this.nextOccurrence = nextOccurrence;
    }
    
    public String getActionRequired() {
        return actionRequired;
    }
    
    public void setActionRequired(String actionRequired) {
        this.actionRequired = actionRequired;
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    public String getActionMethod() {
        return actionMethod;
    }
    
    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }
    
    public String getActionParameters() {
        return actionParameters;
    }
    
    public void setActionParameters(String actionParameters) {
        this.actionParameters = actionParameters;
    }
    
    public Boolean getRequiresConfirmation() {
        return requiresConfirmation;
    }
    
    public void setRequiresConfirmation(Boolean requiresConfirmation) {
        this.requiresConfirmation = requiresConfirmation;
    }
    
    public String getConfirmationMessage() {
        return confirmationMessage;
    }
    
    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }
    
    public Boolean getSendEmail() {
        return sendEmail;
    }
    
    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
    
    public Boolean getSendSMS() {
        return sendSMS;
    }
    
    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }
    
    public Boolean getSendPush() {
        return sendPush;
    }
    
    public void setSendPush(Boolean sendPush) {
        this.sendPush = sendPush;
    }
    
    public Boolean getShowInDashboard() {
        return showInDashboard;
    }
    
    public void setShowInDashboard(Boolean showInDashboard) {
        this.showInDashboard = showInDashboard;
    }
    
    public Boolean getShowInSidebar() {
        return showInSidebar;
    }
    
    public void setShowInSidebar(Boolean showInSidebar) {
        this.showInSidebar = showInSidebar;
    }
    
    public String getNotificationRecipients() {
        return notificationRecipients;
    }
    
    public void setNotificationRecipients(String notificationRecipients) {
        this.notificationRecipients = notificationRecipients;
    }
    
    public String getNotificationTemplate() {
        return notificationTemplate;
    }
    
    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    
    public String getExternalReference() {
        return externalReference;
    }
    
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }
    
    public String getExternalUrl() {
        return externalUrl;
    }
    
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
    
    // Utility methods
    public String getSeverityColor() {
        if (severity != null) {
            switch (severity.toUpperCase()) {
                case "CRITICAL":
                    return "danger";
                case "HIGH":
                    return "warning";
                case "MEDIUM":
                    return "info";
                case "LOW":
                    return "success";
                case "INFO":
                    return "primary";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public String getSeverityIcon() {
        if (severity != null) {
            switch (severity.toUpperCase()) {
                case "CRITICAL":
                    return "pi pi-exclamation-triangle";
                case "HIGH":
                    return "pi pi-exclamation-circle";
                case "MEDIUM":
                    return "pi pi-info-circle";
                case "LOW":
                    return "pi pi-check-circle";
                case "INFO":
                    return "pi pi-info";
                default:
                    return "pi pi-bell";
            }
        }
        return "pi pi-bell";
    }
    
    public String getStatusColor() {
        if (status != null) {
            switch (status.toUpperCase()) {
                case "ACTIVE":
                    return "danger";
                case "ACKNOWLEDGED":
                    return "warning";
                case "RESOLVED":
                    return "success";
                case "DISMISSED":
                    return "secondary";
                case "ESCALATED":
                    return "dark";
                default:
                    return "primary";
            }
        }
        return "primary";
    }
    
    public Boolean isExpired() {
        return expiryDate != null && expiryDate.before(new Date());
    }
    
    public Boolean isActive() {
        return "ACTIVE".equals(status) && !isExpired();
    }
    
    public Boolean requiresAction() {
        return "ACTIVE".equals(status) && !"NONE".equals(actionRequired);
    }
    
    @Override
    public String toString() {
        return "SystemAlertDTO{" +
                "alertId=" + alertId +
                ", alertType='" + alertType + '\'' +
                ", severity='" + severity + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
} 