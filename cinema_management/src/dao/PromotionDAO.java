package dao;

import entity.Promotion;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    
    /**
     * Lấy khuyến mãi theo ID
     */
    public Promotion getPromotionById(int promotionId) {
        String sql = "SELECT * FROM khuyen_mai WHERE id_khuyen_mai = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promotionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPromotion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả khuyến mãi
     */
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM khuyen_mai ORDER BY ngay_bat_dau DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                promotions.add(mapResultSetToPromotion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }
    
    /**
     * Lấy khuyến mãi đang hoạt động
     */
    public List<Promotion> getActivePromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM khuyen_mai WHERE trang_thai = 'ACTIVE' AND ngay_bat_dau <= CURRENT_DATE AND ngay_ket_thuc >= CURRENT_DATE";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                promotions.add(mapResultSetToPromotion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }
    
    /**
     * Thêm khuyến mãi mới
     */
    public boolean addPromotion(Promotion promotion) {
        String sql = "INSERT INTO khuyen_mai (ten_khuyen_mai, mo_ta, phan_tram_giam, so_tien_giam, ngay_bat_dau, ngay_ket_thuc, so_luong_toi_da, da_su_dung, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, promotion.getPromotionName());
            pstmt.setString(2, promotion.getDescription());
            pstmt.setDouble(3, promotion.getDiscountPercent());
            pstmt.setDouble(4, promotion.getDiscountAmount());
            pstmt.setDate(5, Date.valueOf(promotion.getStartDate()));
            pstmt.setDate(6, Date.valueOf(promotion.getEndDate()));
            pstmt.setInt(7, promotion.getMaxQuantity());
            pstmt.setInt(8, promotion.getUsedQuantity());
            pstmt.setString(9, promotion.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật khuyến mãi
     */
    public boolean updatePromotion(Promotion promotion) {
        String sql = "UPDATE khuyen_mai SET ten_khuyen_mai = ?, mo_ta = ?, phan_tram_giam = ?, so_tien_giam = ?, "
                   + "ngay_bat_dau = ?, ngay_ket_thuc = ?, so_luong_toi_da = ?, da_su_dung = ?, trang_thai = ? WHERE id_khuyen_mai = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, promotion.getPromotionName());
            pstmt.setString(2, promotion.getDescription());
            pstmt.setDouble(3, promotion.getDiscountPercent());
            pstmt.setDouble(4, promotion.getDiscountAmount());
            pstmt.setDate(5, Date.valueOf(promotion.getStartDate()));
            pstmt.setDate(6, Date.valueOf(promotion.getEndDate()));
            pstmt.setInt(7, promotion.getMaxQuantity());
            pstmt.setInt(8, promotion.getUsedQuantity());
            pstmt.setString(9, promotion.getStatus());
            pstmt.setInt(10, promotion.getPromotionId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa khuyến mãi
     */
    public boolean deletePromotion(int promotionId) {
        String sql = "DELETE FROM khuyen_mai WHERE id_khuyen_mai = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promotionId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tăng số lần sử dụng khuyến mãi
     */
    public boolean incrementUsedQuantity(int promotionId) {
        String sql = "UPDATE khuyen_mai SET da_su_dung = da_su_dung + 1 WHERE id_khuyen_mai = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promotionId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Promotion object
     */
    private Promotion mapResultSetToPromotion(ResultSet rs) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(rs.getInt("id_khuyen_mai"));
        promotion.setPromotionName(rs.getString("ten_khuyen_mai"));
        promotion.setDescription(rs.getString("mo_ta"));
        promotion.setDiscountPercent(rs.getDouble("phan_tram_giam"));
        promotion.setDiscountAmount(rs.getDouble("so_tien_giam"));
        promotion.setStartDate(rs.getDate("ngay_bat_dau").toLocalDate());
        promotion.setEndDate(rs.getDate("ngay_ket_thuc").toLocalDate());
        promotion.setMaxQuantity(rs.getInt("so_luong_toi_da"));
        promotion.setUsedQuantity(rs.getInt("da_su_dung"));
        promotion.setStatus(rs.getString("trang_thai"));
        return promotion;
    }
}
