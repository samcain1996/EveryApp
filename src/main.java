import java.io.FileNotFoundException;

public class main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Database db = new Database("apps.txt");
		System.out.println(db.toString());
	}	

}
