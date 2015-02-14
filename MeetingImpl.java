import java.util.Calendar;
import java.util.Set;
import java.util.*;
/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public class MeetingImpl implements Meeting {
    private int id;
    private Set<Contact> contacts;
    private Calendar date;

    /**
     * Constructor for a new meeting.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     */
    public MeetingImpl(int id, Set<Contact> contacts, Calendar date) {
        this.id = id;
        this.contacts = contacts;
        this.date = date;        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar getDate() {
        return date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }
}