package ui.banve;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import dao.*;
import entity.*;

public class BanVe_UI extends JPanel {
    // ===== HỆ MÀU DARK MODE ĐỒNG BỘ =====
    private final Color BG = new Color(48, 52, 56);
    private final Color CARD = new Color(31, 32, 44);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color YELLOW = new Color(255, 193, 7);
    
    // Màu ghế theo thực thể ThemGhe_UI
    private final Color COLOR_VIP = new Color(255, 200, 200);
    private final Color COLOR_DOI = new Color(255, 120, 170);
    private final Color COLOR_THUONG = new Color(200, 190, 230);
    private final Color COLOR_CHON = new Color(255, 0, 127);
    private final Color COLOR_DA_DAT = new Color(80, 80, 80);

    private JTable tblSuatChieu;
    private DefaultTableModel modelSuatChieu;
    private JPanel pnlGhe;
    private JTextField txtKhachHang, txtNhanVien, txtTongTien;
    private JButton btnXacNhan, btnHuy, btnThoat; // Khai báo các nút bấm
    
    private SuatChieu_DAO scDAO = new SuatChieu_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private Ghe_DAO gheDAO = new Ghe_DAO();
    
    private List<String> danhSachGheDangChon = new ArrayList<>();
    private BigDecimal giaVeHienTai = BigDecimal.ZERO;
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public BanVe_UI() {
        setLayout(new BorderLayout(10, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // --- 1. CHỌN SUẤT CHIẾU (PHÍA TRÊN) ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(createTitledBorder("Chọn Suất Chiếu Hôm Nay"));

        String[] cols = {"Mã SC", "Tên Phim", "Phòng", "Bắt Đầu", "Kết Thúc", "Giá Vé"};
        modelSuatChieu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSuatChieu = new JTable(modelSuatChieu);
        styleTable(tblSuatChieu);
        
        JScrollPane scrollSC = new JScrollPane(tblSuatChieu);
        scrollSC.setPreferredSize(new Dimension(0, 180));
        pnlTop.add(scrollSC, BorderLayout.CENTER);

        // --- 2. SƠ ĐỒ GHẾ NGỒI ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setOpaque(false);
        pnlCenter.setBorder(createTitledBorder("Sơ đồ Ghế Ngồi"));

        pnlGhe = new JPanel(new GridBagLayout()); // Chống giãn đứng[cite: 10]
        pnlGhe.setBackground(CARD);
        pnlGhe.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        JLabel lblHuongDan = new JLabel("Vui lòng chọn suất chiếu để xem sơ đồ ghế.", SwingConstants.CENTER);
        lblHuongDan.setForeground(new Color(150, 150, 150));
        pnlGhe.add(lblHuongDan);

        JScrollPane scrollGhe = new JScrollPane(pnlGhe);
        scrollGhe.getViewport().setBackground(CARD);
        scrollGhe.setBorder(null);
        pnlCenter.add(scrollGhe, BorderLayout.CENTER);

        // --- 3. THÔNG TIN ĐẶT VÉ ---
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(createTitledBorder("Thông Tin Đặt Vé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        pnlInfo.add(createLabel("Khách hàng (SĐT):"), gbc);
        txtKhachHang = createTextField();
        gbc.gridx = 1; gbc.weightx = 1.0;
        pnlInfo.add(txtKhachHang, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnlInfo.add(createLabel("Nhân viên:"), gbc);
        txtNhanVien = createTextField();
        txtNhanVien.setText("Nguyễn Hoàng A"); 
        txtNhanVien.setEditable(false);
        txtNhanVien.setBackground(new Color(80, 84, 88));
        gbc.gridx = 3; gbc.weightx = 1.0;
        pnlInfo.add(txtNhanVien, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlInfo.add(createLabel("Tổng tiền:"), gbc);
        txtTongTien = createTextField();
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTien.setForeground(YELLOW);
        gbc.gridx = 1; gbc.weightx = 1.0;
        pnlInfo.add(txtTongTien, gbc);

        // --- 4. CHỨC NĂNG ---
        JPanel pnlButtons = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlButtons.setOpaque(false);
        pnlButtons.setBorder(createTitledBorder("Chức năng"));

        btnXacNhan = createActionButton("Thanh Toán", Color.BLUE);
        btnHuy = createActionButton("Hủy Chọn Ghế", new Color(231, 76, 60));
        btnThoat = createActionButton("Thoát", new Color(100, 100, 100));

        pnlButtons.add(btnXacNhan); 
        pnlButtons.add(btnHuy); 
        pnlButtons.add(btnThoat);

        JPanel pnlSouth = new JPanel(new BorderLayout(0, 10));
        pnlSouth.setOpaque(false);
        pnlSouth.add(pnlInfo, BorderLayout.NORTH);
        pnlSouth.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);

        // ===== GÁN SỰ KIỆN =====[cite: 10]
        
        // Chọn suất chiếu
        tblSuatChieu.getSelectionModel().addListSelectionListener(e -> {
            int row = tblSuatChieu.getSelectedRow();
            if (row != -1) {
                String giaVeStr = tblSuatChieu.getValueAt(row, 5).toString().replaceAll("[^0-9]", "");
                giaVeHienTai = new BigDecimal(giaVeStr);
                loadSoDoGheTheoPhong();
                tinhTongTien();
            }
        });

        // Nút Thanh Toán (Kết nối với ThanhToan_UI)[cite: 11]
        btnXacNhan.addActionListener(e -> {
            int row = tblSuatChieu.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu!");
                return;
            }
            if (danhSachGheDangChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!");
                return;
            }

            String maSC = tblSuatChieu.getValueAt(row, 0).toString();
            List<SuatChieu> ds = scDAO.timKiemTheoMa(maSC);
            if (ds.isEmpty()) return;
            SuatChieu scSelected = ds.get(0);

            // Tìm cửa sổ cha để mở Dialog
            Window win = SwingUtilities.getWindowAncestor(this);
            Frame parentFrame = (win instanceof Frame) ? (Frame) win : null;

            ThanhToan_UI dialog = new ThanhToan_UI(
                parentFrame, 
                scSelected, 
                danhSachGheDangChon, 
                txtKhachHang.getText(), 
                giaVeHienTai.multiply(new BigDecimal(danhSachGheDangChon.size()))
            );
            dialog.setVisible(true); 
        });

        // Hủy chọn ghế
        btnHuy.addActionListener(e -> {
            danhSachGheDangChon.clear();
            tinhTongTien();
            loadSoDoGheTheoPhong();
        });

        txtKhachHang.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { kiemTraKhachHang(); }
        });

        loadSuatChieu();
    }

    // Các phương thức phụ trợ (loadSuatChieu, loadSoDoGheTheoPhong, resetMauGhe, v.v.)[cite: 10]
    private void loadSuatChieu() {
        modelSuatChieu.setRowCount(0);
        List<Object[]> ds = scDAO.getAllSuatChieuWithPrice();
        for (Object[] row : ds) {
            modelSuatChieu.addRow(new Object[]{
                row[0], row[1], row[2], 
                ((LocalDateTime)row[3]).format(dtf), 
                ((LocalDateTime)row[3]).plusHours(2).format(dtf),
                df.format(row[4])
            });
        }
    }

    private void loadSoDoGheTheoPhong() {
        int rowSelected = tblSuatChieu.getSelectedRow();
        if (rowSelected == -1) return;

        String maSC = tblSuatChieu.getValueAt(rowSelected, 0).toString();
        List<SuatChieu> dsSC = scDAO.timKiemTheoMa(maSC);
        if (dsSC.isEmpty()) return;
        
        SuatChieu sc = dsSC.get(0);
        pnlGhe.removeAll();
        pnlGhe.setLayout(new GridBagLayout()); 
        
        JPanel pnlContainerGhe = new JPanel(new GridLayout(0, 10, 10, 10));
        pnlContainerGhe.setOpaque(false);
        danhSachGheDangChon.clear();

        List<Ghe> dsGhe = gheDAO.timGheTheoPhong(sc.getPhongChieu().getMaPhong()); 
        for (Ghe g : dsGhe) {
            String labelGhe = g.getHang().toUpperCase() + g.getSoGhe();
            JButton btn = new JButton(labelGhe);
            btn.setPreferredSize(new Dimension(60, 60)); 
            
            if (g.getTrangThai().equalsIgnoreCase("Đã đặt")) {
                btn.setBackground(COLOR_DA_DAT);
                btn.setEnabled(false);
                btn.setForeground(Color.WHITE);
            } else {
                resetMauGhe(btn, g);
                btn.addActionListener(ev -> {
                    if (btn.getBackground().equals(COLOR_CHON)) {
                        resetMauGhe(btn, g);
                        danhSachGheDangChon.remove(labelGhe);
                    } else {
                        btn.setBackground(COLOR_CHON);
                        btn.setForeground(Color.WHITE);
                        danhSachGheDangChon.add(labelGhe);
                    }
                    tinhTongTien();
                });
            }
            pnlContainerGhe.add(btn);
        }
        pnlGhe.add(pnlContainerGhe); 
        pnlGhe.revalidate(); pnlGhe.repaint();
    }

    private void resetMauGhe(JButton btn, Ghe g) {
        if (g.getLoaiGhe().equalsIgnoreCase("VIP")) btn.setBackground(COLOR_VIP);
        else if (g.getLoaiGhe().equalsIgnoreCase("Đôi")) btn.setBackground(COLOR_DOI);
        else btn.setBackground(COLOR_THUONG);
        btn.setForeground(Color.BLACK);
    }

    private void tinhTongTien() {
        BigDecimal tong = giaVeHienTai.multiply(new BigDecimal(danhSachGheDangChon.size()));
        txtTongTien.setText(df.format(tong));
    }

    private void kiemTraKhachHang() {
        String sdt = txtKhachHang.getText().trim();
        if (sdt.isEmpty()) return;
        List<KhachHang> ds = khDAO.timKiemTheoSDT(sdt);
        if (ds.isEmpty()) {
            int res = JOptionPane.showConfirmDialog(this, "Thêm khách hàng mới?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) JOptionPane.showMessageDialog(this, "Vui lòng mở tab 'Thêm Khách Hàng'");
        } else {
            KhachHang kh = ds.get(0);
            txtKhachHang.setText(kh.getTenKhachHang() + " - " + kh.getSDT());
        }
    }

    private TitledBorder createTitledBorder(String t) {
        TitledBorder b = BorderFactory.createTitledBorder(new LineBorder(BORDER), t);
        b.setTitleColor(TEXT); return b;
    }
    private JLabel createLabel(String t) { return new JLabel(t) {{ setForeground(TEXT); setFont(new Font("Segoe UI", Font.BOLD, 14)); }}; }
    private JTextField createTextField() { return new JTextField() {{ setBackground(INPUT_BG); setForeground(TEXT); setCaretColor(TEXT); setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(5, 10, 5, 10))); }}; }
    private JButton createActionButton(String t, Color bg) { return new JButton(t) {{ setBackground(bg); setForeground(TEXT); setFont(new Font("Segoe UI", Font.BOLD, 13)); setFocusPainted(false); }}; }
    private void styleTable(JTable t) { t.setRowHeight(35); t.setBackground(CARD); t.setForeground(TEXT); t.setSelectionBackground(Color.BLUE); t.getTableHeader().setBackground(INPUT_BG); t.getTableHeader().setForeground(TEXT); t.setGridColor(BORDER); }
}