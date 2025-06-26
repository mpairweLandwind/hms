// Welcome Page JavaScript
document.addEventListener("DOMContentLoaded", () => {
  // Initialize AOS (Animate On Scroll)
  const AOS = window.AOS // Declare AOS variable
  if (typeof AOS !== "undefined") {
    AOS.init({
      duration: 800,
      easing: "ease-out-cubic",
      once: true,
      offset: 100,
    })
  }

  // Counter Animation
  animateCounters()

  // Smooth scrolling for anchor links
  initSmoothScrolling()

  // Parallax effects
  initParallaxEffects()

  // Intersection Observer for animations
  initIntersectionObserver()

  // Keyboard navigation
  initKeyboardNavigation()

  // Accessibility announcements
  initAccessibilityFeatures()
})

// Counter Animation
function animateCounters() {
  const counters = document.querySelectorAll("[data-count]")

  counters.forEach((counter) => {
    const target = Number.parseInt(counter.getAttribute("data-count"))
    const duration = 2000 // 2 seconds
    const increment = target / (duration / 16) // 60fps
    let current = 0

    const updateCounter = () => {
      current += increment
      if (current < target) {
        counter.textContent = Math.floor(current).toLocaleString()
        requestAnimationFrame(updateCounter)
      } else {
        counter.textContent = target.toLocaleString()
      }
    }

    // Start animation when element is visible
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          updateCounter()
          observer.unobserve(entry.target)
        }
      })
    })

    observer.observe(counter)
  })
}

// Smooth Scrolling
function initSmoothScrolling() {
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

// Scroll to section function
function scrollToSection(sectionId) {
  const section = document.getElementById(sectionId)
  if (section) {
    section.scrollIntoView({
      behavior: "smooth",
      block: "start",
    })

    // Announce to screen readers
    announceToScreenReader(`Scrolled to ${sectionId} section`)
  }
}

// Parallax Effects
function initParallaxEffects() {
  const parallaxElements = document.querySelectorAll(".hero-visual, .floating-card")

  window.addEventListener("scroll", () => {
    const scrolled = window.pageYOffset
    const rate = scrolled * -0.5

    parallaxElements.forEach((element) => {
      if (element.classList.contains("hero-visual")) {
        element.style.transform = `translateY(${rate * 0.3}px)`
      } else if (element.classList.contains("floating-card")) {
        element.style.transform = `translateY(${rate * 0.1}px)`
      }
    })
  })
}

// Intersection Observer for animations
function initIntersectionObserver() {
  const observerOptions = {
    threshold: 0.1,
    rootMargin: "0px 0px -50px 0px",
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("animate-in")

        // Add staggered animation for grid items
        if (
          entry.target.parentElement.classList.contains("features-grid") ||
          entry.target.parentElement.classList.contains("testimonials-grid")
        ) {
          const siblings = Array.from(entry.target.parentElement.children)
          const index = siblings.indexOf(entry.target)
          entry.target.style.animationDelay = `${index * 0.1}s`
        }
      }
    })
  }, observerOptions)

  // Observe elements
  document.querySelectorAll(".feature-card, .testimonial-card, .section-header").forEach((el) => {
    observer.observe(el)
  })
}

// Keyboard Navigation
function initKeyboardNavigation() {
  // Global keyboard shortcuts
  const PF = window.PF // Declare PF variable
  document.addEventListener("keydown", (e) => {
    // Alt + L for login
    if (e.altKey && e.key === "l") {
      e.preventDefault()
      if (typeof PF !== "undefined" && PF("loginDialog")) {
        PF("loginDialog").show()
        announceToScreenReader("Login dialog opened")
      }
    }

    // Alt + R for register
    if (e.altKey && e.key === "r") {
      e.preventDefault()
      if (typeof PF !== "undefined" && PF("registerDialog")) {
        PF("registerDialog").show()
        announceToScreenReader("Registration dialog opened")
      }
    }

    // Escape to close dialogs
    if (e.key === "Escape") {
      if (typeof PF !== "undefined") {
        ;["loginDialog", "registerDialog", "demoDialog"].forEach((dialogName) => {
          if (PF(dialogName) && PF(dialogName).isVisible()) {
            PF(dialogName).hide()
            announceToScreenReader(`${dialogName} closed`)
          }
        })
      }
    }
  })

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
}

