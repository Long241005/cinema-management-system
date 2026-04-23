package ui.hoadon;

import javax.swing.*;
import java.awt.*;

public class TraCuuHoaDon_UI extends JPanel {
    public TraCuuHoaDon_UI() {
        setBackground(new Color(48, 52, 56));
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("LỊCH SỬ HÓA ĐƠN", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);
        
        // Add table hóa đơn ở đây
    }
}