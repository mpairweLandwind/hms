/**
 * Registration Page JavaScript - Hospital Management System
 * Handles multi-step form navigation, validation, and user experience
 */

// Global variables
let currentStep = 1;
const totalSteps = 3;

// DOM ready function
document.addEventListener('DOMContentLoaded', function() {
    initializeRegistration();
});

/**
 * Initialize registration page functionality
 */
function initializeRegistration() {
    updateProgressSteps();
    setupEventListeners();
    setupFormValidation();
    setupAccessibility();
}

/**
 * Update progress steps display
 */
function updateProgressSteps() {
    const steps = document.querySelectorAll('.step');
    steps.forEach((step, index) => {
        const stepNumber = index + 1;
        if (stepNumber < currentStep) {
            step.classList.add('completed');
            step.classList.remove('active');
        } else if (stepNumber === currentStep) {
            step.classList.add('active');
            step.classList.remove('completed');
        } else {
            step.classList.remove('active', 'completed');
        }
    });
}

/**
 * Setup event listeners for form interactions
 */
function setupEventListeners() {
    // Password confirmation validation
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmPassword');
    
    if (passwordField && confirmPasswordField) {
        confirmPasswordField.addEventListener('input', function() {
            validatePasswordConfirmation();
        });
    }
    
    // Real-time validation for required fields
    const requiredFields = document.querySelectorAll('.form-input[required]');
    requiredFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateField(this);
        });
        
        field.addEventListener('input', function() {
            clearFieldError(this);
        });
    });
    
    // Form submission prevention on Enter key
    const forms = document.querySelectorAll('.registration-form');
    forms.forEach(form => {
        form.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && e.target.type !== 'textarea') {
                e.preventDefault();
                const activeStep = document.querySelector('.form-step.active');
                const submitBtn = activeStep.querySelector('.submit-btn');
                if (submitBtn && submitBtn.style.display !== 'none') {
                    submitBtn.click();
                } else {
                    nextStep();
                }
            }
        });
    });
}

/**
 * Setup form validation
 */
function setupFormValidation() {
    // Email validation
    const emailFields = document.querySelectorAll('input[type="email"]');
    emailFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateEmail(this);
        });
    });
    
    // Phone number validation
    const phoneFields = document.querySelectorAll('input[id*="phone"]');
    phoneFields.forEach(field => {
        field.addEventListener('blur', function() {
            validatePhoneNumber(this);
        });
    });
    
    // Username validation
    const usernameFields = document.querySelectorAll('input[id="username"]');
    usernameFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateUsername(this);
        });
    });
}

/**
 * Setup accessibility features
 */
function setupAccessibility() {
    // Add ARIA labels and roles
    const formSteps = document.querySelectorAll('.form-step');
    formSteps.forEach((step, index) => {
        step.setAttribute('role', 'tabpanel');
        step.setAttribute('aria-labelledby', `step-${index + 1}`);
        step.setAttribute('aria-hidden', index === 0 ? 'false' : 'true');
    });
    
    // Add skip links for keyboard navigation
    addSkipLinks();
    
    // Focus management
    setupFocusManagement();
}

/**
 * Add skip links for accessibility
 */
function addSkipLinks() {
    const skipLinks = document.createElement('div');
    skipLinks.className = 'skip-links';
    skipLinks.innerHTML = `
        <a href="#main-content" class="skip-link">Skip to main content</a>
        <a href="#form-navigation" class="skip-link">Skip to form navigation</a>
    `;
    
    const body = document.body;
    body.insertBefore(skipLinks, body.firstChild);
}

/**
 * Setup focus management
 */
function setupFocusManagement() {
    // Focus first field in each step
    const activeStep = document.querySelector('.form-step.active');
    if (activeStep) {
        const firstInput = activeStep.querySelector('input, select, textarea');
        if (firstInput) {
            firstInput.focus();
        }
    }
}

/**
 * Navigate to next step
 */
function nextStep() {
    if (currentStep < totalSteps) {
        if (validateCurrentStep()) {
            currentStep++;
            showStep(currentStep);
            updateProgressSteps();
            updateNavigationButtons();
            announceStepChange();
        }
    }
}

/**
 * Navigate to previous step
 */
function previousStep() {
    if (currentStep > 1) {
        currentStep--;
        showStep(currentStep);
        updateProgressSteps();
        updateNavigationButtons();
        announceStepChange();
    }
}

