/**
 * Dashboard JavaScript - HMS2
 * Enhanced functionality for the dashboard interface
 */

class DashboardManager {
    constructor() {
        this.currentPage = window.location.pathname;
        this.userRole = this.getUserRole();
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupRealTimeUpdates();
        this.setupCharts();
        this.setupNotifications();
        this.setupQuickActions();
        this.setupSearch();
    }

    setupEventListeners() {
        // Global click handlers
        document.addEventListener('click', (e) => {
            this.handleGlobalClick(e);
        });

        // Keyboard shortcuts
        document.addEventListener('keydown', (e) => {
            this.handleKeyboardShortcuts(e);
        });

        // Window focus/blur events
        window.addEventListener('focus', () => {
            this.handleWindowFocus();
        });

        window.addEventListener('blur', () => {
            this.handleWindowBlur();
        });
    }

    setupRealTimeUpdates() {
        // Update dashboard data every 30 seconds
        setInterval(() => {
            this.updateDashboardData();
        }, 30000);

        // Update user status every minute
        setInterval(() => {
            this.updateUserStatus();
        }, 60000);
    }

    setupCharts() {
        // Initialize charts if Chart.js is available
        if (typeof Chart !== 'undefined') {
            this.initializeCharts();
        }
    }

    setupNotifications() {
        // Check for new notifications every 10 seconds
        setInterval(() => {
            this.checkNewNotifications();
        }, 10000);
    }

    setupQuickActions() {
        const quickActionBtns = document.querySelectorAll('.quick-action-btn');
        quickActionBtns.forEach(btn => {
            btn.addEventListener('click', (e) => {
                this.handleQuickAction(e);
            });
        });
    }

