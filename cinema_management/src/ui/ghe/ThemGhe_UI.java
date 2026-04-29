package ui.phongchieu;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import dao.Ghe_DAO;
import dao.PhongChieu_DAO;
import entity.Ghe;
import entity.PhongChieu;

public class ThemGhe_UI extends JPanel {

    // ===== BẢNG MÀU LOTTE STYLE =====
    private final Color BG = new Color(48, 52, 56);
    private final Color CARD = new Color(31, 32, 44);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);

    // Màu theo loại & trạng thái
    private final Color COLOR_THUONG = new Color(200, 190, 230); // Tím nhạt
    private final Color COLOR_VIP = new Color(255, 200, 200);    // Hồng nhạt
    private final Color COLOR_DOI = new Color(255, 120, 170);    // Hồng đậm
    private final Color COLOR_CHON = new Color(255, 0, 127);     // Hồng Neon (Bạn chọn)
    private final Color COLOR_DA_DAT = new Color(80, 80, 80);    // Xám (Đã đặt)

    private JPanel pnlSoDoGhe;
    private JComboBox<PhongChieu> cmbPhong;
    private Ghe_DAO gheDAO = new Ghe_DAO();
    private PhongChieu_DAO phongDAO = new PhongChieu_DAO();

    public ThemGhe_UI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Header & Chọn phòng
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setOpaque(false);
        cmbPhong = new JComboBox<>();
        cmbPhong.setPreferredSize(new Dimension(250, 35));
        pnlTop.add(new JLabel("Chọn Phòng Chiếu: ") {{ setForeground(TEXT); }});
        pnlTop.add(cmbPhong);
        add(pnlTop, BorderLayout.NORTH);

        // 2. Sơ đồ ghế (FlowLayout)
        pnlSoDoGhe = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlSoDoGhe.setBackground(CARD);
        JScrollPane scroll = new JScrollPane(pnlSoDoGhe);
        scroll.setBorder(new TitledBorder(new LineBorder(BORDER), "SƠ ĐỒ GHẾ THỰC TẾ", 0, 0, null, TEXT));
        add(scroll, BorderLayout.CENTER);

        // 3. Chú thích (Legend)
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        pnlBot.setOpaque(false);
        pnlBot.add(taoLegend("Đã đặt", COLOR_DA_DAT));
        pnlBot.add(taoLegend("Ghế bạn chọn", COLOR_CHON));
        pnlBot.add(taoLegend("Thường", COLOR_THUONG));
        pnlBot.add(taoLegend("VIP", COLOR_VIP));
        pnlBot.add(taoLegend("Đôi", COLOR_DOI));
        add(pnlBot, BorderLayout.SOUTH);

        // Load dữ liệu
        loadPhong();
        cmbPhong.addActionListener(e -> veSoDoTheoDB());
    }

    private void veSoDoTheoDB() {
        pnlSoDoGhe.removeAll();
        PhongChieu pc = (PhongChieu) cmbPhong.getSelectedItem();
        if (pc == null) return;

        List<Ghe> ds = gheDAO.timGheTheoPhong(pc.getMaPhong());
        
        for (Ghe g : ds) {
            JButton btn = new JButton(g.getHang() + g.getSoGhe());
            btn.setPreferredSize(new Dimension(55, 55));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setFocusPainted(false);
            
            // Xử lý trạng thái & Loại ghế
            if (g.getTrangThai().equalsIgnoreCase("Đã đặt")) {
                btn.setBackground(COLOR_DA_DAT);
                btn.setEnabled(false);
            } else {
                if (g.getLoaiGhe().equalsIgnoreCase("VIP")) btn.setBackground(COLOR_VIP);
                else if (g.getLoaiGhe().equalsIgnoreCase("Đôi")) btn.setBackground(COLOR_DOI);
                else btn.setBackground(COLOR_THUONG);
                
                // Sự kiện "Bạn chọn"
                btn.addActionListener(e -> {
                    if (btn.getBackground().equals(COLOR_CHON)) {
                        veSoDoTheoDB(); // Reset về màu gốc
                    } else {
                        btn.setBackground(COLOR_CHON);
                    }
                });
            }
            pnlSoDoGhe.add(btn);
        }
        pnlSoDoGhe.revalidate(); pnlSoDoGhe.repaint();
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
}