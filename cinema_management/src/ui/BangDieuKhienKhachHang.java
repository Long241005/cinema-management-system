package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerDashboardPanel extends JPanel {
    
    private MainFrameNew parentFrame;
    
    public CustomerDashboardPanel(MainFrameNew parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(31, 32, 44));
        
        // Menu
        add(createMenuPanel(), BorderLayout.WEST);
        
        // Content area
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(31, 32, 44));
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(31, 32, 44));
        menuPanel.setPreferredSize(new Dimension(250, getHeight()));
        menuPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        String[] menuItems = {
            "Duyệt Phim",
            "Lịch Chiếu",
            "Đặt Vé",
            "Lịch Sử Vé",
            "Tài Khoản Của Tôi",
            "Khuyến Mãi"
        };
        
        for (String item : menuItems) {
            JButton btn = createMenuButton(item);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(10));
        }
        
        menuPanel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(31, 32, 44));
        
        return scrollPane;
    }
    
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setBackground(new Color(50, 50, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(241, 121, 104));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(50, 50, 60));
            }
        });
        
        btn.addActionListener(e -> handleMenuClick(text));
        return btn;
    }
    
    private void handleMenuClick(String item) {
        // TODO: Implement each menu item
        System.out.println("Clicked: " + item);
        JOptionPane.showMessageDialog(this, "Module: " + item + " đang được phát triển", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
