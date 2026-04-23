package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SuatChieu implements Serializable {
	private String maSC;
	private PhongChieu phongChieu; // Thay String maPhong
	private Phim phim; // Thay String maPhim
	private LocalDate ngayChieu;
	private LocalDateTime gioChieu;

	public SuatChieu() {
	}

	public SuatChieu(String maSC, PhongChieu phongChieu, Phim phim, LocalDate ngayChieu, LocalDateTime gioChieu) {
		this.maSC = maSC;
		this.phongChieu = phongChieu;
		this.phim = phim;
		this.ngayChieu = ngayChieu;
		this.gioChieu = gioChieu;
	}

	public String getMaSC() {
		return maSC;
	}

	public void setMaSC(String maSC) {
		this.maSC = maSC;
	}

	public PhongChieu getPhongChieu() {
		return phongChieu;
	}

	public void setPhongChieu(PhongChieu phongChieu) {
		this.phongChieu = phongChieu;
	}

	public Phim getPhim() {
		return phim;
	}

	public void setPhim(Phim phim) {
		this.phim = phim;
	}

	public LocalDate getNgayChieu() {
		return ngayChieu;
	}

	public void setNgayChieu(LocalDate ngayChieu) {
		this.ngayChieu = ngayChieu;
	}

	public LocalDateTime getGioChieu() {
		return gioChieu;
	}

	public void setGioChieu(LocalDateTime gioChieu) {
		this.gioChieu = gioChieu;
	}

	@Override
	public String toString() {
		return "SuatChieu [maSC=" + maSC + ", phongChieu=" + phongChieu + ", phim=" + phim + ", ngayChieu=" + ngayChieu
				+ ", gioChieu=" + gioChieu + "]";
	}

}