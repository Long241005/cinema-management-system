package ui.ghe;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import dao.Ghe_DAO;
import dao.PhongChieu_DAO;
import entity.Ghe;
import entity.PhongChieu;

public class ThemGhe_UI extends JPanel {

    // ===== HỆ MÀU DARK MODE & LOTTE STYLE =====
    private final Color BG = new Color(48, 52, 56);
    private final Color CARD = new Color(31, 32, 44);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color INPUT_BG = new Color(60, 64, 68);

    private final Color COLOR_THUONG = new Color(200, 190, 230); // Tím nhạt
    private final Color COLOR_VIP = new Color(255, 200, 200);    // Hồng nhạt
    private final Color COLOR_DOI = new Color(255, 120, 170);    // Hồng đậm
    private final Color COLOR_CHON = new Color(255, 0, 127);     // Hồng Neon
    private final Color COLOR_DA_DAT = new Color(80, 80, 80);    // Xám

    private JPanel pnlSoDoGhe;
    private JComboBox<PhongChieu> cmbPhong;
    private JTextField txtMaGhe, txtHang, txtSoGhe;
    private JComboBox<String> cmbLoaiGhe, cmbTrangThai;
    private JButton btnThem, btnCapNhat, btnXoa;

    private Ghe_DAO gheDAO = new Ghe_DAO();
    private PhongChieu_DAO phongDAO = new PhongChieu_DAO();

    public ThemGhe_UI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. TOP PANEL: Chứa Form nhập liệu và Chọn phòng
        JPanel pnlControl = new JPanel(new BorderLayout(20, 20));
        pnlControl.setOpaque(false);

        // --- Chọn phòng ---
        JPanel pnlSelectRoom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSelectRoom.setOpaque(false);
        cmbPhong = new JComboBox<>();
        cmbPhong.setPreferredSize(new Dimension(250, 35));
        pnlSelectRoom.add(new JLabel("Chọn Phòng Chiếu: ") {{ setForeground(TEXT); }});
        pnlSelectRoom.add(cmbPhong);
        pnlControl.add(pnlSelectRoom, BorderLayout.NORTH);

