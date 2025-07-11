/**
 * Core JavaScript - Hospital Management System
 * Common functionality and interactions
 */

// ===================================
// GLOBAL VARIABLES
// ===================================
let mobileMenuOpen = false;
let userMenuOpen = false;
let notificationsOpen = false;

// ===================================
// GLOBAL UTILITIES
// ===================================

// Debounce function for performance optimization
function debounce(func, wait, immediate) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            timeout = null;
            if (!immediate) func(...args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func(...args);
    };
}

// Throttle function for scroll events
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// ===================================
// MODERN HEADER FUNCTIONS
// ===================================

// Mobile menu functions
function toggleMobileMenu() {
    const mobileNav = document.getElementById('mobileNav');
    const mobileOverlay = document.getElementById('mobileOverlay');
    
    if (!mobileMenuOpen) {
        // Open mobile menu
        mobileNav.classList.add('show');
        mobileOverlay.classList.add('show');
        mobileMenuOpen = true;
        
        // Prevent body scroll
        document.body.style.overflow = 'hidden';
        
        // Focus management
        setTimeout(() => {
        const firstLink = mobileNav.querySelector('.mobile-nav-link');
            if (firstLink) firstLink.focus();
        }, 100);
    } else {
        closeMobileMenu();
    }
}

function closeMobileMenu() {
    const mobileNav = document.getElementById('mobileNav');
    const mobileOverlay = document.getElementById('mobileOverlay');
    
    // Close mobile menu
    mobileNav.classList.remove('show');
    mobileOverlay.classList.remove('show');
    mobileMenuOpen = false;
    
    // Restore body scroll
        document.body.style.overflow = '';
        
    // Return focus to menu button
    const menuButton = document.querySelector('.mobile-menu-btn');
    if (menuButton) menuButton.focus();
}

// User menu functions
function toggleUserMenu() {
    const userDropdown = document.getElementById('userDropdown');
    const userMenuBtn = document.querySelector('.user-menu-btn');
    
    if (!userMenuOpen) {
        // Close other menus first
        closeNotifications();
        
        // Open user menu
        userDropdown.classList.add('show');
        userMenuOpen = true;
        
        // Update button state
        if (userMenuBtn) {
            userMenuBtn.setAttribute('aria-expanded', 'true');
        }
        
        // Focus management
        setTimeout(() => {
        const firstItem = userDropdown.querySelector('.dropdown-item');
            if (firstItem) firstItem.focus();
        }, 100);
    } else {
        closeUserMenu();
    }
}

function closeUserMenu() {
    const userDropdown = document.getElementById('userDropdown');
    const userMenuBtn = document.querySelector('.user-menu-btn');
    
    // Close user menu
    userDropdown.classList.remove('show');
    userMenuOpen = false;
    
    // Update button state
    if (userMenuBtn) {
        userMenuBtn.setAttribute('aria-expanded', 'false');
    }
}

// Notifications functions
function toggleNotifications() {
    // Close other menus first
    closeUserMenu();
    
    if (!notificationsOpen) {
        // Open notifications (placeholder for future implementation)
        notificationsOpen = true;
        console.log('Notifications panel would open here');
    } else {
        closeNotifications();
    }
}

function closeNotifications() {
    notificationsOpen = false;
    console.log('Notifications panel would close here');
}

// Search functions
function performSearch(query) {
    if (!query || query.trim() === '') {
        return;
    }
    
    // Show loading state
    const searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.classList.add('searching');
    }
    
    // Simulate search (replace with actual AJAX call)
    setTimeout(() => {
        if (searchInput) {
            searchInput.classList.remove('searching');
        }
        console.log('Searching for:', query);
    }, 500);
}

function clearSearch() {
    const searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.value = '';
        searchInput.focus();
    }
}

// ===================================
// LEGACY NAVIGATION FUNCTIONS (for backward compatibility)
// ===================================

