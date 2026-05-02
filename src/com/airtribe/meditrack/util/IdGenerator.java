package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating unique IDs
 * Demonstrates static initialization, AtomicInteger, and thread safety
 */
public final class IdGenerator {
    
    // Counters for different entity types
    private static final AtomicInteger PATIENT_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger DOCTOR_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger APPOINTMENT_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger BILL_COUNTER = new AtomicInteger(1);
    
    // Static initialization block
    static {
        System.out.println("IdGenerator initialized - ready to generate IDs");
    }
    
    // Private constructor to prevent instantiation
    private IdGenerator() {
        throw new AssertionError("IdGenerator class should not be instantiated");
    }
    
    /**
     * Generate a new patient ID
     * @return unique patient ID
     */
    public static synchronized String generatePatientId() {
        int counter = PATIENT_COUNTER.getAndIncrement();
        return Constants.PATIENT_ID_PREFIX + String.format("%06d", counter);
    }
    
    /**
     * Generate a new doctor ID
     * @return unique doctor ID
     */
    public static synchronized String generateDoctorId() {
        int counter = DOCTOR_COUNTER.getAndIncrement();
        return Constants.DOCTOR_ID_PREFIX + String.format("%06d", counter);
    }
    
    /**
     * Generate a new appointment ID
     * @return unique appointment ID
     */
    public static synchronized String generateAppointmentId() {
        int counter = APPOINTMENT_COUNTER.getAndIncrement();
        return Constants.APPOINTMENT_ID_PREFIX + String.format("%06d", counter);
    }
    
    /**
     * Generate a new bill ID
     * @return unique bill ID
     */
    public static synchronized String generateBillId() {
        int counter = BILL_COUNTER.getAndIncrement();
        return Constants.BILL_ID_PREFIX + String.format("%06d", counter);
    }
    
    /**
     * Generate an ID with custom prefix
     * @param prefix custom prefix
     * @return unique ID with custom prefix
     */
    public static synchronized String generateCustomId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        
        // Use a simple timestamp-based approach for custom IDs
        long timestamp = System.currentTimeMillis();
        return prefix.trim().toUpperCase() + String.format("%010d", timestamp);
    }
    
    /**
     * Get current patient counter value
     * @return current patient counter
     */
    public static int getPatientCounter() {
        return PATIENT_COUNTER.get();
    }
    
    /**
     * Get current doctor counter value
     * @return current doctor counter
     */
    public static int getDoctorCounter() {
        return DOCTOR_COUNTER.get();
    }
    
    /**
     * Get current appointment counter value
     * @return current appointment counter
     */
    public static int getAppointmentCounter() {
        return APPOINTMENT_COUNTER.get();
    }
    
    /**
     * Get current bill counter value
     * @return current bill counter
     */
    public static int getBillCounter() {
        return BILL_COUNTER.get();
    }
    
    /**
     * Reset all counters (for testing purposes)
     */
    public static synchronized void resetAllCounters() {
        PATIENT_COUNTER.set(1);
        DOCTOR_COUNTER.set(1);
        APPOINTMENT_COUNTER.set(1);
        BILL_COUNTER.set(1);
        System.out.println("All ID counters have been reset");
    }
    
    /**
     * Reset patient counter (for testing purposes)
     * @param newValue new counter value
     */
    public static synchronized void resetPatientCounter(int newValue) {
        if (newValue > 0) {
            PATIENT_COUNTER.set(newValue);
        }
    }
    
    /**
     * Reset doctor counter (for testing purposes)
     * @param newValue new counter value
     */
    public static synchronized void resetDoctorCounter(int newValue) {
        if (newValue > 0) {
            DOCTOR_COUNTER.set(newValue);
        }
    }
    
    /**
     * Reset appointment counter (for testing purposes)
     * @param newValue new counter value
     */
    public static synchronized void resetAppointmentCounter(int newValue) {
        if (newValue > 0) {
            APPOINTMENT_COUNTER.set(newValue);
        }
    }
    
    /**
     * Reset bill counter (for testing purposes)
     * @param newValue new counter value
     */
    public static synchronized void resetBillCounter(int newValue) {
        if (newValue > 0) {
            BILL_COUNTER.set(newValue);
        }
    }
    
    /**
     * Validate if an ID has the correct prefix
     * @param id ID to validate
     * @param expectedPrefix expected prefix
     * @return true if valid
     */
    public static boolean isValidIdPrefix(String id, String expectedPrefix) {
        return id != null && id.startsWith(expectedPrefix);
    }
    
    /**
     * Extract the numeric part of an ID
     * @param id ID with prefix
     * @return numeric part as string
     */
    public static String extractNumericPart(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "";
        }
        
        // Find the first digit and extract from there
        for (int i = 0; i < id.length(); i++) {
            if (Character.isDigit(id.charAt(i))) {
                return id.substring(i);
            }
        }
        
        return "";
    }
    
    /**
     * Get statistics about generated IDs
     * @return formatted statistics string
     */
    public static String getStatistics() {
        return String.format(
            "ID Generation Statistics:\n" +
            "  Patients Generated: %d\n" +
            "  Doctors Generated: %d\n" +
            "  Appointments Generated: %d\n" +
            "  Bills Generated: %d\n" +
            "  Total IDs Generated: %d",
            getPatientCounter() - 1,
            getDoctorCounter() - 1,
            getAppointmentCounter() - 1,
            getBillCounter() - 1,
            (getPatientCounter() - 1) + (getDoctorCounter() - 1) + 
            (getAppointmentCounter() - 1) + (getBillCounter() - 1)
        );
    }
}
