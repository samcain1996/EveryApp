import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
	private JTextField tBox1, tBox2; // Multipurpose textbox
	private JList<Application> list; // List containing all apps
	private JList<String> commentList, requestList;
	private DefaultListModel<Application> model;
	private DefaultListModel<String> comments, requestsModel;
	private Database db;
	private JRadioButton guestRadio, adminRadio, modRadio;
	private User user;

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

	public void addComponents(Component... components) {
		for (Component component : components) {
			panel.add(component);
		}
		repaint();
		revalidate();
	}

	public void logIn() {
		panel.removeAll();

		ActionListener lIListener = new logInListener();
		ActionListener createListener = new createAccountFormListener();

		// Button to create account
		JButton createButton = new JButton();
		createButton.setText("Create Account");
		createButton.setSize(200, 25);
		createButton.setLocation(100, 300);
		createButton.addActionListener(createListener);

		// Textfields used for username and password
		tBox1.setLocation(250, 100);
		tBox1.setSize(200, 25);
		tBox1.setText("Username");

		tBox2.setLocation(250, 175);
		tBox2.setSize(200, 25);
		tBox2.setText("Password");

		// Button to log in
		JButton logInButton = new JButton();
		logInButton.setText("Log In");
		logInButton.setSize(100, 25);
		logInButton.setLocation(100, 137);
		logInButton.addActionListener(lIListener);

		addComponents(tBox1, tBox2, logInButton, createButton);
	}

	public void createAccountForm() {
		panel.removeAll();

		tBox1.setText("Username");

		tBox2.setText("Password");

		JButton createButton = new JButton();
		createButton.setText("Create");
		createButton.setSize(100, 25);
		createButton.setLocation(270, 400);

		ButtonGroup radioButtons = new ButtonGroup();

		guestRadio = new JRadioButton("Guest ");
		guestRadio.setBounds(250, 300, 75, 25);
		guestRadio.setSelected(true);
		radioButtons.add(guestRadio);

		adminRadio = new JRadioButton("Admin ");
		adminRadio.setBounds(325, 300, 75, 25);
		radioButtons.add(adminRadio);

		modRadio = new JRadioButton("Mod ");
		modRadio.setBounds(400, 300, 75, 25);
		radioButtons.add(modRadio);

		ActionListener create = new createAccountListener();
		createButton.addActionListener(create);

		addComponents(tBox1, tBox2, guestRadio, adminRadio, modRadio, createButton);
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

		tBox1 = new JTextField();
		tBox1.setLocation(250, 100);
		tBox1.setSize(200, 25);

		tBox2 = new JTextField();

		ActionListener listener = new loadListener();

		load.addActionListener(listener);

		panel.add(load);
		panel.add(tBox1);
	}

	/**
	 * Refreshes a list after it is modified
	 * 
	 * @param lModel the ListModel that is being modified
	 * @param src    the ArrayList from which lModel gets its elements
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
		ActionListener sRListener = new submitRequestViewListener();
		ActionListener lOListener = new logOutListener();

		JButton requestsButton = new JButton();
		requestsButton.setLocation(450, 25);
		requestsButton.setSize(200, 25);

		if (user.getUserType().equals(UserType.ADMIN)) {
			requestsButton.setText("View Requests");
		}

		else {
			requestsButton.setText("Add Request");
		}

		JButton logOut = new JButton();
		logOut.setLocation(450, 70);
		logOut.setSize(200, 25);
		;
		logOut.setText("Log Out");
		logOut.addActionListener(lOListener);

		requestsButton.addActionListener(sRListener);

		// Change location and clear textbox
		tBox1.setText("");
		tBox1.setLocation(25, 25);

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

		addComponents(filter, logOut, viewComments, scrollList, sortCompany, sortName, requestsButton, tBox1);
	}

	void viewRequests() {
		panel.removeAll();

		ActionListener goBackListener = new backListener();

		JButton back = new JButton();
		back.setLocation(400, 25);
		back.setSize(100, 25);
		back.setText("Back");
		back.addActionListener(goBackListener);

		ArrayList<String> requests = new ArrayList<String>();
		File requestsFile = new File("requests.txt");
		String reqInfo = "";
		try {
			Scanner reader = new Scanner(requestsFile);
			while (reader.hasNext()) {
				reqInfo += reader.nextLine();
				reqInfo += "\t" + reader.nextLine();
				reqInfo += "\t" + reader.nextLine();
				requests.add(reqInfo);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No Requests");
			displayApps();
		}

		requestsModel = new DefaultListModel<String>();

		for (String request : requests) {
			requestsModel.addElement(request);
		}

		requestList = new JList<String>(requestsModel);
		requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollList = new JScrollPane(requestList);
		scrollList.setLocation(0, 100);
		scrollList.setSize(500, 300);
		scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		ActionListener acceptListener = new requestListener(true);
		ActionListener denyListener = new requestListener(false);

		JButton acceptRequest = new JButton();
		acceptRequest.setSize(200, 25);
		acceptRequest.setLocation(210, 400);
		acceptRequest.setText("Accept");
		acceptRequest.addActionListener(acceptListener);

		JButton denyRequest = new JButton();
		denyRequest.setSize(200, 25);
		denyRequest.setLocation(420, 400);
		denyRequest.setText("Deny");
		denyRequest.addActionListener(denyListener);

		addComponents(scrollList, acceptRequest, denyRequest, back);
	}

	void submitRequest() {
		panel.removeAll();

		JLabel nameLabel = new JLabel(), companyLabel = new JLabel();
		nameLabel.setLocation(100, 50);
		nameLabel.setSize(100, 25);
		nameLabel.setText("App Name:");

		companyLabel.setLocation(100, 150);
		companyLabel.setSize(100, 25);
		companyLabel.setText("Company:");

		tBox1.setLocation(250, 50);
		tBox1.setSize(100, 25);
		tBox1.setText("");

		tBox2.setLocation(250, 150);
		tBox2.setSize(100, 25);
		tBox2.setText("");

		JButton submit = new JButton();
		submit.setLocation(350, 350);
		submit.setSize(100, 25);
		submit.setText("Submit");

		ActionListener listener = new submitRequestListener();
		ActionListener goBackListener = new backListener();

		JButton back = new JButton();
		back.setLocation(400, 50);
		back.setSize(100, 25);
		back.setText("Back");

		submit.addActionListener(listener);
		back.addActionListener(goBackListener);

		addComponents(nameLabel, companyLabel, tBox1, tBox2, submit, back);
	}

	/**
	 * Sets GUI attributes when displaying the comments of an app
	 * 
	 * @param app Application that is being viewed
	 */
	private void commentView(Application app) {
		panel.removeAll();

		ActionListener back = new backListener();
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
		if (user.getUserType().equals(UserType.BASIC)) {
			removeCommentButton.setEnabled(false);
		}
		tBox1.setLocation(10, 400);

		addComponents(backButton, removeCommentButton, scrollList, appLabel, tBox1, addCommentButton);
	}

	private class loadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			File file = new File(tBox1.getText());
			if (file.exists()) {
				try {
					db = new Database(tBox1.getText());
					logIn();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Invalid file");
			}
		}
	}

	private class logInListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			File file = new File("users.txt");
			String typeHash = "";
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] userHash = digest.digest(tBox1.getText().getBytes(StandardCharsets.UTF_8));
				byte[] passHash = digest.digest(tBox2.getText().getBytes(StandardCharsets.UTF_8));
				String adminHash = DatatypeConverter
						.printHexBinary(digest.digest("admin".getBytes(StandardCharsets.UTF_8)));
				String modHash = DatatypeConverter
						.printHexBinary(digest.digest("mod".getBytes(StandardCharsets.UTF_8)));
				Scanner sc = new Scanner(file);
				boolean logInFound = false;
				while (sc.hasNext()) {
					String a = sc.next(), b = sc.next();
					typeHash = sc.next();
					String a1 = DatatypeConverter.printHexBinary(userHash);
					String b1 = DatatypeConverter.printHexBinary(passHash);
					if (a.equals(a1) && b.equals(b1)) {
						logInFound = true;
						break;
					}
				}
				if (logInFound) {
					UserType type = typeHash.equals(adminHash) ? UserType.ADMIN
							: (typeHash.equals(modHash) ? UserType.MOD : UserType.BASIC);
					user = new User(type, tBox1.getText());
					displayApps();
				} else
					JOptionPane.showMessageDialog(null, "Invalid Log in");

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
			if (!tBox1.getText().equals("") && !tBox2.getText().equals("")) {
				try {
					String accountType = adminRadio.isSelected() ? "admin" : (modRadio.isSelected() ? "mod" : "guest");
					PrintWriter writer = new PrintWriter(new FileOutputStream("users.txt", true));
					MessageDigest digest = MessageDigest.getInstance("SHA-256");
					byte[] userHash = digest.digest(tBox1.getText().getBytes(StandardCharsets.UTF_8));
					byte[] passHash = digest.digest(tBox2.getText().getBytes(StandardCharsets.UTF_8));
					byte[] accountHash = digest.digest(accountType.getBytes(StandardCharsets.UTF_8));
					writer.println(DatatypeConverter.printHexBinary(userHash));
					writer.println(DatatypeConverter.printHexBinary(passHash));
					writer.println(DatatypeConverter.printHexBinary(accountHash));
					writer.close();
					logIn();
				} catch (FileNotFoundException e) {
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
				if (app.getName().contains(tBox1.getText()) || app.getCompany().contains(tBox1.getText())) {
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
					return app1.getName().compareTo(app2.getName());
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
					return app1.getCompany().compareTo(app2.getCompany());
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

	private class backListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			displayApps();
		}

	}

	private class addCommentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!list.getSelectedValue().getComments().contains(tBox1.getText())) {
				list.getSelectedValue().addComment(tBox1.getText());
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

	private class submitRequestViewListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (user.getUserType().equals(UserType.ADMIN)) {
				viewRequests();
			} else {
				submitRequest();
			}
		}

	}

	private class submitRequestListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				PrintWriter writer = new PrintWriter(new FileOutputStream("requests.txt", true));
				writer.println(tBox1.getText() + "\n" + tBox2.getText() + "\n" + user.getName());
				writer.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class logOutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			logIn();
		}

	}

	private class requestListener implements ActionListener {

		boolean accept;

		public requestListener(boolean accept) {
			this.accept = accept;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String app = requestList.getSelectedValue();
			ArrayList<String> newList = new ArrayList<String>();
			File file = new File("requests.txt");
			try {
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String entry = reader.nextLine();
					entry += "\t" + reader.nextLine();
					entry += "\t" + reader.nextLine();
					if (!entry.equals(app)) {
						newList.add(entry);
					}
				}
				reader.close();
				PrintWriter writer = new PrintWriter("requests.txt");
				for (String entry : newList) {
					writer.println(entry);
				}
				writer.close();
				refreshList(requestsModel, newList);
				if (accept) {
					db.add((app.substring(0, app.indexOf('\t'))),
							app.substring(app.indexOf('\t'), app.lastIndexOf('\t')));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
