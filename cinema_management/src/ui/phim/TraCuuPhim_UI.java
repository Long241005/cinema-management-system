package ui.phim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuPhim_UI extends JPanel {
    public TraCuuPhim_UI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(48, 52, 56));

        // Khu vực tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(new Color(48, 52, 56));
        JLabel lbl = new JLabel("Tên phim/Đạo diễn: ");
        lbl.setForeground(Color.WHITE);
        JTextField txt = new JTextField(20);
        JButton btn = new JButton("Tìm");
        pnlSearch.add(lbl); pnlSearch.add(txt); pnlSearch.add(btn);
        add(pnlSearch, BorderLayout.NORTH);

        // Bảng phim (Khớp entity Phim.java)
        String[] cols = {"Mã Phim", "Tên Phim", "Thể Loại", "Đạo Diễn", "Thời Lượng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"PH01", "Lật Mặt 7", "Gia Đình", "Lý Hải", "138 phút"});
        
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}