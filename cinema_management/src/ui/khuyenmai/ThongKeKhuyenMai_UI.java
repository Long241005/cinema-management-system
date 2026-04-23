package ui.khuyenmai;

import javax.swing.*;
import java.awt.*;

public class ThongKeKhuyenMai_UI extends JPanel {
    public ThongKeKhuyenMai_UI() {
        setBackground(new Color(48, 52, 56));
        JLabel lbl = new JLabel("THỐNG KÊ HIỆU QUẢ KHUYẾN MÃI", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        add(lbl, BorderLayout.NORTH);
        
        // Chỗ này bạn có thể vẽ biểu đồ hoặc bảng thống kê số lượt dùng KM
        add(new JLabel("Biểu đồ doanh thu từ khuyến mãi sẽ hiển thị tại đây...", SwingConstants.CENTER) {{
            setForeground(Color.GRAY);
        }}, BorderLayout.CENTER);
    }
}