package com.airtribe.meditrack.exception;

/**
 * Custom exception for invalid data
 * Demonstrates custom exception creation and validation
 */
public class InvalidDataException extends Exception {
    
    private String fieldName;
    private Object invalidValue;
    
    /**
     * Default constructor
     */
    public InvalidDataException() {
        super("Invalid data provided");
    }
    
    /**
     * Constructor with message
     * @param message error message
     */
    public InvalidDataException(String message) {
        super(message);
    }
    
    /**
     * Constructor with field name and invalid value
     * @param fieldName name of the field with invalid data
     * @param invalidValue the invalid value
     */
    public InvalidDataException(String fieldName, Object invalidValue) {
        super("Invalid value for field '" + fieldName + "': " + invalidValue);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    /**
     * Constructor with field name, invalid value, and expected format
     * @param fieldName name of the field with invalid data
     * @param invalidValue the invalid value
     * @param expectedFormat expected format description
     */
    public InvalidDataException(String fieldName, Object invalidValue, String expectedFormat) {
        super("Invalid value for field '" + fieldName + "': " + invalidValue + ". Expected: " + expectedFormat);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    /**
     * Constructor with message and cause
     * @param message error message
     * @param cause underlying cause
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with field name, invalid value, and cause
     * @param fieldName name of the field with invalid data
     * @param invalidValue the invalid value
     * @param cause underlying cause
     */
    public InvalidDataException(String fieldName, Object invalidValue, Throwable cause) {
        super("Invalid value for field '" + fieldName + "': " + invalidValue, cause);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    /**
     * Get the field name with invalid data
     * @return field name
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * Get the invalid value
     * @return invalid value
     */
    public Object getInvalidValue() {
        return invalidValue;
    }
    
    @Override
    public String toString() {
        if (fieldName != null && invalidValue != null) {
            return "InvalidDataException: Invalid value for field '" + fieldName + "': " + invalidValue;
        }
        return "InvalidDataException: " + getMessage();
    }
}
