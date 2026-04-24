package ui.khachhang;

import dao.KhachHang_DAO;
import entity.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThemKhachHang_UI extends JDialog {

    private JTextField txtMaKH, txtTenKH, txtSDT, txtEmail;
    private JComboBox<String> cmbGioiTinh;
    private JTable tableTraCuu;
    private Runnable onCustomerAdded;

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);

    public ThemKhachHang_UI(Frame parent, JTable table, Runnable callback) {
        super(parent, "Thêm khách hàng", true);
        this.tableTraCuu = table;
        this.onCustomerAdded = callback;

        khoiTaoGiaoDien();
        
        // CƠ CHẾ TỰ ĐỘNG LẤY MÃ AN TOÀN
        txtMaKH.setText(phatSinhMaTuDong());

        setSize(550, 550);
        setLocationRelativeTo(parent);
    }

    private void khoiTaoGiaoDien() {
        getContentPane().setBackground(MAU_NEN);
        setLayout(new BorderLayout());

        JLabel lblTieuDe = new JLabel("THÊM KHÁCH HÀNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setBorder(new EmptyBorder(25, 0, 20, 0));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);
        pnlForm.setBorder(new EmptyBorder(0, 50, 0, 50));

        txtMaKH = taoField(pnlForm, "Mã khách hàng:");
        txtMaKH.setEditable(false);
        txtMaKH.setFocusable(false);
        
        txtTenKH = taoField(pnlForm, "Họ tên:");
        txtSDT = taoField(pnlForm, "Số điện thoại:");
        txtEmail = taoField(pnlForm, "Email:");

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        pnlBot.setBackground(MAU_NEN);
        
        JButton btnThem = taoNut("Thêm", new Color(76, 175, 80));
        JButton btnHuy = taoNut("Hủy", new Color(231, 76, 60));

        btnThem.addActionListener(e -> xuLyThem());
        btnHuy.addActionListener(e -> dispose());

        pnlBot.add(btnThem); pnlBot.add(btnHuy);
        add(pnlBot, BorderLayout.SOUTH);
    }

    private void xuLyThem() {
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và SĐT!");
            return;
        }

        KhachHang kh = new KhachHang(txtMaKH.getText(), ten, sdt, email, 0);
        if (new KhachHang_DAO().themKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "thêm khách hàng thành công!");
            if (onCustomerAdded != null) onCustomerAdded.run();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu!");
        }
    }

    /**
     * Hàm phát sinh mã KH0000xx hoàn chỉnh nhất
     */
    private String phatSinhMaTuDong() {
        int max = 0;
        
        // Cách 1: Thử lấy từ JTable (nếu có)
        if (tableTraCuu != null && tableTraCuu.getRowCount() > 0) {
            for (int i = 0; i < tableTraCuu.getRowCount(); i++) {
                String ma = tableTraCuu.getValueAt(i, 0).toString();
                max = Math.max(max, Integer.parseInt(ma.substring(2)));
            }
        } 
        // Cách 2: Nếu Table null, soi thẳng vào Database (dùng hàm đã viết trong DAO)
        else {
            String maMaxDB = new KhachHang_DAO().getMaKHLonNhat();
            if (maMaxDB != null) {
                max = Integer.parseInt(maMaxDB.substring(2));
            }
        }
        
        return String.format("KH%06d", max + 1);
    }

    private JTextField taoField(JPanel p, String lb) {
        p.add(new JLabel(lb) {{ setForeground(Color.WHITE); }});
        JTextField t = new JTextField();
        t.setBackground(MAU_INPUT); t.setForeground(Color.WHITE);
        t.setCaretColor(Color.WHITE);
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.add(t); p.add(Box.createVerticalStrut(15));
        return t;
    }

    private JButton taoNut(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(150, 45));
        b.setFocusPainted(false); b.setBorderPainted(false);
        return b;
    }
}