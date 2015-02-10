
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
    Calendar date1;
    Calendar date2;

    Set<Contact> contacts1;
    Set<Contact> contacts2;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        date1 = new GregorianCalendar(2015, 04, 25);
        date2 = new GregorianCalendar(2014, 04, 25);

        contacts1 = new HashSet<Contact>();
        contacts2 = new HashSet<Contact>();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        date1 = null;
        date2 = null;
    }

    /**
     * Tests if meetins are assigned the correct dates.
     */
    @Test
    public void testMeetingDates() {
        contacts1.add(new ContactImpl("c1", ""));
        contacts1.add(new ContactImpl("c2", "c2 has notes"));
        Meeting meeting1 = new MeetingImpl(contacts1, date1);
        assertEquals(new GregorianCalendar(2015, 04, 25), meeting1.getDate());

        contacts2.add(new ContactImpl("c1", ""));
        contacts2.add(new ContactImpl("c2", "c2 has notes"));
        Meeting meeting2 = new MeetingImpl(contacts2, date2);
        assertEquals(new GregorianCalendar(2014, 04, 25), meeting2.getDate());
    }

    /**
     * Tests if meetings are assigned a unique ID.
     */
    @Test
    public void testHasUniqueId() {
        Meeting meeting1 = new MeetingImpl(contacts1, date1);
        assertEquals(1, meeting1.getId());

        Meeting meeting2 = new MeetingImpl(contacts2, date2);
        assertEquals(2, meeting2.getId());
    }

    /**
     * Tests if a meeting's contacts are retrieved properly.
     */
    @Test
    public void testMeetingContacts() {
        Contact contact1 = new ContactImpl("c1", "");
        Contact contact2 = new ContactImpl("c2", "c2 has notes");
        contacts1.add(contact1);
        contacts1.add(contact2);
        Meeting meeting1 = new MeetingImpl(contacts1, date1);
        assertTrue(meeting1.getContacts().contains(contact1));
        assertTrue(meeting1.getContacts().contains(contact2));

        Contact contact3 = new ContactImpl("c3", "");
        contacts2.add(contact3);
        Meeting meeting2 = new MeetingImpl(contacts2, date2);
        assertTrue(meeting2.getContacts().contains(contact3));
    }
}
