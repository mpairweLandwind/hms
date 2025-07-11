/**
 * Revolutionary Sidebar JavaScript
 * Enhanced functionality for the HMS2 dashboard sidebar
 */

class DashboardSidebar {
    constructor() {
        this.sidebar = document.getElementById('sidebar');
        this.isCollapsed = false;
        this.isMobile = window.innerWidth <= 1024;
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupSubmenuHandlers();
        this.setupResponsiveBehavior();
        this.setupAccessibility();
        this.setupAnimations();
        this.loadUserPreferences();
    }

    setupEventListeners() {
        // Sidebar toggle button
        const toggleBtn = document.querySelector('.sidebar-toggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', () => this.toggleSidebar());
        }

        // Mobile sidebar toggle
        const mobileToggleBtn = document.querySelector('.mobile-sidebar-toggle');
        if (mobileToggleBtn) {
            mobileToggleBtn.addEventListener('click', () => this.toggleMobileSidebar());
        }

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', (e) => {
            if (this.isMobile && this.sidebar && !this.sidebar.contains(e.target) && 
                !mobileToggleBtn.contains(e.target)) {
                this.closeMobileSidebar();
            }
        });

        // Keyboard navigation
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && this.isMobile) {
                this.closeMobileSidebar();
            }
        });

        // Window resize handler
        window.addEventListener('resize', () => {
            this.handleResize();
        });
    }

    setupSubmenuHandlers() {
        const submenuLinks = document.querySelectorAll('.nav-link.has-submenu');
        
        submenuLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleSubmenu(link);
            });

            // Keyboard support for submenus
            link.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    this.toggleSubmenu(link);
                }
            });
        });
    }

    setupResponsiveBehavior() {
        this.handleResize();
    }

    setupAccessibility() {
        // Add ARIA labels and roles
        const navItems = document.querySelectorAll('.nav-item');
        navItems.forEach((item, index) => {
            const link = item.querySelector('.nav-link');
            if (link) {
                link.setAttribute('role', 'menuitem');
                link.setAttribute('tabindex', '0');
                
                const submenu = item.querySelector('.nav-submenu');
                if (submenu) {
                    submenu.setAttribute('role', 'menu');
                    submenu.setAttribute('aria-hidden', 'true');
                    link.setAttribute('aria-expanded', 'false');
                    link.setAttribute('aria-haspopup', 'true');
                }
            }
        });

        // Focus management
        this.setupFocusManagement();
    }

    setupFocusManagement() {
        const focusableElements = this.sidebar.querySelectorAll(
            'a[href], button, input, textarea, select, [tabindex]:not([tabindex="-1"])'
        );

        const firstElement = focusableElements[0];
        const lastElement = focusableElements[focusableElements.length - 1];

        // Trap focus within sidebar when open on mobile
        this.sidebar.addEventListener('keydown', (e) => {
            if (e.key === 'Tab') {
                if (e.shiftKey) {
                    if (document.activeElement === firstElement) {
                        e.preventDefault();
                        lastElement.focus();
                    }
                } else {
                    if (document.activeElement === lastElement) {
                        e.preventDefault();
                        firstElement.focus();
                    }
                }
            }
        });
    }

    setupAnimations() {
        // Add animation classes for smooth transitions
        this.sidebar.classList.add('sidebar-slide-in');
        
        // Remove animation class after animation completes
        setTimeout(() => {
            this.sidebar.classList.remove('sidebar-slide-in');
        }, 300);
    }

    toggleSidebar() {
        if (this.isMobile) {
            this.toggleMobileSidebar();
            return;
        }

        this.isCollapsed = !this.isCollapsed;
        
        if (this.isCollapsed) {
            this.collapseSidebar();
        } else {
            this.expandSidebar();
        }

        this.saveUserPreferences();
        this.updateMainContent();
    }

    toggleMobileSidebar() {
        if (this.sidebar.classList.contains('mobile-open')) {
            this.closeMobileSidebar();
        } else {
            this.openMobileSidebar();
        }
    }

    openMobileSidebar() {
        this.sidebar.classList.add('mobile-open');
        this.sidebar.classList.add('sidebar-slide-in');
        document.body.style.overflow = 'hidden';
        
        // Focus first focusable element
        const firstFocusable = this.sidebar.querySelector('a[href], button');
        if (firstFocusable) {
            firstFocusable.focus();
        }

        // Announce to screen readers
        this.announceToScreenReader('Sidebar opened');
    }

    closeMobileSidebar() {
        this.sidebar.classList.remove('mobile-open');
        this.sidebar.classList.add('sidebar-slide-out');
        
        setTimeout(() => {
            this.sidebar.classList.remove('sidebar-slide-out');
        }, 300);

        document.body.style.overflow = '';
        
        // Return focus to toggle button
        const mobileToggleBtn = document.querySelector('.mobile-sidebar-toggle');
        if (mobileToggleBtn) {
            mobileToggleBtn.focus();
        }

        // Announce to screen readers
        this.announceToScreenReader('Sidebar closed');
    }

    collapseSidebar() {
        this.sidebar.classList.add('collapsed');
        this.sidebar.setAttribute('aria-expanded', 'false');
        
        // Update toggle button
        const toggleBtn = document.querySelector('.sidebar-toggle');
        if (toggleBtn) {
            toggleBtn.setAttribute('aria-label', 'Expand sidebar');
        }
    }

    expandSidebar() {
        this.sidebar.classList.remove('collapsed');
        this.sidebar.setAttribute('aria-expanded', 'true');
        
        // Update toggle button
        const toggleBtn = document.querySelector('.sidebar-toggle');
        if (toggleBtn) {
            toggleBtn.setAttribute('aria-label', 'Collapse sidebar');
        }
    }

    toggleSubmenu(link) {
        const submenu = link.nextElementSibling;
        const isExpanded = link.getAttribute('aria-expanded') === 'true';
        
        // Close other open submenus
        this.closeOtherSubmenus(link);
        
        if (isExpanded) {
            this.closeSubmenu(link, submenu);
        } else {
            this.openSubmenu(link, submenu);
        }
    }

    openSubmenu(link, submenu) {
        link.setAttribute('aria-expanded', 'true');
        submenu.setAttribute('aria-hidden', 'false');
        submenu.style.maxHeight = submenu.scrollHeight + 'px';
        
        // Add active class to parent link
        link.classList.add('submenu-open');
        
        // Focus first submenu item
        const firstSubmenuItem = submenu.querySelector('.nav-sublink');
        if (firstSubmenuItem) {
            setTimeout(() => firstSubmenuItem.focus(), 100);
        }
    }

    closeSubmenu(link, submenu) {
        link.setAttribute('aria-expanded', 'false');
        submenu.setAttribute('aria-hidden', 'true');
        submenu.style.maxHeight = '0';
        
        // Remove active class from parent link
        link.classList.remove('submenu-open');
    }

    closeOtherSubmenus(currentLink) {
        const otherSubmenus = document.querySelectorAll('.nav-link.has-submenu');
        otherSubmenus.forEach(link => {
            if (link !== currentLink) {
                const submenu = link.nextElementSibling;
                this.closeSubmenu(link, submenu);
            }
        });
    }

    handleResize() {
        const wasMobile = this.isMobile;
        this.isMobile = window.innerWidth <= 1024;
        
        if (wasMobile !== this.isMobile) {
            if (!this.isMobile) {
                // Desktop view - close mobile sidebar
                this.closeMobileSidebar();
            }
        }
    }

    updateMainContent() {
        const mainContent = document.querySelector('.dashboard-main');
        if (mainContent) {
            if (this.isCollapsed) {
                mainContent.classList.add('sidebar-collapsed');
            } else {
                mainContent.classList.remove('sidebar-collapsed');
            }
        }
    }

    saveUserPreferences() {
        const preferences = {
            sidebarCollapsed: this.isCollapsed,
            timestamp: Date.now()
        };
        
        try {
            localStorage.setItem('hms2-sidebar-preferences', JSON.stringify(preferences));
        } catch (e) {
            console.warn('Could not save sidebar preferences:', e);
        }
    }

    loadUserPreferences() {
        try {
            const saved = localStorage.getItem('hms2-sidebar-preferences');
            if (saved) {
                const preferences = JSON.parse(saved);
                
                // Only apply preferences if they're recent (within 24 hours)
                const isRecent = (Date.now() - preferences.timestamp) < (24 * 60 * 60 * 1000);
                
                if (isRecent && preferences.sidebarCollapsed && !this.isMobile) {
                    this.isCollapsed = true;
                    this.collapseSidebar();
                    this.updateMainContent();
                }
            }
        } catch (e) {
            console.warn('Could not load sidebar preferences:', e);
        }
    }

    announceToScreenReader(message) {
        // Create a temporary element for screen reader announcements
        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.setAttribute('aria-atomic', 'true');
        announcement.style.position = 'absolute';
        announcement.style.left = '-10000px';
        announcement.style.width = '1px';
        announcement.style.height = '1px';
        announcement.style.overflow = 'hidden';
        
        announcement.textContent = message;
        document.body.appendChild(announcement);
        
        // Remove after announcement
        setTimeout(() => {
            document.body.removeChild(announcement);
        }, 1000);
    }

    // Public methods for external use
    refresh() {
        this.setupSubmenuHandlers();
        this.updateMainContent();
    }

    getState() {
        return {
            isCollapsed: this.isCollapsed,
            isMobile: this.isMobile,
            isMobileOpen: this.sidebar.classList.contains('mobile-open')
        };
    }
}

