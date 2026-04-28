package ui.khachhang;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_DAO;
import entity.KhachHang;

public class CapNhatKhachHang_UI extends JPanel {

    private final Color BG = new Color(48, 52, 56);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);

    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JTextField txtDiem;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnCapNhat;
    private JButton btnLamMoi;

    private KhachHang_DAO dao;

    public CapNhatKhachHang_UI() {
        dao = new KhachHang_DAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("CẬP NHẬT KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        // ===== LEFT FORM =====
        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(430, 0));

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 14));
        form.setOpaque(false);

        txtMaKH = createInput(form, "Mã khách hàng:");
        txtTenKH = createInput(form, "Tên khách hàng:");
        txtSDT = createInput(form, "Số điện thoại:");
        txtEmail = createInput(form, "Email:");
        txtDiem = createInput(form, "Điểm tích lũy:");

        txtMaKH.setEditable(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);

        btnCapNhat = createButton("Cập nhật");
        btnLamMoi = createButton("Làm mới");

        actions.add(btnCapNhat);
        actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        // ===== RIGHT TABLE =====
        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);

        JLabel lblDs = new JLabel("Danh sách khách hàng");
        lblDs.setForeground(TEXT);
        lblDs.setFont(new Font("Segoe UI", Font.BOLD, 18));

        model = new DefaultTableModel(new String[]{
                "Mã KH", "Tên khách hàng", "SĐT", "Email", "Điểm"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // ===== TABLE STYLE =====
        table.setRowHeight(34);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setBackground(new Color(60, 64, 68));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionBackground(BLUE);
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

        // ===== EVENT CLICK TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();

                if (row != -1) {
                    txtMaKH.setText(model.getValueAt(row, 0).toString());
                    txtTenKH.setText(model.getValueAt(row, 1).toString());
                    txtSDT.setText(model.getValueAt(row, 2).toString());
                    txtEmail.setText(model.getValueAt(row, 3).toString());
                    txtDiem.setText(model.getValueAt(row, 4).toString());
                }
            }
        });

        btnCapNhat.addActionListener(e -> capNhatKhachHang());

        btnLamMoi.addActionListener(e -> clearForm());
    }

    // ===== INPUT UI ĐẸP - ĐỀU =====
    private JTextField createInput(JPanel panel, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 8));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(260, 38));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(INPUT_BG);
        txt.setForeground(TEXT);
        txt.setCaretColor(TEXT);

        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);

        panel.add(wrap);
        return txt;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(170, 42));
        btn.setBackground(BLUE);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    // ===== LOAD DATA =====
    private void loadData() {
        model.setRowCount(0);

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
    }

    // ===== UPDATE =====
    private void capNhatKhachHang() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        int diem;

        try {
            diem = Integer.parseInt(txtDiem.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Điểm tích lũy phải là số!");
            return;
        }

        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng cần cập nhật!");
            return;
        }

        KhachHang kh = new KhachHang(
                maKH,
                tenKH,
                sdt,
                email,
                diem
        );

        boolean kq = dao.capNhatKhachHang(kh);

        if (kq) {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật khách hàng thành công!");

            loadData();
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thất bại!");
        }
    }

    // ===== CLEAR =====
    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiem.setText("");

        table.clearSelection();
    }
}