
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
    final static int INVALID_ID = 99;
    final static int PAST_MEETING_ID = 1;
    final static int FUTURE_MEETING_ID = 2;

    ContactManager manager;
    Set<Contact> allContacts;
    Calendar pastDate;
    Calendar futureDate;

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
        pastDate = new GregorianCalendar();
        pastDate.add(1, -2);
        futureDate = new GregorianCalendar();
        futureDate.add(1, 2);

        // This ensures that the static variables form before are correct.
        manager.addNewPastMeeting(allContacts, pastDate, "");
        manager.addFutureMeeting(allContacts, futureDate);
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
        pastDate = null;
        futureDate = null;
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
        ContactManager empty = new ContactManagerImpl();
        Set<Contact> emptySet1 = empty.getContacts("");
        Set<Contact> emptySet2 = empty.getContacts("Tom");

        assertTrue(emptySet1.isEmpty());
        assertTrue(emptySet2.isEmpty());
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
    public void testGettingSingleContactByName() {
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
        manager.getContacts(1, 2, INVALID_ID);
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
    public void testAddFutureMeetingTimeException() {
        manager.addFutureMeeting(allContacts, pastDate);
    }

    /**
    * Tests if adding unknown contacts to future meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnknownContactToFutureMeetingException() {
        Set<Contact> unknown = manager.getContacts("c1");

        unknown.add(new ContactImpl(3, "unknown"));
        manager.addFutureMeeting(unknown, futureDate);
    }

    /**
     * Tests if future meetings can be added and retrieved.
     */
    @Test
    public void testAddAndGetFutureMeetings() {
        // Date assignments are dynamic to ensure tests run in the future
        Calendar futureDate2 = new GregorianCalendar();
        futureDate2.add(1, 2);

        manager.addFutureMeeting(allContacts, futureDate2);

        assertEquals(futureDate, manager.getMeeting(FUTURE_MEETING_ID).getDate());
        assertEquals(futureDate2, manager.getMeeting(3).getDate());
    }

    /**
     * Tests if null is returned when getting a non-existing meeting.
     */
    @Test
    public void testGettingNonExistingMeeting() {
        assertNull(manager.getMeeting(INVALID_ID));
    }

    /**
    * Tests if adding unknown contacts to new past meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnknownContactToPastMeetingException() {
        allContacts.add(new ContactImpl(99, "unknown"));
        manager.addNewPastMeeting(allContacts, pastDate, "");
    }

    /**
    * Tests if adding an empty contacts list to new past meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyContactListToPastMeetingException() {
        Set<Contact> empty = new HashSet<Contact>();
        manager.addNewPastMeeting(empty, pastDate, "");
    }

    /**
    * Tests if passing 'null' as contacts to past new meetings throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testNullForContactListToPastMeetingException() {
        manager.addNewPastMeeting(null, pastDate, "");
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
        manager.addNewPastMeeting(allContacts, pastDate, null);
    }

    /**
     * Tests if new past meetings can be added and retrieved.
     */
    @Test
    public void testAddAndGetPastMeetings() {
        // Date assignments are dynamic to ensure tests run in the future
        Calendar pastDate2 = new GregorianCalendar(2013, 02, 18);
        pastDate2.add(1, -2);

        manager.addNewPastMeeting(allContacts, pastDate2, "");

        assertEquals(pastDate, manager.getMeeting(PAST_MEETING_ID).getDate());
        assertEquals(pastDate2, manager.getMeeting(3).getDate());
    }

    /**
    * Tests if getting a past meeting that lies in the future throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingException() {
        manager.getPastMeeting(FUTURE_MEETING_ID);
    }

    /**
     * Tests if getting a non-existing past meeting return null.
     */
    @Test
    public void testGetNonExistentPastMeeting() {
        assertNull(manager.getPastMeeting(INVALID_ID));
    }

    /**
     * Tests if getting a past meeting can be retrieved via ID.
     */
    @Test
    public void testGetPastMeeting() {
        assertEquals(pastDate, manager.getPastMeeting(PAST_MEETING_ID).getDate());
    }

    /**
    * Tests if getting a future meeting that lies in the past throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingException() {
        manager.getFutureMeeting(PAST_MEETING_ID);
    }

    /**
     * Tests if getting a non-existing future meeting return null.
     */
    @Test
    public void testGetNonExistentFutureMeeting() {
        assertNull(manager.getFutureMeeting(INVALID_ID));
    }

    /**
     * Tests if getting a future meeting can be retrieved via ID.
     */
    @Test
    public void testGetFutureMeeting() {
        assertEquals(futureDate, manager.getFutureMeeting(FUTURE_MEETING_ID).getDate());
    }

    /**
     * Tests if meeting IDs are calculated properly.
     */
    @Test
    public void testMeetingIds() {
        assertEquals(1, manager.getMeeting(PAST_MEETING_ID).getId());
        assertEquals(2, manager.getMeeting(FUTURE_MEETING_ID).getId());
    }

    /**
    * Tests if getting future meetings with an unkonwn contact throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingListUnknownContactException() {
        Contact unknown = new ContactImpl(99, "unknown");
        manager.getFutureMeetingList(unknown);
    }

    /**
     * Tests if getting future meetings with a contact who has no meetings returns an empty list.
     */
    @Test
    public void testGetEmptyFutureMeetingListWithContact() {
        manager.addNewContact("Lazy", "He is so lazy");
        Set<Contact> hasLazy = manager.getContacts("Lazy");
        Contact lazy = getContact(hasLazy, "Lazy");
        List<Meeting> meetings = manager.getFutureMeetingList(lazy);
        assertEquals(0, meetings.size());
    }

    /**
     * Helper method that finds a contact in a set of contacts.
     * 
     * @param contacts the set of contacts that will be searched
     * @param name the name of the contact
     * @return the contact or null if not found
     */
    private Contact getContact(Set<Contact> contacts, String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Tests if getting future meetings of a contact returns a chronologically sorted list.
     */
    @Test
    public void testGetFutureMeetingListWithContact() {
        Calendar futureDate2 = new GregorianCalendar();
        futureDate2.add(1, 1);
        Calendar futureDate3 = new GregorianCalendar();
        futureDate3.add(1, 3);

        Set<Contact> hasC2 = manager.getContacts("c2");
        manager.addFutureMeeting(hasC2, futureDate2);
        manager.addFutureMeeting(hasC2, futureDate3);

        Contact c2 = getContact(hasC2, "c2");
        List<Meeting> meetings = manager.getFutureMeetingList(c2);
        assertEquals(3, meetings.size());
        assertEquals(futureDate2, meetings.get(0).getDate());
        assertEquals(futureDate, meetings.get(1).getDate());
        assertEquals(futureDate3, meetings.get(2).getDate());
    }
    
    /**
    * Tests if getting past meetings with an unkonwn contact throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingListUnknownContactException() {
        Contact unknown = new ContactImpl(99, "unknown");
        manager.getPastMeetingList(unknown);
    }

    /**
     * Tests if getting past meetings with a contact who has no meetings returns an empty list.
     */
    @Test
    public void testGetEmptyPastMeetingListWithContact() {
        manager.addNewContact("Lazy", "He is so lazy");
        Set<Contact> hasLazy = manager.getContacts("Lazy");
        Contact lazy = getContact(hasLazy, "Lazy");
        List<PastMeeting> meetings = manager.getPastMeetingList(lazy);
        assertEquals(0, meetings.size());
    }
    
    /**
     * Tests if getting past meetings of a contact returns a chronologically sorted list.
     */
    @Test
    public void testGetPastMeetingListWithContact() {
        Calendar pastDate2 = new GregorianCalendar();
        pastDate2.add(1, -1);
        Calendar pastDate3 = new GregorianCalendar();
        pastDate3.add(1, -3);

        Set<Contact> hasC2 = manager.getContacts("c2");
        manager.addNewPastMeeting(hasC2, pastDate2, "");
        manager.addNewPastMeeting(hasC2, pastDate3, "");

        Contact c2 = getContact(hasC2, "c2");
        List<PastMeeting> meetings = manager.getPastMeetingList(c2);
        assertEquals(3, meetings.size());
        assertEquals(pastDate3, meetings.get(0).getDate());
        assertEquals(pastDate, meetings.get(1).getDate());
        assertEquals(pastDate2, meetings.get(2).getDate());
    }
    
    /**
     * Test if getting a meeting via date that had no meeting returns en empty list.
     */
    @Test
    public void testGetEmptyMeetingListWithDate() {
        Calendar pastDate2 = new GregorianCalendar();
        pastDate2.add(1, -1);
        Calendar futureDate2 = new GregorianCalendar();
        futureDate2.add(1, 1);
        
        List<Meeting> pastEmpty = manager.getFutureMeetingList(pastDate2);
        List<Meeting> futureEmpty = manager.getFutureMeetingList(futureDate2);
        
        assertEquals(0, pastEmpty.size());
        assertEquals(0, pastEmpty.size());
    }
    
    /**
     * Test if getting meetings via date returns the list of meetings.
     */
    @Test
    public void testGetMeetingListWithDate() {
        List<Meeting> future = manager.getFutureMeetingList(futureDate);
        assertEquals(1, future.size());
        
        Set<Contact> one = manager.getContacts(0);
        manager.addFutureMeeting(one, futureDate);
        
        future = manager.getFutureMeetingList(futureDate);
        assertEquals(2, future.size());
    }
}