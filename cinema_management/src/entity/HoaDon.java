package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Hóa Đơn
 */
public class HoaDon implements Serializable {
    private int maHoaDon;
    private int maKhachHang;
    private int maNhanVien;
    private LocalDateTime ngayHoaDon;
    private double tongTienHang;
    private double soTienGiam;
    private double tongTien;
    private String hinhThucThanhToan; // CASH, CREDIT_CARD, DEBIT_CARD, ONLINE
    private String trangThai; // PAID, PENDING, CANCELLED

    public HoaDon() {}

    public HoaDon(int maHoaDon, int maKhachHang, int maNhanVien, LocalDateTime ngayHoaDon,
                   double tongTienHang, double soTienGiam, double tongTien,
                   String hinhThucThanhToan, String trangThai) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.maNhanVien = maNhanVien;
        this.ngayHoaDon = ngayHoaDon;
        this.tongTienHang = tongTienHang;
        this.soTienGiam = soTienGiam;
        this.tongTien = tongTien;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public LocalDateTime getNgayHoaDon() { return ngayHoaDon; }
    public void setNgayHoaDon(LocalDateTime ngayHoaDon) { this.ngayHoaDon = ngayHoaDon; }

    public double getTongTienHang() { return tongTienHang; }
    public void setTongTienHang(double tongTienHang) { this.tongTienHang = tongTienHang; }

    public double getSoTienGiam() { return soTienGiam; }
    public void setSoTienGiam(double soTienGiam) { this.soTienGiam = soTienGiam; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getHinhThucThanhToan() { return hinhThucThanhToan; }
    public void setHinhThucThanhToan(String hinhThucThanhToan) { this.hinhThucThanhToan = hinhThucThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon=" + maHoaDon +
                ", tongTien=" + tongTien +
                ", ngayHoaDon=" + ngayHoaDon +
                '}';
    }
}
