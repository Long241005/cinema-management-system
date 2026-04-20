package dao;

import entity.Customer;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    /**
     * Lấy khách hàng theo ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM khach_hang WHERE id_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy khách hàng theo email
     */
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM khach_hang WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả khách hàng
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY id_khach_hang";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Thêm khách hàng mới
     */
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO khach_hang (ten_khach_hang, email, so_dien_thoai, cap_thanh_vien, diem_trung_thanh, ngay_dang_ky, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getMembershipLevel());
            pstmt.setInt(5, customer.getLoyaltyPoints());
            pstmt.setDate(6, Date.valueOf(customer.getRegistrationDate()));
            pstmt.setString(7, customer.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật khách hàng
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE khach_hang SET ten_khach_hang = ?, email = ?, so_dien_thoai = ?, "
                   + "cap_thanh_vien = ?, diem_trung_thanh = ?, trang_thai = ? WHERE id_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getMembershipLevel());
            pstmt.setInt(5, customer.getLoyaltyPoints());
            pstmt.setString(6, customer.getStatus());
            pstmt.setInt(7, customer.getCustomerId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa khách hàng
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM khach_hang WHERE id_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("id_khach_hang"));
        customer.setFullName(rs.getString("ten_khach_hang"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("so_dien_thoai"));
        customer.setMembershipLevel(rs.getString("cap_thanh_vien"));
        customer.setLoyaltyPoints(rs.getInt("diem_trung_thanh"));
        customer.setRegistrationDate(rs.getDate("ngay_dang_ky").toLocalDate());
        customer.setStatus(rs.getString("trang_thai"));
        return customer;
    }
}