// Enhanced submenu toggle function for backward compatibility
function toggleSubmenu(element) {
    const sidebar = window.dashboardSidebar;
    if (sidebar) {
        sidebar.toggleSubmenu(element);
    }
}

// Enhanced sidebar toggle function for backward compatibility
function toggleSidebar() {
    const sidebar = window.dashboardSidebar;
    if (sidebar) {
        sidebar.toggleSidebar();
    }
}

// Enhanced mobile sidebar toggle function for backward compatibility
function toggleMobileSidebar() {
    const sidebar = window.dashboardSidebar;
    if (sidebar) {
        sidebar.toggleMobileSidebar();
    }
}

// Initialize sidebar when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    // Initialize the sidebar
    window.dashboardSidebar = new DashboardSidebar();
    
    // Add loading state management
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        // Show loading overlay for navigation
        document.addEventListener('click', function(e) {
            const link = e.target.closest('a[href]');
            if (link && link.href && !link.href.includes('#') && !link.href.includes('javascript:')) {
                loadingOverlay.style.display = 'flex';
            }
        });
        
        // Hide loading overlay when page loads
        window.addEventListener('load', function() {
            loadingOverlay.style.display = 'none';
        });
    }
    
    // Add smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Add keyboard navigation for quick actions
    const quickActionBtn = document.querySelector('.quick-action-btn');
    if (quickActionBtn) {
        quickActionBtn.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    }
    
    // Add notification badge updates
    updateNotificationBadge();
    
    // Add user status updates
    updateUserStatus();
});

// Utility functions
function updateNotificationBadge() {
    // Simulate notification count updates
    setInterval(() => {
        const badge = document.querySelector('.notification-badge');
        if (badge) {
            const currentCount = parseInt(badge.textContent) || 0;
            // Randomly update notification count (for demo purposes)
            if (Math.random() > 0.95) {
                badge.textContent = Math.max(0, currentCount + Math.floor(Math.random() * 3) - 1);
            }
        }
    }, 30000); // Check every 30 seconds
}

function updateUserStatus() {
    // Simulate user status updates
    setInterval(() => {
        const userStatus = document.querySelector('.user-status');
        if (userStatus) {
            const statuses = ['online', 'busy', 'offline'];
            const randomStatus = statuses[Math.floor(Math.random() * statuses.length)];
            userStatus.className = `user-status ${randomStatus}`;
        }
    }, 60000); // Update every minute
}

// Export for module systems
if (typeof module !== 'undefined' && module.exports) {
    module.exports = DashboardSidebar;
} 