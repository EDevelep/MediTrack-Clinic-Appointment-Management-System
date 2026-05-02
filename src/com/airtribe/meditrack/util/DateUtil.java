package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Utility class for date and time operations
 * Demonstrates date formatting, parsing, and manipulation
 */
public final class DateUtil {
    
    // Date formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
    
    // Private constructor to prevent instantiation
    private DateUtil() {
        throw new AssertionError("DateUtil class should not be instantiated");
    }
    
    /**
     * Format date to string
     * @param date date to format
     * @return formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
    
    /**
     * Format time to string
     * @param time time to format
     * @return formatted time string
     */
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }
    
    /**
     * Format date and time to string
     * @param dateTime date and time to format
     * @return formatted date and time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }
    
    /**
     * Parse date string
     * @param dateString date string to parse
     * @return parsed date
     * @throws DateTimeParseException if parsing fails
     */
    public static LocalDate parseDate(String dateString) throws DateTimeParseException {
        return dateString != null && !dateString.trim().isEmpty() ? 
               LocalDate.parse(dateString.trim(), DATE_FORMATTER) : null;
    }
    
    /**
     * Parse time string
     * @param timeString time string to parse
     * @return parsed time
     * @throws DateTimeParseException if parsing fails
     */
    public static LocalTime parseTime(String timeString) throws DateTimeParseException {
        return timeString != null && !timeString.trim().isEmpty() ? 
               LocalTime.parse(timeString.trim(), TIME_FORMATTER) : null;
    }
    
    /**
     * Parse date and time string
     * @param dateTimeString date and time string to parse
     * @return parsed date and time
     * @throws DateTimeParseException if parsing fails
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        return dateTimeString != null && !dateTimeString.trim().isEmpty() ? 
               LocalDateTime.parse(dateTimeString.trim(), DATETIME_FORMATTER) : null;
    }
    
    /**
     * Combine date and time into LocalDateTime
     * @param date date component
     * @param time time component
     * @return combined LocalDateTime
     */
    public static LocalDateTime combine(LocalDate date, LocalTime time) {
        if (date == null) return null;
        return time != null ? LocalDateTime.of(date, time) : date.atStartOfDay();
    }
    
    /**
     * Get current date as formatted string
     * @return current date string
     */
    public static String getCurrentDate() {
        return formatDate(LocalDate.now());
    }
    
    /**
     * Get current time as formatted string
     * @return current time string
     */
    public static String getCurrentTime() {
        return formatTime(LocalTime.now());
    }
    
    /**
     * Get current date and time as formatted string
     * @return current date and time string
     */
    public static String getCurrentDateTime() {
        return formatDateTime(LocalDateTime.now());
    }
    
    /**
     * Add days to a date
     * @param date original date
     * @param days number of days to add (can be negative)
     * @return new date
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date != null ? date.plusDays(days) : null;
    }
    
    /**
     * Add hours to a date and time
     * @param dateTime original date and time
     * @param hours number of hours to add (can be negative)
     * @return new date and time
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, int hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }
    
    /**
     * Add minutes to a date and time
     * @param dateTime original date and time
     * @param minutes number of minutes to add (can be negative)
     * @return new date and time
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, int minutes) {
        return dateTime != null ? dateTime.plusMinutes(minutes) : null;
    }
    
    /**
     * Check if a date is today
     * @param date date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * Check if a date and time is today
     * @param dateTime date and time to check
     * @return true if date and time is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        return dateTime != null && dateTime.toLocalDate().equals(LocalDate.now());
    }
    
    /**
     * Check if a date is in the past
     * @param date date to check
     * @return true if date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if a date and time is in the past
     * @param dateTime date and time to check
     * @return true if date and time is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if a date is in the future
     * @param date date to check
     * @return true if date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if a date and time is in the future
     * @param dateTime date and time to check
     * @return true if date and time is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Get days between two dates
     * @param startDate start date
     * @param endDate end date
     * @return number of days between dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Get hours between two date and times
     * @param startDateTime start date and time
     * @param endDateTime end date and time
     * @return number of hours between date and times
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return java.time.temporal.ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }
    
    /**
     * Get minutes between two date and times
     * @param startDateTime start date and time
     * @param endDateTime end date and time
     * @return number of minutes between date and times
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return java.time.temporal.ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }
    
    /**
     * Check if two date ranges overlap
     * @param start1 start of first range
     * @param end1 end of first range
     * @param start2 start of second range
     * @param end2 end of second range
     * @return true if ranges overlap
     */
    public static boolean rangesOverlap(LocalDateTime start1, LocalDateTime end1, 
                                      LocalDateTime start2, LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        
        return !end1.isBefore(start2) && !start1.isAfter(end2);
    }
    
    /**
     * Get the next available appointment slot
     * @param startTime start time to search from
     * @param durationMinutes duration in minutes
     * @return next available slot
     */
    public static LocalDateTime getNextAvailableSlot(LocalDateTime startTime, int durationMinutes) {
        if (startTime == null) {
            startTime = LocalDateTime.now().plusHours(1); // Default to 1 hour from now
        }
        
        // Round to next 30-minute slot
        LocalDateTime roundedTime = startTime;
        int minutes = roundedTime.getMinute();
        if (minutes % 30 != 0) {
            roundedTime = roundedTime.plusMinutes(30 - (minutes % 30));
        }
        
        // Ensure it's during business hours (9 AM - 5 PM)
        LocalTime businessStart = LocalTime.of(9, 0);
        LocalTime businessEnd = LocalTime.of(17, 0);
        
        if (roundedTime.toLocalTime().isBefore(businessStart)) {
            roundedTime = roundedTime.with(businessStart);
        } else if (roundedTime.toLocalTime().isAfter(businessEnd.minusMinutes(durationMinutes))) {
            roundedTime = roundedTime.plusDays(1).with(businessStart);
        }
        
        return roundedTime;
    }
    
    /**
     * Validate date string format
     * @param dateString date string to validate
     * @return true if valid format
     */
    public static boolean isValidDate(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validate time string format
     * @param timeString time string to validate
     * @return true if valid format
     */
    public static boolean isValidTime(String timeString) {
        try {
            parseTime(timeString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validate date and time string format
     * @param dateTimeString date and time string to validate
     * @return true if valid format
     */
    public static boolean isValidDateTime(String dateTimeString) {
        try {
            parseDateTime(dateTimeString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
