package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;

/**
 * Utility class for data validation
 * Demonstrates static methods, validation patterns, and centralized validation
 */
public final class Validator {
    
    // Private constructor to prevent instantiation
    private Validator() {
        throw new AssertionError("Validator class should not be instantiated");
    }
    
    /**
     * Validate name (first name or last name)
     * @param name name to validate
     * @return true if valid
     */
    public static boolean isValidName(String name) {
        return name != null && 
               !name.trim().isEmpty() && 
               name.trim().length() >= 2 &&
               name.trim().length() <= 50 &&
               name.trim().matches("^[a-zA-Z\\s'-]+$");
    }
    
    /**
     * Validate age
     * @param age age to validate
     * @return true if valid
     */
    public static boolean isValidAge(int age) {
        return age >= Constants.MIN_AGE && age <= Constants.MAX_AGE;
    }
    
    /**
     * Validate phone number (10 digits)
     * @param phoneNumber phone number to validate
     * @return true if valid
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && 
               phoneNumber.matches("^\\d{10}$");
    }
    
    /**
     * Validate email address
     * @param email email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Validate blood group
     * @param bloodGroup blood group to validate
     * @return true if valid
     */
    public static boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null) return false;
        
        String validGroups[] = {
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-",
            "a+", "a-", "b+", "b-", "ab+", "ab-", "o+", "o-"
        };
        
        for (String validGroup : validGroups) {
            if (validGroup.equals(bloodGroup.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate medical license number
     * @param licenseNumber license number to validate
     * @return true if valid
     */
    public static boolean isValidLicenseNumber(String licenseNumber) {
        return licenseNumber != null && 
               !licenseNumber.trim().isEmpty() &&
               licenseNumber.trim().length() >= 5 &&
               licenseNumber.trim().matches("^[A-Za-z0-9-]+$");
    }
    
    /**
     * Validate appointment date (must be in future)
     * @param appointmentDateTime appointment date and time
     * @return true if valid
     */
    public static boolean isValidAppointmentDateTime(java.time.LocalDateTime appointmentDateTime) {
        return appointmentDateTime != null && 
               appointmentDateTime.isAfter(java.time.LocalDateTime.now());
    }
    
    /**
     * Validate amount (must be non-negative)
     * @param amount amount to validate
     * @return true if valid
     */
    public static boolean isValidAmount(double amount) {
        return amount >= 0;
    }
    
    /**
     * Validate ID format
     * @param id ID to validate
     * @param prefix expected prefix
     * @return true if valid
     */
    public static boolean isValidId(String id, String prefix) {
        return id != null && 
               id.startsWith(prefix) &&
               id.length() > prefix.length() &&
               id.substring(prefix.length()).matches("^\\d+$");
    }
    
    /**
     * Validate patient ID
     * @param id patient ID to validate
     * @return true if valid
     */
    public static boolean isValidPatientId(String id) {
        return isValidId(id, Constants.PATIENT_ID_PREFIX);
    }
    
    /**
     * Validate doctor ID
     * @param id doctor ID to validate
     * @return true if valid
     */
    public static boolean isValidDoctorId(String id) {
        return isValidId(id, Constants.DOCTOR_ID_PREFIX);
    }
    
    /**
     * Validate appointment ID
     * @param id appointment ID to validate
     * @return true if valid
     */
    public static boolean isValidAppointmentId(String id) {
        return isValidId(id, Constants.APPOINTMENT_ID_PREFIX);
    }
    
    /**
     * Validate bill ID
     * @param id bill ID to validate
     * @return true if valid
     */
    public static boolean isValidBillId(String id) {
        return isValidId(id, Constants.BILL_ID_PREFIX);
    }
    
    /**
     * Validate room number
     * @param roomNumber room number to validate
     * @return true if valid
     */
    public static boolean isValidRoomNumber(String roomNumber) {
        return roomNumber != null && 
               roomNumber.trim().matches("^[A-Za-z0-9-]+$");
    }
    
    /**
     * Validate payment method
     * @param paymentMethod payment method to validate
     * @return true if valid
     */
    public static boolean isValidPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) return false;
        
        String validMethods[] = {"CASH", "CREDIT_CARD", "DEBIT_CARD", "INSURANCE", "ONLINE"};
        
        for (String validMethod : validMethods) {
            if (validMethod.equalsIgnoreCase(paymentMethod.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate payment status
     * @param paymentStatus payment status to validate
     * @return true if valid
     */
    public static boolean isValidPaymentStatus(String paymentStatus) {
        if (paymentStatus == null) return false;
        
        String validStatuses[] = {"PENDING", "PAID", "REFUNDED", "CANCELLED"};
        
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(paymentStatus.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate symptoms text
     * @param symptoms symptoms to validate
     * @return true if valid
     */
    public static boolean isValidSymptoms(String symptoms) {
        return symptoms == null || 
               symptoms.trim().isEmpty() ||
               (symptoms.trim().length() >= 5 && symptoms.trim().length() <= 500);
    }
    
    /**
     * Validate notes text
     * @param notes notes to validate
     * @return true if valid
     */
    public static boolean isValidNotes(String notes) {
        return notes == null || 
               notes.trim().isEmpty() ||
               (notes.trim().length() >= 5 && notes.trim().length() <= 1000);
    }
    
    /**
     * Validate specialization string
     * @param specialization specialization string to validate
     * @return true if valid
     */
    public static boolean isValidSpecialization(String specialization) {
        if (specialization == null) return false;
        
        try {
            com.airtribe.meditrack.entity.Specialization.valueOf(specialization.toUpperCase().replace(" ", "_"));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Validate appointment status string
     * @param status appointment status string to validate
     * @return true if valid
     */
    public static boolean isValidAppointmentStatus(String status) {
        if (status == null) return false;
        
        try {
            com.airtribe.meditrack.entity.AppointmentStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Generic string validation
     * @param value string to validate
     * @param minLength minimum length
     * @param maxLength maximum length
     * @param allowEmpty if empty strings are allowed
     * @return true if valid
     */
    public static boolean isValidString(String value, int minLength, int maxLength, boolean allowEmpty) {
        if (value == null) return allowEmpty;
        
        String trimmed = value.trim();
        
        if (trimmed.isEmpty()) return allowEmpty;
        
        return trimmed.length() >= minLength && trimmed.length() <= maxLength;
    }
}
