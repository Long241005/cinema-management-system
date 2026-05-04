package entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Lớp đại diện cho Phim
 */
public class Phim implements Serializable {
	private String maPhim;
	private String tenPhim;
	private String daoDien;
	private TheLoai theLoai;

	
	private int thoiLuong; // Phút
	private LocalDate ngayKhoiChieu;
	private String moTa;
	private String duongDanAnh;

	// Constructor mặc định
	public Phim() {
	}

	


	public String getMaPhim() {
		return maPhim;
	}




	



	public TheLoai getTheLoai() {
		return theLoai;
	}




	public void setTheLoai(TheLoai theLoai) {
		this.theLoai = theLoai;
	}




	public Phim(String maPhim, String tenPhim, String daoDien, TheLoai theLoai, int thoiLuong, LocalDate ngayKhoiChieu,
			String moTa, String duongDanAnh) {
		super();
		this.maPhim = maPhim;
		this.tenPhim = tenPhim;
		this.daoDien = daoDien;
		this.theLoai = theLoai;
		this.thoiLuong = thoiLuong;
		this.ngayKhoiChieu = ngayKhoiChieu;
		this.moTa = moTa;
		this.duongDanAnh = duongDanAnh;
	}

	public void setMaPhim(String maPhim) {
		this.maPhim = maPhim;
	}

	public String getTenPhim() {
		return tenPhim;
	}

	public void setTenPhim(String tenPhim) {
		this.tenPhim = tenPhim;
	}

	public String getDaoDien() {
		return daoDien;
	}

	public void setDaoDien(String daoDien) {
		this.daoDien = daoDien;
	}

	public int getThoiLuong() {
		return thoiLuong;
	}

	public void setThoiLuong(int thoiLuong) {
		this.thoiLuong = thoiLuong;
	}

	public LocalDate getNgayKhoiChieu() {
		return ngayKhoiChieu;
	}

	public void setNgayKhoiChieu(LocalDate ngayKhoiChieu) {
		this.ngayKhoiChieu = ngayKhoiChieu;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getDuongDanAnh() {
		return duongDanAnh;
	}

	public void setDuongDanAnh(String duongDanAnh) {
		this.duongDanAnh = duongDanAnh;
	}

	@Override
	public String toString() {
		return   tenPhim  ;
	}
}
