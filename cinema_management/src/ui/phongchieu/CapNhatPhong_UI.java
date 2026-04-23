package ui.phongchieu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CapNhatPhong_UI extends JPanel {
    private JTextField txtMaPhong, txtTenPhong, txtSoGhe;
    private JComboBox<String> cmbLoaiPhong;

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;

    public CapNhatPhong_UI() {
        setBackground(MAU_NEN);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel lblTieuDe = new JLabel("CẬP NHẬT PHÒNG CHIẾU", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(MAU_CHU);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);

        txtMaPhong = taoField(pnlForm, "Mã phòng:");
        txtMaPhong.setEditable(false);
        txtTenPhong = taoField(pnlForm, "Tên phòng:");
        txtSoGhe = taoField(pnlForm, "Số lượng ghế:");
        
        JLabel lblLoai = new JLabel("Loại phòng:");
        lblLoai.setForeground(MAU_CHU);
        cmbLoaiPhong = new JComboBox<>(new String[]{"2D", "3D", "IMAX"});
        cmbLoaiPhong.setBackground(MAU_INPUT);
        cmbLoaiPhong.setForeground(MAU_CHU);
        cmbLoaiPhong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        pnlForm.add(Box.createVerticalStrut(10));
        pnlForm.add(lblLoai);
        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(cmbLoaiPhong);

        add(pnlForm, BorderLayout.CENTER);

        JButton btnLuu = new JButton("Cập nhật ngay");
        btnLuu.setBackground(new Color(33, 150, 243)); // Màu xanh dương
        btnLuu.setForeground(MAU_CHU);
        btnLuu.setPreferredSize(new Dimension(150, 45));

        JPanel pnlSouth = new JPanel();
        pnlSouth.setBackground(MAU_NEN);
        pnlSouth.add(btnLuu);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    private JTextField taoField(JPanel pnl, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        JTextField txt = new JTextField();
        txt.setBackground(MAU_INPUT);
        txt.setForeground(MAU_CHU);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5));
        pnl.add(txt);
        return txt;
    }

    // HÀM QUAN TRỌNG ĐỂ TRANG CHỦ GỌI
    public void chonPhongDeCapNhat(String maPhong) {
        txtMaPhong.setText(maPhong);
        // Tương tự: Lấy dữ liệu từ DAO đổ vào đây
    }
}