package ui.suatchieu;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.*;
import dao.*;
import entity.*;
import java.text.*;
import java.util.Calendar;
import java.util.List;

public class ThemSuatChieu_UI extends JPanel {
    private final Color BG = new Color(48, 52, 56);
    private final Color INPUT_BG = new Color(60, 64, 68);
    private final Color TEXT = Color.WHITE;
    private final Color BORDER = new Color(70, 72, 87);
    private final Color BLUE = new Color(33, 150, 243);

    private JTextField txtMaSC, txtGiaVe;
    private JComboBox<Phim> cboPhim;
    private JComboBox<PhongChieu> cboPhong;
    private JDatePickerImpl datePicker;
    private JSpinner spnGio, spnPhut;
    private DefaultTableModel model;
    private JTable table;

    private SuatChieu_DAO dao = new SuatChieu_DAO();
    private DateTimeFormatter dtfNgay = DateTimeFormatter.ofPattern("d/M/yyyy");
    private DecimalFormat dfTien = new DecimalFormat("#,### VNĐ");

    public ThemSuatChieu_UI() {
        initUI();
        napDuLieuCombo();
        taoMaTuDong();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("THÊM SUẤT CHIẾU MỚI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        // FORM NHẬP LIỆU (BÊN TRÁI)
        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(420, 0));

        JPanel form = new JPanel(new GridLayout(7, 1, 0, 12));
        form.setOpaque(false);

        txtMaSC = createInput(form, "Mã suất chiếu (Tự động):");
        txtMaSC.setEditable(false);
        cboPhim = createComboPhim(form, "Chọn phim:");
        cboPhong = createComboPhong(form, "Chọn phòng:");
        form.add(createDatePickerWrapper("Ngày chiếu (d/M/yyyy):"));
        form.add(createTimeSpinner("Giờ bắt đầu:"));
        
        // GIÁ VÉ LẤY TRỰC TIẾP TỪ Ô NHẬP TEXT
        txtGiaVe = createInput(form, "Giá vé cho suất này (VNĐ):");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);
        JButton btnThem = createButton("Thêm suất chiếu");
        JButton btnLamMoi = createButton("Làm mới");
        actions.add(btnThem);
        actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        // BẢNG HIỂN THỊ (BÊN PHẢI)
        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);

        model = new DefaultTableModel(new String[]{"Mã Suất", "Tên Phim", "Phòng", "Thời Gian", "Giá Vé"}, 0);
        table = new JTable(model);
        thietKeBang(table);

        right.add(new JScrollPane(table), BorderLayout.CENTER);

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themSuatChieu());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            taoMaTuDong();
        });
    }

    private void taoMaTuDong() {
        String maMax = dao.getMaSuatChieuLonNhat();
        int so = 1;
        try {
            so = Integer.parseInt(maMax.substring(2)) + 1;
        } catch (Exception e) {
            so = 1;
        }
        txtMaSC.setText(String.format("SC%04d", so));
    }

    private void themSuatChieu() {
        try {
            LocalDate ngay;
            String inputNgay = datePicker.getJFormattedTextField().getText().trim();

            if (!inputNgay.isEmpty()) {
                ngay = LocalDate.parse(inputNgay, dtfNgay);
            } else {
                java.util.Date d = (java.util.Date) datePicker.getModel().getValue();
                ngay = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            if (ngay.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Ngày chiếu không được ở quá khứ!");
                return;
            }

            // LẤY GIÁ VÉ TỪ Ô NHẬP TEXT
            String giaVeText = txtGiaVe.getText().trim();
            if (giaVeText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giá vé!");
                return;
            }
            BigDecimal giaVeInput = new BigDecimal(giaVeText);
            
            LocalTime gio = LocalTime.of((int) spnGio.getValue(), (int) spnPhut.getValue());

            SuatChieu sc = new SuatChieu(
                    txtMaSC.getText(),
                    (PhongChieu) cboPhong.getSelectedItem(),
                    (Phim) cboPhim.getSelectedItem(),
                    ngay,
                    LocalDateTime.of(ngay, gio)
            );

            if (dao.themSuatChieu(sc, giaVeInput)) { 
                JOptionPane.showMessageDialog(this, "Thành công! Giá vé: " + dfTien.format(giaVeInput));
                loadData();
                taoMaTuDong();
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu! Kiểm tra ngày (d/M/yyyy) và giá vé.");
        }
    }

    private void loadData() {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Gọi hàm lấy dữ liệu tổng hợp (đã có giá vé từ Ve)
        List<Object[]> data = dao.getAllSuatChieuWithPrice();
        
        for (Object[] obj : data) {
            model.addRow(new Object[]{
                obj[0], // maSC
                obj[1], // tenPhim
                obj[2], // tenPhong
                ((LocalDateTime)obj[3]).format(dtf), // gioChieu
                df.format(obj[4]) // giaVe lấy từ lớp Ve đã được định dạng[cite: 6]
            });
        }
    }

    private JPanel createDatePickerWrapper(String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT);
        UtilDateModel m = new UtilDateModel();
        Properties pr = new Properties(); pr.put("text.today", "Hôm nay");
        datePicker = new JDatePickerImpl(new JDatePanelImpl(m, pr), new DateComponentFormatter());
        datePicker.getJFormattedTextField().setEditable(true);
        datePicker.getJFormattedTextField().setBackground(INPUT_BG);
        datePicker.getJFormattedTextField().setForeground(TEXT);
        w.add(lbl, BorderLayout.NORTH); w.add(datePicker, BorderLayout.CENTER);
        return w;
    }

    private JTextField createInput(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT);
        JTextField t = new JTextField(); t.setBackground(INPUT_BG); t.setForeground(TEXT);
        t.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        w.add(lbl, BorderLayout.NORTH); w.add(t, BorderLayout.CENTER);
        p.add(w); return t;
    }

    private JComboBox<Phim> createComboPhim(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT);
        cboPhim = new JComboBox<>(); w.add(lbl, BorderLayout.NORTH); w.add(cboPhim, BorderLayout.CENTER);
        p.add(w); return cboPhim;
    }

    private JComboBox<PhongChieu> createComboPhong(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT);
        cboPhong = new JComboBox<>(); w.add(lbl, BorderLayout.NORTH); w.add(cboPhong, BorderLayout.CENTER);
        p.add(w); return cboPhong;
    }

    private JPanel createTimeSpinner(String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT);
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); p.setOpaque(false);
        spnGio = new JSpinner(new SpinnerNumberModel(20, 0, 23, 1));
        spnPhut = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        p.add(spnGio); p.add(new JLabel(":") {{ setForeground(TEXT); }}); p.add(spnPhut);
        w.add(lbl, BorderLayout.NORTH); w.add(p, BorderLayout.CENTER);
        return w;
    }

    private JButton createButton(String t) {
        JButton b = new JButton(t); b.setPreferredSize(new Dimension(160, 42));
        b.setBackground(BLUE); b.setForeground(TEXT); b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }

    private void thietKeBang(JTable t) {
        t.setRowHeight(34); t.setBackground(new Color(31, 32, 44)); t.setForeground(TEXT);
        t.getTableHeader().setBackground(INPUT_BG); t.getTableHeader().setForeground(TEXT);
    }

    private void napDuLieuCombo() {
        new Phim_DAO().docDanhSachPhim().forEach(cboPhim::addItem);
        new PhongChieu_DAO().docDanhSachPhongChieu().forEach(cboPhong::addItem);
    }

    private void clearForm() {
        datePicker.getModel().setValue(null);
        datePicker.getJFormattedTextField().setText("");
        txtGiaVe.setText("");
    }
}