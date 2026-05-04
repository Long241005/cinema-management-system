package services;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class AuthService {

    private static NhanVien user = null;
    private NhanVien_DAO dao = new NhanVien_DAO();

    // ===== LOGIN =====
    public boolean login(String maNV, String matKhau) {
        NhanVien nv = dao.dangNhap(maNV, matKhau);

        if (nv != null) {
            user = nv;
            return true;
        }
        return false;
    }

    // ===== GET USER =====
    public static NhanVien getUser() {
        return user;
    }

    // ===== CHECK =====
    public static boolean isLogin() {
        return user != null;
    }

    public static boolean isAdmin() {
        return user != null &&
                user.getChucVu().equalsIgnoreCase("Quản lý");
    }

    public static boolean isNhanVien() {
        return user != null &&
                user.getChucVu().equalsIgnoreCase("Nhân viên bán vé");
    }

    // ===== LOGOUT =====
    public static void logout() {
        user = null;
    }
}