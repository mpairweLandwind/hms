package com.hms2.dto.dashboard;

import java.time.LocalDateTime;

/**
 * Task Item DTO for staff dashboard pending tasks
 */
public class TaskItemDTO {
    private String title;
    private String description;
    private String priority; // low, medium, high, urgent
    private String actionUrl;
    private String icon;
    private LocalDateTime dueDate;
    private boolean completed;
    private String assignedTo;
    
    public TaskItemDTO() {
        this.completed = false;
    }
    
    public TaskItemDTO(String title, String description, String priority, String actionUrl, String icon, LocalDateTime dueDate) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.actionUrl = actionUrl;
        this.icon = icon;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
} 