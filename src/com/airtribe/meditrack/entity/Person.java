package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.Validator;

/**
 * Person class representing a person in the medical system
 * Demonstrates inheritance and encapsulation
 */
public class Person extends MedicalEntity {
    
    private String firstName;
    private String lastName;
    private int age;
    private String phoneNumber;
    private String email;
    private String address;
    
    /**
     * Default constructor
     */
    public Person() {
        super();
    }
    
    /**
     * Constructor with basic information
     * @param id unique identifier
     * @param firstName first name
     * @param lastName last name
     * @param age age
     * @param phoneNumber phone number
     */
    public Person(String id, String firstName, String lastName, int age, String phoneNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Full constructor
     * @param id unique identifier
     * @param firstName first name
     * @param lastName last name
     * @param age age
     * @param phoneNumber phone number
     * @param email email address
     * @param address physical address
     */
    public Person(String id, String firstName, String lastName, int age, String phoneNumber, String email, String address) {
        this(id, firstName, lastName, age, phoneNumber);
        this.email = email;
        this.address = address;
    }
    
    // Getters
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getAddress() {
        return address;
    }
    
    // Setters with validation
    public void setFirstName(String firstName) {
        if (Validator.isValidName(firstName)) {
            this.firstName = firstName;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid first name: " + firstName);
        }
    }
    
    public void setLastName(String lastName) {
        if (Validator.isValidName(lastName)) {
            this.lastName = lastName;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid last name: " + lastName);
        }
    }
    
    public void setAge(int age) {
        if (Validator.isValidAge(age)) {
            this.age = age;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid age: " + age + ". Must be between " + Constants.MIN_AGE + " and " + Constants.MAX_AGE);
        }
    }
    
    public void setPhoneNumber(String phoneNumber) {
        if (Validator.isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
        }
    }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || Validator.isValidEmail(email)) {
            this.email = email;
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }
    
    public void setAddress(String address) {
        this.address = address;
        updateTimestamp();
    }
    
    /**
     * Get full name
     * @return full name (first + last)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String getDisplayName() {
        return getFullName();
    }
    
    @Override
    public boolean validate() {
        return Validator.isValidName(firstName) &&
               Validator.isValidName(lastName) &&
               Validator.isValidAge(age) &&
               Validator.isValidPhoneNumber(phoneNumber) &&
               (email == null || email.trim().isEmpty() || Validator.isValidEmail(email));
    }
    
    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check name components
        if (firstName != null && firstName.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        if (lastName != null && lastName.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check email
        if (email != null && email.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check phone
        if (phoneNumber != null && phoneNumber.contains(query)) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
