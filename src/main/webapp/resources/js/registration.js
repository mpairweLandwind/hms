// Registration form step management
let currentStep = 1
const totalSteps = 3

document.addEventListener("DOMContentLoaded", () => {
  initializeRegistrationForm()
})

function initializeRegistrationForm() {
  updateStepDisplay()
  updateNavigationButtons()
  initializeFormValidation()
}

function nextStep() {
  if (validateCurrentStep()) {
    if (currentStep < totalSteps) {
      hideCurrentStep()
      currentStep++
      showCurrentStep()
      updateStepDisplay()
      updateNavigationButtons()
      announceStepChange()
    }
  }
}

function previousStep() {
  if (currentStep > 1) {
    hideCurrentStep()
    currentStep--
    showCurrentStep()
    updateStepDisplay()
    updateNavigationButtons()
    announceStepChange()
  }
}

function hideCurrentStep() {
  const currentStepElement = document.querySelector(`[data-step="${currentStep}"]`)
  if (currentStepElement) {
    currentStepElement.classList.remove("active")
  }
}

function showCurrentStep() {
  const nextStepElement = document.querySelector(`[data-step="${currentStep}"]`)
  if (nextStepElement) {
    nextStepElement.classList.add("active")

    // Focus first input in the step
    const firstInput = nextStepElement.querySelector("input, select, textarea")
    if (firstInput) {
      setTimeout(() => firstInput.focus(), 100)
    }
  }
}

function updateStepDisplay() {
  // Update progress steps
  document.querySelectorAll(".step").forEach((step, index) => {
    const stepNumber = index + 1
    step.classList.remove("active", "completed")

    if (stepNumber < currentStep) {
      step.classList.add("completed")
    } else if (stepNumber === currentStep) {
      step.classList.add("active")
    }
  })
}

function updateNavigationButtons() {
  const prevBtn = document.querySelector(".prev-btn")
  const nextBtn = document.querySelector(".next-btn")
  const submitBtn = document.querySelector(".submit-btn")

  // Previous button
  if (prevBtn) {
    prevBtn.style.display = currentStep > 1 ? "inline-flex" : "none"
  }

  // Next/Submit button
  if (currentStep === totalSteps) {
    if (nextBtn) nextBtn.style.display = "none"
    if (submitBtn) submitBtn.style.display = "inline-flex"
  } else {
    if (nextBtn) nextBtn.style.display = "inline-flex"
    if (submitBtn) submitBtn.style.display = "none"
  }
}

function validateCurrentStep() {
  const currentStepElement = document.querySelector(`[data-step="${currentStep}"]`)
  if (!currentStepElement) return true

  const requiredFields = currentStepElement.querySelectorAll("[required]")
  let isValid = true

  requiredFields.forEach((field) => {
    if (!validateField(field)) {
      isValid = false
    }
  })

  return isValid
}

function validateField(field) {
  const value = field.value ? field.value.trim() : ""
  const fieldType = field.type
  const isRequired = field.hasAttribute("required")

  // Clear previous errors
  clearFieldError(field)

  // Required validation
  if (isRequired && !value) {
    showFieldError(field, "This field is required")
    return false
  }

  if (!value) return true // Skip other validations if field is empty and not required

  // Email validation
  if (fieldType === "email" || field.id.toLowerCase().includes("email")) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) {
      showFieldError(field, "Please enter a valid email address")
      return false
    }
  }

  // Password validation
  if (fieldType === "password") {
    if (value.length < 8) {
      showFieldError(field, "Password must be at least 8 characters long")
      return false
    }

    // Check for password confirmation
    const confirmField = document.querySelector('#confirmPassword, [id$="confirmPassword"]')
    if (confirmField && field.id.toLowerCase().includes("password") && !field.id.toLowerCase().includes("confirm")) {
      if (confirmField.value && confirmField.value !== value) {
        showFieldError(confirmField, "Passwords do not match")
        return false
      }
    }
  }

  // Phone validation
  if (field.id.toLowerCase().includes("phone")) {
    const phoneRegex = /^[+]?[1-9][\d]{0,15}$/
    if (!phoneRegex.test(value.replace(/[\s\-$$$$]/g, ""))) {
      showFieldError(field, "Please enter a valid phone number")
      return false
    }
  }

  return true
}

function showFieldError(field, message) {
  field.classList.add("error")

  const errorElement = document.createElement("div")
  errorElement.className = "field-error"
  errorElement.textContent = message
  errorElement.setAttribute("role", "alert")

  const formGroup = field.closest(".form-group")
  if (formGroup) {
    formGroup.appendChild(errorElement)
  }

  // Announce to screen readers
  if (window.announceToScreenReader) {
    window.announceToScreenReader(message)
  }
}

function clearFieldError(field) {
  field.classList.remove("error")

  const formGroup = field.closest(".form-group")
  if (formGroup) {
    const existingError = formGroup.querySelector(".field-error")
    if (existingError) {
      existingError.remove()
    }
  }
}

function initializeFormValidation() {
  const form = document.querySelector(".registration-form")
  if (!form) return

  const inputs = form.querySelectorAll("input, select, textarea")

  inputs.forEach((input) => {
    input.addEventListener("blur", function () {
      validateField(this)
    })

    input.addEventListener("input", function () {
      if (this.classList.contains("error")) {
        validateField(this)
      }
    })
  })
}

function announceStepChange() {
  const stepTitles = ["Account Information", "Personal Information", "Role-Specific Information"]
  const title = stepTitles[currentStep - 1] || `Step ${currentStep}`

  if (window.announceToScreenReader) {
    window.announceToScreenReader(`Now on ${title}, step ${currentStep} of ${totalSteps}`)
  }
}

function showSuccessMessage(message) {
  // Create and show success notification
  const notification = document.createElement("div")
  notification.className = "success-notification"
  notification.innerHTML = `
        <div class="notification-content">
            <i class="pi pi-check-circle"></i>
            <span>${message}</span>
        </div>
    `

  document.body.appendChild(notification)

  // Auto remove after 5 seconds
  setTimeout(() => {
    if (notification.parentNode) {
      notification.parentNode.removeChild(notification)
    }
  }, 5000)

  if (window.announceToScreenReader) {
    window.announceToScreenReader(message)
  }
}

// Export functions for global use
window.nextStep = nextStep
window.previousStep = previousStep
window.showSuccessMessage = showSuccessMessage
