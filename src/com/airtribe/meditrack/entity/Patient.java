package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient class extending Person and implementing Cloneable
 * Demonstrates inheritance, cloning (deep vs shallow), and encapsulation
 */
public class Patient extends Person implements Cloneable {
    
    private String medicalHistory;
    private List<String> allergies;
    private String bloodGroup;
    private String emergencyContact;
    private String insuranceProvider;
    private String insuranceNumber;
    
    /**
     * Default constructor
     */
    public Patient() {
        super();
        this.allergies = new ArrayList<>();
    }
    
    /**
     * Constructor with basic information
     * @param id unique identifier
     * @param firstName first name
     * @param lastName last name
     * @param age age
     * @param phoneNumber phone number
     * @param bloodGroup blood group
     */
    public Patient(String id, String firstName, String lastName, int age, String phoneNumber, String bloodGroup) {
        super(id, firstName, lastName, age, phoneNumber);
        this.bloodGroup = bloodGroup;
        this.allergies = new ArrayList<>();
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
     * @param medicalHistory medical history
     * @param bloodGroup blood group
     * @param emergencyContact emergency contact
     * @param insuranceProvider insurance provider
     * @param insuranceNumber insurance number
     */
    public Patient(String id, String firstName, String lastName, int age, String phoneNumber, 
                   String email, String address, String medicalHistory, String bloodGroup, 
                   String emergencyContact, String insuranceProvider, String insuranceNumber) {
        super(id, firstName, lastName, age, phoneNumber, email, address);
        this.medicalHistory = medicalHistory;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.insuranceProvider = insuranceProvider;
        this.insuranceNumber = insuranceNumber;
        this.allergies = new ArrayList<>();
    }
    
    // Getters
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public List<String> getAllergies() {
        return new ArrayList<>(allergies); // Return defensive copy
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public String getInsuranceNumber() {
        return insuranceNumber;
    }
    
    // Setters with validation
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
        updateTimestamp();
    }
    
    public void setBloodGroup(String bloodGroup) {
        if (Validator.isValidBloodGroup(bloodGroup)) {
            this.bloodGroup = bloodGroup;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid blood group: " + bloodGroup);
        }
    }
    
    public void setEmergencyContact(String emergencyContact) {
        if (emergencyContact == null || emergencyContact.trim().isEmpty() || 
            Validator.isValidPhoneNumber(emergencyContact)) {
            this.emergencyContact = emergencyContact;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid emergency contact: " + emergencyContact);
        }
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
        updateTimestamp();
    }
    
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
        updateTimestamp();
    }
    
    /**
     * Add allergy to patient's allergy list
     * @param allergy allergy to add
     */
    public void addAllergy(String allergy) {
        if (allergy != null && !allergy.trim().isEmpty() && !allergies.contains(allergy.trim())) {
            allergies.add(allergy.trim());
            updateTimestamp();
        }
    }
    
    /**
     * Remove allergy from patient's allergy list
     * @param allergy allergy to remove
     * @return true if removed
     */
    public boolean removeAllergy(String allergy) {
        boolean removed = allergies.remove(allergy);
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Check if patient has specific allergy
     * @param allergy allergy to check
     * @return true if patient has allergy
     */
    public boolean hasAllergy(String allergy) {
        return allergies.contains(allergy);
    }
    
    /**
     * Get number of allergies
     * @return number of allergies
     */
    public int getAllergyCount() {
        return allergies.size();
    }
    
    /**
     * Clear all allergies
     */
    public void clearAllergies() {
        if (!allergies.isEmpty()) {
            allergies.clear();
            updateTimestamp();
        }
    }
    
    @Override
    public boolean validate() {
        return super.validate() && 
               Validator.isValidBloodGroup(bloodGroup) &&
               (emergencyContact == null || emergencyContact.trim().isEmpty() || Validator.isValidPhoneNumber(emergencyContact));
    }
    
    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check medical history
        if (medicalHistory != null && medicalHistory.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check blood group
        if (bloodGroup != null && bloodGroup.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check allergies
        for (String allergy : allergies) {
            if (allergy.toLowerCase().contains(lowerQuery)) {
                return true;
            }
        }
        
        // Check insurance provider
        if (insuranceProvider != null && insuranceProvider.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Create a deep copy of the patient
     * Demonstrates deep cloning vs shallow cloning
     * @return deep copy of patient
     */
    @Override
    public Patient clone() {
        try {
            // Shallow copy
            Patient cloned = (Patient) super.clone();
            
            // Deep copy of mutable fields
            cloned.allergies = new ArrayList<>(this.allergies);
            
            // Clone immutable fields (String is immutable, so reference copy is fine)
            cloned.medicalHistory = this.medicalHistory;
            cloned.bloodGroup = this.bloodGroup;
            cloned.emergencyContact = this.emergencyContact;
            cloned.insuranceProvider = this.insuranceProvider;
            cloned.insuranceNumber = this.insuranceNumber;
            
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Patient cloning not supported", e);
        }
    }
    
    /**
     * Demonstrate shallow copy (for comparison)
     * @return shallow copy of patient
     */
    public Patient shallowCopy() {
        Patient copy = new Patient();
        copy.id = this.id;
        copy.firstName = this.firstName;
        copy.lastName = this.lastName;
        copy.age = this.age;
        copy.phoneNumber = this.phoneNumber;
        copy.email = this.email;
        copy.address = this.address;
        copy.medicalHistory = this.medicalHistory;
        copy.bloodGroup = this.bloodGroup;
        copy.emergencyContact = this.emergencyContact;
        copy.insuranceProvider = this.insuranceProvider;
        copy.insuranceNumber = this.insuranceNumber;
        copy.allergies = this.allergies; // Same reference - shallow copy!
        copy.createdAt = this.createdAt;
        copy.updatedAt = this.updatedAt;
        
        return copy;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", allergies=" + allergies +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
