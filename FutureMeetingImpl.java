import java.util.*;
import java.io.Serializable;
/**
 * A meeting to be held in the future
 * 
 * @author Stefan E. Mayer
 * @version 1.0
 */
public class FutureMeetingImpl extends MeetingImpl
                               implements FutureMeeting, Serializable {
    /**
     * Constructor for a future meeting.
     * 
     * @param id the id of the meeting
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     */
    public FutureMeetingImpl(int id, Set<Contact> contacts, Calendar date) {
        super(id, contacts, date);
    }
}