/**
 * Show specific step
 */
function showStep(stepNumber) {
    const steps = document.querySelectorAll('.form-step');
    steps.forEach((step, index) => {
        if (index + 1 === stepNumber) {
            step.classList.add('active');
            step.setAttribute('aria-hidden', 'false');
        } else {
            step.classList.remove('active');
            step.setAttribute('aria-hidden', 'true');
        }
    });
    
    // Focus first field in new step
    setTimeout(() => {
        const activeStep = document.querySelector('.form-step.active');
        if (activeStep) {
            const firstInput = activeStep.querySelector('input, select, textarea');
            if (firstInput) {
                firstInput.focus();
            }
        }
    }, 100);
}

/**
 * Update navigation buttons visibility
 */
function updateNavigationButtons() {
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const submitBtn = document.querySelector('.submit-btn');
    
    if (prevBtn) {
        prevBtn.style.display = currentStep > 1 ? 'flex' : 'none';
    }
    
    if (nextBtn) {
        nextBtn.style.display = currentStep < totalSteps ? 'flex' : 'none';
    }
    
    if (submitBtn) {
        submitBtn.style.display = currentStep === totalSteps ? 'flex' : 'none';
    }
}

/**
 * Validate current step
 */
function validateCurrentStep() {
    const activeStep = document.querySelector('.form-step.active');
    const requiredFields = activeStep.querySelectorAll('.form-input[required]');
    let isValid = true;
    
    requiredFields.forEach(field => {
        if (!validateField(field)) {
            isValid = false;
        }
    });
    
    // Special validations for specific steps
    if (currentStep === 1) {
        isValid = validateStep1() && isValid;
    } else if (currentStep === 2) {
        isValid = validateStep2() && isValid;
    } else if (currentStep === 3) {
        isValid = validateStep3() && isValid;
    }
    
    if (!isValid) {
        showStepError('Please complete all required fields correctly.');
    }
    
    return isValid;
}

/**
 * Validate step 1 (Account Information)
 */
function validateStep1() {
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    
    let isValid = true;
    
    // Username validation
    if (username && !validateUsername(username)) {
        isValid = false;
    }
    
    // Email validation
    if (email && !validateEmail(email)) {
        isValid = false;
    }
    
    // Password validation
    if (password && !validatePassword(password)) {
        isValid = false;
    }
    
    // Password confirmation
    if (confirmPassword && !validatePasswordConfirmation()) {
        isValid = false;
    }
    
    return isValid;
}

/**
 * Validate step 2 (Personal Information)
 */
function validateStep2() {
    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const phoneNumber = document.getElementById('phoneNumber');
    
    let isValid = true;
    
    // Name validation
    if (firstName && !validateName(firstName)) {
        isValid = false;
    }
    
    if (lastName && !validateName(lastName)) {
        isValid = false;
    }
    
    // Phone validation
    if (phoneNumber && !validatePhoneNumber(phoneNumber)) {
        isValid = false;
    }
    
    return isValid;
}

/**
 * Validate step 3 (Role-specific Information)
 */
function validateStep3() {
    // Check if all required checkboxes are checked
    const requiredCheckboxes = document.querySelectorAll('.custom-checkbox[required]');
    let isValid = true;
    
    requiredCheckboxes.forEach(checkbox => {
        if (!checkbox.checked) {
            isValid = false;
            showFieldError(checkbox, 'This field is required.');
        }
    });
    
    return isValid;
}

/**
 * Validate individual field
 */
