package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connect.DatabaseConnection;
import entity.KhuyenMai;

public class KhuyenMai_DAO {

    private Connection getConn() throws Exception {
        return DatabaseConnection.getInstance().getConnection();
    }

    private KhuyenMai map(ResultSet rs) throws SQLException {
        return new KhuyenMai(
                rs.getString("maKM"),
                rs.getString("tenKhuyenMai"),
                rs.getBigDecimal("phanTramGiam"),
                rs.getDate("ngayBatDau").toLocalDate(),
                rs.getDate("ngayKetThuc").toLocalDate()
        );
    }

    // ===== TỰ SINH MÃ =====


    // ===== THÊM =====
    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setBigDecimal(3, km.getPhanTramGiam());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== ĐỌC =====
    public List<KhuyenMai> docDanhSachKM() {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public List<KhuyenMai> timTheoMa(String ma) {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ?";

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + ma + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(map(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public List<KhuyenMai> timTheoTen(String ten) {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE tenKhuyenMai LIKE ?";

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + ten + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(map(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    // ===== AUTO MÃ KM =====
    public String taoMaTuDong() {
        String sql = "SELECT MAX(maKM) FROM KhuyenMai";

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ma = rs.getString(1);

                if (ma != null) {
                    int so = Integer.parseInt(ma.substring(2));
                    return String.format("KM%03d", so + 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "KM001";
    }
    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai=?, phanTramGiam=?, ngayBatDau=?, ngayKetThuc=? WHERE maKM=?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getTenKhuyenMai());
            ps.setBigDecimal(2, km.getPhanTramGiam());
            ps.setDate(3, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(4, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(5, km.getMaKM());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean xoaKhuyenMai(String maKM) {
        String sql = "DELETE FROM KhuyenMai WHERE maKM=?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKM);

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Không thể xóa vì đang được sử dụng!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<Object[]> thongKeKhuyenMai() {
        List<Object[]> ds = new ArrayList<>();

        String sql = """
        SELECT km.maKM, km.tenKhuyenMai,
               COUNT(hd.maKM) AS soLuot,
               SUM(hd.tienGiam) AS tongTien
        FROM HoaDon hd
        JOIN KhuyenMai km ON hd.maKM = km.maKM
        GROUP BY km.maKM, km.tenKhuyenMai
    """;

        try (Connection con = getConn();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getDouble(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public List<Object[]> thongKeTheoThang(LocalDate tu, LocalDate den){
        List<Object[]> ds = new ArrayList<>();

        String sql = """
        SELECT FORMAT(ngayLap,'MM-yyyy'), COUNT(*)
        FROM HoaDon
        WHERE maKM IS NOT NULL
          AND ngayLap BETWEEN ? AND ?
        GROUP BY FORMAT(ngayLap,'MM-yyyy')
        ORDER BY MIN(ngayLap)
    """;

        try(Connection con = getConn();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setDate(1, java.sql.Date.valueOf(tu));
            ps.setDate(2, java.sql.Date.valueOf(den));

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ds.add(new Object[]{
                        rs.getString(1),
                        rs.getInt(2)
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return ds;
    }
    public List<Object[]> thongKeTheoNgay(LocalDate tu, LocalDate den){
        List<Object[]> ds = new ArrayList<>();

        String sql = """
        SELECT CAST(ngayLap AS DATE), COUNT(*)
        FROM HoaDon
        WHERE maKM IS NOT NULL
          AND ngayLap BETWEEN ? AND ?
        GROUP BY CAST(ngayLap AS DATE)
        ORDER BY 1
    """;

        try(Connection con = getConn();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setDate(1, java.sql.Date.valueOf(tu));
            ps.setDate(2, java.sql.Date.valueOf(den));

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ds.add(new Object[]{
                        rs.getDate(1),
                        rs.getInt(2)
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return ds;
    }

}