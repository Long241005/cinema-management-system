package ui.phim;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Properties;
import dao.Phim_DAO;
import entity.Phim;
import org.jdatepicker.impl.*;

public class ThemPhim_UI extends JPanel {
    private JTextField txtMa, txtTen, txtDaoDien, txtTheLoai, txtThoiLuong;
    private JDatePickerImpl datePicker; 
    private JTextArea txtMoTa;
    private JLabel lblPoster;
    private String tenFileAnhLuuTrongDB = ""; 
    private File fileAnhGoc = null; // Lưu file người dùng vừa chọn để xử lý khi nhấn Lưu
    private Phim_DAO phimDAO = new Phim_DAO();

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

        txtMa = taoHangInput(pnlLeft, "Mã Phim:", 0, gbcForm, fontLabel);
        txtTen = taoHangInput(pnlLeft, "Tên Phim:", 1, gbcForm, fontLabel);
        txtTheLoai = taoHangInput(pnlLeft, "Thể loại:", 2, gbcForm, fontLabel);
        txtDaoDien = taoHangInput(pnlLeft, "Đạo diễn:", 3, gbcForm, fontLabel);
        txtThoiLuong = taoHangInput(pnlLeft, "Thời lượng (phút):", 4, gbcForm, fontLabel);
        
        // Ngày khởi chiếu
        gbcForm.gridx = 0; gbcForm.gridy = 5; gbcForm.weightx = 0.2;
        pnlLeft.add(new JLabel("Ngày khởi chiếu:") {{ setForeground(Color.LIGHT_GRAY); setFont(fontLabel); }}, gbcForm);
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today"); p.put("text.month", "Month"); p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        pnlLeft.add(datePicker, gbcForm);

        // Mô tả
        gbcForm.gridx = 0; gbcForm.gridy = 6; gbcForm.weightx = 0.2;
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

        // NÚT XÁC NHẬN
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

        // GÁN SỰ KIỆN ENTER CHO CÁC Ô NHẬP
        ganSuKienEnter(btnLuu);
    }

    private void ganSuKienEnter(JButton btnTarget) {
        // Danh sách các ô nhập theo thứ tự ưu tiên
        JTextField[] fields = {txtMa, txtTen, txtTheLoai, txtDaoDien, txtThoiLuong};
        
        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].addActionListener(e -> {
                // Nếu chưa phải ô cuối cùng thì nhảy xuống ô kế tiếp
                if (index < fields.length - 1) {
                    fields[index + 1].requestFocus();
                } else {
                    // Nếu là ô cuối (Thời lượng), thực hiện nhấn nút Lưu
                    btnTarget.doClick();
                }
            });
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
            // Preview tạm thời
            ImageIcon icon = new ImageIcon(fileAnhGoc.getAbsolutePath());
            lblPoster.setIcon(new ImageIcon(icon.getImage().getScaledInstance(320, 450, Image.SCALE_SMOOTH)));
            lblPoster.setText("");
        }
    }

    private void xuLyLuuPhim() {
        try {
            String maPhim = txtMa.getText().trim();
            if (maPhim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phim trước!");
                return;
            }

            // 1. XỬ LÝ COPY ẢNH VÀO THƯ MỤC IMG VỚI TÊN [MAPHIM].JPG
            if (fileAnhGoc != null) {
                tenFileAnhLuuTrongDB = maPhim + ".jpg";
                Path dest = Paths.get("src/img/" + tenFileAnhLuuTrongDB);
                Files.createDirectories(dest.getParent()); // Đảm bảo thư mục img tồn tại
                Files.copy(fileAnhGoc.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            }

            // 2. LẤY DỮ LIỆU CÒN LẠI
            java.util.Date dateSelected = (java.util.Date) datePicker.getModel().getValue();
            LocalDate ngay = dateSelected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Phim p = new Phim(maPhim, txtTen.getText().trim(), txtDaoDien.getText().trim(), 
                             txtTheLoai.getText().trim(), Integer.parseInt(txtThoiLuong.getText().trim()), 
                             ngay, txtMoTa.getText().trim(), tenFileAnhLuuTrongDB);

            if (phimDAO.themPhim(p)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công! Ảnh đã được lưu là " + tenFileAnhLuuTrongDB);
                xoaTrang();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: Kiểm tra dữ liệu hoặc quyền ghi file vào thư mục img.");
            e.printStackTrace();
        }
    }

    private void xoaTrang() {
        txtMa.setText(""); txtTen.setText(""); txtTheLoai.setText("");
        txtDaoDien.setText(""); txtThoiLuong.setText(""); txtMoTa.setText("");
        datePicker.getModel().setValue(null);
        lblPoster.setIcon(null); lblPoster.setText("CHƯA CÓ ẢNH");
        fileAnhGoc = null;
    }
}