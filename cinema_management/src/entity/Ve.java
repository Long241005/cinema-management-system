package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Vé
 */
public class Ve implements Serializable {
    private int maVe;
    private int maSuatChieu;
    private int maKhachHang;
    private int maGhe;
    private LocalDateTime ngayMua;
    private double gia;
    private String trangThai; // VALID, USED, CANCELLED

    public Ve() {}

    public Ve(int maVe, int maSuatChieu, int maKhachHang, int maGhe,
                  LocalDateTime ngayMua, double gia, String trangThai) {
        this.maVe = maVe;
        this.maSuatChieu = maSuatChieu;
        this.maKhachHang = maKhachHang;
        this.maGhe = maGhe;
        this.ngayMua = ngayMua;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }

    public int getMaSuatChieu() { return maSuatChieu; }
    public void setMaSuatChieu(int maSuatChieu) { this.maSuatChieu = maSuatChieu; }

    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }

    public int getMaGhe() { return maGhe; }
    public void setMaGhe(int maGhe) { this.maGhe = maGhe; }

    public LocalDateTime getNgayMua() { return ngayMua; }
    public void setNgayMua(LocalDateTime ngayMua) { this.ngayMua = ngayMua; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "Ve{" +
                "maVe=" + maVe +
                ", maSuatChieu=" + maSuatChieu +
                ", gia=" + gia +
                '}';
    }
}
