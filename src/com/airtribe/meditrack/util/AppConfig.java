package com.airtribe.meditrack.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Singleton class for application configuration
 * Demonstrates Singleton pattern (eager and lazy initialization)
 */
public class AppConfig {
    
    // Eager initialization - instance created when class is loaded
    private static final AppConfig INSTANCE = new AppConfig();
    
    private Properties properties;
    private final String CONFIG_FILE = "data/app.properties";
    
    // Private constructor to prevent instantiation
    private AppConfig() {
        properties = new Properties();
        loadDefaultProperties();
        loadPropertiesFromFile();
    }
    
    /**
     * Get the singleton instance
     * @return AppConfig instance
     */
    public static AppConfig getInstance() {
        return INSTANCE;
    }
    
    /**
     * Lazy initialization alternative (commented out to show difference)
     * Uncomment this method and comment out eager initialization above to use lazy initialization
     */
    /*
    private static volatile AppConfig instance;
    
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    */
    
    /**
     * Load default properties
     */
    private void loadDefaultProperties() {
        properties.setProperty("app.name", "MediTrack Clinic Appointment Management System");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("app.author", "MediTrack Team");
        properties.setProperty("tax.rate", "0.15");
        properties.setProperty("service.charge", "0.05");
        properties.setProperty("consultation.fee", "100.0");
        properties.setProperty("appointment.duration", "30");
        properties.setProperty("max.appointments.per.day", "50");
        properties.setProperty("data.save.on.exit", "true");
        properties.setProperty("backup.enabled", "true");
        properties.setProperty("backup.interval.hours", "24");
        properties.setProperty("notification.enabled", "true");
        properties.setProperty("notification.reminder.hours", "2");
    }
    
    /**
     * Load properties from file
     */
    private void loadPropertiesFromFile() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            System.out.println("Configuration loaded from file: " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("Configuration file not found, using defaults: " + e.getMessage());
        }
    }
    
    /**
     * Save properties to file
     */
    public void savePropertiesToFile() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "MediTrack Application Configuration");
            System.out.println("Configuration saved to file: " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }
    
    /**
     * Get property value
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     * @param key property key
     * @param defaultValue default value
     * @return property value or default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property as integer
     * @param key property key
     * @return property value as integer
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    /**
     * Get property as integer with default
     * @param key property key
     * @param defaultValue default value
     * @return property value as integer or default
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get property as double
     * @param key property key
     * @return property value as double
     */
    public double getDoubleProperty(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }
    
    /**
     * Get property as double with default
     * @param key property key
     * @param defaultValue default value
     * @return property value as double or default
     */
    public double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get property as boolean
     * @param key property key
     * @return property value as boolean
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    /**
     * Get property as boolean with default
     * @param key property key
     * @param defaultValue default value
     * @return property value as boolean or default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, double value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Get all properties
     * @return copy of all properties
     */
    public Properties getAllProperties() {
        Properties copy = new Properties();
        copy.putAll(properties);
        return copy;
    }
    
    /**
     * Reset to default properties
     */
    public void resetToDefaults() {
        properties.clear();
        loadDefaultProperties();
        System.out.println("Configuration reset to defaults");
    }
    
    /**
     * Print all configuration properties
     */
    public void printConfiguration() {
        System.out.println("=".repeat(50));
        System.out.println("APPLICATION CONFIGURATION");
        System.out.println("=".repeat(50));
        
        properties.forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
        
        System.out.println("=".repeat(50));
    }
    
    /**
     * Get configuration summary
     * @return formatted configuration summary
     */
    public String getConfigurationSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration Summary:\n");
        sb.append("  App Name: ").append(getProperty("app.name")).append("\n");
        sb.append("  Version: ").append(getProperty("app.version")).append("\n");
        sb.append("  Tax Rate: ").append(getDoubleProperty("tax.rate") * 100).append("%\n");
        sb.append("  Service Charge: ").append(getDoubleProperty("service.charge") * 100).append("%\n");
        sb.append("  Consultation Fee: $").append(getDoubleProperty("consultation.fee")).append("\n");
        sb.append("  Appointment Duration: ").append(getIntProperty("appointment.duration")).append(" minutes\n");
        sb.append("  Max Appointments/Day: ").append(getIntProperty("max.appointments.per.day")).append("\n");
        sb.append("  Data Save on Exit: ").append(getBooleanProperty("data.save.on.exit")).append("\n");
        sb.append("  Backup Enabled: ").append(getBooleanProperty("backup.enabled")).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Validate configuration
     * @return true if configuration is valid
     */
    public boolean validateConfiguration() {
        boolean isValid = true;
        
        // Check required properties
        if (getProperty("app.name") == null) {
            System.out.println("Missing app.name property");
            isValid = false;
        }
        
        if (getDoubleProperty("tax.rate", -1) < 0) {
            System.out.println("Invalid tax.rate property");
            isValid = false;
        }
        
        if (getDoubleProperty("consultation.fee", -1) < 0) {
            System.out.println("Invalid consultation.fee property");
            isValid = false;
        }
        
        if (getIntProperty("appointment.duration", -1) <= 0) {
            System.out.println("Invalid appointment.duration property");
            isValid = false;
        }
        
        return isValid;
    }
}
