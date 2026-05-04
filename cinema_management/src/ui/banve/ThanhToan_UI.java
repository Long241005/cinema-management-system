package ui.banve;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import entity.SuatChieu;

public class ThanhToan_UI extends JDialog {
    // ===== HỆ MÀU DARK MODE ĐỒNG BỘ =====
    private final Color BG = new Color(48, 52, 56);
    private final Color CARD = new Color(31, 32, 44);
    private final Color TEXT = Color.WHITE;
    private final Color HIGHLIGHT = new Color(255, 193, 7);
    private final Color BORDER = new Color(70, 72, 87);

    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

    public ThanhToan_UI(Frame parent, SuatChieu sc, List<String> dsGhe, String khachHang, BigDecimal tongTien) {
        super(parent, "TÍNH TIỀN", true);
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(20, 20));

        // --- 1. HEADER: TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("TÍNH TIỀN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(TEXT);
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- 2. CENTER: THÔNG TIN CHUNG & BẢNG VÉ ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 20));
        pnlCenter.setOpaque(false);
        pnlCenter.setBorder(new EmptyBorder(0, 30, 0, 30));

        // Thông tin hóa đơn[cite: 1, 10]
        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 40, 15));
        pnlInfo.setOpaque(false);
        
        pnlInfo.add(createLabelPair("Mã hóa đơn:", "HD0001", Color.RED)); // Mã mặc định theo yêu cầu
        pnlInfo.add(createLabelPair("Tên phim:", sc.getPhim().getTenPhim(), HIGHLIGHT));
        pnlInfo.add(createLabelPair("Nhân viên:", "Nguyễn Hoàng A", TEXT)); // Nhân viên mặc định[cite: 1]
        pnlInfo.add(createLabelPair("Phòng:", sc.getPhongChieu().getTenPhong(), TEXT));
        pnlInfo.add(createLabelPair("Khách hàng:", khachHang, HIGHLIGHT));
        pnlInfo.add(createLabelPair("Giờ thanh toán:", LocalDateTime.now().format(dtf), TEXT));

        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // Bảng danh sách vé (Thay cho danh sách món ăn)
        String[] cols = {"STT", "Mã Ghế", "Đơn Giá", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        int stt = 1;
        for (String ghe : dsGhe) {
            model.addRow(new Object[]{stt++, ghe, df.format(tongTien.divide(new BigDecimal(dsGhe.size()))), 1, df.format(tongTien.divide(new BigDecimal(dsGhe.size())))});
        }
        
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD);
        scroll.setBorder(new LineBorder(BORDER));
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // --- 3. SOUTH: TỔNG KẾT & NÚT BẤM ---
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setOpaque(false);
        pnlSouth.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Phần tính toán tiền
        JPanel pnlSummary = new JPanel(new GridLayout(3, 1, 0, 10));
        pnlSummary.setOpaque(false);
        pnlSummary.add(createLabelPair("Tổng cộng:", df.format(tongTien), HIGHLIGHT));
        pnlSummary.add(createLabelPair("Thuế (VAT 10%):", df.format(tongTien.multiply(new BigDecimal("0.1"))), TEXT));
        pnlSummary.add(createLabelPair("Tổng thanh toán:", df.format(tongTien.multiply(new BigDecimal("1.1"))), HIGHLIGHT));
        
        pnlSouth.add(pnlSummary, BorderLayout.EAST);

        // Các nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        pnlButtons.setOpaque(false);
        
        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setBackground(new Color(76, 175, 80));
        btnThanhToan.setForeground(TEXT);
        btnThanhToan.setPreferredSize(new Dimension(150, 45));
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setBackground(new Color(231, 76, 60));
        btnDong.setForeground(TEXT);
        btnDong.setPreferredSize(new Dimension(120, 45));
        btnDong.addActionListener(e -> dispose());

        pnlButtons.add(btnThanhToan);
        pnlButtons.add(btnDong);
        pnlSouth.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlSouth, BorderLayout.SOUTH);
    }

    // Helper tạo cặp nhãn thông tin
    private JPanel createLabelPair(String label, String value, Color valueColor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        JLabel lblL = new JLabel(label);
        lblL.setForeground(new Color(180, 180, 180));
        lblL.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblV = new JLabel(value);
        lblV.setForeground(valueColor);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        p.add(lblL);
        p.add(lblV);
        return p;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.setBackground(CARD);
        t.setForeground(TEXT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.getTableHeader().setBackground(new Color(60, 64, 68));
        t.getTableHeader().setForeground(TEXT);
        t.setGridColor(BORDER);
    }
}