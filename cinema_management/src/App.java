import javax.swing.SwingUtilities;

import ui.MainFrameNew;

public class App {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrameNew frame = new MainFrameNew();
            frame.setVisible(true);
        });
    }
}
