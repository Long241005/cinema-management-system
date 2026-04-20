package entity;
import java.io.Serializable;
public class Phim implements Serializable {
    private int maPhim;
    private String tenPhim;
    private String daoClac;
    private String theLoai;
    private int thoiLuong; // Thời lượng (phút)
    private String ngonNgu;
    private String moTa;
    private String urlPoster;
    private double danhGia;

    // Constructor mặc định
    public Phim() {}

    // Constructor đầy đủ
    public Phim(int maPhim, String tenPhim, String daoClac, String theLoai,
                int thoiLuong, String ngonNgu, String moTa, String urlPoster, double danhGia) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.daoClac = daoClac;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.ngonNgu = ngonNgu;
        this.moTa = moTa;
        this.urlPoster = urlPoster;
        this.danhGia = danhGia;
    }

    // Getters and Setters
    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public String getDaoClac() { return daoClac; }
    public void setDaoClac(String daoClac) { this.daoClac = daoClac; }

    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }

    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getNgonNgu() { return ngonNgu; }
    public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getUrlPoster() { return urlPoster; }
    public void setUrlPoster(String urlPoster) { this.urlPoster = urlPoster; }

    public double getDanhGia() { return danhGia; }
    public void setDanhGia(double danhGia) { this.danhGia = danhGia; }

    @Override
    public String toString() {
        return "Phim{" +
                "maPhim=" + maPhim +
                ", tenPhim='" + tenPhim + '\'' +
                ", daoClac='" + daoClac + '\'' +
                ", theLoai='" + theLoai + '\'' +
                ", thoiLuong=" + thoiLuong +
                ", ngonNgu='" + ngonNgu + '\'' +
                ", danhGia=" + danhGia +
                '}';
    }
}
