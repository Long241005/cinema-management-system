package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Suất Chiếu (Showtime)
 */
public class Showtime implements Serializable {
    private int showtimeId;
    private int filmId;
    private int theaterId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double ticketPrice;
    private int availableSeats;
    private String status; // SCHEDULED, ONGOING, COMPLETED, CANCELLED

    public Showtime() {}

    public Showtime(int showtimeId, int filmId, int theaterId, LocalDateTime startTime,
                    LocalDateTime endTime, double ticketPrice, int availableSeats, String status) {
        this.showtimeId = showtimeId;
        this.filmId = filmId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
        this.status = status;
    }

    // Getters and Setters
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public int getFilmId() { return filmId; }
    public void setFilmId(int filmId) { this.filmId = filmId; }

    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Showtime{" +
                "showtimeId=" + showtimeId +
                ", filmId=" + filmId +
                ", startTime=" + startTime +
                '}';
    }
}