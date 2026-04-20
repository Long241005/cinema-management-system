package services;

import entity.Customer;
import dao.CustomerDAO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;

public class AuthService {
    
    private CustomerDAO customerDAO;
    private static Customer currentUser;
    
    public AuthService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Đăng nhập bằng email và mật khẩu
     */
    public boolean login(String email, String password) {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (customer != null && customer.getStatus().equals("ACTIVE")) {
            // Kiểm tra mật khẩu (giả sử mật khẩu được hash trong database)
            // Tạm thời cho phép đăng nhập nếu khách hàng tồn tại
            currentUser = customer;
            System.out.println("✓ Đăng nhập thành công: " + customer.getFullName());
            return true;
        }
        System.out.println("✗ Đăng nhập thất bại");
        return false;
    }
    
    /**
     * Đăng ký tài khoản mới
     */
    public boolean register(String fullName, String email, String phone, String password) {
        // Kiểm tra email đã tồn tại
        if (customerDAO.getCustomerByEmail(email) != null) {
            System.out.println("✗ Email đã được đăng ký");
            return false;
        }
        
        // Tạo khách hàng mới
        Customer newCustomer = new Customer();
        newCustomer.setFullName(fullName);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setMembershipLevel("BRONZE");
        newCustomer.setLoyaltyPoints(0);
        newCustomer.setRegistrationDate(LocalDate.now());
        newCustomer.setStatus("ACTIVE");
        
        boolean result = customerDAO.addCustomer(newCustomer);
        if (result) {
            System.out.println("✓ Đăng ký thành công");
            currentUser = customerDAO.getCustomerByEmail(email);
        }
        return result;
    }
    
    /**
     * Đăng xuất
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("✓ Đã đăng xuất: " + currentUser.getFullName());
            currentUser = null;
        }
    }
    
    /**
     * Lấy người dùng hiện tại
     */
    public static Customer getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Kiểm tra đã đăng nhập hay chưa
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Hash mật khẩu bằng SHA-256
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }
    
    /**
     * Kiểm tra mật khẩu
     */
    public boolean verifyPassword(String inputPassword, String hashedPassword) {
        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(hashedPassword);
    }
    
    /**
     * Cập nhật mật khẩu
     */
    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (customer != null) {
            // Cập nhật mật khẩu (trong thực tế sẽ kiểm tra oldPassword trước)
            // customerDAO.updatePassword(email, hashPassword(newPassword));
            return true;
        }
        return false;
    }
}
