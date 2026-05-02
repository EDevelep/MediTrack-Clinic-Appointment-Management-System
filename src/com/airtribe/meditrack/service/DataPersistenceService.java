package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for data persistence using CSV files
 * Demonstrates File I/O, try-with-resources, and serialization
 */
public class DataPersistenceService {
    
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    
    /**
     * Constructor
     * @param doctorService doctor service
     * @param patientService patient service
     * @param appointmentService appointment service
     */
    public DataPersistenceService(DoctorService doctorService, PatientService patientService, 
                                AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }
    
    /**
     * Save all data to CSV files
     */
    public void saveAllData() {
        try {
            System.out.println("Saving data to CSV files...");
            
            saveDoctorsToCSV();
            savePatientsToCSV();
            saveAppointmentsToCSV();
            
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    /**
     * Load all data from CSV files
     */
    public void loadAllData() {
        try {
            System.out.println("Loading data from CSV files...");
            
            loadDoctorsFromCSV();
            loadPatientsFromCSV();
            loadAppointmentsFromCSV();
            
            System.out.println("All data loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    /**
     * Save doctors to CSV file
     */
    public void saveDoctorsToCSV() {
        List<List<String>> csvData = new ArrayList<>();
        
        // Add header
        csvData.add(List.of("ID", "FirstName", "LastName", "Age", "PhoneNumber", "Email", 
                           "Address", "Specialization", "LicenseNumber", "YearsOfExperience", 
                           "ConsultationFee", "Available", "CreatedAt", "UpdatedAt"));
        
        // Add doctor data
        List<Doctor> doctors = doctorService.getAllDoctors();
        for (Doctor doctor : doctors) {
            List<String> row = List.of(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                String.valueOf(doctor.getAge()),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "",
                doctor.getAddress() != null ? doctor.getAddress() : "",
                doctor.getSpecialization().toString(),
                doctor.getLicenseNumber(),
                String.valueOf(doctor.getYearsOfExperience()),
                String.valueOf(doctor.getConsultationFee()),
                String.valueOf(doctor.isAvailable()),
                DateUtil.formatDateTime(doctor.getCreatedAt()),
                DateUtil.formatDateTime(doctor.getUpdatedAt())
            );
            csvData.add(row);
        }
        
        try {
            CSVUtil.writeCSV(Constants.DOCTOR_DATA_FILE, csvData, false);
            System.out.println("Saved " + doctors.size() + " doctors to " + Constants.DOCTOR_DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save doctors: " + e.getMessage());
        }
    }
    
    /**
     * Load doctors from CSV file
     */
    public void loadDoctorsFromCSV() {
        try {
            List<List<String>> csvData = CSVUtil.readCSV(Constants.DOCTOR_DATA_FILE);
            if (csvData.isEmpty()) {
                System.out.println("No doctor data found in " + Constants.DOCTOR_DATA_FILE);
                return;
            }
            
            // Skip header row
            List<List<String>> dataRows = csvData.subList(1, csvData.size());
            int loadedCount = 0;
            
            for (List<String> row : dataRows) {
                try {
                    Doctor doctor = parseDoctorFromCSV(row);
                    doctorService.createDoctor(doctor);
                    loadedCount++;
                } catch (Exception e) {
                    System.out.println("Error loading doctor from CSV: " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + loadedCount + " doctors from " + Constants.DOCTOR_DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to read doctors CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Parse doctor from CSV row
     */
    private Doctor parseDoctorFromCSV(List<String> row) {
        if (row.size() < 13) {
            throw new IllegalArgumentException("Invalid CSV row format for doctor");
        }
        
        String id = row.get(0);
        String firstName = row.get(1);
        String lastName = row.get(2);
        int age = Integer.parseInt(row.get(3));
        String phoneNumber = row.get(4);
        String email = row.get(5).isEmpty() ? null : row.get(5);
        String address = row.get(6).isEmpty() ? null : row.get(6);
        Specialization specialization = Specialization.valueOf(row.get(7));
        String licenseNumber = row.get(8);
        int yearsOfExperience = Integer.parseInt(row.get(9));
        double consultationFee = Double.parseDouble(row.get(10));
        boolean available = Boolean.parseBoolean(row.get(11));
        LocalDateTime createdAt = DateUtil.parseDateTime(row.get(12));
        LocalDateTime updatedAt = DateUtil.parseDateTime(row.get(13));
        
        Doctor doctor = new Doctor(id, firstName, lastName, age, phoneNumber, email, address,
                                   specialization, licenseNumber, yearsOfExperience, consultationFee);
        doctor.setAvailable(available);
        doctor.setCreatedAt(createdAt);
        doctor.setUpdatedAt(updatedAt);
        
        return doctor;
    }
    
    /**
     * Save patients to CSV file
     */
    public void savePatientsToCSV() {
        List<List<String>> csvData = new ArrayList<>();
        
        // Add header
        csvData.add(List.of("ID", "FirstName", "LastName", "Age", "PhoneNumber", "Email", 
                           "Address", "MedicalHistory", "BloodGroup", "EmergencyContact", 
                           "InsuranceProvider", "InsuranceNumber", "Allergies", "CreatedAt", "UpdatedAt"));
        
        // Add patient data
        List<Patient> patients = patientService.getAllPatients();
        for (Patient patient : patients) {
            List<String> row = List.of(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                String.valueOf(patient.getAge()),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : "",
                patient.getAddress() != null ? patient.getAddress() : "",
                patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "",
                patient.getBloodGroup(),
                patient.getEmergencyContact() != null ? patient.getEmergencyContact() : "",
                patient.getInsuranceProvider() != null ? patient.getInsuranceProvider() : "",
                patient.getInsuranceNumber() != null ? patient.getInsuranceNumber() : "",
                String.join(";", patient.getAllergies()),
                DateUtil.formatDateTime(patient.getCreatedAt()),
                DateUtil.formatDateTime(patient.getUpdatedAt())
            );
            csvData.add(row);
        }
        
        try {
            CSVUtil.writeCSV(Constants.PATIENT_DATA_FILE, csvData, false);
            System.out.println("Saved " + patients.size() + " patients to " + Constants.PATIENT_DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save patients: " + e.getMessage());
        }
    }
    
    /**
     * Load patients from CSV file
     */
    public void loadPatientsFromCSV() {
        try {
            List<List<String>> csvData = CSVUtil.readCSV(Constants.PATIENT_DATA_FILE);
            if (csvData.isEmpty()) {
                System.out.println("No patient data found in " + Constants.PATIENT_DATA_FILE);
                return;
            }
            
            // Skip header row
            List<List<String>> dataRows = csvData.subList(1, csvData.size());
            int loadedCount = 0;
            
            for (List<String> row : dataRows) {
                try {
                    Patient patient = parsePatientFromCSV(row);
                    patientService.createPatient(patient);
                    loadedCount++;
                } catch (Exception e) {
                    System.out.println("Error loading patient from CSV: " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + loadedCount + " patients from " + Constants.PATIENT_DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to read patients CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Parse patient from CSV row
     */
    private Patient parsePatientFromCSV(List<String> row) {
        if (row.size() < 14) {
            throw new IllegalArgumentException("Invalid CSV row format for patient");
        }
        
        String id = row.get(0);
        String firstName = row.get(1);
        String lastName = row.get(2);
        int age = Integer.parseInt(row.get(3));
        String phoneNumber = row.get(4);
        String email = row.get(5).isEmpty() ? null : row.get(5);
        String address = row.get(6).isEmpty() ? null : row.get(6);
        String medicalHistory = row.get(7).isEmpty() ? null : row.get(7);
        String bloodGroup = row.get(8);
        String emergencyContact = row.get(9).isEmpty() ? null : row.get(9);
        String insuranceProvider = row.get(10).isEmpty() ? null : row.get(10);
        String insuranceNumber = row.get(11).isEmpty() ? null : row.get(11);
        String allergiesStr = row.get(12);
        LocalDateTime createdAt = DateUtil.parseDateTime(row.get(13));
        LocalDateTime updatedAt = DateUtil.parseDateTime(row.get(14));
        
        Patient patient = new Patient(id, firstName, lastName, age, phoneNumber, email, address,
                                    medicalHistory, bloodGroup, emergencyContact, 
                                    insuranceProvider, insuranceNumber);
        
        // Add allergies
        if (!allergiesStr.isEmpty()) {
            String[] allergies = allergiesStr.split(";");
            for (String allergy : allergies) {
                if (!allergy.trim().isEmpty()) {
                    patient.addAllergy(allergy.trim());
                }
            }
        }
        
        patient.setCreatedAt(createdAt);
        patient.setUpdatedAt(updatedAt);
        
        return patient;
    }
    
    /**
     * Save appointments to CSV file
     */
    public void saveAppointmentsToCSV() {
        List<List<String>> csvData = new ArrayList<>();
        
        // Add header
        csvData.add(List.of("ID", "PatientID", "DoctorID", "AppointmentDateTime", "EndTime", 
                           "Status", "Symptoms", "Notes", "Fee", "RoomNumber", "CreatedAt", "UpdatedAt"));
        
        // Add appointment data
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment appointment : appointments) {
            List<String> row = List.of(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                DateUtil.formatDateTime(appointment.getAppointmentDateTime()),
                DateUtil.formatDateTime(appointment.getEndTime()),
                appointment.getStatus().toString(),
                appointment.getSymptoms() != null ? appointment.getSymptoms() : "",
                appointment.getNotes() != null ? appointment.getNotes() : "",
                String.valueOf(appointment.getFee()),
                appointment.getRoomNumber() != null ? appointment.getRoomNumber() : "",
                DateUtil.formatDateTime(appointment.getCreatedAt()),
                DateUtil.formatDateTime(appointment.getUpdatedAt())
            );
            csvData.add(row);
        }
        
        try {
            CSVUtil.writeCSV(Constants.APPOINTMENT_DATA_FILE, csvData, false);
            System.out.println("Saved " + appointments.size() + " appointments to " + Constants.APPOINTMENT_DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save appointments: " + e.getMessage());
        }
    }
    
    /**
     * Load appointments from CSV file
     */
    public void loadAppointmentsFromCSV() {
        try {
            List<List<String>> csvData = CSVUtil.readCSV(Constants.APPOINTMENT_DATA_FILE);
            if (csvData.isEmpty()) {
                System.out.println("No appointment data found in " + Constants.APPOINTMENT_DATA_FILE);
                return;
            }
            
            // Skip header row
            List<List<String>> dataRows = csvData.subList(1, csvData.size());
            int loadedCount = 0;
            
            for (List<String> row : dataRows) {
                try {
                    Appointment appointment = parseAppointmentFromCSV(row);
                    appointmentService.createAppointment(appointment);
                    loadedCount++;
                } catch (Exception e) {
                    System.out.println("Error loading appointment from CSV: " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + loadedCount + " appointments from " + Constants.APPOINTMENT_DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to read appointments CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Parse appointment from CSV row
     */
    private Appointment parseAppointmentFromCSV(List<String> row) {
        if (row.size() < 11) {
            throw new IllegalArgumentException("Invalid CSV row format for appointment");
        }
        
        String id = row.get(0);
        String patientId = row.get(1);
        String doctorId = row.get(2);
        LocalDateTime appointmentDateTime = DateUtil.parseDateTime(row.get(3));
        LocalDateTime endTime = DateUtil.parseDateTime(row.get(4));
        AppointmentStatus status = AppointmentStatus.valueOf(row.get(5));
        String symptoms = row.get(6).isEmpty() ? null : row.get(6);
        String notes = row.get(7).isEmpty() ? null : row.get(7);
        double fee = Double.parseDouble(row.get(8));
        String roomNumber = row.get(9).isEmpty() ? null : row.get(9);
        LocalDateTime createdAt = DateUtil.parseDateTime(row.get(10));
        LocalDateTime updatedAt = DateUtil.parseDateTime(row.get(11));
        
        // Get patient and doctor from services
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found: " + patientId);
        }
        if (doctorOpt.isEmpty()) {
            throw new IllegalArgumentException("Doctor not found: " + doctorId);
        }
        
        Appointment appointment = new Appointment(id, patientOpt.get(), doctorOpt.get(), 
                                                appointmentDateTime, symptoms, notes, roomNumber);
        appointment.setStatus(status);
        appointment.setFee(fee);
        appointment.setCreatedAt(createdAt);
        appointment.setUpdatedAt(updatedAt);
        
        return appointment;
    }
    
    /**
     * Export data to backup files
     */
    public void createBackup() {
        try {
            String timestamp = DateUtil.formatDateTime(LocalDateTime.now()).replace(":", "-").replace(" ", "_");
            
            // Create backup files with timestamp
            String backupDir = "data/backup_" + timestamp;
            java.io.File dir = new java.io.File(backupDir);
            dir.mkdirs();
            
            // Copy current files to backup
            copyFile(Constants.DOCTOR_DATA_FILE, backupDir + "/doctors.csv");
            copyFile(Constants.PATIENT_DATA_FILE, backupDir + "/patients.csv");
            copyFile(Constants.APPOINTMENT_DATA_FILE, backupDir + "/appointments.csv");
            
            System.out.println("Backup created successfully: " + backupDir);
        } catch (Exception e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    /**
     * Copy file from source to destination
     */
    private void copyFile(String source, String destination) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             FileWriter writer = new FileWriter(destination)) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Get data statistics
     * @return formatted statistics
     */
    public String getDataStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data Persistence Statistics:\n");
        sb.append("  Doctors: ").append(doctorService.getDoctorCount()).append("\n");
        sb.append("  Patients: ").append(patientService.getPatientCount()).append("\n");
        sb.append("  Appointments: ").append(appointmentService.getAppointmentCount()).append("\n");
        sb.append("  Data Files:\n");
        sb.append("    ").append(Constants.DOCTOR_DATA_FILE).append("\n");
        sb.append("    ").append(Constants.PATIENT_DATA_FILE).append("\n");
        sb.append("    ").append(Constants.APPOINTMENT_DATA_FILE).append("\n");
        
        return sb.toString();
    }
}
