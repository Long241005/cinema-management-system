package ui.phim;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Properties;
import dao.Phim_DAO;
import dao.TheLoai_DAO; 
import entity.Phim;
import entity.TheLoai; 
import org.jdatepicker.impl.*;

public class ThemPhim_UI extends JPanel {
    private JTextField txtMa, txtTen, txtDaoDien;
    private JComboBox<TheLoai> cmbTheLoai; 
    private JSpinner spnThoiLuong; 
    private JDatePickerImpl datePicker; 
    private JTextArea txtMoTa;
    private JLabel lblPoster;
    private File fileAnhGoc = null; 
    
    private Phim_DAO phimDAO = new Phim_DAO();
    private TheLoai_DAO theLoaiDAO = new TheLoai_DAO();

    public ThemPhim_UI() {
        setLayout(new OverlayLayout(this));

        // --- LỚP 1: BACKGROUND ---
        JPanel pnlBackground = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                java.net.URL bgURL = getClass().getResource("/img/background.jpg");
                if (bgURL != null) {
                    Image img = new ImageIcon(bgURL).getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 210)); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // --- LỚP 2: NỘI DUNG CHÍNH ---
        JPanel pnlMain = new JPanel(new BorderLayout(0, 20));
        pnlMain.setOpaque(false);
        pnlMain.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblHeader = new JLabel("THÊM PHIM MỚI", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHeader.setForeground(Color.WHITE);
        pnlMain.add(lblHeader, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setOpaque(false);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1.0;

        // --- CỘT TRÁI: FORM NHẬP ---
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setOpaque(false);
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(12, 10, 12, 20);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 16);

        // 1. Mã Phim (Tự động)
        txtMa = taoHangInput(pnlLeft, "Mã Phim:", 0, gbcForm, fontLabel);
        txtMa.setEditable(false);
        txtMa.setText(phimDAO.phatSinhMaPhimTuDong()); 

        // 2. Tên Phim[cite: 11]
        txtTen = taoHangInput(pnlLeft, "Tên Phim:", 1, gbcForm, fontLabel);

        // 3. Thể Loại[cite: 11]
        gbcForm.gridy = 2; gbcForm.gridx = 0; gbcForm.weightx = 0.2;
        pnlLeft.add(new JLabel("Thể loại:") {{ setForeground(Color.LIGHT_GRAY); setFont(fontLabel); }}, gbcForm);
        
        JPanel pnlTheLoaiGroup = new JPanel(new BorderLayout(10, 0));
        pnlTheLoaiGroup.setOpaque(false);
        cmbTheLoai = new JComboBox<>();
        loadDataTheLoai(); 
        JButton btnThemNhanhTL = new JButton("+");
        btnThemNhanhTL.addActionListener(e -> xuLyThemTheLoaiNhanh());
        
        pnlTheLoaiGroup.add(cmbTheLoai, BorderLayout.CENTER);
        pnlTheLoaiGroup.add(btnThemNhanhTL, BorderLayout.EAST);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        pnlLeft.add(pnlTheLoaiGroup, gbcForm);

        // 4. Đạo diễn[cite: 11]
        txtDaoDien = taoHangInput(pnlLeft, "Đạo diễn:", 3, gbcForm, fontLabel);

        // 5. Thời lượng[cite: 11]
        gbcForm.gridy = 4; gbcForm.gridx = 0; gbcForm.weightx = 0.2;
        pnlLeft.add(new JLabel("Thời lượng (phút):") {{ setForeground(Color.LIGHT_GRAY); setFont(fontLabel); }}, gbcForm);
        spnThoiLuong = new JSpinner(new SpinnerNumberModel(120, 1, 600, 1));
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        pnlLeft.add(spnThoiLuong, gbcForm);
        
        // 6. Ngày khởi chiếu[cite: 11]
        gbcForm.gridy = 5; gbcForm.gridx = 0; gbcForm.weightx = 0.2;
        pnlLeft.add(new JLabel("Ngày khởi chiếu:") {{ setForeground(Color.LIGHT_GRAY); setFont(fontLabel); }}, gbcForm);
        UtilDateModel dateModel = new UtilDateModel();
        Properties pr = new Properties();
        pr.put("text.today", "Today"); pr.put("text.month", "Month"); pr.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, pr);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        pnlLeft.add(datePicker, gbcForm);

        // 7. Mô tả[cite: 11]
        gbcForm.gridy = 6; gbcForm.gridx = 0; gbcForm.weightx = 0.2;
        pnlLeft.add(new JLabel("Mô tả phim:") {{ setForeground(Color.LIGHT_GRAY); setFont(fontLabel); }}, gbcForm);
        txtMoTa = new JTextArea(5, 20);
        txtMoTa.setBackground(new Color(60, 64, 68)); txtMoTa.setForeground(Color.WHITE);
        txtMoTa.setLineWrap(true);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        pnlLeft.add(new JScrollPane(txtMoTa), gbcForm);

        gbcMain.gridx = 0; gbcMain.weightx = 0.6;
        pnlContent.add(pnlLeft, gbcMain);

        // --- CỘT PHẢI: POSTER ---
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setOpaque(false);

