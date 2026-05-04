package ui.phim;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import entity.Phim;
import entity.TheLoai;
import dao.Phim_DAO;
import dao.TheLoai_DAO;

public class TraCuuPhim_UI extends JPanel {

    private final Color MAU_NEN_TAB = new Color(48, 52, 56);
    private final Color MAU_THANH_TIM_KIEM = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU_CHUNG = Color.WHITE;
    private final Color MAU_VIEN_INPUT = new Color(70, 72, 87);
    private final Color MAU_VANG = new Color(255, 193, 7);

    private JPanel pnlGridPhim; 
    private JLabel lblSoLuong;
    private JTextField txtTimMa, txtTimTen;
    private JComboBox<Object> cmbTheLoai;
    private Phim_DAO phimDAO;
    private TheLoai_DAO theLoaiDAO;

    public TraCuuPhim_UI() {
        phimDAO = new Phim_DAO();
        theLoaiDAO = new TheLoai_DAO();
        setLayout(new BorderLayout());
        setBackground(MAU_NEN_TAB);

        JPanel panelChinh = new JPanel(new BorderLayout(0, 15));
        panelChinh.setBackground(MAU_NEN_TAB);
        panelChinh.setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. THANH ĐIỀU KHIỂI
        JPanel pnlDieuKhien = new JPanel(new BorderLayout(20, 0));
        pnlDieuKhien.setBackground(MAU_NEN_TAB);

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlTimKiem.setBackground(MAU_NEN_TAB);

        txtTimMa = new JTextField(8);
        txtTimTen = new JTextField(12);
        
        cmbTheLoai = new JComboBox<>();
        loadDataTheLoai();

        pnlTimKiem.add(taoWrapperInput("Mã phim:", txtTimMa, 120));
        pnlTimKiem.add(taoWrapperInput("Tên phim:", txtTimTen, 160));
        pnlTimKiem.add(taoWrapperInput("Loại:", cmbTheLoai, 120));

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlNut.setBackground(MAU_NEN_TAB);
        
        JButton btnLamMoi = taoNutChucNang("Làm mới", new Color(33, 150, 243));
        btnLamMoi.addActionListener(e -> {
            txtTimMa.setText("");
            txtTimTen.setText("");
            loadDataTheLoai(); 
            cmbTheLoai.setSelectedIndex(0);
            lamMoiGrid();
        });
        pnlNut.add(btnLamMoi);

        pnlDieuKhien.add(pnlTimKiem, BorderLayout.WEST);
        pnlDieuKhien.add(pnlNut, BorderLayout.EAST);

        // 2. VÙNG HIỂN THỊ SỐ LƯỢNG
        lblSoLuong = new JLabel("Số phim khởi chiếu: 0");
        lblSoLuong.setForeground(new Color(180, 180, 180));
        lblSoLuong.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel pnlTopWrap = new JPanel(new BorderLayout(0, 10));
        pnlTopWrap.setOpaque(false);
        pnlTopWrap.add(pnlDieuKhien, BorderLayout.NORTH);
        pnlTopWrap.add(lblSoLuong, BorderLayout.SOUTH);
        panelChinh.add(pnlTopWrap, BorderLayout.NORTH);

        // 3. VÙNG LƯỚI CARD PHIM
        pnlGridPhim = new JPanel(new GridLayout(0, 6, 20, 25)); 
        pnlGridPhim.setBackground(MAU_NEN_TAB);

        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(MAU_NEN_TAB);
        pnlWrapper.add(pnlGridPhim, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(pnlWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(MAU_NEN_TAB);
        tuyChinhScrollBar(scroll);
        panelChinh.add(scroll, BorderLayout.CENTER);

        add(panelChinh);

        KeyAdapter search = new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { lamMoiGrid(); }
        };
        txtTimMa.addKeyListener(search);
        txtTimTen.addKeyListener(search);
        cmbTheLoai.addActionListener(e -> lamMoiGrid());

        lamMoiGrid();
    }

    private void loadDataTheLoai() {
        cmbTheLoai.removeAllItems();
        cmbTheLoai.addItem("Tất cả"); 
        List<TheLoai> ds = theLoaiDAO.getAllTheLoai();
        for (TheLoai tl : ds) {
            cmbTheLoai.addItem(tl); 
        }
    }

    private void lamMoiGrid() {
        String ma = txtTimMa.getText().trim();
        String ten = txtTimTen.getText().trim();
        String theLoai = cmbTheLoai.getSelectedItem().toString();

        List<Phim> ds = phimDAO.timKiemPhim(ma, ten, theLoai); 
        
        pnlGridPhim.removeAll();
        lblSoLuong.setText("Số phim khởi chiếu: " + ds.size());
        for (Phim p : ds) {
            pnlGridPhim.add(taoCardPhim(p));
        }
        pnlGridPhim.revalidate();
        pnlGridPhim.repaint();
    }

    private JPanel taoCardPhim(Phim p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(31, 32, 44)); 
        card.setBorder(new LineBorder(MAU_VIEN_INPUT, 1));
        card.setPreferredSize(new Dimension(180, 310));
        card.setMaximumSize(new Dimension(180, 310));

        JLabel lblImg = new JLabel("", SwingConstants.CENTER);
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImg.setBorder(new EmptyBorder(10, 10, 10, 10));

        // TỐI ƯU HÓA LOAD ẢNH: Kiểm tra cả Resource và File hệ thống
        String imagePath = "src/img/" + p.getDuongDanAnh();
        File fileImg = new File(imagePath);
        ImageIcon icon = null;

        if (fileImg.exists()) {
            icon = new ImageIcon(imagePath); // Ưu tiên đọc trực tiếp từ thư mục src
        } else {
            java.net.URL imgURL = getClass().getResource("/img/" + p.getDuongDanAnh());
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            }
        }

        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(160, 220, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } else {
            lblImg.setText("Chưa có ảnh");
            lblImg.setForeground(Color.GRAY);
        }

