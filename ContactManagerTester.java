
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * The test class ContactManagerTester.
 *
 * @author  Stefan E. Mayer
 * @version 1.0
 */
public class ContactManagerTester {
    final static int INVALID_ID = 99;
    final static int NEGATIVE_ID = -1;
    final static int TWO_HOURS_EARLIER_ID = 1;
    final static int TWO_HOURS_LATER_ID = 2;
    static File config = new File("contacts.txt");

    ContactManager manager;
    Set<Contact> allContacts;
    Set<Contact> onlyOneContact;
    Set<Contact> onlyLazy;

    Calendar threeHoursEarlier;
    Calendar twoHoursEarlier;
    Calendar twoHoursLater;
    Calendar threeHoursLater;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        // Date assignments are dynamic to ensure tests run in the future
        threeHoursEarlier = new GregorianCalendar();
        threeHoursEarlier.add(Calendar.HOUR_OF_DAY, -3);
        twoHoursEarlier = new GregorianCalendar();
        twoHoursEarlier.add(Calendar.HOUR_OF_DAY, -2);
        twoHoursLater = new GregorianCalendar();
        twoHoursLater.add(Calendar.HOUR_OF_DAY, 2);
        threeHoursLater = new GregorianCalendar();
        threeHoursLater.add(Calendar.HOUR_OF_DAY, 3);

        // Creating a new contact manager with some contacts
        manager = new ContactManagerImpl();
        manager.addNewContact("c1", "notes1");
        manager.addNewContact("c2", "notes2");
        manager.addNewContact("c3", "notes3");

        // Getting some sets of contacts to work with
        allContacts = manager.getContacts(1, 2, 3);
        onlyOneContact = manager.getContacts("c2");

        // Providing a past and a future meeting. This ensures that the static variables form before are correct.
        manager.addNewPastMeeting(allContacts, twoHoursEarlier, "");
        manager.addFutureMeeting(allContacts, twoHoursLater);

        // Adding another contact who does not attent meetings and a set containing this contact
        manager.addNewContact("Lazy", "");
        onlyLazy = manager.getContacts("Lazy");
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
        onlyOneContact = null;
        onlyLazy = null;
        threeHoursEarlier = null;
        twoHoursEarlier = null;
        twoHoursLater = null;
        threeHoursLater = null;
        // Ensure that following tests start with a clean config file.
        config.delete();
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

