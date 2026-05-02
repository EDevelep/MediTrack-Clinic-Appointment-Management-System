package com.airtribe.meditrack.entity;

/**
 * Enum representing appointment status
 * Demonstrates enum usage with state transitions
 */
public enum AppointmentStatus {
    PENDING("Pending", "Appointment requested but not confirmed"),
    CONFIRMED("Confirmed", "Appointment scheduled and confirmed"),
    CANCELLED("Cancelled", "Appointment cancelled by patient or clinic"),
    COMPLETED("Completed", "Appointment has taken place"),
    NO_SHOW("No Show", "Patient did not attend the appointment");

    private final String displayName;
    private final String description;

    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Check if status transition is valid
     * @param fromStatus current status
     * @param toStatus target status
     * @return true if transition is valid
     */
    public static boolean isValidTransition(AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        if (fromStatus == null) return false;
        
        switch (fromStatus) {
            case PENDING:
                return toStatus == CONFIRMED || toStatus == CANCELLED;
            case CONFIRMED:
                return toStatus == COMPLETED || toStatus == CANCELLED || toStatus == NO_SHOW;
            case CANCELLED:
            case COMPLETED:
            case NO_SHOW:
                return false; // Terminal states
            default:
                return false;
        }
    }

    /**
     * Find status by display name (case-insensitive)
     * @param displayName the display name to search for
     * @return matching status or null
     */
    public static AppointmentStatus findByDisplayName(String displayName) {
        for (AppointmentStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        return null;
    }
}
