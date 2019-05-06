import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Database {
	private ArrayList<Application> apps;
	private String fileName;
	
	/**
	 * Create empty database
	 */
	public Database() {
		apps = new ArrayList<Application>();
	}
	
	/**
	 * Create database from file
	 * 
	 * @param file file to load database from
	 * @throws FileNotFoundException
	 */
	public Database(String file) throws FileNotFoundException {
		apps = new ArrayList<Application>();
		fileName = file.toString();
		load(file);
	}
	
	/**
	 * Loads database from file 
	 * 
	 * @param filePath path of file
	 * @throws FileNotFoundException
	 */
	private void load(String filePath) throws FileNotFoundException {
		File file = new File(filePath); // Create file to read in
		Scanner reader = new Scanner(file); // Create scanner to read from file
		
		// Populate database
		while (reader.hasNext()) {
			String name = reader.next(), company = reader.next();
			apps.add(new Application(name, company));
		}
		
		reader.close(); // Close scanner
	}
	
	/**
	 * Attempts to add entry to database
	 * 
	 * @param name name of application
	 * @param company name of company
	 * @return whether adding was successful
	 */
	public boolean add(String name, String company) {
		Application temp = new Application(name, company); // Create application from data
		
		if (apps.contains(temp)) { return false; } // return false if application is already in database
		
		// add application and return true if not in database
		apps.add(temp); 
		
		return true;
	}
	
	/**
	 * Deletes entry from database
	 * 
	 * @param name name of application to delete
	 * @return whether application is able to be deleted
	 */
	public void delete(String name, String company) {
		apps.remove(new Application(name, company));
	}
	
	private Application getApp(String name) {
		Application returnApp = null;
		for (Application app : apps) {
			if (app.getName().equals(name)) {
				returnApp = app;
				break;
			}
		}
		return returnApp;
	}
	
	public void loadComments() {
		File commentsFile = new File("comments.txt");
		try {
			Scanner reader = new Scanner(commentsFile);
			while (reader.hasNext()) {
				String appName = reader.nextLine();
				getApp(appName).addComment(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "No comments to load");
			e.printStackTrace();
		}
		
	}
	
	public void saveComments() {
		try {
			PrintWriter writer = new PrintWriter("comments.txt");
			for (Application app : apps) {
				for (String comment : app.getComments()) {
					writer.println(app.getName());
					writer.println(comment);
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Getter for all Applications in database
	 * @return ArrayList of all Applications
	 */
	public ArrayList<Application> getApps() { return apps; }
	
	@Override
	public String toString() {
		String data = "";
		for (Application app : apps) {
			data += "Name: " + app.getName() + "\tCompany: " 
					+ app.getCompany() + "\n";
		}
		return data;
	}
	
	public String getDatabaseName() { return fileName; }
}
