
public class Application {
	private String name, company;
	
	public Application(String name, String company) {
		this.name = name;
		this.company = company;
	}
	
	public String getName() { return name; }
	
	public String getCompany() { return company; }
	
	@Override
	public String toString() {
		return name + "    " + company;
	}
}
