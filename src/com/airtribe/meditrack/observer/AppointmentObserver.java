package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Observer interface for appointment notifications
 * Demonstrates Observer pattern for event handling
 */
public interface AppointmentObserver {
    
    /**
     * Called when an appointment is created
     * @param appointment the created appointment
     */
    void onAppointmentCreated(Appointment appointment);
    
    /**
     * Called when an appointment is updated
     * @param appointment the updated appointment
     */
    void onAppointmentUpdated(Appointment appointment);
    
    /**
     * Called when an appointment is cancelled
     * @param appointment the cancelled appointment
     */
    void onAppointmentCancelled(Appointment appointment);
    
    /**
     * Called when an appointment is completed
     * @param appointment the completed appointment
     */
    void onAppointmentCompleted(Appointment appointment);
    
    /**
     * Called when an appointment is confirmed
     * @param appointment the confirmed appointment
     */
    void onAppointmentConfirmed(Appointment appointment);
    
    /**
     * Called when an appointment is marked as no-show
     * @param appointment the no-show appointment
     */
    void onAppointmentNoShow(Appointment appointment);
    
    /**
     * Get observer name
     * @return observer name
     */
    String getObserverName();
}
