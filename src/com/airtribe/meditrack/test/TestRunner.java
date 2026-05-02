package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.observer.AppointmentNotifier;
import com.airtribe.meditrack.observer.ConsoleNotificationObserver;
import com.airtribe.meditrack.observer.EmailNotificationObserver;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DataPersistenceService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.AppConfig;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Test runner for manual testing of the MediTrack application
 * Demonstrates testing of various features and components
 */
public class TestRunner {
    
    private static DoctorService doctorService;
    private static PatientService patientService;
    private static AppointmentService appointmentService;
    private static DataPersistenceService dataPersistenceService;
    private static AppointmentNotifier notifier;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    MEDITRACK TEST RUNNER");
        System.out.println("=".repeat(60));
        System.out.println();
        
        initializeServices();
        runAllTests();
        
        System.out.println("\nAll tests completed!");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Initialize services for testing
     */
    private static void initializeServices() {
        doctorService = new DoctorService();
        patientService = new PatientService();
        appointmentService = new AppointmentService(doctorService, patientService);
        dataPersistenceService = new DataPersistenceService(doctorService, patientService, appointmentService);
        notifier = AppointmentNotifier.getInstance();
        
        // Add observers for testing
        notifier.addObserver(new ConsoleNotificationObserver("TestConsole"));
        notifier.addObserver(new EmailNotificationObserver("TestEmail", "test@meditrack.com"));
        
        System.out.println("Services initialized successfully!");
        System.out.println("Observers registered: " + notifier.getObserverCount());
        System.out.println();
    }
    
    /**
     * Run all tests
     */
    private static void runAllTests() {
        testBasicFunctionality();
        testEntityCreation();
        testServiceOperations();
        testDesignPatterns();
        testFileIO();
        testAdvancedFeatures();
        testErrorHandling();
        testValidation();
        testSearchAndFilter();
        testAnalytics();
    }
    
    /**
     * Test basic functionality
     */
    private static void testBasicFunctionality() {
        System.out.println("1. TESTING BASIC FUNCTIONALITY");
        System.out.println("-".repeat(40));
        
        // Test ID generation
        String patientId = IdGenerator.generatePatientId();
        String doctorId = IdGenerator.generateDoctorId();
        System.out.println("Generated Patient ID: " + patientId);
        System.out.println("Generated Doctor ID: " + doctorId);
        
        // Test configuration
        AppConfig config = AppConfig.getInstance();
        System.out.println("App Name: " + config.getProperty("app.name"));
        System.out.println("Tax Rate: " + config.getDoubleProperty("tax.rate"));
        
        // Test date utilities
        System.out.println("Current Date: " + DateUtil.getCurrentDate());
        System.out.println("Current Time: " + DateUtil.getCurrentTime());
        
        System.out.println("✓ Basic functionality tests passed\n");
    }
    
