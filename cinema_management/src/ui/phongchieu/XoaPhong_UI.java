package ui.phongchieu;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.PhongChieu_DAO;
import entity.PhongChieu;

public class XoaPhong_UI extends JPanel {

    private final Color BG = new Color(48, 52, 56);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color RED = new Color(220, 53, 69);
    private final Color BLUE = new Color(33, 150, 243);

    private JTextField txtMaPhong, txtTenPhong, txtSoGhe, txtLoaiPhong;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnXoa, btnLamMoi;

    private PhongChieu_DAO dao;

    public XoaPhong_UI() {
        dao = new PhongChieu_DAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // TIÊU ĐỀ
        JLabel lblTitle = new JLabel("XÓA PHÒNG CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        // FORM HIỂN THỊ THÔNG TIN (BÊN TRÁI)
        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(430, 0));

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 12));
        form.setOpaque(false);

        txtMaPhong = createInput(form, "Mã phòng chiếu:");
        txtTenPhong = createInput(form, "Tên phòng chiếu:");
        txtSoGhe = createInput(form, "Số lượng ghế:");
        txtLoaiPhong = createInput(form, "Loại phòng:");

        // Vô hiệu hóa chỉnh sửa để chỉ dùng để xem trước khi xóa[cite: 4]
        txtMaPhong.setEditable(false);
        txtTenPhong.setEditable(false);
        txtSoGhe.setEditable(false);
        txtLoaiPhong.setEditable(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);

        btnXoa = createButton("Xóa phòng", RED);
        btnLamMoi = createButton("Làm mới", BLUE);

        actions.add(btnXoa);
        actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        // BẢNG DANH SÁCH (BÊN PHẢI)
        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);

        JLabel lblDs = new JLabel("Danh sách phòng chiếu hiện có");
        lblDs.setForeground(TEXT);
        lblDs.setFont(new Font("Segoe UI", Font.BOLD, 18));

        model = new DefaultTableModel(new String[]{
                "Mã Phòng", "Tên Phòng", "Số Ghế", "Loại Phòng"
        }, 0);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Thiết kế bảng đồng bộ[cite: 2, 4]
        table.setRowHeight(34);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(60, 64, 68));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(BLUE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(new Color(31, 32, 44));

        right.add(lblDs, BorderLayout.NORTH);
        right.add(scroll, BorderLayout.CENTER);

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Sự kiện click bảng[cite: 4]
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    hienThiLenForm(row);
                }
            }
        });

        btnXoa.addActionListener(e -> xoaPhong());
        btnLamMoi.addActionListener(e -> clearForm());
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
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(txt, BorderLayout.CENTER);
        panel.add(wrap);
        return txt;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setBackground(color);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    private void hienThiLenForm(int row) {
        txtMaPhong.setText(model.getValueAt(row, 0).toString());
        txtTenPhong.setText(model.getValueAt(row, 1).toString());
        txtSoGhe.setText(model.getValueAt(row, 2).toString());
        txtLoaiPhong.setText(model.getValueAt(row, 3).toString());
    }

    private void loadData() {
        model.setRowCount(0);
        List<PhongChieu> ds = dao.docDanhSachPhongChieu();
        for (PhongChieu p : ds) {
            model.addRow(new Object[]{p.getMaPhong(), p.getTenPhong(), p.getSoGhe(), p.getLoaiPhong()});
        }
    }

    private void xoaPhong() {
        String ma = txtMaPhong.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu cần xóa từ danh sách!");
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa phòng này? Điều này có thể ảnh hưởng đến ghế và lịch chiếu.", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ketQua = dao.xoaPhongChieu(ma);
            if (ketQua) {
                JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thành công!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Có thể do ràng buộc dữ liệu (ghế hoặc lịch chiếu).");
            }
        }
    }

    private void clearForm() {
        txtMaPhong.setText("");
        txtTenPhong.setText("");
        txtSoGhe.setText("");
        txtLoaiPhong.setText("");
        table.clearSelection();
    }
}