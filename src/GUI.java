import java.awt.*;

import javax.swing.*;

public class GUI extends JFrame {
	protected JPanel panel;
	
	public GUI() {
		setAttributes();
		createPanel();
		setVisible(true);
		add(panel);
	}
	
	private void setAttributes() {
		setSize(600, 480); // Set Window Size
		setTitle("EveryApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
	}
}