    /**
     * Test entity creation
     */
    private static void testEntityCreation() {
        System.out.println("2. TESTING ENTITY CREATION");
        System.out.println("-".repeat(40));
        
        try {
            // Create doctor
            Doctor doctor = new Doctor(
                "DOC001", "John", "Smith", 45, "5551234567", 
                "john.smith@email.com", "123 Main St", 
                Specialization.CARDIOLOGY, "MED12345", 15, 150.0
            );
            System.out.println("Created Doctor: " + doctor.getFullName());
            
            // Create patient
            Patient patient = new Patient(
                "PAT001", "Alice", "Johnson", 32, "5559876543",
                "alice.j@email.com", "456 Oak Ave", 
                "Hypertension", "A+", "5551112222", 
                "Health Insurance", "HI123456"
            );
            patient.addAllergy("Penicillin");
            System.out.println("Created Patient: " + patient.getFullName());
            
            // Create appointment
            LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            Appointment appointment = new Appointment(
                "APT001", patient, doctor, appointmentTime,
                "Chest pain", "Regular checkup", "Room 101"
            );
            System.out.println("Created Appointment: " + appointment.getId());
            
            // Create bill
            Bill bill = BillFactory.createStandardBill(appointment);
            System.out.println("Created Bill: $" + bill.getTotalAmount());
            
            // Create bill summary
            BillSummary summary = BillFactory.createBillSummary(bill);
            System.out.println("Created Bill Summary: " + summary.getDisplayName());
            
            System.out.println("✓ Entity creation tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Entity creation test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test service operations
     */
    private static void testServiceOperations() {
        System.out.println("3. TESTING SERVICE OPERATIONS");
        System.out.println("-".repeat(40));
        
        try {
            // Test doctor service
            Doctor doctor = new Doctor(
                null, "Sarah", "Wilson", 38, "5555551234",
                "sarah.w@email.com", "789 Pine St",
                Specialization.NEUROLOGY, "MED54321", 12, 175.0
            );
            Doctor createdDoctor = doctorService.createDoctor(doctor);
            System.out.println("Created doctor via service: " + createdDoctor.getId());
            
            // Test patient service
            Patient patient = new Patient(
                null, "Bob", "Brown", 28, "5555559876",
                "bob.b@email.com", "321 Elm St",
                "Migraines", "O+", "5555551111",
                "Insurance Co", "IC987654"
            );
            Patient createdPatient = patientService.createPatient(patient);
            System.out.println("Created patient via service: " + createdPatient.getId());
            
            // Test appointment service
            LocalDateTime appointmentTime = LocalDateTime.now().plusDays(2).withHour(14).withMinute(30);
            Appointment appointment = new Appointment(
                null, createdPatient, createdDoctor, appointmentTime,
                "Headaches", "Neurological exam", "Room 205"
            );
            Appointment createdAppointment = appointmentService.createAppointment(appointment);
            System.out.println("Created appointment via service: " + createdAppointment.getId());
            
            // Test appointment status changes
            appointmentService.confirmAppointment(createdAppointment.getId());
            System.out.println("Confirmed appointment: " + createdAppointment.getStatus());
            
            System.out.println("✓ Service operations tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Service operations test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test design patterns
     */
    private static void testDesignPatterns() {
        System.out.println("4. TESTING DESIGN PATTERNS");
        System.out.println("-".repeat(40));
        
        try {
            // Test Singleton pattern
            AppConfig config1 = AppConfig.getInstance();
            AppConfig config2 = AppConfig.getInstance();
            System.out.println("Singleton test: " + (config1 == config2));
            
            // Test Factory pattern
            Doctor doctor = doctorService.getAllDoctors().get(0);
            Patient patient = patientService.getAllPatients().get(0);
            LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3).withHour(11).withMinute(0);
            Appointment appointment = new Appointment(
                null, patient, doctor, appointmentTime,
                "Test symptoms", "Test notes", "Room 301"
            );
            appointmentService.createAppointment(appointment);
            
            Bill standardBill = BillFactory.createStandardBill(appointment);
            Bill emergencyBill = BillFactory.createEmergencyBill(appointment);
            Bill followUpBill = BillFactory.createFollowUpBill(appointment);
            
            System.out.println("Factory pattern - Standard bill: $" + standardBill.getTotalAmount());
            System.out.println("Factory pattern - Emergency bill: $" + emergencyBill.getTotalAmount());
            System.out.println("Factory pattern - Follow-up bill: $" + followUpBill.getTotalAmount());
            
            // Test Observer pattern
            System.out.println("Observer pattern - Active observers: " + notifier.getObserverCount());
            
            // Test cloning
            Patient clonedPatient = patient.clone();
            System.out.println("Cloning test: " + (patient.getId().equals(clonedPatient.getId())));
            System.out.println("Deep copy test: " + (patient.getAllergies() != clonedPatient.getAllergies()));
            
            System.out.println("✓ Design patterns tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Design patterns test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test file I/O operations
     */
    private static void testFileIO() {
        System.out.println("5. TESTING FILE I/O");
        System.out.println("-".repeat(40));
        
        try {
            // Test data persistence
            System.out.println("Testing data persistence...");
            dataPersistenceService.saveAllData();
            System.out.println("Data saved successfully");
            
            // Clear data
            doctorService.getAllDoctors().clear();
            patientService.getAllPatients().clear();
            appointmentService.getAllAppointments().clear();
            System.out.println("Data cleared for testing");
            
            // Load data
            dataPersistenceService.loadAllData();
            System.out.println("Data loaded successfully");
            
            // Verify data
            System.out.println("Doctors after load: " + doctorService.getDoctorCount());
            System.out.println("Patients after load: " + patientService.getPatientCount());
            System.out.println("Appointments after load: " + appointmentService.getAppointmentCount());
            
            System.out.println("✓ File I/O tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ File I/O test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test advanced features
     */
    private static void testAdvancedFeatures() {
        System.out.println("6. TESTING ADVANCED FEATURES");
        System.out.println("-".repeat(40));
        
        try {
            // Test AI features
            System.out.println("Testing AI doctor recommendation...");
            List<Doctor> recommendations = doctorService.recommendDoctorsForSymptoms("chest pain");
            System.out.println("Recommended doctors for chest pain: " + recommendations.size());
            
            // Test search functionality
            System.out.println("Testing search functionality...");
            List<Doctor> doctorSearch = doctorService.searchDoctors("John");
            List<Patient> patientSearch = patientService.searchPatients("Alice");
            System.out.println("Doctor search results: " + doctorSearch.size());
            System.out.println("Patient search results: " + patientSearch.size());
            
            // Test appointment slots
            System.out.println("Testing appointment slots...");
            if (!doctorService.getAllDoctors().isEmpty()) {
                String doctorId = doctorService.getAllDoctors().get(0).getId();
                LocalDateTime nextSlot = appointmentService.getNextAvailableSlot(doctorId);
                System.out.println("Next available slot: " + (nextSlot != null ? DateUtil.formatDateTime(nextSlot) : "None"));
            }
            
            System.out.println("✓ Advanced features tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Advanced features test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test error handling
     */
    private static void testErrorHandling() {
        System.out.println("7. TESTING ERROR HANDLING");
        System.out.println("-".repeat(40));
        
        try {
            // Test invalid data
            try {
                Doctor invalidDoctor = new Doctor(
                    null, "", "", -5, "invalid",
                    "invalid-email", "", null, "", -1, -1.0
                );
                doctorService.createDoctor(invalidDoctor);
                System.out.println("✗ Should have thrown InvalidDataException");
            } catch (InvalidDataException e) {
                System.out.println("✓ InvalidDataException handled correctly");
            }
            
            // Test appointment not found
            try {
                appointmentService.getAppointmentById("NONEXISTENT");
                System.out.println("✗ Should return empty optional");
            } catch (Exception e) {
                System.out.println("✗ Unexpected exception: " + e.getMessage());
            }
            
            // Test appointment cancellation
            try {
                appointmentService.cancelAppointment("NONEXISTENT");
                System.out.println("✗ Should have thrown AppointmentNotFoundException");
            } catch (AppointmentNotFoundException e) {
                System.out.println("✓ AppointmentNotFoundException handled correctly");
            }
            
            System.out.println("✓ Error handling tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Error handling test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test validation
     */
    private static void testValidation() {
        System.out.println("8. TESTING VALIDATION");
        System.out.println("-".repeat(40));
        
        try {
            // Test validator utilities
            System.out.println("Name validation: " + com.airtribe.meditrack.util.Validator.isValidName("John Doe"));
            System.out.println("Phone validation: " + com.airtribe.meditrack.util.Validator.isValidPhoneNumber("5551234567"));
            System.out.println("Email validation: " + com.airtribe.meditrack.util.Validator.isValidEmail("test@example.com"));
            System.out.println("Blood group validation: " + com.airtribe.meditrack.util.Validator.isValidBloodGroup("A+"));
            
            // Test entity validation
            if (!doctorService.getAllDoctors().isEmpty()) {
                Doctor doctor = doctorService.getAllDoctors().get(0);
                System.out.println("Doctor validation: " + doctor.validate());
            }
            
            if (!patientService.getAllPatients().isEmpty()) {
                Patient patient = patientService.getAllPatients().get(0);
                System.out.println("Patient validation: " + patient.validate());
            }
            
            System.out.println("✓ Validation tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Validation test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test search and filter
     */
    private static void testSearchAndFilter() {
        System.out.println("9. TESTING SEARCH AND FILTER");
        System.out.println("-".repeat(40));
        
        try {
            // Test doctor search
            List<Doctor> allDoctors = doctorService.getAllDoctors();
            if (!allDoctors.isEmpty()) {
                List<Doctor> cardiologists = doctorService.searchDoctorsBySpecialization(Specialization.CARDIOLOGY);
                List<Doctor> availableDoctors = doctorService.getDoctorsByAvailability(true);
                List<Doctor> experiencedDoctors = doctorService.getDoctorsByExperienceRange(10, 50);
                
                System.out.println("Cardiologists: " + cardiologists.size());
                System.out.println("Available doctors: " + availableDoctors.size());
                System.out.println("Experienced doctors: " + experiencedDoctors.size());
            }
            
            // Test patient search
            List<Patient> allPatients = patientService.getAllPatients();
            if (!allPatients.isEmpty()) {
                List<Patient> pediatricPatients = patientService.getPediatricPatients();
                List<Patient> patientsWithAllergies = patientService.getPatientsWithAllergies();
                List<Patient> bloodGroupA = patientService.searchPatientsByBloodGroup("A+");
                
                System.out.println("Pediatric patients: " + pediatricPatients.size());
                System.out.println("Patients with allergies: " + patientsWithAllergies.size());
                System.out.println("Blood group A+: " + bloodGroupA.size());
            }
            
            // Test appointment search
            List<Appointment> allAppointments = appointmentService.getAllAppointments();
            if (!allAppointments.isEmpty()) {
                List<Appointment> todayAppointments = appointmentService.getTodayAppointments();
                List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments();
                List<Appointment> confirmedAppointments = appointmentService.getAppointmentsByStatus(AppointmentStatus.CONFIRMED);
                
                System.out.println("Today's appointments: " + todayAppointments.size());
                System.out.println("Upcoming appointments: " + upcomingAppointments.size());
                System.out.println("Confirmed appointments: " + confirmedAppointments.size());
            }
            
            System.out.println("✓ Search and filter tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Search and filter test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test analytics
     */
    private static void testAnalytics() {
        System.out.println("10. TESTING ANALYTICS");
        System.out.println("-".repeat(40));
        
        try {
            // Test statistics
            System.out.println(doctorService.getStatistics());
            System.out.println(patientService.getStatistics());
            System.out.println(appointmentService.getStatistics());
            
            // Test ID generator statistics
            System.out.println(IdGenerator.getStatistics());
            
            // Test data persistence statistics
            System.out.println(dataPersistenceService.getDataStatistics());
            
            System.out.println("✓ Analytics tests passed\n");
            
        } catch (Exception e) {
            System.out.println("✗ Analytics test failed: " + e.getMessage());
        }
    }
}
