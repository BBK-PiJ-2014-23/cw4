import java.util.Calendar;
import java.util.Set;
import java.util.*;
/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public class MeetingImpl implements Meeting {
    private static int lastId = 0;
    
    private int id;
    private Set<Contact> contacts;
    private Calendar date;
    
    /**
     * Constructor for a new meeting.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     */
    public MeetingImpl(Set<Contact> contacts, Calendar date) {
        lastId++;
        this.id = lastId;
        this.contacts = contacts;
        this.date = date;        
    }
    
    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return id;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Return the details of people that attended the meeting.
     *
     * The list contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an
     * arbitraty number of them.
     *
     * @return the details of people that attended the meeting.
     */
    public Set<Contact> getContacts() {
        return contacts;
    }
}