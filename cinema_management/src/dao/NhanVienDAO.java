package dao;

import entity.Employee;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    /**
     * Lấy nhân viên theo ID
     */
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM nhan_vien WHERE id_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy nhân viên theo email
     */
    public Employee getEmployeeByEmail(String email) {
        String sql = "SELECT * FROM nhan_vien WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả nhân viên
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien ORDER BY ten_nhan_vien";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Thêm nhân viên mới
     */
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO nhan_vien (ten_nhan_vien, email, so_dien_thoai, chuc_vu, luong, ngay_vao_lam, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFullName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPhone());
            pstmt.setString(4, employee.getPosition());
            pstmt.setDouble(5, employee.getSalary());
            pstmt.setDate(6, Date.valueOf(employee.getHireDate()));
            pstmt.setString(7, employee.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật nhân viên
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE nhan_vien SET ten_nhan_vien = ?, email = ?, so_dien_thoai = ?, "
                   + "chuc_vu = ?, luong = ?, trang_thai = ? WHERE id_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFullName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPhone());
            pstmt.setString(4, employee.getPosition());
            pstmt.setDouble(5, employee.getSalary());
            pstmt.setString(6, employee.getStatus());
            pstmt.setInt(7, employee.getEmployeeId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa nhân viên
     */
    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM nhan_vien WHERE id_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Employee object
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("id_nhan_vien"));
        employee.setFullName(rs.getString("ten_nhan_vien"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("so_dien_thoai"));
        employee.setPosition(rs.getString("chuc_vu"));
        employee.setSalary(rs.getDouble("luong"));
        employee.setHireDate(rs.getDate("ngay_vao_lam").toLocalDate());
        employee.setStatus(rs.getString("trang_thai"));
        return employee;
    }
}
