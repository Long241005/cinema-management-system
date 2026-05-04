package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.NhanVien;

public class NhanVien_DAO {

    private Connection conn;

    public NhanVien_DAO() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=RapChieuPhim;encrypt=true;trustServerCertificate=true",
                    "sa",
                    "sapassword"

            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<NhanVien> docDanhSachNhanVien() {
        List<NhanVien> ds = new ArrayList<>();

        try {
            String sql = "SELECT * FROM NhanVien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("ngaySinh"),
                        rs.getString("SDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getString("chucVu")
                );
                ds.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public NhanVien dangNhap(String maNV, String matKhau) {

        String sql = "SELECT * FROM NhanVien WHERE maNV = ? AND matKhau = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, maNV);
            ps.setString(2, matKhau);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setChucVu(rs.getString("chucVu"));

                return nv;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<NhanVien> timKiemTheoMa(String ma) {
        List<NhanVien> ds = new ArrayList<>();

        try {
            String sql = "SELECT * FROM NhanVien WHERE maNV LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + ma + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ds.add(new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("ngaySinh"),
                        rs.getString("SDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getString("chucVu")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }


    public List<NhanVien> timKiemTheoSDT(String sdt) {
        List<NhanVien> ds = new ArrayList<>();

        try {
            String sql = "SELECT * FROM NhanVien WHERE SDT LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + sdt + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ds.add(new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("ngaySinh"),
                        rs.getString("SDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getString("chucVu")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public int demSoLuongNhanVien() {
        int count = 0;

        try {
            String sql = "SELECT COUNT(*) FROM NhanVien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
    public boolean themNhanVien(NhanVien nv) {
        boolean result = false;

        try {
            String sql = "INSERT INTO NhanVien "
                    + "(maNV, tenNV, gioiTinh, ngaySinh, SDT, email, diaChi, chucVu) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setBoolean(3, nv.isGioiTinh());
            ps.setString(4, nv.getNgaySinh());
            ps.setString(5, nv.getSDT());
            ps.setString(6, nv.getEmail());
            ps.setString(7, nv.getDiaChi());
            ps.setString(8, nv.getChucVu());

            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public boolean capNhatNhanVien(NhanVien nv) {
        boolean result = false;

        try {
            String sql = "UPDATE NhanVien SET "
                    + "tenNV = ?, "
                    + "gioiTinh = ?, "
                    + "SDT = ?, "
                    + "email = ?, "
                    + "diaChi = ?, "
                    + "chucVu = ? "
                    + "WHERE maNV = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nv.getTenNV());
            ps.setBoolean(2, nv.isGioiTinh());
            ps.setString(3, nv.getSDT());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getChucVu());
            ps.setString(7, nv.getMaNV());

            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public boolean xoaNhanVien(String maNV) {
        boolean result = false;

        try {
            String sql = "DELETE FROM NhanVien WHERE maNV = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);

            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
