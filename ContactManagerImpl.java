import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.*;
/**
 * A class to manage your contacts and meetings.
 */
public class ContactManagerImpl implements ContactManager {
    private Set<Contact> allContacts;
    private int lastContactId;
    private List<Meeting> allMeetings;
    private int lastMeetingId;

    /**
     * Create a new contact manager.
     */
    public ContactManagerImpl() {
        allContacts = new HashSet<Contact>();
        lastContactId = 0;
        allMeetings = new ArrayList<Meeting>();
        lastMeetingId = 0;
    }

    /**
     * Add a new meeting to be held in the future.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     * of if any contact is unknown / non-existent
     */
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        Calendar now = new GregorianCalendar();
        if (date.before(now)) {
            throw new IllegalArgumentException("Date is in the past!");
        }
        if (hasUnknownContact(contacts)) {
            throw new IllegalArgumentException("Unknown contact(s) present!");
        }
        lastMeetingId++;
        allMeetings.add(new FutureMeetingImpl(lastMeetingId, contacts, date));
        return lastMeetingId;
    }

    /**
     * Returns the PAST meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the future
     */
    public PastMeeting getPastMeeting(int id) {
        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            return null;
        } else if (meeting.getClass() == FutureMeetingImpl.class) {
            throw new IllegalArgumentException("Meeting with that ID happens in the future");
        } else {
            return (PastMeeting)meeting;
        }
    }

    /**
     * Returns the FUTURE meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    public FutureMeeting getFutureMeeting(int id) {
        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            return null;
        } else if (meeting.getClass() == PastMeetingImpl.class) {
            throw new IllegalArgumentException("Meeting with that ID happend in the past");
        } else {
            return (FutureMeeting)meeting;
        }
    }

    /**
     * Returns the meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
    public Meeting getMeeting(int id) {
        for (Meeting meeting : allMeetings) {
            if (meeting.getId() == id) {
                return meeting;
            } 
        }
        return null;
    }

    /**
     * Returns the list of future meetings scheduled with this contact.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact is unknown!");
        }
        List<Meeting> searchedMeetings = new ArrayList<Meeting>();
        for (Meeting meeting : allMeetings) {
            if (meeting.getContacts().contains(contact) && meeting.getClass() == FutureMeetingImpl.class) {
                // This block inserts meetings in a chronological fashion.
                int i = 0;
                while (i < searchedMeetings.size() && meeting.getDate().after(searchedMeetings.get(i).getDate())) {
                    i++;
                }
                searchedMeetings.add(i, meeting);
            }
        }
        return searchedMeetings;
    }

    /**
     * Returns the list of meetings that are scheduled for, or that took
     * place on, the specified date
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     * 
     * FORUM:
     * The method must return also past meetings if it is called with a 
     * past date as parameter.
     * The name is confusing, a better name would have been getMeetingList(Calendar) 
     * or even getMeetingListOn(Calendar). We apologise for the confusion.
     *
     * @param date the date
     * @return the list of meetings
     */
    public List<Meeting> getFutureMeetingList(Calendar date) {
        return null;
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * FORUM:
     * 'future' should be a typo, should be 'past'.
     * @throws IllegalArgumentException if the contact does not exist
     */
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact is unknown!");
        }
        List<PastMeeting> searchedMeetings = new ArrayList<PastMeeting>();
        for (Meeting meeting : allMeetings) {
            if (meeting.getContacts().contains(contact) && meeting.getClass() == PastMeetingImpl.class) {
                // This block inserts meetings in a chronological fashion.
                int i = 0;
                while (i < searchedMeetings.size() && meeting.getDate().after(searchedMeetings.get(i).getDate())) {
                    i++;
                }
                searchedMeetings.add(i, (PastMeeting)meeting);
            }
        }
        return searchedMeetings;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is
     * empty, or any of the contacts does not exist
     * @throws NullPointerException if any of the arguments is null
     */
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if (contacts == null || date == null || text == null) {
            throw new NullPointerException("Illegal arguement(s)!");
        }
        if (contacts.size() == 0) {
            throw new IllegalArgumentException("Contact list is empty!");
        }
        if (hasUnknownContact(contacts)) {
            throw new IllegalArgumentException("Unknown contact(s) present!");
        }
        lastMeetingId++;
        allMeetings.add(new PastMeetingImpl(lastMeetingId, contacts, date, text));
    }

    /**
     * Add notes to a meeting.
     *
     * This method is used when a future meeting takes place, and is
     * then converted to a past meeting (with notes).
     *
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id the ID of the meeting
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting is set for a date in the future
     * @throws NullPointerException if the notes are null
     */
    public void addMeetingNotes(int id, String text) {

    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null
     */
    public void addNewContact(String name, String notes) {
        if (name == null || notes == null) {
            throw new NullPointerException("'null' is invalid for either parameter!");
        }
        lastContactId++;
        Contact newGuy = new ContactImpl(lastContactId, name);
        newGuy.addNotes(notes);
        allContacts.add(newGuy);
    }

    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
     */
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> searched = new HashSet<Contact>();
        for (int id : ids) {
            if (id <= 0 || id > lastContactId) {
                throw new IllegalArgumentException("ID is not valid");
            }
            for (Contact member : allContacts) {
                if (member.getId() == id) {
                    searched.add(member);
                }
            }
        }
        return searched;
    }

    /**
     * Returns a list with the contacts whose name contains that string.
     *
     * @param name the string to search for
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
    public Set<Contact> getContacts(String name) {
        if (name == null) {
            throw new NullPointerException("'null' is invalid as parameter!");
        }
        Set<Contact> searched = new HashSet<Contact>();
        // Empty name must be excluded, otherwise all contacts are returned.
        if (name.length() > 0) {
            for (Contact member : allContacts) {
                if (member.getName().contains(name)) {
                    searched.add(member);
                }
            }
        }
        return searched;
    }

    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is
     * closed and when/if the user requests it.
     */
    public void flush() {

    }

    /**
     * Helper method that checks if a set of contacts contains an unknown contact.
     * 
     * @param contacts a set of contacts
     * @return true if one contact is unknown, false otherwise
     */
    private boolean hasUnknownContact(Set<Contact> contacts) {
        for (Contact member : contacts) {
            if (!allContacts.contains(member)) {
                return true;
            }
        }
        return false;
    }
}