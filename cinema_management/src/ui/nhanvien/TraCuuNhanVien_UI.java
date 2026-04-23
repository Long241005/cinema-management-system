package ui.nhanvien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuNhanVien_UI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTim;
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_CHU = Color.WHITE;

    public TraCuuNhanVien_UI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Thanh tìm kiếm
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setBackground(MAU_NEN);
        JLabel lbl = new JLabel("Tìm nhân viên (Tên/SĐT): ");
        lbl.setForeground(MAU_CHU);
        txtTim = new JTextField(25);
        JButton btnTim = new JButton("Tìm");
        pnlTop.add(lbl); pnlTop.add(txtTim); pnlTop.add(btnTim);
        add(pnlTop, BorderLayout.NORTH);

        // Bảng dữ liệu (Khớp với entity NhanVien)
        String[] cols = {"Mã NV", "Họ Tên", "Giới Tính", "SĐT", "Chức Vụ"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}