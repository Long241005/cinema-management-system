package dao;

import entity.Film;
import connect.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {
    
    /**
     * Lấy phim theo ID
     */
    public Film getFilmById(int filmId) {
        String sql = "SELECT * FROM phim WHERE id_phim = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFilm(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả phim
     */
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM phim ORDER BY ten_phim";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                films.add(mapResultSetToFilm(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }
    
    /**
     * Lấy phim theo thể loại
     */
    public List<Film> getFilmsByGenre(String genre) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM phim WHERE the_loai = ? ORDER BY ten_phim";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                films.add(mapResultSetToFilm(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }
    
    /**
     * Thêm phim mới
     */
    public boolean addFilm(Film film) {
        String sql = "INSERT INTO phim (ten_phim, dao_dien, the_loai, thoi_luong, ngon_ngu, mo_ta, url_poster, danh_gia) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, film.getFilmName());
            pstmt.setString(2, film.getDirector());
            pstmt.setString(3, film.getGenre());
            pstmt.setInt(4, film.getDuration());
            pstmt.setString(5, film.getLanguage());
            pstmt.setString(6, film.getDescription());
            pstmt.setString(7, film.getPosterUrl());
            pstmt.setDouble(8, film.getRating());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật phim
     */
    public boolean updateFilm(Film film) {
        String sql = "UPDATE phim SET ten_phim = ?, dao_dien = ?, the_loai = ?, thoi_luong = ?, "
                   + "ngon_ngu = ?, mo_ta = ?, url_poster = ?, danh_gia = ? WHERE id_phim = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, film.getFilmName());
            pstmt.setString(2, film.getDirector());
            pstmt.setString(3, film.getGenre());
            pstmt.setInt(4, film.getDuration());
            pstmt.setString(5, film.getLanguage());
            pstmt.setString(6, film.getDescription());
            pstmt.setString(7, film.getPosterUrl());
            pstmt.setDouble(8, film.getRating());
            pstmt.setInt(9, film.getFilmId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa phim
     */
    public boolean deleteFilm(int filmId) {
        String sql = "DELETE FROM phim WHERE id_phim = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Film object
     */
    private Film mapResultSetToFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("id_phim"));
        film.setFilmName(rs.getString("ten_phim"));
        film.setDirector(rs.getString("dao_dien"));
        film.setGenre(rs.getString("the_loai"));
        film.setDuration(rs.getInt("thoi_luong"));
        film.setLanguage(rs.getString("ngon_ngu"));
        film.setDescription(rs.getString("mo_ta"));
        film.setPosterUrl(rs.getString("url_poster"));
        film.setRating(rs.getDouble("danh_gia"));
        return film;
    }
}
