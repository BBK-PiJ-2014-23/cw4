import java.util.Calendar;
import java.util.Set;
import java.util.*;
/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public class MeetingImpl implements Meeting {
    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return -1;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate() {
        return new GregorianCalendar();
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
        return new HashSet<Contact>();
    }
}