    setupSearch() {
        const searchInput = document.querySelector('.global-search');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                this.handleSearch(e);
            });

            searchInput.addEventListener('keydown', (e) => {
                if (e.key === 'Enter') {
                    this.performSearch();
                }
            });
        }
    }

    handleGlobalClick(e) {
        // Handle clicks on dashboard elements
        const target = e.target;

        // Close dropdowns when clicking outside
        if (!target.closest('.dropdown, .overlay-panel')) {
            this.closeAllDropdowns();
        }

        // Handle card clicks
        if (target.closest('.dashboard-card')) {
            this.handleCardClick(target.closest('.dashboard-card'));
        }

        // Handle action buttons
        if (target.closest('.action-btn')) {
            this.handleActionButton(target.closest('.action-btn'));
        }
    }

    handleKeyboardShortcuts(e) {
        // Ctrl/Cmd + K for search
        if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
            e.preventDefault();
            this.focusSearch();
        }

        // Ctrl/Cmd + N for notifications
        if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
            e.preventDefault();
            this.toggleNotifications();
        }

        // Escape to close modals/dropdowns
        if (e.key === 'Escape') {
            this.closeAllDropdowns();
        }

        // Alt + 1-9 for quick navigation
        if (e.altKey && e.key >= '1' && e.key <= '9') {
            e.preventDefault();
            this.navigateToQuickLink(parseInt(e.key));
        }
    }

    handleWindowFocus() {
        // Refresh data when window gains focus
        this.updateDashboardData();
        this.updateNotificationBadge();
    }

    handleWindowBlur() {
        // Pause real-time updates when window loses focus
        this.pauseRealTimeUpdates();
    }

    updateDashboardData() {
        // Update dashboard statistics
        this.updateStatistics();
        
        // Update recent activity
        this.updateRecentActivity();
        
        // Update charts
        this.updateCharts();
    }

    updateStatistics() {
        // Update dashboard statistics via AJAX
        fetch('/api/dashboard/statistics')
            .then(response => response.json())
            .then(data => {
                this.updateStatisticsDisplay(data);
            })
            .catch(error => {
                console.warn('Failed to update statistics:', error);
            });
    }

    updateRecentActivity() {
        // Update recent activity feed
        fetch('/api/dashboard/recent-activity')
            .then(response => response.json())
            .then(data => {
                this.updateActivityFeed(data);
            })
            .catch(error => {
                console.warn('Failed to update recent activity:', error);
            });
    }

    updateCharts() {
        // Update chart data
        const charts = document.querySelectorAll('.chart-container');
        charts.forEach(chart => {
            const chartId = chart.dataset.chartId;
            if (chartId) {
                this.updateChart(chartId);
            }
        });
    }

    updateChart(chartId) {
        fetch(`/api/dashboard/chart/${chartId}`)
            .then(response => response.json())
            .then(data => {
                this.updateChartData(chartId, data);
            })
            .catch(error => {
                console.warn(`Failed to update chart ${chartId}:`, error);
            });
    }

    checkNewNotifications() {
        fetch('/api/notifications/unread-count')
            .then(response => response.json())
            .then(data => {
                this.updateNotificationBadge(data.count);
            })
            .catch(error => {
                console.warn('Failed to check notifications:', error);
            });
    }

    updateNotificationBadge(count) {
        const badge = document.querySelector('.notification-badge');
        if (badge) {
            if (count > 0) {
                badge.textContent = count;
                badge.style.display = 'block';
            } else {
                badge.style.display = 'none';
            }
        }
    }

    handleQuickAction(e) {
        const action = e.target.dataset.action;
        switch (action) {
            case 'add-patient':
                window.location.href = '/views/patients/patient-registration';
                break;
            case 'book-appointment':
                window.location.href = '/views/appointments/appointment-booking';
                break;
            case 'create-invoice':
                window.location.href = '/views/billing/billing-create';
                break;
            case 'new-record':
                window.location.href = '/views/medical-records/record-create';
                break;
            default:
                console.warn('Unknown quick action:', action);
        }
    }

    handleCardClick(card) {
        const cardType = card.dataset.type;
        const cardId = card.dataset.id;
        
        switch (cardType) {
            case 'patient':
                window.location.href = `/views/patients/patient-details?id=${cardId}`;
                break;
            case 'appointment':
                window.location.href = `/views/appointments/appointment-details?id=${cardId}`;
                break;
            case 'billing':
                window.location.href = `/views/billing/billing-details?id=${cardId}`;
                break;
            default:
                console.warn('Unknown card type:', cardType);
        }
    }

    handleActionButton(button) {
        const action = button.dataset.action;
        const itemId = button.dataset.id;
        
        switch (action) {
            case 'edit':
                this.editItem(itemId);
                break;
            case 'delete':
                this.deleteItem(itemId);
                break;
            case 'view':
                this.viewItem(itemId);
                break;
            default:
                console.warn('Unknown action:', action);
        }
    }

    editItem(itemId) {
        // Handle edit action
        console.log('Edit item:', itemId);
    }

    deleteItem(itemId) {
        // Handle delete action with confirmation
        if (confirm('Are you sure you want to delete this item?')) {
            console.log('Delete item:', itemId);
        }
    }

    viewItem(itemId) {
        // Handle view action
        console.log('View item:', itemId);
    }

    closeAllDropdowns() {
        const dropdowns = document.querySelectorAll('.dropdown, .overlay-panel');
        dropdowns.forEach(dropdown => {
            dropdown.classList.remove('show');
        });
    }

    focusSearch() {
        const searchInput = document.querySelector('.global-search');
        if (searchInput) {
            searchInput.focus();
        }
    }

    toggleNotifications() {
        const notificationPanel = document.querySelector('.notification-panel');
        if (notificationPanel) {
            notificationPanel.classList.toggle('show');
        }
    }

    navigateToQuickLink(index) {
        const quickLinks = [
            '/views/dashboard/admin-dashboard',
            '/views/patients/patient-list',
            '/views/appointments/appointment-list',
            '/views/billing/billing-list',
            '/views/doctors/doctor-list',
            '/views/staff/staff-list',
            '/views/reports/reports',
            '/views/settings/settings',
            '/views/profile/profile'
        ];
        
        if (quickLinks[index - 1]) {
            window.location.href = quickLinks[index - 1];
        }
    }

    handleSearch(e) {
        const query = e.target.value;
        if (query.length >= 2) {
            this.performSearchSuggestions(query);
        }
    }

    performSearch() {
        const searchInput = document.querySelector('.global-search');
        if (searchInput && searchInput.value.trim()) {
            const query = searchInput.value.trim();
            window.location.href = `/views/search/results?q=${encodeURIComponent(query)}`;
        }
    }

    performSearchSuggestions(query) {
        fetch(`/api/search/suggestions?q=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                this.updateSearchSuggestions(data);
            })
            .catch(error => {
                console.warn('Failed to get search suggestions:', error);
            });
    }

    updateSearchSuggestions(suggestions) {
        const suggestionsContainer = document.querySelector('.search-suggestions');
        if (suggestionsContainer) {
            suggestionsContainer.innerHTML = '';
            suggestions.forEach(suggestion => {
                const item = document.createElement('div');
                item.className = 'suggestion-item';
                item.textContent = suggestion.text;
                item.addEventListener('click', () => {
                    window.location.href = suggestion.url;
                });
                suggestionsContainer.appendChild(item);
            });
        }
    }

    updateStatisticsDisplay(data) {
        // Update statistics cards
        Object.keys(data).forEach(key => {
            const element = document.querySelector(`[data-stat="${key}"]`);
            if (element) {
                element.textContent = data[key];
            }
        });
    }

    updateActivityFeed(data) {
        const activityContainer = document.querySelector('.activity-feed');
        if (activityContainer) {
            activityContainer.innerHTML = '';
            data.forEach(activity => {
                const item = document.createElement('div');
                item.className = 'activity-item';
                item.innerHTML = `
                    <div class="activity-icon">
                        <i class="pi ${activity.icon}"></i>
                    </div>
                    <div class="activity-content">
                        <p>${activity.description}</p>
                        <span class="activity-time">${activity.time}</span>
                    </div>
                `;
                activityContainer.appendChild(item);
            });
        }
    }

    updateChartData(chartId, data) {
        // Update chart data if Chart.js is available
        if (window.charts && window.charts[chartId]) {
            const chart = window.charts[chartId];
            chart.data = data;
            chart.update();
        }
    }

    initializeCharts() {
        // Initialize dashboard charts
        const chartContainers = document.querySelectorAll('.chart-container');
        chartContainers.forEach(container => {
            const chartId = container.dataset.chartId;
            const chartType = container.dataset.chartType;
            const chartData = JSON.parse(container.dataset.chartData || '{}');
            
            this.createChart(chartId, chartType, chartData, container);
        });
    }

    createChart(chartId, chartType, data, container) {
        const ctx = container.querySelector('canvas');
        if (!ctx) return;

        if (!window.charts) {
            window.charts = {};
        }

        window.charts[chartId] = new Chart(ctx, {
            type: chartType,
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }

    getUserRole() {
        // Get user role from page or session
        const roleElement = document.querySelector('[data-user-role]');
        return roleElement ? roleElement.dataset.userRole : 'GUEST';
    }

    pauseRealTimeUpdates() {
        // Pause real-time updates when window loses focus
        console.log('Pausing real-time updates');
    }

    // Utility methods
    showLoading() {
        const overlay = document.getElementById('loading-overlay');
        if (overlay) {
            overlay.style.display = 'flex';
        }
    }

    hideLoading() {
        const overlay = document.getElementById('loading-overlay');
        if (overlay) {
            overlay.style.display = 'none';
        }
    }

    showMessage(message, type = 'info') {
        // Show message using PrimeFaces growl or custom notification
        if (typeof PF !== 'undefined' && PF('globalMessages')) {
            PF('globalMessages').add({
                severity: type,
                summary: message,
                detail: message
            });
        } else {
            // Fallback to alert
            alert(message);
        }
    }

    // Public API
    refresh() {
        this.updateDashboardData();
    }

    getState() {
        return {
            currentPage: this.currentPage,
            userRole: this.userRole,
            timestamp: Date.now()
        };
    }
}

// Initialize dashboard when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    // Initialize the dashboard manager
    window.dashboardManager = new DashboardManager();
    
    // Add global error handling
    window.addEventListener('error', function(e) {
        console.error('Dashboard error:', e.error);
        if (window.dashboardManager) {
            window.dashboardManager.showMessage('An error occurred. Please refresh the page.', 'error');
        }
    });
    
    // Add unhandled promise rejection handling
    window.addEventListener('unhandledrejection', function(e) {
        console.error('Unhandled promise rejection:', e.reason);
        if (window.dashboardManager) {
            window.dashboardManager.showMessage('A network error occurred.', 'error');
        }
    });
});

// Export for module systems
if (typeof module !== 'undefined' && module.exports) {
    module.exports = DashboardManager;
} 