        assertTrue(empty.getContacts("").isEmpty());
        assertTrue(empty.getContacts("UnknownContact").isEmpty());
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
        assertTrue(manager.getContacts("UnknownContact").isEmpty());
    }

    /**
     * Tests retrieving a contact without a name which should retrieve all contacts.
     */
    @Test
    public void testGettingEmptyNamedContact() {
        Set<Contact> all = manager.getContacts("");

        assertEquals(4, all.size());
        assertTrue(hasContact(all, "c1"));
        assertTrue(hasContact(all, "c2"));
        assertTrue(hasContact(all, "c3"));
        assertTrue(hasContact(all, "Lazy"));
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
        manager.getContacts(1, 2, NEGATIVE_ID);
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
        manager.addFutureMeeting(allContacts, twoHoursEarlier);
    }

    /**
    * Tests if adding unknown contacts to future meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddUnknownContactToFutureMeetingException() {
        allContacts.add(new ContactImpl(3, "unknown"));
        manager.addFutureMeeting(allContacts, twoHoursLater);
    }

    /**
     * Tests if future meetings can be retrieved via ID..
     */
    @Test
    public void testAddAndGetFutureMeetings() {
        // Date assignments are dynamic to ensure tests run in the future
        Calendar oneHourLater = new GregorianCalendar();
        oneHourLater.add(Calendar.HOUR_OF_DAY, 1);

        manager.addFutureMeeting(allContacts, oneHourLater);

        assertEquals(twoHoursLater, manager.getMeeting(TWO_HOURS_LATER_ID).getDate());
        assertEquals(oneHourLater, manager.getMeeting(3).getDate());
    }

    /**
     * Tests if null is returned when getting a meeting via a non-existing ID.
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
        allContacts.add(new ContactImpl(99, "UnknownContact"));
        manager.addNewPastMeeting(allContacts, twoHoursEarlier, "");
    }

    /**
    * Tests if adding an empty contacts list to new past meetings throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyContactListToPastMeetingException() {
        Set<Contact> empty = new HashSet<Contact>();
        manager.addNewPastMeeting(empty, twoHoursEarlier, "");
    }

    /**
    * Tests if passing 'null' as contacts to past new meetings throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testNullForContactListToPastMeetingException() {
        manager.addNewPastMeeting(null, twoHoursEarlier, "");
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
        manager.addNewPastMeeting(allContacts, twoHoursEarlier, null);
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

        assertEquals(twoHoursEarlier, manager.getMeeting(TWO_HOURS_EARLIER_ID).getDate());
        assertEquals(pastDate2, manager.getMeeting(3).getDate());
    }

    /**
    * Tests if getting a past meeting that lies in the future throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingException() {
        manager.getPastMeeting(TWO_HOURS_LATER_ID);
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
        assertEquals(TWO_HOURS_EARLIER_ID, manager.getPastMeeting(TWO_HOURS_EARLIER_ID).getId());
    }

    /**
    * Tests if getting a future meeting that lies in the past throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingException() {
        manager.getFutureMeeting(TWO_HOURS_EARLIER_ID);
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
        assertEquals(TWO_HOURS_LATER_ID, manager.getFutureMeeting(TWO_HOURS_LATER_ID).getId());
    }

    /**
     * Tests if meeting IDs are calculated properly.
     */
    @Test
    public void testMeetingIds() {
        assertEquals(1, manager.getMeeting(TWO_HOURS_EARLIER_ID).getId());
        assertEquals(2, manager.getMeeting(TWO_HOURS_LATER_ID).getId());
    }

    /**
    * Tests if getting future meetings of an unkonwn contact throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingListUnknownContactException() {
        Contact unknown = new ContactImpl(99, "UnknownContact");
        manager.getFutureMeetingList(unknown);
    }

    /**
     * Tests if getting future meetings with a contact who has no meetings returns an empty list.
     */
    @Test
    public void testGetEmptyFutureMeetingListWithContact() {
        Contact lazy = getContact(onlyLazy, "Lazy");
        assertTrue(manager.getFutureMeetingList(lazy).isEmpty());
    }

    /**
     * Helper method that finds a contact in a set of contacts.
     * 
     * THIS IS BUGGY. DOES NOT ITTERATE(?)
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
        manager.addFutureMeeting(onlyOneContact, threeHoursLater);
        Contact c2 = getContact(onlyOneContact, "c2");
        List<Meeting> meetings = manager.getFutureMeetingList(c2);

        assertEquals(2, meetings.size());
        assertEquals(twoHoursLater, meetings.get(0).getDate());
        assertEquals(threeHoursLater, meetings.get(1).getDate());
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
        Contact lazy = getContact(onlyLazy, "Lazy");
        assertTrue(manager.getPastMeetingList(lazy).isEmpty());
    }

    /**
     * Tests if getting past meetings of a contact returns a chronologically sorted list.
     */
    @Test
    public void testGetPastMeetingListWithContact() {
        manager.addNewPastMeeting(onlyOneContact, threeHoursEarlier, "");
        Contact c2 = getContact(onlyOneContact, "c2");
        List<PastMeeting> meetings = manager.getPastMeetingList(c2);

        assertEquals(2, meetings.size());
        assertEquals(threeHoursEarlier, meetings.get(0).getDate());
        assertEquals(twoHoursEarlier, meetings.get(1).getDate());
    }

    /**
     * Test if getting a meeting via date that had no meeting returns en empty list.
     */
    @Test
    public void testGetEmptyMeetingListWithDate() {
        Calendar longAgo = new GregorianCalendar();
        longAgo.add(1, -1);
        Calendar farAhead = new GregorianCalendar();
        farAhead.add(1, 1);

        List<Meeting> pastEmpty = manager.getFutureMeetingList(longAgo);
        List<Meeting> futureEmpty = manager.getFutureMeetingList(farAhead);

        assertTrue(pastEmpty.isEmpty());
        assertTrue(futureEmpty.isEmpty());
    }

    /**
     * Test if getting meetings via date returns a chronological list of meetings from that day.
     */
    @Test
    public void testGetMeetingListWithDate() {
        List<Meeting> future = manager.getFutureMeetingList(twoHoursLater);
        assertEquals(2, future.size());
        assertEquals(twoHoursEarlier, future.get(0).getDate());
        assertEquals(twoHoursLater, future.get(1).getDate());

        manager.addNewPastMeeting(onlyOneContact, threeHoursEarlier, "");
        manager.addFutureMeeting(onlyOneContact, threeHoursLater);

        future = manager.getFutureMeetingList(twoHoursLater);

        assertEquals(4, future.size());
        assertEquals(threeHoursEarlier, future.get(0).getDate());
        assertEquals(twoHoursEarlier, future.get(1).getDate());
        assertEquals(twoHoursLater, future.get(2).getDate());
        assertEquals(threeHoursLater, future.get(3).getDate());
    }

    /**
    * Tests if adding notes to an unknown meeting throws an exception.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMeetingNotesMeetingNotExistsException() {
        manager.addMeetingNotes(INVALID_ID, "notes");
    }

    /**
    * Tests if adding notes to a future meeting throws an exception.
    */
    @Test(expected = IllegalStateException.class)
    public void testAddMeetingNotesToFutureMeetingException() {
        manager.addMeetingNotes(TWO_HOURS_LATER_ID, "notes");
    }

    /**
    * Tests if adding null to a meeting's notes throws an exception.
    */
    @Test(expected = NullPointerException.class)
    public void testAddMeetingNotesNullException() {
        manager.addMeetingNotes(TWO_HOURS_EARLIER_ID, null);
    }

    /**
     * Test if formerly future meetings that took place are converted and if notes are added.
     */
    @Test
    public void testAddMeetingNotesConvertToPastMeeting() {
        Calendar now = new GregorianCalendar();
        manager.addFutureMeeting(allContacts, now);

        manager.addMeetingNotes(3, "Just happened");

        assertTrue(manager.getMeeting(3).getClass() == PastMeetingImpl.class);
        assertEquals("Just happened", manager.getPastMeeting(3).getNotes());
    }

    /**
     * Test if notes are added to past meetings.
     */
    @Test
    public void testAddMeetingNotesToPastMeeting() {
        manager.addMeetingNotes(TWO_HOURS_EARLIER_ID, "Notes added");
        assertEquals("Notes added", manager.getPastMeeting(TWO_HOURS_EARLIER_ID).getNotes());
    }

    /**
     * Test if a config file is created when there isn't one.
     */
    @Test
    public void testFlushCreateConfigFile() {
        manager.flush();
        assertTrue(config.exists());
    }

    /**
     * Test if objects are loaded properly if a config file exists.
     */
    @Test
    public void testConstructorLoadConfig() {
        // This ensures that new contact manager 'configured' will find a config file.
        manager.flush();

        ContactManager configured = new ContactManagerImpl();

        // There should be four contacts
        Set<Contact> all = configured.getContacts("");
        assertEquals(4, all.size());
        assertTrue(hasContact(all, "c1"));
        assertTrue(hasContact(all, "c2"));
        assertTrue(hasContact(all, "c3"));
        assertTrue(hasContact(all, "Lazy"));

        // There should be one past and one future meeting
        assertEquals(twoHoursEarlier, configured.getMeeting(TWO_HOURS_EARLIER_ID).getDate());
        assertEquals(twoHoursLater, configured.getMeeting(TWO_HOURS_LATER_ID).getDate());
        assertNull(configured.getMeeting(3));
    }

    /**
     * Test if loading from an empty config files causes issues.
     */
    @Test
    public void testFlushEmptyManager() {
        // Delte present config file so it won't be loaded.
        config.delete();

        // Create a new contact manager and save an empty state
        ContactManager empty = new ContactManagerImpl();
        empty.flush();

        // Creat another contact manager that loads an empty state
        ContactManager anotherEmpty = new ContactManagerImpl();
        Set<Contact> all = anotherEmpty.getContacts("");
        assertEquals(0, all.size());

        assertNull(anotherEmpty.getMeeting(0));
    }
}