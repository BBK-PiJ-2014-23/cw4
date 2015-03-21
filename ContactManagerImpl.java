import java.util.*;
import java.io.*;
/**
 * A class to manage your contacts and meetings.
 * 
 * @author Stefan E. Mayer
 * @version 1.0
 */
public class ContactManagerImpl implements ContactManager {
    private Set<Contact> allContacts;
    private int lastContactId;

    private List<Meeting> allMeetings;
    private int lastMeetingId;

    private File config;

    /**
     * Create a new contact manager.
     * If a config file exists, it loads data from it,
     * otherwise it creates new objects.
     */
    public ContactManagerImpl() {
        config = new File("contacts.txt");
        if (config.exists()) {
            try (FileInputStream fis = new FileInputStream(config)) {
                ObjectInputStream in = new ObjectInputStream(fis);
                allContacts = (HashSet)in.readObject();
                lastContactId = (int)in.readObject();
                allMeetings = (ArrayList)in.readObject();
                lastMeetingId = (int)in.readObject();
                in.close();
            } catch (FileNotFoundException fileNotFound) {
                fileNotFound.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (ClassNotFoundException classNotFound) {
                classNotFound.printStackTrace();
            }
        } else {
            allContacts = new HashSet<Contact>();
            lastContactId = 0;
            allMeetings = new ArrayList<Meeting>();
            lastMeetingId = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        Calendar now = new GregorianCalendar();
        if (date.before(now)) {
            throw new IllegalArgumentException("Date is in the past!");
        }
        if (hasUnknownContact(contacts)) {
            throw new IllegalArgumentException("Unknown contact(s) present!");
        }
        // Meeting ID is calculated here in order to avoid
        // static variables in the MeetingImpl class.
        lastMeetingId++;

        allMeetings.add(new FutureMeetingImpl(lastMeetingId, contacts, date));
        return lastMeetingId;
    }

    /**
     * Returns the PAST meeting with the requested ID,
     * or null if it there is none.
     * 
     * IMPORTANT: Since there are separate classes for past and
     *            future meetings, this implementation classifies
     *            meetings according to their class.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a
     *         meeting with that ID happening in the future.
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            return null;
        } else if (meeting.getClass() == FutureMeetingImpl.class) {
            throw new IllegalArgumentException(
                      "Meeting with that ID is a future meeting!");
        } else {
            return (PastMeeting)meeting;
        }
    }

    /**
     * Returns the FUTURE meeting with the requested ID,
     * or null if there is none.
     * 
     * IMPORTANT: Since there are seperate classes for past
     *            and future meetings, this implementation classifies
     *            meetings according to their class.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting
     *         with that ID happening in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            return null;
        } else if (meeting.getClass() == PastMeetingImpl.class) {
            throw new IllegalArgumentException(
                      "Meeting with that ID is a past meeting!");
        } else {
            return (FutureMeeting)meeting;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * IMPORTANT: Since there are seperate classes for past and
     *            future meetings, this implementation classifies meetings
     *            according to their class.
     * 
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled
     *         with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact is unknown!");
        }
        List<Meeting> searchedMeetings = new ArrayList<Meeting>();
        for (Meeting meeting : allMeetings) {
            if (meeting.getContacts().contains(contact) &&
                meeting.getClass() == FutureMeetingImpl.class) {
                // This block inserts meetings in a chronological fashion.
                int i = 0;
                while (i < searchedMeetings.size() &&
                       meeting.getDate().after(searchedMeetings.get(i)
                                                               .getDate())) {
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
     * IMPORTANT: According to a forum post by Sergio, this method
     *            also needs to return past meetings if the data
     *            parameter lies in the past.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param date the date
     * @return the list of meetings
     */
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        List<Meeting> searchedMeetings = new ArrayList<Meeting>();
        for (Meeting meeting : allMeetings) {
            if (isSameDay(meeting.getDate(), date)) {
                // This block inserts meetings in a chronological fashion.
                int i = 0;
                while (i < searchedMeetings.size() &&
                       meeting.getDate().after(searchedMeetings.get(i)
                                                               .getDate())) {
                    i++;
                }
                searchedMeetings.add(i, meeting);
            }
        }
        return searchedMeetings;
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     * 
     * IMPORTANT: According to the forum, 'future'
     *            is a typo and should be 'past'.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled
     *         with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     */
    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact is unknown!");
        }
        List<PastMeeting> searchedMeetings = new ArrayList<PastMeeting>();
        for (Meeting meeting : allMeetings) {
            if (meeting.getContacts().contains(contact) &&
                meeting.getClass() == PastMeetingImpl.class) {
                // This block inserts meetings in a chronological fashion.
                int i = 0;
                while (i < searchedMeetings.size() &&
                       meeting.getDate().after(searchedMeetings.get(i)
                                                               .getDate())) {
                    i++;
                }
                searchedMeetings.add(i, (PastMeeting)meeting);
            }
        }
        return searchedMeetings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts,
                                  Calendar date, String text) {
        if (contacts.size() == 0) {
            throw new IllegalArgumentException("Contact list is empty!");
        }
        if (hasUnknownContact(contacts)) {
            throw new IllegalArgumentException("Unknown contact(s) present!");
        }
        if (contacts == null || date == null || text == null) {
            throw new NullPointerException("Illegal 'null' argument(s)!");
        }
        // Meeting ID is calculated here in order to avoid
        // static variables in the MeetingImpl class.
        lastMeetingId++;

        allMeetings.add(new PastMeetingImpl(lastMeetingId,
                                            contacts, date, text));
    }

