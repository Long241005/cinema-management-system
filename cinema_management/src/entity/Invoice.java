package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Hóa Đơn (Invoice)
 */
public class Invoice implements Serializable {
    private int invoiceId;
    private int customerId;
    private int employeeId;
    private LocalDateTime invoiceDate;
    private double subtotal;
    private double discountAmount;
    private double totalAmount;
    private String paymentMethod; // CASH, CREDIT_CARD, DEBIT_CARD, ONLINE
    private String status; // PAID, PENDING, CANCELLED

    public Invoice() {}

    public Invoice(int invoiceId, int customerId, int employeeId, LocalDateTime invoiceDate,
                   double subtotal, double discountAmount, double totalAmount,
                   String paymentMethod, String status) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.invoiceDate = invoiceDate;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters and Setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", totalAmount=" + totalAmount +
                ", invoiceDate=" + invoiceDate +
                '}';
    }
}