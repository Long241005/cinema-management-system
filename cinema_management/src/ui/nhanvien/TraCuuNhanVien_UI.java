package ui.nhanvien;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class TraCuuNhanVien_UI extends JPanel {

    // ===== Đồng bộ màu sắc với hệ thống =====
    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_THANH_TIM_KIEM = new Color(60, 64, 68);
    private final Color MAU_NHAN_XAM = new Color(100, 104, 124);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_VIEN_INPUT = new Color(70, 72, 87);
    private final Color MAU_XANH = new Color(33, 150, 243);

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtTimKiem;
    private JTextField txtMaNV, txtTenNV, txtGioiTinh, txtSDT, txtEmail, txtDiaChi, txtChucVu;

    private NhanVien_DAO nhanVienDAO;

    public TraCuuNhanVien_UI() {
        nhanVienDAO = new NhanVien_DAO();
        khoiTaoGiaoDien();
        loadData();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(0, 20));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("TRA CỨU NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(MAU_CHU);
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelMain = new JPanel(new BorderLayout(0, 20));
        panelMain.setOpaque(false);

        // ===== FORM THÔNG TIN =====
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 20, 15));
        pnlForm.setOpaque(false);

        txtMaNV = taoInput(pnlForm, "Mã nhân viên:");
        txtTenNV = taoInput(pnlForm, "Tên nhân viên:");
        txtGioiTinh = taoInput(pnlForm, "Giới tính:");
        txtSDT = taoInput(pnlForm, "Số điện thoại:");
        txtEmail = taoInput(pnlForm, "Email:");
        txtDiaChi = taoInput(pnlForm, "Địa chỉ:");
        txtChucVu = taoInput(pnlForm, "Chức vụ:");

        txtMaNV.setEditable(false);

        // ===== TÌM KIẾM =====
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm theo mã NV hoặc SĐT:");
        lblSearch.setForeground(MAU_CHU);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setBackground(MAU_THANH_TIM_KIEM);
        txtTimKiem.setForeground(MAU_CHU);
        txtTimKiem.setCaretColor(MAU_CHU);
        txtTimKiem.setBorder(new LineBorder(MAU_VIEN_INPUT));

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setBackground(MAU_XANH);
        btnTim.setForeground(MAU_CHU);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{
                "Mã NV", "Tên NV", "Giới tính", "SĐT", "Email", "Địa chỉ", "Chức vụ"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // chỉ xem, không cho sửa
            }
        };
        thietKeBang();

        JScrollPane scrollPane = new JScrollPane(table);
        tuyChinhScrollBar(scrollPane);
        scrollPane.setPreferredSize(new Dimension(0, 250));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);
        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(pnlSearch, BorderLayout.CENTER);
        pnlCenter.add(scrollPane, BorderLayout.SOUTH);

        panelMain.add(pnlCenter, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtMaNV.setText(model.getValueAt(row, 0).toString());
                    txtTenNV.setText(model.getValueAt(row, 1).toString());
                    txtGioiTinh.setText(model.getValueAt(row, 2).toString());
                    txtSDT.setText(model.getValueAt(row, 3).toString());
                    txtEmail.setText(model.getValueAt(row, 4).toString());
                    txtDiaChi.setText(model.getValueAt(row, 5).toString());
                    txtChucVu.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        btnTim.addActionListener(e -> timKiem());
    }

    private JTextField taoInput(JPanel pnl, String label) {
        JPanel wrap = new JPanel(new BorderLayout(5, 8));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(MAU_CHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(250, 40)); // tăng kích thước ô text
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBackground(MAU_THANH_TIM_KIEM);
        txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_VIEN_INPUT, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        pnl.add(wrap);

        return txt;
    }

    private void loadData() {
        model.setRowCount(0);
        List<NhanVien> ds = nhanVienDAO.docDanhSachNhanVien();

        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getSDT(),
                    nv.getEmail(),
                    nv.getDiaChi(),
                    nv.getChucVu()
            });
        }
    }

    private void timKiem() {
        String key = txtTimKiem.getText().trim();
        List<NhanVien> ds;

        if (key.startsWith("NV")) {
            ds = nhanVienDAO.timKiemTheoMa(key);
        } else {
            ds = nhanVienDAO.timKiemTheoSDT(key);
        }

        model.setRowCount(0);
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSDT(),
                    nv.getEmail(),
                    nv.getDiaChi(),
                    nv.getChucVu()
            });
        }
    }

    private void thietKeBang() {
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(MAU_THANH_TIM_KIEM);
        table.getTableHeader().setForeground(MAU_CHU);
        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);
    }

    private void tuyChinhScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MAU_NHAN_XAM;
                this.trackColor = MAU_NEN;
            }
        });
    }
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tra Cứu Nhân Viên");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new TraCuuNhanVien_UI());

            frame.setVisible(true);
        });
    }
}
