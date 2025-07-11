package com.hms2.dto.dashboard;

import java.time.LocalDateTime;

/**
 * Health Tip DTO for patient dashboard health tips
 */
public class HealthTipDTO {
    private String title;
    private String description;
    private String icon;
    private String category;
    private String source;
    private LocalDateTime createdAt;
    
    public HealthTipDTO() {
        this.createdAt = LocalDateTime.now();
    }
    
    public HealthTipDTO(String title, String description, String icon) {
        this();
        this.title = title;
        this.description = description;
        this.icon = icon;
    }
    
    public HealthTipDTO(String title, String description, String icon, String category, String source) {
        this(title, description, icon);
        this.category = category;
        this.source = source;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 