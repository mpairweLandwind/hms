package com.hms2.dto.dashboard;

/**
 * Quick Action DTO for dashboard quick action buttons
 */
public class QuickActionDTO {
    private String title;
    private String icon;
    private String action;
    private String color;
    private String description;
    private String url;
    private boolean enabled;
    
    public QuickActionDTO() {}
    
    public QuickActionDTO(String title, String icon, String action, String color) {
        this.title = title;
        this.icon = icon;
        this.action = action;
        this.color = color;
        this.enabled = true;
    }
    
    public QuickActionDTO(String title, String icon, String action, String color, String description, String url) {
        this(title, icon, action, color);
        this.description = description;
        this.url = url;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
} 