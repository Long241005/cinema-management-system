package ui.suatchieu;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.*;
import dao.*;
import entity.*;

public class CapNhatSuatChieu_UI extends JPanel {
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
    private JTable table;
    private DefaultTableModel model;
    private JButton btnCapNhat, btnLamMoi;

    private SuatChieu_DAO scDAO = new SuatChieu_DAO();
    private Phim_DAO phimDAO = new Phim_DAO();
    private PhongChieu_DAO phongDAO = new PhongChieu_DAO();
    
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DecimalFormat df = new DecimalFormat("#,###");

    public CapNhatSuatChieu_UI() {
        initUI();
        napDuLieuCombo();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("CẬP NHẬT SUẤT CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT);
        add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(430, 0));

        JPanel form = new JPanel(new GridLayout(7, 1, 0, 12));
        form.setOpaque(false);

        txtMaSC = createInput(form, "Mã suất chiếu (Không thể sửa):");
        txtMaSC.setEditable(false); 
        
        cboPhim = createComboPhim(form, "Chọn phim chiếu:");
        cboPhong = createComboPhong(form, "Chọn phòng chiếu:");
        form.add(createDatePickerWrapper("Ngày chiếu (dd/MM/yyyy):"));
        form.add(createTimeSpinner("Giờ bắt đầu:"));
        txtGiaVe = createInput(form, "Giá vé cho suất này (VNĐ):");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actions.setOpaque(false);
        btnCapNhat = createButton("Cập nhật dữ liệu");
        btnLamMoi = createButton("Làm mới form");
        actions.add(btnCapNhat); actions.add(btnLamMoi);

        left.add(form, BorderLayout.CENTER);
        left.add(actions, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(0, 15));
        right.setOpaque(false);
        model = new DefaultTableModel(new String[]{"Mã Suất", "Phim", "Phòng", "Thời Gian", "Giá Vé"}, 0);
        table = new JTable(model);
        thietKeBang(table);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { fillFormFromTable(); }
        });

        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnLamMoi.addActionListener(e -> clearForm());
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtMaSC.setText(model.getValueAt(row, 0).toString());
            
            String tPhim = model.getValueAt(row, 1).toString();
            for(int i=0; i<cboPhim.getItemCount(); i++)
                if(cboPhim.getItemAt(i).getTenPhim().equals(tPhim)) cboPhim.setSelectedIndex(i);

            String tPhong = model.getValueAt(row, 2).toString();
            for(int i=0; i<cboPhong.getItemCount(); i++)
                if(cboPhong.getItemAt(i).getTenPhong().equals(tPhong)) cboPhong.setSelectedIndex(i);

            LocalDateTime batDau = LocalDateTime.parse(model.getValueAt(row, 3).toString(), dtf);
            datePicker.getModel().setDate(batDau.getYear(), batDau.getMonthValue()-1, batDau.getDayOfMonth());
            datePicker.getModel().setSelected(true);
            spnGio.setValue(batDau.getHour());
            spnPhut.setValue(batDau.getMinute());

            // Lấy giá vé từ bảng (xóa dấu phẩy phân cách nghìn để đưa về dạng số)
            String giaVeStr = model.getValueAt(row, 4).toString().replace(",", "");
            txtGiaVe.setText(giaVeStr);
        }
    }

    private void xuLyCapNhat() {
        if (txtMaSC.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu cần sửa!");
            return;
        }
        try {
            java.util.Date d = (java.util.Date) datePicker.getModel().getValue();
            LocalDate ngay = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime gio = LocalTime.of((int)spnGio.getValue(), (int)spnPhut.getValue());
            LocalDateTime batDau = LocalDateTime.of(ngay, gio);
            
            // Lấy giá vé mới từ ô nhập văn bản[cite: 4, 5]
            BigDecimal giaVeMoi = new BigDecimal(txtGiaVe.getText().trim());

            SuatChieu sc = new SuatChieu(txtMaSC.getText(), (PhongChieu)cboPhong.getSelectedItem(), 
                                        (Phim)cboPhim.getSelectedItem(), ngay, batDau);
            
            // 1. Cập nhật thông tin Suất Chiếu[cite: 4, 6]
            boolean checkSC = scDAO.capNhatSuatChieu(sc);
            
            // 2. Cập nhật Giá Vé trong bảng Ve
            boolean checkGia = scDAO.capNhatGiaVe(sc.getMaSC(), giaVeMoi);

            if (checkSC && checkGia) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công suất chiếu và giá vé!");
                loadData(); // Tải lại bảng để hiện con số mới[cite: 4, 5]
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu! Kiểm tra lại định dạng số tiền.");
        }
    }

    private void loadData() {
        model.setRowCount(0);
        // Gọi hàm lấy dữ liệu JOIN từ bảng Ve để có giá vé[cite: 6]
        List<Object[]> data = scDAO.getAllSuatChieuWithPrice(); 
        
        for (Object[] obj : data) {
            String giaVeHienThi = (obj[4] != null) ? df.format(obj[4]) : "0";
            
            model.addRow(new Object[]{
                obj[0], // maSC
                obj[1], // tenPhim
                obj[2], // tenPhong
                ((LocalDateTime)obj[3]).format(dtf), // Thời gian format dd/MM/yyyy HH:mm
                giaVeHienThi // Hiển thị giá vé thực tế lấy từ lớp Ve[cite: 6]
            });
        }
    }

    private JTextField createInput(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField t = new JTextField(); t.setBackground(INPUT_BG); t.setForeground(TEXT);
        t.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        w.add(lbl, BorderLayout.NORTH); w.add(t, BorderLayout.CENTER);
        p.add(w); return t;
    }

    private JComboBox<Phim> createComboPhim(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cboPhim = new JComboBox<>(); w.add(lbl, BorderLayout.NORTH); w.add(cboPhim, BorderLayout.CENTER);
        p.add(w); return cboPhim;
    }

    private JComboBox<PhongChieu> createComboPhong(JPanel p, String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cboPhong = new JComboBox<>(); w.add(lbl, BorderLayout.NORTH); w.add(cboPhong, BorderLayout.CENTER);
        p.add(w); return cboPhong;
    }

    private JPanel createDatePickerWrapper(String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        UtilDateModel m = new UtilDateModel();
        Properties pr = new Properties(); pr.put("text.today", "Hôm nay");
        datePicker = new JDatePickerImpl(new JDatePanelImpl(m, pr), new DateComponentFormatter());
        w.add(lbl, BorderLayout.NORTH); w.add(datePicker, BorderLayout.CENTER);
        return w;
    }
    

    private JPanel createTimeSpinner(String l) {
        JPanel w = new JPanel(new BorderLayout(5, 6)); w.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setForeground(TEXT); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); pnl.setOpaque(false);
        spnGio = new JSpinner(new SpinnerNumberModel(20, 0, 23, 1));
        spnPhut = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        pnl.add(spnGio); pnl.add(new JLabel(":") {{ setForeground(TEXT); }}); pnl.add(spnPhut);
        w.add(lbl, BorderLayout.NORTH); w.add(pnl, BorderLayout.CENTER);
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
        t.setSelectionBackground(BLUE);
    }

    private void napDuLieuCombo() {
        phimDAO.docDanhSachPhim().forEach(cboPhim::addItem);
        phongDAO.docDanhSachPhongChieu().forEach(cboPhong::addItem);
    }

    private void clearForm() {
        txtMaSC.setText("");
        txtGiaVe.setText("");
        datePicker.getModel().setValue(null);
        table.clearSelection();
    }
    
}