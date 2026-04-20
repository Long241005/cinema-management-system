package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    // ========== CÀI ĐẶT TÙY CHỈNH KÍCH THƯỚC ==========
    private final int KICH_THUOC_LOGO = 130; 
    private final int CHIEU_CAO_ITEM_CHA = 60; // Tăng từ 50 lên 60 để giãn cách
    private final int CHIEU_CAO_ITEM_CON = 45; // Tăng từ 40 lên 45
    private final int SIZE_CHU_CHA = 18;       // Chữ menu chính to lên
    private final int SIZE_CHU_CON = 16;       // Chữ menu con to lên

    private final Color MAU_NEN_MENU = new Color(31, 32, 44);
    private final Color MAU_MENU_CHON = new Color(241, 121, 104); 
    private final Color MAU_CHU_TRANG = Color.WHITE;

    private JPanel menuPanel;
    private JPanel mainContentPanel;
    private final List<JPanel> tatCaItemMenu = new ArrayList<>();
    private final List<JPanel> danhSachSubMenu = new ArrayList<>();

    public MainFrame() {
        khoiTaoCuaSo();
        JPanel menuContainer = taoMenuContainer();
        add(menuContainer, BorderLayout.WEST);
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.BLACK);
        add(mainContentPanel, BorderLayout.CENTER);
        hienThiNoiDung("Hệ thống");
    }

    private void khoiTaoCuaSo() {
        setTitle("Hệ thống Quản lý Rạp Chiếu Phim T3L");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private JPanel taoMenuContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(280, getHeight())); // Tăng độ rộng menu lên 280px cho thoáng
        container.setBackground(MAU_NEN_MENU);

        menuPanel = new JPanel();
        menuPanel.setBackground(MAU_NEN_MENU);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(taoLogoPanelCoKhungTron());
        menuPanel.add(taoClockPanel());
        menuPanel.add(Box.createVerticalStrut(25)); // Giãn cách giữa đầu và thân menu

        // DANH SÁCH MENU (Chữ to & Giãn cách)
        themMenuItem("Hệ thống", null, true);
        themMenuItem("Phim", List.of("Tra cứu phim", "Thêm phim mới", "Thể loại phim"), false);
        themMenuItem("Lịch chiếu", List.of("Lịch chiếu hôm nay", "Quản lý suất chiếu"), false);
        themMenuItem("Phòng & Ghế", List.of("Sơ đồ phòng", "Cấu hình ghế"), false);
        themMenuItem("Nhân viên", List.of("Tra cứu nhân viên", "Thêm nhân viên", "Cập nhật nhân viên"), false);
        themMenuItem("Khách hàng", List.of("Tra cứu khách hàng", "Thêm khách hàng", "Cập nhật khách hàng", "Thống kê khách hàng"), false);
        themMenuItem("Hóa đơn", List.of("Tra cứu hóa đơn", "Thống kê hóa đơn"), false);
        themMenuItem("Khuyến mãi", List.of("Tra cứu khuyến mãi", "Thêm khuyến mãi", "Cập nhật khuyến mãi", "Thống kê khuyến mãi"), false);

        menuPanel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        container.add(scroll, BorderLayout.CENTER);
        container.add(taoUserPanel(), BorderLayout.SOUTH);
        return container;
    }

    private void themMenuItem(String text, List<String> subItems, boolean isSelected) {
        // Tăng Padding trái từ 20 lên 30 để chữ lùi vào trong đẹp hơn
        JPanel itemCha = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, (CHIEU_CAO_ITEM_CHA - 25) / 2));
        itemCha.setBackground(MAU_NEN_MENU);
        itemCha.setMaximumSize(new Dimension(300, CHIEU_CAO_ITEM_CHA));
        itemCha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel(text);
        lbl.setForeground(MAU_CHU_TRANG);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, SIZE_CHU_CHA)); // Áp dụng cỡ chữ to
        itemCha.add(lbl);

        menuPanel.add(itemCha);
        tatCaItemMenu.add(itemCha);

        if (subItems != null) {
            JPanel subMenuPanel = new JPanel();
            subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
            subMenuPanel.setBackground(MAU_NEN_MENU);
            subMenuPanel.setVisible(false);
            danhSachSubMenu.add(subMenuPanel);

            for (int i = 0; i < subItems.size(); i++) {
                SubItemPanel itemCon = new SubItemPanel(subItems.get(i), (i == subItems.size() - 1));
                itemCon.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        doiMau(itemCon);
                        hienThiNoiDung(layTextLabel(itemCon));
                    }
                });
                subMenuPanel.add(itemCon);
                tatCaItemMenu.add(itemCon);
            }
            menuPanel.add(subMenuPanel);

            itemCha.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    boolean s = subMenuPanel.isVisible();
                    for (JPanel sub : danhSachSubMenu) sub.setVisible(false);
                    subMenuPanel.setVisible(!s);
                    menuPanel.revalidate();
                    menuPanel.repaint();
                }
            });
        } else {
            itemCha.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    for (JPanel sub : danhSachSubMenu) sub.setVisible(false);
                    doiMau(itemCha);
                    hienThiNoiDung(text);
                    menuPanel.revalidate();
                }
            });
        }
        if (isSelected) doiMau(itemCha);
    }

    private String layTextLabel(JPanel p) {
        for (Component c : p.getComponents()) { if (c instanceof JLabel) return ((JLabel) c).getText(); }
        return "";
    }

    private void doiMau(JPanel panel) {
        for (JPanel p : tatCaItemMenu) p.setBackground(MAU_NEN_MENU);
        panel.setBackground(MAU_MENU_CHON);
    }

    // ========== CÁC CLASS HỖ TRỢ VẼ (GIÃN CÁCH) ==========

    private class SubItemPanel extends JPanel {
        private boolean isLast;
        public SubItemPanel(String text, boolean isLast) {
            this.isLast = isLast;
            // Tăng Vgap lên 12 để giãn các dòng menu con
            setLayout(new FlowLayout(FlowLayout.LEFT, 55, 12));
            setBackground(MAU_NEN_MENU);
            setMaximumSize(new Dimension(300, CHIEU_CAO_ITEM_CON));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel lbl = new JLabel(text);
            lbl.setForeground(new Color(200, 200, 200));
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, SIZE_CHU_CON)); // Chữ menu con to
            add(lbl);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(1.5f)); // Đường nối đậm hơn chút
            g2d.drawLine(35, 0, 35, isLast ? CHIEU_CAO_ITEM_CON / 2 : CHIEU_CAO_ITEM_CON);
            g2d.drawLine(35, CHIEU_CAO_ITEM_CON / 2, 48, CHIEU_CAO_ITEM_CON / 2);
        }
    }

    // --- Giữ nguyên các hàm Logo, Clock, User và Background của bản trước ---
    private JPanel taoLogoPanelCoKhungTron() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        p.setBackground(MAU_NEN_MENU);
        p.setMaximumSize(new Dimension(300, KICH_THUOC_LOGO + 50));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/logo_cinema.jpg"));
            BufferedImage bufferedImage = new BufferedImage(KICH_THUOC_LOGO, KICH_THUOC_LOGO, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Double(0, 0, KICH_THUOC_LOGO, KICH_THUOC_LOGO));
            g2.drawImage(icon.getImage(), 0, 0, KICH_THUOC_LOGO, KICH_THUOC_LOGO, null);
            g2.setClip(null);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Ellipse2D.Double(2, 2, KICH_THUOC_LOGO - 4, KICH_THUOC_LOGO - 4));
            g2.dispose();
            p.add(new JLabel(new ImageIcon(bufferedImage)));
        } catch (Exception e) { p.add(new JLabel("LOGO T3L")); }
        return p;
    }

    private JPanel taoClockPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(MAU_NEN_MENU);
        p.setMaximumSize(new Dimension(300, 70));
        JLabel t = new JLabel("00:00:00");
        JLabel d = new JLabel("01/01/2026");
        t.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Đồng hồ to hơn
        t.setForeground(Color.WHITE); d.setForeground(Color.GRAY);
        t.setAlignmentX(CENTER_ALIGNMENT); d.setAlignmentX(CENTER_ALIGNMENT);
        new Timer(1000, e -> {
            t.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            d.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }).start();
        p.add(t); p.add(d);
        return p;
    }

    private void hienThiNoiDung(String t) {
        mainContentPanel.removeAll();
        if (t.equals("Hệ thống")) {
            mainContentPanel.add(new BackgroundPanel("/img/bg_cinema1.jpg"));
        } else {
            JLabel lbl = new JLabel(t, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 60));
            lbl.setForeground(Color.WHITE);
            mainContentPanel.add(lbl);
        }
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel taoUserPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MAU_NEN_MENU);
        p.setBorder(new EmptyBorder(15, 30, 20, 30));
        JLabel u = new JLabel("T3L Admin Team");
        u.setFont(new Font("Segoe UI", Font.BOLD, 16));
        u.setForeground(Color.WHITE);
        p.add(u, BorderLayout.WEST);
        return p;
    }

    private class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) {
            try { java.net.URL url = getClass().getResource(path);
                if (url != null) img = new ImageIcon(url).getImage(); 
            } catch (Exception e) {}
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 190));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI", Font.BOLD, 90));
           // g.drawString("Xin Chào!", getWidth()/2 - 200, getHeight()/2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}