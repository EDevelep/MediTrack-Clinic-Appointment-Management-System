package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class for appointment notifications
 * Demonstrates Observer pattern for managing multiple observers
 */
public class AppointmentNotifier {
    
    private List<AppointmentObserver> observers;
    private static AppointmentNotifier instance;
    
    /**
     * Private constructor for singleton pattern
     */
    private AppointmentNotifier() {
        this.observers = new ArrayList<>();
    }
    
    /**
     * Get singleton instance
     * @return AppointmentNotifier instance
     */
    public static synchronized AppointmentNotifier getInstance() {
        if (instance == null) {
            instance = new AppointmentNotifier();
        }
        return instance;
    }
    
    /**
     * Add observer
     * @param observer observer to add
     */
    public void addObserver(AppointmentObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer added: " + observer.getObserverName());
        }
    }
    
    /**
     * Remove observer
     * @param observer observer to remove
     */
    public void removeObserver(AppointmentObserver observer) {
        if (observer != null && observers.remove(observer)) {
            System.out.println("Observer removed: " + observer.getObserverName());
        }
    }
    
    /**
     * Remove all observers
     */
    public void removeAllObservers() {
        observers.clear();
        System.out.println("All observers removed");
    }
    
    /**
     * Get all observers
     * @return list of observers
     */
    public List<AppointmentObserver> getObservers() {
        return new ArrayList<>(observers);
    }
    
    /**
     * Get observer count
     * @return number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Notify observers of appointment creation
     * @param appointment the created appointment
     */
    public void notifyAppointmentCreated(Appointment appointment) {
        System.out.println("Notifying observers: Appointment Created - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentCreated(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify observers of appointment update
     * @param appointment the updated appointment
     */
    public void notifyAppointmentUpdated(Appointment appointment) {
        System.out.println("Notifying observers: Appointment Updated - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentUpdated(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify observers of appointment cancellation
     * @param appointment the cancelled appointment
     */
    public void notifyAppointmentCancelled(Appointment appointment) {
        System.out.println("Notifying observers: Appointment Cancelled - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentCancelled(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify observers of appointment completion
     * @param appointment the completed appointment
     */
    public void notifyAppointmentCompleted(Appointment appointment) {
        System.out.println("Notifying observers: Appointment Completed - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentCompleted(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify observers of appointment confirmation
     * @param appointment the confirmed appointment
     */
    public void notifyAppointmentConfirmed(Appointment appointment) {
        System.out.println("Notifying observers: Appointment Confirmed - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentConfirmed(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify observers of appointment no-show
     * @param appointment the no-show appointment
     */
    public void notifyAppointmentNoShow(Appointment appointment) {
        System.out.println("Notifying observers: Appointment No-Show - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                observer.onAppointmentNoShow(appointment);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Notify all observers with custom message
     * @param appointment the appointment
     * @param message custom message
     */
    public void notifyCustomMessage(Appointment appointment, String message) {
        System.out.println("Notifying observers: " + message + " - " + appointment.getId());
        for (AppointmentObserver observer : observers) {
            try {
                // This would require extending the interface, for now we'll just log
                System.out.println(observer.getObserverName() + ": " + message);
            } catch (Exception e) {
                System.out.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Send appointment reminders for upcoming appointments
     * @param appointment the appointment
     */
    public void sendAppointmentReminder(Appointment appointment) {
        if (appointment.isUpcoming()) {
            System.out.println("Sending reminder for upcoming appointment: " + appointment.getId());
            notifyCustomMessage(appointment, "Appointment Reminder: " + 
                DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        }
    }
    
    /**
     * Get observer statistics
     * @return formatted statistics
     */
    public String getObserverStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Observer Statistics:\n");
        sb.append("  Total Observers: ").append(observers.size()).append("\n");
        
        if (!observers.isEmpty()) {
            sb.append("  Active Observers:\n");
            for (AppointmentObserver observer : observers) {
                sb.append("    - ").append(observer.getObserverName()).append("\n");
            }
        }
        
        return sb.toString();
    }
}
