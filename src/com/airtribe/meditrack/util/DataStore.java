package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generic data store class for managing entities
 * Demonstrates generics, collections, and CRUD operations
 * @param <T> type of entity to store
 */
public class DataStore<T> {
    
    private final Map<String, T> dataMap;
    private final Class<T> entityClass;
    
    /**
     * Constructor
     * @param entityClass class of entities to store
     */
    public DataStore(Class<T> entityClass) {
        this.dataMap = new HashMap<>();
        this.entityClass = entityClass;
    }
    
    /**
     * Save an entity to the data store
     * @param entity entity to save
     * @return saved entity
     */
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        String id = extractId(entity);
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty");
        }
        
        dataMap.put(id, entity);
        return entity;
    }
    
    /**
     * Find entity by ID
     * @param id entity ID
     * @return entity or null if not found
     */
    public T findById(String id) {
        return id != null ? dataMap.get(id) : null;
    }
    
    /**
     * Find all entities
     * @return list of all entities
     */
    public List<T> findAll() {
        return new ArrayList<>(dataMap.values());
    }
    
    /**
     * Update an existing entity
     * @param entity entity to update
     * @return updated entity
     */
    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        String id = extractId(entity);
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty");
        }
        
        if (!dataMap.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID '" + id + "' not found");
        }
        
        dataMap.put(id, entity);
        return entity;
    }
    
    /**
     * Delete entity by ID
     * @param id entity ID
     * @return true if deleted, false if not found
     */
    public boolean deleteById(String id) {
        return id != null && dataMap.remove(id) != null;
    }
    
    /**
     * Delete entity
     * @param entity entity to delete
     * @return true if deleted, false if not found
     */
    public boolean delete(T entity) {
        if (entity == null) {
            return false;
        }
        
        String id = extractId(entity);
        return deleteById(id);
    }
    
    /**
     * Check if entity exists
     * @param id entity ID
     * @return true if exists
     */
    public boolean exists(String id) {
        return id != null && dataMap.containsKey(id);
    }
    
    /**
     * Check if entity exists
     * @param entity entity to check
     * @return true if exists
     */
    public boolean exists(T entity) {
        if (entity == null) {
            return false;
        }
        
        String id = extractId(entity);
        return exists(id);
    }
    
    /**
     * Get count of entities
     * @return number of entities
     */
    public int count() {
        return dataMap.size();
    }
    
    /**
     * Clear all entities
     */
    public void clear() {
        dataMap.clear();
    }
    
    /**
     * Check if data store is empty
     * @return true if empty
     */
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }
    
    /**
     * Get all entity IDs
     * @return list of entity IDs
     */
    public List<String> getAllIds() {
        return new ArrayList<>(dataMap.keySet());
    }
    
    /**
     * Search entities by query (for entities implementing Searchable)
     * @param query search query
     * @return list of matching entities
     */
    @SuppressWarnings("unchecked")
    public List<T> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        
        return dataMap.values().stream()
                .filter(entity -> {
                    try {
                        // Check if entity implements Searchable interface
                        if (entity instanceof com.airtribe.meditrack.interface.Searchable) {
                            return ((com.airtribe.meditrack.interface.Searchable) entity).matches(query);
                        }
                        
                        // Fallback to string comparison
                        return entity.toString().toLowerCase().contains(query.toLowerCase());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Filter entities by predicate
     * @param predicate filter predicate
     * @return list of filtered entities
     */
    public List<T> filter(java.util.function.Predicate<T> predicate) {
        return dataMap.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Sort entities by comparator
     * @param comparator comparator for sorting
     * @return sorted list of entities
     */
    public List<T> sort(java.util.Comparator<T> comparator) {
        return dataMap.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * Get entities in page format
     * @param page page number (1-based)
     * @param pageSize page size
     * @return page of entities
     */
    public List<T> getPage(int page, int pageSize) {
        if (page < 1 || pageSize < 1) {
            return new ArrayList<>();
        }
        
        List<T> allEntities = findAll();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allEntities.size());
        
        if (startIndex >= allEntities.size()) {
            return new ArrayList<>();
        }
        
        return allEntities.subList(startIndex, endIndex);
    }
    
    /**
     * Get total pages for given page size
     * @param pageSize page size
     * @return total pages
     */
    public int getTotalPages(int pageSize) {
        if (pageSize < 1) {
            return 0;
        }
        
        int totalEntities = count();
        return (int) Math.ceil((double) totalEntities / pageSize);
    }
    
    /**
     * Extract ID from entity using reflection
     * @param entity entity to extract ID from
     * @return entity ID
     */
    private String extractId(T entity) {
        try {
            // Try to getId() method first
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            // Fallback to toString() if getId() not available
            return entity.toString();
        }
    }
    
    /**
     * Get statistics about the data store
     * @return formatted statistics string
     */
    public String getStatistics() {
        return String.format(
            "DataStore Statistics for %s:\n" +
            "  Total Entities: %d\n" +
            "  Is Empty: %s\n" +
            "  Entity IDs: %s",
            entityClass.getSimpleName(),
            count(),
            isEmpty(),
            String.join(", ", getAllIds())
        );
    }
    
    /**
     * Validate all entities in the data store
     * @return list of invalid entities
     */
    @SuppressWarnings("unchecked")
    public List<T> validateAll() {
        return dataMap.values().stream()
                .filter(entity -> {
                    try {
                        // Check if entity has validate() method
                        java.lang.reflect.Method validateMethod = entity.getClass().getMethod("validate");
                        Boolean isValid = (Boolean) validateMethod.invoke(entity);
                        return !isValid;
                    } catch (Exception e) {
                        return true; // Consider invalid if validation fails
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get data as map (for serialization purposes)
     * @return map representation of data
     */
    public Map<String, T> getDataAsMap() {
        return new HashMap<>(dataMap);
    }
    
    /**
     * Load data from map (for deserialization purposes)
     * @param data map data to load
     */
    public void loadFromMap(Map<String, T> data) {
        if (data != null) {
            dataMap.clear();
            dataMap.putAll(data);
        }
    }
}
