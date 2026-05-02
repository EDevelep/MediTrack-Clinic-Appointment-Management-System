package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interface.Searchable;
import java.time.LocalDateTime;

/**
 * Abstract base class for all medical entities
 * Demonstrates abstraction and common behavior
 */
public abstract class MedicalEntity implements Searchable {
    
    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    /**
     * Default constructor
     */
    protected MedicalEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with ID
     * @param id unique identifier
     */
    protected MedicalEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
        updateTimestamp();
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Update the timestamp when entity is modified
     */
    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Abstract method to get display name
     * @return display name
     */
    public abstract String getDisplayName();
    
    /**
     * Abstract method to validate entity data
     * @return true if valid
     */
    public abstract boolean validate();
    
    /**
     * Default implementation of search matching
     * @param query search query
     * @return true if matches
     */
    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return false;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check ID match
        if (id != null && id.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check display name match
        if (getDisplayName() != null && getDisplayName().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MedicalEntity that = (MedicalEntity) obj;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
