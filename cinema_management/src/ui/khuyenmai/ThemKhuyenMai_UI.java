package ui.khuyenmai;

import javax.swing.*;
import java.awt.*;

public class ThemKhuyenMai_UI extends JPanel {
    public ThemKhuyenMai_UI() {
        setBackground(new Color(48, 52, 56));
        setLayout(new GridBagLayout()); // Dùng để căn giữa form
        
        JLabel lbl = new JLabel("THÊM CHƯƠNG TRÌNH KHUYẾN MÃI");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lbl);
        // Bạn có thể copy hàm taoField từ ThemNhanVien sang đây để làm form nhanh
    }
}