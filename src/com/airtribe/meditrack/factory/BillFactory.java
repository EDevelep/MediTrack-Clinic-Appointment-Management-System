package com.airtribe.meditrack.factory;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.util.IdGenerator;

/**
 * Factory class for creating different types of bills
 * Demonstrates Factory pattern for object creation
 */
public class BillFactory {
    
    /**
     * Bill types enumeration
     */
    public enum BillType {
        STANDARD,
        EMERGENCY,
        CONSULTATION,
        FOLLOW_UP,
        SPECIALIZED
    }
    
    /**
     * Create a standard bill
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createStandardBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Standard billing - consultation fee only
        bill.setConsultationFee(appointment.getFee());
        
        return bill;
    }
    
    /**
     * Create an emergency bill with additional charges
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createEmergencyBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Emergency billing - higher consultation fee
        double emergencyFee = appointment.getFee() * 1.5; // 50% surcharge
        bill.setConsultationFee(emergencyFee);
        
        // Add emergency service charge
        bill.addItemizedCharge("Emergency Service Fee", 50.0);
        bill.addItemizedCharge("Priority Processing", 25.0);
        
        return bill;
    }
    
    /**
     * Create a consultation-only bill
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createConsultationBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Consultation-only billing
        bill.setConsultationFee(appointment.getFee());
        
        // No additional charges for simple consultation
        return bill;
    }
    
    /**
     * Create a follow-up bill with discount
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createFollowUpBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Follow-up billing - discounted consultation fee
        double discountedFee = appointment.getFee() * 0.7; // 30% discount
        bill.setConsultationFee(discountedFee);
        
        // Add follow-up processing fee
        bill.addItemizedCharge("Follow-up Processing", 10.0);
        
        return bill;
    }
    
    /**
     * Create a specialized bill based on doctor's specialization
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createSpecializedBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Base consultation fee
        bill.setConsultationFee(appointment.getFee());
        
        // Add specialization-specific charges
        String specialization = appointment.getDoctor().getSpecialization().toString();
        
        switch (specialization) {
            case "CARDIOLOGY":
                bill.addItemizedCharge("Cardiac Assessment", 75.0);
                bill.addItemizedCharge("ECG Interpretation", 50.0);
                break;
            case "NEUROLOGY":
                bill.addItemizedCharge("Neurological Examination", 80.0);
                bill.addItemizedCharge("Cognitive Assessment", 40.0);
                break;
            case "ORTHOPEDICS":
                bill.addItemizedCharge("Orthopedic Evaluation", 60.0);
                bill.addItemizedCharge("Range of Motion Test", 30.0);
                break;
            case "DERMATOLOGY":
                bill.addItemizedCharge("Dermatological Examination", 45.0);
                bill.addItemizedCharge("Skin Analysis", 35.0);
                break;
            case "PEDIATRICS":
                bill.addItemizedCharge("Pediatric Assessment", 55.0);
                bill.addItemizedCharge("Growth Evaluation", 25.0);
                break;
            default:
                bill.addItemizedCharge("Specialized Consultation", 40.0);
                break;
        }
        
        return bill;
    }
    
    /**
     * Create bill based on type
     * @param billType type of bill to create
     * @param appointment the appointment
     * @return created bill
     */
    public static Bill createBill(BillType billType, Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        switch (billType) {
            case STANDARD:
                return createStandardBill(appointment);
            case EMERGENCY:
                return createEmergencyBill(appointment);
            case CONSULTATION:
                return createConsultationBill(appointment);
            case FOLLOW_UP:
                return createFollowUpBill(appointment);
            case SPECIALIZED:
                return createSpecializedBill(appointment);
            default:
                throw new IllegalArgumentException("Unknown bill type: " + billType);
        }
    }
    
    /**
     * Create bill with custom charges
     * @param appointment the appointment
     * @param customCharges array of charge descriptions and amounts
     * @return created bill
     */
    public static Bill createCustomBill(Appointment appointment, String[] customCharges) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Base consultation fee
        bill.setConsultationFee(appointment.getFee());
        
        // Add custom charges
        if (customCharges != null) {
            for (String charge : customCharges) {
                if (charge != null && charge.contains(":")) {
                    String[] parts = charge.split(":");
                    if (parts.length == 2) {
                        try {
                            String description = parts[0].trim();
                            double amount = Double.parseDouble(parts[1].trim());
                            bill.addItemizedCharge(description, amount);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid charge format: " + charge);
                        }
                    }
                }
            }
        }
        
        return bill;
    }
    
    /**
     * Create bill with discount
     * @param appointment the appointment
     * @param discountPercentage discount percentage (0-100)
     * @return created bill
     */
    public static Bill createDiscountedBill(Appointment appointment, double discountPercentage) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        Bill bill = new Bill(IdGenerator.generateBillId(), appointment);
        
        // Calculate discounted consultation fee
        double discountedFee = appointment.getFee() * (1 - discountPercentage / 100);
        bill.setConsultationFee(discountedFee);
        
        return bill;
    }
    
    /**
     * Create bill summary from bill
     * @param bill the bill
     * @return bill summary
     */
    public static BillSummary createBillSummary(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }
        
        return BillSummary.fromBill(bill);
    }
    
    /**
     * Get bill type description
     * @param billType bill type
     * @return description
     */
    public static String getBillTypeDescription(BillType billType) {
        switch (billType) {
            case STANDARD:
                return "Standard Consultation Bill";
            case EMERGENCY:
                return "Emergency Service Bill";
            case CONSULTATION:
                return "Consultation-Only Bill";
            case FOLLOW_UP:
                return "Follow-up Visit Bill";
            case SPECIALIZED:
                return "Specialized Treatment Bill";
            default:
                return "Unknown Bill Type";
        }
    }
    
    /**
     * Get available bill types
     * @return array of available bill types
     */
    public static BillType[] getAvailableBillTypes() {
        return BillType.values();
    }
    
    /**
     * Calculate estimated bill amount for type
     * @param billType bill type
     * @param baseFee base consultation fee
     * @return estimated total amount
     */
    public static double estimateBillAmount(BillType billType, double baseFee) {
        switch (billType) {
            case STANDARD:
                return baseFee * 1.15; // Base fee + tax
            case EMERGENCY:
                return (baseFee * 1.5 + 75.0) * 1.15; // 50% surcharge + emergency charges + tax
            case CONSULTATION:
                return baseFee * 1.15; // Base fee + tax
            case FOLLOW_UP:
                return (baseFee * 0.7 + 10.0) * 1.15; // 30% discount + processing + tax
            case SPECIALIZED:
                return (baseFee + 75.0) * 1.15; // Base fee + specialized charges + tax
            default:
                return baseFee * 1.15;
        }
    }
}
