package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import services.AuthService;

public class RegisterPanel extends JPanel {
    
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel errorLabel;
    private AuthService authService;
    private MainFrame parentFrame;
    
    public RegisterPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authService = new AuthService();
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createRegisterForm(), BorderLayout.CENTER);
    }
    
    private JPanel createRegisterForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(31, 32, 44));
        formPanel.setBorder(new EmptyBorder(40, 100, 40, 100));
        
        // Title
        JLabel titleLabel = new JLabel("Đăng Ký Tài Khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Full Name
        JLabel nameLabel = new JLabel("Họ và tên:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        formPanel.add(nameLabel);
        
        fullNameField = new JTextField();
        fullNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fullNameField.setMaximumSize(new Dimension(400, 40));
        fullNameField.setBackground(new Color(50, 50, 60));
        fullNameField.setForeground(Color.WHITE);
        fullNameField.setCaretColor(Color.WHITE);
        formPanel.add(fullNameField);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(Color.WHITE);
        formPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setMaximumSize(new Dimension(400, 40));
        emailField.setBackground(new Color(50, 50, 60));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Phone
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneLabel.setForeground(Color.WHITE);
        formPanel.add(phoneLabel);
        
        phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneField.setMaximumSize(new Dimension(400, 40));
        phoneField.setBackground(new Color(50, 50, 60));
        phoneField.setForeground(Color.WHITE);
        phoneField.setCaretColor(Color.WHITE);
        formPanel.add(phoneField);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Password
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(400, 40));
        passwordField.setBackground(new Color(50, 50, 60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Confirm Password
        JLabel confirmLabel = new JLabel("Xác nhận mật khẩu:");
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmLabel.setForeground(Color.WHITE);
        formPanel.add(confirmLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setMaximumSize(new Dimension(400, 40));
        confirmPasswordField.setBackground(new Color(50, 50, 60));
        confirmPasswordField.setForeground(Color.WHITE);
        confirmPasswordField.setCaretColor(Color.WHITE);
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(241, 121, 104));
        formPanel.add(errorLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(31, 32, 44));
        
        registerButton = new JButton("Đăng Ký");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(241, 121, 104));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(150, 45));
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(this::handleRegister);
        
        backButton = new JButton("Quay Lại");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 100, 120));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 45));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> parentFrame.showLoginPanel());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);
        
        formPanel.add(Box.createVerticalGlue());
        return formPanel;
    }
    
    private void handleRegister(ActionEvent e) {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Vui lòng điền đầy đủ tất cả thông tin");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Mật khẩu xác nhận không khớp");
            return;
        }
        
        if (password.length() < 6) {
            errorLabel.setText("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorLabel.setText("Email không hợp lệ");
            return;
        }
        
        if (authService.register(fullName, email, phone, password)) {
            errorLabel.setText("");
            clearFields();
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.showLoginPanel();
        } else {
            errorLabel.setText("Email đã được đăng ký hoặc lỗi hệ thống");
        }
    }
    
    private void clearFields() {
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