// Mobile navigation toggle (legacy)
function toggleMobileNav() {
    toggleMobileMenu(); // Use new function
}

function openMobileNav() {
    toggleMobileMenu(); // Use new function
}

function closeMobileNav() {
    closeMobileMenu(); // Use new function
}

// User menu toggle (legacy)
function openUserMenu() {
    toggleUserMenu(); // Use new function
}

// Search functionality (legacy)
function toggleSearch() {
    // Implementation for search functionality
    console.log('Search toggle clicked');
    // TODO: Implement search modal or expandable search
}

// ===================================
// UTILITY FUNCTIONS
// ===================================

function showLoading(message = 'Loading...') {
    // Create loading overlay
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loadingOverlay';
    loadingOverlay.className = 'loading-overlay';
    loadingOverlay.innerHTML = `
        <div class="loading-content">
            <div class="loading-spinner"></div>
            <p class="loading-message">${message}</p>
        </div>
    `;
    
    document.body.appendChild(loadingOverlay);
    
    // Add loading styles if not already present
    if (!document.getElementById('loadingStyles')) {
        const loadingStyles = document.createElement('style');
        loadingStyles.id = 'loadingStyles';
        loadingStyles.textContent = `
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 9999;
                backdrop-filter: blur(4px);
            }
            
            .loading-content {
                background: white;
                padding: 2rem;
                border-radius: 12px;
                text-align: center;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            }
            
            .loading-spinner {
                width: 40px;
                height: 40px;
                border: 3px solid #e2e8f0;
                border-top: 3px solid #3b82f6;
                border-radius: 50%;
                animation: spin 1s linear infinite;
                margin: 0 auto 1rem auto;
            }
            
            .loading-message {
                margin: 0;
                color: #475569;
                font-weight: 500;
            }
            
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(loadingStyles);
    }
}

function hideLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

function showSuccessMessage(message) {
    showNotification(message, 'success');
}

function showErrorMessage(message) {
    showNotification(message, 'error');
}

function showWarningMessage(message) {
    showNotification(message, 'warning');
}

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="notification-icon pi ${getNotificationIcon(type)}"></i>
            <span class="notification-message">${message}</span>
            <button class="notification-close" onclick="this.parentElement.parentElement.remove()">
                <i class="pi pi-times"></i>
            </button>
        </div>
    `;
    
    // Add notification styles if not already present
    if (!document.getElementById('notificationStyles')) {
        const notificationStyles = document.createElement('style');
        notificationStyles.id = 'notificationStyles';
        notificationStyles.textContent = `
            .notification {
                position: fixed;
                top: 20px;
                right: 20px;
                background: white;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                border-left: 4px solid;
                z-index: 10000;
                max-width: 400px;
                animation: slideInRight 0.3s ease;
            }
            
            .notification-success {
                border-left-color: #10b981;
            }
            
            .notification-error {
                border-left-color: #ef4444;
            }
            
            .notification-warning {
                border-left-color: #f59e0b;
            }
            
            .notification-info {
                border-left-color: #3b82f6;
            }
            
            .notification-content {
                display: flex;
                align-items: center;
                gap: 0.75rem;
                padding: 1rem;
            }
            
            .notification-icon {
                font-size: 1.25rem;
                flex-shrink: 0;
            }
            
            .notification-success .notification-icon {
                color: #10b981;
            }
            
            .notification-error .notification-icon {
                color: #ef4444;
            }
            
            .notification-warning .notification-icon {
                color: #f59e0b;
            }
            
            .notification-info .notification-icon {
                color: #3b82f6;
            }
            
            .notification-message {
                flex: 1;
                color: #1e293b;
                font-size: 0.875rem;
            }
            
            .notification-close {
                background: transparent;
                border: none;
                color: #64748b;
                cursor: pointer;
                padding: 0.25rem;
                border-radius: 4px;
                transition: all 0.2s ease;
            }
            
            .notification-close:hover {
                background: #f1f5f9;
                color: #1e293b;
            }
            
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        `;
        document.head.appendChild(notificationStyles);
    }
    
    // Add to page
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

function getNotificationIcon(type) {
    switch (type) {
        case 'success': return 'pi-check-circle';
        case 'error': return 'pi-exclamation-triangle';
        case 'warning': return 'pi-exclamation-circle';
        default: return 'pi-info-circle';
    }
}

// ===================================
// FORM VALIDATION
// ===================================

// Real-time form validation
function setupFormValidation() {
    const forms = document.querySelectorAll('form[data-validate]');
    
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            // Real-time validation
            input.addEventListener('blur', () => validateField(input));
            input.addEventListener('input', debounce(() => validateField(input), 300));
        });
        
        // Form submission validation
        form.addEventListener('submit', (e) => {
            if (!validateForm(form)) {
                e.preventDefault();
            }
        });
    });
}

