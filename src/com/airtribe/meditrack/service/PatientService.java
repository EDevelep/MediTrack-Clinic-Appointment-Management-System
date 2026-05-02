package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing patients
 * Demonstrates business logic, CRUD operations, and polymorphism
 */
public class PatientService {
    
    private final DataStore<Patient> patientStore;
    
    /**
     * Constructor
     */
    public PatientService() {
        this.patientStore = new DataStore<>(Patient.class);
    }
    
    /**
     * Create a new patient
     * @param patient patient to create
     * @return created patient
     * @throws InvalidDataException if patient data is invalid
     */
    public Patient createPatient(Patient patient) throws InvalidDataException {
        if (patient == null) {
            throw new InvalidDataException("Patient cannot be null");
        }
        
        // Validate patient data
        if (!patient.validate()) {
            throw new InvalidDataException("Invalid patient data");
        }
        
        // Generate ID if not provided
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            patient.setId(IdGenerator.generatePatientId());
        }
        
        // Validate ID format
        if (!Validator.isValidPatientId(patient.getId())) {
            throw new InvalidDataException("Invalid patient ID format", patient.getId());
        }
        
        // Check if patient already exists
        if (patientStore.exists(patient.getId())) {
            throw new InvalidDataException("Patient with ID '" + patient.getId() + "' already exists");
        }
        
        // Check for duplicate phone number (optional business rule)
        List<Patient> existingPatients = patientStore.findAll();
        for (Patient existing : existingPatients) {
            if (existing.getPhoneNumber().equals(patient.getPhoneNumber())) {
                throw new InvalidDataException("Patient with phone number '" + patient.getPhoneNumber() + "' already exists");
            }
        }
        
