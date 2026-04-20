package entity;

import java.io.Serializable;

/**
 * Lớp đại diện cho Phòng Chiếu
 */
public class PhongChieu implements Serializable {
    private int maPhongChieu;
    private String tenPhongChieu;
    private int tongSoGhe;
    private String viTri;
    private String dinhDang; // 2D, 3D, IMAX, v.v.
    private String trangThai; // ACTIVE, MAINTENANCE, CLOSED

    public PhongChieu() {}

    public PhongChieu(int maPhongChieu, String tenPhongChieu, int tongSoGhe, 
                   String viTri, String dinhDang, String trangThai) {
        this.maPhongChieu = maPhongChieu;
        this.tenPhongChieu = tenPhongChieu;
        this.tongSoGhe = tongSoGhe;
        this.viTri = viTri;
        this.dinhDang = dinhDang;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaPhongChieu() { return maPhongChieu; }
    public void setMaPhongChieu(int maPhongChieu) { this.maPhongChieu = maPhongChieu; }

    public String getTenPhongChieu() { return tenPhongChieu; }
    public void setTenPhongChieu(String tenPhongChieu) { this.tenPhongChieu = tenPhongChieu; }

    public int getTongSoGhe() { return tongSoGhe; }
    public void setTongSoGhe(int tongSoGhe) { this.tongSoGhe = tongSoGhe; }

    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }

    public String getDinhDang() { return dinhDang; }
    public void setDinhDang(String dinhDang) { this.dinhDang = dinhDang; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return tenPhongChieu + " (" + dinhDang + ")";
    }
}
