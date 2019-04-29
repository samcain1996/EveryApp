import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.bind.DatatypeConverter;

public class GUI extends JFrame {
	private JPanel panel; // Panel to add components to
	private JTextField tBox, passTBox; // Multipurpose textbox
	private JList<Application> list; // List containing all apps
	private JList<String> commentList;
	private DefaultListModel<Application> model;
	private DefaultListModel<String> comments;
	private Database db;
	
	/**
	 * Constructor for GUI
	 */
	public GUI() {
		panel = new JPanel();
		setInitialAttributes();	
		
		panel.setLayout(null);
		
		setVisible(true);
		add(panel);
	}
	
	public void logIn() {
		panel.removeAll();
		
		ActionListener lIListener = new logInListener();
		ActionListener createListener = new createAccountFormListener();
		
		JButton createButton = new JButton();
		createButton.setText("Create Account");
		createButton.setSize(200, 25);
		createButton.setLocation(100, 300);
		
		tBox.setText("Username");
		
		passTBox = new JTextField();
		passTBox.setLocation(250, 175);
		passTBox.setSize(200, 25);
		passTBox.setText("Password");
		
		JButton logInButton = new JButton();
		logInButton.setText("Log In");
		logInButton.setSize(100, 25);
		logInButton.setLocation(100, 137);
		
		logInButton.addActionListener(lIListener);
		createButton.addActionListener(createListener);
		
		panel.add(tBox);
		panel.add(passTBox);
		panel.add(logInButton);
		panel.add(createButton);
		
		repaint();
		revalidate();
	}
	
	public void createAccountForm() {
		panel.removeAll();
		
		ActionListener create = new createAccountListener();
		
		tBox.setText("Username");
		
		passTBox = new JTextField();
		passTBox.setLocation(250, 175);
		passTBox.setSize(200, 25);
		passTBox.setText("Password");
		
		JButton createButton = new JButton();
		createButton.setText("Create");
		createButton.setSize(100, 25);
		createButton.setLocation(100, 300);
	
		createButton.addActionListener(create);
		
		panel.add(tBox);
		panel.add(passTBox);
		panel.add(createButton);
		
		repaint();
		revalidate();
	}

	/**
	 * Sets GUI attributes for start screen
	 */
	private void setInitialAttributes() {
		setSize(640, 480);
		setTitle("EveryApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton load = new JButton();
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
	
	/**
	 * Refreshes a list after it is modified
	 * 
	 * @param lModel the ListModel that is being modified
	 * @param src the ArrayList from which lModel gets its elements
	 */
	private void refreshList(DefaultListModel lModel, ArrayList src) {
		lModel.clear();
		for (Object thing : src) {
			lModel.addElement(thing);
		}
	}
	
	/*
	 * Sets GUI attributes for displaying apps
	 */
	private void displayApps() {
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
	    scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.add(filter);
		panel.add(viewComments);
		panel.add(scrollList);
		panel.add(sortCompany);
		panel.add(sortName);
		panel.add(tBox);
		revalidate();
		repaint();
	}
	
	/**
	 * Sets GUI attributes when displaying the comments of an app
	 * 
	 * @param app Application that is being viewed
	 */
	private void commentView(Application app) {
		panel.removeAll();
		
		ActionListener back = new commentsBackListener();
		ActionListener addComment = new addCommentListener();
		ActionListener removeComment = new removeCommentListener();
		
		JLabel appLabel = new JLabel();
		appLabel.setLocation(25, 50);
		appLabel.setText(app.getName());
		appLabel.setSize(app.getName().length() * 10, 25);

		comments = new DefaultListModel<String>();
		for (String comment : app.getComments()) {
			comments.addElement(comment);
		}
		
		commentList = new JList<String>(comments);
		
		JScrollPane scrollList = new JScrollPane(commentList);
	    scrollList.setLocation(0, 100);
	    scrollList.setSize(500, 300);
	    scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
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
		removeCommentButton.addActionListener(removeComment);
		
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
					logIn();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else { System.out.println("Invalid file"); }
		}	
	}
	
	private class logInListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			MessageDigest digest;
			File file = new File("users.txt");
			try {
				digest = MessageDigest.getInstance("SHA-256");
				byte[] userHash = digest.digest(tBox.getText().getBytes(StandardCharsets.UTF_8));
				byte[] passHash = digest.digest(passTBox.getText().getBytes(StandardCharsets.UTF_8));
				Scanner sc = new Scanner(file);
				while (sc.hasNext()) {		
					if (sc.next().equals(DatatypeConverter.printHexBinary(userHash)) &&
							sc.next().equals(DatatypeConverter.printHexBinary(passHash)))
						displayApps();
					else 
						JOptionPane.showMessageDialog(null, "Invalid Log in");
				}
				
				sc.close();
			} catch (NoSuchAlgorithmException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private class createAccountFormListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			createAccountForm();
		}
		
	}
	
	private class createAccountListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (!tBox.getText().equals("") && !tBox.getText().equals("")) {
				try {
					PrintWriter writer = new PrintWriter("users.txt", "UTF-8");
					MessageDigest digest = MessageDigest.getInstance("SHA-256");
					byte[] userHash = digest.digest(tBox.getText().getBytes(StandardCharsets.UTF_8));
					byte[] passHash = digest.digest(passTBox.getText().getBytes(StandardCharsets.UTF_8));
					writer.println(DatatypeConverter.printHexBinary(userHash));
					writer.println(DatatypeConverter.printHexBinary(passHash));
					writer.close();
					logIn();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
			displayApps();
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
			list.getSelectedValue().removeComment(commentList.getSelectedValue());
			refreshList(comments, list.getSelectedValue().getComments());
		}
		
	}
	
}
