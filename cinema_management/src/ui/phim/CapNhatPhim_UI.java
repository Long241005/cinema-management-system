package ui.phim;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import entity.Phim;
import entity.TheLoai;
import dao.Phim_DAO;
import dao.TheLoai_DAO;

public class CapNhatPhim_UI extends JPanel {

    // ========== HẰNG SỐ MÀU SẮC ==========
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_THANH_TIM_KIEM = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_VIEN_INPUT = new Color(70, 72, 87);
    private final Color MAU_VANG = new Color(255, 193, 7);

    // ========== COMPONENTS ==========
    private JTextField txtMa, txtTen, txtDaoDien, txtNgay;
    private JComboBox<TheLoai> cmbTheLoai; // Đã đổi sang đối tượng
    private JSpinner spnThoiLuong; // Đã đổi sang Spinner điều chỉnh số
    private JLabel lblPoster;
    private JTable table;
    private DefaultTableModel tableModel;
    private Phim_DAO phimDAO;
    private TheLoai_DAO theLoaiDAO;
    private String tenFileAnhHienTai = "";

    public CapNhatPhim_UI() {
        phimDAO = new Phim_DAO();
        theLoaiDAO = new TheLoai_DAO();
        khoiTaoGiaoDien();
        loadDataTheLoai(); // Nạp danh sách thể loại vào ComboBox
        lamMoiBang();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- 1. PHẦN TRÊN: FORM NHẬP LIỆU & POSTER ---
        JPanel pnlTopInfo = new JPanel(new BorderLayout(40, 0));
        pnlTopInfo.setOpaque(false);

        // Ảnh bên trái
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setOpaque(false);
        lblPoster = new JLabel("CHỌN PHIM ĐỂ XEM ẢNH", SwingConstants.CENTER);
        lblPoster.setPreferredSize(new Dimension(180, 250));
        lblPoster.setBorder(new LineBorder(MAU_VIEN_INPUT, 1));
        lblPoster.setForeground(Color.GRAY);
        pnlLeft.add(lblPoster, BorderLayout.CENTER);

        // Form nhập liệu ở giữa (Grid 3 hàng 2 cột)
        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlForm.setOpaque(false);
        
        txtMa = taoInput(pnlForm, "Mã phim:"); txtMa.setEditable(false);
        txtTen = taoInput(pnlForm, "Tên phim:");
        txtDaoDien = taoInput(pnlForm, "Đạo diễn:");

        // Thể loại: ComboBox đối tượng
        cmbTheLoai = new JComboBox<>();
        cmbTheLoai.setBackground(MAU_THANH_TIM_KIEM);
        cmbTheLoai.setForeground(MAU_CHU);
        pnlForm.add(taoWrapComponent("Thể loại:", cmbTheLoai));

        // Thời lượng: Spinner điều chỉnh lên xuống[cite: 2]
        spnThoiLuong = new JSpinner(new SpinnerNumberModel(120, 1, 500, 1));
        pnlForm.add(taoWrapComponent("Thời lượng (phút):", spnThoiLuong));

        txtNgay = taoInput(pnlForm, "Ngày khởi chiếu (dd/MM/yyyy):");

        // Nút hành động
        JPanel pnlRightBtns = new JPanel();
        pnlRightBtns.setLayout(new BoxLayout(pnlRightBtns, BoxLayout.Y_AXIS));
        pnlRightBtns.setOpaque(false);
        JButton btnUpdate = taoNutHanhDong("Cập nhật", MAU_VANG);
        JButton btnDelete = taoNutHanhDong("     Xóa    ", new Color(244, 67, 54));
        
        btnUpdate.addActionListener(e -> xuLyCapNhat());
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
        pnlBottom.add(scroll, BorderLayout.CENTER);

        add(pnlTopInfo, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);

        // Sự kiện click bảng
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

    private void loadDataTheLoai() {
        cmbTheLoai.removeAllItems();
        List<TheLoai> ds = theLoaiDAO.getAllTheLoai();
        for (TheLoai tl : ds) cmbTheLoai.addItem(tl);
    }

    public void lamMoiBang() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Phim> ds = phimDAO.docDanhSachPhim();
        for (Phim p : ds) {
            tableModel.addRow(new Object[]{
                p.getMaPhim(), 
                p.getTenPhim(), 
                p.getDaoDien(), 
                p.getTheLoai().getTenTheLoai(), // Hiển thị tên thể loại từ đối tượng[cite: 2]
                p.getThoiLuong(), 
                p.getNgayKhoiChieu().format(formatter)
            });
        }
    }

    public void hienThiThongTinPhim(Phim p) {
        if (p == null) return;
        
        txtMa.setText(p.getMaPhim());
        txtTen.setText(p.getTenPhim());
        txtDaoDien.setText(p.getDaoDien());
        
        // Gán đối tượng Thể loại vào ComboBox[cite: 2]
        cmbTheLoai.setSelectedItem(p.getTheLoai()); 
        
        // Gán giá trị vào Spinner[cite: 2]
        spnThoiLuong.setValue(p.getThoiLuong());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtNgay.setText(p.getNgayKhoiChieu().format(formatter));
        tenFileAnhHienTai = p.getDuongDanAnh();

        // Load ảnh poster
        try {
            java.net.URL imgURL = getClass().getResource("/img/" + p.getDuongDanAnh());
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
                lblPoster.setIcon(new ImageIcon(img));
                lblPoster.setText("");
            } else {
                lblPoster.setIcon(null);
                lblPoster.setText("Không tìm thấy ảnh");
            }
        } catch (Exception ex) { lblPoster.setText("Lỗi load ảnh"); }
    }

    private void xuLyCapNhat() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim để cập nhật!");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ngay = LocalDate.parse(txtNgay.getText().trim(), formatter);
            
            // Lấy đối tượng Thể loại trực tiếp từ ComboBox[cite: 2]
            TheLoai tl = (TheLoai) cmbTheLoai.getSelectedItem();

            Phim p = new Phim(
                ma,
                txtTen.getText().trim(),
                txtDaoDien.getText().trim(),
                tl, // Truyền đối tượng thay vì String[cite: 2]
                (int) spnThoiLuong.getValue(),
                ngay,
                "", 
                tenFileAnhHienTai
            );

            if (phimDAO.capNhatPhim(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công phim " + ma);
                lamMoiBang();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage());
        }
    }

    private void xuLyXoa() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) return;

        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa phim " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (phimDAO.xoaPhim(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                lamMoiBang();
                lblPoster.setIcon(null);
                lblPoster.setText("CHỌN PHIM ĐỂ XEM ẢNH");
            }
        }
    }

    // ========== HÀM HỖ TRỢ GIAO DIỆN ==========
    private JPanel taoWrapComponent(String label, JComponent comp) {
        JPanel wrap = new JPanel(new BorderLayout(5, 5));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setForeground(MAU_CHU);
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(comp, BorderLayout.CENTER);
        return wrap;
    }

    private JTextField taoInput(JPanel pnl, String label) {
        JTextField txt = new JTextField();
        txt.setBackground(MAU_THANH_TIM_KIEM); txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU); txt.setBorder(new LineBorder(MAU_VIEN_INPUT));
        pnl.add(taoWrapComponent(label, txt));
        return txt;
    }

    private JButton taoNutHanhDong(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg); b.setForeground(MAU_CHU);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(130, 45));
        return b;
    }

    private void thietKeBang() {
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);
        table.setRowHeight(35);
        table.setSelectionBackground(MAU_VANG);
        table.setSelectionForeground(Color.BLACK);
    }
}