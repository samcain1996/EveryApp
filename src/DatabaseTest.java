import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;


public class DatabaseTest {

    Database database;
    @Before
    public void setUp() throws Exception {
        database = new Database();
    }
    @Test
    public void testGetApps() {
        //getApps not null
        assertNotNull(database.getApps());
    }
    @Test
    public void testAdd() {
        //Check the length, increase the length each time when you add
        assertTrue(database.add("A", "a"));
        assertTrue(database.add("B", "b"));
        assertTrue(database.add("C", "c"));
        //Add the same item, return false
        assertFalse(database.add("C", "c"));
    }
    
    @Test
    public void testgetApps2() {
        //Check the length, increase the length each time when you add
        assertEquals("size must be 3 ."+database.toString(), database.getApps().size(), 3);
        
        //When adding the same item, the length is unchanged
        database.add("C", "c");
        assertEquals("size must be 3 ."+database.toString(), database.getApps().size(), 3);
        
        //When adding item, increase the length by 1
        database.add("D", "d");
        assertEquals("size must be 4 ."+database.toString(), database.getApps().size(), 4);

    }
    

    @Test
    public void testDelete() {
        //Delete items that do not exist, the length must be unchanged
        database.delete("X", "x");
        assertEquals("delete not exist item ,size must be 3 ."+database.toString(), database.getApps().size(), 3);
        //Delete the existing item, the length needs to be reduced by 1
        database.delete("A", "a");
        assertEquals("delete an exist item ,size must be 2 ."+database.toString(), database.getApps().size(), 2);
    }
    

    @Test
    public void testFile() {
        try {
            //Test file does not exist
            database = new Database("AAA");
            fail("Expected an FileNotFoundException to be thrown");
        } catch (FileNotFoundException fileNotFoundException) {
        }  
        
    }

    @Test
    public void testFile2() {
        try {
            //Test load file
            database = new Database("apps.txt");
        } catch (FileNotFoundException fileNotFoundException) {
            fail("Not expected FileNotFoundException to be thrown");
        }  
    }
    

    @Test
    public void testFile3() {
        try {
            //App length is 4 after loading file data
        	/* App1 1Comp
        	 App2 Company2
        	 MyApp MyComp
        	 AppEtc AppCompany*/
            database = new Database("apps.txt");
            assertEquals("size must be 4 ."+database.toString(), database.getApps().size(), 4);
        } catch (FileNotFoundException e) {
            fail("Not expected FileNotFoundException to be thrown");
        }
    }

    @Test
    public void testFile4() {
        try {
            //App length is 4 after loading file data
            /* App1 1Comp
            App2 Company2
            MyApp MyComp
            AppEtc AppCompany*/
            database = new Database("apps.txt");
            database.delete("App2", "Company2");

            //Delete one, the length becomes 3
            assertEquals("size must be 3 ."+database.toString(), database.getApps().size(), 3);
        } catch (FileNotFoundException e) {
            fail("Not expected FileNotFoundException to be thrown");
        }
    }
    
}