        lblPoster = new JLabel("CHƯA CÓ ẢNH", SwingConstants.CENTER);
        lblPoster.setBorder(new LineBorder(Color.WHITE, 2));
        lblPoster.setPreferredSize(new Dimension(320, 450));
        lblPoster.setMaximumSize(new Dimension(320, 450));
        lblPoster.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPoster.setForeground(Color.WHITE);
        
        JButton btnChonAnh = new JButton("CHỌN ẢNH");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.addActionListener(e -> xuLyChonAnh());

        pnlRight.add(Box.createVerticalGlue());
        pnlRight.add(lblPoster);
        pnlRight.add(Box.createVerticalStrut(20));
        pnlRight.add(btnChonAnh);
        pnlRight.add(Box.createVerticalGlue());

        gbcMain.gridx = 1; gbcMain.weightx = 0.4;
        pnlContent.add(pnlRight, gbcMain);
        pnlMain.add(pnlContent, BorderLayout.CENTER);

        // NÚT XÁC NHẬN[cite: 11]
        JButton btnLuu = new JButton("XÁC NHẬN THÊM PHIM");
        btnLuu.setBackground(new Color(76, 175, 80));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLuu.setPreferredSize(new Dimension(280, 50));
        
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setOpaque(false);
        pnlSouth.add(btnLuu);
        btnLuu.addActionListener(e -> xuLyLuuPhim());
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);

        add(pnlMain);
        add(pnlBackground);
    }

    private void loadDataTheLoai() {
        cmbTheLoai.removeAllItems();
        for (TheLoai tl : theLoaiDAO.getAllTheLoai()) {
            cmbTheLoai.addItem(tl);
        }
    }

    private void xuLyThemTheLoaiNhanh() {
        String tenMoi = JOptionPane.showInputDialog(this, "Nhập tên thể loại mới:");
        if (tenMoi != null && !tenMoi.trim().isEmpty()) {
            TheLoai tl = new TheLoai("", tenMoi);
            if (theLoaiDAO.themTheLoai(tl)) {
                loadDataTheLoai();
                cmbTheLoai.setSelectedItem(tl);
            }
        }
    }

    private JTextField taoHangInput(JPanel p, String label, int row, GridBagConstraints gbc, Font f) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.2;
        p.add(new JLabel(label) {{ setForeground(Color.LIGHT_GRAY); setFont(f); }}, gbc);
        JTextField t = new JTextField();
        t.setBackground(new Color(60, 64, 68)); t.setForeground(Color.WHITE);
        t.setCaretColor(Color.WHITE);
        t.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1; gbc.weightx = 0.8;
        p.add(t, gbc);
        return t;
    }

    private void xuLyChonAnh() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileAnhGoc = fc.getSelectedFile();
            ImageIcon icon = new ImageIcon(fileAnhGoc.getAbsolutePath());
            lblPoster.setIcon(new ImageIcon(icon.getImage().getScaledInstance(320, 450, Image.SCALE_SMOOTH)));
            lblPoster.setText("");
        }
    }

    private void xuLyLuuPhim() {
        try {
            String maPhim = txtMa.getText().trim();
            String tenPhim = txtTen.getText().trim();
            if (tenPhim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phim!");
                return;
            }

            // 1. XỬ LÝ POSTER: Lưu vào cả src và bin để đồng bộ ngay lập tức[cite: 11]
            String tenAnh = "";
            if (fileAnhGoc != null) {
                tenAnh = maPhim + ".jpg";
                // Lưu vào src
                Path destSrc = Paths.get("src/img/" + tenAnh);
                Files.createDirectories(destSrc.getParent());
                Files.copy(fileAnhGoc.toPath(), destSrc, StandardCopyOption.REPLACE_EXISTING);
                
                // Lưu vào bin (thư mục thực thi) để load được ngay
                Path destBin = Paths.get("bin/img/" + tenAnh);
                if (Files.exists(destBin.getParent())) {
                    Files.copy(fileAnhGoc.toPath(), destBin, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // 2. LẤY DỮ LIỆU TỪ SPINNER VÀ COMBOBOX[cite: 11]
            TheLoai tl = (TheLoai) cmbTheLoai.getSelectedItem();
            int thoiLuong = (int) spnThoiLuong.getValue();

            // 3. LẤY NGÀY[cite: 11]
            java.util.Date dateSelected = (java.util.Date) datePicker.getModel().getValue();
            if (dateSelected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày khởi chiếu!");
                return;
            }
            LocalDate ngay = dateSelected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 4. TẠO ĐỐI TƯỢNG VÀ LƯU[cite: 11]
            Phim p = new Phim(maPhim, tenPhim, txtDaoDien.getText().trim(), 
                             tl, thoiLuong, ngay, txtMoTa.getText().trim(), tenAnh);

            if (phimDAO.themPhim(p)) {
                JOptionPane.showMessageDialog(this, "Thêm phim " + tenPhim + " thành công!");
                xoaTrang();
                // Cập nhật mã phim mới để tránh lỗi trùng khóa chính[cite: 11]
                txtMa.setText(phimDAO.phatSinhMaPhimTuDong()); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void xoaTrang() {
        txtTen.setText("");
        txtDaoDien.setText("");
        spnThoiLuong.setValue(120);
        txtMoTa.setText("");
        datePicker.getModel().setValue(null);
        lblPoster.setIcon(null); lblPoster.setText("CHƯA CÓ ẢNH");
        fileAnhGoc = null;
    }
}