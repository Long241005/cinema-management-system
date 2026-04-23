package ui.phim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongKePhim_UI extends JPanel {
    public ThongKePhim_UI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(48, 52, 56));

        // Panel thẻ tổng số
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(new Color(48, 52, 56));
        pnlCards.add(taoCard("Tổng Phim", "24", new Color(52, 152, 219)));
        pnlCards.add(taoCard("Phim Đang Chiếu", "10", new Color(46, 204, 113)));
        pnlCards.add(taoCard("Tổng Suất Chiếu", "156", new Color(241, 196, 15)));
        add(pnlCards, BorderLayout.NORTH);

        // Bảng chi tiết doanh thu phim
        String[] cols = {"Mã Phim", "Tên Phim", "Số Vé Bán", "Doanh Thu"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JPanel taoCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(color);
        JLabel lblT = new JLabel(title, SwingConstants.CENTER);
        JLabel lblV = new JLabel(value, SwingConstants.CENTER);
        lblV.setFont(new Font("Arial", Font.BOLD, 20));
        card.add(lblT); card.add(lblV);
        return card;
    }
}