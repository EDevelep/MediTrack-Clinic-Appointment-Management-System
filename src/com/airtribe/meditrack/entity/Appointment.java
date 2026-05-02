package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.DateUtil;
import java.time.LocalDateTime;

/**
 * Appointment class representing a medical appointment
 * Demonstrates cloning, encapsulation, and business logic
 */
public class Appointment extends MedicalEntity implements Cloneable {
    
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String symptoms;
    private String notes;
    private double fee;
    private String roomNumber;
    
    /**
     * Default constructor
     */
    public Appointment() {
        super();
        this.status = AppointmentStatus.PENDING;
        this.fee = Constants.CONSULTATION_FEE;
    }
    
    /**
     * Constructor with basic information
     * @param id unique identifier
     * @param patient patient object
     * @param doctor doctor object
     * @param appointmentDateTime appointment date and time
     */
    public Appointment(String id, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        super(id);
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.endTime = appointmentDateTime.plusMinutes(Constants.APPOINTMENT_DURATION_MINUTES);
        this.status = AppointmentStatus.PENDING;
        this.fee = doctor != null ? doctor.getConsultationFee() : Constants.CONSULTATION_FEE;
    }
    
    /**
     * Full constructor
     * @param id unique identifier
     * @param patient patient object
     * @param doctor doctor object
     * @param appointmentDateTime appointment date and time
     * @param symptoms patient symptoms
     * @param notes additional notes
     * @param roomNumber consultation room
     */
    public Appointment(String id, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, 
                      String symptoms, String notes, String roomNumber) {
        this(id, patient, doctor, appointmentDateTime);
        this.symptoms = symptoms;
        this.notes = notes;
        this.roomNumber = roomNumber;
    }
    
    // Getters
    public Patient getPatient() {
        return patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public String getSymptoms() {
        return symptoms;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public double getFee() {
        return fee;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    // Setters with validation
    public void setPatient(Patient patient) {
        if (patient != null) {
            this.patient = patient;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Patient cannot be null");
        }
    }
    
    public void setDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctor = doctor;
            this.fee = doctor.getConsultationFee();
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
    }
    
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        if (appointmentDateTime != null && appointmentDateTime.isAfter(LocalDateTime.now())) {
            this.appointmentDateTime = appointmentDateTime;
            this.endTime = appointmentDateTime.plusMinutes(Constants.APPOINTMENT_DURATION_MINUTES);
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Appointment date must be in the future");
        }
    }
    
    public void setStatus(AppointmentStatus status) {
        if (status != null && AppointmentStatus.isValidTransition(this.status, status)) {
            this.status = status;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid status transition from " + this.status + " to " + status);
        }
    }
    
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
        updateTimestamp();
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
        updateTimestamp();
    }
    
    public void setFee(double fee) {
        if (fee >= 0) {
            this.fee = fee;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Fee cannot be negative: " + fee);
        }
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
        updateTimestamp();
    }
    
    /**
     * Confirm the appointment
     */
    public void confirm() {
        setStatus(AppointmentStatus.CONFIRMED);
    }
    
    /**
     * Cancel the appointment
     */
    public void cancel() {
        setStatus(AppointmentStatus.CANCELLED);
    }
    
    /**
     * Mark appointment as completed
     */
    public void complete() {
        setStatus(AppointmentStatus.COMPLETED);
    }
    
    /**
     * Mark appointment as no-show
     */
    public void markNoShow() {
        setStatus(AppointmentStatus.NO_SHOW);
    }
    
    /**
     * Check if appointment is in the past
     * @return true if appointment date has passed
     */
    public boolean isPast() {
        return appointmentDateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if appointment is today
     * @return true if appointment is today
     */
    public boolean isToday() {
        return appointmentDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    
    /**
     * Check if appointment is upcoming
     * @return true if appointment is in the future
     */
    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Get duration of appointment in minutes
     * @return duration in minutes
     */
    public long getDurationMinutes() {
        return java.time.Duration.between(appointmentDateTime, endTime).toMinutes();
    }
    
    /**
     * Check if appointment conflicts with another appointment
     * @param other other appointment to check
     * @return true if there's a conflict
     */
    public boolean conflictsWith(Appointment other) {
        if (other == null || !doctor.equals(other.doctor)) {
            return false;
        }
        
        return !(this.endTime.isBefore(other.appointmentDateTime) || 
                this.appointmentDateTime.isAfter(other.endTime));
    }
    
    @Override
    public String getDisplayName() {
        return "Appointment: " + patient.getFullName() + " with Dr. " + doctor.getFullName() + 
               " on " + DateUtil.formatDateTime(appointmentDateTime);
    }
    
    @Override
    public boolean validate() {
        return patient != null && patient.validate() &&
               doctor != null && doctor.validate() &&
               appointmentDateTime != null &&
               appointmentDateTime.isAfter(LocalDateTime.now()) &&
               endTime != null &&
               status != null &&
               fee >= 0;
    }
    
    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check patient information
        if (patient != null && patient.matches(query)) {
            return true;
        }
        
        // Check doctor information
        if (doctor != null && doctor.matches(query)) {
            return true;
        }
        
        // Check symptoms
        if (symptoms != null && symptoms.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check notes
        if (notes != null && notes.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check status
        if (status != null && status.toString().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check room number
        if (roomNumber != null && roomNumber.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check date
        if (DateUtil.formatDate(appointmentDateTime).contains(lowerQuery)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Create a deep copy of the appointment
     * Demonstrates deep cloning with nested objects
     * @return deep copy of appointment
     */
    @Override
    public Appointment clone() {
        try {
            // Shallow copy
            Appointment cloned = (Appointment) super.clone();
            
            // Deep copy of mutable objects
            cloned.patient = this.patient != null ? this.patient.clone() : null;
            cloned.doctor = this.doctor; // Doctor is typically immutable in business context
            cloned.appointmentDateTime = this.appointmentDateTime;
            cloned.endTime = this.endTime;
            cloned.status = this.status; // Enum is immutable
            
            // Clone immutable fields (String is immutable)
            cloned.symptoms = this.symptoms;
            cloned.notes = this.notes;
            cloned.roomNumber = this.roomNumber;
            
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Appointment cloning not supported", e);
        }
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patient=" + (patient != null ? patient.getFullName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getFullName() : "null") +
                ", appointmentDateTime=" + DateUtil.formatDateTime(appointmentDateTime) +
                ", status=" + status +
                ", fee=" + fee +
                ", roomNumber='" + roomNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
