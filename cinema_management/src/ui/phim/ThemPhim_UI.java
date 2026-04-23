package ui.phim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThemPhim_UI extends JPanel {
    private JTextField txtMa, txtTen, txtDaoDien, txtTheLoai, txtThoiLuong;
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);

    public ThemPhim_UI() {
        setLayout(new BorderLayout());
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(30, 80, 30, 80));

        JLabel lblTieuDe = new JLabel("THÊM PHIM MỚI", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(Color.WHITE);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);

        txtMa = taoField(pnlForm, "Mã phim:");
        txtTen = taoField(pnlForm, "Tên phim:");
        txtTheLoai = taoField(pnlForm, "Thể loại:");
        txtDaoDien = taoField(pnlForm, "Đạo diễn:");
        txtThoiLuong = taoField(pnlForm, "Thời lượng (phút):");

        add(pnlForm, BorderLayout.CENTER);

        JButton btnThem = new JButton("Thêm Phim");
        btnThem.setBackground(new Color(76, 175, 80));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel pnlBtn = new JPanel(); pnlBtn.setBackground(MAU_NEN); pnlBtn.add(btnThem);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private JTextField taoField(JPanel pnl, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        JTextField txt = new JTextField();
        txt.setBackground(MAU_INPUT);
        txt.setForeground(Color.WHITE);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5));
        pnl.add(txt);
        return txt;
    }
}