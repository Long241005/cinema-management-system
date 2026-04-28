package ui.nhanvien;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class ThemNhanVien_UI extends JPanel {

    private final Color BG = new Color(48, 52, 56);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);

    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail, txtDiaChi;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboGioiTinh;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnThem, btnLamMoi;

    private NhanVien_DAO dao;

    public ThemNhanVien_UI() {
        dao = new NhanVien_DAO();
        initUI();
        taoMaTuDong();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        // LEFT FORM
        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(420, 0));

        JPanel form = new JPanel(new GridLayout(7, 1, 0, 12));
        form.setOpaque(false);

        txtMaNV = createInput(form, "Mã nhân viên:");
        txtTenNV = createInput(form, "Tên nhân viên:");
        cboGioiTinh = createCombo(form, "Giới tính:");
        txtSDT = createInput(form, "Số điện thoại:");
        txtEmail = createInput(form, "Email:");
        txtDiaChi = createInput(form, "Địa chỉ:");
        cboChucVu = createChucVuCombo(form, "Chức vụ:");

        txtMaNV.setEditable(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);

        btnThem = createButton("Thêm nhân viên");
        btnLamMoi = createButton("Làm mới");

        actions.add(btnThem);
        actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        // RIGHT TABLE
        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);

        JLabel lblDs = new JLabel("Danh sách nhân viên hiện có");
        lblDs.setForeground(TEXT);
        lblDs.setFont(new Font("Segoe UI", Font.BOLD, 18));

        model = new DefaultTableModel(new String[]{
                "Mã NV", "Tên NV", "Giới tính", "SĐT", "Chức vụ"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setRowHeight(34);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setBackground(new Color(60, 64, 68));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionBackground(new Color(33, 150, 243));
        table.setSelectionForeground(Color.WHITE);

        table.setGridColor(new Color(70, 72, 87));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(new Color(31, 32, 44));

        right.add(lblDs, BorderLayout.NORTH);
        right.add(scroll, BorderLayout.CENTER);

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themNhanVien());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            taoMaTuDong();
        });
    }

    private JTextField createInput(JPanel panel, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 6));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(250, 40));
        txt.setBackground(INPUT_BG);
        txt.setForeground(TEXT);
        txt.setCaretColor(TEXT);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        panel.add(wrap);
        return txt;
    }

    private JComboBox<String> createChucVuCombo(JPanel panel, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 6));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> cbo = new JComboBox<>(new String[]{"Quản Lý", "Nhân viên bán vé"});
        cbo.setPreferredSize(new Dimension(250, 40));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(cbo, BorderLayout.CENTER);
        panel.add(wrap);
        return cbo;
    }

    private JComboBox<String> createCombo(JPanel panel, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 6));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> cbo = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbo.setPreferredSize(new Dimension(250, 40));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(cbo, BorderLayout.CENTER);
        panel.add(wrap);
        return cbo;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setBackground(BLUE);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    private void taoMaTuDong() {
        int so = dao.demSoLuongNhanVien() + 1;
        txtMaNV.setText(String.format("NV%03d", so));
    }

    private void loadData() {
        model.setRowCount(0);
        List<NhanVien> ds = dao.docDanhSachNhanVien();

        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getSDT(),
                    nv.getChucVu()
            });
        }
    }

    private void themNhanVien() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        boolean gioiTinh = cboGioiTinh.getSelectedItem().toString().equals("Nam");
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String chucVu = cboChucVu.getSelectedItem().toString();

        if (tenNV.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NhanVien nv = new NhanVien(
                maNV,
                tenNV,
                gioiTinh,
                "",
                sdt,
                email,
                diaChi,
                chucVu
        );

        boolean ketQua = dao.themNhanVien(nv);

        if (ketQua) {
            JOptionPane.showMessageDialog(this,
                    "Thêm nhân viên thành công!");
            clearForm();
            taoMaTuDong();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm nhân viên thất bại!");
        }
    }

    private void clearForm() {
        txtTenNV.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        cboChucVu.setSelectedIndex(0);
        cboGioiTinh.setSelectedIndex(0);
    }
}
