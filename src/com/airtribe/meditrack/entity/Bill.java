package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interface.Payable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill class representing a medical bill
 * Demonstrates interface implementation, polymorphism, and billing strategies
 */
public class Bill extends MedicalEntity implements Payable {
    
    private Appointment appointment;
    private double consultationFee;
    private double additionalCharges;
    private double discount;
    private double taxAmount;
    private double totalAmount;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private List<String> itemizedCharges;
    
    /**
     * Default constructor
     */
    public Bill() {
        super();
        this.paymentStatus = "PENDING";
        this.itemizedCharges = new ArrayList<>();
    }
    
    /**
     * Constructor with appointment
     * @param id unique identifier
     * @param appointment related appointment
     */
    public Bill(String id, Appointment appointment) {
        super(id);
        this.appointment = appointment;
        this.consultationFee = appointment != null ? appointment.getFee() : Constants.CONSULTATION_FEE;
        this.additionalCharges = 0.0;
        this.discount = 0.0;
        this.paymentStatus = "PENDING";
        this.itemizedCharges = new ArrayList<>();
        calculateTotals();
    }
    
    /**
     * Full constructor
     * @param id unique identifier
     * @param appointment related appointment
     * @param consultationFee consultation fee
     * @param additionalCharges additional charges
     * @param discount discount amount
     */
    public Bill(String id, Appointment appointment, double consultationFee, 
                double additionalCharges, double discount) {
        super(id);
        this.appointment = appointment;
        this.consultationFee = consultationFee;
        this.additionalCharges = additionalCharges;
        this.discount = discount;
        this.paymentStatus = "PENDING";
        this.itemizedCharges = new ArrayList<>();
        calculateTotals();
    }
    
    // Getters
    public Appointment getAppointment() {
        return appointment;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public double getAdditionalCharges() {
        return additionalCharges;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public List<String> getItemizedCharges() {
        return new ArrayList<>(itemizedCharges); // Defensive copy
    }
    
    // Setters with validation
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        if (appointment != null) {
            this.consultationFee = appointment.getFee();
        }
        calculateTotals();
        updateTimestamp();
    }
    
    public void setConsultationFee(double consultationFee) {
        if (consultationFee >= 0) {
            this.consultationFee = consultationFee;
            calculateTotals();
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Consultation fee cannot be negative: " + consultationFee);
        }
    }
    
    public void setAdditionalCharges(double additionalCharges) {
        if (additionalCharges >= 0) {
            this.additionalCharges = additionalCharges;
            calculateTotals();
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Additional charges cannot be negative: " + additionalCharges);
        }
    }
    
    public void setDiscount(double discount) {
        if (discount >= 0 && discount <= (consultationFee + additionalCharges)) {
            this.discount = discount;
            calculateTotals();
            updateTimestamp();
        } else {
            throw new IllegalArgumentException("Invalid discount amount: " + discount);
        }
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        if ("PAID".equals(paymentStatus)) {
            this.paymentDate = LocalDateTime.now();
        }
        updateTimestamp();
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        updateTimestamp();
    }
    
    /**
     * Add itemized charge
     * @param charge description of charge
     * @param amount charge amount
     */
    public void addItemizedCharge(String charge, double amount) {
        if (charge != null && !charge.trim().isEmpty() && amount > 0) {
            itemizedCharges.add(charge + ": $" + String.format("%.2f", amount));
            this.additionalCharges += amount;
            calculateTotals();
            updateTimestamp();
        }
    }
    
    /**
     * Remove itemized charge
     * @param charge charge description to remove
     * @return true if removed
     */
    public boolean removeItemizedCharge(String charge) {
        boolean removed = itemizedCharges.removeIf(c -> c.startsWith(charge + ":"));
        if (removed) {
            recalculateAdditionalCharges();
            calculateTotals();
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Recalculate additional charges from itemized charges
     */
    private void recalculateAdditionalCharges() {
        this.additionalCharges = itemizedCharges.stream()
            .mapToDouble(charge -> {
                String[] parts = charge.split(":");
                if (parts.length == 2) {
                    try {
                        return Double.parseDouble(parts[1].replace("$", "").trim());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                }
                return 0.0;
            })
            .sum();
    }
    
    /**
     * Process payment
     * @param paymentMethod payment method used
     */
    public void processPayment(String paymentMethod) {
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            this.paymentMethod = paymentMethod;
            this.paymentStatus = "PAID";
            this.paymentDate = LocalDateTime.now();
            updateTimestamp();
        }
    }
    
    /**
     * Refund payment
     */
    public void refund() {
        this.paymentStatus = "REFUNDED";
        updateTimestamp();
    }
    
    /**
     * Check if bill is paid
     * @return true if paid
     */
    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    /**
     * Check if bill is overdue
     * @return true if overdue
     */
    public boolean isOverdue() {
        if (isPaid()) return false;
        
        if (appointment != null) {
            LocalDateTime dueDate = appointment.getAppointmentDateTime().plusDays(30);
            return LocalDateTime.now().isAfter(dueDate);
        }
        
        return false;
    }
    
    @Override
    public double calculateBaseAmount() {
        return consultationFee + additionalCharges - discount;
    }
    
    @Override
    public double calculateTax() {
        return calculateTax(calculateBaseAmount(), Constants.TAX_RATE);
    }
    
    @Override
    public double calculateTotalAmount() {
        return calculateTotal(calculateBaseAmount(), calculateTax());
    }
    
    /**
     * Calculate all amounts (tax and total)
     */
    private void calculateTotals() {
        double baseAmount = calculateBaseAmount();
        this.taxAmount = calculateTax(baseAmount, Constants.TAX_RATE);
        this.totalAmount = calculateTotal(baseAmount, this.taxAmount);
    }
    
    @Override
    public String getDisplayName() {
        return "Bill #" + id + " - " + (appointment != null ? appointment.getPatient().getFullName() : "Unknown") + 
               " - $" + String.format("%.2f", totalAmount);
    }
    
    @Override
    public boolean validate() {
        return appointment != null && appointment.validate() &&
               consultationFee >= 0 &&
               additionalCharges >= 0 &&
               discount >= 0 &&
               paymentStatus != null;
    }
    
    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Check appointment information
        if (appointment != null && appointment.matches(query)) {
            return true;
        }
        
        // Check payment status
        if (paymentStatus != null && paymentStatus.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check payment method
        if (paymentMethod != null && paymentMethod.toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check amounts
        if (String.valueOf(totalAmount).contains(query)) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", appointment=" + (appointment != null ? appointment.getId() : "null") +
                ", consultationFee=" + consultationFee +
                ", additionalCharges=" + additionalCharges +
                ", discount=" + discount +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
