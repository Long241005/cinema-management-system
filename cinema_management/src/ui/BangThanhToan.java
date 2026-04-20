package ui;

import services.BookingService;
import services.AuthService;
import entity.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PaymentPanel extends JPanel {
    
    private BookingService bookingService;
    private int showtimeId;
    private List<Integer> selectedSeatIds;
    private double totalPrice;
    
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JComboBox<String> paymentMethodCombo;
    private JTextArea orderSummaryArea;
    private Runnable onPaymentSuccess;
    
    public PaymentPanel(int showtimeId, List<Integer> seatIds, double price, Runnable onSuccess) {
        this.bookingService = new BookingService();
        this.showtimeId = showtimeId;
        this.selectedSeatIds = seatIds;
        this.totalPrice = price;
        this.onPaymentSuccess = onSuccess;
        
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createPaymentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 32, 44));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Xác Nhận Thanh Toán");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 32, 44));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Left panel - Order summary
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(31, 32, 44));
        
        JLabel summaryLabel = new JLabel("Tóm Tắt Đơn Hàng");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryLabel.setForeground(Color.WHITE);
        leftPanel.add(summaryLabel, BorderLayout.NORTH);
        
        orderSummaryArea = new JTextArea();
        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orderSummaryArea.setBackground(new Color(50, 50, 60));
        orderSummaryArea.setForeground(Color.WHITE);
        orderSummaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        orderSummaryArea.setLineWrap(true);
        orderSummaryArea.setWrapStyleWord(true);
        
        updateOrderSummary();
        
        JScrollPane scrollPane = new JScrollPane(orderSummaryArea);
        scrollPane.setBorder(null);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right panel - Payment details
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(31, 32, 44));
        rightPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        
        // Subtotal
        JPanel subtotalPanel = createPriceRow("Tổng Tiền:", String.format("%.0f VND", totalPrice));
        rightPanel.add(subtotalPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        
        // Discount
        JPanel discountPanel = createPriceRow("Khuyến Mãi:", "0 VND");
        rightPanel.add(discountPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        
        // Payment method
        JLabel methodLabel = new JLabel("Hình Thức Thanh Toán:");
        methodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        methodLabel.setForeground(Color.WHITE);
        rightPanel.add(methodLabel);
        
        paymentMethodCombo = new JComboBox<>(new String[] {
            "Tiền Mặt", "Thẻ Tín Dụng", "Chuyển Khoản", "E-Wallet"
        });
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        paymentMethodCombo.setBackground(new Color(50, 50, 60));
        paymentMethodCombo.setForeground(Color.WHITE);
        paymentMethodCombo.setMaximumSize(new Dimension(300, 35));
        rightPanel.add(paymentMethodCombo);
        rightPanel.add(Box.createVerticalStrut(25));
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(31, 32, 44));
        
        JLabel totalTitleLabel = new JLabel("Tổng Cộng:");
        totalTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalTitleLabel.setForeground(Color.WHITE);
        
        totalLabel = new JLabel(String.format("%.0f VND", totalPrice));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(241, 121, 104));
        
        totalPanel.add(totalTitleLabel, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        rightPanel.add(totalPanel);
        
        rightPanel.add(Box.createVerticalGlue());
        
        // Add panels
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createPriceRow(String label, String price) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(31, 32, 44));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComponent.setForeground(Color.WHITE);
        
        JLabel priceComponent = new JLabel(price);
        priceComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priceComponent.setForeground(Color.LIGHT_GRAY);
        
        row.add(labelComponent, BorderLayout.WEST);
        row.add(priceComponent, BorderLayout.EAST);
        
        if (label.contains("Khuyến Mãi")) {
            discountLabel = priceComponent;
        } else if (label.contains("Tổng Tiền")) {
            subtotalLabel = priceComponent;
        }
        
        return row;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(31, 32, 44));
        
        JButton paymentBtn = new JButton("Thanh Toán");
        paymentBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        paymentBtn.setBackground(new Color(241, 121, 104));
        paymentBtn.setForeground(Color.WHITE);
        paymentBtn.setFocusPainted(false);
        paymentBtn.setPreferredSize(new Dimension(150, 45));
        paymentBtn.addActionListener(e -> processPayment());
        
        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(100, 100, 120));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(150, 45));
        cancelBtn.addActionListener(e -> {
            // Go back
        });
        
        buttonPanel.add(paymentBtn);
        buttonPanel.add(cancelBtn);
        
        return buttonPanel;
    }
    
    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("═════════════════════════════════════\n");
        summary.append("Suất Chiếu ID: ").append(showtimeId).append("\n");
        summary.append("Số Ghế: ").append(selectedSeatIds.size()).append("\n");
        summary.append("ID Ghế: ");
        for (int i = 0; i < selectedSeatIds.size(); i++) {
            summary.append(selectedSeatIds.get(i));
            if (i < selectedSeatIds.size() - 1) summary.append(", ");
        }
        summary.append("\n");
        summary.append("═════════════════════════════════════\n");
        summary.append("\nGiá tiền chi tiết:\n");
        summary.append("• Giá vé: ").append(String.format("%.0f VND", totalPrice / selectedSeatIds.size())).append("/ghế\n");
        summary.append("• Số ghế: ").append(selectedSeatIds.size()).append("\n");
        summary.append("• Tổng: ").append(String.format("%.0f VND", totalPrice)).append("\n");
        
        orderSummaryArea.setText(summary.toString());
    }
    
    private void processPayment() {
        Customer customer = AuthService.getCurrentUser();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Bạn cần đăng nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        
        if (bookingService.bookTickets(customer.getCustomerId(), showtimeId, selectedSeatIds, totalPrice, paymentMethod)) {
            JOptionPane.showMessageDialog(this, 
                "Thanh toán thành công!\n\nVé của bạn đã được xác nhận.\nVui lòng check email để nhận chi tiết vé.", 
                "Thành Công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            if (onPaymentSuccess != null) {
                onPaymentSuccess.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi trong quá trình thanh toán. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
