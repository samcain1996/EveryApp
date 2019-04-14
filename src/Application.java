import java.util.ArrayList;

public class Application {
	private String name, company;
	private ArrayList<String> comments;
	
	/**
	 * Constructor for Application
	 * @param name name of the Application
	 * @param company company that made the Application
	 */
	public Application(String name, String company) {
		this.name = name;
		this.company = company;
		comments = new ArrayList<String>();
	}
	
	/**
	 * Adds a comment to the Application
	 * @param comment comment to be added
	 */
	public void addComment(String comment) {
		comments.add(comment);
	}
	
	/**
	 * Removes a comment from the application
	 * @param comment comment to be removed
	 */
	public void removeComment(String comment) {
		comments.remove(comment);
	}
	
	/**
	 * Getter for name
	 * @return Application name
	 */
	public String getName() { return name; }
	
	/**
	 * Getter for the company
	 * @return company name
	 */
	public String getCompany() { return company; }
	
	/**
	 * Getter for comments
	 * @return ArrayList with all comments for Application
	 */
	public ArrayList<String> getComments() { return comments; }
	
	@Override
	public String toString() {
		return name + "\t" + company;
	}
}
