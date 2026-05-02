package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

/**
 * Immutable BillSummary class
 * Demonstrates immutability, thread safety, and defensive programming
 */
public final class BillSummary {
    
    private final String billId;
    private final String patientName;
    private final String doctorName;
    private final LocalDateTime appointmentDate;
    private final double consultationFee;
    private final double additionalCharges;
    private final double discount;
    private final double taxAmount;
    private final double totalAmount;
    private final String paymentStatus;
    private final LocalDateTime paymentDate;
    private final LocalDateTime generatedAt;
    
    /**
     * Constructor for BillSummary
     * @param billId bill identifier
     * @param patientName patient full name
     * @param doctorName doctor full name
     * @param appointmentDate appointment date
     * @param consultationFee consultation fee
     * @param additionalCharges additional charges
     * @param discount discount amount
     * @param taxAmount tax amount
     * @param totalAmount total amount
     * @param paymentStatus payment status
     * @param paymentDate payment date
     */
    public BillSummary(String billId, String patientName, String doctorName, LocalDateTime appointmentDate,
                      double consultationFee, double additionalCharges, double discount, double taxAmount,
                      double totalAmount, String paymentStatus, LocalDateTime paymentDate) {
        this.billId = billId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.consultationFee = consultationFee;
        this.additionalCharges = additionalCharges;
        this.discount = discount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.generatedAt = LocalDateTime.now();
    }
    
    /**
     * Create BillSummary from Bill object
     * @param bill source bill
     * @return BillSummary instance
     */
    public static BillSummary fromBill(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }
        
        String patientName = "";
        String doctorName = "";
        LocalDateTime appointmentDate = null;
        
        if (bill.getAppointment() != null) {
            if (bill.getAppointment().getPatient() != null) {
                patientName = bill.getAppointment().getPatient().getFullName();
            }
            if (bill.getAppointment().getDoctor() != null) {
                doctorName = bill.getAppointment().getDoctor().getFullName();
            }
            appointmentDate = bill.getAppointment().getAppointmentDateTime();
        }
        
        return new BillSummary(
            bill.getId(),
            patientName,
            doctorName,
            appointmentDate,
            bill.getConsultationFee(),
            bill.getAdditionalCharges(),
            bill.getDiscount(),
            bill.getTaxAmount(),
            bill.getTotalAmount(),
            bill.getPaymentStatus(),
            bill.getPaymentDate()
        );
    }
    
    // Getters only - no setters for immutability
    public String getBillId() {
        return billId;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
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
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    /**
     * Get base amount (consultation + additional - discount)
     * @return base amount
     */
    public double getBaseAmount() {
        return consultationFee + additionalCharges - discount;
    }
    
    /**
     * Check if bill is paid
     * @return true if paid
     */
    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    /**
     * Check if bill has discount
     * @return true if discount > 0
     */
    public boolean hasDiscount() {
        return discount > 0;
    }
    
    /**
     * Check if bill has additional charges
     * @return true if additional charges > 0
     */
    public boolean hasAdditionalCharges() {
        return additionalCharges > 0;
    }
    
    /**
     * Get discount percentage
     * @return discount percentage (0-100)
     */
    public double getDiscountPercentage() {
        double subtotal = consultationFee + additionalCharges;
        if (subtotal == 0) return 0;
        return (discount / subtotal) * 100;
    }
    
    /**
     * Get tax percentage
     * @return tax percentage (0-100)
     */
    public double getTaxPercentage() {
        double baseAmount = getBaseAmount();
        if (baseAmount == 0) return 0;
        return (taxAmount / baseAmount) * 100;
    }
    
    @Override
    public String toString() {
        return "BillSummary{" +
                "billId='" + billId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", totalAmount=$" + String.format("%.2f", totalAmount) +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", generatedAt=" + generatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BillSummary that = (BillSummary) obj;
        
        return billId != null ? billId.equals(that.billId) : that.billId == null;
    }
    
    @Override
    public int hashCode() {
        return billId != null ? billId.hashCode() : 0;
    }
    
    /**
     * Generate formatted bill summary
     * @return formatted string representation
     */
    public String generateFormattedSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("           BILL SUMMARY\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Bill ID: ").append(billId).append("\n");
        sb.append("Generated: ").append(generatedAt).append("\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("Patient: ").append(patientName).append("\n");
        sb.append("Doctor: ").append(doctorName).append("\n");
        sb.append("Appointment: ").append(appointmentDate).append("\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("CONSULTATION FEE: $").append(String.format("%.2f", consultationFee)).append("\n");
        
        if (hasAdditionalCharges()) {
            sb.append("ADDITIONAL CHARGES: $").append(String.format("%.2f", additionalCharges)).append("\n");
        }
        
        sb.append("SUBTOTAL: $").append(String.format("%.2f", consultationFee + additionalCharges)).append("\n");
        
        if (hasDiscount()) {
            sb.append("DISCOUNT: -$").append(String.format("%.2f", discount))
              .append(" (").append(String.format("%.1f", getDiscountPercentage())).append("%)\n");
        }
        
        sb.append("BASE AMOUNT: $").append(String.format("%.2f", getBaseAmount())).append("\n");
        sb.append("TAX: $").append(String.format("%.2f", taxAmount))
          .append(" (").append(String.format("%.1f", getTaxPercentage())).append("%)\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("TOTAL AMOUNT: $").append(String.format("%.2f", totalAmount)).append("\n");
        sb.append("PAYMENT STATUS: ").append(paymentStatus).append("\n");
        
        if (paymentDate != null) {
            sb.append("PAID ON: ").append(paymentDate).append("\n");
        }
        
        sb.append("=".repeat(50)).append("\n");
        
        return sb.toString();
    }
}
