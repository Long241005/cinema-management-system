package ui.suatchieu;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import dao.SuatChieu_DAO;
import entity.SuatChieu;

public class TraCuuSuatChieu_UI extends JPanel {

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_VIEN = new Color(70, 72, 87);
    private final Color MAU_XANH = new Color(33, 150, 243);

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JTextField txtMaSC, txtTenPhim, txtPhong, txtNgayChieu, txtGioChieu, txtGiaVe;
    
    private SuatChieu_DAO suatChieuDAO;
    private DateTimeFormatter dtfNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter dtfGio = DateTimeFormatter.ofPattern("HH:mm");
    private DecimalFormat dfTien = new DecimalFormat("#,### VNĐ");

    public TraCuuSuatChieu_UI() {
        suatChieuDAO = new SuatChieu_DAO();
        initUI();
        loadAllData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("TRA CỨU SUẤT CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(MAU_CHU);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 20));
        pnlCenter.setOpaque(false);

        // --- FORM THÔNG TIN CHI TIẾT (READ-ONLY) ---
        JPanel pnlForm = new JPanel(new GridLayout(3, 3, 20, 15));
        pnlForm.setOpaque(false);

        txtMaSC = taoInput(pnlForm, "Mã suất chiếu:");
        txtTenPhim = taoInput(pnlForm, "Tên phim:");
        txtPhong = taoInput(pnlForm, "Phòng chiếu:");
        txtNgayChieu = taoInput(pnlForm, "Ngày chiếu:");
        txtGioChieu = taoInput(pnlForm, "Giờ bắt đầu:");
        txtGiaVe = taoInput(pnlForm, "Giá vé:");

        setFieldsEditable(false);
      //  txtGiaVe.setBackground(new Color(80, 55, 45));
        txtGiaVe.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- THANH TÌM KIẾM ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("🔍 Tìm theo mã suất hoặc tên phim:");
        lblSearch.setForeground(MAU_CHU);
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtTimKiem = new JTextField(25);
        txtTimKiem.setBackground(MAU_INPUT);
        txtTimKiem.setForeground(MAU_CHU);
        txtTimKiem.setCaretColor(MAU_CHU);
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(new LineBorder(MAU_VIEN), new EmptyBorder(8, 12, 8, 12)));

        JButton btnTim = createButton("Tìm kiếm", MAU_XANH);
        JButton btnLamMoi = createButton("Làm mới", MAU_INPUT);

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnLamMoi);

        // --- BẢNG HIỂN THỊ ---
        model = new DefaultTableModel(new String[]{
            "Mã Suất", "Tên Phim", "Phòng", "Ngày Chiếu", "Giờ", "Giá Vé"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        formatTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(31, 32, 44));
        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(pnlSearch, BorderLayout.CENTER);
        pnlCenter.add(scroll, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { fillFormFromTable(); }
        });

        btnTim.addActionListener(e -> thucHienTimKiem());
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadAllData();
            clearFields();
        });
        txtTimKiem.addActionListener(e -> thucHienTimKiem());
    }

    private void loadAllData() {
        // Sử dụng hàm lấy dữ liệu JOIN từ bảng Ve để có giá vé
        List<Object[]> ds = suatChieuDAO.getAllSuatChieuWithPrice();
        hienThiLenBang(ds);
    }

    
    private void thucHienTimKiem() {
        String key = txtTimKiem.getText().trim();
        if (key.isEmpty()) {
            loadAllData();
        } else {
            // Gọi đúng hàm timKiemSuatChieuWithPrice vừa thêm vào DAO[cite: 9]
            List<Object[]> data = suatChieuDAO.timKiemSuatChieuWithPrice(key);
            hienThiLenBang(data);
        }
    }

    private void hienThiLenBang(List<Object[]> ds) {
        model.setRowCount(0);
        for (Object[] obj : ds) {
            LocalDateTime gioChieu = (LocalDateTime) obj[3];
            BigDecimal giaVe = (BigDecimal) obj[4];
            
            model.addRow(new Object[]{
                obj[0], // maSC
                obj[1], // tenPhim
                obj[2], // tenPhong
                gioChieu.format(dtfNgay),
                gioChieu.format(dtfGio),
                dfTien.format(giaVe != null ? giaVe : BigDecimal.ZERO) // Hiển thị giá vé thực tế[cite: 6]
            });
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtMaSC.setText(model.getValueAt(row, 0).toString());
            txtTenPhim.setText(model.getValueAt(row, 1).toString());
            txtPhong.setText(model.getValueAt(row, 2).toString());
            txtNgayChieu.setText(model.getValueAt(row, 3).toString());
            txtGioChieu.setText(model.getValueAt(row, 4).toString());
            txtGiaVe.setText(model.getValueAt(row, 5).toString());
        }
    }

    private JTextField taoInput(JPanel pnl, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 5));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txt = new JTextField();
        txt.setBackground(MAU_INPUT);
        txt.setForeground(MAU_CHU);
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(MAU_VIEN), new EmptyBorder(5, 10, 5, 10)));
        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        pnl.add(wrap);
        return txt;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(MAU_CHU);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setFocusPainted(false);
        return btn;
    }

    private void formatTable() {
        table.setRowHeight(34);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);
        table.setSelectionBackground(MAU_XANH);
        table.getTableHeader().setBackground(MAU_INPUT);
        table.getTableHeader().setForeground(MAU_CHU);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void setFieldsEditable(boolean status) {
        txtMaSC.setEditable(status); txtTenPhim.setEditable(status);
        txtPhong.setEditable(status); txtNgayChieu.setEditable(status);
        txtGioChieu.setEditable(status); txtGiaVe.setEditable(status);
    }

    private void clearFields() {
        txtMaSC.setText(""); txtTenPhim.setText(""); txtPhong.setText("");
        txtNgayChieu.setText(""); txtGioChieu.setText(""); txtGiaVe.setText("");
    }
}