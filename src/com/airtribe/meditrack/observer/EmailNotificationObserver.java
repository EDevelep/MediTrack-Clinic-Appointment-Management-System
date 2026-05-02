package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;

/**
 * Email notification observer for appointment events
 * Demonstrates another concrete Observer implementation
 */
public class EmailNotificationObserver implements AppointmentObserver {
    
    private final String observerName;
    private final String emailAddress;
    
    /**
     * Constructor
     * @param observerName observer name
     * @param emailAddress email address for notifications
     */
    public EmailNotificationObserver(String observerName, String emailAddress) {
        this.observerName = observerName;
        this.emailAddress = emailAddress;
    }
    
    @Override
    public void onAppointmentCreated(Appointment appointment) {
        String subject = "New Appointment Scheduled - " + appointment.getId();
        String body = buildEmailBody(
            "NEW APPOINTMENT SCHEDULED",
            appointment,
            "Your appointment has been successfully scheduled."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public void onAppointmentUpdated(Appointment appointment) {
        String subject = "Appointment Updated - " + appointment.getId();
        String body = buildEmailBody(
            "APPOINTMENT UPDATED",
            appointment,
            "Your appointment details have been updated."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        String subject = "Appointment Cancelled - " + appointment.getId();
        String body = buildEmailBody(
            "APPOINTMENT CANCELLED",
            appointment,
            "Your appointment has been cancelled."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public void onAppointmentCompleted(Appointment appointment) {
        String subject = "Appointment Completed - " + appointment.getId();
        String body = buildEmailBody(
            "APPOINTMENT COMPLETED",
            appointment,
            "Your appointment has been completed. Thank you for visiting us."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public void onAppointmentConfirmed(Appointment appointment) {
        String subject = "Appointment Confirmed - " + appointment.getId();
        String body = buildEmailBody(
            "APPOINTMENT CONFIRMED",
            appointment,
            "Your appointment has been confirmed. Please arrive 10 minutes early."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public void onAppointmentNoShow(Appointment appointment) {
        String subject = "Appointment No-Show - " + appointment.getId();
        String body = buildEmailBody(
            "APPOINTMENT NO-SHOW",
            appointment,
            "You missed your scheduled appointment. Please contact us to reschedule."
        );
        sendEmail(subject, body);
    }
    
    @Override
    public String getObserverName() {
        return observerName;
    }
    
    /**
     * Build email body
     * @param title email title
     * @param appointment appointment details
     * @param message additional message
     * @return formatted email body
     */
    private String buildEmailBody(String title, Appointment appointment, String message) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(appointment.getPatient().getFullName()).append(",\n\n");
        body.append(title).append("\n");
        body.append("=".repeat(title.length())).append("\n\n");
        body.append(message).append("\n\n");
        body.append("Appointment Details:\n");
        body.append("  Appointment ID: ").append(appointment.getId()).append("\n");
        body.append("  Doctor: Dr. ").append(appointment.getDoctor().getFullName()).append("\n");
        body.append("  Specialization: ").append(appointment.getDoctor().getSpecialization().getDisplayName()).append("\n");
        body.append("  Date & Time: ").append(DateUtil.formatDateTime(appointment.getAppointmentDateTime())).append("\n");
        body.append("  Status: ").append(appointment.getStatus().getDisplayName()).append("\n");
        
        if (appointment.getRoomNumber() != null) {
            body.append("  Room: ").append(appointment.getRoomNumber()).append("\n");
        }
        
        body.append("  Consultation Fee: $").append(String.format("%.2f", appointment.getFee())).append("\n\n");
        
        if (appointment.getSymptoms() != null && !appointment.getSymptoms().trim().isEmpty()) {
            body.append("Symptoms: ").append(appointment.getSymptoms()).append("\n\n");
        }
        
        body.append("If you have any questions, please contact our clinic.\n\n");
        body.append("Best regards,\n");
        body.append("MediTrack Clinic Team\n");
        body.append("Phone: (555) 123-4567\n");
        body.append("Email: info@meditrack.com\n");
        
        return body.toString();
    }
    
    /**
     * Send email (simulated)
     * @param subject email subject
     * @param body email body
     */
    private void sendEmail(String subject, String body) {
        // In a real implementation, this would use an email service
        // For demonstration, we'll just log the email
        System.out.println("📧 [" + observerName + "] EMAIL SENT");
        System.out.println("   To: " + emailAddress);
        System.out.println("   Subject: " + subject);
        System.out.println("   Body: " + body.substring(0, Math.min(100, body.length())) + "...");
        System.out.println("   Sent: " + DateUtil.getCurrentDateTime());
        System.out.println();
    }
    
    /**
     * Get email address
     * @return email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }
}
