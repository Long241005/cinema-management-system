package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Lớp đại diện cho Khuyến Mãi
 */
public class KhuyenMai implements Serializable {
    private String maKM;
    private String tenKhuyenMai;
    private BigDecimal phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    public KhuyenMai() {}

    public KhuyenMai(String maKM, String tenKhuyenMai, BigDecimal phanTramGiam,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.maKM = maKM;
        this.tenKhuyenMai = tenKhuyenMai;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters and Setters
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public void setTenKhuyenMai(String tenKhuyenMai) { this.tenKhuyenMai = tenKhuyenMai; }

    public BigDecimal getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(BigDecimal phanTramGiam) { this.phanTramGiam = phanTramGiam; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    @Override
    public String toString() {
        return tenKhuyenMai;
    }
}
