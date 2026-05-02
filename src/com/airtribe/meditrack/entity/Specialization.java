package com.airtribe.meditrack.entity;

/**
 * Enum representing doctor specializations
 * Demonstrates enum usage in Java
 */
public enum Specialization {
    CARDIOLOGY("Cardiology", "Heart and cardiovascular system"),
    NEUROLOGY("Neurology", "Brain and nervous system"),
    ORTHOPEDICS("Orthopedics", "Bones and joints"),
    PEDIATRICS("Pediatrics", "Children's health"),
    DERMATOLOGY("Dermatology", "Skin and related conditions"),
    PSYCHIATRY("Psychiatry", "Mental health"),
    GENERAL_MEDICINE("General Medicine", "General health concerns"),
    ONCOLOGY("Oncology", "Cancer treatment"),
    GYNECOLOGY("Gynecology", "Women's health"),
    OPHTHALMOLOGY("Ophthalmology", "Eye care");

    private final String displayName;
    private final String description;

    Specialization(String displayName, String description) {
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
     * Find specialization by display name (case-insensitive)
     * @param displayName the display name to search for
     * @return matching specialization or null
     */
    public static Specialization findByDisplayName(String displayName) {
        for (Specialization spec : values()) {
            if (spec.displayName.equalsIgnoreCase(displayName)) {
                return spec;
            }
        }
        return null;
    }
}
