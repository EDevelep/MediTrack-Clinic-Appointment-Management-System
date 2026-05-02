package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing doctors
 * Demonstrates business logic, CRUD operations, and polymorphism
 */
public class DoctorService {
    
    private final DataStore<Doctor> doctorStore;
    
    /**
     * Constructor
     */
    public DoctorService() {
        this.doctorStore = new DataStore<>(Doctor.class);
    }
    
    /**
     * Create a new doctor
     * @param doctor doctor to create
     * @return created doctor
     * @throws InvalidDataException if doctor data is invalid
     */
    public Doctor createDoctor(Doctor doctor) throws InvalidDataException {
        if (doctor == null) {
            throw new InvalidDataException("Doctor cannot be null");
        }
        
        // Validate doctor data
        if (!doctor.validate()) {
            throw new InvalidDataException("Invalid doctor data");
        }
        
        // Generate ID if not provided
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            doctor.setId(IdGenerator.generateDoctorId());
        }
        
        // Validate ID format
        if (!Validator.isValidDoctorId(doctor.getId())) {
            throw new InvalidDataException("Invalid doctor ID format", doctor.getId());
        }
        
        // Check if doctor already exists
        if (doctorStore.exists(doctor.getId())) {
            throw new InvalidDataException("Doctor with ID '" + doctor.getId() + "' already exists");
        }
        
        // Check if license number is unique
        List<Doctor> existingDoctors = doctorStore.findAll();
        for (Doctor existing : existingDoctors) {
            if (existing.getLicenseNumber().equals(doctor.getLicenseNumber())) {
                throw new InvalidDataException("Doctor with license number '" + doctor.getLicenseNumber() + "' already exists");
            }
        }
        
