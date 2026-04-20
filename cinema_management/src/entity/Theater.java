package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Phòng Chiếu (Theater)
 */
public class Theater implements Serializable {
    private int theaterId;
    private String theaterName;
    private int totalSeats;
    private String location;
    private String format; // 2D, 3D, IMAX, v.v.
    private String status; // ACTIVE, MAINTENANCE, CLOSED

    public Theater() {}

    public Theater(int theaterId, String theaterName, int totalSeats, 
                   String location, String format, String status) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.totalSeats = totalSeats;
        this.location = location;
        this.format = format;
        this.status = status;
    }

    // Getters and Setters
    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }

    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return theaterName + " (" + format + ")";
    }
}