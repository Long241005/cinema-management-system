package dao;

import entity.Ticket;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    
    /**
     * Lấy vé theo ID
     */
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM ve WHERE id_ve = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTicket(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả vé của khách hàng
     */
    public List<Ticket> getTicketsByCustomerId(int customerId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ve WHERE id_khach_hang = ? ORDER BY ngay_dat DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Lấy vé theo suất chiếu
     */
    public List<Ticket> getTicketsByShowtimeId(int showtimeId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ve WHERE id_suat = ? ORDER BY ngay_dat DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtimeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Thêm vé mới
     */
    public boolean addTicket(Ticket ticket) {
        String sql = "INSERT INTO ve (id_khach_hang, id_suat, id_ghe, gia_ve, trang_thai, ngay_dat) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticket.getCustomerId());
            pstmt.setInt(2, ticket.getShowtimeId());
            pstmt.setInt(3, ticket.getSeatId());
            pstmt.setDouble(4, ticket.getPrice());
            pstmt.setString(5, ticket.getStatus());
            pstmt.setDate(6, Date.valueOf(ticket.getBookingDate()));
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật vé
     */
    public boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE ve SET id_khach_hang = ?, id_suat = ?, id_ghe = ?, gia_ve = ?, trang_thai = ? WHERE id_ve = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticket.getCustomerId());
            pstmt.setInt(2, ticket.getShowtimeId());
            pstmt.setInt(3, ticket.getSeatId());
            pstmt.setDouble(4, ticket.getPrice());
            pstmt.setString(5, ticket.getStatus());
            pstmt.setInt(6, ticket.getTicketId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa vé
     */
    public boolean deleteTicket(int ticketId) {
        String sql = "DELETE FROM ve WHERE id_ve = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy số vé đã bán cho suất chiếu
     */
    public int getTicketCountByShowtime(int showtimeId) {
        String sql = "SELECT COUNT(*) as total FROM ve WHERE id_suat = ? AND trang_thai = 'PAID'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtimeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Map ResultSet thành Ticket object
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getInt("id_ve"));
        ticket.setCustomerId(rs.getInt("id_khach_hang"));
        ticket.setShowtimeId(rs.getInt("id_suat"));
        ticket.setSeatId(rs.getInt("id_ghe"));
        ticket.setPrice(rs.getDouble("gia_ve"));
        ticket.setStatus(rs.getString("trang_thai"));
        ticket.setBookingDate(rs.getDate("ngay_dat").toLocalDate());
        return ticket;
    }
}
