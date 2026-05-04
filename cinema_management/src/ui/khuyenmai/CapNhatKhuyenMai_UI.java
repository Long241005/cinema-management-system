package ui.khuyenmai;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import ui.DateLabelFormatter;

public class CapNhatKhuyenMai_UI extends JPanel {

    private final Color MAU_NEN = new Color(48, 52, 56);
    private final Color MAU_THANH = new Color(60, 64, 68);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_XANH = new Color(33, 150, 243);
    private final Color MAU_DO = new Color(220, 53, 69);
    private final Color MAU_VANG = new Color(255, 193, 7);
    private final Color MAU_GRID = new Color(70, 72, 87);

    private JTextField txtMa, txtTen, txtPhanTram;
    private JDatePickerImpl dateBD, dateKT;

    private JTable table;
    private DefaultTableModel model;

    private KhuyenMai_DAO dao;

    public CapNhatKhuyenMai_UI() {
        dao = new KhuyenMai_DAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("SỬA KHUYẾN MÃI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(MAU_CHU);
        add(title, BorderLayout.NORTH);

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

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(input("Mã KM", txtMa = new JTextField()), gbc);

        gbc.gridx = 1;
        form.add(input("Tên KM", txtTen = new JTextField()), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(dateInput("Ngày bắt đầu", dateBD = createDatePicker()), gbc);

        gbc.gridx = 1;
        form.add(dateInput("Ngày kết thúc", dateKT = createDatePicker()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(input("Phần trăm giảm", txtPhanTram = new JTextField()), gbc);
        gbc.gridwidth = 1;

        txtMa.setEditable(false);

        main.add(form);

        // ===== BUTTON =====
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBtn.setOpaque(false);

        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa");
        JButton btnReload = new JButton("Reload");
        JButton btnClear = new JButton("Xóa trắng");

        btnSua.setBackground(MAU_XANH);
        btnXoa.setBackground(MAU_DO);
        btnReload.setBackground(MAU_THANH);
        btnClear.setBackground(MAU_VANG);

        btnSua.setForeground(Color.WHITE);
        btnXoa.setForeground(Color.WHITE);
        btnReload.setForeground(Color.WHITE);
        btnClear.setForeground(Color.BLACK);

        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnReload);
        pnlBtn.add(btnClear);

        main.add(pnlBtn);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{
                "Mã KM", "Tên", "%", "Ngày BD", "Ngày KT"
        }, 0);

        table = new JTable(model);
        styleTable();

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(900, 250));
        sp.getViewport().setBackground(new Color(31, 32, 44));
        sp.setBorder(new LineBorder(MAU_GRID));

        main.add(sp);

        add(main, BorderLayout.CENTER);

        loadData();

        // ===== EVENT =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();

                txtMa.setText(model.getValueAt(r, 0).toString());
                txtTen.setText(model.getValueAt(r, 1).toString());
                txtPhanTram.setText(model.getValueAt(r, 2).toString());

                setDate(dateBD, model.getValueAt(r, 3));
                setDate(dateKT, model.getValueAt(r, 4));
            }
        });

        btnSua.addActionListener(e -> capNhat());
        btnXoa.addActionListener(e -> xoa());
        btnReload.addActionListener(e -> loadData());
        btnClear.addActionListener(e -> clear());
    }

    // ===== ACTION =====
    private void capNhat() {
        try {
            KhuyenMai km = new KhuyenMai(
                    txtMa.getText(),
                    txtTen.getText(),
                    new BigDecimal(txtPhanTram.getText()),
                    getDate(dateBD),
                    getDate(dateKT)
            );

            if (dao.capNhatKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void xoa() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaKhuyenMai(txtMa.getText())) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadData();
                clear();
            }
        }
    }

    private void clear() {
        txtMa.setText("");
        txtTen.setText("");
        txtPhanTram.setText("");
        ((UtilDateModel) dateBD.getModel()).setValue(null);
        ((UtilDateModel) dateKT.getModel()).setValue(null);
    }

    // ===== LOAD =====
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

    // ===== DATE =====
    private JDatePickerImpl createDatePicker() {
        UtilDateModel m = new UtilDateModel();
        Properties p = new Properties();
        JDatePanelImpl panel = new JDatePanelImpl(m, p);
        return new JDatePickerImpl(panel, new DateLabelFormatter());
    }

    private LocalDate getDate(JDatePickerImpl picker) {
        Date d = (Date) picker.getModel().getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void setDate(JDatePickerImpl picker, Object value) {
        try {
            LocalDate ld = LocalDate.parse(value.toString());
            Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            ((UtilDateModel) picker.getModel()).setValue(date);
        } catch (Exception ignored) {}
    }

    // ===== UI =====
    private JPanel input(String label, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setForeground(MAU_CHU);

        txt.setPreferredSize(new Dimension(250, 32));
        txt.setBackground(MAU_THANH);
        txt.setForeground(MAU_CHU);

        p.add(l, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel dateInput(String label, JDatePickerImpl date) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setForeground(MAU_CHU);

        p.add(l, BorderLayout.NORTH);
        p.add(date, BorderLayout.CENTER);
        return p;
    }

    private void styleTable() {
        table.setRowHeight(35);
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(Color.WHITE);

        table.getTableHeader().setBackground(MAU_THANH);
        table.getTableHeader().setForeground(Color.WHITE);

        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);

        table.setGridColor(MAU_GRID);
        table.setShowGrid(true);
    }
}