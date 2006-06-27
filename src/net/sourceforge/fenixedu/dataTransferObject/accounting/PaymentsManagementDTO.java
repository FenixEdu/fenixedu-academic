/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;

public class PaymentsManagementDTO implements Serializable {

    private DomainReference<Candidacy> candidacy;
    private List<EntryDTO> entryDTOs;
    
    public PaymentsManagementDTO(Candidacy candidacy) {
        setCandidacy(candidacy);
        setEntryDTOs(new ArrayList<EntryDTO>());
    }
    
    public Candidacy getCandidacy() {
        return (this.candidacy != null) ? this.candidacy.getObject() : null;
    }

    public void setCandidacy(Candidacy candidacy) {
        this.candidacy = (candidacy != null) ? new DomainReference<Candidacy>(candidacy) : null;
    }

    public List<EntryDTO> getEntryDTOs() {
        return entryDTOs;
    }
    
    private void setEntryDTOs(List<EntryDTO> entryDTOs) {
        this.entryDTOs = entryDTOs;
    }

}
