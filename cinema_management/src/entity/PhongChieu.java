package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Phòng Chiếu
 */
public class PhongChieu implements Serializable {
    private String maPhong;
    private String tenPhong;
    private int soGhe;
    private String loaiPhong; // 2D, 3D, IMAX

    public PhongChieu() {}

    public PhongChieu(String maPhong, String tenPhong, int soGhe, String loaiPhong) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soGhe = soGhe;
        this.loaiPhong = loaiPhong;
    }

    // Getters and Setters
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    @Override
    public String toString() {
        return tenPhong + " (" + loaiPhong + ")";
    }
}
