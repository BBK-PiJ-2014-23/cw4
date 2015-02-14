import java.util.*;
/**
 * A meeting that was held in the past.
 *
 * It includes your notes about what happened and what was agreed.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes;

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     */
    public PastMeetingImpl(int id, Set<Contact> contacts, Calendar date, String text) {
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