// Enhanced Welcome Page JavaScript
document.addEventListener("DOMContentLoaded", () => {
  // Initialize page
  initializePage()

  // Initialize form validation
  initializeFormValidation()

  // Initialize accessibility features
  initializeAccessibility()

  // Initialize animations
  initializeAnimations()

  // Initialize keyboard shortcuts
  initializeKeyboardShortcuts()
})

function initializePage() {
  // Announce page load to screen readers
  announceToScreenReader("HealthCare Plus welcome page loaded. Use tab to navigate or press Alt+S to sign in.")

  // Initialize smooth scrolling
  initializeSmoothScrolling()

  // Initialize loading states
  initializeLoadingStates()
}

function initializeFormValidation() {
  const forms = document.querySelectorAll("form")

  forms.forEach((form) => {
    const inputs = form.querySelectorAll("input, select, textarea")

    inputs.forEach((input) => {
      // Real-time validation
      input.addEventListener("blur", function () {
        validateField(this)
      })

      input.addEventListener("input", function () {
        if (this.classList.contains("error")) {
          validateField(this)
        }
      })
    })
  })
}

function validateField(field) {
  const value = field.value.trim()
  const type = field.type
  const required = field.hasAttribute("required")
  let isValid = true
  let errorMessage = ""

  // Clear previous errors
  clearFieldError(field)

  // Required field validation
  if (required && !value) {
    isValid = false
    errorMessage = "This field is required"
  }

  // Email validation
  if (type === "email" && value) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) {
      isValid = false
      errorMessage = "Please enter a valid email address"
    }
  }

  // Password validation
  if (type === "password" && value) {
    if (value.length < 8) {
      isValid = false
      errorMessage = "Password must be at least 8 characters long"
    }
  }

  // Username validation
  if (field.id.includes("username") || field.id.includes("Username")) {
    if (value && value.length < 3) {
      isValid = false
      errorMessage = "Username must be at least 3 characters long"
    }
  }

  // Phone validation
  if (field.id.includes("phone") || field.id.includes("Phone")) {
    if (value) {
      const phoneRegex = /^[+]?[1-9][\d]{0,15}$/
      if (!phoneRegex.test(value.replace(/[\s\-()]/g, ""))) {
        isValid = false
        errorMessage = "Please enter a valid phone number"
      }
    }
  }

  // Display error if invalid
  if (!isValid) {
    showFieldError(field, errorMessage)
  }

  return isValid
}

function showFieldError(field, message) {
  field.classList.add("error")

  const errorElement = document.createElement("div")
  errorElement.className = "field-error"
  errorElement.textContent = message
  errorElement.setAttribute("role", "alert")

  const fieldGroup = field.closest(".form-group")
  if (fieldGroup) {
    fieldGroup.appendChild(errorElement)
  }

  announceToScreenReader(message)
}

function clearFieldError(field) {
  field.classList.remove("error")

  const fieldGroup = field.closest(".form-group")
  if (fieldGroup) {
    const existingError = fieldGroup.querySelector(".field-error")
    if (existingError) {
      existingError.remove()
    }
  }
}

function initializeAccessibility() {
  // Create ARIA live region
  if (!document.getElementById("aria-live-region")) {
    const liveRegion = document.createElement("div")
    liveRegion.id = "aria-live-region"
    liveRegion.setAttribute("aria-live", "polite")
    liveRegion.setAttribute("aria-atomic", "true")
    liveRegion.className = "sr-only"
    liveRegion.style.cssText = "position: absolute; left: -10000px; width: 1px; height: 1px; overflow: hidden;"
    document.body.appendChild(liveRegion)
  }

  // Enhanced focus management
  const focusableElements = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'

  document.addEventListener("keydown", (e) => {
    if (e.key === "Tab") {
      const focusable = Array.from(document.querySelectorAll(focusableElements))
      const currentIndex = focusable.indexOf(document.activeElement)

      if (e.shiftKey) {
        // Shift + Tab (backward)
        if (currentIndex === 0) {
          e.preventDefault()
          focusable[focusable.length - 1].focus()
        }
      } else {
        // Tab (forward)
        if (currentIndex === focusable.length - 1) {
          e.preventDefault()
          focusable[0].focus()
        }
      }
    }
  })

  // Button feedback
  document.querySelectorAll("button, .btn").forEach((button) => {
    button.addEventListener("click", function () {
      const buttonText = this.textContent || this.getAttribute("aria-label") || "Button"
      announceToScreenReader(`${buttonText} activated`)
    })
  })
}

function initializeAnimations() {
  // Intersection Observer for scroll animations
  const observerOptions = {
    threshold: 0.1,
    rootMargin: "0px 0px -50px 0px",
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("animate-in")

        // Staggered animations for grid items
        if (
          entry.target.parentElement.classList.contains("features-grid") ||
          entry.target.parentElement.classList.contains("contact-grid")
        ) {
          const siblings = Array.from(entry.target.parentElement.children)
          const index = siblings.indexOf(entry.target)
          entry.target.style.animationDelay = `${index * 0.1}s`
        }
      }
    })
  }, observerOptions)

  // Observe elements
  document.querySelectorAll(".feature-card, .contact-card, .trust-item").forEach((el) => {
    observer.observe(el)
  })

  // Counter animations
  animateCounters()
}

function animateCounters() {
  const counters = document.querySelectorAll(".stat-number")

  counters.forEach((counter) => {
    const text = counter.textContent
    const target = Number.parseInt(text.replace(/[^\d]/g, ""))

    if (target) {
      const duration = 2000
      const increment = target / (duration / 16)
      let current = 0

      const updateCounter = () => {
        current += increment
        if (current < target) {
          counter.textContent = text.replace(/\d+/, Math.floor(current).toLocaleString())
          requestAnimationFrame(updateCounter)
        } else {
          counter.textContent = text.replace(/\d+/, target.toLocaleString())
        }
      }

      const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            updateCounter()
            observer.unobserve(entry.target)
          }
        })
      })

      observer.observe(counter)
    }
  })
}