        // --- Form nhập liệu (GridLayout 2 cột cho cân đối) ---
        JPanel pnlFields = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlFields.setOpaque(false);
        pnlFields.setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(15, 15, 15, 15)));

        txtMaGhe = createTextField("Mã ghế (Tự động):", pnlFields, false);
        txtHang = createTextField("Hàng (A, B...):", pnlFields, true);
        txtSoGhe = createTextField("Số ghế (1-10):", pnlFields, true);

        cmbLoaiGhe = createComboBox("Loại ghế:", new String[]{"Thường", "VIP", "Đôi"}, pnlFields);
        cmbTrangThai = createComboBox("Trạng thái:", new String[]{"Còn trống", "Đã đặt"}, pnlFields);

        // --- Nút bấm ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlButtons.setOpaque(false);
        btnThem = createButton("Thêm ghế", new Color(33, 150, 243));
        btnCapNhat = createButton("Cập nhật", new Color(255, 193, 7));
        btnXoa = createButton("Xóa ghế", new Color(231, 76, 60));
        pnlButtons.add(btnThem); pnlButtons.add(btnCapNhat); pnlButtons.add(btnXoa);

        pnlControl.add(pnlFields, BorderLayout.CENTER);
        pnlControl.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlControl, BorderLayout.NORTH);

        // 2. CENTER PANEL: Sơ đồ ghế 10 ghế/hàng cố định
        pnlSoDoGhe = new JPanel(new GridLayout(0, 10, 10, 10)); // GridLayout 10 cột cố định
        pnlSoDoGhe.setBackground(CARD);
        pnlSoDoGhe.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(pnlSoDoGhe);
        scroll.setBorder(new TitledBorder(new LineBorder(BORDER), "SƠ ĐỒ GHẾ THỰC TẾ (10 GHẾ/HÀNG)", 0, 0, null, TEXT));
        scroll.getViewport().setBackground(CARD);
        add(scroll, BorderLayout.CENTER);

        // 3. BOTTOM PANEL: Chú thích (Legend)
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        pnlBot.setOpaque(false);
        pnlBot.add(taoLegend("Đã đặt", COLOR_DA_DAT));
        pnlBot.add(taoLegend("Bạn chọn", COLOR_CHON));
        pnlBot.add(taoLegend("Thường", COLOR_THUONG));
        pnlBot.add(taoLegend("VIP", COLOR_VIP));
        pnlBot.add(taoLegend("Đôi", COLOR_DOI));
        add(pnlBot, BorderLayout.SOUTH);

        // --- Gán sự kiện ---
        loadPhong();
        taoMaTuDong();
        cmbPhong.addActionListener(e -> veSoDoTheoDB());
        btnThem.addActionListener(e -> xuLyThem());
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnXoa.addActionListener(e -> xuLyXoa());
    }

    private void veSoDoTheoDB() {
        pnlSoDoGhe.removeAll();
        PhongChieu pc = (PhongChieu) cmbPhong.getSelectedItem();
        if (pc == null) return;

        // Lấy danh sách thực tế và đưa vào Map để truy xuất nhanh theo vị trí
        List<Ghe> ds = gheDAO.timGheTheoPhong(pc.getMaPhong());
        Map<String, Ghe> mapGhe = new HashMap<>();
        for (Ghe g : ds) {
            mapGhe.put(g.getHang().toUpperCase() + g.getSoGhe(), g);
        }

        // Vẽ cố định các hàng A, B, C...
        char row = 'A';
        for (int i = 0; i < 8; i++) { // Giả sử hiển thị 8 hàng A->H
            for (int j = 1; j <= 10; j++) { // 10 ghế mỗi hàng
                String key = row + String.valueOf(j);
                if (mapGhe.containsKey(key)) {
                    Ghe g = mapGhe.get(key);
                    JButton btn = new JButton(key);
                    styleSeatButton(btn, g);
                    btn.addActionListener(e -> loadGheLenForm(g));
                    pnlSoDoGhe.add(btn);
                } else {
                    // Nếu vị trí này chưa có ghế trong DB, hiện ô trống hoặc nút ẩn
                    pnlSoDoGhe.add(new JLabel("")); 
                }
            }
            row++;
        }
        pnlSoDoGhe.revalidate(); pnlSoDoGhe.repaint();
    }

    private void styleSeatButton(JButton btn, Ghe g) {
        btn.setPreferredSize(new Dimension(55, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);

        if (g.getTrangThai().equalsIgnoreCase("Đã đặt")) {
            btn.setBackground(COLOR_DA_DAT);
        } else {
            if (g.getLoaiGhe().equalsIgnoreCase("VIP")) btn.setBackground(COLOR_VIP);
            else if (g.getLoaiGhe().equalsIgnoreCase("Đôi")) btn.setBackground(COLOR_DOI);
            else btn.setBackground(COLOR_THUONG);
        }
    }

    private void loadGheLenForm(Ghe g) {
        txtMaGhe.setText(g.getMaGhe());
        txtHang.setText(g.getHang());
        txtSoGhe.setText(String.valueOf(g.getSoGhe()));
        cmbLoaiGhe.setSelectedItem(g.getLoaiGhe());
        cmbTrangThai.setSelectedItem(g.getTrangThai());
    }

  

    private void xuLyCapNhat() {
        try {
            PhongChieu pc = (PhongChieu) cmbPhong.getSelectedItem();
            Ghe g = new Ghe(txtMaGhe.getText(), pc, txtHang.getText().toUpperCase(), 
                           Integer.parseInt(txtSoGhe.getText()), 
                           cmbLoaiGhe.getSelectedItem().toString(), 
                           cmbTrangThai.getSelectedItem().toString());
            if (gheDAO.capNhatGhe(g)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                veSoDoTheoDB();
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi cập nhật!"); }
    }

    private void xuLyXoa() {
        if (gheDAO.xoaGhe(txtMaGhe.getText())) {
            JOptionPane.showMessageDialog(this, "Đã xóa ghế!");
            veSoDoTheoDB(); taoMaTuDong();
        }
    }

    private void taoMaTuDong() {
        int count = gheDAO.demSoLuongGhe();
        txtMaGhe.setText(String.format("G%04d", count + 1));
    }

    // ===== CÁC HÀM TIỆN ÍCH UI =====
    private JTextField createTextField(String label, JPanel pnl, boolean editable) {
        pnl.add(new JLabel(label) {{ setForeground(TEXT); }});
        JTextField txt = new JTextField();
        txt.setBackground(INPUT_BG); txt.setForeground(TEXT); txt.setCaretColor(TEXT);
        txt.setEditable(editable);
        txt.setBorder(new LineBorder(BORDER));
        pnl.add(txt);
        return txt;
    }

    private JComboBox<String> createComboBox(String label, String[] items, JPanel pnl) {
        pnl.add(new JLabel(label) {{ setForeground(TEXT); }});
        JComboBox<String> cmb = new JComboBox<>(items);
        cmb.setBackground(INPUT_BG); cmb.setForeground(TEXT);
        pnl.add(cmb);
        return cmb;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color); btn.setForeground(TEXT);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private void loadPhong() {
        for (PhongChieu p : phongDAO.docDanhSachPhongChieu()) cmbPhong.addItem(p);
    }

    private JPanel taoLegend(String t, Color c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); p.setOpaque(false);
        JLabel b = new JLabel() {{ setOpaque(true); setBackground(c); setPreferredSize(new Dimension(20, 20)); }};
        p.add(b); p.add(new JLabel(t) {{ setForeground(TEXT); }});
        return p;
    }
    private void xuLyThem() {
        // 1. Khởi tạo Dialog phong cách Dark Theme (Đã sửa scope để Action có thể truy cập)
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "THÊM GHẾ MỚI", true);
        dialog.getContentPane().setBackground(new Color(48, 52, 56));
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 550);
        dialog.setLocationRelativeTo(this);

        // Tiêu đề
        JLabel lblTitle = new JLabel("NHẬP THÔNG TIN GHẾ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        dialog.add(lblTitle, BorderLayout.NORTH);

        // 2. Panel Form nhập liệu
        JPanel pnlForm = new JPanel(new GridLayout(5, 1, 0, 15));
        pnlForm.setBackground(new Color(31, 32, 44));
        pnlForm.setBorder(new CompoundBorder(new LineBorder(new Color(70, 72, 87)), new EmptyBorder(20, 30, 20, 30)));

        // Mã ghế tự động gợi ý lúc mở Dialog
        String maMaxHienTai = gheDAO.layMaLonNhat();
        int nextNum = (maMaxHienTai != null) ? Integer.parseInt(maMaxHienTai.substring(1)) + 1 : 1;
        String maGoiY = String.format("G%04d", nextNum);

        JTextField txtNewMa = createDialogInput(pnlForm, "Mã ghế (Tự động):", maGoiY, false);
        JTextField txtNewHang = createDialogInput(pnlForm, "Hàng (A, B...):", "", true);
        JTextField txtNewSo = createDialogInput(pnlForm, "Số ghế (Nhập số hoặc [số lượng]):", "", true);
        
        JComboBox<String> cbLoai = new JComboBox<>(new String[]{"Thường", "VIP", "Đôi"});
        styleComboBox(pnlForm, "Loại ghế:", cbLoai);
        
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Còn trống", "Đã đặt"});
        styleComboBox(pnlForm, "Trạng thái:", cbTrangThai);

        // 3. Nút Xác Nhận (Gộp chung sự kiện)
        JButton btnConfirm = new JButton("XÁC NHẬN LƯU");
        btnConfirm.setBackground(new Color(33, 150, 243));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setPreferredSize(new Dimension(0, 45));
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- SỰ KIỆN LƯU THÔNG MINH ---
        btnConfirm.addActionListener(e -> {
            try {
                PhongChieu pc = (PhongChieu) cmbPhong.getSelectedItem();
                String hangNhap = txtNewHang.getText().trim().toUpperCase();
                String giaTriNhap = txtNewSo.getText().trim();

                if (pc == null || hangNhap.isEmpty() || giaTriNhap.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                int soLuongCanThem = 1;
                int soGheBatDau = 1;

                // Kiểm tra kiểu nhập [n] hoặc số thường
                if (giaTriNhap.startsWith("[") && giaTriNhap.endsWith("]")) {
                    soLuongCanThem = Integer.parseInt(giaTriNhap.substring(1, giaTriNhap.length() - 1));
                    soGheBatDau = 1; // Thêm hàng loạt mặc định bắt đầu từ ghế số 1
                } else {
                    soLuongCanThem = 1;
                    soGheBatDau = Integer.parseInt(giaTriNhap);
                }

                char hangHienTai = hangNhap.charAt(0);
                int demThanhCong = 0;

                // Vòng lặp thêm ghế và tự động nhảy hàng
                for (int i = 0; i < soLuongCanThem; i++) {
                    // Nếu số ghế > 10 thì nhảy sang hàng tiếp theo (A11 -> B1)
                    if (soGheBatDau > 10) {
                        soGheBatDau = 1;
                        hangHienTai++; 
                    }

                    // Lấy mã PK mới nhất trong từng vòng lặp để không bị trùng[cite: 1]
                    String maMaxVongLap = gheDAO.layMaLonNhat();
                    int nextId = (maMaxVongLap != null) ? Integer.parseInt(maMaxVongLap.substring(1)) + 1 : 1;
                    String maMoi = String.format("G%04d", nextId);

                    Ghe g = new Ghe(
                        maMoi, pc, String.valueOf(hangHienTai), 
                        soGheBatDau, cbLoai.getSelectedItem().toString(), 
                        cbTrangThai.getSelectedItem().toString()
                    );

                    if (gheDAO.themGhe(g)) {
                        demThanhCong++;
                    }
                    soGheBatDau++;
                }

                if (demThanhCong > 0) {
                    JOptionPane.showMessageDialog(dialog, "Đã thêm thành công " + demThanhCong + " ghế!");
                    dialog.dispose();
                    veSoDoTheoDB(); 
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi định dạng: Nhập số hoặc [số lượng]!");
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnConfirm, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    
    // --- Hàm hỗ trợ tạo Input phong cách Dark ---
    private JTextField createDialogInput(JPanel p, String lb, String val, boolean ed) {
        JPanel w = new JPanel(new BorderLayout(5, 5)); w.setOpaque(false);
        JLabel l = new JLabel(lb); l.setForeground(new Color(180, 180, 180));
        JTextField t = new JTextField(val);
        t.setEditable(ed); t.setBackground(INPUT_BG); t.setForeground(TEXT);
        t.setCaretColor(TEXT); t.setBorder(new LineBorder(BORDER));
        w.add(l, BorderLayout.NORTH); w.add(t, BorderLayout.CENTER);
        p.add(w); return t;
    }

    private void styleComboBox(JPanel p, String lb, JComboBox c) {
        JPanel w = new JPanel(new BorderLayout(5, 5)); w.setOpaque(false);
        JLabel l = new JLabel(lb); l.setForeground(new Color(180, 180, 180));
        c.setBackground(INPUT_BG); c.setForeground(TEXT);
        w.add(l, BorderLayout.NORTH); w.add(c, BorderLayout.CENTER);
        p.add(w);
    }
}