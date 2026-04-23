package ui.khachhang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongKeKhachHang_UI extends JPanel {
    public ThongKeKhachHang_UI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(48, 52, 56));

        // Thống kê nhanh
        JPanel pnlTop = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTop.setBackground(new Color(48, 52, 56));
        pnlTop.add(taoCard("Tổng Khách Hàng", "1,250", new Color(155, 89, 182)));
        pnlTop.add(taoCard("Khách Hàng Thân Thiết", "450", new Color(52, 152, 219)));
        add(pnlTop, BorderLayout.NORTH);

        // Bảng khách hàng tiêu biểu
        String[] cols = {"Mã KH", "Tên Khách Hàng", "SĐT", "Tổng Chi Tiêu", "Hạng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JPanel taoCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(color);
        JLabel lblT = new JLabel(title, SwingConstants.CENTER);
        JLabel lblV = new JLabel(value, SwingConstants.CENTER);
        lblV.setFont(new Font("Arial", Font.BOLD, 22));
        lblV.setForeground(Color.WHITE);
        lblT.setForeground(Color.WHITE);
        card.add(lblT); card.add(lblV);
        return card;
    }
}