import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;


public class ApplicationTest {

    Database database;
    @Before
    public void setUp() throws Exception {
        database = new Database();
    }

    @Test
    public void testApplication1() {
        Application application1 = new Application("A", "a");
        Application application2 = new Application("A", "a");
        assertEquals("Application equal", application1, application2);
        Application application3 = new Application("A", "b");
        assertEquals("Application not equal", application1, application3);
    }

    @Test
    public void testApplication2() {
        Application application = new Application("A", "a");
        assertEquals("Application name ", application.getName(), "A");
        assertEquals("Application company ", application.getCompany(), "a");
    }

    @Test
    public void testApplication3() {
        Application application = new Application("A", "a");
        assertEquals("Application getComments ", application.getComments().size(), 0);
        application.addComment("1");
        assertEquals("Application getComments ", application.getComments().size(), 1);
    }
}