package ui.khuyenmai;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

import dao.KhuyenMai_DAO;
import ui.DateLabelFormatter;

public class ThongKeKhuyenMai_UI extends JPanel {

    private final Color BG = new Color(36,37,42);
    private final Color CARD = new Color(50,52,60);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70,72,87);
    private final Color BLUE = new Color(33,150,243);

    private JDatePickerImpl dateTu, dateDen;
    private JComboBox<String> cboLoai;

    private JTable table;
    private DefaultTableModel model;

    private KhuyenMai_DAO dao;

    public ThongKeKhuyenMai_UI() {
        dao = new KhuyenMai_DAO();

        setLayout(new BorderLayout(15,15));
        setBackground(BG);
        setBorder(new EmptyBorder(20,25,20,25));

        add(createTop(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
    }

    // ===== TOP =====
    private JPanel createTop() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setOpaque(false);

        JLabel title = new JLabel("THỐNG KÊ KHUYẾN MÃI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        panel.add(title, BorderLayout.NORTH);

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT,15,10));
        filter.setOpaque(false);

        dateTu = createDatePicker();
        dateDen = createDatePicker();

        cboLoai = new JComboBox<>(new String[]{"Theo ngày","Theo tháng"});
        JButton btn = new JButton("Thống kê");

        btn.setBackground(BLUE);
        btn.setForeground(Color.WHITE);

        filter.add(new JLabel("Từ:"));
        filter.add(dateTu);
        filter.add(new JLabel("Đến:"));
        filter.add(dateDen);
        filter.add(cboLoai);
        filter.add(btn);

        btn.addActionListener(e -> loadData());

        panel.add(filter, BorderLayout.SOUTH);
        return panel;
    }

    // ===== TABLE =====
    private JScrollPane createTable() {
        model = new DefaultTableModel(new String[]{
                "Thời gian", "Số lượt sử dụng"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(32);
        table.setBackground(new Color(30,30,35));
        table.setForeground(TEXT);

        table.getTableHeader().setBackground(CARD);
        table.getTableHeader().setForeground(TEXT);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER));
        sp.getViewport().setBackground(new Color(30,30,35));

        return sp;
    }

    // ===== DATE PICKER =====
    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(panel, new DateLabelFormatter());
    }

    private LocalDate getDate(JDatePickerImpl picker){
        Date d = (Date) picker.getModel().getValue();
        if(d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ===== LOAD DATA =====
    private void loadData() {
        try {
            LocalDate tu = getDate(dateTu);
            LocalDate den = getDate(dateDen);

            if (tu == null || den == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                return;
            }

            boolean theoNgay = cboLoai.getSelectedIndex() == 0;

            List<Object[]> ds = theoNgay
                    ? dao.thongKeTheoNgay(tu, den)
                    : dao.thongKeTheoThang(tu, den);

            model.setRowCount(0);

            for (Object[] r : ds) {
                model.addRow(new Object[]{
                        r[0], // ngày hoặc tháng
                        r[1]  // số lượt
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!");
        }
    }
}