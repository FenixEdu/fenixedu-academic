/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.util.EntryPhase;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class IngressionInformationBean implements Serializable {
    
    private Ingression ingression;
    
    private EntryPhase entryPhase;

    public IngressionInformationBean() {
	super();
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public Ingression getIngression() {
        return ingression;
    }

    public void setIngression(Ingression ingression) {
        this.ingression = ingression;
    }
    
}
