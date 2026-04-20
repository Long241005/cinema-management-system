package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Suất Chiếu
 */
public class SuatChieu implements Serializable {
    private int maSuatChieu;
    private int maPhim;
    private int maPhongChieu;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private double giaVe;
    private int soGheCon;
    private String trangThai; // SCHEDULED, ONGOING, COMPLETED, CANCELLED

    public SuatChieu() {}

    public SuatChieu(int maSuatChieu, int maPhim, int maPhongChieu, LocalDateTime thoiGianBatDau,
                    LocalDateTime thoiGianKetThuc, double giaVe, int soGheCon, String trangThai) {
        this.maSuatChieu = maSuatChieu;
        this.maPhim = maPhim;
        this.maPhongChieu = maPhongChieu;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.giaVe = giaVe;
        this.soGheCon = soGheCon;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaSuatChieu() { return maSuatChieu; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }

    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }

    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public LocalDateTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public LocalDateTime getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public double getGiaVe() { return giaVe; }
    public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

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
