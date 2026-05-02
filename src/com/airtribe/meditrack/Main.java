package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main application class with menu-driven console UI
 * Demonstrates command-line usage, user interaction, and application flow
 */
public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final DoctorService doctorService = new DoctorService();
    private static final PatientService patientService = new PatientService();
    private static final AppointmentService appointmentService = new AppointmentService(doctorService, patientService);
    private static boolean loadData = false;
    
    public static void main(String[] args) {
        // Check for --loadData command line argument
        for (String arg : args) {
            if ("--loadData".equals(arg)) {
                loadData = true;
                break;
            }
        }
        
        System.out.println("=".repeat(60));
        System.out.println("    " + com.airtribe.meditrack.constants.Constants.APP_NAME);
        System.out.println("    Version: " + com.airtribe.meditrack.constants.Constants.VERSION);
        System.out.println("=".repeat(60));
        System.out.println();
        
        if (loadData) {
            System.out.println("Loading saved data...");
            // TODO: Implement data loading from CSV files
            System.out.println("Data loading completed.");
            System.out.println();
        }
        
        runMainMenu();
    }
    
    /**
     * Run the main menu
     */
    private static void runMainMenu() {
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        manageDoctors();
                        break;
                    case 2:
                        managePatients();
                        break;
                    case 3:
                        manageAppointments();
                        break;
                    case 4:
                        manageBills();
                        break;
                    case 5:
                        searchEntities();
                        break;
                    case 6:
                        viewStatistics();
                        break;
                    case 7:
                        aiFeatures();
                        break;
                    case 8:
                        if (confirmExit()) {
                            System.out.println("Thank you for using MediTrack!");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println();
            pressEnterToContinue();
        }
    }
    
    /**
     * Display main menu options
     */
    private static void displayMainMenu() {
        System.out.println("MAIN MENU");
        System.out.println("1. Manage Doctors");
        System.out.println("2. Manage Patients");
        System.out.println("3. Manage Appointments");
        System.out.println("4. Manage Bills");
        System.out.println("5. Search");
        System.out.println("6. View Statistics");
        System.out.println("7. AI Features");
        System.out.println("8. Exit");
        System.out.println("-".repeat(30));
    }
    
    
    private static void manageDoctors() {
        while (true) {
            displayDoctorsMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        createDoctor();
                        break;
                    case 2:
                        viewAllDoctors();
                        break;
                    case 3:
                        searchDoctors();
                        break;
                    case 4:
                        updateDoctor();
                        break;
                    case 5:
                        deleteDoctor();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println();
            pressEnterToContinue();
        }
    }
    
    /**
     * Display doctors menu
     */
    private static void displayDoctorsMenu() {
        System.out.println("DOCTORS MENU");
        System.out.println("1. Add New Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. Search Doctors");
        System.out.println("4. Update Doctor");
        System.out.println("5. Delete Doctor");
        System.out.println("6. Back to Main Menu");
        System.out.println("-".repeat(30));
    }
    
    /**
     * Create a new doctor
     */
    private static void createDoctor() {
        System.out.println("ADD NEW DOCTOR");
        System.out.println("-".repeat(20));
        
        try {
            String firstName = getStringInput("First Name: ");
            String lastName = getStringInput("Last Name: ");
            int age = getIntInput("Age: ");
            String phoneNumber = getStringInput("Phone Number (10 digits): ");
            String email = getStringInput("Email (optional): ");
            String address = getStringInput("Address (optional): ");
            
            System.out.println("\nAvailable Specializations:");
            Specialization[] specializations = Specialization.values();
            for (int i = 0; i < specializations.length; i++) {
                System.out.println((i + 1) + ". " + specializations[i].getDisplayName());
            }
            
            int specChoice = getIntInput("Select Specialization: ");
            if (specChoice < 1 || specChoice > specializations.length) {
                System.out.println("Invalid specialization selection.");
                return;
            }
            
            Specialization specialization = specializations[specChoice - 1];
            String licenseNumber = getStringInput("License Number: ");
            int yearsOfExperience = getIntInput("Years of Experience: ");
            double consultationFee = getDoubleInput("Consultation Fee: ");
            
            Doctor doctor = new Doctor(
                null, firstName, lastName, age, phoneNumber, email, address,
                specialization, licenseNumber, yearsOfExperience, consultationFee
            );
            
            Doctor created = doctorService.createDoctor(doctor);
            System.out.println("Doctor created successfully!");
            System.out.println("Doctor ID: " + created.getId());
            
        } catch (InvalidDataException e) {
            System.out.println("Failed to create doctor: " + e.getMessage());
        }
    }
    
    /**
     * View all doctors
     */
    private static void viewAllDoctors() {
        System.out.println("ALL DOCTORS");
        System.out.println("-".repeat(20));
        
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        
        System.out.printf("%-12s %-20s %-15s %-10s %-10s %-10s%n",
            "ID", "Name", "Specialization", "Experience", "Fee", "Available");
        System.out.println("-".repeat(80));
        
        for (Doctor doctor : doctors) {
            System.out.printf("%-12s %-20s %-15s %-10d %-10.2f %-10s%n",
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialization().getDisplayName(),
                doctor.getYearsOfExperience(),
                doctor.getConsultationFee(),
                doctor.isAvailable() ? "Yes" : "No"
            );
        }
        
        System.out.println("\nTotal Doctors: " + doctors.size());
    }
    
    /**
     * Search doctors
     */
    private static void searchDoctors() {
        System.out.println("SEARCH DOCTORS");
        System.out.println("-".repeat(20));
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Specialization");
        System.out.println("4. Search by Availability");
        
        int choice = getIntInput("Search by: ");
        List<Doctor> results;
        
        switch (choice) {
            case 1:
                String id = getStringInput("Enter Doctor ID: ");
                results = doctorService.searchDoctorsById(id);
                break;
            case 2:
                String name = getStringInput("Enter Doctor Name: ");
                results = doctorService.searchDoctorsByName(name);
                break;
            case 3:
                System.out.println("Available Specializations:");
                Specialization[] specializations = Specialization.values();
                for (int i = 0; i < specializations.length; i++) {
                    System.out.println((i + 1) + ". " + specializations[i].getDisplayName());
                }
                int specChoice = getIntInput("Select Specialization: ");
                if (specChoice < 1 || specChoice > specializations.length) {
                    System.out.println("Invalid specialization selection.");
                    return;
                }
                results = doctorService.searchDoctorsBySpecialization(specializations[specChoice - 1]);
                break;
            case 4:
                boolean available = getYesNoInput("Show available doctors only? ");
                results = doctorService.getDoctorsByAvailability(available);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        displaySearchResults(results);
    }
    
    /**
     * Update doctor
     */
    private static void updateDoctor() {
        System.out.println("UPDATE DOCTOR");
        System.out.println("-".repeat(20));
        
        String doctorId = getStringInput("Enter Doctor ID: ");
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found.");
            return;
        }
        
        Doctor doctor = doctorOpt.get();
        System.out.println("Current Information:");
        System.out.println("Name: " + doctor.getFullName());
        System.out.println("Specialization: " + doctor.getSpecialization().getDisplayName());
        System.out.println("Experience: " + doctor.getYearsOfExperience() + " years");
        System.out.println("Fee: $" + doctor.getConsultationFee());
        System.out.println("Available: " + (doctor.isAvailable() ? "Yes" : "No"));
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Consultation Fee");
        System.out.println("2. Availability");
        System.out.println("3. Years of Experience");
        
        int choice = getIntInput("Update: ");
        
        try {
            switch (choice) {
                case 1:
                    double newFee = getDoubleInput("New Consultation Fee: ");
                    doctor.setConsultationFee(newFee);
                    break;
                case 2:
                    boolean newAvailability = getYesNoInput("Available? ");
                    doctor.setAvailable(newAvailability);
                    break;
                case 3:
                    int newExperience = getIntInput("New Years of Experience: ");
                    doctor.setYearsOfExperience(newExperience);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
            doctorService.updateDoctor(doctor);
            System.out.println("Doctor updated successfully!");
            
        } catch (InvalidDataException e) {
            System.out.println("Failed to update doctor: " + e.getMessage());
        }
    }
    
    /**
     * Delete doctor
     */
    private static void deleteDoctor() {
        System.out.println("DELETE DOCTOR");
        System.out.println("-".repeat(20));
        
        String doctorId = getStringInput("Enter Doctor ID: ");
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found.");
            return;
        }
        
        Doctor doctor = doctorOpt.get();
        System.out.println("Doctor: " + doctor.getFullName());
        System.out.println("Specialization: " + doctor.getSpecialization().getDisplayName());
        
        if (getYesNoInput("Are you sure you want to delete this doctor? ")) {
            if (doctorService.deleteDoctor(doctorId)) {
                System.out.println("Doctor deleted successfully!");
            } else {
                System.out.println("Failed to delete doctor.");
            }
        }
    }
    
    /**
     * Manage patients menu
     */
    private static void managePatients() {
        while (true) {
            displayPatientsMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        createPatient();
                        break;
                    case 2:
                        viewAllPatients();
                        break;
                    case 3:
                        searchPatients();
                        break;
                    case 4:
                        updatePatient();
                        break;
                    case 5:
                        deletePatient();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println();
            pressEnterToContinue();
        }
    }
    
    /**
     * Display patients menu
     */
    private static void displayPatientsMenu() {
        System.out.println("PATIENTS MENU");
        System.out.println("1. Add New Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Search Patients");
        System.out.println("4. Update Patient");
        System.out.println("5. Delete Patient");
        System.out.println("6. Back to Main Menu");
        System.out.println("-".repeat(30));
    }
    
    /**
     * Create a new patient
     */
    private static void createPatient() {
        System.out.println("ADD NEW PATIENT");
        System.out.println("-".repeat(20));
        
        try {
            String firstName = getStringInput("First Name: ");
            String lastName = getStringInput("Last Name: ");
            int age = getIntInput("Age: ");
            String phoneNumber = getStringInput("Phone Number (10 digits): ");
            String email = getStringInput("Email (optional): ");
            String address = getStringInput("Address (optional): ");
            String bloodGroup = getStringInput("Blood Group (e.g., A+, O-, etc.): ");
            String emergencyContact = getStringInput("Emergency Contact (optional): ");
            String insuranceProvider = getStringInput("Insurance Provider (optional): ");
            String insuranceNumber = getStringInput("Insurance Number (optional): ");
            
            Patient patient = new Patient(
                null, firstName, lastName, age, phoneNumber, email, address,
                null, bloodGroup, emergencyContact, insuranceProvider, insuranceNumber
            );
            
            // Add allergies if any
            if (getYesNoInput("Does the patient have any allergies? ")) {
                boolean addingAllergies = true;
                while (addingAllergies) {
                    String allergy = getStringInput("Enter allergy (or press Enter to finish): ");
                    if (allergy.trim().isEmpty()) {
                        addingAllergies = false;
                    } else {
                        patient.addAllergy(allergy);
                    }
                }
            }
            
            Patient created = patientService.createPatient(patient);
            System.out.println("Patient created successfully!");
            System.out.println("Patient ID: " + created.getId());
            
        } catch (InvalidDataException e) {
            System.out.println("Failed to create patient: " + e.getMessage());
        }
    }
    
    /**
     * View all patients
     */
    private static void viewAllPatients() {
        System.out.println("ALL PATIENTS");
        System.out.println("-".repeat(20));
        
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        
        System.out.printf("%-12s %-20s %-8s %-12s %-10s %-10s%n",
            "ID", "Name", "Age", "Phone", "Blood", "Allergies");
        System.out.println("-".repeat(80));
        
        for (Patient patient : patients) {
            System.out.printf("%-12s %-20s %-8d %-12s %-10s %-10d%n",
                patient.getId(),
                patient.getFullName(),
                patient.getAge(),
                patient.getPhoneNumber(),
                patient.getBloodGroup(),
                patient.getAllergyCount()
            );
        }
        
        System.out.println("\nTotal Patients: " + patients.size());
    }
    
    /**
     * Search patients
     */
    private static void searchPatients() {
        System.out.println("SEARCH PATIENTS");
        System.out.println("-".repeat(20));
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Age");
        System.out.println("4. Search by Blood Group");
        System.out.println("5. Search by Phone Number");
        
        int choice = getIntInput("Search by: ");
        List<Patient> results;
        
        switch (choice) {
            case 1:
                String id = getStringInput("Enter Patient ID: ");
                results = patientService.searchPatientsById(id);
                break;
            case 2:
                String name = getStringInput("Enter Patient Name: ");
                results = patientService.searchPatientsByName(name);
                break;
            case 3:
                int age = getIntInput("Enter Age: ");
                results = patientService.searchPatientsByAge(age);
                break;
            case 4:
                String bloodGroup = getStringInput("Enter Blood Group: ");
                results = patientService.searchPatientsByBloodGroup(bloodGroup);
                break;
            case 5:
                String phone = getStringInput("Enter Phone Number: ");
                results = patientService.searchPatientsByPhoneNumber(phone);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        displaySearchResults(results);
    }
    
    /**
     * Update patient
     */
    private static void updatePatient() {
        System.out.println("UPDATE PATIENT");
        System.out.println("-".repeat(20));
        
        String patientId = getStringInput("Enter Patient ID: ");
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }
        
        Patient patient = patientOpt.get();
        System.out.println("Current Information:");
        System.out.println("Name: " + patient.getFullName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Blood Group: " + patient.getBloodGroup());
        System.out.println("Allergies: " + patient.getAllergies());
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Add Allergy");
        System.out.println("2. Remove Allergy");
        System.out.println("3. Medical History");
        System.out.println("4. Insurance Information");
        
        int choice = getIntInput("Update: ");
        
        try {
            switch (choice) {
                case 1:
                    String allergy = getStringInput("Enter allergy to add: ");
                    if (patientService.addAllergyToPatient(patientId, allergy)) {
                        System.out.println("Allergy added successfully!");
                    } else {
                        System.out.println("Failed to add allergy.");
                    }
                    break;
                case 2:
                    String allergyToRemove = getStringInput("Enter allergy to remove: ");
                    if (patientService.removeAllergyFromPatient(patientId, allergyToRemove)) {
                        System.out.println("Allergy removed successfully!");
                    } else {
                        System.out.println("Failed to remove allergy.");
                    }
                    break;
                case 3:
                    String medicalHistory = getStringInput("Enter medical history: ");
                    if (patientService.updateMedicalHistory(patientId, medicalHistory)) {
                        System.out.println("Medical history updated successfully!");
                    } else {
                        System.out.println("Failed to update medical history.");
                    }
                    break;
                case 4:
                    System.out.println("Insurance update feature coming soon.");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
        } catch (Exception e) {
            System.out.println("Failed to update patient: " + e.getMessage());
        }
    }
    
    /**
     * Delete patient
     */
    private static void deletePatient() {
        System.out.println("DELETE PATIENT");
        System.out.println("-".repeat(20));
        
        String patientId = getStringInput("Enter Patient ID: ");
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }
        
        Patient patient = patientOpt.get();
        System.out.println("Patient: " + patient.getFullName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Blood Group: " + patient.getBloodGroup());
        
        if (getYesNoInput("Are you sure you want to delete this patient? ")) {
            if (patientService.deletePatient(patientId)) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("Failed to delete patient.");
            }
        }
    }
    
    /**
     * Manage appointments menu
     */
    private static void manageAppointments() {
        while (true) {
            displayAppointmentsMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        createAppointment();
                        break;
                    case 2:
                        viewAllAppointments();
                        break;
                    case 3:
                        searchAppointments();
                        break;
                    case 4:
                        updateAppointment();
                        break;
                    case 5:
                        cancelAppointment();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println();
            pressEnterToContinue();
        }
    }
    
    /**
     * Display appointments menu
     */
    private static void displayAppointmentsMenu() {
        System.out.println("APPOINTMENTS MENU");
        System.out.println("1. Schedule New Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. Search Appointments");
        System.out.println("4. Update Appointment");
        System.out.println("5. Cancel Appointment");
        System.out.println("6. Back to Main Menu");
        System.out.println("-".repeat(30));
    }
    
    /**
     * Create a new appointment
     */
    private static void createAppointment() {
        System.out.println("SCHEDULE NEW APPOINTMENT");
        System.out.println("-".repeat(30));
        
        try {
            // Select patient
            String patientId = getStringInput("Enter Patient ID: ");
            Optional<Patient> patientOpt = patientService.getPatientById(patientId);
            if (patientOpt.isEmpty()) {
                System.out.println("Patient not found.");
                return;
            }
            Patient patient = patientOpt.get();
            
            // Select doctor
            String doctorId = getStringInput("Enter Doctor ID: ");
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            if (doctorOpt.isEmpty()) {
                System.out.println("Doctor not found.");
                return;
            }
            Doctor doctor = doctorOpt.get();
            
            // Check if doctor is available
            if (!doctor.isAvailable()) {
                System.out.println("Doctor is not available for appointments.");
                return;
            }
            
            // Get appointment date and time
            System.out.println("Available time slots for " + doctor.getFullName() + ":");
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, tomorrow);
            
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots found for tomorrow.");
                return;
            }
            
            System.out.println("Available slots for " + DateUtil.formatDate(tomorrow.toLocalDate()) + ":");
            for (int i = 0; i < Math.min(availableSlots.size(), 10); i++) {
                System.out.println((i + 1) + ". " + DateUtil.formatTime(availableSlots.get(i).toLocalTime()));
            }
            
            int slotChoice = getIntInput("Select time slot (1-" + Math.min(availableSlots.size(), 10) + "): ");
            if (slotChoice < 1 || slotChoice > availableSlots.size()) {
                System.out.println("Invalid slot selection.");
                return;
            }
            
            LocalDateTime appointmentDateTime = availableSlots.get(slotChoice - 1);
            
            // Get symptoms and notes
            String symptoms = getStringInput("Symptoms (optional): ");
            String notes = getStringInput("Notes (optional): ");
            String roomNumber = getStringInput("Room Number (optional): ");
            
            Appointment appointment = new Appointment(
                null, patient, doctor, appointmentDateTime, symptoms, notes, roomNumber
            );
            
            Appointment created = appointmentService.createAppointment(appointment);
            System.out.println("Appointment scheduled successfully!");
            System.out.println("Appointment ID: " + created.getId());
            System.out.println("Date & Time: " + DateUtil.formatDateTime(created.getAppointmentDateTime()));
            
        } catch (InvalidDataException e) {
            System.out.println("Failed to schedule appointment: " + e.getMessage());
        }
    }
    
    /**
     * View all appointments
     */
    private static void viewAllAppointments() {
        System.out.println("ALL APPOINTMENTS");
        System.out.println("-".repeat(20));
        
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        
        System.out.printf("%-12s %-20s %-20s %-20s %-10s %-10s%n",
            "ID", "Patient", "Doctor", "Date & Time", "Status", "Fee");
        System.out.println("-".repeat(100));
        
        for (Appointment appointment : appointments) {
            System.out.printf("%-12s %-20s %-20s %-20s %-10s $%-9.2f%n",
                appointment.getId(),
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                DateUtil.formatDateTime(appointment.getAppointmentDateTime()),
                appointment.getStatus().getDisplayName(),
                appointment.getFee()
            );
        }
        
        System.out.println("\nTotal Appointments: " + appointments.size());
    }
    
    /**
     * Search appointments
     */
    private static void searchAppointments() {
        System.out.println("SEARCH APPOINTMENTS");
        System.out.println("-".repeat(20));
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Patient");
        System.out.println("3. Search by Doctor");
        System.out.println("4. Search by Status");
        System.out.println("5. Search by Date");
        System.out.println("6. Today's Appointments");
        
        int choice = getIntInput("Search by: ");
        List<Appointment> results;
        
        switch (choice) {
            case 1:
                String id = getStringInput("Enter Appointment ID: ");
                results = appointmentService.searchAppointments(id);
                break;
            case 2:
                String patientId = getStringInput("Enter Patient ID: ");
                results = appointmentService.getAppointmentsByPatient(patientId);
                break;
            case 3:
                String doctorId = getStringInput("Enter Doctor ID: ");
                results = appointmentService.getAppointmentsByDoctor(doctorId);
                break;
            case 4:
                System.out.println("Available Statuses:");
                AppointmentStatus[] statuses = AppointmentStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
                }
                int statusChoice = getIntInput("Select Status: ");
                if (statusChoice < 1 || statusChoice > statuses.length) {
                    System.out.println("Invalid status selection.");
                    return;
                }
                results = appointmentService.getAppointmentsByStatus(statuses[statusChoice - 1]);
                break;
            case 5:
                String dateStr = getStringInput("Enter Date (YYYY-MM-DD): ");
                try {
                    LocalDateTime date = DateUtil.parseDateTime(dateStr + " 00:00");
                    results = appointmentService.getAppointmentsByDate(date);
                } catch (Exception e) {
                    System.out.println("Invalid date format.");
                    return;
                }
                break;
            case 6:
                results = appointmentService.getTodayAppointments();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        displaySearchResults(results);
    }
    
    /**
     * Update appointment
     */
    private static void updateAppointment() {
        System.out.println("UPDATE APPOINTMENT");
        System.out.println("-".repeat(20));
        
        String appointmentId = getStringInput("Enter Appointment ID: ");
        Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(appointmentId);
        
        if (appointmentOpt.isEmpty()) {
            System.out.println("Appointment not found.");
            return;
        }
        
        Appointment appointment = appointmentOpt.get();
        System.out.println("Current Information:");
        System.out.println("Patient: " + appointment.getPatient().getFullName());
        System.out.println("Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("Date & Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("Status: " + appointment.getStatus().getDisplayName());
        System.out.println("Symptoms: " + (appointment.getSymptoms() != null ? appointment.getSymptoms() : "None"));
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Confirm Appointment");
        System.out.println("2. Complete Appointment");
        System.out.println("3. Mark as No Show");
        System.out.println("4. Update Symptoms");
        System.out.println("5. Update Notes");
        
        int choice = getIntInput("Update: ");
        
        try {
            switch (choice) {
                case 1:
                    appointmentService.confirmAppointment(appointmentId);
                    System.out.println("Appointment confirmed!");
                    break;
                case 2:
                    appointmentService.completeAppointment(appointmentId);
                    System.out.println("Appointment completed!");
                    break;
                case 3:
                    appointmentService.markNoShow(appointmentId);
                    System.out.println("Appointment marked as no-show!");
                    break;
                case 4:
                    String newSymptoms = getStringInput("Enter new symptoms: ");
                    appointment.setSymptoms(newSymptoms);
                    appointmentService.updateAppointment(appointment);
                    System.out.println("Symptoms updated!");
                    break;
                case 5:
                    String newNotes = getStringInput("Enter new notes: ");
                    appointment.setNotes(newNotes);
                    appointmentService.updateAppointment(appointment);
                    System.out.println("Notes updated!");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("Failed to update appointment: " + e.getMessage());
        }
    }
    
    /**
     * Cancel appointment
     */
    private static void cancelAppointment() {
        System.out.println("CANCEL APPOINTMENT");
        System.out.println("-".repeat(20));
        
        String appointmentId = getStringInput("Enter Appointment ID: ");
        Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(appointmentId);
        
        if (appointmentOpt.isEmpty()) {
            System.out.println("Appointment not found.");
            return;
        }
        
        Appointment appointment = appointmentOpt.get();
        System.out.println("Appointment: " + appointment.getPatient().getFullName() + " with Dr. " + appointment.getDoctor().getFullName());
        System.out.println("Date & Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("Status: " + appointment.getStatus().getDisplayName());
        
        if (getYesNoInput("Are you sure you want to cancel this appointment? ")) {
            try {
                appointmentService.cancelAppointment(appointmentId);
                System.out.println("Appointment cancelled successfully!");
            } catch (AppointmentNotFoundException | InvalidDataException e) {
                System.out.println("Failed to cancel appointment: " + e.getMessage());
            }
        }
    }
    
    /**
     * Manage bills menu
     */
    private static void manageBills() {
        System.out.println("MANAGE BILLS");
        System.out.println("-".repeat(20));
        System.out.println("Bill management features coming soon...");
        System.out.println("This will include:");
        System.out.println("- Create bills from appointments");
        System.out.println("- Process payments");
        System.out.println("- Generate bill summaries");
        System.out.println("- View billing history");
    }
    
    /**
     * Search entities menu
     */
    private static void searchEntities() {
        System.out.println("SEARCH ENTITIES");
        System.out.println("-".repeat(20));
        String query = getStringInput("Enter search query: ");
        
        System.out.println("\nSEARCH RESULTS");
        System.out.println("-".repeat(20));
        
        // Search doctors
        List<Doctor> doctorResults = doctorService.searchDoctors(query);
        if (!doctorResults.isEmpty()) {
            System.out.println("Doctors (" + doctorResults.size() + "):");
            for (Doctor doctor : doctorResults) {
                System.out.println("  - " + doctor.getFullName() + " (" + doctor.getSpecialization().getDisplayName() + ")");
            }
            System.out.println();
        }
        
        // Search patients
        List<Patient> patientResults = patientService.searchPatients(query);
        if (!patientResults.isEmpty()) {
            System.out.println("Patients (" + patientResults.size() + "):");
            for (Patient patient : patientResults) {
                System.out.println("  - " + patient.getFullName() + " (Age: " + patient.getAge() + ")");
            }
            System.out.println();
        }
        
        // Search appointments
        List<Appointment> appointmentResults = appointmentService.searchAppointments(query);
        if (!appointmentResults.isEmpty()) {
            System.out.println("Appointments (" + appointmentResults.size() + "):");
            for (Appointment appointment : appointmentResults) {
                System.out.println("  - " + appointment.getPatient().getFullName() + " with Dr. " + 
                    appointment.getDoctor().getFullName() + " on " + 
                    DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
            }
        }
        
        if (doctorResults.isEmpty() && patientResults.isEmpty() && appointmentResults.isEmpty()) {
            System.out.println("No results found for '" + query + "'");
        }
    }
    
    /**
     * View statistics
     */
    private static void viewStatistics() {
        System.out.println("SYSTEM STATISTICS");
        System.out.println("=".repeat(50));
        
        System.out.println("\n" + doctorService.getStatistics());
        System.out.println("\n" + patientService.getStatistics());
        System.out.println("\n" + appointmentService.getStatistics());
        
        System.out.println("\n" + com.airtribe.meditrack.util.IdGenerator.getStatistics());
    }
    
    /**
     * AI features menu
     */
    private static void aiFeatures() {
        System.out.println("AI FEATURES");
        System.out.println("-".repeat(20));
        System.out.println("1. Doctor Recommendation by Symptoms");
        System.out.println("2. Find Available Appointment Slots");
        System.out.println("3. Analytics Dashboard");
        
        int choice = getIntInput("Select feature: ");
        
        switch (choice) {
            case 1:
                recommendDoctorsBySymptoms();
                break;
            case 2:
                findAvailableSlots();
                break;
            case 3:
                showAnalytics();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Recommend doctors by symptoms
     */
    private static void recommendDoctorsBySymptoms() {
        System.out.println("DOCTOR RECOMMENDATION BY SYMPTOMS");
        System.out.println("-".repeat(40));
        
        String symptoms = getStringInput("Enter patient symptoms: ");
        List<Doctor> recommendations = doctorService.recommendDoctorsForSymptoms(symptoms);
        
        if (recommendations.isEmpty()) {
            System.out.println("No doctors available for these symptoms.");
            return;
        }
        
        System.out.println("\nRECOMMENDED DOCTORS:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s %-15s %-10s %-10s%n",
            "Name", "Specialization", "Rating", "Fee");
        System.out.println("-".repeat(60));
        
        for (int i = 0; i < Math.min(recommendations.size(), 5); i++) {
            Doctor doctor = recommendations.get(i);
            System.out.printf("%-20s %-15s %-10.1f $%-9.2f%n",
                doctor.getFullName(),
                doctor.getSpecialization().getDisplayName(),
                doctor.calculateRating(),
                doctor.getConsultationFee()
            );
        }
    }
    
    /**
     * Find available appointment slots
     */
    private static void findAvailableSlots() {
        System.out.println("FIND AVAILABLE APPOINTMENT SLOTS");
        System.out.println("-".repeat(40));
        
        String doctorId = getStringInput("Enter Doctor ID: ");
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found.");
            return;
        }
        
        Doctor doctor = doctorOpt.get();
        System.out.println("Doctor: " + doctor.getFullName());
        
        LocalDateTime nextSlot = appointmentService.getNextAvailableSlot(doctorId);
        if (nextSlot != null) {
            System.out.println("Next available slot: " + DateUtil.formatDateTime(nextSlot));
        } else {
            System.out.println("No available slots found in the next 30 days.");
        }
    }
    
    /**
     * Show analytics dashboard
     */
    private static void showAnalytics() {
        System.out.println("ANALYTICS DASHBOARD");
        System.out.println("=".repeat(50));
        
        // Doctor analytics
        List<Doctor> doctors = doctorService.getAllDoctors();
        double avgDoctorExperience = doctors.stream()
            .mapToInt(Doctor::getYearsOfExperience)
            .average()
            .orElse(0.0);
        
        // Patient analytics
        List<Patient> patients = patientService.getAllPatients();
        double avgPatientAge = patients.stream()
            .mapToInt(Patient::getAge)
            .average()
            .orElse(0.0);
        
        // Appointment analytics
        List<Appointment> appointments = appointmentService.getAllAppointments();
        long todayAppointments = appointments.stream()
            .filter(Appointment::isToday)
            .count();
        
        System.out.println("Key Metrics:");
        System.out.println("  Average Doctor Experience: " + String.format("%.1f", avgDoctorExperience) + " years");
        System.out.println("  Average Patient Age: " + String.format("%.1f", avgPatientAge) + " years");
        System.out.println("  Today's Appointments: " + todayAppointments);
        
        // Top specializations
        System.out.println("\nTop Specializations:");
        doctors.stream()
            .collect(java.util.stream.Collectors.groupingBy(Doctor::getSpecialization, java.util.stream.Collectors.counting()))
            .entrySet()
            .stream()
            .sorted(java.util.Map.Entry.<com.airtribe.meditrack.entity.Specialization, Long>comparingByValue().reversed())
            .limit(3)
            .forEach(entry -> System.out.println("  " + entry.getKey().getDisplayName() + ": " + entry.getValue() + " doctors"));
    }
    
    /**
     * Display search results
     */
    private static <T> void displaySearchResults(List<T> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        
        System.out.println("\nSEARCH RESULTS (" + results.size() + " found):");
        System.out.println("-".repeat(40));
        
        for (T result : results) {
            System.out.println(result.toString());
        }
    }
    
    /**
     * Confirm exit
     */
    private static boolean confirmExit() {
        return getYesNoInput("Are you sure you want to exit? ");
    }
    
    /**
     * Press enter to continue
     */
    private static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Get string input from user
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Get integer input from user
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get double input from user
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get yes/no input from user
     */
    private static boolean getYesNoInput(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) {
                return true;
            } else if (input.equals("N") || input.equals("NO")) {
                return false;
            } else {
                System.out.println("Please enter Y or N.");
            }
        }
    }
}
