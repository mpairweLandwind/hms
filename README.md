# Hospital Management System

A comprehensive, enterprise-grade hospital management system built with modern Java technologies.

##  Features

### Multi-Role User Management
- **Patients**: Personal health records, appointment scheduling, billing tracking
- **Doctors**: Patient management, medical records, prescription system
- **Staff**: Administrative operations, billing management, system oversight

### Core Functionality
-  **Appointment Scheduling**: Advanced booking system with conflict detection
-  **Medical Records**: Comprehensive patient history and vital signs tracking
-  **Prescription Management**: Medication tracking with inventory management
-  **Billing System**: Integrated payment processing and insurance handling
-  **Role-Based Dashboards**: Tailored interfaces for each user type
-  **Security**: BCrypt encryption, role-based access control, session management

## Technology Stack

- **Backend**: Java 21, maven ,hibernate, CDI 4.0
- **Web Framework**: JSF 4.0, PrimeFaces 13
- **ORM**: Hibernate 6.2 with JPA 3.1
- **Database**: PostgreSQL 17
- **Security**: BCrypt password hashing
- **Build Tool**: Maven 3.9
- **Testing**: JUnit 5, Mockito

## Architecture

- **Model**: Contains domain models and enums
- **Repository**: Implements data access layer with generic and entity-specific repositories
- **Service**: Encapsulates business logic for various functionalities
- **Controller**: Manages web requests and responses
- **Webapp**: Holds JSF pages and resources
- **Docs**: Includes API documentation, user guide, and developer notes
- **Scripts**: Contains automation scripts for deployment and testing

## Getting Started

1. Clone the repository
2. Configure PostgreSQL database
3. Update hibernate.cfg.xml with your database credentials
4. Run `mvn clean install`
5. Deploy to your application server

## License

MIT License