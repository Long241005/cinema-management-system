package ui.phongchieu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemPhong_UI extends JDialog {

    // ========== COMPONENTS (Khớp với bảng PhongChieu trong SQL) ==========
    private JTextField txtMaPhong;
    private JTextField txtTenPhong;
    private JTextField txtSoGhe;
    private JComboBox<String> cmbLoaiPhong; // 2D, 3D, IMAX

    // ========== MÀU SẮC & FONT (Đồng bộ với các trang khác của bạn) ==========
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_NEN_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU_TRANG = Color.WHITE;
    private final Color MAU_NUT_THEM = new Color(76, 175, 80);
    private final Color MAU_NUT_THEM_HOVER = new Color(39, 174, 96);
    private final Color MAU_NUT_HUY = new Color(231, 76, 60);
    private final Color MAU_NUT_HUY_HOVER = new Color(192, 57, 43);

    public ThemPhong_UI(JFrame parent) {
        super(parent, "Thêm phòng chiếu mới", true);
        
        khoiTaoGiaoDien();
        
        // Giả lập mã phòng tự động
        txtMaPhong.setText("PC" + System.currentTimeMillis() % 1000);
        
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void khoiTaoGiaoDien() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(MAU_NEN);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("THÊM PHÒNG CHIẾU", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(MAU_CHU_TRANG);
        mainPanel.add(lblTieuDe, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(MAU_NEN);

        txtMaPhong = taoField(formPanel, "Mã phòng:");
        txtMaPhong.setEditable(false); // Không cho sửa mã
        
        txtTenPhong = taoField(formPanel, "Tên phòng:");
        txtSoGhe = taoField(formPanel, "Số lượng ghế:");
        
        // ComboBox loại phòng (2D, 3D, IMAX theo SQL)
        cmbLoaiPhong = taoComboBox(formPanel, "Loại phòng:", new String[]{"2D", "3D", "IMAX"});

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

    private JComboBox<String> taoComboBox(JPanel panel, String labelText, String[] items) {
        JLabel label = new JLabel(labelText);
        label.setForeground(MAU_CHU_TRANG);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(MAU_NEN_INPUT);
        comboBox.setForeground(MAU_CHU_TRANG);
        comboBox.setPreferredSize(new Dimension(350, 35));
        comboBox.setMaximumSize(new Dimension(350, 35));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboBox);
        panel.add(Box.createVerticalStrut(15));
        return comboBox;
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
        String ten = txtTenPhong.getText().trim();
        String soGheStr = txtSoGhe.getText().trim();

        if (ten.isEmpty() || soGheStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            int soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) throw new NumberFormatException();
            
            // Tạm thời chỉ thông báo vì chưa dùng DAO
            JOptionPane.showMessageDialog(this, "Đã thêm phòng: " + ten + " (" + soGhe + " ghế)");
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng ghế phải là số nguyên dương!");
        }
    }
}