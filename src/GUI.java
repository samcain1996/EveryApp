import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;

public class GUI extends JFrame {
	private JPanel panel;
	private JButton load;
	private JTextField tBox, addCommentBox;
	private JList<Application> list;
	private ArrayList<Application> appArray;
	private DefaultListModel<Application> model;
	private DefaultListModel<String> comments;
	private Database db;
	
	public GUI() {
		panel = new JPanel();
		setInitialAttributes();	
		
		panel.setLayout(null);
		
		setVisible(true);
		add(panel);
	}

	private void setInitialAttributes() {
		setSize(640, 480);
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
	
	private void refreshList(DefaultListModel lModel, ArrayList src) {
		for (Object thing : src) {
			lModel.addElement(thing);
		}
	}
	
	private void setDisplayAttributes() {
		panel.removeAll();
		
		ActionListener fListener = new filterListener();
		ActionListener sNListener = new sortNameListener();
		ActionListener sCListener = new sortCompanyListener();
		ActionListener vCListener = new viewCommentsListener();
		
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
		
		JButton viewComments = new JButton();
		viewComments.setLocation(100, 400);
		viewComments.setSize(200, 25);
		viewComments.setText("View Comments");
		viewComments.addActionListener(vCListener);
		
		model = new DefaultListModel<Application>();
		for (Application app : db.getApps()) {
			model.addElement(app);
		}
		list = new JList<Application>(model);   
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    JScrollPane scrollList = new JScrollPane(list);   
	    scrollList.setLocation(0, 100);
	    scrollList.setSize(500, 300);
		
		panel.add(filter);
		panel.add(viewComments);
		panel.add(scrollList);
		panel.add(sortCompany);
		panel.add(sortName);
		panel.add(tBox);
		revalidate();
		repaint();
	}
	
	private void commentView(Application app) {
		panel.removeAll();
		
		ActionListener back = new commentsBackListener();
		ActionListener addComment = new addCommentListener();
		
		JLabel appLabel = new JLabel();
		appLabel.setLocation(25, 50);
		appLabel.setText(app.getName());
		appLabel.setSize(app.getName().length() * 10, 25);

		comments = new DefaultListModel<String>();
		for (String comment : app.getComments()) {
			comments.addElement(comment);
		}
		
		JList<String> commentList = new JList<String>(comments);
		
		JScrollPane scrollList = new JScrollPane(commentList);
	    scrollList.setLocation(0, 100);
	    scrollList.setSize(500, 300);
		
		JButton backButton = new JButton();
		backButton.setSize(100, 25);
		backButton.setLocation(450, 25);
		backButton.setText("Back");
		backButton.addActionListener(back);
		
		JButton addCommentButton = new JButton();
		addCommentButton.setSize(200, 25);
		addCommentButton.setLocation(210, 400);
		addCommentButton.setText("Add Comment");
		addCommentButton.addActionListener(addComment);
		
		JButton removeCommentButton = new JButton();
		removeCommentButton.setSize(200, 25);
		removeCommentButton.setLocation(420, 400);
		removeCommentButton.setText("Remove Comment");
		
		tBox.setLocation(10, 400);

		panel.add(backButton);
		panel.add(removeCommentButton);
		panel.add(scrollList);
		panel.add(appLabel);
		panel.add(tBox);
		panel.add(addCommentButton);
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
			refreshList(model, db.getApps());
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
			refreshList(model, db.getApps());
		}
	}
	
	private class viewCommentsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			commentView(list.getSelectedValue());
		}
		
	}
	
	private class commentsBackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setDisplayAttributes();
		}
		
	}
	
	private class addCommentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!list.getSelectedValue().getComments().contains(tBox.getText())) {
				list.getSelectedValue().addComment(tBox.getText());
				refreshList(comments, list.getSelectedValue().getComments());
			}
		}
	}
	
	private class removeCommentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
}
