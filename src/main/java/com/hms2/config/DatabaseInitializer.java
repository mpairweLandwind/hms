package com.hms2.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.enums.AppointmentStatus;
import com.hms2.enums.DoctorStatus;
import com.hms2.enums.MedicalRecordStatus;
import com.hms2.enums.StaffStatus;
import com.hms2.enums.UserRole;
import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Department;
import com.hms2.model.Doctor;
import com.hms2.model.MedicalRecord;
import com.hms2.model.Medication;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Staff;
import com.hms2.model.User;
import com.hms2.repository.AppointmentRepository;
import com.hms2.repository.BillingRepository;
import com.hms2.repository.DepartmentRepository;
import com.hms2.repository.DoctorRepository;
import com.hms2.repository.MedicalRecordRepository;
import com.hms2.repository.MedicationRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.repository.PrescriptionMedicationRepository;
import com.hms2.repository.PrescriptionRepository;
import com.hms2.repository.StaffRepository;
import com.hms2.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Database Initializer for HMS2
 * Populates the database with sample data on first run
 */
@ApplicationScoped
public class DatabaseInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private DepartmentRepository departmentRepository;
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private StaffRepository staffRepository;
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Inject
    private MedicalRecordRepository medicalRecordRepository;
    
    @Inject
    private PrescriptionRepository prescriptionRepository;
    
    @Inject
    private MedicationRepository medicationRepository;
    
    @Inject
    private PrescriptionMedicationRepository prescriptionMedicationRepository;
    
    @Inject
    private BillingRepository billingRepository;
    
    private boolean isTablePresent(String tableName) {
        try {
            String sql = "SELECT to_regclass('public." + tableName + "')";
            Object result = entityManager.createNativeQuery(sql).getSingleResult();
            return result != null;
        } catch (Exception e) {
            logger.error("Error checking table existence: " + tableName, e);
            return false;
        }
    }
    
    @PostConstruct
    @Transactional
    public void initializeDatabase() {
        try {
            // Robust check for table existence
            if (!isTablePresent("user")) {
                logger.error("Critical table 'user' does not exist. Please ensure the schema is created before initialization.");
                return;
            }
            
            // Check if data already exists
            if (isDataAlreadyInitialized()) {
                logger.info("Database already contains data. Skipping initialization.");
                return;
            }
            
            logger.info("Starting database initialization with sample data...");
            
            // Initialize data in order of dependencies
            initializeUsers();
            initializeDepartments();
            initializeDoctors();
            initializePatients();
            initializeStaff();
            initializeMedications();
            initializeAppointments();
            initializeMedicalRecords();
            initializePrescriptions();
            initializeBillings();
            
            logger.info("Database initialization completed successfully!");
            
        } catch (Exception e) {
            logger.error("Error during database initialization", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    private boolean isDataAlreadyInitialized() {
        try {
            // Check if any users exist
            Long userCount = userRepository.count();
            return userCount > 0;
        } catch (Exception e) {
            logger.warn("Error checking if data is initialized, proceeding with initialization", e);
            return false;
        }
    }
    
    private void initializeUsers() {
        logger.info("Initializing users...");
        
        List<User> users = Arrays.asList(
            // Admin users
            createUser("admin", "admin@hms.com", "admin123", UserRole.ADMIN, "ACTIVE"),
            createUser("superadmin", "superadmin@hms.com", "super123", UserRole.ADMIN, "ACTIVE"),
            
            // Doctor users
            createUser("dr.smith", "dr.smith@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            createUser("dr.johnson", "dr.johnson@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            createUser("dr.williams", "dr.williams@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            createUser("dr.brown", "dr.brown@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            createUser("dr.davis", "dr.davis@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            createUser("dr.miller", "dr.miller@hms.com", "doctor123", UserRole.DOCTOR, "ACTIVE"),
            
            // Staff users
            createUser("nurse.jones", "nurse.jones@hms.com", "staff123", UserRole.STAFF, "ACTIVE"),
            createUser("nurse.garcia", "nurse.garcia@hms.com", "staff123", UserRole.STAFF, "ACTIVE"),
            createUser("receptionist.rodriguez", "receptionist.rodriguez@hms.com", "staff123", UserRole.STAFF, "ACTIVE"),
            createUser("technician.martinez", "technician.martinez@hms.com", "staff123", UserRole.STAFF, "ACTIVE"),
            
            // Patient users
            createUser("patient.anderson", "patient.anderson@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.taylor", "patient.taylor@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.thomas", "patient.thomas@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.jackson", "patient.jackson@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.white", "patient.white@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.harris", "patient.harris@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.martin", "patient.martin@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.thompson", "patient.thompson@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.garcia", "patient.garcia@email.com", "patient123", UserRole.PATIENT, "ACTIVE"),
            createUser("patient.martinez", "patient.martinez@email.com", "patient123", UserRole.PATIENT, "ACTIVE")
        );
        
        for (User user : users) {
            userRepository.save(user);
        }
        
        logger.info("Initialized {} users", users.size());
    }
    
    private User createUser(String username, String email, String password, UserRole role, String status) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password); // In production, this should be hashed
        user.setRole(role);
        user.setStatus(status);
        user.setCreatedBy("SYSTEM");
        return user;
    }
    
    private void initializeDepartments() {
        logger.info("Initializing departments...");
        
        List<Department> departments = Arrays.asList(
            createDepartment("Cardiology", "Heart and cardiovascular system treatment", "Floor 1, Wing A", "555-0101", "cardiology@hms.com"),
            createDepartment("Neurology", "Brain and nervous system disorders", "Floor 1, Wing B", "555-0102", "neurology@hms.com"),
            createDepartment("Orthopedics", "Bones, joints, and musculoskeletal system", "Floor 2, Wing A", "555-0103", "orthopedics@hms.com"),
            createDepartment("Pediatrics", "Children's healthcare", "Floor 2, Wing B", "555-0104", "pediatrics@hms.com"),
            createDepartment("Emergency Medicine", "Emergency and urgent care", "Ground Floor", "555-0105", "emergency@hms.com"),
            createDepartment("Radiology", "Medical imaging and diagnostics", "Floor 3, Wing A", "555-0106", "radiology@hms.com"),
            createDepartment("Oncology", "Cancer treatment and research", "Floor 3, Wing B", "555-0107", "oncology@hms.com"),
            createDepartment("General Surgery", "Surgical procedures", "Floor 4, Wing A", "555-0108", "surgery@hms.com"),
            createDepartment("Internal Medicine", "Adult medical care", "Floor 4, Wing B", "555-0109", "internal@hms.com"),
            createDepartment("Psychiatry", "Mental health and behavioral disorders", "Floor 5, Wing A", "555-0110", "psychiatry@hms.com")
        );
        
        for (Department department : departments) {
            departmentRepository.save(department);
        }
        
        logger.info("Initialized {} departments", departments.size());
    }
    
    private Department createDepartment(String name, String description, String location, String phone, String email) {
        Department department = new Department();
        department.setDepartmentName(name);
        department.setDescription(description);
        department.setLocation(location);
        department.setPhoneNumber(phone);
        department.setEmail(email);
        department.setStatus("ACTIVE");
        department.setCreatedBy("SYSTEM");
        return department;
    }
    
    private void initializeDoctors() {
        logger.info("Initializing doctors...");
        
        List<Department> departments = departmentRepository.findAll();
        
        List<Doctor> doctors = Arrays.asList(
            createDoctor("dr.smith", "John", "Smith", "dr.smith@hms.com", "555-1001", "Cardiologist", "MD12345", 15, "MD, FACC", departments.get(0)),
            createDoctor("dr.johnson", "Sarah", "Johnson", "dr.johnson@hms.com", "555-1002", "Neurologist", "MD12346", 12, "MD, PhD", departments.get(1)),
            createDoctor("dr.williams", "Michael", "Williams", "dr.williams@hms.com", "555-1003", "Orthopedic Surgeon", "MD12347", 18, "MD, FACS", departments.get(2)),
            createDoctor("dr.brown", "Emily", "Brown", "dr.brown@hms.com", "555-1004", "Pediatrician", "MD12348", 10, "MD, FAAP", departments.get(3)),
            createDoctor("dr.davis", "David", "Davis", "dr.davis@hms.com", "555-1005", "Emergency Physician", "MD12349", 8, "MD, FACEP", departments.get(4)),
            createDoctor("dr.miller", "Lisa", "Miller", "dr.miller@hms.com", "555-1006", "Radiologist", "MD12350", 14, "MD, FRCR", departments.get(5))
        );
        
        for (Doctor doctor : doctors) {
            doctorRepository.save(doctor);
        }
        
        logger.info("Initialized {} doctors", doctors.size());
    }
    
    private Doctor createDoctor(String username, String firstName, String lastName, String email, 
                               String phone, String specialization, String license, int experience, 
                               String qualifications, Department department) {
        User user = userRepository.findByUsername(username).orElse(null);
        
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phone);
        doctor.setAddress("123 Medical Center Dr, Healthcare City, HC 12345");
        doctor.setSpecialization(specialization);
        doctor.setLicenseNumber(license);
        doctor.setExperience(experience);
        doctor.setQualifications(qualifications);
        doctor.setActive(true);
        doctor.setCreatedBy("SYSTEM");
        return doctor;
    }
    
    private void initializePatients() {
        logger.info("Initializing patients...");
        
        List<Patient> patients = Arrays.asList(
            createPatient("patient.anderson", "Robert", "Anderson", "patient.anderson@email.com", "555-2001", "MALE", "A+", "Peanuts"),
            createPatient("patient.taylor", "Jennifer", "Taylor", "patient.taylor@email.com", "555-2002", "FEMALE", "O+", null),
            createPatient("patient.thomas", "Christopher", "Thomas", "patient.thomas@email.com", "555-2003", "MALE", "B+", "Shellfish"),
            createPatient("patient.jackson", "Amanda", "Jackson", "patient.jackson@email.com", "555-2004", "FEMALE", "AB+", null),
            createPatient("patient.white", "Daniel", "White", "patient.white@email.com", "555-2005", "MALE", "O-", "Dairy"),
            createPatient("patient.harris", "Jessica", "Harris", "patient.harris@email.com", "555-2006", "FEMALE", "A-", null),
            createPatient("patient.martin", "Matthew", "Martin", "patient.martin@email.com", "555-2007", "MALE", "B-", "Latex"),
            createPatient("patient.thompson", "Ashley", "Thompson", "patient.thompson@email.com", "555-2008", "FEMALE", "AB-", null),
            createPatient("patient.garcia", "James", "Garcia", "patient.garcia@email.com", "555-2009", "MALE", "O+", "Penicillin"),
            createPatient("patient.martinez", "Sofia", "Martinez", "patient.martinez@email.com", "555-2010", "FEMALE", "A+", null)
        );
        
        for (Patient patient : patients) {
            patientRepository.save(patient);
        }
        
        logger.info("Initialized {} patients", patients.size());
    }
    
    private Patient createPatient(String username, String firstName, String lastName, String email, 
                                 String phone, String gender, String bloodType, String allergies) {
        User user = userRepository.findByUsername(username).orElse(null);
        
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhoneNumber(phone);
        patient.setAddress("456 Health Street, Wellness City, WC 67890");
        patient.setDateOfBirth(Date.from(LocalDate.now().minusYears(25 + (int)(Math.random() * 50)).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        patient.setGender(gender);
        patient.setEmergencyContact(firstName + " " + lastName + " Sr.");
        patient.setEmergencyPhone("555-9999");
        patient.setInsuranceNumber("INS" + String.format("%06d", (int)(Math.random() * 999999)));
        patient.setBloodType(bloodType);
        patient.setAllergies(allergies);
        patient.setActive(true);
        patient.setCreatedBy("SYSTEM");
        return patient;
    }
    
    private void initializeStaff() {
        logger.info("Initializing staff...");
        
        List<Department> departments = departmentRepository.findAll();
        
        List<Staff> staffMembers = Arrays.asList(
            createStaff("nurse.jones", "Patricia", "Jones", "nurse.jones@hms.com", "555-3001", "Registered Nurse", departments.get(0)),
            createStaff("nurse.garcia", "Maria", "Garcia", "nurse.garcia@hms.com", "555-3002", "Licensed Practical Nurse", departments.get(1)),
            createStaff("receptionist.rodriguez", "Carlos", "Rodriguez", "receptionist.rodriguez@hms.com", "555-3003", "Receptionist", departments.get(4)),
            createStaff("technician.martinez", "Ana", "Martinez", "technician.martinez@hms.com", "555-3004", "Medical Technician", departments.get(5))
        );
        
        for (Staff staff : staffMembers) {
            staffRepository.save(staff);
        }
        
        logger.info("Initialized {} staff members", staffMembers.size());
    }
    
    private Staff createStaff(String username, String firstName, String lastName, String email, 
                             String phone, String position, Department department) {
        User user = userRepository.findByUsername(username).orElse(null);
        
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setEmail(email);
        staff.setPhoneNumber(phone);
        staff.setAddress("789 Staff Avenue, Service City, SC 11111");
        staff.setPosition(position);
        staff.setEmployeeId("EMP" + String.format("%06d", (int)(Math.random() * 999999)));
        staff.setHireDate(Date.from(LocalDate.now().minusYears(1 + (int)(Math.random() * 10)).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        staff.setSalary(new BigDecimal("45000").add(new BigDecimal((int)(Math.random() * 20000))));
        staff.setStatus(StaffStatus.VERIFIED);
        staff.setDepartment(department);
        staff.setCreatedBy("SYSTEM");
        return staff;
    }
    
    private void initializeMedications() {
        logger.info("Initializing medications...");
        
        List<Medication> medications = Arrays.asList(
            createMedication("Aspirin", "Pain reliever and blood thinner", "Tablet", "100mg", "Bayer", new BigDecimal("5.99")),
            createMedication("Ibuprofen", "Anti-inflammatory pain reliever", "Tablet", "200mg", "Advil", new BigDecimal("8.99")),
            createMedication("Acetaminophen", "Pain reliever and fever reducer", "Tablet", "500mg", "Tylenol", new BigDecimal("7.99")),
            createMedication("Lisinopril", "ACE inhibitor for blood pressure", "Tablet", "10mg", "Generic", new BigDecimal("12.99")),
            createMedication("Metformin", "Diabetes medication", "Tablet", "500mg", "Generic", new BigDecimal("15.99")),
            createMedication("Atorvastatin", "Cholesterol-lowering medication", "Tablet", "20mg", "Lipitor", new BigDecimal("25.99")),
            createMedication("Omeprazole", "Proton pump inhibitor", "Capsule", "20mg", "Prilosec", new BigDecimal("18.99")),
            createMedication("Amlodipine", "Calcium channel blocker", "Tablet", "5mg", "Norvasc", new BigDecimal("22.99")),
            createMedication("Metoprolol", "Beta blocker", "Tablet", "25mg", "Lopressor", new BigDecimal("16.99")),
            createMedication("Losartan", "Angiotensin receptor blocker", "Tablet", "50mg", "Cozaar", new BigDecimal("20.99"))
        );
        
        for (Medication medication : medications) {
            medicationRepository.save(medication);
        }
        
        logger.info("Initialized {} medications", medications.size());
    }
    
    private Medication createMedication(String name, String description, String form, String strength, 
                                       String manufacturer, BigDecimal price) {
        Medication medication = new Medication();
        medication.setMedicationName(name);
        medication.setDescription(description);
        medication.setDosageForm(form);
        medication.setStrength(strength);
        medication.setManufacturer(manufacturer);
        medication.setUnitPrice(price);
        medication.setStockQuantity(100 + (int)(Math.random() * 900));
        medication.setStatus("AVAILABLE");
        medication.setCreatedBy("SYSTEM");
        return medication;
    }
    
    private void initializeAppointments() {
        logger.info("Initializing appointments...");
        
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();
        
        for (int i = 0; i < 20; i++) {
            Doctor doctor = doctors.get(i % doctors.size());
            Patient patient = patients.get(i % patients.size());
            
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(i + 1).withHour(9 + (i % 8)).withMinute(0));
            appointment.setReason("Regular checkup");
            appointment.setAppointmentType("ROUTINE");
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setNotes("Patient scheduled for routine examination");
            appointment.setDurationMinutes(30);
            appointment.setCreatedBy("SYSTEM");
            
            appointmentRepository.save(appointment);
        }
        
        logger.info("Initialized 20 appointments");
    }
    
    private void initializeMedicalRecords() {
        logger.info("Initializing medical records...");
        
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();
        
        for (int i = 0; i < 15; i++) {
            Doctor doctor = doctors.get(i % doctors.size());
            Patient patient = patients.get(i % patients.size());
            
            MedicalRecord record = new MedicalRecord();
            record.setPatient(patient);
            record.setDoctor(doctor);
            record.setVisitDate(LocalDateTime.now().minusDays(i + 1));
            record.setChiefComplaint("High blood pressure, headache");
            record.setDiagnosis("Hypertension");
            record.setTreatmentPlan("Prescribed Lisinopril 10mg daily");
            record.setNotes("Patient shows improvement with medication");
            record.setStatus(MedicalRecordStatus.COMPLETED);
            record.setCreatedBy("SYSTEM");
            
            medicalRecordRepository.save(record);
        }
        
        logger.info("Initialized 15 medical records");
    }
    
    private void initializePrescriptions() {
        logger.info("Initializing prescriptions...");
        
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();
        List<Medication> medications = medicationRepository.findAll();
        
        for (int i = 0; i < 12; i++) {
            Doctor doctor = doctors.get(i % doctors.size());
            Patient patient = patients.get(i % patients.size());
            Medication medication = medications.get(i % medications.size());
            
            Prescription prescription = new Prescription();
            prescription.setPatient(patient);
            prescription.setDoctor(doctor);
            prescription.setPrescriptionDate(LocalDateTime.now().minusDays(i + 1));
            prescription.setStatus("ACTIVE");
            prescription.setNotes("Take as directed");
            prescription.setCreatedBy("SYSTEM");
            
            prescriptionRepository.save(prescription);
            
            // Create prescription medication
            PrescriptionMedication prescriptionMedication = new PrescriptionMedication();
            prescriptionMedication.setPrescription(prescription);
            prescriptionMedication.setMedication(medication);
            prescriptionMedication.setDosage("1 tablet");
            prescriptionMedication.setFrequency("Daily");
            prescriptionMedication.setDurationDays(30);
            prescriptionMedication.setQuantity(30);
            prescriptionMedication.setInstructions("Take with food");
            prescriptionMedication.setCreatedBy("SYSTEM");
            
            prescriptionMedicationRepository.save(prescriptionMedication);
        }
        
        logger.info("Initialized 12 prescriptions with medications");
    }
    
    private void initializeBillings() {
        logger.info("Initializing billings...");
        
        List<Patient> patients = patientRepository.findAll();
        
        for (int i = 0; i < 10; i++) {
            Patient patient = patients.get(i % patients.size());
            
            Billing billing = new Billing();
            billing.setPatient(patient);
            billing.setInvoiceNumber("INV" + String.format("%06d", i + 1));
            billing.setBillingDate(LocalDateTime.now().minusDays(i + 1));
            billing.setTotalAmount(new BigDecimal("150.00").add(new BigDecimal((int)(Math.random() * 200))));
            billing.setPaidAmount(new BigDecimal("0.00"));
            billing.setDiscountAmount(new BigDecimal("10.00"));
            billing.setTaxAmount(new BigDecimal("12.50"));
            billing.setStatus("PENDING");
            billing.setPaymentMethod("CASH");
            billing.setDescription("Consultation and examination");
            billing.setCreatedBy("SYSTEM");
            
            billingRepository.save(billing);
        }
        
        logger.info("Initialized 10 billings");
    }
} 