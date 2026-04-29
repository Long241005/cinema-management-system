package entity;

import java.io.Serializable;

public class Ghe implements Serializable {
	private String maGhe;
	private PhongChieu phongChieu; // Thay String maPhong
	private String hang;
	private int soGhe;
	private String trangThai;

	public Ghe() {
	}

	public Ghe(String maGhe, PhongChieu phongChieu, String hang, int soGhe, String trangThai) {
		super();
		this.maGhe = maGhe;
		this.phongChieu = phongChieu;
		this.hang = hang;
		this.soGhe = soGhe;
		this.trangThai = trangThai;
	}

	public String getMaGhe() {
		return maGhe;
	}

	public void setMaGhe(String maGhe) {
		this.maGhe = maGhe;
	}

	public PhongChieu getPhongChieu() {
		return phongChieu;
	}

	public void setPhongChieu(PhongChieu phongChieu) {
		this.phongChieu = phongChieu;
	}

	public String getHang() {
		return hang;
	}

	public void setHang(String hang) {
		this.hang = hang;
	}

	public int getSoGhe() {
		return soGhe;
	}

	public void setSoGhe(int soGhe) {
		this.soGhe = soGhe;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "Ghe [maGhe=" + maGhe + ", phongChieu=" + phongChieu + ", hang=" + hang + ", soGhe=" + soGhe
				+ ", trangThai=" + trangThai + "]";
	}

}