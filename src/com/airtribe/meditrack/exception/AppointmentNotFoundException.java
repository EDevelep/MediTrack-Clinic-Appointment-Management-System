package com.airtribe.meditrack.exception;

/**
 * Custom exception for when an appointment is not found
 * Demonstrates custom exception creation and exception chaining
 */
public class AppointmentNotFoundException extends Exception {
    
    private String appointmentId;
    
    /**
     * Default constructor
     */
    public AppointmentNotFoundException() {
        super("Appointment not found");
    }
    
    /**
     * Constructor with message
     * @param message error message
     */
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with appointment ID
     * @param appointmentId the appointment ID that was not found
     */
    public AppointmentNotFoundException(String appointmentId) {
        super("Appointment with ID '" + appointmentId + "' not found");
        this.appointmentId = appointmentId;
    }
    
    /**
     * Constructor with message and cause
     * @param message error message
     * @param cause underlying cause
     */
    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with appointment ID and cause
     * @param appointmentId the appointment ID that was not found
     * @param cause underlying cause
     */
    public AppointmentNotFoundException(String appointmentId, Throwable cause) {
        super("Appointment with ID '" + appointmentId + "' not found", cause);
        this.appointmentId = appointmentId;
    }
    
    /**
     * Get the appointment ID that was not found
     * @return appointment ID
     */
    public String getAppointmentId() {
        return appointmentId;
    }
    
    @Override
    public String toString() {
        if (appointmentId != null) {
            return "AppointmentNotFoundException: Appointment with ID '" + appointmentId + "' not found";
        }
        return "AppointmentNotFoundException: " + getMessage();
    }
}
