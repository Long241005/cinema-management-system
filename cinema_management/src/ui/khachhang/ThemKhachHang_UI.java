package ui.khachhang;

import entity.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemKhachHang_UI extends JDialog {

    // ========== COMPONENTS ==========
    private JTextField txtMaKH;
    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail; // Thay đổi từ cmbGioiTinh sang txtEmail cho khớp SQL

    private Runnable onCustomerAdded;
    private String soDienThoaiBanDau;

    // ========== MÀU SẮC & FONT (Giữ nguyên style của bạn) ==========
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_NEN_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU_TRANG = Color.WHITE;
    private final Color MAU_NUT_THEM = new Color(76, 175, 80);
    private final Color MAU_NUT_THEM_HOVER = new Color(39, 174, 96);
    private final Color MAU_NUT_HUY = new Color(231, 76, 60);
    private final Color MAU_NUT_HUY_HOVER = new Color(192, 57, 43);

    public ThemKhachHang_UI(Frame parent, String soDienThoai, Runnable onCustomerAdded) {
        super(parent, "Thêm khách hàng mới", true);
        this.onCustomerAdded = onCustomerAdded;
        this.soDienThoaiBanDau = soDienThoai;
        
        khoiTaoGiaoDien();
        
        // Điền dữ liệu giả định/ban đầu
        txtMaKH.setText("KH" + System.currentTimeMillis() % 1000000); // Tạo mã tạm cho đẹp UI
        if (this.soDienThoaiBanDau != null) {
            txtSoDienThoai.setText(this.soDienThoaiBanDau);
        }
        
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void khoiTaoGiaoDien() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(MAU_NEN);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("THÊM KHÁCH HÀNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(MAU_CHU_TRANG);
        mainPanel.add(lblTieuDe, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(MAU_NEN);

        txtMaKH = taoField(formPanel, "Mã khách hàng:");
        txtMaKH.setEditable(false);
        txtHoTen = taoField(formPanel, "Tên khách hàng:");
        txtSoDienThoai = taoField(formPanel, "Số điện thoại:");
        txtEmail = taoField(formPanel, "Email:");

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Nút bấm
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(MAU_NEN);

        JButton btnThem = taoNut("Thêm", MAU_NUT_THEM, MAU_NUT_THEM_HOVER);
        JButton btnHuy = taoNut("Hủy", MAU_NUT_HUY, MAU_NUT_HUY_HOVER);

        btnThem.addActionListener(e -> xuLyThem());
        btnHuy.addActionListener(e -> dispose());

        btnPanel.add(btnThem);
        btnPanel.add(btnHuy);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JTextField taoField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setForeground(MAU_CHU_TRANG);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField textField = new JTextField();
        textField.setBackground(MAU_NEN_INPUT);
        textField.setForeground(MAU_CHU_TRANG);
        textField.setCaretColor(MAU_CHU_TRANG);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 72, 87)),
                new EmptyBorder(5, 10, 5, 10)));
        textField.setPreferredSize(new Dimension(350, 35));
        textField.setMaximumSize(new Dimension(350, 35));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(15));
        return textField;
    }

    private JButton taoNut(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(MAU_CHU_TRANG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void xuLyThem() {
        // Tạm thời chỉ thông báo và đóng Dialog vì chưa có DAO
        String ten = txtHoTen.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Đã ghi nhận thông tin khách hàng: " + ten);
        
        if (onCustomerAdded != null) {
            onCustomerAdded.run();
        }
        dispose();
    }
}