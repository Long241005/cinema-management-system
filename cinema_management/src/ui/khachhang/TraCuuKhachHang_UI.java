package ui.khachhang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuKhachHang_UI extends JPanel {
    public TraCuuKhachHang_UI() {
        setLayout(new BorderLayout());
        setBackground(new Color(48, 52, 56));
        
        JLabel lbl = new JLabel("TRA CỨU KHÁCH HÀNG", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lbl, BorderLayout.NORTH);

        String[] cols = {"Mã KH", "Tên Khách Hàng", "SĐT", "Email", "Điểm Tích Lũy"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}