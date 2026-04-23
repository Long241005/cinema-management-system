package ui.phongchieu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyPhong_UI extends JPanel {
    public QuanLyPhong_UI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(48, 52, 56));

        JLabel lbl = new JLabel("DANH SÁCH PHÒNG CHIẾU", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);
        add(lbl, BorderLayout.NORTH);

        String[] cols = {"Mã Phòng", "Tên Phòng", "Số Ghế", "Loại Phòng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        
        // Mẫu dữ liệu khớp với SQL của bạn
        model.addRow(new Object[]{"P01", "Phòng 01", "120", "IMAX"});
        model.addRow(new Object[]{"P02", "Phòng 02", "80", "2D"});

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}