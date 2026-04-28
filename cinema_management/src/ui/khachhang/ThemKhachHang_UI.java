package ui.khachhang;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_DAO;
import entity.KhachHang;

public class ThemKhachHang_UI extends JPanel {

    // ===== MÀU SẮC ĐỒNG BỘ HỆ THỐNG =====
    private final Color BG = new Color(48, 52, 56);
    private final Color CARD = new Color(31, 32, 44);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);
    private final Color GREEN = new Color(76, 175, 80);
    private final Color LABEL = new Color(180, 180, 180);

    private JTextField txtMaKH, txtTenKH, txtSDT, txtEmail, txtDiem;
    private JTable table;
    private DefaultTableModel model;

    private JButton btnThem, btnLamMoi;
    private KhachHang_DAO dao;

    public ThemKhachHang_UI() {
        dao = new KhachHang_DAO();
        initUI();
        taoMaTuDong();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("THÊM KHÁCH HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(TEXT);
        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(25, 0));
        main.setOpaque(false);

        // ===== FORM PANEL =====
        JPanel leftCard = new JPanel(new BorderLayout(0, 20));
        leftCard.setBackground(CARD);
        leftCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        leftCard.setPreferredSize(new Dimension(460, 0));

        JLabel formTitle = new JLabel("Thông tin khách hàng");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(TEXT);

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 15));
        form.setOpaque(false);

        txtMaKH = createInput(form, "Mã khách hàng");
        txtTenKH = createInput(form, "Tên khách hàng");
        txtSDT = createInput(form, "Số điện thoại");
        txtEmail = createInput(form, "Email");
        txtDiem = createInput(form, "Điểm tích lũy");

        txtMaKH.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        btnThem = createButton("Thêm khách hàng", BLUE);
        btnLamMoi = createButton("Làm mới", GREEN);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnLamMoi);

        leftCard.add(formTitle, BorderLayout.NORTH);
        leftCard.add(form, BorderLayout.CENTER);
        leftCard.add(buttonPanel, BorderLayout.SOUTH);

        // ===== TABLE PANEL =====
        JPanel rightCard = new JPanel(new BorderLayout(0, 15));
        rightCard.setBackground(CARD);
        rightCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel tableTitle = new JLabel("Danh sách khách hàng hiện có");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableTitle.setForeground(TEXT);

        model = new DefaultTableModel(new String[]{
                "Mã KH", "Tên khách hàng", "SĐT", "Email", "Điểm"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER));
        scrollPane.getViewport().setBackground(CARD);

        rightCard.add(tableTitle, BorderLayout.NORTH);
        rightCard.add(scrollPane, BorderLayout.CENTER);

        main.add(leftCard, BorderLayout.WEST);
        main.add(rightCard, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themKhachHang());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            taoMaTuDong();
        });
    }

    private JTextField createInput(JPanel panel, String labelText) {
        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(LABEL);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(300, 42));
        txt.setBackground(INPUT_BG);
        txt.setForeground(TEXT);
        txt.setCaretColor(TEXT);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)
        ));

        wrap.add(label, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        panel.add(wrap);

        return txt;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable() {
        table.setRowHeight(38);
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(BORDER);
        table.setSelectionBackground(BLUE);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(INPUT_BG);
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void taoMaTuDong() {
        try {
            int so = dao.demSoLuongKhachHang() + 1;
            txtMaKH.setText(String.format("KH%03d", so));
        } catch (Exception e) {
            txtMaKH.setText("KH001");
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<KhachHang> ds = dao.docDanhSachKhachHang();
            for (KhachHang kh : ds) {
                model.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getTenKhachHang(),
                        kh.getSDT(),
                        kh.getEmail(),
                        kh.getDiemTichLuy()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không load được dữ liệu khách hàng!");
        }
    }

    private void themKhachHang() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (tenKH.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        int diem = 0;
        try {
            if (!txtDiem.getText().trim().isEmpty()) {
                diem = Integer.parseInt(txtDiem.getText().trim());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số!");
            return;
        }

        KhachHang kh = new KhachHang(maKH, tenKH, sdt, email, diem);
        boolean ok = dao.themKhachHang(kh);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            clearForm();
            taoMaTuDong();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!");
        }
    }

    private void clearForm() {
        txtTenKH.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiem.setText("0");
    }
}
