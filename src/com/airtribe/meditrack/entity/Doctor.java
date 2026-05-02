package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.Validator;

/**
 * Doctor class extending Person
 * Demonstrates inheritance, constructor chaining, and polymorphism
 */
public class Doctor extends Person {
    
    private Specialization specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private double consultationFee;
    private boolean available;
    
    /**
     * Default constructor
     */
    public Doctor() {
        super();
        this.consultationFee = Constants.CONSULTATION_FEE;
        this.available = true;
    }
    
    /**
     * Constructor with basic information
     * @param id unique identifier
     * @param firstName first name
     * @param lastName last name
     * @param age age
     * @param phoneNumber phone number
     * @param specialization doctor's specialization
     * @param licenseNumber medical license number
     */
    public Doctor(String id, String firstName, String lastName, int age, String phoneNumber, 
                  Specialization specialization, String licenseNumber) {
        super(id, firstName, lastName, age, phoneNumber);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.consultationFee = Constants.CONSULTATION_FEE;
        this.available = true;
    }
    
    /**
     * Full constructor
     * @param id unique identifier
     * @param firstName first name
     * @param lastName last name
     * @param age age
     * @param phoneNumber phone number
     * @param email email address
     * @param address physical address
     * @param specialization doctor's specialization
     * @param licenseNumber medical license number
     * @param yearsOfExperience years of experience
     * @param consultationFee consultation fee
     */
    public Doctor(String id, String firstName, String lastName, int age, String phoneNumber, 
                  String email, String address, Specialization specialization, 
                  String licenseNumber, int yearsOfExperience, double consultationFee) {
        super(id, firstName, lastName, age, phoneNumber, email, address);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.consultationFee = consultationFee;
        this.available = true;
    }
    
    // Getters
    public Specialization getSpecialization() {
        return specialization;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    // Setters with validation
    public void setSpecialization(Specialization specialization) {
        if (specialization != null) {
            this.specialization = specialization;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Specialization cannot be null");
        }
    }
    
    public void setLicenseNumber(String licenseNumber) {
        if (Validator.isValidLicenseNumber(licenseNumber)) {
            this.licenseNumber = licenseNumber;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid license number: " + licenseNumber);
        }
    }
    
    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience >= 0 && yearsOfExperience <= getAge() - 25) {
            this.yearsOfExperience = yearsOfExperience;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid years of experience: " + yearsOfExperience);
        }
    }
    
    public void setConsultationFee(double consultationFee) {
        if (consultationFee > 0) {
            this.consultationFee = consultationFee;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Consultation fee must be positive: " + consultationFee);
        }
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
        updateTimestamp();
    }
    
    /**
     * Check if doctor can treat specific symptoms (for AI feature)
     * @param symptoms patient symptoms
     * @return true if doctor can treat
     */
    public boolean canTreatSymptoms(String symptoms) {
        if (symptoms == null || specialization == null) {
            return false;
        }
        
        String lowerSymptoms = symptoms.toLowerCase();
        
        switch (specialization) {
            case CARDIOLOGY:
                return lowerSymptoms.contains("chest") || lowerSymptoms.contains("heart") || 
                       lowerSymptoms.contains("blood pressure");
            case NEUROLOGY:
                return lowerSymptoms.contains("headache") || lowerSymptoms.contains("brain") || 
                       lowerSymptoms.contains("nerve");
            case ORTHOPEDICS:
                return lowerSymptoms.contains("bone") || lowerSymptoms.contains("joint") || 
                       lowerSymptoms.contains("fracture") || lowerSymptoms.contains("pain");
            case PEDIATRICS:
                return lowerSymptoms.contains("child") || lowerSymptoms.contains("baby");
            case DERMATOLOGY:
                return lowerSymptoms.contains("skin") || lowerSymptoms.contains("rash");
            case GENERAL_MEDICINE:
                return true; // General medicine can handle most cases
            default:
                return false;
        }
    }
    
    /**
     * Calculate doctor's rating based on experience and specialization
     * @return rating from 1 to 5
     */
    public double calculateRating() {
        double baseRating = 3.0;
        
        // Add rating based on experience
        if (yearsOfExperience >= 20) {
            baseRating += 1.5;
        } else if (yearsOfExperience >= 10) {
            baseRating += 1.0;
        } else if (yearsOfExperience >= 5) {
            baseRating += 0.5;
        }
        
        // Cap at 5.0
        return Math.min(baseRating, 5.0);
    }
    
    @Override
    public boolean validate() {
        return super.validate() && 
               specialization != null &&
               Validator.isValidLicenseNumber(licenseNumber) &&
               yearsOfExperience >= 0 &&
               consultationFee > 0;
    }
    
    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check specialization
        if (specialization != null && specialization.toString().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check license number
        if (licenseNumber != null && licenseNumber.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", specialization=" + specialization +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", consultationFee=" + consultationFee +
                ", available=" + available +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
