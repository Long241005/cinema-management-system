package ui;

import dao.FilmDAO;
import entity.Film;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class MovieManagementPanel extends JPanel {
    
    private FilmDAO filmDAO;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, directorField, genreField, durationField, languageField, ratingField;
    private JTextArea descriptionArea;
    
    public MovieManagementPanel() {
        this.filmDAO = new FilmDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        
        loadMovies();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Quản Lý Phim");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(31, 32, 44));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        formPanel.setPreferredSize(new Dimension(350, 0));
        
        // Tên Phim
        formPanel.add(createLabel("Tên Phim:"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Đạo Diễn
        formPanel.add(createLabel("Đạo Diễn:"));
        directorField = createTextField();
        formPanel.add(directorField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Thể Loại
        formPanel.add(createLabel("Thể Loại:"));
        genreField = createTextField();
        formPanel.add(genreField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Thời Lượng
        formPanel.add(createLabel("Thời Lượng (phút):"));
        durationField = createTextField();
        formPanel.add(durationField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Ngôn Ngữ
        formPanel.add(createLabel("Ngôn Ngữ:"));
        languageField = createTextField();
        formPanel.add(languageField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Đánh Giá
        formPanel.add(createLabel("Đánh Giá:"));
        ratingField = createTextField();
        formPanel.add(ratingField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Mô Tả
        formPanel.add(createLabel("Mô Tả:"));
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setBackground(new Color(50, 50, 60));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setCaretColor(Color.WHITE);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(new Color(31, 32, 44));
        
        JButton addBtn = createButton("Thêm");
        addBtn.addActionListener(e -> addMovie());
        
        JButton updateBtn = createButton("Sửa");
        updateBtn.addActionListener(e -> updateMovie());
        
        JButton deleteBtn = createButton("Xóa");
        deleteBtn.addActionListener(e -> deleteMovie());
        
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        
        formPanel.add(btnPanel);
        formPanel.add(Box.createVerticalGlue());
        
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        return formScroll;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(31, 32, 44));
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 20));
        
        tableModel = new DefaultTableModel(new Object[] {
            "ID", "Tên Phim", "Đạo Diễn", "Thể Loại", "Thời Lượng", "Đánh Giá"
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
        movieTable.setRowHeight(25);
        movieTable.setSelectionBackground(new Color(241, 121, 104));
        
        movieTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = movieTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadFormData(selectedRow);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.setBackground(new Color(31, 32, 44));
        scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 60));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
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
            row.add(film.getDuration());
            row.add(film.getRating());
            tableModel.addRow(row);
        }
    }
    
    private void loadFormData(int row) {
        int filmId = (int) tableModel.getValueAt(row, 0);
        Film film = filmDAO.getFilmById(filmId);
        
        if (film != null) {
            nameField.setText(film.getFilmName());
            directorField.setText(film.getDirector());
            genreField.setText(film.getGenre());
            durationField.setText(String.valueOf(film.getDuration()));
            languageField.setText(film.getLanguage());
            ratingField.setText(String.valueOf(film.getRating()));
            descriptionArea.setText(film.getDescription());
        }
    }
    
    private void addMovie() {
        if (!validateForm()) return;
        
        Film film = new Film();
        film.setFilmName(nameField.getText());
        film.setDirector(directorField.getText());
        film.setGenre(genreField.getText());
        film.setDuration(Integer.parseInt(durationField.getText()));
        film.setLanguage(languageField.getText());
        film.setRating(Double.parseDouble(ratingField.getText()));
        film.setDescription(descriptionArea.getText());
        
        if (filmDAO.addFilm(film)) {
            JOptionPane.showMessageDialog(this, "Thêm phim thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadMovies();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phim", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phim để sửa", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) return;
        
        int filmId = (int) tableModel.getValueAt(selectedRow, 0);
        Film film = new Film();
        film.setFilmId(filmId);
        film.setFilmName(nameField.getText());
        film.setDirector(directorField.getText());
        film.setGenre(genreField.getText());
        film.setDuration(Integer.parseInt(durationField.getText()));
        film.setLanguage(languageField.getText());
        film.setRating(Double.parseDouble(ratingField.getText()));
        film.setDescription(descriptionArea.getText());
        
        if (filmDAO.updateFilm(film)) {
            JOptionPane.showMessageDialog(this, "Cập nhật phim thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadMovies();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phim", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phim để xóa", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int filmId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa phim này?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (filmDAO.deleteFilm(filmId)) {
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadMovies();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa phim", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
            directorField.getText().trim().isEmpty() ||
            genreField.getText().trim().isEmpty() ||
            durationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        nameField.setText("");
        directorField.setText("");
        genreField.setText("");
        durationField.setText("");
        languageField.setText("");
        ratingField.setText("");
        descriptionArea.setText("");
        movieTable.clearSelection();
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }
    
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBackground(new Color(50, 50, 60));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setMaximumSize(new Dimension(300, 30));
        return tf;
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(241, 121, 104));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(80, 30));
        return btn;
    }
}
