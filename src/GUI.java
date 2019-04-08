import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;

public class GUI extends JFrame {
	private JPanel panel;
	private JButton load;
	private JTextField tBox;
	private JList<Application> list;
	private ArrayList<Application> appArray;
	private DefaultListModel<Application> model;
	private Database db;
	
	public GUI() {
		panel = new JPanel();
		setInitialAttributes();	
		
		panel.setLayout(null);
		
		setVisible(true);
		add(panel);
	}
	
	private void setInitialAttributes() {
		setSize(600, 480);
		setTitle("EveryApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		appArray = new ArrayList<Application>();
		
		load = new JButton();
		load.setLocation(100, 100);
		load.setSize(100, 25);
		load.setText("Load");
		
		tBox = new JTextField();
		tBox.setLocation(250, 100);
		tBox.setSize(200, 25);
		
		ActionListener listener = new loadListener();
		
		load.addActionListener(listener);
		
		panel.add(load);
		panel.add(tBox);
	}
	
	private void refreshList() {
		for (Application app : db.getApps()) {
			model.addElement(app);
		}
	}
	
	private void setDisplayAttributes() {
		panel.remove(load);
		
		ActionListener fListener = new filterListener();
		ActionListener sNListener = new sortNameListener();
		ActionListener sCListener = new sortCompanyListener();
		
		// Change location and clear textbox
		tBox.setText("");
		tBox.setLocation(25, 25);
		
		JButton filter = new JButton();
		filter.setLocation(300, 25);
		filter.setSize(100, 25);
		filter.setText("Filter");
		filter.addActionListener(fListener);
		
		JButton sortName = new JButton();
		sortName.setLocation(25, 70);
		sortName.setSize(100, 25);
		sortName.setText("Name");
		sortName.addActionListener(sNListener);
		
		JButton sortCompany = new JButton();
		sortCompany.setLocation(220, 70);
		sortCompany.setSize(100, 25);
		sortCompany.setText("Company");
		sortCompany.addActionListener(sCListener);
		
		model = new DefaultListModel<Application>();
		for (Application app : db.getApps()) {
			model.addElement(app);
		}
		list = new JList<Application>(model);    

	    JScrollPane scrollList = new JScrollPane(list);   
	    scrollList.setLocation(0, 100);
	    scrollList.setSize(500, 300);
		
		panel.add(filter);
		panel.add(scrollList);
		panel.add(sortCompany);
		panel.add(sortName);
		revalidate();
		repaint();
	}
	
	private class loadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			File file = new File(tBox.getText());
			if (file.exists()) {
				try {
					db = new Database(tBox.getText());
					setDisplayAttributes();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else { System.out.println("Invalid file"); }
		}	
	}
	
	private class filterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.clear();
			for (Application app : db.getApps()) {
				if (app.getName().contains(tBox.getText()) ||
						app.getCompany().contains(tBox.getText())) {
					model.addElement(app);
				}
			}
			// refreshList();
		}
	}
	
	private class sortNameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.clear();
			Collections.sort(db.getApps(), new Comparator<Application>() {
				@Override
			    public int compare(Application app1, Application app2) {
						return  app1.getName().compareTo(app2.getName());
			    }
			});
			refreshList();
		}
	}
	
	private class sortCompanyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.clear();
			Collections.sort(db.getApps(), new Comparator<Application>() {
				@Override
			    public int compare(Application app1, Application app2) {
						return  app1.getCompany().compareTo(app2.getCompany());
			    }
			});
			refreshList();
		}
	}
	
}