function initializeSmoothScrolling() {
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault()
      const target = document.querySelector(this.getAttribute("href"))
      if (target) {
        target.scrollIntoView({
          behavior: "smooth",
          block: "start",
        })
      }
    })
  })
}

function initializeKeyboardShortcuts() {
  document.addEventListener("keydown", (e) => {
    // Alt + S for sign in focus
    if (e.altKey && e.key === "s") {
      e.preventDefault()
      const usernameField = document.getElementById("loginForm:loginUsername")
      if (usernameField) {
        usernameField.focus()
        announceToScreenReader("Sign in form focused")
      }
    }

    // Alt + R for registration
    if (e.altKey && e.key === "r") {
      e.preventDefault()
      showRegistrationForm("PATIENT")
    }

    // Escape to close dialogs
    if (e.key === "Escape") {
      const PF = window.PF
      if (typeof PF !== "undefined") {
        ;["registrationDialog", "forgotPasswordDialog"].forEach((dialogName) => {
          if (PF(dialogName) && PF(dialogName).isVisible()) {
            PF(dialogName).hide()
            announceToScreenReader(`${dialogName} closed`)
          }
        })
      }
    }
  })
}

function initializeLoadingStates() {
  // Create loading overlay if it doesn't exist
  if (!document.getElementById("loading-overlay")) {
    const overlay = document.createElement("div")
    overlay.id = "loading-overlay"
    overlay.className = "loading-overlay"
    overlay.style.display = "none"
    overlay.innerHTML = `
      <div class="loading-content">
        <div class="loading-spinner">
          <i class="pi pi-spin pi-spinner" aria-hidden="true"></i>
        </div>
        <p class="loading-text">Please wait...</p>
      </div>
    `
    document.body.appendChild(overlay)
  }
}

// Utility Functions
function scrollToSection(sectionId) {
  const section = document.getElementById(sectionId)
  if (section) {
    section.scrollIntoView({
      behavior: "smooth",
      block: "start",
    })
    announceToScreenReader(`Scrolled to ${sectionId} section`)
  }
}

function showRegistrationForm(role = "PATIENT") {
  const PF = window.PF
  if (typeof PF !== "undefined" && PF("registrationDialog")) {
    PF("registrationDialog").show()

    // Set the role after dialog opens
    setTimeout(() => {
      if (window.selectRole) {
        window.selectRole(role)
      }
    }, 100)

    announceToScreenReader(`Registration dialog opened for ${role.toLowerCase()}`)
  }
}

function showLoading(message = "Loading...") {
  const overlay = document.getElementById("loading-overlay")
  if (overlay) {
    const loadingText = overlay.querySelector(".loading-text")
    if (loadingText) {
      loadingText.textContent = message
    }
    overlay.style.display = "flex"
    announceToScreenReader(message)
  }
}

function hideLoading() {
  const overlay = document.getElementById("loading-overlay")
  if (overlay) {
    overlay.style.display = "none"
    announceToScreenReader("Loading complete")
  }
}

function announceToScreenReader(message) {
  const liveRegion = document.getElementById("aria-live-region")
  if (liveRegion) {
    liveRegion.textContent = message

    // Clear after announcement
    setTimeout(() => {
      liveRegion.textContent = ""
    }, 1000)
  }
}

// Form Enhancement Functions
function enhancePasswordField(fieldId) {
  const field = document.getElementById(fieldId)
  if (field) {
    const wrapper = field.parentElement

    // Add password strength indicator
    const strengthIndicator = document.createElement("div")
    strengthIndicator.className = "password-strength"
    strengthIndicator.innerHTML = `
      <div class="strength-bar">
        <div class="strength-fill"></div>
      </div>
      <span class="strength-text">Password strength</span>
    `
    wrapper.appendChild(strengthIndicator)

    field.addEventListener("input", function () {
      updatePasswordStrength(this, strengthIndicator)
    })
  }
}

function updatePasswordStrength(field, indicator) {
  const password = field.value
  let strength = 0
  let strengthText = "Weak"

  // Length check
  if (password.length >= 8) strength += 1
  if (password.length >= 12) strength += 1

  // Character variety checks
  if (/[a-z]/.test(password)) strength += 1
  if (/[A-Z]/.test(password)) strength += 1
  if (/[0-9]/.test(password)) strength += 1
  if (/[^A-Za-z0-9]/.test(password)) strength += 1

  // Determine strength level
  if (strength >= 5) {
    strengthText = "Very Strong"
  } else if (strength >= 4) {
    strengthText = "Strong"
  } else if (strength >= 3) {
    strengthText = "Medium"
  } else if (strength >= 2) {
    strengthText = "Fair"
  }

  // Update indicator
  const fill = indicator.querySelector(".strength-fill")
  const text = indicator.querySelector(".strength-text")

  fill.style.width = `${(strength / 6) * 100}%`
  fill.className = `strength-fill strength-${strength}`
  text.textContent = strengthText
}

// Export functions for global use
window.scrollToSection = scrollToSection
window.showRegistrationForm = showRegistrationForm
window.showLoading = showLoading
window.hideLoading = hideLoading
window.announceToScreenReader = announceToScreenReader

// Auto-initialize password fields
document.addEventListener("DOMContentLoaded", () => {
  const passwordFields = document.querySelectorAll('input[type="password"]')
  passwordFields.forEach((field) => {
    if (field.id.includes("password") || field.id.includes("Password")) {
      enhancePasswordField(field.id)
    }
  })
})
