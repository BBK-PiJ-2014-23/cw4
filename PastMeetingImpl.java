import java.util.*;
import java.io.Serializable;
/**
 * A meeting that was held in the past.
 *
 * It includes your notes about what happened and what was agreed.
 * 
 * @author Stefan E. Mayer
 * @version 1.0
 */
public class PastMeetingImpl extends MeetingImpl
                             implements PastMeeting, Serializable {
    private String notes;

    /**
     * Create a new record for a meeting that took place in the past.
     * 
     * @param id the id of the meeting
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     */
    public PastMeetingImpl(int id, Set<Contact> contacts,
                           Calendar date, String text) {
        super(id, contacts, date);
        notes = text;
    }

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    @Override
    public String getNotes() {
        return notes;
    }
}