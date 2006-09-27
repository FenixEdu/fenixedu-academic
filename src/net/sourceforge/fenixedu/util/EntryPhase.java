/*
 * Created on Jun 23, 2004
 *  
 */
package net.sourceforge.fenixedu.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author João Mota
 * 
 */
public class EntryPhase implements Serializable {

    public static final int FIRST_PHASE = 1;

    public static final int SECOND_PHASE = 2;

    public static final String FIRST_PHASE_STRING = "1ª Fase";

    public static final String SECOND_PHASE_STRING = "2ª Fase";

    public static final EntryPhase FIRST_PHASE_OBJ = new EntryPhase(EntryPhase.FIRST_PHASE);

    public static final EntryPhase SECOND_PHASE_OBJ = new EntryPhase(EntryPhase.SECOND_PHASE);

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
         * @param entryPhase
         *                The entryPhase to set.
         */
    public void setEntryPhase(Integer entryPhase) {
	this.entryPhase = entryPhase;
    }

    public String toString() {
	switch (entryPhase.intValue()) {
	case FIRST_PHASE:
	    return "FIRST_PHASE";
	case SECOND_PHASE:
	    return "SECOND_PHASE";
	default:
	    throw new Error("Unknown entry phase value.");
	}
    }

    public static List<EntryPhase> getAll() {
	ArrayList<EntryPhase> result = new ArrayList<EntryPhase>();
	result.add(EntryPhase.FIRST_PHASE_OBJ);
	result.add(EntryPhase.SECOND_PHASE_OBJ);
	return result;
    }

    public String getName() {
	if (this.entryPhase.equals(1)) {
	    return EntryPhase.FIRST_PHASE_STRING;
	}
	if (this.entryPhase.equals(2)) {
	    return EntryPhase.SECOND_PHASE_STRING;
	}
	return null;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof EntryPhase) {
	    return ((EntryPhase) obj).getEntryPhase().equals(this.getEntryPhase());
	}
	return false;
    }

}