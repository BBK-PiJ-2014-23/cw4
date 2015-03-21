import java.io.Serializable;
/**
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique), a name (probably unique, but maybe
 * not), and notes that the user may want to save about them.
 * 
 * @author Stefan E. Mayer
 * @version 1.0
 */
public class ContactImpl implements Contact, Serializable {
    private int id;
    private String name;
    private String notes;
    
    /**
     * Create a new contact with the specified name and notes.
     *
     * @param id the id of the contact.
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     */
    public ContactImpl(int id, String name, String notes) {
        this.id = id;
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
     * Add notes about the contact.
     * 
     * IMPORTANT: As it is not entirely clear if adding notes implies changing or overwriting,
     * this implementation will overwrite any notes present with the notes present as parameter.
     *
     * @param note the notes to be added
     */
    @Override
    public void addNotes(String note) {
        this.notes = note;
    }
}