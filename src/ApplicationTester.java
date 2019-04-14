import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApplicationTester {
	
	@Test
	void test() {
		Application appTest = new Application("TestName", "TestCompany");
		
		assertEquals("Test\tTestCompany", appTest.toString());
	}

}
