
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class ContactManagerTester.
 *
 * @author  Stefan E. Mayer
 * @version 1.0
 */
public class ContactManagerTester{
    ContactManager manager;
    Set<Contact> allContacts;
    Calendar past;
    Calendar future;
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        manager = new ContactManagerImpl();
        manager.addNewContact("c1", "notes1");
        manager.addNewContact("c2", "notes2");
        manager.addNewContact("c3", "notes3");
        allContacts = manager.getContacts(1, 2, 3);
        // Date assignments are dynamic - one year subtracted for past, one year added for future - to ensure tests run in the future
        past = new GregorianCalendar();
        past.add(1, -1);
        future = new GregorianCalendar();
        future.add(1, 1);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        manager = null;
        allContacts = null;
        past = null;
        future = null;
    }

    /**
    * Tests nullpointer exception when trying to create a contact with name 'null'.
    */
    @Test(expected = NullPointerException.class)
    public void testNewContactNameException() {
        manager.addNewContact(null, "notes");
    }

    /**
    * Tests nullpointer exception when trying to create a contact with notes 'null'.
    */
    @Test(expected = NullPointerException.class)
    public void testNewContactNotesException() {
        manager.addNewContact("name", null);
    }

    //     /**
    //      * Tests nullpointer exception when trying to retrieve a contact by name 'null'.
    //      */
    //     @Test(expected = NullPointerException.class)
    //     public void testGettingContactByNameException() {
    //         manager.getContactsString(null);
    //     }

    /**
     * Tests to ensure that getting contacts from empty contacts set is also empty.
     */
    @Test
    public void testGettingContactByNameFromEmptyList() {
        Set<Contact> empty1 = manager.getContacts("");
        Set<Contact> empty2 = manager.getContacts("Tom");
        assertTrue(empty1.isEmpty());
        assertTrue(empty2.isEmpty());
    }

    /**
     * Helper method to find contacts in sets via name.
     */
    private boolean hasContact(Set<Contact> set, String name) {
        boolean found = false;
        for (Contact member : set) {
            if (member.getName().equals(name)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Tests if a uniquely named contact can be retrieved.
     */
    @Test
    public void testAddingAndGettingSingleContactByName() {
        Set<Contact> single = manager.getContacts("c2");
        assertEquals(1, single.size());
        assertTrue(hasContact(single, "c2"));
    }

    /**
     * Tests retrieving a non-existing contact via name.
     */
    @Test
    public void testGettingNonExistingContact() {
        Set<Contact> none = manager.getContacts("NotHere");
        assertEquals(0, none.size());
    }

    /**
     * Tests retrieving a contact without a name.
     */
    @Test
    public void testGettingEmptyNamedContact() {
        Set<Contact> none = manager.getContacts("");
        assertEquals(0, none.size());
    }

    /**
     * Tests if serveral similarily named contacts can be retrieved.
     */
    @Test
    public void testGettingSimilarilyNamedContacts() {
        Set<Contact> several = manager.getContacts("c");
        assertEquals(3, several.size());
        assertTrue(hasContact(several, "c1"));
        assertTrue(hasContact(several, "c2"));
        assertTrue(hasContact(several, "c3"));
    }

    /**
    * Tests illegal argument exception when retrieving contacts with a negative ID.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGettingContactNegativeIdException() {
        manager.getContacts(1, 2, -1);
    }

    /**
    * Tests illegal argument exception when retrieving contacts with non-existing IDs.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGettingContactNonExistingIdException() {
        manager.getContacts(1, 2, 66);
    }

    /**
     * Tests if contacts can be retrieved via IDs.
     */
    @Test
    public void testGettingContactsViaID() {
        Set<Contact> several = manager.getContacts(2, 1);
        assertEquals(2, several.size());
        assertTrue(hasContact(several, "c1"));
        assertTrue(hasContact(several, "c2"));
    }

    /**
    * Tests if using a past date for future meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddingFutureMeetingTimeException() {
        manager.addFutureMeeting(allContacts, past);
    }

    /**
    * Tests if adding unknown contacts to future meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnknownContactToFutureMeetingException() {
        Set<Contact> unknown = manager.getContacts("c1");
        unknown.add(new ContactImpl(99, "unknown"));
        manager.addFutureMeeting(unknown, future);
    }

    /**
     * Tests if future meetings can be added and retrieved.
     */
    @Test
    public void testAddingAndGettingFutureMeetings() {
        manager.addFutureMeeting(allContacts, future);
        // Date assignments are dynamic to ensure tests run in the future
        Calendar future2 = new GregorianCalendar();
        future2.add(1, 2);
        manager.addFutureMeeting(allContacts, future2);
        assertEquals(1, manager.getMeeting(1).getId());
        assertEquals(2, manager.getMeeting(2).getId());
        assertEquals(future, manager.getMeeting(1).getDate());
        assertEquals(future2, manager.getMeeting(2).getDate());
    }

    /**
     * Tests if null is returned when getting a non-existing meeting.
     */
    @Test
    public void testGettingNonExistingMeeting() {
        assertNull(manager.getMeeting(99));
    }
    
    /**
    * Tests if adding unknown contacts to new past meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnknownContactToPastMeetingException() {
        Set<Contact> unknown = manager.getContacts("c1");
        unknown.add(new ContactImpl(99, "unknown"));
        manager.addNewPastMeeting(unknown, past, "");
    }
    
    /**
    * Tests if adding an empty contacts list to new past meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyContactListToPastMeetingException() {
        Set<Contact> empty = new HashSet<Contact>();
        manager.addNewPastMeeting(empty, past, "");
    }
    
    /**
    * Tests if passing 'null' as contacts to past new meetings throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testNullForContactListToPastMeetingException() {
        manager.addNewPastMeeting(null, past, "");
    }
    
    /**
    * Tests if passing 'null' as date to new past meetings throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testNullForDateToPastMeetingException() {
        manager.addNewPastMeeting(allContacts, null, "");
    }
    
    /**
    * Tests if passing 'null' as text mesage to new past meetings throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testNullForTextToPastMeetingException() {
        manager.addNewPastMeeting(allContacts, past, null);
    }
    
    /**
     * Tests if new past meetings can be added and retrieved.
     */
    @Test
    public void testAddingAndGettingPastMeetings() {
        manager.addNewPastMeeting(allContacts, past, "");
        // Date assignments are dynamic to ensure tests run in the future
        Calendar past2 = new GregorianCalendar(2013, 02, 18);
        past2.add(1, -2);
        manager.addNewPastMeeting(allContacts, past2, "");
        assertEquals(1, manager.getMeeting(1).getId());
        assertEquals(2, manager.getMeeting(2).getId());
        assertEquals(past, manager.getMeeting(1).getDate());
        assertEquals(past2, manager.getMeeting(2).getDate());
    }
}