        JLabel lblTen = new JLabel(p.getTenPhim().toUpperCase(), SwingConstants.CENTER);
        lblTen.setForeground(MAU_CHU_CHUNG);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel(p.getThoiLuong() + " phút | " + p.getTheLoai().getTenTheLoai(), SwingConstants.CENTER);
        lblSub.setForeground(MAU_VANG);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblImg);
        card.add(lblTen);
        card.add(Box.createVerticalStrut(5));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(10));
        
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.setBorder(new LineBorder(MAU_VANG, 1)); }
            @Override public void mouseExited(MouseEvent e) { card.setBorder(new LineBorder(MAU_VIEN_INPUT, 1)); }
        });

        return card;
    }

    private JPanel taoWrapperInput(String label, JComponent comp, int width) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MAU_NEN_TAB);
        JLabel lbl = new JLabel(label);
        lbl.setOpaque(true); lbl.setBackground(MAU_NHAN_XAM); lbl.setForeground(MAU_CHU_CHUNG);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setBorder(new EmptyBorder(0, 15, 0, 15));
        comp.setBackground(MAU_THANH_TIM_KIEM); comp.setForeground(MAU_CHU_CHUNG);
        comp.setPreferredSize(new Dimension(width, 35));
        comp.setBorder(BorderFactory.createCompoundBorder(new LineBorder(MAU_VIEN_INPUT, 1), new EmptyBorder(5, 10, 5, 10)));
        p.add(lbl, BorderLayout.WEST); p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JButton taoNutChucNang(String t, Color bg) {
        JButton b = new JButton(t); b.setBackground(bg); b.setForeground(MAU_CHU_CHUNG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14)); b.setPreferredSize(new Dimension(110, 40));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void tuyChinhScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                this.thumbColor = MAU_NHAN_XAM; this.trackColor = MAU_NEN_TAB;
            }
        });
    }
}