package entity;
import java.io.Serializable;
import java.time.LocalDate;
public class NhanVien implements Serializable{
	 private int maNhanVien;
	    private String hoTen;
	    private String email;
	    private String soDienThoai;
	    private String chucVu; // MANAGER, CASHIER, STAFF, CLEANER
	    private double luong;
	    private LocalDate ngayVaoLam;
	    private String trangThai; // ACTIVE, INACTIVE, ON_LEAVE

	    public NhanVien() {}

	    public NhanVien(int maNhanVien, String hoTen, String email, String soDienThoai,
	                    String chucVu, double luong, LocalDate ngayVaoLam, String trangThai) {
	        this.maNhanVien = maNhanVien;
	        this.hoTen = hoTen;
	        this.email = email;
	        this.soDienThoai = soDienThoai;
	        this.chucVu = chucVu;
	        this.luong = luong;
	        this.ngayVaoLam = ngayVaoLam;
	        this.trangThai = trangThai;
	    }

	    // Getters and Setters
	    public int getMaNhanVien() { return maNhanVien; }
	    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

	    public String getHoTen() { return hoTen; }
	    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getSoDienThoai() { return soDienThoai; }
	    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

	    public String getChucVu() { return chucVu; }
	    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

	    public double getLuong() { return luong; }
	    public void setLuong(double luong) { this.luong = luong; }

	    public LocalDate getNgayVaoLam() { return ngayVaoLam; }
	    public void setNgayVaoLam(LocalDate ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

	    public String getTrangThai() { return trangThai; }
	    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

	    @Override
	    public String toString() {
	        return hoTen + " (" + chucVu + ")";
	    }
}