function validateField(field) {
    const formGroup = field.closest('.form-group');
    if (!formGroup) return true;
    
    const errorElement = formGroup.querySelector('.form-error');
    let isValid = true;
    let errorMessage = '';
    
    // Required field validation
    if (field.hasAttribute('required') && !field.value.trim()) {
        isValid = false;
        errorMessage = 'This field is required';
    }
    
    // Email validation
    if (field.type === 'email' && field.value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(field.value)) {
            isValid = false;
            errorMessage = 'Please enter a valid email address';
        }
    }
    
    // Phone validation
    if (field.type === 'tel' && field.value) {
        const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
        if (!phoneRegex.test(field.value.replace(/\s/g, ''))) {
            isValid = false;
            errorMessage = 'Please enter a valid phone number';
        }
    }
    
    // Update UI
    if (errorElement) {
    if (isValid) {
            errorElement.style.display = 'none';
            field.classList.remove('error');
    } else {
            errorElement.textContent = errorMessage;
            errorElement.style.display = 'block';
            field.classList.add('error');
        }
    }
    
    return isValid;
}

function validateForm(form) {
    const inputs = form.querySelectorAll('input, select, textarea');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// ===================================
// ACCESSIBILITY HELPERS
// ===================================

function createFocusTrap(element) {
    const focusableElements = element.querySelectorAll(
        'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    
    const firstElement = focusableElements[0];
    const lastElement = focusableElements[focusableElements.length - 1];
    
    element.addEventListener('keydown', function(event) {
        if (event.key === 'Tab') {
            if (event.shiftKey) {
                if (document.activeElement === firstElement) {
                    event.preventDefault();
                    lastElement.focus();
                }
            } else {
                if (document.activeElement === lastElement) {
                    event.preventDefault();
                    firstElement.focus();
                }
            }
        }
    });
}

function announceToScreenReader(message) {
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
    
        setTimeout(() => {
        document.body.removeChild(announcement);
        }, 1000);
}

// ===================================
// FORMATTING UTILITIES
// ===================================

function formatCurrency(amount, currency = 'USD') {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: currency
    }).format(amount);
}

function formatDate(date, options = {}) {
    const defaultOptions = {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    };
    return new Intl.DateTimeFormat('en-US', { ...defaultOptions, ...options }).format(new Date(date));
}

function formatPhoneNumber(phoneNumber) {
    const cleaned = phoneNumber.replace(/\D/g, '');
    const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
    if (match) {
        return '(' + match[1] + ') ' + match[2] + '-' + match[3];
    }
    return phoneNumber;
}

// ===================================
// CLIPBOARD UTILITIES
// ===================================

async function copyToClipboard(text) {
    try {
        await navigator.clipboard.writeText(text);
        showSuccessMessage('Copied to clipboard!');
    } catch (err) {
        console.error('Failed to copy text: ', err);
        showErrorMessage('Failed to copy to clipboard');
    }
}

// ===================================
// TOOLTIP FUNCTIONALITY
// ===================================

function initializeTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');
    
    tooltipElements.forEach(element => {
        element.addEventListener('mouseenter', showTooltip);
        element.addEventListener('mouseleave', hideTooltip);
        element.addEventListener('focus', showTooltip);
        element.addEventListener('blur', hideTooltip);
    });
}

function showTooltip(e) {
    const element = e.target;
    const tooltipText = element.getAttribute('data-tooltip');
    
    if (!tooltipText) return;
    
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    tooltip.textContent = tooltipText;
    
    document.body.appendChild(tooltip);
    
    const rect = element.getBoundingClientRect();
    tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
    tooltip.style.top = rect.top - tooltip.offsetHeight - 8 + 'px';
    
    element.tooltip = tooltip;
}

function hideTooltip(e) {
    const element = e.target;
    if (element.tooltip) {
        element.tooltip.remove();
        element.tooltip = null;
    }
}

// ===================================
// AUTO-SAVE FUNCTIONALITY
// ===================================

function setupAutoSave() {
    const forms = document.querySelectorAll('form[data-autosave]');
    
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            input.addEventListener('input', debounce(() => {
                saveFormData(form, form.getAttribute('data-autosave'));
            }, 1000));
        });
    });
}

function saveFormData(form, formId) {
    const formData = new FormData(form);
    const data = {};
    
    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }
    
    localStorage.setItem(`form_${formId}`, JSON.stringify(data));
    console.log('Form data auto-saved');
}

function loadFormData(form, formId) {
    const savedData = localStorage.getItem(`form_${formId}`);
    
    if (savedData) {
        const data = JSON.parse(savedData);
        
        Object.keys(data).forEach(key => {
            const field = form.querySelector(`[name="${key}"]`);
            if (field) {
                field.value = data[key];
            }
        });
    }
}

// ===================================
// EVENT LISTENERS
// ===================================

document.addEventListener('DOMContentLoaded', function() {
    // Close menus when clicking outside
    document.addEventListener('click', function(event) {
        const userMenu = document.querySelector('.user-menu');
        const mobileNav = document.getElementById('mobileNav');
        
        // Close user menu if clicking outside
        if (userMenu && !userMenu.contains(event.target) && userMenuOpen) {
            closeUserMenu();
        }
        
        // Close mobile menu if clicking outside (handled by overlay)
        if (mobileNav && !mobileNav.contains(event.target) && !event.target.closest('.mobile-menu-btn') && mobileMenuOpen) {
            closeMobileMenu();
        }
    });
    
    // Handle escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            if (mobileMenuOpen) {
                closeMobileMenu();
            }
            if (userMenuOpen) {
                closeUserMenu();
            }
            if (notificationsOpen) {
                closeNotifications();
            }
        }
    });
    
    // Handle search input
    const searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                performSearch(this.value);
            }
        });
    }
    
    // Initialize components
    setupFormValidation();
    initializeTooltips();
    setupAutoSave();
    
    console.log('Core components initialized');
});

// ===================================
// EXPORT FUNCTIONS
// ===================================

// Export functions for use in other scripts
window.HMS = {
    // Modern header functions
    toggleMobileMenu,
    closeMobileMenu,
    toggleUserMenu,
    closeUserMenu,
    toggleNotifications,
    closeNotifications,
    performSearch,
    clearSearch,
    
    // Utility functions
    showLoading,
    hideLoading,
    showSuccessMessage,
    showErrorMessage,
    showWarningMessage,
    showNotification,
    
    // Legacy functions (for backward compatibility)
    toggleMobileNav,
    openMobileNav,
    closeMobileNav,
    openUserMenu,
    toggleSearch,
    
    // Utility functions
    debounce,
    throttle,
    createFocusTrap,
    announceToScreenReader,
    formatCurrency,
    formatDate,
    formatPhoneNumber,
    copyToClipboard,
    validateField,
    validateForm,
    saveFormData,
    loadFormData
}; 