package ui.khuyenmai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuKhuyenMai_UI extends JPanel {
    public TraCuuKhuyenMai_UI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(48, 52, 56));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(new Color(48, 52, 56));
        JLabel lbl = new JLabel("Mã/Tên KM: "); lbl.setForeground(Color.WHITE);
        JTextField txt = new JTextField(20);
        JButton btn = new JButton("Tra cứu");
        pnlSearch.add(lbl); pnlSearch.add(txt); pnlSearch.add(btn);
        add(pnlSearch, BorderLayout.NORTH);

        String[] cols = {"Mã KM", "Tên Chương Trình", "Chiết Khấu", "Ngày BĐ", "Ngày KT"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}