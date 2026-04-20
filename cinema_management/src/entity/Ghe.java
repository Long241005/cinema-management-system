package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Ghế
 */
public class Ghe implements Serializable {
    private int maGhe;
    private int maPhongChieu;
    private String hangGhe;
    private int soGhe;
    private String loaiGhe; // STANDARD, VIP, COUPLE
    private double gia;
    private String trangThai; // AVAILABLE, BOOKED, BROKEN

    public Ghe() {}

    public Ghe(int maGhe, int maPhongChieu, String hangGhe, int soGhe,
                String loaiGhe, double gia, String trangThai) {
        this.maGhe = maGhe;
        this.maPhongChieu = maPhongChieu;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
        this.loaiGhe = loaiGhe;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaGhe() { return maGhe; }
    public void setMaGhe(int maGhe) { this.maGhe = maGhe; }

    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public String getHangGhe() { return hangGhe; }
    public void setHangGhe(String hangGhe) { this.hangGhe = hangGhe; }

    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }

    public String getLoaiGhe() { return loaiGhe; }
    public void setLoaiGhe(String loaiGhe) { this.loaiGhe = loaiGhe; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return hangGhe + soGhe + " (" + loaiGhe + ")";
    }
}
