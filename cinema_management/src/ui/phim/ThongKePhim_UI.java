package ui.phim;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import dao.Phim_DAO;
import dao.SuatChieu_DAO;

public class ThongKePhim_UI extends JPanel {
    private JLabel lblTongPhim, lblPhimDangChieu, lblTongSuatChieu;
    private JTable table;
    private DefaultTableModel model;
    private Phim_DAO phimDAO = new Phim_DAO();
    private SuatChieu_DAO scDAO = new SuatChieu_DAO();
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");

    public ThongKePhim_UI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(48, 52, 56)); // Màu nền đồng bộ
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Header: Tiêu đề trang thống kê
        JLabel lblHeader = new JLabel("THỐNG KÊ DOANH THU PHIM", SwingConstants.LEFT);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Panel trung tâm chứa Cards và Bảng
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 30));
        pnlCenter.setOpaque(false);

        // --- Panel Cards Thống kê (Dashboard) ---
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 25, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 120));

        lblTongPhim = new JLabel("0");
        lblPhimDangChieu = new JLabel("0");
        lblTongSuatChieu = new JLabel("0");

        pnlCards.add(taoCard("TỔNG SỐ PHIM", lblTongPhim, new Color(52, 152, 219), "🎬"));
        pnlCards.add(taoCard("PHIM ĐANG CHIẾU", lblPhimDangChieu, new Color(46, 204, 113), "🔥"));
        pnlCards.add(taoCard("TỔNG SUẤT CHIẾU", lblTongSuatChieu, new Color(241, 196, 15), "📅"));
        
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- Panel Bảng chi tiết ---
        String[] cols = {"Mã Phim", "Tên Phim", "Thể Loại", "Số Vé Đã Bán", "Doanh Thu"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        thietKeBang(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(31, 32, 44));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(70, 72, 87)));
        
        pnlCenter.add(scroll, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // Tải dữ liệu thực tế lên giao diện
        lamMoiThongKe();
    }

    private JPanel taoCard(String title, JLabel lblValue, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(31, 32, 44));
        card.setBorder(new CompoundBorder(
            new LineBorder(color, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Phần icon bên trái
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        lblIcon.setForeground(color);
        card.add(lblIcon, BorderLayout.WEST);

        // Phần text bên phải
        JPanel pnlText = new JPanel(new GridLayout(2, 1));
        pnlText.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(180, 180, 180));
        
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(Color.WHITE);
        
        pnlText.add(lblTitle);
        pnlText.add(lblValue);
        
        card.add(pnlText, BorderLayout.CENTER);
        return card;
    }

    private void thietKeBang(JTable t) {
        t.setRowHeight(40);
        t.setBackground(new Color(31, 32, 44));
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setSelectionBackground(new Color(52, 152, 219));
        t.setGridColor(new Color(70, 72, 87));
        t.setShowGrid(true); // Hiển thị đường kẻ để nhìn chuyên nghiệp hơn

        t.getTableHeader().setBackground(new Color(60, 64, 68));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.getTableHeader().setPreferredSize(new Dimension(0, 45));
    }

    public void lamMoiThongKe() {
        // 1. Cập nhật các con số tổng quát từ DAO[cite: 9, 11]
        int tongPhim = phimDAO.docDanhSachPhim().size();
        int tongSuat = scDAO.demSoLuongSuatChieu();
        
        lblTongPhim.setText(String.valueOf(tongPhim));
        lblPhimDangChieu.setText(String.valueOf(tongPhim > 0 ? tongPhim - 1 : 0)); // Ví dụ trừ đi 1 phim sắp chiếu
        lblTongSuatChieu.setText(String.valueOf(tongSuat));

        // 2. Tải dữ liệu vào bảng (Ở đây Long có thể viết thêm hàm lấy doanh thu trong DAO)[cite: 9]
        model.setRowCount(0);
        phimDAO.docDanhSachPhim().forEach(p -> {
            // Giả lập dữ liệu doanh thu (Thành Long có thể thay bằng hàm query thật từ bảng Ve)[cite: 6, 9]
            int soVe = (int) (Math.random() * 100); 
            double doanhThu = soVe * 85000;
            
            model.addRow(new Object[]{
                p.getMaPhim(),
                p.getTenPhim(),
                p.getTheLoai().getTenTheLoai(), // Lấy từ thực thể
                soVe,
                df.format(doanhThu)
            });
        });
    }
}