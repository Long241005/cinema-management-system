package ui.hoadon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongKeHoaDon_UI extends JPanel {
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_CHU = Color.WHITE;

    public ThongKeHoaDon_UI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel tiêu đề và bộ lọc
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(MAU_NEN);
        
        JLabel lblTieuDe = new JLabel("THỐNG KÊ DOANH THU HÓA ĐƠN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(MAU_CHU);
        pnlHeader.add(lblTieuDe, BorderLayout.NORTH);

        add(pnlHeader, BorderLayout.NORTH);

        // Bảng dữ liệu chi tiết
        String[] columns = {"Ngày", "Số Lượng Hóa Đơn", "Tổng Doanh Thu", "Ghi Chú"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        // Dữ liệu mẫu
        model.addRow(new Object[]{"24/04/2026", "45", "12,500,000 VNĐ", "Ổn định"});
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}