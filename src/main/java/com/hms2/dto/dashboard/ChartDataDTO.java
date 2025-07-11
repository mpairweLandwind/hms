package com.hms2.dto.dashboard;

import java.math.BigDecimal;

public class ChartDataDTO {
    
    private String label;
    private String category;
    private String subCategory;
    private String period; // DAY, WEEK, MONTH, YEAR, CUSTOM
    private String date;
    private String time;
    private Long value;
    private BigDecimal amount;
    private String color;
    private String icon;
    private String tooltip;
    private String url;
    private Boolean isClickable;
    private String metadata;
    
    // Additional data for complex charts
    private Long secondaryValue;
    private BigDecimal secondaryAmount;
    private String secondaryLabel;
    private String secondaryColor;
    
    // Percentage and comparison data
    private Double percentage;
    private Double changePercentage;
    private String changeDirection; // INCREASE, DECREASE, NO_CHANGE
    private String changeColor;
    
    // Constructors
    public ChartDataDTO() {}
    
    public ChartDataDTO(String label, Long value) {
        this.label = label;
        this.value = value;
    }
    
    public ChartDataDTO(String label, BigDecimal amount) {
        this.label = label;
        this.amount = amount;
    }
    
    public ChartDataDTO(String label, Long value, String color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }
    
    public ChartDataDTO(String label, BigDecimal amount, String color) {
        this.label = label;
        this.amount = amount;
        this.color = color;
    }
    
    // Getters and Setters
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
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
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public Long getValue() {
        return value;
    }
    
    public void setValue(Long value) {
        this.value = value;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getTooltip() {
        return tooltip;
    }
    
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Boolean getIsClickable() {
        return isClickable;
    }
    
    public void setIsClickable(Boolean isClickable) {
        this.isClickable = isClickable;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    
    public Long getSecondaryValue() {
        return secondaryValue;
    }
    
    public void setSecondaryValue(Long secondaryValue) {
        this.secondaryValue = secondaryValue;
    }
    
    public BigDecimal getSecondaryAmount() {
        return secondaryAmount;
    }
    
    public void setSecondaryAmount(BigDecimal secondaryAmount) {
        this.secondaryAmount = secondaryAmount;
    }
    
    public String getSecondaryLabel() {
        return secondaryLabel;
    }
    
    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel = secondaryLabel;
    }
    
    public String getSecondaryColor() {
        return secondaryColor;
    }
    
    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
    
    public Double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
    public Double getChangePercentage() {
        return changePercentage;
    }
    
    public void setChangePercentage(Double changePercentage) {
        this.changePercentage = changePercentage;
    }
    
    public String getChangeDirection() {
        return changeDirection;
    }
    
    public void setChangeDirection(String changeDirection) {
        this.changeDirection = changeDirection;
    }
    
    public String getChangeColor() {
        return changeColor;
    }
    
    public void setChangeColor(String changeColor) {
        this.changeColor = changeColor;
    }
    
    // Utility methods
    public String getFormattedValue() {
        if (value != null) {
            return value.toString();
        }
        return "0";
    }
    
    public String getFormattedAmount() {
        if (amount != null) {
            return "$" + amount.toString();
        }
        return "$0.00";
    }
    
    public String getFormattedPercentage() {
        if (percentage != null) {
            return String.format("%.1f%%", percentage);
        }
        return "0%";
    }
    
    public String getFormattedChangePercentage() {
        if (changePercentage != null) {
            String direction = changeDirection != null ? changeDirection : "";
            return String.format("%s%.1f%%", direction.equals("INCREASE") ? "+" : "", changePercentage);
        }
        return "0%";
    }
    
    public String getChangeIcon() {
        if (changeDirection != null) {
            switch (changeDirection.toUpperCase()) {
                case "INCREASE":
                    return "pi pi-arrow-up";
                case "DECREASE":
                    return "pi pi-arrow-down";
                case "NO_CHANGE":
                    return "pi pi-minus";
                default:
                    return "pi pi-minus";
            }
        }
        return "pi pi-minus";
    }
    
    public String getChangeColorClass() {
        if (changeDirection != null) {
            switch (changeDirection.toUpperCase()) {
                case "INCREASE":
                    return "text-success";
                case "DECREASE":
                    return "text-danger";
                case "NO_CHANGE":
                    return "text-muted";
                default:
                    return "text-muted";
            }
        }
        return "text-muted";
    }
    
    public Boolean hasSecondaryData() {
        return secondaryValue != null || secondaryAmount != null;
    }
    
    public Boolean hasChangeData() {
        return changePercentage != null || changeDirection != null;
    }
    
    @Override
    public String toString() {
        return "ChartDataDTO{" +
                "label='" + label + '\'' +
                ", value=" + value +
                ", amount=" + amount +
                ", color='" + color + '\'' +
                ", percentage=" + percentage +
                '}';
    }
} 