package ui.phim;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.time.format.DateTimeFormatter;

import entity.Phim;
import dao.Phim_DAO;

public class CapNhatPhim_UI extends JPanel {

    // ========== HẰNG SỐ MÀU SẮC (Đồng bộ với hệ thống) ==========
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_THANH_TIM_KIEM = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_VIEN_INPUT = new Color(70, 72, 87);
    private final Color MAU_VANG = new Color(255, 193, 7);

    // ========== COMPONENTS ==========
    private JTextField txtMa, txtTen, txtTheLoai, txtDaoDien, txtThoiLuong, txtNgay;
    private JLabel lblPoster;
    private JTable table;
    private DefaultTableModel tableModel;
    private Phim_DAO phimDAO;
    private String tenFileAnhHienTai = ""; // Lưu tên file để dùng khi Update

    public CapNhatPhim_UI() {
        phimDAO = new Phim_DAO();
        khoiTaoGiaoDien();
        lamMoiBang();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- 1. PHẦN TRÊN: FORM NHẬP LIỆU & POSTER ---
        JPanel pnlTopInfo = new JPanel(new BorderLayout(40, 0));
        pnlTopInfo.setOpaque(false);

        // Ảnh bên trái (Tự động load từ /img/)
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setOpaque(false);
        lblPoster = new JLabel("CHỌN PHIM ĐỂ XEM ẢNH", SwingConstants.CENTER);
        lblPoster.setPreferredSize(new Dimension(180, 250)); // Tỉ lệ poster đứng
        lblPoster.setBorder(new LineBorder(MAU_VIEN_INPUT, 1));
        lblPoster.setForeground(Color.GRAY);
        pnlLeft.add(lblPoster, BorderLayout.CENTER);

        // Form nhập liệu ở giữa
        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlForm.setOpaque(false);
        txtMa = taoInput(pnlForm, "Mã phim:"); txtMa.setEditable(false);
        txtTen = taoInput(pnlForm, "Tên phim:");
        txtDaoDien = taoInput(pnlForm, "Đạo diễn:");
        txtTheLoai = taoInput(pnlForm, "Thể loại:");
        txtThoiLuong = taoInput(pnlForm, "Thời lượng (phút):");
        txtNgay = taoInput(pnlForm, "Ngày khởi chiếu (yyyy-MM-dd):");
        

        // Nút hành động bên phải
        JPanel pnlRightBtns = new JPanel();
        pnlRightBtns.setLayout(new BoxLayout(pnlRightBtns, BoxLayout.Y_AXIS));
        pnlRightBtns.setOpaque(false);
        JButton btnUpdate = taoNutHanhDong("Cập nhật", MAU_VANG);
        JButton btnDelete = taoNutHanhDong("     Xóa    ", new Color(244, 67, 54));
        
        btnUpdate.addActionListener(e -> xuLyCapNhat());

        // --- GÁN SỰ KIỆN NÚT XÓA ---
        btnDelete.addActionListener(e -> xuLyXoa());
        
        pnlRightBtns.add(Box.createVerticalGlue());
        pnlRightBtns.add(btnUpdate);
        pnlRightBtns.add(Box.createVerticalStrut(15));
        pnlRightBtns.add(btnDelete);
        pnlRightBtns.add(Box.createVerticalGlue());

        pnlTopInfo.add(pnlLeft, BorderLayout.WEST);
        pnlTopInfo.add(pnlForm, BorderLayout.CENTER);
        pnlTopInfo.add(pnlRightBtns, BorderLayout.EAST);

        // --- 2. PHẦN DƯỚI: BẢNG DANH SÁCH ---
        JPanel pnlBottom = new JPanel(new BorderLayout(0, 10));
        pnlBottom.setOpaque(false);

        String[] columns = {"Mã phim", "Tên phim", "Đạo diễn", "Thể loại", "Thời lượng", "Ngày chiếu"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        thietKeBang();
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(0, 250));
        tuyChinhScrollBar(scroll);
        
        scroll.getViewport().setBackground(new Color(31, 32, 44)); // Cùng màu với table
        scroll.setBorder(BorderFactory.createEmptyBorder());

        pnlBottom.add(new JLabel("DANH SÁCH PHIM ĐANG KHỞI CHIẾU") {{ 
            setForeground(MAU_NHAN_XAM); 
            setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        }}, BorderLayout.NORTH);
        pnlBottom.add(scroll, BorderLayout.CENTER);

        add(pnlTopInfo, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);

        // --- SỰ KIỆN CLICK BẢNG: LOAD ẢNH TỰ ĐỘNG ---
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String ma = tableModel.getValueAt(row, 0).toString();
                    hienThiThongTinPhim(phimDAO.timPhimTheoMa(ma));
                }
            }
        });
    }

    public void lamMoiBang() {
        tableModel.setRowCount(0);
        List<Phim> ds = phimDAO.docDanhSachPhim();
        for (Phim p : ds) {
            tableModel.addRow(new Object[]{
                p.getMaPhim(), p.getTenPhim(), p.getDaoDien(), 
                p.getTheLoai(), p.getThoiLuong(), p.getNgayKhoiChieu()
            });
        }
    }

    /**
     * Hàm này tự động tìm ảnh trong folder img dựa trên tên file lưu trong DB
     */
    public void hienThiThongTinPhim(Phim p) {
        if (p == null) return;
        
        txtMa.setText(p.getMaPhim());
        txtTen.setText(p.getTenPhim());
        txtDaoDien.setText(p.getDaoDien());
        txtTheLoai.setText(p.getTheLoai());
        txtThoiLuong.setText(String.valueOf(p.getThoiLuong()));
      //  txtNgay.setText(p.getNgayKhoiChieu().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtNgay.setText(p.getNgayKhoiChieu().format(formatter));
        tenFileAnhHienTai = p.getDuongDanAnh();

        // Xử lý load ảnh vuông từ thư mục resources /img/
        try {
            java.net.URL imgURL = getClass().getResource("/img/" + p.getDuongDanAnh());
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                // Scale ảnh vuông vức để vừa với khung 180x250
                Image img = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
                lblPoster.setIcon(new ImageIcon(img));
                lblPoster.setText("");
            } else {
                lblPoster.setIcon(null);
                lblPoster.setText("Không tìm thấy: " + p.getDuongDanAnh());
            }
        } catch (Exception ex) {
            lblPoster.setText("Lỗi load ảnh");
        }
    }

    private JTextField taoInput(JPanel pnl, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 5));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setForeground(MAU_CHU);
        JTextField txt = new JTextField();
        txt.setBackground(MAU_THANH_TIM_KIEM); txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU); txt.setBorder(new LineBorder(MAU_VIEN_INPUT));
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(txt, BorderLayout.CENTER);
        pnl.add(wrap);
        return txt;
    }

    private JButton taoNutHanhDong(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg); b.setForeground(MAU_CHU);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(130, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void thietKeBang() {
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(MAU_THANH_TIM_KIEM);
        table.getTableHeader().setForeground(MAU_CHU);
        table.setSelectionBackground(MAU_VANG);
        table.setSelectionForeground(Color.BLACK);
    }

    private void tuyChinhScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                this.thumbColor = MAU_NHAN_XAM; this.trackColor = MAU_NEN;
            }
        });
    }
    private void xuLyXoa() {
        String ma = txtMa.getText().trim(); // Lấy mã phim đang hiển thị trên form
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xóa!");
            return;
        }

        // Hỏi xác nhận trước khi xóa cho chắc ăn
        int opt = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa phim " + ma + " không?", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (opt == JOptionPane.YES_OPTION) {
            // GỌI HÀM TỪ DAO Ở ĐÂY
            if (phimDAO.xoaPhim(ma)) { 
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
               // xoaTrangForm(); // Xóa sạch các ô nhập liệu
                lamMoiBang();   // Load lại danh sách phim dưới bảng
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Phim có thể đang được sử dụng ở bảng khác.");
            }
        }
    }
    private void xuLyCapNhat() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim từ bảng để cập nhật!");
            return;
        }

        try {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String ngayStr = txtNgay.getText().trim();
            java.time.LocalDate ngay = java.time.LocalDate.parse(ngayStr, formatter);

            // 2. Tạo đối tượng Phim để cập nhật
            Phim p = new Phim(
                ma,
                txtTen.getText().trim(),
                txtDaoDien.getText().trim(),
                txtTheLoai.getText().trim(),
                Integer.parseInt(txtThoiLuong.getText().trim()),
                ngay,
                "", // Có thể lấy thêm từ JTextArea moTa nếu bạn có
                tenFileAnhHienTai // Giữ nguyên tên file ảnh cũ trong Database
            );

            // 3. Gọi DAO để thực thi lệnh UPDATE
            if (phimDAO.capNhatPhim(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công phim " + ma);
                lamMoiBang(); // Tải lại bảng để thấy dữ liệu mới
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Vui lòng nhập đúng định dạng dd/MM/yyyy (VD: 28/04/2026)");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là con số!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}