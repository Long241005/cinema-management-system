package entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Lớp đại diện cho Khuyến Mãi
 */
public class KhuyenMai implements Serializable {
    private int maKhuyenMai;
    private String maKhuyenMaiCode;
    private String moTa;
    private double phanTramGiam;
    private double soTienGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private int soLanToiDa;
    private int soLanDaSuDung;
    private String trangThai; // ACTIVE, EXPIRED, INACTIVE

    public KhuyenMai() {}

    public KhuyenMai(int maKhuyenMai, String maKhuyenMaiCode, String moTa,
                     double phanTramGiam, double soTienGiam,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLanToiDa, 
                     int soLanDaSuDung, String trangThai) {
        this.maKhuyenMai = maKhuyenMai;
        this.maKhuyenMaiCode = maKhuyenMaiCode;
        this.moTa = moTa;
        this.phanTramGiam = phanTramGiam;
        this.soTienGiam = soTienGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soLanToiDa = soLanToiDa;
        this.soLanDaSuDung = soLanDaSuDung;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaKhuyenMai() { return maKhuyenMai; }
    public void setMaKhuyenMai(int maKhuyenMai) { this.maKhuyenMai = maKhuyenMai; }

    public String getMaKhuyenMaiCode() { return maKhuyenMaiCode; }
    public void setMaKhuyenMaiCode(String maKhuyenMaiCode) { this.maKhuyenMaiCode = maKhuyenMaiCode; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double phanTramGiam) { this.phanTramGiam = phanTramGiam; }

    public double getSoTienGiam() { return soTienGiam; }
    public void setSoTienGiam(double soTienGiam) { this.soTienGiam = soTienGiam; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public int getSoLanToiDa() { return soLanToiDa; }
    public void setSoLanToiDa(int soLanToiDa) { this.soLanToiDa = soLanToiDa; }

    public int getSoLanDaSuDung() { return soLanDaSuDung; }
    public void setSoLanDaSuDung(int soLanDaSuDung) { this.soLanDaSuDung = soLanDaSuDung; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return maKhuyenMaiCode + " - " + moTa;
    }
}
