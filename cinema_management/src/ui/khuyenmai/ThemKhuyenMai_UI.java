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

public class ThemKhuyenMai_UI extends JPanel {

    private final Color MAU_NEN   = new Color(48, 52, 56);
    private final Color MAU_THANH = new Color(60, 64, 68);
    private final Color MAU_CHU   = Color.WHITE;
    private final Color MAU_XANH  = new Color(33, 150, 243);
    private final Color MAU_DO    = new Color(220, 53, 69);
    private final Color MAU_GRID  = new Color(70, 72, 87);

    private JTextField txtMa, txtTen, txtPhanTram;
    private JDatePickerImpl dateBD, dateKT;

    private JTable table;
    private DefaultTableModel model;

    private KhuyenMai_DAO dao;

    public ThemKhuyenMai_UI() {
        dao = new KhuyenMai_DAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(MAU_NEN);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("THÊM KHUYẾN MÃI", SwingConstants.CENTER);
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

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(input("Mã KM", txtMa = new JTextField()), gbc);

        gbc.gridx = 1;
        form.add(input("Tên KM", txtTen = new JTextField()), gbc);

        // Row 2 (DatePicker)
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(dateInput("Ngày bắt đầu", dateBD = createDatePicker()), gbc);

        gbc.gridx = 1;
        form.add(dateInput("Ngày kết thúc", dateKT = createDatePicker()), gbc);

        // Row 3 full
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(input("Phần trăm giảm", txtPhanTram = new JTextField()), gbc);
        gbc.gridwidth = 1;

        txtMa.setEditable(false);
        txtMa.setText(dao.taoMaTuDong()); // 🔥 auto mã ban đầu

        main.add(form);

        // ===== BUTTON =====
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBtn.setOpaque(false);

        JButton btnThem   = new JButton("Thêm");
        JButton btnReload = new JButton("Reload");
        JButton btnClear  = new JButton("Xóa trắng");

        btnThem.setBackground(MAU_XANH);
        btnReload.setBackground(MAU_THANH);
        btnClear.setBackground(MAU_DO);

        btnThem.setForeground(Color.WHITE);
        btnReload.setForeground(Color.WHITE);
        btnClear.setForeground(Color.WHITE);

        pnlBtn.add(btnThem);
        pnlBtn.add(btnReload);
        pnlBtn.add(btnClear);

        main.add(pnlBtn);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{
                "Mã KM", "Tên", "% Giảm", "Ngày BD", "Ngày KT"
        }, 0);

        table = new JTable(model) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable();

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(950, 260));
        sp.getViewport().setBackground(new Color(31, 32, 44));
        sp.setBorder(new LineBorder(MAU_GRID));

        main.add(Box.createVerticalStrut(10));
        main.add(sp);

        add(main, BorderLayout.CENTER);

        // ===== LOAD =====
        loadData();

        // ===== EVENT =====
        btnThem.addActionListener(e -> them());
        btnReload.addActionListener(e -> loadData());
        btnClear.addActionListener(e -> clear());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r != -1) {
                    txtMa.setText(model.getValueAt(r, 0).toString());
                    txtTen.setText(model.getValueAt(r, 1).toString());
                    txtPhanTram.setText(model.getValueAt(r, 2).toString());

                    // set lại DatePicker từ bảng
                    setDate(dateBD, model.getValueAt(r, 3));
                    setDate(dateKT, model.getValueAt(r, 4));
                }
            }
        });
    }

    // ===== UI HELPERS =====
    private JPanel input(String label, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setForeground(MAU_CHU);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txt.setPreferredSize(new Dimension(250, 32));
        txt.setBackground(MAU_THANH);
        txt.setForeground(MAU_CHU);
        txt.setCaretColor(MAU_CHU);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(90, 90, 90)),
                new EmptyBorder(6, 10, 6, 10)
        ));

        p.add(l, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel dateInput(String label, JDatePickerImpl date) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setForeground(MAU_CHU);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));

        p.add(l, BorderLayout.NORTH);
        p.add(date, BorderLayout.CENTER);
        return p;
    }

    // ===== DATE PICKER =====
    private JDatePickerImpl createDatePicker() {
        UtilDateModel m = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl panel = new JDatePanelImpl(m, p);
        return new JDatePickerImpl(panel, new DateLabelFormatter());
    }

    private LocalDate getDate(JDatePickerImpl picker) {
        Date d = (Date) picker.getModel().getValue();
        if (d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void setDate(JDatePickerImpl picker, Object value) {
        try {
            if (value == null) return;

            LocalDate ld = (value instanceof LocalDate)
                    ? (LocalDate) value
                    : LocalDate.parse(value.toString());

            Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // 🔥 FIX LỖI Ở ĐÂY
            UtilDateModel model = (UtilDateModel) picker.getModel();
            model.setValue(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== TABLE STYLE =====
    private void styleTable() {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(new Color(31, 32, 44));
        table.setForeground(MAU_CHU);

        table.getTableHeader().setBackground(MAU_THANH);
        table.getTableHeader().setForeground(MAU_CHU);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionBackground(MAU_XANH);
        table.setSelectionForeground(Color.WHITE);

        table.setGridColor(MAU_GRID);
        table.setShowGrid(true);
    }

    // ===== DATA =====
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

    private void them() {
        try {
            LocalDate bd = getDate(dateBD);
            LocalDate kt = getDate(dateKT);

            if (bd == null || kt == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                return;
            }
            if (bd.isAfter(kt)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }

            KhuyenMai km = new KhuyenMai(
                    txtMa.getText().trim(),
                    txtTen.getText().trim(),
                    new BigDecimal(txtPhanTram.getText().trim()),
                    bd, kt
            );

            if (dao.themKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                txtMa.setText(dao.taoMaTuDong()); // 🔥 mã mới
                loadData();
                clear();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void clear() {
        txtTen.setText("");
        txtPhanTram.setText("");
        dateBD.getModel().setValue(null);
        dateKT.getModel().setValue(null);

        txtMa.setText(dao.taoMaTuDong()); // 🔥 luôn sinh mã mới
        txtTen.requestFocus();
    }
}