// Accessibility Features
function initAccessibilityFeatures() {
  // Create ARIA live region if it doesn't exist
  if (!document.getElementById("aria-live-region")) {
    const liveRegion = document.createElement("div")
    liveRegion.id = "aria-live-region"
    liveRegion.setAttribute("aria-live", "polite")
    liveRegion.setAttribute("aria-atomic", "true")
    liveRegion.className = "sr-only"
    document.body.appendChild(liveRegion)
  }

  // Announce page sections when they come into view
  const sectionObserver = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const sectionName =
            entry.target.getAttribute("aria-labelledby") ||
            entry.target.querySelector("h2, h3")?.textContent ||
            "Section"
          announceToScreenReader(`Entered ${sectionName} section`)
        }
      })
    },
    { threshold: 0.5 },
  )

  document.querySelectorAll('section[role="region"]').forEach((section) => {
    sectionObserver.observe(section)
  })

  // Enhanced button feedback
  document.querySelectorAll("button, .btn").forEach((button) => {
    button.addEventListener("click", function () {
      const buttonText = this.textContent || this.getAttribute("aria-label") || "Button"
      announceToScreenReader(`${buttonText} activated`)
    })
  })
}

// Screen Reader Announcements
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

// Form Validation Enhancement
function enhanceFormValidation() {
  const forms = document.querySelectorAll("form")

  forms.forEach((form) => {
    const inputs = form.querySelectorAll("input, select, textarea")

    inputs.forEach((input) => {
      // Real-time validation feedback
      input.addEventListener("blur", function () {
        validateField(this)
      })

      input.addEventListener("input", function () {
        if (this.classList.contains("error")) {
          validateField(this)
        }
      })
    })

    // Form submission
    form.addEventListener("submit", (e) => {
      let isValid = true

      inputs.forEach((input) => {
        if (!validateField(input)) {
          isValid = false
        }
      })

      if (!isValid) {
        e.preventDefault()
        const firstError = form.querySelector(".error")
        if (firstError) {
          firstError.focus()
          announceToScreenReader("Please correct the errors in the form")
        }
      }
    })
  })
}

// Field Validation
function validateField(field) {
  const value = field.value.trim()
  const type = field.type
  const required = field.hasAttribute("required")
  let isValid = true
  let errorMessage = ""

  // Clear previous errors
  field.classList.remove("error")
  const existingError = field.parentElement.querySelector(".error-message")
  if (existingError) {
    existingError.remove()
  }

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

  // Phone validation
  if (field.name === "phone" && value) {
    const phoneRegex = /^[+]?[1-9][\d]{0,15}$/
    if (!phoneRegex.test(value.replace(/[\s\-$$$$]/g, ""))) {
      isValid = false
      errorMessage = "Please enter a valid phone number"
    }
  }

  // Display error if invalid
  if (!isValid) {
    field.classList.add("error")
    const errorElement = document.createElement("div")
    errorElement.className = "error-message"
    errorElement.textContent = errorMessage
    errorElement.setAttribute("role", "alert")
    field.parentElement.appendChild(errorElement)

    announceToScreenReader(errorMessage)
  }

  return isValid
}

// Loading States
function showLoading(message = "Loading...") {
  const overlay = document.getElementById("loading-overlay")
  if (overlay) {
    overlay.querySelector("p").textContent = message
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

// Utility Functions
function debounce(func, wait) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

function throttle(func, limit) {
  let inThrottle
  return function () {
    const args = arguments
    
    if (!inThrottle) {
      func.apply(this, args)
      inThrottle = true
      setTimeout(() => (inThrottle = false), limit)
    }
  }
}

// Export functions for global use
window.scrollToSection = scrollToSection
window.announceToScreenReader = announceToScreenReader
window.showLoading = showLoading
window.hideLoading = hideLoading
