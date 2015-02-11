/**
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique), a name (probably unique, but maybe
 * not), and notes that the user may want to save about them.
 */
public class ContactImpl implements Contact {
    private static int lastId = 0;
    
    private int id;
    private String name;
    private String notes;
    
    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     */
    public ContactImpl(String name, String notes) {
        lastId++;
        this.id = lastId;
        this.name = name;
        this.notes = notes;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotes(String note) {
        this.notes = note;
    }
}