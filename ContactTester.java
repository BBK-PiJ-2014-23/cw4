
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
    Contact c1;
    Contact c2;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        c1 = new ContactImpl(1, "c1", "");
        c2 = new ContactImpl(2, "c2", "has notes");
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        c1 = null;
        c2 = null;
    }

    /**
     * Tests the correctness of a contact's information.
     */
    @Test
    public void testContactInformation() {
        assertEquals("c1", c1.getName());
        assertEquals("", c1.getNotes());

        assertEquals("c2", c2.getName());
        assertEquals("has notes", c2.getNotes());
    }

    /**
     * Tests if contacts are assigned a unique ID.
     */
    @Test
    public void testHasUniqueId() {
        assertEquals(1, c1.getId());
        assertEquals(2, c2.getId());
    }
    
    /**
     * Tests if contacts' notes are updated properly.
     */
    @Test
    public void testAddingNotes() {
        c1.addNotes("c1 had notes added later");
        assertEquals("c1 had notes added later", c1.getNotes());
        
        c2.addNotes("c2 had notes modified later");
        assertEquals("c2 had notes modified later", c2.getNotes());
    }
}