package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Khách Hàng
 */
public class KhachHang implements Serializable {
    private String maKH;
    private String tenKhachHang;
    private String SDT;
    private String Email;
    private int diemTichLuy;

    public KhachHang() {}

    public KhachHang(String maKH, String tenKhachHang, String SDT, String Email, int diemTichLuy) {
        this.maKH = maKH;
        this.tenKhachHang = tenKhachHang;
        this.SDT = SDT;
        this.Email = Email;
        this.diemTichLuy = diemTichLuy;
    }

    // Getters and Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public String getEmail() { return Email; }
    public void setEmail(String Email) { this.Email = Email; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    @Override
    public String toString() {
        return tenKhachHang;
    }
}
