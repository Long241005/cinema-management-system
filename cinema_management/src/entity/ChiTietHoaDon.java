package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ChiTietHoaDon implements Serializable {
	private HoaDon hoaDon; // Thành phần của khóa chính
	private Ve ve; // Thành phần của khóa chính
	private int soLuong;
	private BigDecimal thanhTien;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(HoaDon hoaDon, Ve ve, int soLuong, BigDecimal thanhTien) {
		this.hoaDon = hoaDon;
		this.ve = ve;
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
	}

	// Getters và Setters
	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public Ve getVe() {
		return ve;
	}

	public void setVe(Ve ve) {
		this.ve = ve;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public BigDecimal getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(BigDecimal thanhTien) {
		this.thanhTien = thanhTien;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChiTietHoaDon that = (ChiTietHoaDon) o;
		return Objects.equals(hoaDon, that.hoaDon) && Objects.equals(ve, that.ve);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hoaDon, ve);
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon{" + "maHoaDon=" + (hoaDon != null ? hoaDon.getMaHoaDon() : "null") + ", maVe="
				+ (ve != null ? ve.getMaVe() : "null") + ", soLuong=" + soLuong + ", thanhTien=" + thanhTien + '}';
	}
}