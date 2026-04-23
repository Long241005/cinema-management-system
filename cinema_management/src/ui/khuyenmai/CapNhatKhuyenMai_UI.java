package ui.khuyenmai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CapNhatKhuyenMai_UI extends JPanel {
    private JTextField txtMaKM, txtTenKM, txtPhanTram, txtNgayBD, txtNgayKT;
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);

    public CapNhatKhuyenMai_UI() {
        setLayout(new BorderLayout());
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(30, 80, 30, 80));

        JLabel lblTieuDe = new JLabel("CẬP NHẬT CHƯƠNG TRÌNH KHUYẾN MÃI", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(Color.WHITE);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);

        txtMaKM = taoField(pnlForm, "Mã khuyến mãi (Không được sửa):");
        txtMaKM.setEditable(false);
        txtTenKM = taoField(pnlForm, "Tên chương trình:");
        txtPhanTram = taoField(pnlForm, "Phần trăm giảm (%):");
        txtNgayBD = taoField(pnlForm, "Ngày bắt đầu (yyyy-mm-dd):");
        txtNgayKT = taoField(pnlForm, "Ngày kết thúc (yyyy-mm-dd):");

        add(pnlForm, BorderLayout.CENTER);

        JButton btnLuu = new JButton("Lưu Cập Nhật");
        btnLuu.setBackground(new Color(33, 150, 243));
        btnLuu.setForeground(Color.WHITE);
        JPanel pnlBtn = new JPanel(); pnlBtn.setBackground(MAU_NEN); pnlBtn.add(btnLuu);
        add(pnlBtn, BorderLayout.SOUTH);
    }

    private JTextField taoField(JPanel pnl, String label) {
        JLabel lbl = new JLabel(label); lbl.setForeground(Color.WHITE);
        JTextField txt = new JTextField();
        txt.setBackground(MAU_INPUT); txt.setForeground(Color.WHITE);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnl.add(Box.createVerticalStrut(10)); pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5)); pnl.add(txt);
        return txt;
    }

    // Hàm để TrangChu_UI gọi khi nhấn sửa
    public void chonKMDeCapNhat(String maKM) {
        txtMaKM.setText(maKM);
    }
}