package ui.khachhang;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicScrollBarUI;

import entity.KhachHang;
import dao.KhachHang_DAO;

public class TraCuuKhachHang_UI extends JPanel {

    // ========== HẰNG SỐ MÀU SẮC ==========
    private final Color MAU_NEN_TAB = new Color(48, 52, 56);
    private final Color MAU_THANH_TIM_KIEM = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU_CHUNG = Color.WHITE;
    private final Color MAU_NEN_ITEM = new Color(31, 32, 44);
    private final Color MAU_VIEN_INPUT = new Color(70, 72, 87);

    // ========== COMPONENTS ==========
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTimMa, txtTimTen, txtTimSDT;
    private JComboBox<String> cmbSapXep;
    private KhachHang_DAO khachHangDAO;

    public TraCuuKhachHang_UI() {
        khachHangDAO = new KhachHang_DAO();
        khoiTaoGiaoDien();
        lamMoiBang(); // Đổ dữ liệu từ SQL khi vừa mở trang
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout());
        setBackground(MAU_NEN_TAB);

        JPanel panelChinh = new JPanel(new BorderLayout(0, 15));
        panelChinh.setBackground(MAU_NEN_TAB);
        panelChinh.setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Tiêu đề
        panelChinh.add(taoPanelTieuDe(), BorderLayout.NORTH);

        // 2. Khu vực điều khiển (Tìm kiếm + Nút bấm)
        JPanel pnlDieuKhien = new JPanel(new BorderLayout(20, 0));
        pnlDieuKhien.setBackground(MAU_NEN_TAB);

        // Nhóm tìm kiếm & Lọc
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlTimKiem.setBackground(MAU_NEN_TAB);

        // Ô Lọc Sắp Xếp
        cmbSapXep = new JComboBox<>(new String[]{
            "Sắp xếp", "Tên A-Z", "Tên Z-A", "Điểm: Thấp - Cao", "Điểm: Cao - Thấp"
        });
        cmbSapXep.addActionListener(e -> thucHienSapXep());
        pnlTimKiem.add(taoWrapperInput("Lọc:", cmbSapXep, 160));

        // 3 Ô Tìm kiếm Live Search
        txtTimMa = new JTextField(10);
        txtTimTen = new JTextField(15);
        txtTimSDT = new JTextField(12);

