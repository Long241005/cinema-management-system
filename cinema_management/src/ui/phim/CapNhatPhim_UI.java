package ui.phim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CapNhatPhim_UI extends JPanel {
    private JTextField txtMaPhim, txtTenPhim, txtTheLoai, txtDaoDien, txtThoiLuong;
    private JTextArea tarMoTa;
    
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;

    public CapNhatPhim_UI() {
        setBackground(MAU_NEN);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 50, 20, 50));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("CẬP NHẬT THÔNG TIN PHIM", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(MAU_CHU);
        add(lblTieuDe, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(MAU_NEN);

        txtMaPhim = taoField(pnlCenter, "Mã phim (Không được sửa):");
        txtMaPhim.setEditable(false);
        txtTenPhim = taoField(pnlCenter, "Tên phim:");
        txtTheLoai = taoField(pnlCenter, "Thể loại:");
        txtDaoDien = taoField(pnlCenter, "Đạo diễn:");
        txtThoiLuong = taoField(pnlCenter, "Thời lượng (phút):");

        add(pnlCenter, BorderLayout.CENTER);

        // Nút bấm
        JButton btnLuu = new JButton("Lưu thay đổi");
        btnLuu.setBackground(new Color(76, 175, 80));
        btnLuu.setForeground(MAU_CHU);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setPreferredSize(new Dimension(150, 45));
        
        JPanel pnlSouth = new JPanel();
        pnlSouth.setBackground(MAU_NEN);
        pnlSouth.add(btnLuu);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    private JTextField taoField(JPanel pnl, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txt = new JTextField();
        txt.setBackground(MAU_INPUT);
        txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5));
        pnl.add(txt);
        return txt;
    }

    // HÀM QUAN TRỌNG ĐỂ TRANG CHỦ GỌI
    public void chonPhimDeCapNhat(String maPhim) {
        txtMaPhim.setText(maPhim);
        // Sau này dùng DAO để lấy thông tin chi tiết và điền vào các ô còn lại
        // Phim p = phimDAO.timTheoMa(maPhim);
        // txtTenPhim.setText(p.getTenPhim());
    }
}