import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Database {
	private ArrayList<Application> apps;
	
	public Database() {
		apps = new ArrayList<Application>();
	}
	
	public Database(String fileName) throws FileNotFoundException {
		super();
		File file = new File(fileName);
		Scanner reader = new Scanner(file);
		
		while (reader.hasNext()) {
			String name = reader.next(), company = reader.next();
			apps.add(new Application(name, company));
		}
		
		reader.close();
	}
	
	@Override
	public String toString() {
		String data = "";
		for (Application app : apps) {
			data += "Name: " + app.getName() + "\tCompany: "
					+ app.getCompany() + "\n";
		}
		return data;
	}
}