        return doctorStore.save(doctor);
    }
    
    /**
     * Get doctor by ID
     * @param doctorId doctor ID
     * @return optional containing doctor if found
     */
    public Optional<Doctor> getDoctorById(String doctorId) {
        return Optional.ofNullable(doctorStore.findById(doctorId));
    }
    
    /**
     * Get all doctors
     * @return list of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return doctorStore.findAll();
    }
    
    /**
     * Update doctor information
     * @param doctor doctor to update
     * @return updated doctor
     * @throws InvalidDataException if doctor data is invalid
     */
    public Doctor updateDoctor(Doctor doctor) throws InvalidDataException {
        if (doctor == null) {
            throw new InvalidDataException("Doctor cannot be null");
        }
        
        // Validate doctor data
        if (!doctor.validate()) {
            throw new InvalidDataException("Invalid doctor data");
        }
        
        // Check if doctor exists
        if (!doctorStore.exists(doctor.getId())) {
            throw new InvalidDataException("Doctor with ID '" + doctor.getId() + "' not found");
        }
        
        return doctorStore.update(doctor);
    }
    
    /**
     * Delete doctor by ID
     * @param doctorId doctor ID
     * @return true if deleted
     */
    public boolean deleteDoctor(String doctorId) {
        return doctorStore.deleteById(doctorId);
    }
    
    /**
     * Search doctors by query (overloaded method)
     * @param query search query
     * @return list of matching doctors
     */
    public List<Doctor> searchDoctors(String query) {
        return doctorStore.search(query);
    }
    
    /**
     * Search doctors by ID (overloaded method)
     * @param doctorId doctor ID
     * @return list of matching doctors
     */
    public List<Doctor> searchDoctorsById(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return List.of();
        }
        
        return doctorStore.filter(doctor -> doctor.getId().contains(doctorId));
    }
    
    /**
     * Search doctors by name (overloaded method)
     * @param name doctor name
     * @return list of matching doctors
     */
    public List<Doctor> searchDoctorsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        
        return doctorStore.filter(doctor -> 
            doctor.getFullName().toLowerCase().contains(name.toLowerCase()));
    }
    
    /**
     * Search doctors by specialization
     * @param specialization doctor specialization
     * @return list of matching doctors
     */
    public List<Doctor> searchDoctorsBySpecialization(Specialization specialization) {
        if (specialization == null) {
            return List.of();
        }
        
        return doctorStore.filter(doctor -> doctor.getSpecialization() == specialization);
    }
    
    /**
     * Get doctors by availability
     * @param available availability status
     * @return list of available doctors
     */
    public List<Doctor> getDoctorsByAvailability(boolean available) {
        return doctorStore.filter(doctor -> doctor.isAvailable() == available);
    }
    
    /**
     * Get available doctors by specialization
     * @param specialization doctor specialization
     * @return list of available doctors with given specialization
     */
    public List<Doctor> getAvailableDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.filter(doctor -> 
            doctor.isAvailable() && doctor.getSpecialization() == specialization);
    }
    
    /**
     * Recommend doctors for symptoms (AI feature)
     * @param symptoms patient symptoms
     * @return list of recommended doctors
     */
    public List<Doctor> recommendDoctorsForSymptoms(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return List.of();
        }
        
        return doctorStore.filter(doctor -> 
            doctor.isAvailable() && doctor.canTreatSymptoms(symptoms))
            .stream()
            .sorted((d1, d2) -> Double.compare(d2.calculateRating(), d1.calculateRating()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get doctors by experience range
     * @param minYears minimum years of experience
     * @param maxYears maximum years of experience
     * @return list of doctors in experience range
     */
    public List<Doctor> getDoctorsByExperienceRange(int minYears, int maxYears) {
        return doctorStore.filter(doctor -> 
            doctor.getYearsOfExperience() >= minYears && 
            doctor.getYearsOfExperience() <= maxYears);
    }
    
    /**
     * Get doctors by fee range
     * @param minFee minimum consultation fee
     * @param maxFee maximum consultation fee
     * @return list of doctors in fee range
     */
    public List<Doctor> getDoctorsByFeeRange(double minFee, double maxFee) {
        return doctorStore.filter(doctor -> 
            doctor.getConsultationFee() >= minFee && 
            doctor.getConsultationFee() <= maxFee);
    }
    
    /**
     * Get top rated doctors
     * @param limit maximum number of doctors to return
     * @return list of top rated doctors
     */
    public List<Doctor> getTopRatedDoctors(int limit) {
        return doctorStore.sort((d1, d2) -> Double.compare(d2.calculateRating(), d1.calculateRating()))
            .stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Set doctor availability
     * @param doctorId doctor ID
     * @param available availability status
     * @return true if updated
     */
    public boolean setDoctorAvailability(String doctorId, boolean available) {
        Optional<Doctor> doctorOpt = getDoctorById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setAvailable(available);
            try {
                updateDoctor(doctor);
                return true;
            } catch (InvalidDataException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Get doctor statistics
     * @return formatted statistics string
     */
    public String getStatistics() {
        List<Doctor> allDoctors = getAllDoctors();
        
        long availableCount = allDoctors.stream()
            .filter(Doctor::isAvailable)
            .count();
        
        double avgExperience = allDoctors.stream()
            .mapToInt(Doctor::getYearsOfExperience)
            .average()
            .orElse(0.0);
        
        double avgFee = allDoctors.stream()
            .mapToDouble(Doctor::getConsultationFee)
            .average()
            .orElse(0.0);
        
        // Count by specialization
        String specializationStats = "";
        if (!allDoctors.isEmpty()) {
            specializationStats = allDoctors.stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
        }
        
        return String.format(
            "Doctor Statistics:\n" +
            "  Total Doctors: %d\n" +
            "  Available Doctors: %d\n" +
            "  Average Experience: %.1f years\n" +
            "  Average Consultation Fee: $%.2f\n" +
            "  Specializations: %s",
            allDoctors.size(),
            availableCount,
            avgExperience,
            avgFee,
            specializationStats.isEmpty() ? "None" : specializationStats
        );
    }
    
    /**
     * Validate all doctors
     * @return list of invalid doctors
     */
    public List<Doctor> validateAllDoctors() {
        return doctorStore.validateAll();
    }
    
    /**
     * Get doctor count
     * @return number of doctors
     */
    public int getDoctorCount() {
        return doctorStore.count();
    }
    
    /**
     * Check if doctor exists
     * @param doctorId doctor ID
     * @return true if exists
     */
    public boolean doctorExists(String doctorId) {
        return doctorStore.exists(doctorId);
    }
}
