package entity;
import java.io.Serializable;
import java.time.LocalDate;
public class KhachHang implements Serializable{
	 private int maKhachHang;
	    private String hoTen;
	    private String email;
	    private String soDienThoai;
	    private String mucThanhVien; // BRONZE, SILVER, GOLD, PLATINUM
	    private int diemTichLuy;
	    private LocalDate ngayDangKy;
	    private String trangThai; // ACTIVE, INACTIVE

	    public KhachHang() {}

	    public KhachHang(int maKhachHang, String hoTen, String email, String soDienThoai,
	                    String mucThanhVien, int diemTichLuy, LocalDate ngayDangKy, String trangThai) {
	        this.maKhachHang = maKhachHang;
	        this.hoTen = hoTen;
	        this.email = email;
	        this.soDienThoai = soDienThoai;
	        this.mucThanhVien = mucThanhVien;
	        this.diemTichLuy = diemTichLuy;
	        this.ngayDangKy = ngayDangKy;
	        this.trangThai = trangThai;
	    }

	    // Getters and Setters
	    public int getMaKhachHang() { return maKhachHang; }
	    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }

	    public String getHoTen() { return hoTen; }
	    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getSoDienThoai() { return soDienThoai; }
	    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

	    public String getMucThanhVien() { return mucThanhVien; }
	    public void setMucThanhVien(String mucThanhVien) { this.mucThanhVien = mucThanhVien; }

	    public int getDiemTichLuy() { return diemTichLuy; }
	    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

	    public LocalDate getNgayDangKy() { return ngayDangKy; }
	    public void setNgayDangKy(LocalDate ngayDangKy) { this.ngayDangKy = ngayDangKy; }

	    public String getTrangThai() { return trangThai; }
	    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

	    @Override
	    public String toString() {
	        return hoTen + " (" + mucThanhVien + ")";
	    }
}
