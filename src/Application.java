import java.util.ArrayList;

public class Application {
	private String name, company;
	private ArrayList<String> comments;
	
	public Application(String name, String company) {
		this.name = name;
		this.company = company;
		comments = new ArrayList<String>();
	}
	
	public void addComment(String comment) {
		comments.add(comment);
	}
	
	public String getName() { return name; }
	
	public String getCompany() { return company; }
	
	public ArrayList<String> getComments() { return comments; }
	
	@Override
	public String toString() {
		return name + "    " + company;
	}
}
