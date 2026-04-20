package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Chi Tiết Hóa Đơn (InvoiceDetail)
 */
public class InvoiceDetail implements Serializable {
    private int detailId;
    private int invoiceId;
    private int ticketId;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public InvoiceDetail() {}

    public InvoiceDetail(int detailId, int invoiceId, int ticketId, String itemDescription,
                         int quantity, double unitPrice, double totalPrice) {
        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.ticketId = ticketId;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "detailId=" + detailId +
                ", itemDescription='" + itemDescription + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}