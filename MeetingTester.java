
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class MeetingTester.
 *
 * @author  Stefan E. Mayer
 * @version 1.0
 */
public class MeetingTester {
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
     * Tests if meetins are assigned the correct dates.
     */
    @Test
    public void testMeetingDates() {
        Set<Contact> contacts1 = new HashSet<Contact>();
        contacts1.add(new ContactImpl("c1", ""));
        contacts1.add(new ContactImpl("c2", "c2 has notes"));
        Calendar date1 = new GregorianCalendar(2015, 04, 25);
        Meeting meeting1 = new MeetingImpl(contacts1, date1);
        assertEquals(new GregorianCalendar(2015, 04, 25), meeting1.getDate());
        
        Set<Contact> contacts2 = new HashSet<Contact>();
        contacts2.add(new ContactImpl("c1", ""));
        contacts2.add(new ContactImpl("c2", "c2 has notes"));
        Calendar date2 = new GregorianCalendar(2014, 04, 25);
        Meeting meeting2 = new MeetingImpl(contacts2, date2);
        assertEquals(new GregorianCalendar(2014, 04, 25), meeting2.getDate());
    }
    
    /**
     * Tests if meetings are assigned a unique ID.
     */
    @Test
    public void testHasUniqueId() {
        Set<Contact> contacts1 = new HashSet<Contact>();
        Calendar date1 = new GregorianCalendar(2015, 04, 25);
        Meeting meeting1 = new MeetingImpl(contacts1, date1);
        assertEquals(1, meeting1.getId());
        
        Set<Contact> contacts2 = new HashSet<Contact>();
        Calendar date2 = new GregorianCalendar(2014, 04, 25);
        Meeting meeting2 = new MeetingImpl(contacts2, date2);
        assertEquals(2, meeting2.getId());
    }
}