        return patientStore.save(patient);
    }
    
    /**
     * Get patient by ID
     * @param patientId patient ID
     * @return optional containing patient if found
     */
    public Optional<Patient> getPatientById(String patientId) {
        return Optional.ofNullable(patientStore.findById(patientId));
    }
    
    /**
     * Get all patients
     * @return list of all patients
     */
    public List<Patient> getAllPatients() {
        return patientStore.findAll();
    }
    
    /**
     * Update patient information
     * @param patient patient to update
     * @return updated patient
     * @throws InvalidDataException if patient data is invalid
     */
    public Patient updatePatient(Patient patient) throws InvalidDataException {
        if (patient == null) {
            throw new InvalidDataException("Patient cannot be null");
        }
        
        // Validate patient data
        if (!patient.validate()) {
            throw new InvalidDataException("Invalid patient data");
        }
        
        // Check if patient exists
        if (!patientStore.exists(patient.getId())) {
            throw new InvalidDataException("Patient with ID '" + patient.getId() + "' not found");
        }
        
        return patientStore.update(patient);
    }
    
    /**
     * Delete patient by ID
     * @param patientId patient ID
     * @return true if deleted
     */
    public boolean deletePatient(String patientId) {
        return patientStore.deleteById(patientId);
    }
    
    /**
     * Search patients by query (overloaded method)
     * @param query search query
     * @return list of matching patients
     */
    public List<Patient> searchPatients(String query) {
        return patientStore.search(query);
    }
    
    /**
     * Search patients by ID (overloaded method)
     * @param patientId patient ID
     * @return list of matching patients
     */
    public List<Patient> searchPatientsById(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> patient.getId().contains(patientId));
    }
    
    /**
     * Search patients by name (overloaded method)
     * @param name patient name
     * @return list of matching patients
     */
    public List<Patient> searchPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> 
            patient.getFullName().toLowerCase().contains(name.toLowerCase()));
    }
    
    /**
     * Search patients by age (overloaded method)
     * @param age patient age
     * @return list of matching patients
     */
    public List<Patient> searchPatientsByAge(int age) {
        return patientStore.filter(patient -> patient.getAge() == age);
    }
    
    /**
     * Search patients by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of patients in age range
     */
    public List<Patient> searchPatientsByAgeRange(int minAge, int maxAge) {
        return patientStore.filter(patient -> 
            patient.getAge() >= minAge && patient.getAge() <= maxAge);
    }
    
    /**
     * Search patients by blood group
     * @param bloodGroup blood group
     * @return list of matching patients
     */
    public List<Patient> searchPatientsByBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> 
            bloodGroup.equalsIgnoreCase(patient.getBloodGroup()));
    }
    
    /**
     * Search patients by phone number
     * @param phoneNumber phone number
     * @return list of matching patients
     */
    public List<Patient> searchPatientsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> 
            patient.getPhoneNumber().contains(phoneNumber));
    }
    
    /**
     * Search patients by email
     * @param email email address
     * @return list of matching patients
     */
    public List<Patient> searchPatientsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> 
            patient.getEmail() != null && 
            patient.getEmail().toLowerCase().contains(email.toLowerCase()));
    }
    
    /**
     * Get patients by allergy
     * @param allergy allergy to search for
     * @return list of patients with specified allergy
     */
    public List<Patient> getPatientsByAllergy(String allergy) {
        if (allergy == null || allergy.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> patient.hasAllergy(allergy));
    }
    
    /**
     * Get patients by insurance provider
     * @param insuranceProvider insurance provider
     * @return list of patients with specified insurance
     */
    public List<Patient> getPatientsByInsuranceProvider(String insuranceProvider) {
        if (insuranceProvider == null || insuranceProvider.trim().isEmpty()) {
            return List.of();
        }
        
        return patientStore.filter(patient -> 
            patient.getInsuranceProvider() != null && 
            patient.getInsuranceProvider().toLowerCase().contains(insuranceProvider.toLowerCase()));
    }
    
    /**
     * Get pediatric patients (children under 18)
     * @return list of pediatric patients
     */
    public List<Patient> getPediatricPatients() {
        return patientStore.filter(patient -> patient.getAge() < 18);
    }
    
    /**
     * Get geriatric patients (seniors 65 and older)
     * @return list of geriatric patients
     */
    public List<Patient> getGeriatricPatients() {
        return patientStore.filter(patient -> patient.getAge() >= 65);
    }
    
    /**
     * Add allergy to patient
     * @param patientId patient ID
     * @param allergy allergy to add
     * @return true if added successfully
     */
    public boolean addAllergyToPatient(String patientId, String allergy) {
        Optional<Patient> patientOpt = getPatientById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.addAllergy(allergy);
            try {
                updatePatient(patient);
                return true;
            } catch (InvalidDataException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Remove allergy from patient
     * @param patientId patient ID
     * @param allergy allergy to remove
     * @return true if removed successfully
     */
    public boolean removeAllergyFromPatient(String patientId, String allergy) {
        Optional<Patient> patientOpt = getPatientById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            boolean removed = patient.removeAllergy(allergy);
            if (removed) {
                try {
                    updatePatient(patient);
                    return true;
                } catch (InvalidDataException e) {
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Update patient medical history
     * @param patientId patient ID
     * @param medicalHistory new medical history
     * @return true if updated successfully
     */
    public boolean updateMedicalHistory(String patientId, String medicalHistory) {
        Optional<Patient> patientOpt = getPatientById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setMedicalHistory(medicalHistory);
            try {
                updatePatient(patient);
                return true;
            } catch (InvalidDataException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Get patient statistics
     * @return formatted statistics string
     */
    public String getStatistics() {
        List<Patient> allPatients = getAllPatients();
        
        double avgAge = allPatients.stream()
            .mapToInt(Patient::getAge)
            .average()
            .orElse(0.0);
        
        long pediatricCount = allPatients.stream()
            .filter(patient -> patient.getAge() < 18)
            .count();
        
        long geriatricCount = allPatients.stream()
            .filter(patient -> patient.getAge() >= 65)
            .count();
        
        // Count by blood group
        String bloodGroupStats = "";
        if (!allPatients.isEmpty()) {
            bloodGroupStats = allPatients.stream()
                .collect(Collectors.groupingBy(Patient::getBloodGroup, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
        }
        
        // Count patients with insurance
        long insuredCount = allPatients.stream()
            .filter(patient -> patient.getInsuranceProvider() != null && !patient.getInsuranceProvider().trim().isEmpty())
            .count();
        
        return String.format(
            "Patient Statistics:\n" +
            "  Total Patients: %d\n" +
            "  Average Age: %.1f years\n" +
            "  Pediatric Patients: %d\n" +
            "  Geriatric Patients: %d\n" +
            "  Insured Patients: %d\n" +
            "  Blood Groups: %s",
            allPatients.size(),
            avgAge,
            pediatricCount,
            geriatricCount,
            insuredCount,
            bloodGroupStats.isEmpty() ? "None" : bloodGroupStats
        );
    }
    
    /**
     * Validate all patients
     * @return list of invalid patients
     */
    public List<Patient> validateAllPatients() {
        return patientStore.validateAll();
    }
    
    /**
     * Get patient count
     * @return number of patients
     */
    public int getPatientCount() {
        return patientStore.count();
    }
    
    /**
     * Check if patient exists
     * @param patientId patient ID
     * @return true if exists
     */
    public boolean patientExists(String patientId) {
        return patientStore.exists(patientId);
    }
    
    /**
     * Get patients with allergies
     * @return list of patients who have allergies
     */
    public List<Patient> getPatientsWithAllergies() {
        return patientStore.filter(patient -> patient.getAllergyCount() > 0);
    }
    
    /**
     * Get patients by number of allergies
     * @param minAllergies minimum number of allergies
     * @return list of patients with at least specified number of allergies
     */
    public List<Patient> getPatientsByAllergyCount(int minAllergies) {
        return patientStore.filter(patient -> patient.getAllergyCount() >= minAllergies);
    }
}
