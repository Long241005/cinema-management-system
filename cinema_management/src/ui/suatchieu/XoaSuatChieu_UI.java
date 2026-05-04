package ui.suatchieu;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import dao.SuatChieu_DAO;

public class XoaSuatChieu_UI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private SuatChieu_DAO scDAO = new SuatChieu_DAO();
    
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");

    public XoaSuatChieu_UI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(48, 52, 56));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("XÓA SUẤT CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã Suất", "Tên Phim", "Phòng", "Thời Gian", "Giá Vé"}, 0);
        table = new JTable(model);
        thietKeBang(table);
        
        JButton btnXoa = new JButton("XÓA SUẤT CHIẾU");
        btnXoa.setBackground(new Color(220, 53, 69)); 
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoa.setPreferredSize(new Dimension(180, 40)); 
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String ma = table.getValueAt(row, 0).toString();
                String tenPhim = table.getValueAt(row, 1).toString();
                
                int opt = JOptionPane.showConfirmDialog(
                    this, 
                    "Bạn có chắc chắn muốn xóa suất chiếu mã: " + ma + " (" + tenPhim + ")?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (opt == JOptionPane.YES_OPTION) {
                    if (scDAO.xoaSuatChieu(ma)) {
                        JOptionPane.showMessageDialog(this, "Đã xóa suất chiếu thành công!");
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một suất chiếu để xóa!");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnXoa);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(31, 32, 44));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 72, 87)));
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        loadData();
    }

    private void thietKeBang(JTable t) {
        t.setRowHeight(34);
        t.setBackground(new Color(31, 32, 44));
        t.setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(220, 53, 69)); 
        t.setSelectionForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // CẤU HÌNH ĐƯỜNG KẺ CHO TABLE
        t.setShowGrid(true); // Hiển thị đường kẻ
        t.setGridColor(new Color(70, 72, 87)); // Đặt màu cho đường kẻ (cùng màu border hệ thống)
        t.setIntercellSpacing(new Dimension(1, 1)); // Khoảng cách giữa các ô
        
        t.getTableHeader().setBackground(new Color(60, 64, 68));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        t.getColumnModel().getColumn(0).setPreferredWidth(80);
        t.getColumnModel().getColumn(1).setPreferredWidth(220);
        t.getColumnModel().getColumn(2).setPreferredWidth(100);
        t.getColumnModel().getColumn(3).setPreferredWidth(160);
        t.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void loadData() {
        model.setRowCount(0);
        // Lấy dữ liệu JOIN từ bảng Ve để hiển thị giá vé[cite: 9]
        List<Object[]> data = scDAO.getAllSuatChieuWithPrice();
        for (Object[] obj : data) {
            model.addRow(new Object[]{
                obj[0], // maSC
                obj[1], // tenPhim
                obj[2], // tenPhong
                ((LocalDateTime)obj[3]).format(dtf), // gioChieu
                obj[4] != null ? df.format(obj[4]) : "0 VNĐ" // giaVe từ bảng Ve[cite: 9]
            });
        }
    }
}