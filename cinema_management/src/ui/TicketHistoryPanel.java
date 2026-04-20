package ui;

import dao.TicketDAO;
import dao.ShowtimeDAO;
import dao.FilmDAO;
import entity.Ticket;
import entity.Showtime;
import entity.Film;
import services.AuthService;
import entity.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

public class TicketHistoryPanel extends JPanel {
    
    private TicketDAO ticketDAO;
    private ShowtimeDAO showtimeDAO;
    private FilmDAO filmDAO;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    
    public TicketHistoryPanel() {
        this.ticketDAO = new TicketDAO();
        this.showtimeDAO = new ShowtimeDAO();
        this.filmDAO = new FilmDAO();
        
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        loadTickets();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Lịch Sử Vé Của Tôi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(31, 32, 44));
        tablePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        tableModel = new DefaultTableModel(new Object[] {
            "ID Vé", "Phim", "Phòng", "Ghế", "Giờ Chiếu", "Giá", "Trạng Thái", "Ngày Mua"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ticketTable = new JTable(tableModel);
        ticketTable.setBackground(new Color(50, 50, 60));
        ticketTable.setForeground(Color.WHITE);
        ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketTable.getTableHeader().setBackground(new Color(241, 121, 104));
        ticketTable.getTableHeader().setForeground(Color.WHITE);
        ticketTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        ticketTable.setRowHeight(25);
        ticketTable.setSelectionBackground(new Color(241, 121, 104));
        
        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ticketTable.getSelectedRow();
                if (selectedRow >= 0) {
                    showTicketDetails(selectedRow);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setBackground(new Color(31, 32, 44));
        scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 60));
        scrollPane.getHorizontalScrollBar().setBackground(new Color(50, 50, 60));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(31, 32, 44));
        
        JButton refreshBtn = createButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadTickets());
        
        JButton viewDetailsBtn = createButton("Xem Chi Tiết");
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow >= 0) {
                showTicketDetails(selectedRow);
            }
        });
        
        JButton printBtn = createButton("In Vé");
        printBtn.addActionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow >= 0) {
                printTicket(selectedRow);
            }
        });
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(printBtn);
        
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void loadTickets() {
        tableModel.setRowCount(0);
        Customer currentUser = AuthService.getCurrentUser();
        
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Bạn cần đăng nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Ticket> tickets = ticketDAO.getTicketsByCustomerId(currentUser.getCustomerId());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Ticket ticket : tickets) {
            Showtime showtime = showtimeDAO.getShowtimeById(ticket.getShowtimeId());
            Film film = filmDAO.getFilmById(showtime.getFilmId());
            
            Vector<Object> row = new Vector<>();
            row.add(ticket.getTicketId());
            row.add(film.getFilmName());
            row.add("Phòng " + showtime.getTheaterId());
            row.add("A" + ticket.getSeatId()); // Simplified seat display
            row.add(showtime.getStartTime().format(dateFormatter));
            row.add(String.format("%.0f VND", ticket.getPrice()));
            row.add(ticket.getStatus());
            row.add(ticket.getBookingDate());
            
            tableModel.addRow(row);
        }
    }
    
    private void showTicketDetails(int row) {
        int ticketId = (int) tableModel.getValueAt(row, 0);
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        Showtime showtime = showtimeDAO.getShowtimeById(ticket.getShowtimeId());
        Film film = filmDAO.getFilmById(showtime.getFilmId());
        
        StringBuilder details = new StringBuilder();
        details.append("═════════════════════════════════════\n");
        details.append("CHI TIẾT VÉ\n");
        details.append("═════════════════════════════════════\n\n");
        details.append("ID Vé: ").append(ticket.getTicketId()).append("\n");
        details.append("Phim: ").append(film.getFilmName()).append("\n");
        details.append("Thể Loại: ").append(film.getGenre()).append("\n");
        details.append("Phòng: ").append(showtime.getTheaterId()).append("\n");
        details.append("Ghế: A").append(ticket.getSeatId()).append("\n");
        details.append("Thời Gian Chiếu: ").append(showtime.getStartTime()).append("\n");
        details.append("Giá Vé: ").append(String.format("%.0f VND", ticket.getPrice())).append("\n");
        details.append("Trạng Thái: ").append(ticket.getStatus()).append("\n");
        details.append("Ngày Mua: ").append(ticket.getBookingDate()).append("\n");
        details.append("\n═════════════════════════════════════\n");
        details.append("Mô Tả Phim:\n").append(film.getDescription()).append("\n");
        
        JOptionPane.showMessageDialog(this, details.toString(), "Chi Tiết Vé", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void printTicket(int row) {
        int ticketId = (int) tableModel.getValueAt(row, 0);
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        Showtime showtime = showtimeDAO.getShowtimeById(ticket.getShowtimeId());
        Film film = filmDAO.getFilmById(showtime.getFilmId());
        
        StringBuilder printContent = new StringBuilder();
        printContent.append("\n\n");
        printContent.append("╔═══════════════════════════════════════╗\n");
        printContent.append("║          RẠP CHIẾU PHIM T3L            ║\n");
        printContent.append("║              VÉ XEM PHIM               ║\n");
        printContent.append("╚═══════════════════════════════════════╝\n\n");
        printContent.append("VÉ NUMBER: ").append(String.format("%08d", ticketId)).append("\n");
        printContent.append("Phim: ").append(film.getFilmName()).append("\n");
        printContent.append("Phòng: ").append(showtime.getTheaterId()).append(" | Ghế: A").append(ticket.getSeatId()).append("\n");
        printContent.append("Giờ Chiếu: ").append(showtime.getStartTime()).append("\n");
        printContent.append("Giá: ").append(String.format("%.0f VND", ticket.getPrice())).append("\n");
        printContent.append("Trạng Thái: ").append(ticket.getStatus()).append("\n\n");
        printContent.append("═════════════════════════════════════════\n");
        printContent.append("Cảm ơn bạn đã lựa chọn Rạp T3L!\n");
        printContent.append("═════════════════════════════════════════\n\n");
        
        // In ra console (trong thực tế sẽ in qua printer)
        System.out.println(printContent.toString());
        
        JOptionPane.showMessageDialog(this, "Vé đã được in!\n(Chi tiết xem ở console)", "In Vé", JOptionPane.INFORMATION_MESSAGE);
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
