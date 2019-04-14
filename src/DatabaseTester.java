import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class DatabaseTester {

	@Test
	void test() throws FileNotFoundException {
		Database dbTest = new Database("apps.txt");
		
	}

}
