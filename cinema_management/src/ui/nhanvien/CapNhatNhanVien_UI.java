package ui.nhanvien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CapNhatNhanVien_UI extends JPanel {
    private JTextField txtMaNV, txtTen, txtSDT, txtEmail, txtChucVu;
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);

    public CapNhatNhanVien_UI() {
        setLayout(new BorderLayout());
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(30, 80, 30, 80));

        JLabel lblTieuDe = new JLabel("CẬP NHẬT THÔNG TIN NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTieuDe.setForeground(Color.WHITE);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(MAU_NEN);

        txtMaNV = taoField(pnlForm, "Mã nhân viên (Không được sửa):");
        txtMaNV.setEditable(false);
        txtTen = taoField(pnlForm, "Họ và tên:");
        txtSDT = taoField(pnlForm, "Số điện thoại:");
        txtEmail = taoField(pnlForm, "Email:");
        txtChucVu = taoField(pnlForm, "Chức vụ:");

        add(pnlForm, BorderLayout.CENTER);

        JButton btnLuu = new JButton("Lưu thay đổi");
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

    public void chonNhanVienDeCapNhat(String maNV) {
        txtMaNV.setText(maNV);
    }
}