    /**
     * Add notes to a meeting.
     * 
     * IMPORTANT: As it is not entirely clear if adding notes implies
     *            changing or overwriting, this implementation will overwrite
     *            any notes present with the notes present as parameter.
     *
     * This method is used when a future meeting takes place, and is
     * then converted to a past meeting (with notes).
     *
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id the ID of the meeting
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting
     *         is set for a date in the future
     * @throws NullPointerException if the notes are null
     */
    @Override
    public void addMeetingNotes(int id, String text) {
        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            throw new IllegalArgumentException("ID is not valid");
        }
        Calendar now = new GregorianCalendar();
        if (meeting.getDate().after(now)) {
            throw new IllegalStateException(
                      "Meeting with that ID happens in the future");
        }
        if (text == null) {
            throw new NullPointerException(
                      "'null' is invalid as text parameter!");
        }

        Set<Contact> contacts = meeting.getContacts();
        Calendar date = meeting.getDate();
        allMeetings.remove(meeting);
        allMeetings.add(id - 1, new PastMeetingImpl(id, contacts, date, text));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewContact(String name, String notes) {
        if (name == null || notes == null) {
            throw new NullPointerException(
                      "'null' is invalid for either parameter!");
        }
        // Contact ID is calculated here in order to avoid
        // static variables in the ContactImpl class.
        lastContactId++;

        Contact newGuy = new ContactImpl(lastContactId, name, notes);
        allContacts.add(newGuy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public Set<Contact> getContacts(String name) {
        if (name == null) {
            throw new NullPointerException("'null' is invalid as parameter!");
        }
        Set<Contact> searched = new HashSet<Contact>();
        for (Contact member : allContacts) {
            if (member.getName().contains(name)) {
                searched.add(member);
            }
        }
        return searched;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        if (config.exists()) {
            config.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(config)) {
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(allContacts);
            out.writeObject(lastContactId);
            out.writeObject(allMeetings);
            out.writeObject(lastMeetingId);
            out.close();
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Config file cannot be created");
            fileNotFound.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Helper method that checks if a set of
     * contacts contains an unknown contact.
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

    /**
     * Helper method that checks if two calendars share the same date.
     * 
     * @param one the first calendar object
     * @param two the second calendar object
     * @return true if the dates of both calendar objects match, false otherwise
     */
    private boolean isSameDay(Calendar one, Calendar two) {
        if (one.get(Calendar.YEAR) != two.get(Calendar.YEAR)) {
            return false;
        } else if (one.get(Calendar.MONTH) != two.get(Calendar.MONTH)) {
            return false;
        } else if (one.get(Calendar.DATE) != two.get(Calendar.DATE)) {
            return false;
        }
        return true;
    }
}