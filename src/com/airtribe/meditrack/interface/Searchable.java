package com.airtribe.meditrack.interface;

/**
 * Interface for searchable entities
 * Demonstrates interface usage with default methods
 */
public interface Searchable {
    
    /**
     * Get the unique identifier for this entity
     * @return unique ID
     */
    String getId();
    
    /**
     * Get the name of this entity for search purposes
     * @return display name
     */
    String getDisplayName();
    
    /**
     * Check if this entity matches the search query
     * @param query search query
     * @return true if matches
     */
    boolean matches(String query);
    
    /**
     * Default method to perform case-insensitive search
     * @param text text to search in
     * @param query search query
     * @return true if query is found in text (case-insensitive)
     */
    default boolean containsIgnoreCase(String text, String query) {
        if (text == null || query == null) {
            return false;
        }
        return text.toLowerCase().contains(query.toLowerCase());
    }
    
    /**
     * Default method to check if entity matches by ID
     * @param id ID to match
     * @return true if IDs match
     */
    default boolean matchesById(String id) {
        return getId() != null && getId().equals(id);
    }
}
