package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Ghế (Seat)
 */
public class Seat implements Serializable {
    private int seatId;
    private int theaterId;
    private String seatRow;
    private int seatNumber;
    private String seatType; // STANDARD, VIP, COUPLE
    private double price;
    private String status; // AVAILABLE, BOOKED, BROKEN

    public Seat() {}

    public Seat(int seatId, int theaterId, String seatRow, int seatNumber,
                String seatType, double price, String status) {
        this.seatId = seatId;
        this.theaterId = theaterId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }

    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return seatRow + seatNumber + " (" + seatType + ")";
    }
}