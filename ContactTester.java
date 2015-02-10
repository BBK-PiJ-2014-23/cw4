
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ContactTester.
 *
 * @author  Stefan E. Mayer
 * @version 1.0
 */
public class ContactTester {
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {

    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {

    }

    /**
     * Tests the correctness of a contact's information.
     */
    @Test
    public void testContactInformation() {
        Contact c1 = new ContactImpl("c1", "");
        assertEquals("c1", c1.getName());
        assertEquals("", c1.getNotes());

        Contact c2 = new ContactImpl("c2", "c2 has notes");
        assertEquals("c2", c2.getName());
        assertEquals("c2 has notes", c2.getNotes());
    }

    /**
     * Tests if contacts are assigned a unique ID.
     */
    @Test
    public void testHasUniqueId() {
        Contact c1 = new ContactImpl("c1", "");
        assertEquals(1, c1.getId());

        Contact c2 = new ContactImpl("c2", "c2 has notes");
        assertEquals(2, c2.getId());
    }
}