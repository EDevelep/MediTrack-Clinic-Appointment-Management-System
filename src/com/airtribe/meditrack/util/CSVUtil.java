package com.airtribe.meditrack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for CSV operations
 * Demonstrates file I/O, try-with-resources, and CSV parsing
 */
public final class CSVUtil {
    
    // Private constructor to prevent instantiation
    private CSVUtil() {
        throw new AssertionError("CSVUtil class should not be instantiated");
    }
    
    /**
     * Read CSV file and return list of rows
     * @param filePath path to CSV file
     * @return list of rows, where each row is a list of string values
     * @throws IOException if file cannot be read
     */
    public static List<List<String>> readCSV(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Skip header line if needed (uncomment if you want to skip headers)
                // if (isFirstLine) {
                //     isFirstLine = false;
                //     continue;
                // }
                
                List<String> row = parseCSVLine(line);
                data.add(row);
            }
        }
        
        return data;
    }
    
    /**
     * Parse a single CSV line into list of values
     * @param line CSV line to parse
     * @return list of values
     */
    public static List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentValue.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        
        // Add the last value
        values.add(currentValue.toString().trim());
        
        return values;
    }
    
    /**
     * Write data to CSV file
     * @param filePath path to CSV file
     * @param data list of rows to write
     * @param includeHeader whether to include header row
     * @throws IOException if file cannot be written
     */
    public static void writeCSV(String filePath, List<List<String>> data, boolean includeHeader) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (List<String> row : data) {
                String line = formatCSVLine(row);
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Format a list of values into CSV line
     * @param values list of values to format
     * @return CSV line string
     */
    public static String formatCSVLine(List<String> values) {
        StringBuilder line = new StringBuilder();
        
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i) != null ? values.get(i) : "";
            
            // Quote value if it contains comma, quote, or newline
            if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                // Escape quotes by doubling them
                value = value.replace("\"", "\"\"");
                line.append("\"").append(value).append("\"");
            } else {
                line.append(value);
            }
            
            if (i < values.size() - 1) {
                line.append(",");
            }
        }
        
        return line.toString();
    }
    
    /**
     * Read CSV file with headers and return as list of string arrays
     * @param filePath path to CSV file
     * @return list of rows as string arrays
     * @throws IOException if file cannot be read
     */
    public static List<String[]> readCSVWithHeaders(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    List<String> row = parseCSVLine(line);
                    data.add(row.toArray(new String[0]));
                }
            }
        }
        
        return data;
    }
    
    /**
     * Write CSV file with headers
     * @param filePath path to CSV file
     * @param headers header row
     * @param data data rows
     * @throws IOException if file cannot be written
     */
    public static void writeCSVWithHeaders(String filePath, String[] headers, List<String[]> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write headers
            writer.write(formatCSVLine(List.of(headers)));
            writer.newLine();
            
            // Write data rows
            for (String[] row : data) {
                writer.write(formatCSVLine(List.of(row)));
                writer.newLine();
            }
        }
    }
    
    /**
     * Append data to existing CSV file
     * @param filePath path to CSV file
     * @param row row to append
     * @throws IOException if file cannot be written
     */
    public static void appendToCSV(String filePath, List<String> row) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = formatCSVLine(row);
            writer.write(line);
            writer.newLine();
        }
    }
    
    /**
     * Convert string array to list
     * @param array string array
     * @return list of strings
     */
    public static List<String> arrayToList(String[] array) {
        List<String> list = new ArrayList<>();
        if (array != null) {
            for (String value : array) {
                list.add(value);
            }
        }
        return list;
    }
    
    /**
     * Convert list to string array
     * @param list list of strings
     * @return string array
     */
    public static String[] listToArray(List<String> list) {
        if (list == null) {
            return new String[0];
        }
        return list.toArray(new String[0]);
    }
    
    /**
     * Validate CSV format
     * @param line line to validate
     * @return true if valid CSV format
     */
    public static boolean isValidCSVLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }
        
        try {
            parseCSVLine(line);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Escape value for CSV
     * @param value value to escape
     * @return escaped value
     */
    public static String escapeCSVValue(String value) {
        if (value == null) {
            return "";
        }
        
        // Escape quotes and wrap in quotes if needed
        if (value.contains("\"") || value.contains(",") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
    
    /**
     * Unescape CSV value
     * @param value value to unescape
     * @return unescaped value
     */
    public static String unescapeCSVValue(String value) {
        if (value == null) {
            return "";
        }
        
        // Remove surrounding quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            // Unescape quotes
            value = value.replace("\"\"", "\"");
        }
        
        return value;
    }
}
