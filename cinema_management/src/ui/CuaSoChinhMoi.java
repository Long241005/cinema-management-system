package ui;

import services.AuthService;
import entity.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrameNew extends JFrame {
    
    private JPanel mainContentPanel;
    private JPanel menuPanel;
    private JPanel headerPanel;
    private final List<JPanel> tatCaItemMenu = new ArrayList<>();
    private final List<JPanel> danhSachSubMenu = new ArrayList<>();
    
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private CustomerDashboardPanel customerPanel;
    private AdminDashboardPanel adminPanel;
    
    private static final Color MAU_NEN_MENU = new Color(31, 32, 44);
    private static final Color MAU_MENU_CHON = new Color(241, 121, 104);
    private static final Color MAU_CHU_TRANG = Color.WHITE;
    
    public MainFrameNew() {
        setTitle("Hệ thống Quản lý Rạp Chiếu Phim T3L");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Bắt đầu từ LoginPanel
        showLoginPanel();
    }
    
    /**
     * Hiển thị LoginPanel
     */
    public void showLoginPanel() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        loginPanel = new LoginPanel(this);
        add(loginPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    /**
     * Hiển thị RegisterPanel
     */
    public void showRegisterPanel() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        registerPanel = new RegisterPanel(this);
        add(registerPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    /**
     * Hiển thị Customer Dashboard
     */
    public void showCustomerDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        Customer currentUser = AuthService.getCurrentUser();
        if (currentUser != null) {
            headerPanel = createHeaderPanel(currentUser.getFullName(), "KHÁCH HÀNG");
            add(headerPanel, BorderLayout.NORTH);
            
            customerPanel = new CustomerDashboardPanel(this);
            add(customerPanel, BorderLayout.CENTER);
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Hiển thị Admin Dashboard
     */
    public void showAdminDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        Customer currentUser = AuthService.getCurrentUser();
        if (currentUser != null) {
            headerPanel = createHeaderPanel(currentUser.getFullName(), "QUẢN TRỊ VIÊN");
            add(headerPanel, BorderLayout.NORTH);
            
            adminPanel = new AdminDashboardPanel(this);
            add(adminPanel, BorderLayout.CENTER);
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Tạo Header Panel với thông tin người dùng
     */
    private JPanel createHeaderPanel(String userName, String role) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        // Logo/Title
        JLabel titleLabel = new JLabel("Rạp Chiếu Phim T3L");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(MAU_CHU_TRANG);
        
        // User Info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        userPanel.setBackground(new Color(31, 32, 44));
        
        JLabel userLabel = new JLabel("Xin chào: " + userName + " (" + role + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(Color.LIGHT_GRAY);
        
        JButton logoutButton = new JButton("Đăng Xuất");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(241, 121, 104));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> handleLogout());
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        AuthService authService = new AuthService();
        authService.logout();
        showLoginPanel();
    }
}
