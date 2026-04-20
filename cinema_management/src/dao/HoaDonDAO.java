package dao;

import entity.Invoice;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    
    /**
     * Lấy hóa đơn theo ID
     */
    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT * FROM hoa_don WHERE id_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy hóa đơn của khách hàng
     */
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM hoa_don WHERE id_khach_hang = ? ORDER BY ngay_lap DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
    /**
     * Lấy tất cả hóa đơn
     */
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM hoa_don ORDER BY ngay_lap DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
    /**
     * Thêm hóa đơn mới
     */
    public int addInvoice(Invoice invoice) {
        String sql = "INSERT INTO hoa_don (id_khach_hang, tong_tien, tien_giam, tong_thanh_toan, phuong_thuc_thanh_toan, ngay_lap, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_hoa_don";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setDouble(2, invoice.getTotalAmount());
            pstmt.setDouble(3, invoice.getDiscount());
            pstmt.setDouble(4, invoice.getFinalAmount());
            pstmt.setString(5, invoice.getPaymentMethod());
            pstmt.setDate(6, Date.valueOf(invoice.getInvoiceDate()));
            pstmt.setString(7, invoice.getStatus());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_hoa_don");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Cập nhật hóa đơn
     */
    public boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE hoa_don SET id_khach_hang = ?, tong_tien = ?, tien_giam = ?, "
                   + "tong_thanh_toan = ?, phuong_thuc_thanh_toan = ?, trang_thai = ? WHERE id_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setDouble(2, invoice.getTotalAmount());
            pstmt.setDouble(3, invoice.getDiscount());
            pstmt.setDouble(4, invoice.getFinalAmount());
            pstmt.setString(5, invoice.getPaymentMethod());
            pstmt.setString(6, invoice.getStatus());
            pstmt.setInt(7, invoice.getInvoiceId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa hóa đơn
     */
    public boolean deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM hoa_don WHERE id_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy doanh thu theo ngày
     */
    public double getRevenueByDate(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(tong_thanh_toan), 0) as revenue FROM hoa_don WHERE DATE(ngay_lap) = ? AND trang_thai = 'PAID'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /**
     * Map ResultSet thành Invoice object
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("id_hoa_don"));
        invoice.setCustomerId(rs.getInt("id_khach_hang"));
        invoice.setTotalAmount(rs.getDouble("tong_tien"));
        invoice.setDiscount(rs.getDouble("tien_giam"));
        invoice.setFinalAmount(rs.getDouble("tong_thanh_toan"));
        invoice.setPaymentMethod(rs.getString("phuong_thuc_thanh_toan"));
        invoice.setInvoiceDate(rs.getDate("ngay_lap").toLocalDate());
        invoice.setStatus(rs.getString("trang_thai"));
        return invoice;
    }
}
