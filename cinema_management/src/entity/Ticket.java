package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Vé (Ticket)
 */
public class Ticket implements Serializable {
    private int ticketId;
    private int showtimeId;
    private int customerId;
    private int seatId;
    private LocalDateTime purchaseDate;
    private double price;
    private String status; // VALID, USED, CANCELLED

    public Ticket() {}

    public Ticket(int ticketId, int showtimeId, int customerId, int seatId,
                  LocalDateTime purchaseDate, double price, String status) {
        this.ticketId = ticketId;
        this.showtimeId = showtimeId;
        this.customerId = customerId;
        this.seatId = seatId;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", showtimeId=" + showtimeId +
                ", price=" + price +
                '}';
    }
}