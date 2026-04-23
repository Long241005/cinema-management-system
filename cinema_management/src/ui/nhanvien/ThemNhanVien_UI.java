package ui.nhanvien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThemNhanVien_UI extends JPanel {
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;

    public ThemNhanVien_UI() {
        setLayout(new BorderLayout());
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(30, 100, 30, 100));

        JLabel lblTieuDe = new JLabel("THÊM NHÂN VIÊN MỚI", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(MAU_CHU);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);

        taoField(pnlForm, "Mã nhân viên:");
        taoField(pnlForm, "Tên nhân viên:");
        taoField(pnlForm, "Số điện thoại:");
        taoField(pnlForm, "Địa chỉ:");

        add(pnlForm, BorderLayout.CENTER);

        JButton btnThem = new JButton("Xác nhận thêm");
        btnThem.setBackground(new Color(76, 175, 80));
        btnThem.setForeground(MAU_CHU);
        btnThem.setPreferredSize(new Dimension(150, 40));
        JPanel pnlBtn = new JPanel(); pnlBtn.setBackground(MAU_NEN); pnlBtn.add(btnThem);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private void taoField(JPanel pnl, String label) {
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
    }
}