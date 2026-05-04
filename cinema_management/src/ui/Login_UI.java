package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import entity.NhanVien;
import services.AuthService;

public class Login_UI extends JFrame {

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_XANH = new Color(33, 150, 243);

    private JTextField txtMa;
    private JPasswordField txtPass;

    public Login_UI() {

        setTitle("Đăng nhập hệ thống");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAU_NEN);

        // ===== TITLE =====
        JLabel title = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(MAU_CHU);
        title.setBorder(new EmptyBorder(15, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        // ===== PANEL FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtMa = createInput();
        txtPass = new JPasswordField();
        styleInput(txtPass);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(createLabel("Mã nhân viên"), gbc);

        gbc.gridy = 1;
        form.add(txtMa, gbc);

        // Row 2
        gbc.gridy = 2;
        form.add(createLabel("Mật khẩu"), gbc);

        gbc.gridy = 3;
        form.add(txtPass, gbc);

        add(form, BorderLayout.CENTER);

        // ===== BUTTON =====
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(MAU_XANH);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(0, 40));

        JPanel pnlBtn = new JPanel(new BorderLayout());
        pnlBtn.setOpaque(false);
        pnlBtn.setBorder(new EmptyBorder(10, 40, 20, 40));
        pnlBtn.add(btnLogin, BorderLayout.CENTER);

        add(pnlBtn, BorderLayout.SOUTH);

        // ===== EVENT =====
        btnLogin.addActionListener(e -> xuLyLogin());
    }

    // ===== LOGIN LOGIC =====
    private void xuLyLogin() {
        String ma = txtMa.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (ma.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đầy đủ thông tin!");
            return;
        }

        AuthService auth = new AuthService();

        if (auth.login(ma, pass)) {

            NhanVien nv = AuthService.getUser();

            JOptionPane.showMessageDialog(this,
                    "Xin chào: " + nv.getTenNV()
                            + "\nChức vụ: " + nv.getChucVu());

            new TrangChu_UI().setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
        }
    }

    // ===== UI COMPONENT =====
    private JTextField createInput() {
        JTextField txt = new JTextField();
        styleInput(txt);
        return txt;
    }

    private void styleInput(JTextField txt) {
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(MAU_INPUT);
        txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(70, 72, 87)),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(MAU_CHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login_UI().setVisible(true));
    }
}