        KeyAdapter liveSearch = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { thucHienTimKiem(); }
        };
        txtTimMa.addKeyListener(liveSearch);
        txtTimTen.addKeyListener(liveSearch);
        txtTimSDT.addKeyListener(liveSearch);

        pnlTimKiem.add(taoWrapperInput("Mã KH:", txtTimMa, 150));
        pnlTimKiem.add(taoWrapperInput("Tên KH:", txtTimTen, 180));
        pnlTimKiem.add(taoWrapperInput("SĐT:", txtTimSDT, 150));

        // Nhóm nút chức năng
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlNut.setBackground(MAU_NEN_TAB);

        JButton btnThem = taoNutChucNang("Thêm", new Color(76, 175, 80));
        btnThem.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ThemKhachHang_UI(frame, table, () -> lamMoiBang()).setVisible(true);
        });

        JButton btnLamMoi = taoNutChucNang("Làm mới", new Color(33, 150, 243));
        btnLamMoi.addActionListener(e -> {
            txtTimMa.setText(""); txtTimTen.setText(""); txtTimSDT.setText("");
            cmbSapXep.setSelectedIndex(0);
            lamMoiBang();
        });

        pnlNut.add(btnThem);
        pnlNut.add(btnLamMoi);

        pnlDieuKhien.add(pnlTimKiem, BorderLayout.WEST);
        pnlDieuKhien.add(pnlNut, BorderLayout.EAST);

        // 3. Bảng dữ liệu
        String[] columns = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email", "Tích điểm"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        thietKeBang();

        JScrollPane scrollPane = new JScrollPane(table);
        tuyChinhScrollBar(scrollPane);

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(MAU_NEN_TAB);
        pnlCenter.add(pnlDieuKhien, BorderLayout.NORTH);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        panelChinh.add(pnlCenter, BorderLayout.CENTER);
        add(panelChinh);
    }

    // ========== HÀM XỬ LÝ DỮ LIỆU DAO ==========

    public void lamMoiBang() {
        hienThiDanhSach(khachHangDAO.docDanhSachKhachHang());
    }

    private void hienThiDanhSach(List<KhachHang> ds) {
        tableModel.setRowCount(0);
        for (KhachHang kh : ds) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(), kh.getTenKhachHang(), kh.getSDT(), kh.getEmail(), kh.getDiemTichLuy()
            });
        }
    }

    private void thucHienTimKiem() {
        String ma = txtTimMa.getText().trim();
        String ten = txtTimTen.getText().trim();
        String sdt = txtTimSDT.getText().trim();

        if (ma.isEmpty() && ten.isEmpty() && sdt.isEmpty()) {
            lamMoiBang();
            return;
        }

        List<KhachHang> ds;
        if (!ma.isEmpty()) ds = khachHangDAO.timKiemTheoMa(ma);
        else if (!ten.isEmpty()) ds = khachHangDAO.timKiemTheoTen(ten);
        else ds = khachHangDAO.timKiemTheoSDT(sdt);

        hienThiDanhSach(ds);
    }

    private void thucHienSapXep() {
        int index = cmbSapXep.getSelectedIndex();
        if (index == 0) {
            lamMoiBang();
            return;
        }

        List<KhachHang> ds = null;
        switch (index) {
            case 1: ds = khachHangDAO.sapXepTheoTen(true); break;
            case 2: ds = khachHangDAO.sapXepTheoTen(false); break;
            case 3: ds = khachHangDAO.sapXepTheoDiem(true); break;
            case 4: ds = khachHangDAO.sapXepTheoDiem(false); break;
        }

        if (ds != null) hienThiDanhSach(ds);
    }

    // ========== HELPER UI METHODS ==========

    private JPanel taoPanelTieuDe() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(MAU_NEN_TAB);
        JLabel lbl = new JLabel("TRA CỨU KHÁCH HÀNG");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lbl.setForeground(MAU_CHU_CHUNG);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, MAU_NHAN_XAM),
            new EmptyBorder(10, 30, 10, 30)
        ));
        p.add(lbl);
        return p;
    }

    /**
     * Hàm dùng chung cho cả JTextField và JComboBox
     */
    private JPanel taoWrapperInput(String label, JComponent comp, int width) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MAU_NEN_TAB);
        
        JLabel lbl = new JLabel(label);
        lbl.setOpaque(true);
        lbl.setBackground(MAU_NHAN_XAM);
        lbl.setForeground(MAU_CHU_CHUNG);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        comp.setBackground(MAU_THANH_TIM_KIEM);
        comp.setForeground(MAU_CHU_CHUNG);
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comp.setPreferredSize(new Dimension(width, 35));
        comp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MAU_VIEN_INPUT, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        if (comp instanceof JTextField) {
            ((JTextField) comp).setCaretColor(MAU_CHU_CHUNG);
        }

        p.add(lbl, BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JButton taoNutChucNang(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg);
        b.setForeground(MAU_CHU_CHUNG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(110, 40));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void thietKeBang() {
        table.setBackground(MAU_NEN_ITEM);
        table.setForeground(MAU_CHU_CHUNG);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setGridColor(new Color(60, 62, 77));
        table.setSelectionBackground(new Color(70, 72, 90));
        table.setSelectionForeground(MAU_CHU_CHUNG);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setOpaque(false);
        for(int i=0; i<table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JTableHeader h = table.getTableHeader();
        h.setBackground(MAU_NEN_ITEM);
        h.setForeground(MAU_CHU_CHUNG);
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setPreferredSize(new Dimension(0, 45));
    }

    private void tuyChinhScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                this.thumbColor = MAU_NHAN_XAM;
                this.trackColor = MAU_NEN_TAB;
            }
        });
        sp.setBorder(BorderFactory.createLineBorder(new Color(60, 62, 77)));
    }
    
    // Thêm hàm này để các class khác có thể lấy table nếu cần
    public JTable getTable() {
        return table;
    }
}