package com.airtribe.meditrack.interface;

/**
 * Interface for entities that can be billed
 * Demonstrates interface usage with default methods
 */
public interface Payable {
    
    /**
     * Calculate the base amount before taxes
     * @return base amount
     */
    double calculateBaseAmount();
    
    /**
     * Calculate tax amount
     * @return tax amount
     */
    double calculateTax();
    
    /**
     * Calculate total amount including taxes
     * @return total amount
     */
    double calculateTotalAmount();
    
    /**
     * Get payment status
     * @return payment status
     */
    String getPaymentStatus();
    
    /**
     * Set payment status
     * @param status new payment status
     */
    void setPaymentStatus(String status);
    
    /**
     * Default method to calculate tax using standard rate
     * @param baseAmount base amount
     * @param taxRate tax rate (e.g., 0.15 for 15%)
     * @return tax amount
     */
    default double calculateTax(double baseAmount, double taxRate) {
        return baseAmount * taxRate;
    }
    
    /**
     * Default method to calculate total amount
     * @param baseAmount base amount
     * @param taxAmount tax amount
     * @return total amount
     */
    default double calculateTotal(double baseAmount, double taxAmount) {
        return baseAmount + taxAmount;
    }
    
    /**
     * Check if payment is completed
     * @return true if payment is completed
     */
    default boolean isPaymentCompleted() {
        return "PAID".equalsIgnoreCase(getPaymentStatus());
    }
}
