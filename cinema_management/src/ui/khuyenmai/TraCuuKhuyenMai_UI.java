package ui.khuyenmai;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

public class TraCuuKhuyenMai_UI extends JPanel {

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_THANH = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_XANH = new Color(33, 150, 243);

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtPhanTram, txtNgayBD, txtNgayKT, txtTim;

    private KhuyenMai_DAO dao;

    public TraCuuKhuyenMai_UI() {
        dao = new KhuyenMai_DAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("TRA CỨU KHUYẾN MÃI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(MAU_CHU);
        add(title, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(taoInput("Mã KM", txtMa = new JTextField()), gbc);

        gbc.gridx = 1;
        form.add(taoInput("Tên KM", txtTen = new JTextField()), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(taoInput("Ngày bắt đầu", txtNgayBD = new JTextField()), gbc);

        gbc.gridx = 1;
        form.add(taoInput("Ngày kết thúc", txtNgayKT = new JTextField()), gbc);

        // Row 3 (full width)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(taoInput("Phần trăm giảm", txtPhanTram = new JTextField()), gbc);

        gbc.gridwidth = 1;

        txtMa.setEditable(false);

        main.add(form);

        // ===== SEARCH =====
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm mã hoặc tên:");
        lblSearch.setForeground(MAU_CHU);

        txtTim = new JTextField(20);
        txtTim.setBackground(MAU_THANH);
        txtTim.setForeground(MAU_CHU);

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setBackground(MAU_XANH);
        btnTim.setForeground(Color.WHITE);

        search.add(lblSearch);
        search.add(txtTim);
        search.add(btnTim);

        main.add(Box.createVerticalStrut(10));
        main.add(search);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{
                "Mã KM", "Tên", "% Giảm", "Ngày BD", "Ngày KT"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(30);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setBackground(MAU_THANH);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(950, 260));

        main.add(Box.createVerticalStrut(10));
        main.add(sp);

        add(main, BorderLayout.CENTER);

        // ===== LOAD DATA =====
        loadData();

        // ===== EVENT =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                txtMa.setText(model.getValueAt(r, 0).toString());
                txtTen.setText(model.getValueAt(r, 1).toString());
                txtPhanTram.setText(model.getValueAt(r, 2).toString());
                txtNgayBD.setText(model.getValueAt(r, 3).toString());
                txtNgayKT.setText(model.getValueAt(r, 4).toString());
            }
        });

        btnTim.addActionListener(e -> timKiem());
    }

    private JPanel taoInput(String label, JTextField txt) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(MAU_THANH);
        txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU);

        txt.setPreferredSize(new Dimension(250, 32));

        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(90, 90, 90)),
                new EmptyBorder(6, 10, 6, 10)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txt, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        model.setRowCount(0);
        List<KhuyenMai> ds = dao.docDanhSachKM();

        for (KhuyenMai km : ds) {
            model.addRow(new Object[]{
                    km.getMaKM(),
                    km.getTenKhuyenMai(),
                    km.getPhanTramGiam(),
                    km.getNgayBatDau(),
                    km.getNgayKetThuc()
            });
        }
    }

    private void timKiem() {
        String key = txtTim.getText().trim();
        List<KhuyenMai> ds;

        if (key.startsWith("KM")) {
            ds = dao.timTheoMa(key);
        } else {
            ds = dao.timTheoTen(key);
        }

        model.setRowCount(0);
        for (KhuyenMai km : ds) {
            model.addRow(new Object[]{
                    km.getMaKM(),
                    km.getTenKhuyenMai(),
                    km.getPhanTramGiam(),
                    km.getNgayBatDau(),
                    km.getNgayKetThuc()
            });
        }
    }
}