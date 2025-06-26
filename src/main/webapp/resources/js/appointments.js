// Appointment Management JavaScript

document.addEventListener("DOMContentLoaded", () => {
  initializeAppointmentManagement()
})

function initializeAppointmentManagement() {
  // Initialize date pickers with constraints
  initializeDatePickers()

  // Setup form validation
  setupFormValidation()

  // Initialize tooltips
  initializeTooltips()

  // Setup keyboard shortcuts
  setupKeyboardShortcuts()
}

function initializeDatePickers() {
  // Set minimum date to today for all appointment date pickers
  const today = new Date()
  const dateInputs = document.querySelectorAll('input[id*="appointmentDate"], input[id*="newDate"]')

  dateInputs.forEach((input) => {
    if (input.type === "text" && input.classList.contains("hasDatepicker")) {
      // Configure datepicker options
      const $ = window.jQuery // Declare the $ variable
      $(input).datepicker("option", {
        minDate: today,
        maxDate: "+1Y",
        showWeek: true,
        firstDay: 1,
        dateFormat: "dd/mm/yy",
      })
    }
  })
}

function setupFormValidation() {
  // Real-time validation for appointment forms
  const forms = document.querySelectorAll('form[id*="Appointment"]')

  forms.forEach((form) => {
    const inputs = form.querySelectorAll("input, select, textarea")

    inputs.forEach((input) => {
      input.addEventListener("blur", function () {
        validateField(this)
      })

      input.addEventListener("input", function () {
        clearFieldError(this)
      })
    })
  })
}

function validateField(field) {
  const value = field.value.trim()
  const fieldName = field.getAttribute("data-field-name") || field.name || field.id

  // Clear previous errors
  clearFieldError(field)

  // Required field validation
  if (field.hasAttribute("required") && !value) {
    showFieldError(field, `${fieldName} is required`)
    return false
  }

  // Email validation
  if (field.type === "email" && value && !isValidEmail(value)) {
    showFieldError(field, "Please enter a valid email address")
    return false
  }

  // Date validation
  if (field.type === "date" || field.classList.contains("date-field")) {
    if (value && !isValidFutureDate(value)) {
      showFieldError(field, "Please select a future date")
      return false
    }
  }

  return true
}

function showFieldError(field, message) {
  clearFieldError(field)

  const errorDiv = document.createElement("div")
  errorDiv.className = "field-error"
  errorDiv.textContent = message
  errorDiv.style.color = "#e74c3c"
  errorDiv.style.fontSize = "0.875rem"
  errorDiv.style.marginTop = "5px"

  field.parentNode.appendChild(errorDiv)
  field.classList.add("error")
}

function clearFieldError(field) {
  const errorDiv = field.parentNode.querySelector(".field-error")
  if (errorDiv) {
    errorDiv.remove()
  }
  field.classList.remove("error")
}

function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

function isValidFutureDate(dateString) {
  const selectedDate = new Date(dateString)
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return selectedDate >= today
}

function initializeTooltips() {
  // Initialize tooltips for buttons and icons
  const tooltipElements = document.querySelectorAll("[title]")

  tooltipElements.forEach((element) => {
    element.addEventListener("mouseenter", function () {
      showTooltip(this, this.getAttribute("title"))
    })

    element.addEventListener("mouseleave", () => {
      hideTooltip()
    })
  })
}

function showTooltip(element, text) {
  const tooltip = document.createElement("div")
  tooltip.className = "custom-tooltip"
  tooltip.textContent = text
  tooltip.style.cssText = `
        position: absolute;
        background: #333;
        color: white;
        padding: 8px 12px;
        border-radius: 4px;
        font-size: 0.875rem;
        z-index: 1000;
        pointer-events: none;
        white-space: nowrap;
    `

  document.body.appendChild(tooltip)

  const rect = element.getBoundingClientRect()
  tooltip.style.left = rect.left + rect.width / 2 - tooltip.offsetWidth / 2 + "px"
  tooltip.style.top = rect.top - tooltip.offsetHeight - 8 + "px"
}

function hideTooltip() {
  const tooltip = document.querySelector(".custom-tooltip")
  if (tooltip) {
    tooltip.remove()
  }
}

