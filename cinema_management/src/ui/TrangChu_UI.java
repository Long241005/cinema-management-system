package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import entity.Phim;
import entity.PhongChieu;
import ui.khachhang.ThongKeKhachHang_UI;
import ui.khuyenmai.ThongKeKhuyenMai_UI;
import ui.ghe.ThemGhe_UI;
import ui.hoadon.ThongKeHoaDon_UI;
import ui.hoadon.TraCuuHoaDon_UI;
import ui.khuyenmai.TraCuuKhuyenMai_UI;
import ui.nhanvien.XoaNhanVien_UI;
import ui.phongchieu.CapNhatPhong_UI;
import ui.phongchieu.XoaPhong_UI;
import ui.phongchieu.ThemPhong_UI;
import ui.phongchieu.TraCuuPhong_UI;
import ui.khachhang.CapNhatKhachHang_UI;
import ui.khachhang.TraCuuKhachHang_UI;
import ui.khachhang.ThemKhachHang_UI;
import ui.khuyenmai.CapNhatKhuyenMai_UI;
import ui.khuyenmai.ThemKhuyenMai_UI;
import ui.phim.CapNhatPhim_UI;
import ui.phim.TraCuuPhim_UI;
import ui.phim.ThemPhim_UI;
import ui.phim.ThongKePhim_UI;
import ui.nhanvien.CapNhatNhanVien_UI;
import ui.nhanvien.ThemNhanVien_UI;
import ui.nhanvien.TraCuuNhanVien_UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrangChu_UI extends JFrame {

    // ========== BIẾN TOÀN CỤC - PANELS ==========
    private JPanel menuPanel;
    private JPanel mainContentPanel;
    private final List<JPanel> tatCaCacItemMenuCoTheClick;
    private JPanel itemMenuDangDuocChon;
    private final List<JPanel> cacMenuCha;
    private JPanel menuDangMoRong = null;

    // ========== BIẾN TOÀN CỤC - MÀU SẮC ==========
    private final Color MAU_NEN_MENU_MAC_DINH = new Color(31, 32, 44);
    private final Color MAU_NEN_MENU_HOVER = new Color(70, 70, 70);
    private final Color MAU_NEN_MENU_DUOC_CHON = new Color(241, 121, 104);
    private final Color MAU_NEN_MENU_CON_HOVER = new Color(80, 80, 80);
    private final Color MAU_CHU_TRANG = Color.WHITE;
    private final Color MAU_NEN_DEN = Color.BLACK;

    // ========== BIẾN TOÀN CỤC - KÍCH THƯỚC ==========
    private final Dimension KICH_THUOC_MENU = new Dimension(250, 0);
    private final Dimension KICH_THUOC_LOGO_PANEL = new Dimension(250, 150);
    private final Dimension KICH_THUOC_NGAY_GIO_PANEL = new Dimension(250, 60);
    private final Dimension KICH_THUOC_ITEM_MENU = new Dimension(250, 50);
    private final Dimension KICH_THUOC_ITEM_MENU_CON = new Dimension(250, 40);
    private final Dimension KICH_THUOC_PANEL_DUOI = new Dimension(250, 60);
    private final Dimension KICH_THUOC_TOI_THIEU = new Dimension(1200, 700);
    private final Dimension KICH_THUOC_CUA_SO = new Dimension(1800, 950);

    // ========== BIẾN TOÀN CỤC - FONT CHỮ ==========
    private final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_NGAY = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_MENU_CHA = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_MENU_CON = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font FONT_NGUOI_DUNG = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_NOI_DUNG = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_XIN_CHAO = new Font("Segoe UI", Font.BOLD, 100);

    // ========== BIẾN TOÀN CỤC - KÍCH THƯỚC ICON ==========
    private final int KICH_THUOC_ICON_MENU = 24;
    private final int KICH_THUOC_ICON_DANG_XUAT = 24;
    private final int KICH_THUOC_LOGO = 170;

    public TrangChu_UI() {
        khoiTaoCuaSo();
        tatCaCacItemMenuCoTheClick = new ArrayList<>();
        cacMenuCha = new ArrayList<>();

        JPanel menuContainerPanel = taoMenuContainerPanel();
        add(menuContainerPanel, BorderLayout.WEST);

        mainContentPanel = taoMainContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);

        hienThiNoiDung("Hệ thống");
    }

    private void khoiTaoCuaSo() {
        setTitle("Hệ thống Quản lý Rạp Chiếu Phim ");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(KICH_THUOC_CUA_SO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(KICH_THUOC_TOI_THIEU);
        setLayout(new BorderLayout());
    }

    private JPanel taoMenuContainerPanel() {
        JPanel menuContainerPanel = new JPanel(new BorderLayout());
        menuContainerPanel.setPreferredSize(new Dimension(KICH_THUOC_MENU.width, getHeight()));
        menuContainerPanel.setBackground(MAU_NEN_MENU_MAC_DINH);

        taoMenuPanel();
        JScrollPane menuScrollPane = taoMenuScrollPane();
        menuContainerPanel.add(menuScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = taoPanelDuoi();
        menuContainerPanel.add(bottomPanel, BorderLayout.SOUTH);

        return menuContainerPanel;
    }

    private void taoMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(taoLogoPanel());
        menuPanel.add(taoNgayGioPanel());
        menuPanel.add(Box.createVerticalStrut(10));

        themCacMenuItem();

        menuPanel.add(Box.createVerticalGlue());
    }

    private JPanel taoLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        logoPanel.setMaximumSize(KICH_THUOC_LOGO_PANEL);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/IMG/logocinema.jpg"));
            Image logoImage = logoIcon.getImage().getScaledInstance(KICH_THUOC_LOGO, KICH_THUOC_LOGO, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            logoPanel.add(new JLabel("CINEMA"));
        }

        return logoPanel;
    }

    private JPanel taoNgayGioPanel() {
        JPanel dateTimePanel = new JPanel();
        dateTimePanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        dateTimePanel.setMaximumSize(KICH_THUOC_NGAY_GIO_PANEL);
        dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.Y_AXIS));
        dateTimePanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel timeLabel = taoNhanGio();
        JLabel dateLabel = taoNhanNgay();

        capNhatNgayGio(timeLabel, dateLabel);
        new Timer(1000, e -> capNhatNgayGio(timeLabel, dateLabel)).start();

        dateTimePanel.add(timeLabel);
        dateTimePanel.add(dateLabel);

        return dateTimePanel;
    }

    private JLabel taoNhanGio() {
        JLabel timeLabel = new JLabel();
        timeLabel.setForeground(MAU_CHU_TRANG);
        timeLabel.setFont(FONT_LOGO);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return timeLabel;
    }

    private JLabel taoNhanNgay() {
        JLabel dateLabel = new JLabel();
        dateLabel.setForeground(MAU_CHU_TRANG);
        dateLabel.setFont(FONT_NGAY);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return dateLabel;
    }

    private void themCacMenuItem() {
        themMenuItem("Hệ thống", "/IMG/setting_32px_v2.png", null, true);
        themMenuItem("Phim", "/IMG/film-roll.png", 
            Arrays.asList("Tra cứu phim", "Thêm phim mới", "Cập nhật phim", "Thống kê phim"), false);
        themMenuItem("Phòng chiếu", "/IMG/film-roll.png", 
            Arrays.asList("Cập nhật phòng", "Thêm phòng", "Tra cứu phòng","Xóa phòng"), false);
        themMenuItem("Ghế", "/IMG/film-roll.png", // Bạn có thể thay icon khác nếu có
                Arrays.asList("Tra cứu ghế", "Thêm ghế mới", "Cập nhật ghế"), false);
        themMenuItem("Nhân viên", "/IMG/nhanvien_32px_v2.png", 
            Arrays.asList("Tra cứu nhân viên", "Thêm nhân viên", "Cập nhật nhân viên","Xóa nhân viên"), false);
        themMenuItem("Khách hàng", "/IMG/khachhang_32px_v2.png", 
            Arrays.asList("Tra cứu khách hàng", "Thêm khách hàng", "Cập nhật khách hàng", "Thống kê khách hàng"), false);
        themMenuItem("Hóa đơn", "/IMG/hoadon_32px_v2.png", 
            Arrays.asList("Tra cứu hóa đơn", "Thống kê hóa đơn"), false);
        themMenuItem("Khuyến mãi", "/IMG/khuyenmai_32px_v2.png", 
            Arrays.asList("Tra cứu khuyến mãi", "Thêm khuyến mãi", "Cập nhật khuyến mãi","Thống kê khuyến mãi"), false);
    }

    private JScrollPane taoMenuScrollPane() {
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(null);
        menuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return menuScrollPane;
    }

    private JPanel taoPanelDuoi() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        bottomPanel.setPreferredSize(KICH_THUOC_PANEL_DUOI);
        bottomPanel.setLayout(new BorderLayout(5, 0));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 15, 15));

        JLabel userLabel = taoNhanNguoiDung();
        bottomPanel.add(userLabel, BorderLayout.WEST);

        JLabel logoutButton = taoNutDangXuat();
        if (logoutButton != null) {
            bottomPanel.add(logoutButton, BorderLayout.EAST);
        }

        return bottomPanel;
    }

    private JLabel taoNhanNguoiDung() {
        // Giả sử có lớp Auth hoặc context tương tự từ code cũ
        String tenHienThi = "HHL Team"; 
        JLabel userLabel = new JLabel(tenHienThi);
        userLabel.setForeground(MAU_CHU_TRANG);
        userLabel.setFont(FONT_NGUOI_DUNG);
        return userLabel;
    }

    private JLabel taoNutDangXuat() {
        try {
            ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/IMG/dangxuat_64px.png"));
            Image logoutImage = logoutIcon.getImage().getScaledInstance(
                KICH_THUOC_ICON_DANG_XUAT, KICH_THUOC_ICON_DANG_XUAT, Image.SCALE_SMOOTH);
            JLabel logoutButton = new JLabel(new ImageIcon(logoutImage));
            caiDatNutDangXuat(logoutButton);
            return logoutButton;
        } catch (Exception e) {
            return null;
        }
    }

    private void caiDatNutDangXuat(JLabel logoutButton) {
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setToolTipText("Đăng xuất");
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Xác nhận đăng xuất?", "Thoát", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) dispose();
            }
        });
    }

    private void themMenuItem(String text, String iconPath, List<String> cacItemCon, boolean duocChon) {
        JPanel itemMenuCha = taoItemMenuCha(text, iconPath);
        menuPanel.add(itemMenuCha);
        tatCaCacItemMenuCoTheClick.add(itemMenuCha);

        JPanel subMenuPanel = taoSubMenuPanel(cacItemCon, itemMenuCha);
        menuPanel.add(subMenuPanel);

        ganSuKienChoItemMenuCha(itemMenuCha, text, cacItemCon, subMenuPanel);

        if (duocChon) {
            chonMenuItem(itemMenuCha);
        }
    }

    private JPanel taoItemMenuCha(String text, String iconPath) {
        JPanel itemPanel = new JPanel();
        itemPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        itemPanel.setMaximumSize(KICH_THUOC_ITEM_MENU);
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 25));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        themIconVaoPanel(itemPanel, iconPath);
        themTextVaoPanel(itemPanel, text, FONT_MENU_CHA);

        return itemPanel;
    }

    private void themIconVaoPanel(JPanel panel, String iconPath) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image image = icon.getImage().getScaledInstance(KICH_THUOC_ICON_MENU, KICH_THUOC_ICON_MENU, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(image)));
        } catch (Exception e) {
            panel.add(new JLabel("•"));
        }
    }

    private void themTextVaoPanel(JPanel panel, String text, Font font) {
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(MAU_CHU_TRANG);
        textLabel.setFont(font);
        panel.add(textLabel);
    }

    private JPanel taoSubMenuPanel(List<String> cacItemCon, JPanel itemMenuCha) {
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setMaximumSize(new Dimension(
            KICH_THUOC_MENU.width, 
            cacItemCon == null ? 0 : cacItemCon.size() * KICH_THUOC_ITEM_MENU_CON.height
        ));
        subMenuPanel.setVisible(false);

        if (cacItemCon != null && !cacItemCon.isEmpty()) {
            cacMenuCha.add(itemMenuCha);
            themCacItemCon(subMenuPanel, cacItemCon);
        }

        return subMenuPanel;
    }

    private void themCacItemCon(JPanel subMenuPanel, List<String> cacItemCon) {
        for (int i = 0; i < cacItemCon.size(); i++) {
            String textItemCon = cacItemCon.get(i);
            boolean laItemCuoi = (i == cacItemCon.size() - 1);

            JPanel itemConPanel = taoItemMenuCon(textItemCon, laItemCuoi);
            subMenuPanel.add(itemConPanel);
            tatCaCacItemMenuCoTheClick.add(itemConPanel);

            ganSuKienChoItemMenuCon(itemConPanel, textItemCon);
        }
    }

    private JPanel taoItemMenuCon(String text, boolean laItemCuoi) {
        JPanel subItemPanel = new SubMenuItemPanel(laItemCuoi);
        subItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 45, 8));
        subItemPanel.setBackground(MAU_NEN_MENU_MAC_DINH);
        subItemPanel.setMaximumSize(KICH_THUOC_ITEM_MENU_CON);
        subItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        themTextVaoPanel(subItemPanel, text, FONT_MENU_CON);

        return subItemPanel;
    }

    private void ganSuKienChoItemMenuCha(JPanel itemMenuCha, String text, List<String> cacItemCon, JPanel subMenuPanel) {
        itemMenuCha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cacItemCon == null || cacItemCon.isEmpty()) {
                    chonMenuItem(itemMenuCha);
                    hienThiNoiDung(text);
                    dongTatCaMenuDangMo();
                } else {
                    batTatSubMenu(itemMenuCha, subMenuPanel);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (itemMenuCha != itemMenuDangDuocChon) itemMenuCha.setBackground(MAU_NEN_MENU_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (itemMenuCha != itemMenuDangDuocChon) itemMenuCha.setBackground(MAU_NEN_MENU_MAC_DINH);
            }
        });
    }

    private void ganSuKienChoItemMenuCon(JPanel itemMenuCon, String text) {
        itemMenuCon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chonMenuItem(itemMenuCon);
                hienThiNoiDung(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (itemMenuCon != itemMenuDangDuocChon) itemMenuCon.setBackground(MAU_NEN_MENU_CON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (itemMenuCon != itemMenuDangDuocChon) itemMenuCon.setBackground(MAU_NEN_MENU_MAC_DINH);
            }
        });
    }

    private void batTatSubMenu(JPanel itemMenuCha, JPanel subMenu) {
        if (subMenu.isVisible()) {
            subMenu.setVisible(false);
            menuDangMoRong = null;
        } else {
            dongTatCaMenuDangMo();
            subMenu.setVisible(true);
            menuDangMoRong = itemMenuCha;
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void dongTatCaMenuDangMo() {
        if (menuDangMoRong != null) {
            int index = menuPanel.getComponentZOrder(menuDangMoRong);
            if (index != -1 && index + 1 < menuPanel.getComponentCount()) {
                menuPanel.getComponent(index + 1).setVisible(false);
            }
            menuDangMoRong = null;
        }
    }

    private void chonMenuItem(JPanel panelDuocChon) {
        if (itemMenuDangDuocChon != null) {
            itemMenuDangDuocChon.setBackground(MAU_NEN_MENU_MAC_DINH);
        }
        itemMenuDangDuocChon = panelDuocChon;
        itemMenuDangDuocChon.setBackground(MAU_NEN_MENU_DUOC_CHON);
    }

    private void capNhatNgayGio(JLabel timeLabel, JLabel dateLabel) {
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private JPanel taoMainContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(MAU_NEN_DEN);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    public void hienThiNoiDung(String tenTab) {
        chonMenuTheoTen(tenTab);

        
//        if (tenTab.equals("Thêm phòng")) {
//            new ThemPhong_UI(this).setVisible(true);
//            return; 
//        }
        
        mainContentPanel.removeAll();

        switch (tenTab) {
            case "Hệ thống":
                mainContentPanel.add(new BackgroundPanel("/img/background.jpg"), BorderLayout.CENTER);
                break;
            case "Tra cứu phim":
                mainContentPanel.add(new TraCuuPhim_UI(), BorderLayout.CENTER);
                break;
            case "Thêm phim mới":
                mainContentPanel.add(new ThemPhim_UI(), BorderLayout.CENTER);
                break;
            case "Cập nhật phim":
                mainContentPanel.add(new CapNhatPhim_UI(), BorderLayout.CENTER);
                break;
            case "Thống kê phim":
                mainContentPanel.add(new ThongKePhim_UI(), BorderLayout.CENTER);
                break;
            case "Thêm phòng":
                mainContentPanel.add(new ThemPhong_UI(), BorderLayout.CENTER); 
                break;
            case "Cập nhật phòng": 
                mainContentPanel.add(new CapNhatPhong_UI(), BorderLayout.CENTER); 
                break;
            case "Tra cứu phòng":
                mainContentPanel.add(new TraCuuPhong_UI(), BorderLayout.CENTER); 
                break;
            case "Xóa phòng":
                mainContentPanel.add(new XoaPhong_UI(), BorderLayout.CENTER); 
                break;
            case "Thêm ghế mới":
                mainContentPanel.add(new ThemGhe_UI(), BorderLayout.CENTER);
                break;
            case "Tra cứu nhân viên":
                mainContentPanel.add(new TraCuuNhanVien_UI(), BorderLayout.CENTER);
                break;
            case "Thêm nhân viên":
                mainContentPanel.add(new ThemNhanVien_UI(), BorderLayout.CENTER);
                break;
            case "Cập nhật nhân viên":
                mainContentPanel.add(new CapNhatNhanVien_UI(), BorderLayout.CENTER);
                break;
            case "Xóa nhân viên":
                mainContentPanel.add(new XoaNhanVien_UI(), BorderLayout.CENTER);
                break;
            case "Tra cứu khách hàng":
                mainContentPanel.add(new TraCuuKhachHang_UI(), BorderLayout.CENTER);
                break;
            case "Thêm khách hàng":
                mainContentPanel.add(new ThemKhachHang_UI(), BorderLayout.CENTER);
                break;
            case "Cập nhật khách hàng":
                mainContentPanel.add(new CapNhatKhachHang_UI(), BorderLayout.CENTER);
                break;
            case "Thống kê khách hàng":
                mainContentPanel.add(new ThongKeKhachHang_UI(), BorderLayout.CENTER);
                break;
            case "Tra cứu hóa đơn":
                mainContentPanel.add(new TraCuuHoaDon_UI(), BorderLayout.CENTER);
                break;
            case "Thống kê hóa đơn":
                mainContentPanel.add(new ThongKeHoaDon_UI(), BorderLayout.CENTER);
                break;
            case "Tra cứu khuyến mãi":
                mainContentPanel.add(new TraCuuKhuyenMai_UI(), BorderLayout.CENTER);
                break;
            case "Thêm khuyến mãi":
                mainContentPanel.add(new ThemKhuyenMai_UI(), BorderLayout.CENTER);
                break;
            case "Cập nhật khuyến mãi":
                mainContentPanel.add(new CapNhatKhuyenMai_UI(), BorderLayout.CENTER);
                break;
            case "Thống kê khuyến mãi":
                mainContentPanel.add(new ThongKeKhuyenMai_UI(), BorderLayout.CENTER);
                break;
            default:
                JLabel contentLabel = new JLabel("Nội dung cho: " + tenTab, SwingConstants.CENTER);
                contentLabel.setForeground(MAU_CHU_TRANG);
                contentLabel.setFont(FONT_NOI_DUNG);
                mainContentPanel.add(contentLabel, BorderLayout.CENTER);
                break;
        }

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    private void chonMenuTheoTen(String tenMenu) {
        for (JPanel menuItem : tatCaCacItemMenuCoTheClick) {
            for (Component comp : menuItem.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText() != null && label.getText().equals(tenMenu)) {
                        chonMenuItem(menuItem);
                        return;
                    }
                }
            }
        }
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        public BackgroundPanel(String imagePath) {
            setLayout(new BorderLayout());
            setBackground(MAU_NEN_DEN);
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
                this.backgroundImage = icon.getImage();
            } catch (Exception e) {
                this.backgroundImage = null;
            }
            JLabel lblXinChao = new JLabel("Xin Chào!", SwingConstants.CENTER);
            lblXinChao.setForeground(MAU_CHU_TRANG);
            lblXinChao.setFont(FONT_XIN_CHAO);
            add(lblXinChao, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private class SubMenuItemPanel extends JPanel {
        private final boolean laItemCuoi;
        private final Color MAU_DUONG_KE = new Color(90, 90, 90);

        public SubMenuItemPanel(boolean laItemCuoi) {
            this.laItemCuoi = laItemCuoi;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(MAU_DUONG_KE);
            int midY = getHeight() / 2;
            g2d.drawLine(30, midY, 40, midY);
            if (laItemCuoi) g2d.drawLine(30, 0, 30, midY);
            else g2d.drawLine(30, 0, 30, getHeight());
        }
    }
    
    public void hienThiTrangCapNhatPhim(Phim phim) {
        chonMenuTheoTen("Cập nhật phim");
        mainContentPanel.removeAll();
        
        CapNhatPhim_UI panelCapNhat = new CapNhatPhim_UI();
        // Đổ dữ liệu phim đã chọn từ Tra cứu sang Form Cập nhật
        panelCapNhat.hienThiThongTinPhim(phim); 
        
        mainContentPanel.add(panelCapNhat, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
//    public void hienThiTrangCapNhatPhong(PhongChieu phong) {
//        mainContentPanel.removeAll();
//        CapNhatPhong_UI panelCapNhat = new CapNhatPhong_UI();
//        panelCapNhat.chonPhongDeCapNhat(phong.getMaPhong());
//        mainContentPanel.add(panelCapNhat, BorderLayout.CENTER);
//        mainContentPanel.revalidate();
//        mainContentPanel.repaint();
//    }
    
    
    public void hienThiTrangCapNhatPhong(PhongChieu phong) {
        // 1. Highlight menu "Cập nhật phòng" bên trái
        chonMenuTheoTen("Câp nhật phòng"); 
        
        // 2. Làm sạch khung nội dung chính
        mainContentPanel.removeAll();
        
        // 3. Khởi tạo UI Cập nhật
        CapNhatPhong_UI panelCapNhat = new CapNhatPhong_UI();
        
        // 4. Đổ dữ liệu phòng chiếu vào các ô nhập liệu (Sử dụng phương thức fillData)
        panelCapNhat.fillData(phong);
        
        // 5. Hiển thị lên màn hình
        mainContentPanel.add(panelCapNhat, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChu_UI().setVisible(true));
    }
}