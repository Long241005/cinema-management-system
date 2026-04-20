package ui;

import dao.FilmDAO;
import entity.Film;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class MovieListPanel extends JPanel {
    
    private FilmDAO filmDAO;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    
    public MovieListPanel() {
        this.filmDAO = new FilmDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        loadMovies();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Danh Sách Phim");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(31, 32, 44));
        tablePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Tạo table model với các cột
        tableModel = new DefaultTableModel(new Object[] {
            "ID", "Tên Phim", "Đạo Diễn", "Thể Loại", "Thời Lượng", "Ngôn Ngữ", "Đánh Giá"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        movieTable = new JTable(tableModel);
        movieTable.setBackground(new Color(50, 50, 60));
        movieTable.setForeground(Color.WHITE);
        movieTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        movieTable.getTableHeader().setBackground(new Color(241, 121, 104));
        movieTable.getTableHeader().setForeground(Color.WHITE);
        movieTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        movieTable.setRowHeight(25);
        movieTable.setSelectionBackground(new Color(241, 121, 104));
        
        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.setBackground(new Color(31, 32, 44));
        scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 60));
        scrollPane.getHorizontalScrollBar().setBackground(new Color(50, 50, 60));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(31, 32, 44));
        
        JButton refreshBtn = createButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadMovies());
        
        JButton detailsBtn = createButton("Chi Tiết");
        detailsBtn.addActionListener(e -> showMovieDetails());
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(detailsBtn);
        
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void loadMovies() {
        tableModel.setRowCount(0);
        List<Film> films = filmDAO.getAllFilms();
        
        for (Film film : films) {
            Vector<Object> row = new Vector<>();
            row.add(film.getFilmId());
            row.add(film.getFilmName());
            row.add(film.getDirector());
            row.add(film.getGenre());
            row.add(film.getDuration() + " phút");
            row.add(film.getLanguage());
            row.add(film.getRating() + "/10");
            tableModel.addRow(row);
        }
    }
    
    private void showMovieDetails() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int filmId = (int) tableModel.getValueAt(selectedRow, 0);
        Film film = filmDAO.getFilmById(filmId);
        
        if (film != null) {
            String details = String.format(
                "Tên Phim: %s\nĐạo Diễn: %s\nThể Loại: %s\nThời Lượng: %d phút\n" +
                "Ngôn Ngữ: %s\nĐánh Giá: %.1f/10\n\nMô Tả:\n%s",
                film.getFilmName(),
                film.getDirector(),
                film.getGenre(),
                film.getDuration(),
                film.getLanguage(),
                film.getRating(),
                film.getDescription()
            );
            JOptionPane.showMessageDialog(this, details, "Chi Tiết Phim", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(241, 121, 104));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }
}
