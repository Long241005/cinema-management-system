package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDon implements Serializable {
	private String maHoaDon;
	private NhanVien nhanVien; // Thay String maNV
	private KhachHang khachHang; // Thay String maKH
	private KhuyenMai khuyenMai; // Thay String maKM
	private Thue thue; // Thay String maThue
	private LocalDate ngayLap;
	private BigDecimal tongTien;

	public HoaDon() {
	}

	public HoaDon(String maHoaDon, NhanVien nhanVien, KhachHang khachHang, KhuyenMai khuyenMai, Thue thue,
			LocalDate ngayLap, BigDecimal tongTien) {
		this.maHoaDon = maHoaDon;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.khuyenMai = khuyenMai;
		this.thue = thue;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
	}


	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public Thue getThue() {
		return thue;
	}

	public void setThue(Thue thue) {
		this.thue = thue;
	}

	public LocalDate getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", nhanVien=" + nhanVien + ", khachHang=" + khachHang + ", khuyenMai="
				+ khuyenMai + ", thue=" + thue + ", ngayLap=" + ngayLap + ", tongTien=" + tongTien + "]";
	}
	
}