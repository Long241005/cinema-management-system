package ui.phongchieu;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dao.PhongChieu_DAO;
import entity.PhongChieu;

public class TraCuuPhong_UI extends JPanel {

    // ===== Màu sắc đồng bộ hệ thống =====
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_INPUT_BG = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_VIEN = new Color(70, 72, 87);
    private final Color MAU_XANH = new Color(33, 150, 243);

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtTimKiem;
    private JTextField txtMaPhong, txtTenPhong, txtSoGhe, txtLoaiPhong;

    private PhongChieu_DAO phongChieuDAO;

    public TraCuuPhong_UI() {
        phongChieuDAO = new PhongChieu_DAO();
        khoiTaoGiaoDien();
        loadData();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("TRA CỨU PHÒNG CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(MAU_CHU);
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelMain = new JPanel(new BorderLayout(0, 20));
        panelMain.setOpaque(false);

        // ===== FORM THÔNG TIN (CHỈ XEM) =====
        JPanel pnlForm = new JPanel(new GridLayout(2, 2, 20, 15));
        pnlForm.setOpaque(false);

        txtMaPhong = taoInput(pnlForm, "Mã phòng chiếu:");
        txtTenPhong = taoInput(pnlForm, "Tên phòng chiếu:");
        txtSoGhe = taoInput(pnlForm, "Số lượng ghế:");
        txtLoaiPhong = taoInput(pnlForm, "Loại phòng:");

        // Không cho phép chỉnh sửa trong giao diện tra cứu[cite: 3]
        txtMaPhong.setEditable(false);
        txtTenPhong.setEditable(false);
        txtSoGhe.setEditable(false);
        txtLoaiPhong.setEditable(false);

        // ===== THANH TÌM KIẾM =====
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Nhập mã phòng cần tìm (VD: P0001):");
        lblSearch.setForeground(MAU_CHU);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setBackground(MAU_INPUT_BG);
        txtTimKiem.setForeground(MAU_CHU);
        txtTimKiem.setCaretColor(MAU_CHU);
        txtTimKiem.setPreferredSize(new Dimension(200, 35));
        txtTimKiem.setBorder(new LineBorder(MAU_VIEN));

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setBackground(MAU_XANH);
        btnTim.setForeground(MAU_CHU);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnLamMoi = new JButton("Làm mới danh sách");
        btnLamMoi.setBackground(MAU_INPUT_BG);
        btnLamMoi.setForeground(MAU_CHU);

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnLamMoi);

        // ===== BẢNG DỮ LIỆU =====
        model = new DefaultTableModel(new String[]{
                "Mã Phòng", "Tên Phòng", "Số Ghế", "Loại Phòng"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        thietKeBang();

        JScrollPane scrollPane = new JScrollPane(table);
        tuyChinhScrollBar(scrollPane);
        scrollPane.setPreferredSize(new Dimension(0, 300));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);
        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(pnlSearch, BorderLayout.CENTER);
        pnlCenter.add(scrollPane, BorderLayout.SOUTH);

        panelMain.add(pnlCenter, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtMaPhong.setText(model.getValueAt(row, 0).toString());
                    txtTenPhong.setText(model.getValueAt(row, 1).toString());
                    txtSoGhe.setText(model.getValueAt(row, 2).toString());
                    txtLoaiPhong.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        btnTim.addActionListener(e -> timKiem());
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadData();
        });
    }

    private JTextField taoInput(JPanel pnl, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 8));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(250, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBackground(MAU_INPUT_BG);
        txt.setForeground(MAU_CHU);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_VIEN, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        pnl.add(wrap);
        return txt;
    }

    private void loadData() {
        model.setRowCount(0);
        List<PhongChieu> ds = phongChieuDAO.docDanhSachPhongChieu();
        for (PhongChieu p : ds) {
            model.addRow(new Object[]{ p.getMaPhong(), p.getTenPhong(), p.getSoGhe(), p.getLoaiPhong() });
        }
    }

    private void timKiem() {
        String ma = txtTimKiem.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phòng cần tìm!");
            return;
        }

        PhongChieu p = phongChieuDAO.timPhongTheoMa(ma);
        model.setRowCount(0);
        if (p != null) {
            model.addRow(new Object[]{ p.getMaPhong(), p.getTenPhong(), p.getSoGhe(), p.getLoaiPhong() });
            // Hiển thị lên form thông tin
            txtMaPhong.setText(p.getMaPhong());
            txtTenPhong.setText(p.getTenPhong());
            txtSoGhe.setText(String.valueOf(p.getSoGhe()));
            txtLoaiPhong.setText(p.getLoaiPhong());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng có mã: " + ma);
            loadData();
        }
    }

    private void thietKeBang() {
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(MAU_INPUT_BG);
        table.getTableHeader().setForeground(MAU_CHU);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);
    }

    private void tuyChinhScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MAU_NHAN_XAM;
                this.trackColor = MAU_NEN;
            }
        });
    }
}