package com.hms2.dto.dashboard;

import java.time.LocalDateTime;

/**
 * Dashboard Alert DTO for system alerts and notifications
 */
public class DashboardAlertDTO {
    private String title;
    private String message;
    private String severity; // info, warning, error, success
    private String priority; // low, medium, high, critical
    private LocalDateTime timestamp;
    private String icon;
    private boolean acknowledged;
    private String actionUrl;
    
    public DashboardAlertDTO() {
        this.timestamp = LocalDateTime.now();
        this.acknowledged = false;
    }
    
    public DashboardAlertDTO(String title, String message, String severity, String priority) {
        this();
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.priority = priority;
    }
    
    public DashboardAlertDTO(String title, String message, String severity, String priority, String icon) {
        this(title, message, severity, priority);
        this.icon = icon;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
} 