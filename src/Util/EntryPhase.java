/*
 * Created on Jun 23, 2004
 *  
 */
package Util;

/**
 * @author João Mota
 *  
 */
public class EntryPhase {

    public static final int FIRST_PHASE = 1;

    public static final int SECOND_PHASE = 2;

    public static final String FIRST_PHASE_STRING = "Licenciatura";

    public static final String SECOND_PHASE_STRING = "Mestrado";

    public static final EntryPhase FIRST_PHASE_OBJ = new EntryPhase(
            EntryPhase.FIRST_PHASE);

    public static final EntryPhase SECOND_PHASE_OBJ = new EntryPhase(
            EntryPhase.SECOND_PHASE);

    private Integer entryPhase;

    public EntryPhase() {
    }

    public EntryPhase(int entryPhase) {
        this.entryPhase = new Integer(entryPhase);
    }

    public EntryPhase(Integer entryPhase) {
        this.entryPhase = entryPhase;
    }

    public EntryPhase(String entryPhase) {
        if (entryPhase.equals(EntryPhase.FIRST_PHASE_STRING))
                this.entryPhase = new Integer(EntryPhase.FIRST_PHASE);
        if (entryPhase.equals(EntryPhase.SECOND_PHASE_STRING))
                this.entryPhase = new Integer(EntryPhase.SECOND_PHASE);
    }

    /**
     * @return Returns the entryPhase.
     */
    public Integer getEntryPhase() {
        return entryPhase;
    }
    /**
     * @param entryPhase The entryPhase to set.
     */
    public void setEntryPhase(Integer entryPhase) {
        this.entryPhase = entryPhase;
    }
}