function validateField(field) {
    const value = field.value.trim();
    
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, 'This field is required.');
        return false;
    }
    
    // Type-specific validation
    if (field.type === 'email' && value) {
        return validateEmail(field);
    }
    
    if (field.id && field.id.includes('phone') && value) {
        return validatePhoneNumber(field);
    }
    
    if (field.id === 'username' && value) {
        return validateUsername(field);
    }
    
    if (field.id && field.id.includes('name') && value) {
        return validateName(field);
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate email format
 */
function validateEmail(field) {
    const email = field.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (email && !emailRegex.test(email)) {
        showFieldError(field, 'Please enter a valid email address.');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate phone number
 */
function validatePhoneNumber(field) {
    const phone = field.value.replace(/\D/g, '');
    
    if (phone && (phone.length < 10 || phone.length > 15)) {
        showFieldError(field, 'Please enter a valid phone number (10-15 digits).');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate username
 */
function validateUsername(field) {
    const username = field.value.trim();
    
    if (username.length < 3) {
        showFieldError(field, 'Username must be at least 3 characters long.');
        return false;
    }
    
    if (username.length > 50) {
        showFieldError(field, 'Username must be less than 50 characters.');
        return false;
    }
    
    if (!/^[a-zA-Z0-9_]+$/.test(username)) {
        showFieldError(field, 'Username can only contain letters, numbers, and underscores.');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate name fields
 */
function validateName(field) {
    const name = field.value.trim();
    
    if (name.length < 1) {
        showFieldError(field, 'Name is required.');
        return false;
    }
    
    if (name.length > 50) {
        showFieldError(field, 'Name must be less than 50 characters.');
        return false;
    }
    
    if (!/^[a-zA-Z\s'-]+$/.test(name)) {
        showFieldError(field, 'Name can only contain letters, spaces, hyphens, and apostrophes.');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate password
 */
function validatePassword(field) {
    const password = field.value;
    
    if (password.length < 6) {
        showFieldError(field, 'Password must be at least 6 characters long.');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

/**
 * Validate password confirmation
 */
function validatePasswordConfirmation() {
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
        showFieldError(confirmPassword, 'Passwords do not match.');
        return false;
    }
    
    clearFieldError(confirmPassword);
    return true;
}

/**
 * Show field error
 */
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.classList.add('error');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    errorDiv.setAttribute('role', 'alert');
    
    field.parentNode.appendChild(errorDiv);
    
    // Announce error to screen readers
    announceToScreenReader(message);
}

/**
 * Clear field error
 */
function clearFieldError(field) {
    field.classList.remove('error');
    
    const errorDiv = field.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
}

/**
 * Show step error
 */
function showStepError(message) {
    const messagesContainer = document.getElementById('messages');
    if (messagesContainer) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'ui-message ui-message-error';
        errorDiv.innerHTML = `<span class="ui-message-error-icon pi pi-exclamation-triangle"></span>${message}`;
        messagesContainer.appendChild(errorDiv);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    }
    
    announceToScreenReader(message);
}

/**
 * Show success message
 */
function showSuccessMessage(message) {
    const messagesContainer = document.getElementById('messages');
    if (messagesContainer) {
        const successDiv = document.createElement('div');
        successDiv.className = 'ui-message ui-message-info';
        successDiv.innerHTML = `<span class="ui-message-info-icon pi pi-check-circle"></span>${message}`;
        messagesContainer.appendChild(successDiv);
    }
    
    announceToScreenReader(message);
}

/**
 * Show loading state
 */
function showLoading(message = 'Processing...') {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        const loadingText = loadingOverlay.querySelector('.loading-text');
        if (loadingText) {
            loadingText.textContent = message;
        }
        loadingOverlay.style.display = 'flex';
    }
}

/**
 * Hide loading state
 */
function hideLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'none';
    }
}

/**
 * Announce to screen readers
 */
function announceToScreenReader(message) {
    const ariaLive = document.getElementById('aria-live-region');
    if (ariaLive) {
        ariaLive.textContent = message;
        setTimeout(() => {
            ariaLive.textContent = '';
        }, 1000);
    }
}

/**
 * Announce step change
 */
function announceStepChange() {
    const stepLabels = ['Account Information', 'Personal Information', 'Professional Information'];
    const message = `Step ${currentStep} of ${totalSteps}: ${stepLabels[currentStep - 1]}`;
    announceToScreenReader(message);
}

/**
 * Handle registration completion
 */
function handleRegistrationComplete(args) {
    if (args.validationFailed) {
        return;
    }
    
    if (args.success) {
        showSuccessMessage('Account created successfully! Please check your email for verification.');
        setTimeout(() => {
            window.location.href = '/index.xhtml';
        }, 3000);
    } else {
        showStepError('Registration failed. Please try again.');
    }
}

// Export functions for global access
window.nextStep = nextStep;
window.previousStep = previousStep;
window.handleRegistrationComplete = handleRegistrationComplete;
window.showLoading = showLoading;
window.hideLoading = hideLoading;
window.showSuccessMessage = showSuccessMessage;
