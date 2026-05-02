package ui.phongchieu;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import dao.PhongChieu_DAO;
import entity.PhongChieu;

public class ThemPhong_UI extends JPanel {
    private final Color BG = new Color(48, 52, 56);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);

    private JTextField txtMaPhong, txtTenPhong, txtSoGhe;
    private JComboBox<String> cboLoaiPhong;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnLamMoi;
    private PhongChieu_DAO dao;

    public ThemPhong_UI() {
        dao = new PhongChieu_DAO();
        initUI();
        taoMaTuDong();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("THÊM PHÒNG CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        // FORM BÊN TRÁI
        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(420, 0));

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 12));
        form.setOpaque(false);

        txtMaPhong = createInput(form, "Mã phòng:");
        txtMaPhong.setEditable(false);
        txtTenPhong = createInput(form, "Tên phòng:");
        txtSoGhe = createInput(form, "Số ghế:");
        cboLoaiPhong = createCombo(form, "Loại phòng:", new String[]{"2D", "3D", "IMAX", "4DX"});

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);
        btnThem = createButton("Thêm phòng");
        btnLamMoi = createButton("Làm mới");
        actions.add(btnThem);
        actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        // BẢNG BÊN PHẢI
        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);
        model = new DefaultTableModel(new String[]{"Mã Phòng", "Tên Phòng", "Số Ghế", "Loại"}, 0);
        table = new JTable(model);
        thietKeBang(table);
        
        right.add(new JLabel("Danh sách phòng hiện có") {{ setForeground(TEXT); setFont(new Font("Segoe UI", Font.BOLD, 18)); }}, BorderLayout.NORTH);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themPhong());
        btnLamMoi.addActionListener(e -> { clearForm(); taoMaTuDong(); });
    }

    private JTextField createInput(JPanel panel, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 6));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setForeground(TEXT);
        JTextField txt = new JTextField();
        txt.setBackground(INPUT_BG); txt.setForeground(TEXT); txt.setBorder(new LineBorder(BORDER));
        txt.setPreferredSize(new Dimension(250, 40));
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(txt, BorderLayout.CENTER);
        panel.add(wrap);
        return txt;
    }

    private JComboBox<String> createCombo(JPanel panel, String label, String[] items) {
        JPanel wrap = new JPanel(new BorderLayout(5, 6));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setForeground(TEXT);
        JComboBox<String> cbo = new JComboBox<>(items);
        cbo.setPreferredSize(new Dimension(250, 40));
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(cbo, BorderLayout.CENTER);
        panel.add(wrap);
        return cbo;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setBackground(BLUE); btn.setForeground(TEXT);
        return btn;
    }

    private void thietKeBang(JTable t) {
        t.setRowHeight(34);
        t.setBackground(new Color(31, 32, 44));
        t.setForeground(TEXT);
        t.getTableHeader().setBackground(INPUT_BG);
        t.getTableHeader().setForeground(TEXT);
    }

    private void taoMaTuDong() {
        String maxMa = dao.getMaPhongLonNhat();
        int so = 1;
        if(maxMa != null) so = Integer.parseInt(maxMa.substring(1)) + 1;
        txtMaPhong.setText(String.format("P%04d", so));
    }

    private void loadData() {
        model.setRowCount(0);
        List<PhongChieu> ds = dao.docDanhSachPhongChieu();
        for (PhongChieu p : ds) model.addRow(new Object[]{p.getMaPhong(), p.getTenPhong(), p.getSoGhe(), p.getLoaiPhong()});
    }

    private void themPhong() {
        try {
            PhongChieu p = new PhongChieu(txtMaPhong.getText(), txtTenPhong.getText(), Integer.parseInt(txtSoGhe.getText()), cboLoaiPhong.getSelectedItem().toString());
            if (dao.themPhongChieu(p)) {
                JOptionPane.showMessageDialog(this, "Thành công!");
                loadData(); clearForm(); taoMaTuDong();
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!"); }
    }

    private void clearForm() {
        txtTenPhong.setText(""); txtSoGhe.setText(""); cboLoaiPhong.setSelectedIndex(0);
    }
}