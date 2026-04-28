package ui.khachhang;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_DAO;
import entity.KhachHang;

public class ThongKeKhachHang_UI extends JPanel {

    private final Color BG = new Color(48, 52, 56);
    private final Color CARD_BG = new Color(60, 64, 68);
    private final Color TABLE_BG = new Color(31, 32, 44);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);

    private JLabel lblTongKhachHang;
    private JLabel lblKhachVIP;
    private JLabel lblDiemCaoNhat;

    private JTable table;
    private DefaultTableModel model;

    private KhachHang_DAO dao;

    public ThongKeKhachHang_UI() {
        dao = new KhachHang_DAO();
        initUI();
        loadThongKe();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("THỐNG KÊ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        // ===== CARD THỐNG KÊ =====
        JPanel pnlTop = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlTop.setOpaque(false);

        lblTongKhachHang = createCard(pnlTop, "Tổng số khách hàng", "0");
        lblKhachVIP = createCard(pnlTop, "Khách hàng VIP", "0");
        lblDiemCaoNhat = createCard(pnlTop, "Điểm tích lũy cao nhất", "0");

        // ===== TABLE =====
        JPanel pnlTable = new JPanel(new BorderLayout(0, 15));
        pnlTable.setOpaque(false);

        JLabel lblBang = new JLabel("Bảng xếp hạng khách hàng");
        lblBang.setForeground(TEXT);
        lblBang.setFont(new Font("Segoe UI", Font.BOLD, 18));

        model = new DefaultTableModel(new String[]{
                "Top", "Mã KH", "Tên khách hàng", "SĐT", "Email", "Điểm tích lũy", "Xếp hạng"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(TABLE_BG);

        pnlTable.add(lblBang, BorderLayout.NORTH);
        pnlTable.add(scroll, BorderLayout.CENTER);

        content.add(pnlTop, BorderLayout.NORTH);
        content.add(pnlTable, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    private JLabel createCard(JPanel parent, String title, String value) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(BLUE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        parent.add(card);
        return lblValue;
    }

    private void styleTable() {
        table.setRowHeight(34);
        table.setBackground(TABLE_BG);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setBackground(CARD_BG);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionBackground(BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // ===== LOAD DỮ LIỆU TỪ DATABASE =====
    private void loadThongKe() {
        // lấy danh sách đã sắp xếp theo điểm tích lũy giảm dần
        List<KhachHang> ds = dao.sapXepTheoDiem(false);

        // Tổng số khách hàng
        lblTongKhachHang.setText(String.valueOf(ds.size()));

        int vip = 0;
        int maxDiem = 0;

        model.setRowCount(0);

        int top = 1;

        for (KhachHang kh : ds) {
            int diem = kh.getDiemTichLuy();

            // khách VIP: điểm >= 500
            if (diem >= 500) {
                vip++;
            }

            // điểm cao nhất
            if (diem > maxDiem) {
                maxDiem = diem;
            }

            String xepHang;

            if (diem >= 1000) {
                xepHang = "VIP Kim Cương";
            } else if (diem >= 700) {
                xepHang = "VIP Vàng";
            } else if (diem >= 500) {
                xepHang = "VIP Bạc";
            } else if (diem >= 300) {
                xepHang = "Thân thiết";
            } else {
                xepHang = "Thành viên";
            }

            model.addRow(new Object[]{
                    top++, // top xếp hạng
                    kh.getMaKH(),
                    kh.getTenKhachHang(),
                    kh.getSDT(),
                    kh.getEmail(),
                    diem,
                    xepHang
            });
        }

        lblKhachVIP.setText(String.valueOf(vip));
        lblDiemCaoNhat.setText(String.valueOf(maxDiem));
    }
}