package com.hms2.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive Dashboard DTO containing all dashboard components
 */
public class DashboardDTO {
    private List<QuickActionDTO> quickActions;
    private List<DashboardAlertDTO> alerts;
    private List<HealthTipDTO> healthTips;
    private List<TaskItemDTO> pendingTasks;
    private Map<String, Object> chartData;
    private Map<String, Long> statistics;
    private Map<String, Double> metrics;
    private String lastUpdated;
    
    // Constructors
    public DashboardDTO() {
        this.lastUpdated = LocalDateTime.now().toString();
    }
    
    // Getters and Setters
    public List<QuickActionDTO> getQuickActions() { return quickActions; }
    public void setQuickActions(List<QuickActionDTO> quickActions) { this.quickActions = quickActions; }
    
    public List<DashboardAlertDTO> getAlerts() { return alerts; }
    public void setAlerts(List<DashboardAlertDTO> alerts) { this.alerts = alerts; }
    
    public List<HealthTipDTO> getHealthTips() { return healthTips; }
    public void setHealthTips(List<HealthTipDTO> healthTips) { this.healthTips = healthTips; }
    
    public List<TaskItemDTO> getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(List<TaskItemDTO> pendingTasks) { this.pendingTasks = pendingTasks; }
    
    public Map<String, Object> getChartData() { return chartData; }
    public void setChartData(Map<String, Object> chartData) { this.chartData = chartData; }
    
    public Map<String, Long> getStatistics() { return statistics; }
    public void setStatistics(Map<String, Long> statistics) { this.statistics = statistics; }
    
    public Map<String, Double> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Double> metrics) { this.metrics = metrics; }
    
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
} 