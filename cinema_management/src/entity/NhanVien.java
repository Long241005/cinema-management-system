package entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Lớp đại diện cho Nhân Viên
 */
public class NhanVien implements Serializable {
    private String maNV;
    private String tenNV;
    private int gioiTinh; // 1: Nam, 0: Nữ
    private LocalDate ngaySinh;
    private String SDT;
    private String email;
    private String diaChi;
    private String chucVu;

    public NhanVien() {}

    public NhanVien(String maNV, String tenNV, int gioiTinh, LocalDate ngaySinh,
                    String SDT, String email, String diaChi, String chucVu) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.SDT = SDT;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
    }

    // Getters and Setters
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public int getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(int gioiTinh) { this.gioiTinh = gioiTinh; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    @Override
    public String toString() {
        return tenNV + " (" + chucVu + ")";
    }
}
