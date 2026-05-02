package com.airtribe.meditrack.constants;

/**
 * Application constants
 * Demonstrates static initialization and constants usage
 */
public class Constants {
    
    // Tax rates
    public static final double TAX_RATE = 0.15; // 15% tax
    public static final double SERVICE_CHARGE = 0.05; // 5% service charge
    
    // File paths
    public static final String PATIENT_DATA_FILE = "data/patients.csv";
    public static final String DOCTOR_DATA_FILE = "data/doctors.csv";
    public static final String APPOINTMENT_DATA_FILE = "data/appointments.csv";
    public static final String BILL_DATA_FILE = "data/bills.csv";
    
    // Application settings
    public static final int MAX_APPOINTMENTS_PER_DAY = 50;
    public static final int APPOINTMENT_DURATION_MINUTES = 30;
    public static final double CONSULTATION_FEE = 100.0;
    
    // ID generation
    public static final String PATIENT_ID_PREFIX = "PAT";
    public static final String DOCTOR_ID_PREFIX = "DOC";
    public static final String APPOINTMENT_ID_PREFIX = "APT";
    public static final String BILL_ID_PREFIX = "BIL";
    
    // Validation constants
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    public static final int PHONE_NUMBER_LENGTH = 10;
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    
    // Application metadata
    public static final String APP_NAME = "MediTrack Clinic Appointment Management System";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "MediTrack Team";
    
    // Static initialization block
    static {
        System.out.println("Constants class loaded - initializing application settings");
        System.out.println("Tax Rate: " + (TAX_RATE * 100) + "%");
        System.out.println("Service Charge: " + (SERVICE_CHARGE * 100) + "%");
    }
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}
