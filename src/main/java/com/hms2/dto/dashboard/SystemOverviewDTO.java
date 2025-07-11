package com.hms2.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public class SystemOverviewDTO {
    
    // User Statistics
    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;
    private Long deletedUsers;
    private Long pendingUserVerifications;
    
    // Patient Statistics
    private Long totalPatients;
    private Long activePatients;
    private Long inactivePatients;
    private Long deletedPatients;
    private Long pendingPatientVerifications;
    private Long newPatientsThisMonth;
    private Long newPatientsThisWeek;
    
    // Doctor Statistics
    private Long totalDoctors;
    private Long activeDoctors;
    private Long inactiveDoctors;
    private Long deletedDoctors;
    private Long pendingDoctorVerifications;
    private Long availableDoctors;
    private Long onLeaveDoctors;
    
    // Staff Statistics
    private Long totalStaff;
    private Long activeStaff;
    private Long inactiveStaff;
    private Long deletedStaff;
    private Long pendingStaffVerifications;
    private Long availableStaff;
    private Long onLeaveStaff;
    
    // Department Statistics
    private Long totalDepartments;
    private Long activeDepartments;
    private Long inactiveDepartments;
    private Long deletedDepartments;
    
    // Appointment Statistics
    private Long totalAppointments;
    private Long pendingAppointments;
    private Long approvedAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Long todayAppointments;
    private Long tomorrowAppointments;
    private Long thisWeekAppointments;
    private Long thisMonthAppointments;
    
    // Billing Statistics
    private Long totalBills;
    private Long pendingBills;
    private Long paidBills;
    private Long overdueBills;
    private Long disputedBills;
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;
    private BigDecimal collectedRevenue;
    private BigDecimal overdueAmount;
    private BigDecimal thisMonthRevenue;
    private BigDecimal thisWeekRevenue;
    private BigDecimal todayRevenue;
    
    // Medical Records Statistics
    private Long totalMedicalRecords;
    private Long pendingMedicalRecords;
    private Long completedMedicalRecords;
    private Long deletedMedicalRecords;
    
    // Prescription Statistics
    private Long totalPrescriptions;
    private Long pendingPrescriptions;
    private Long activePrescriptions;
    private Long completedPrescriptions;
    private Long deletedPrescriptions;
    
    // Medication Statistics
    private Long totalMedications;
    private Long availableMedications;
    private Long lowStockMedications;
    private Long outOfStockMedications;
    
    // Pending Approvals
    private List<PendingApprovalDTO> pendingApprovals;
    private Long totalPendingApprovals;
    private Long urgentApprovals;
    private Long highPriorityApprovals;
    private Long normalPriorityApprovals;
    
    // System Health
    private String systemStatus; // HEALTHY, WARNING, CRITICAL
    private String databaseStatus;
    private String lastBackupDate;
    private String uptime;
    private Integer activeSessions;
    private Double systemLoad;
    private Long diskUsage;
    private Long memoryUsage;
    
    // Recent Activities
    private List<RecentActivityDTO> recentActivities;
    private List<SystemAlertDTO> systemAlerts;
    
    // Charts Data
    private List<ChartDataDTO> userGrowthChart;
    private List<ChartDataDTO> revenueChart;
    private List<ChartDataDTO> appointmentChart;
    private List<ChartDataDTO> billingChart;
    private List<ChartDataDTO> departmentUtilizationChart;
    
    // Quick Actions
    private List<QuickActionDTO> quickActions;
    
    // Constructors
    public SystemOverviewDTO() {}
    
    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public Long getInactiveUsers() {
        return inactiveUsers;
    }
    
    public void setInactiveUsers(Long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }
    
    public Long getDeletedUsers() {
        return deletedUsers;
    }
    
    public void setDeletedUsers(Long deletedUsers) {
        this.deletedUsers = deletedUsers;
    }
    
    public Long getPendingUserVerifications() {
        return pendingUserVerifications;
    }
    
    public void setPendingUserVerifications(Long pendingUserVerifications) {
        this.pendingUserVerifications = pendingUserVerifications;
    }
    
    public Long getTotalPatients() {
        return totalPatients;
    }
    
    public void setTotalPatients(Long totalPatients) {
        this.totalPatients = totalPatients;
    }
    
    public Long getActivePatients() {
        return activePatients;
    }
    
    public void setActivePatients(Long activePatients) {
        this.activePatients = activePatients;
    }
    
    public Long getInactivePatients() {
        return inactivePatients;
    }
    
    public void setInactivePatients(Long inactivePatients) {
        this.inactivePatients = inactivePatients;
    }
    
    public Long getDeletedPatients() {
        return deletedPatients;
    }
    
    public void setDeletedPatients(Long deletedPatients) {
        this.deletedPatients = deletedPatients;
    }
    
    public Long getPendingPatientVerifications() {
        return pendingPatientVerifications;
    }
    
    public void setPendingPatientVerifications(Long pendingPatientVerifications) {
        this.pendingPatientVerifications = pendingPatientVerifications;
    }
    
    public Long getNewPatientsThisMonth() {
        return newPatientsThisMonth;
    }
    
    public void setNewPatientsThisMonth(Long newPatientsThisMonth) {
        this.newPatientsThisMonth = newPatientsThisMonth;
    }
    
    public Long getNewPatientsThisWeek() {
        return newPatientsThisWeek;
    }
    
    public void setNewPatientsThisWeek(Long newPatientsThisWeek) {
        this.newPatientsThisWeek = newPatientsThisWeek;
    }
    
    public Long getTotalDoctors() {
        return totalDoctors;
    }
    
    public void setTotalDoctors(Long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }
    
    public Long getActiveDoctors() {
        return activeDoctors;
    }
    
    public void setActiveDoctors(Long activeDoctors) {
        this.activeDoctors = activeDoctors;
    }
    
    public Long getInactiveDoctors() {
        return inactiveDoctors;
    }
    
    public void setInactiveDoctors(Long inactiveDoctors) {
        this.inactiveDoctors = inactiveDoctors;
    }
    
    public Long getDeletedDoctors() {
        return deletedDoctors;
    }
    
    public void setDeletedDoctors(Long deletedDoctors) {
        this.deletedDoctors = deletedDoctors;
    }
    
    public Long getPendingDoctorVerifications() {
        return pendingDoctorVerifications;
    }
    
    public void setPendingDoctorVerifications(Long pendingDoctorVerifications) {
        this.pendingDoctorVerifications = pendingDoctorVerifications;
    }
    
    public Long getAvailableDoctors() {
        return availableDoctors;
    }
    
    public void setAvailableDoctors(Long availableDoctors) {
        this.availableDoctors = availableDoctors;
    }
    
    public Long getOnLeaveDoctors() {
        return onLeaveDoctors;
    }
    
    public void setOnLeaveDoctors(Long onLeaveDoctors) {
        this.onLeaveDoctors = onLeaveDoctors;
    }
    
    public Long getTotalStaff() {
        return totalStaff;
    }
    
    public void setTotalStaff(Long totalStaff) {
        this.totalStaff = totalStaff;
    }
    
    public Long getActiveStaff() {
        return activeStaff;
    }
    
    public void setActiveStaff(Long activeStaff) {
        this.activeStaff = activeStaff;
    }
    
    public Long getInactiveStaff() {
        return inactiveStaff;
    }
    
    public void setInactiveStaff(Long inactiveStaff) {
        this.inactiveStaff = inactiveStaff;
    }
    
    public Long getDeletedStaff() {
        return deletedStaff;
    }
    
    public void setDeletedStaff(Long deletedStaff) {
        this.deletedStaff = deletedStaff;
    }
    
    public Long getPendingStaffVerifications() {
        return pendingStaffVerifications;
    }
    
    public void setPendingStaffVerifications(Long pendingStaffVerifications) {
        this.pendingStaffVerifications = pendingStaffVerifications;
    }
    
    public Long getAvailableStaff() {
        return availableStaff;
    }
    
    public void setAvailableStaff(Long availableStaff) {
        this.availableStaff = availableStaff;
    }
    
    public Long getOnLeaveStaff() {
        return onLeaveStaff;
    }
    
    public void setOnLeaveStaff(Long onLeaveStaff) {
        this.onLeaveStaff = onLeaveStaff;
    }
    
    public Long getTotalDepartments() {
        return totalDepartments;
    }
    
    public void setTotalDepartments(Long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }
    
    public Long getActiveDepartments() {
        return activeDepartments;
    }
    
    public void setActiveDepartments(Long activeDepartments) {
        this.activeDepartments = activeDepartments;
    }
    
    public Long getInactiveDepartments() {
        return inactiveDepartments;
    }
    
    public void setInactiveDepartments(Long inactiveDepartments) {
        this.inactiveDepartments = inactiveDepartments;
    }
    
    public Long getDeletedDepartments() {
        return deletedDepartments;
    }
    
    public void setDeletedDepartments(Long deletedDepartments) {
        this.deletedDepartments = deletedDepartments;
    }
    
    public Long getTotalAppointments() {
        return totalAppointments;
    }
    
    public void setTotalAppointments(Long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }
    
    public Long getPendingAppointments() {
        return pendingAppointments;
    }
    
    public void setPendingAppointments(Long pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }
    
    public Long getApprovedAppointments() {
        return approvedAppointments;
    }
    
    public void setApprovedAppointments(Long approvedAppointments) {
        this.approvedAppointments = approvedAppointments;
    }
    
    public Long getCompletedAppointments() {
        return completedAppointments;
    }
    
    public void setCompletedAppointments(Long completedAppointments) {
        this.completedAppointments = completedAppointments;
    }
    
    public Long getCancelledAppointments() {
        return cancelledAppointments;
    }
    
    public void setCancelledAppointments(Long cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }
    
    public Long getTodayAppointments() {
        return todayAppointments;
    }
    
    public void setTodayAppointments(Long todayAppointments) {
        this.todayAppointments = todayAppointments;
    }
    
    public Long getTomorrowAppointments() {
        return tomorrowAppointments;
    }
    
    public void setTomorrowAppointments(Long tomorrowAppointments) {
        this.tomorrowAppointments = tomorrowAppointments;
    }
    
    public Long getThisWeekAppointments() {
        return thisWeekAppointments;
    }
    
    public void setThisWeekAppointments(Long thisWeekAppointments) {
        this.thisWeekAppointments = thisWeekAppointments;
    }
    
    public Long getThisMonthAppointments() {
        return thisMonthAppointments;
    }
    
    public void setThisMonthAppointments(Long thisMonthAppointments) {
        this.thisMonthAppointments = thisMonthAppointments;
    }
    
    public Long getTotalBills() {
        return totalBills;
    }
    
    public void setTotalBills(Long totalBills) {
        this.totalBills = totalBills;
    }
    
    public Long getPendingBills() {
        return pendingBills;
    }
    
    public void setPendingBills(Long pendingBills) {
        this.pendingBills = pendingBills;
    }
    
    public Long getPaidBills() {
        return paidBills;
    }
    
    public void setPaidBills(Long paidBills) {
        this.paidBills = paidBills;
    }
    
    public Long getOverdueBills() {
        return overdueBills;
    }
    
    public void setOverdueBills(Long overdueBills) {
        this.overdueBills = overdueBills;
    }
    
    public Long getDisputedBills() {
        return disputedBills;
    }
    
    public void setDisputedBills(Long disputedBills) {
        this.disputedBills = disputedBills;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public BigDecimal getPendingRevenue() {
        return pendingRevenue;
    }
    
    public void setPendingRevenue(BigDecimal pendingRevenue) {
        this.pendingRevenue = pendingRevenue;
    }
    
    public BigDecimal getCollectedRevenue() {
        return collectedRevenue;
    }
    
    public void setCollectedRevenue(BigDecimal collectedRevenue) {
        this.collectedRevenue = collectedRevenue;
    }
    
    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }
    
    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }
    
    public BigDecimal getThisMonthRevenue() {
        return thisMonthRevenue;
    }
    
    public void setThisMonthRevenue(BigDecimal thisMonthRevenue) {
        this.thisMonthRevenue = thisMonthRevenue;
    }
    
    public BigDecimal getThisWeekRevenue() {
        return thisWeekRevenue;
    }
    
    public void setThisWeekRevenue(BigDecimal thisWeekRevenue) {
        this.thisWeekRevenue = thisWeekRevenue;
    }
    
    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }
    
    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }
    
    public Long getTotalMedicalRecords() {
        return totalMedicalRecords;
    }
    
    public void setTotalMedicalRecords(Long totalMedicalRecords) {
        this.totalMedicalRecords = totalMedicalRecords;
    }
    
    public Long getPendingMedicalRecords() {
        return pendingMedicalRecords;
    }
    
    public void setPendingMedicalRecords(Long pendingMedicalRecords) {
        this.pendingMedicalRecords = pendingMedicalRecords;
    }
    
    public Long getCompletedMedicalRecords() {
        return completedMedicalRecords;
    }
    
    public void setCompletedMedicalRecords(Long completedMedicalRecords) {
        this.completedMedicalRecords = completedMedicalRecords;
    }
    
    public Long getDeletedMedicalRecords() {
        return deletedMedicalRecords;
    }
    
    public void setDeletedMedicalRecords(Long deletedMedicalRecords) {
        this.deletedMedicalRecords = deletedMedicalRecords;
    }
    
    public Long getTotalPrescriptions() {
        return totalPrescriptions;
    }
    
    public void setTotalPrescriptions(Long totalPrescriptions) {
        this.totalPrescriptions = totalPrescriptions;
    }
    
    public Long getPendingPrescriptions() {
        return pendingPrescriptions;
    }
    
    public void setPendingPrescriptions(Long pendingPrescriptions) {
        this.pendingPrescriptions = pendingPrescriptions;
    }
    
    public Long getActivePrescriptions() {
        return activePrescriptions;
    }
    
    public void setActivePrescriptions(Long activePrescriptions) {
        this.activePrescriptions = activePrescriptions;
    }
    
    public Long getCompletedPrescriptions() {
        return completedPrescriptions;
    }
    
    public void setCompletedPrescriptions(Long completedPrescriptions) {
        this.completedPrescriptions = completedPrescriptions;
    }
    
    public Long getDeletedPrescriptions() {
        return deletedPrescriptions;
    }
    
    public void setDeletedPrescriptions(Long deletedPrescriptions) {
        this.deletedPrescriptions = deletedPrescriptions;
    }
    
    public Long getTotalMedications() {
        return totalMedications;
    }
    
    public void setTotalMedications(Long totalMedications) {
        this.totalMedications = totalMedications;
    }
    
    public Long getAvailableMedications() {
        return availableMedications;
    }
    
    public void setAvailableMedications(Long availableMedications) {
        this.availableMedications = availableMedications;
    }
    
    public Long getLowStockMedications() {
        return lowStockMedications;
    }
    
    public void setLowStockMedications(Long lowStockMedications) {
        this.lowStockMedications = lowStockMedications;
    }
    
    public Long getOutOfStockMedications() {
        return outOfStockMedications;
    }
    
    public void setOutOfStockMedications(Long outOfStockMedications) {
        this.outOfStockMedications = outOfStockMedications;
    }
    
    public List<PendingApprovalDTO> getPendingApprovals() {
        return pendingApprovals;
    }
    
    public void setPendingApprovals(List<PendingApprovalDTO> pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }
    
    public Long getTotalPendingApprovals() {
        return totalPendingApprovals;
    }
    
    public void setTotalPendingApprovals(Long totalPendingApprovals) {
        this.totalPendingApprovals = totalPendingApprovals;
    }
    
    public Long getUrgentApprovals() {
        return urgentApprovals;
    }
    
    public void setUrgentApprovals(Long urgentApprovals) {
        this.urgentApprovals = urgentApprovals;
    }
    
    public Long getHighPriorityApprovals() {
        return highPriorityApprovals;
    }
    
    public void setHighPriorityApprovals(Long highPriorityApprovals) {
        this.highPriorityApprovals = highPriorityApprovals;
    }
    
    public Long getNormalPriorityApprovals() {
        return normalPriorityApprovals;
    }
    
    public void setNormalPriorityApprovals(Long normalPriorityApprovals) {
        this.normalPriorityApprovals = normalPriorityApprovals;
    }
    
    public String getSystemStatus() {
        return systemStatus;
    }
    
    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }
    
    public String getDatabaseStatus() {
        return databaseStatus;
    }
    
    public void setDatabaseStatus(String databaseStatus) {
        this.databaseStatus = databaseStatus;
    }
    
    public String getLastBackupDate() {
        return lastBackupDate;
    }
    
    public void setLastBackupDate(String lastBackupDate) {
        this.lastBackupDate = lastBackupDate;
    }
    
    public String getUptime() {
        return uptime;
    }
    
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
    
    public Integer getActiveSessions() {
        return activeSessions;
    }
    
    public void setActiveSessions(Integer activeSessions) {
        this.activeSessions = activeSessions;
    }
    
    public Double getSystemLoad() {
        return systemLoad;
    }
    
    public void setSystemLoad(Double systemLoad) {
        this.systemLoad = systemLoad;
    }
    
    public Long getDiskUsage() {
        return diskUsage;
    }
    
    public void setDiskUsage(Long diskUsage) {
        this.diskUsage = diskUsage;
    }
    
    public Long getMemoryUsage() {
        return memoryUsage;
    }
    
    public void setMemoryUsage(Long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    
    public List<RecentActivityDTO> getRecentActivities() {
        return recentActivities;
    }
    
    public void setRecentActivities(List<RecentActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }
    
    public List<SystemAlertDTO> getSystemAlerts() {
        return systemAlerts;
    }
    
    public void setSystemAlerts(List<SystemAlertDTO> systemAlerts) {
        this.systemAlerts = systemAlerts;
    }
    
    public List<ChartDataDTO> getUserGrowthChart() {
        return userGrowthChart;
    }
    
    public void setUserGrowthChart(List<ChartDataDTO> userGrowthChart) {
        this.userGrowthChart = userGrowthChart;
    }
    
    public List<ChartDataDTO> getRevenueChart() {
        return revenueChart;
    }
    
    public void setRevenueChart(List<ChartDataDTO> revenueChart) {
        this.revenueChart = revenueChart;
    }
    
    public List<ChartDataDTO> getAppointmentChart() {
        return appointmentChart;
    }
    
    public void setAppointmentChart(List<ChartDataDTO> appointmentChart) {
        this.appointmentChart = appointmentChart;
    }
    
    public List<ChartDataDTO> getBillingChart() {
        return billingChart;
    }
    
    public void setBillingChart(List<ChartDataDTO> billingChart) {
        this.billingChart = billingChart;
    }
    
    public List<ChartDataDTO> getDepartmentUtilizationChart() {
        return departmentUtilizationChart;
    }
    
    public void setDepartmentUtilizationChart(List<ChartDataDTO> departmentUtilizationChart) {
        this.departmentUtilizationChart = departmentUtilizationChart;
    }
    
    public List<QuickActionDTO> getQuickActions() {
        return quickActions;
    }
    
    public void setQuickActions(List<QuickActionDTO> quickActions) {
        this.quickActions = quickActions;
    }
    
    // Utility methods
    public Long getTotalPendingVerifications() {
        return (pendingUserVerifications != null ? pendingUserVerifications : 0L) +
               (pendingPatientVerifications != null ? pendingPatientVerifications : 0L) +
               (pendingDoctorVerifications != null ? pendingDoctorVerifications : 0L) +
               (pendingStaffVerifications != null ? pendingStaffVerifications : 0L);
    }
    
    public Long getTotalDeletedRecords() {
        return (deletedUsers != null ? deletedUsers : 0L) +
               (deletedPatients != null ? deletedPatients : 0L) +
               (deletedDoctors != null ? deletedDoctors : 0L) +
               (deletedStaff != null ? deletedStaff : 0L) +
               (deletedDepartments != null ? deletedDepartments : 0L) +
               (deletedMedicalRecords != null ? deletedMedicalRecords : 0L) +
               (deletedPrescriptions != null ? deletedPrescriptions : 0L);
    }
    
    @Override
    public String toString() {
        return "SystemOverviewDTO{" +
                "totalUsers=" + totalUsers +
                ", totalPatients=" + totalPatients +
                ", totalDoctors=" + totalDoctors +
                ", totalStaff=" + totalStaff +
                ", totalAppointments=" + totalAppointments +
                ", totalBills=" + totalBills +
                ", totalRevenue=" + totalRevenue +
                ", pendingApprovals=" + totalPendingApprovals +
                ", systemStatus='" + systemStatus + '\'' +
                '}';
    }
} 