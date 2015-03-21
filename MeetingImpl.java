import java.util.*;
import java.io.Serializable;
/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 * 
 * @author Stefan E. Mayer
 * @version 1.0
 */
public class MeetingImpl implements Meeting, Serializable {
    private int id;
    private Set<Contact> contacts;
    private Calendar date;

    /**
     * Constructor for a new meeting.
     * 
     * @param id the id of the meeting
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