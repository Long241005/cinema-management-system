package ui;

import dao.SeatDAO;
import entity.Seat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionPanel extends JPanel {
    
    private int theaterId;
    private SeatDAO seatDAO;
    private List<Seat> allSeats;
    private List<Integer> selectedSeatIds;
    private List<SeatButton> seatButtons;
    
    private double totalPrice = 0;
    
    public SeatSelectionPanel(int theaterId) {
        this.theaterId = theaterId;
        this.seatDAO = new SeatDAO();
        this.selectedSeatIds = new ArrayList<>();
        this.seatButtons = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSeatMapPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        
        loadSeats();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Chọn Ghế Ngồi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createSeatMapPanel() {
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.Y_AXIS));
        mapPanel.setBackground(new Color(31, 32, 44));
        mapPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        legendPanel.setBackground(new Color(31, 32, 44));
        
        legendPanel.add(createLegendItem("Còn trống", new Color(100, 150, 100)));
        legendPanel.add(createLegendItem("Đã chọn", new Color(241, 121, 104)));
        legendPanel.add(createLegendItem("Đã bán", new Color(200, 50, 50)));
        
        mapPanel.add(legendPanel);
        mapPanel.add(Box.createVerticalStrut(20));
        
        // Seat grid
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(new Color(31, 32, 44));
        gridPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Group seats by row
        String[] rows = {"A", "B", "C", "D", "E"};
        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
            rowPanel.setBackground(new Color(31, 32, 44));
            
            JLabel rowLabel = new JLabel(row);
            rowLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            rowLabel.setForeground(Color.WHITE);
            rowLabel.setPreferredSize(new Dimension(30, 30));
            rowPanel.add(rowLabel);
            
            for (int i = 1; i <= 10; i++) {
                Seat seat = findSeatByRowAndNumber(row, i);
                SeatButton seatBtn = new SeatButton(seat);
                seatBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (seat.getStatus().equals("AVAILABLE")) {
                            seatBtn.toggleSelection();
                            if (seatBtn.isSelected()) {
                                selectedSeatIds.add(seat.getSeatId());
                                totalPrice += seat.getPrice();
                            } else {
                                selectedSeatIds.remove(Integer.valueOf(seat.getSeatId()));
                                totalPrice -= seat.getPrice();
                            }
                            updatePriceLabel();
                        }
                    }
                });
                seatButtons.add(seatBtn);
                rowPanel.add(seatBtn);
            }
            
            gridPanel.add(rowPanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBackground(new Color(31, 32, 44));
        mapPanel.add(scrollPane);
        
        return mapPanel;
    }
    
    private JPanel createLegendItem(String label, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        item.setBackground(new Color(31, 32, 44));
        
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        
        JLabel label_text = new JLabel(label);
        label_text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label_text.setForeground(Color.WHITE);
        
        item.add(colorBox);
        item.add(label_text);
        
        return item;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(31, 32, 44));
        bottom.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel priceLabel = new JLabel("Tổng tiền: 0 VND");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(241, 121, 104));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(31, 32, 44));
        JLabel selectedLabel = new JLabel("Ghế đã chọn: 0");
        selectedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectedLabel.setForeground(Color.WHITE);
        
        infoPanel.add(selectedLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(priceLabel);
        
        bottom.add(infoPanel, BorderLayout.CENTER);
        
        return bottom;
    }
    
    private void updatePriceLabel() {
        revalidate();
        repaint();
    }
    
    private void loadSeats() {
        allSeats = seatDAO.getSeatsByTheaterId(theaterId);
    }
    
    private Seat findSeatByRowAndNumber(String row, int number) {
        for (Seat seat : allSeats) {
            if (seat.getSeatRow().equals(row) && seat.getSeatNumber() == number) {
                return seat;
            }
        }
        return new Seat(0, theaterId, row, number, "STANDARD", 80000, "AVAILABLE");
    }
    
    public List<Integer> getSelectedSeatIds() {
        return selectedSeatIds;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    // Inner class cho nút ghế
    private class SeatButton extends JButton {
        private Seat seat;
        private boolean selected = false;
        
        public SeatButton(Seat seat) {
            this.seat = seat;
            setText(seat.getSeatRow() + seat.getSeatNumber());
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setPreferredSize(new Dimension(35, 35));
            setFocusPainted(false);
            updateColor();
        }
        
        public void toggleSelection() {
            selected = !selected;
            updateColor();
        }
        
        public boolean isSelected() {
            return selected;
        }
        
        private void updateColor() {
            if (seat.getStatus().equals("BOOKED")) {
                setBackground(new Color(200, 50, 50));
                setForeground(Color.WHITE);
                setEnabled(false);
            } else if (selected) {
                setBackground(new Color(241, 121, 104));
                setForeground(Color.WHITE);
                setEnabled(true);
            } else {
                setBackground(new Color(100, 150, 100));
                setForeground(Color.WHITE);
                setEnabled(true);
            }
        }
    }
}
