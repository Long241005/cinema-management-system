package services;

import entity.KhachHang;
import java.util.HashMap;
import java.util.Map;
import entity.NhanVien;

/**
 * Dịch vụ xác thực (Authentication Service)
 * Quản lý đăng nhập, đăng ký, đăng xuất cho người dùng
 */
public class AuthService {
    
    private static NhanVien nhanVienHienTai = null;
    private static KhachHang khachHangHienTai = null;
    
    // Mô phỏng cơ sở dữ liệu người dùng (sẽ được thay thế bằng database)
    private static Map<String, String> taiKhoanNhanVien = new HashMap<>();
    private static Map<String, String> taiKhoanKhachHang = new HashMap<>();
    
    static {
        // Tài khoản nhân viên mặc định
        taiKhoanNhanVien.put("admin@cinema.com", "admin123");
        taiKhoanNhanVien.put("nv001@cinema.com", "nv123456");
        
        // Tài khoản khách hàng mặc định
        taiKhoanKhachHang.put("customer@example.com", "customer123");
        taiKhoanKhachHang.put("user123@example.com", "user123456");
    }
    
    /**
     * Đăng nhập nhân viên
     */
    public boolean dangNhapNhanVien(String email, String matKhau) {
        if (email == null || matKhau == null) {
            return false;
        }
        
        if (taiKhoanNhanVien.containsKey(email) && 
            taiKhoanNhanVien.get(email).equals(matKhau)) {
            // Tạo đối tượng nhân viên (thực tế sẽ lấy từ database)
            nhanVienHienTai = new NhanVien();
            nhanVienHienTai.setMaNV("NV001");
            nhanVienHienTai.setTenNV("Nhân Viên Test");
            nhanVienHienTai.setEmail(email);
            nhanVienHienTai.setChucVu("Quản Lý");
            return true;
        }
        return false;
    }
    
    /**
     * Đăng nhập khách hàng
     */
    public boolean dangNhapKhachHang(String email, String matKhau) {
        if (email == null || matKhau == null) {
            return false;
        }
        
        if (taiKhoanKhachHang.containsKey(email) && 
            taiKhoanKhachHang.get(email).equals(matKhau)) {
            // Tạo đối tượng khách hàng (thực tế sẽ lấy từ database)
            khachHangHienTai = new KhachHang();
            khachHangHienTai.setMaKH("KH001");
            khachHangHienTai.setTenKhachHang("Khách Hàng Test");
            khachHangHienTai.setEmail(email);
            return true;
        }
        return false;
    }
    
    /**
     * Đăng ký tài khoản khách hàng
     */
    public boolean dangKyKhachHang(String hoTen, String email, String sdt, String matKhau) {
        // Kiểm tra email đã tồn tại
        if (taiKhoanKhachHang.containsKey(email)) {
            return false;
        }
        
        // Kiểm tra mật khẩu
        if (matKhau == null || matKhau.length() < 6) {
            return false;
        }
        
        // Thêm tài khoản mới
        taiKhoanKhachHang.put(email, matKhau);
        
        // Tạo khách hàng mới
        khachHangHienTai = new KhachHang();
        khachHangHienTai.setMaKH("KH" + System.currentTimeMillis());
        khachHangHienTai.setTenKhachHang(hoTen);
        khachHangHienTai.setEmail(email);
        khachHangHienTai.setSDT(sdt);
        khachHangHienTai.setDiemTichLuy(0);
        
        return true;
    }
    
    /**
     * Lấy nhân viên hiện tại
     */
    public static NhanVien getNhanVienHienTai() {
        return nhanVienHienTai;
    }
    
    /**
     * Lấy khách hàng hiện tại
     */
    public static KhachHang getKhachHangHienTai() {
        return khachHangHienTai;
    }
    
    /**
     * Kiểm tra khách hàng đã đăng nhập
     */
    public static boolean isKhachHangDaDangNhap() {
        return khachHangHienTai != null;
    }
    
    /**
     * Kiểm tra nhân viên đã đăng nhập
     */
    public static boolean isNhanVienDaDangNhap() {
        return nhanVienHienTai != null;
    }
    
    /**
     * Đăng xuất
     */
    public static void dangXuat() {
        nhanVienHienTai = null;
        khachHangHienTai = null;
    }
    
    /**
     * Thay đổi mật khẩu
     */
    public boolean thayDoiMatKhau(String email, String matKhauCu, String matKhauMoi) {
        if (taiKhoanNhanVien.containsKey(email)) {
            if (taiKhoanNhanVien.get(email).equals(matKhauCu)) {
                taiKhoanNhanVien.put(email, matKhauMoi);
                return true;
            }
        } else if (taiKhoanKhachHang.containsKey(email)) {
            if (taiKhoanKhachHang.get(email).equals(matKhauCu)) {
                taiKhoanKhachHang.put(email, matKhauMoi);
                return true;
            }
        }
        return false;
    }
}
