package ui;

import dao.FilmDAO;
import dao.ShowtimeDAO;
import entity.Film;
import entity.Showtime;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

public class ShowtimeSelectionPanel extends JPanel {
    
    private FilmDAO filmDAO;
    private ShowtimeDAO showtimeDAO;
    private JComboBox<String> filmCombo;
    private JTable showtimeTable;
    private DefaultTableModel tableModel;
    private Showtime selectedShowtime;
    
    public ShowtimeSelectionPanel(Runnable onShowtimeSelected) {
        this.filmDAO = new FilmDAO();
        this.showtimeDAO = new ShowtimeDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSelectionPanel(onShowtimeSelected), BorderLayout.CENTER);
        
        loadFilms();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Chọn Suất Chiếu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createSelectionPanel(Runnable onShowtimeSelected) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 32, 44));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Film selection
        JPanel filmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filmPanel.setBackground(new Color(31, 32, 44));
        
        JLabel filmLabel = new JLabel("Chọn Phim:");
        filmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filmLabel.setForeground(Color.WHITE);
        
        filmCombo = new JComboBox<>();
        filmCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filmCombo.setBackground(new Color(50, 50, 60));
        filmCombo.setForeground(Color.WHITE);
        filmCombo.setPreferredSize(new Dimension(300, 35));
        filmCombo.addActionListener(e -> loadShowtimes());
        
        filmPanel.add(filmLabel);
        filmPanel.add(filmCombo);
        
        panel.add(filmPanel, BorderLayout.NORTH);
        
        // Showtimes table
        tableModel = new DefaultTableModel(new Object[] {
            "ID", "Phòng", "Giờ Chiếu", "Thời Gian Kết Thúc", "Giá Vé", "Trạng Thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        showtimeTable = new JTable(tableModel);
        showtimeTable.setBackground(new Color(50, 50, 60));
        showtimeTable.setForeground(Color.WHITE);
        showtimeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showtimeTable.getTableHeader().setBackground(new Color(241, 121, 104));
        showtimeTable.getTableHeader().setForeground(Color.WHITE);
        showtimeTable.setRowHeight(25);
        showtimeTable.setSelectionBackground(new Color(241, 121, 104));
        
        showtimeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = showtimeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int showtimeId = (int) tableModel.getValueAt(selectedRow, 0);
                selectedShowtime = showtimeDAO.getShowtimeById(showtimeId);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(showtimeTable);
        scrollPane.setBackground(new Color(31, 32, 44));
        scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 60));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(31, 32, 44));
        
        JButton selectBtn = new JButton("Chọn Suất Chiếu");
        selectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectBtn.setBackground(new Color(241, 121, 104));
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFocusPainted(false);
        selectBtn.setPreferredSize(new Dimension(150, 40));
        selectBtn.addActionListener(e -> {
            if (selectedShowtime != null) {
                if (onShowtimeSelected != null) {
                    onShowtimeSelected.run();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(selectBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadFilms() {
        filmCombo.removeAllItems();
        List<Film> films = filmDAO.getAllFilms();
        for (Film film : films) {
            filmCombo.addItem(film.getFilmId() + " - " + film.getFilmName());
        }
    }
    
    private void loadShowtimes() {
        tableModel.setRowCount(0);
        
        String selectedItem = (String) filmCombo.getSelectedItem();
        if (selectedItem == null) return;
        
        int filmId = Integer.parseInt(selectedItem.split(" - ")[0]);
        List<Showtime> showtimes = showtimeDAO.getShowtimesByFilmId(filmId);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Showtime showtime : showtimes) {
            Vector<Object> row = new Vector<>();
            row.add(showtime.getShowtimeId());
            row.add("Phòng " + showtime.getTheaterId());
            row.add(showtime.getStartTime().format(dateFormatter));
            row.add(showtime.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            row.add(String.format("%.0f VND", showtime.getTicketPrice()));
            row.add(showtime.getStatus());
            tableModel.addRow(row);
        }
    }
    
    public Showtime getSelectedShowtime() {
        return selectedShowtime;
    }
}
