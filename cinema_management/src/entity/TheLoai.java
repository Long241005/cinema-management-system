package entity;

import java.util.Objects;

public class TheLoai {
private String maTheLoai;
private String tenTheLoai;
public String getMaTheLoai() {
	return maTheLoai;
}
public void setMaTheLoai(String maTheLoai) {
	this.maTheLoai = maTheLoai;
}
public String getTenTheLoai() {
	return tenTheLoai;
}
public void setTenTheLoai(String tenTheLoai) {
	this.tenTheLoai = tenTheLoai;
}
public TheLoai(String maTheLoai, String tenTheLoai) {
	super();
	this.maTheLoai = maTheLoai;
	this.tenTheLoai = tenTheLoai;
}
public TheLoai() {
	super();
}
@Override
public String toString() {
	return tenTheLoai;
}
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TheLoai other = (TheLoai) obj;
    return Objects.equals(tenTheLoai, other.tenTheLoai); // So sánh theo tên để khớp với thể loại mới thêm
}

@Override
public int hashCode() {
    return Objects.hash(tenTheLoai);
}
}
