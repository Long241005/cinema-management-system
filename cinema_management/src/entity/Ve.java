package entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ve implements Serializable {
	private String maVe;
	private SuatChieu suatChieu; // maSC
	private Ghe ghe; // maGhe
	private BigDecimal giaVe;
	private String trangThai; // Đã bán, Chờ thanh toán...

	public Ve() {
	}

	public Ve(String maVe, SuatChieu suatChieu, Ghe ghe, BigDecimal giaVe, String trangThai) {
		this.maVe = maVe;
		this.suatChieu = suatChieu;
		this.ghe = ghe;
		this.giaVe = giaVe;
		this.trangThai = trangThai;
	}

	public String getMaVe() {
		return maVe;
	}

	public void setMaVe(String maVe) {
		this.maVe = maVe;
	}

	public SuatChieu getSuatChieu() {
		return suatChieu;
	}

	public void setSuatChieu(SuatChieu suatChieu) {
		this.suatChieu = suatChieu;
	}

	public Ghe getGhe() {
		return ghe;
	}

	public void setGhe(Ghe ghe) {
		this.ghe = ghe;
	}

	public BigDecimal getGiaVe() {
		return giaVe;
	}

	public void setGiaVe(BigDecimal giaVe) {
		this.giaVe = giaVe;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "Ve [maVe=" + maVe + ", suatChieu=" + suatChieu + ", ghe=" + ghe + ", giaVe=" + giaVe + ", trangThai="
				+ trangThai + "]";
	}

}