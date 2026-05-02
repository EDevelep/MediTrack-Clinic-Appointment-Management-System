package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;

/**
 * Console notification observer for appointment events
 * Demonstrates concrete Observer implementation
 */
public class ConsoleNotificationObserver implements AppointmentObserver {
    
    private final String observerName;
    
    /**
     * Constructor
     * @param observerName observer name
     */
    public ConsoleNotificationObserver(String observerName) {
        this.observerName = observerName;
    }
    
    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("📅 [" + observerName + "] NEW APPOINTMENT CREATED");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Status: " + appointment.getStatus().getDisplayName());
        System.out.println("   Fee: $" + String.format("%.2f", appointment.getFee()));
        System.out.println();
    }
    
    @Override
    public void onAppointmentUpdated(Appointment appointment) {
        System.out.println("🔄 [" + observerName + "] APPOINTMENT UPDATED");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Status: " + appointment.getStatus().getDisplayName());
        System.out.println("   Updated: " + DateUtil.formatDateTime(appointment.getUpdatedAt()));
        System.out.println();
    }
    
    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("❌ [" + observerName + "] APPOINTMENT CANCELLED");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Scheduled Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Cancelled: " + DateUtil.formatDateTime(appointment.getUpdatedAt()));
        System.out.println();
    }
    
    @Override
    public void onAppointmentCompleted(Appointment appointment) {
        System.out.println("✅ [" + observerName + "] APPOINTMENT COMPLETED");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Completed: " + DateUtil.formatDateTime(appointment.getUpdatedAt()));
        System.out.println("   Fee: $" + String.format("%.2f", appointment.getFee()));
        System.out.println();
    }
    
    @Override
    public void onAppointmentConfirmed(Appointment appointment) {
        System.out.println("✓ [" + observerName + "] APPOINTMENT CONFIRMED");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Confirmed: " + DateUtil.formatDateTime(appointment.getUpdatedAt()));
        System.out.println();
    }
    
    @Override
    public void onAppointmentNoShow(Appointment appointment) {
        System.out.println("⚠️ [" + observerName + "] APPOINTMENT NO-SHOW");
        System.out.println("   ID: " + appointment.getId());
        System.out.println("   Patient: " + appointment.getPatient().getFullName());
        System.out.println("   Doctor: " + appointment.getDoctor().getFullName());
        System.out.println("   Scheduled Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("   Marked No-Show: " + DateUtil.formatDateTime(appointment.getUpdatedAt()));
        System.out.println();
    }
    
    @Override
    public String getObserverName() {
        return observerName;
    }
}