function setupKeyboardShortcuts() {
  document.addEventListener("keydown", (e) => {
    // Ctrl/Cmd + N for new appointment
    if ((e.ctrlKey || e.metaKey) && e.key === "n") {
      e.preventDefault()
      const newButton = document.querySelector('button[onclick*="createAppointmentDialog"]')
      if (newButton) {
        newButton.click()
      }
    }

    // Escape to close dialogs
    if (e.key === "Escape") {
      closeAllDialogs()
    }
  })
}

function closeAllDialogs() {
  // Close PrimeFaces dialogs
  const PF = window.PF // Declare the PF variable
  if (typeof PF !== "undefined") {
    ;[
      "createAppointmentDialog",
      "editAppointmentDialog",
      "cancelAppointmentDialog",
      "rescheduleAppointmentDialog",
    ].forEach((dialogName) => {
      try {
        const dialog = PF(dialogName)
        if (dialog && dialog.isVisible()) {
          dialog.hide()
        }
      } catch (e) {
        // Dialog might not exist
      }
    })
  }
}

// Appointment status management
function updateAppointmentStatus(appointmentId, newStatus) {
  // Show loading indicator
  showLoadingIndicator()

  // Make AJAX call to update status
  fetch("/api/appointments/" + appointmentId + "/status", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ status: newStatus }),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.success) {
        showSuccessMessage("Appointment status updated successfully")
        refreshAppointmentTable()
      } else {
        showErrorMessage("Failed to update appointment status")
      }
    })
    .catch((error) => {
      console.error("Error updating appointment status:", error)
      showErrorMessage("An error occurred while updating the appointment")
    })
    .finally(() => {
      hideLoadingIndicator()
    })
}

function showLoadingIndicator() {
  const loader = document.createElement("div")
  loader.id = "loading-indicator"
  loader.innerHTML = '<div class="spinner"></div><span>Processing...</span>'
  loader.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 9999;
        display: flex;
        align-items: center;
        gap: 15px;
    `

  const spinnerStyle = document.createElement("style")
  spinnerStyle.textContent = `
        .spinner {
            width: 20px;
            height: 20px;
            border: 2px solid #f3f3f3;
            border-top: 2px solid #3498db;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    `

  document.head.appendChild(spinnerStyle)
  document.body.appendChild(loader)
}

function hideLoadingIndicator() {
  const loader = document.getElementById("loading-indicator")
  if (loader) {
    loader.remove()
  }
}

function showSuccessMessage(message) {
  showMessage(message, "success")
}

function showErrorMessage(message) {
  showMessage(message, "error")
}

function showMessage(message, type) {
  const messageDiv = document.createElement("div")
  messageDiv.className = `message-toast ${type}`
  messageDiv.textContent = message
  messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 4px;
        color: white;
        font-weight: 500;
        z-index: 10000;
        animation: slideInRight 0.3s ease-out;
        max-width: 400px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    `

  if (type === "success") {
    messageDiv.style.backgroundColor = "#27ae60"
  } else if (type === "error") {
    messageDiv.style.backgroundColor = "#e74c3c"
  }

  document.body.appendChild(messageDiv)

  // Auto remove after 5 seconds
  setTimeout(() => {
    messageDiv.style.animation = "slideOutRight 0.3s ease-out"
    setTimeout(() => messageDiv.remove(), 300)
  }, 5000)
}

function refreshAppointmentTable() {
  // Trigger PrimeFaces table refresh if available
  const PF = window.PF // Declare the PF variable
  if (typeof PF !== "undefined") {
    try {
      // Refresh main appointments table
      const appointmentsTable = PF("appointmentsTable")
      if (appointmentsTable) {
        appointmentsTable.filter()
      }

      // Refresh today's appointments table
      const todaysTable = PF("todaysAppointmentsTable")
      if (todaysTable) {
        todaysTable.filter()
      }
    } catch (e) {
      // Tables might not be initialized
      console.log("Could not refresh tables:", e)
    }
  }
}

// Export functions for global access
window.AppointmentManager = {
  updateStatus: updateAppointmentStatus,
  refreshTable: refreshAppointmentTable,
  showSuccess: showSuccessMessage,
  showError: showErrorMessage,
}
