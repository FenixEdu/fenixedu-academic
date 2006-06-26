/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.domain.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class DFACandidacyPaymentEvent extends Event {
    
    public DFACandidacyPaymentEvent(DFACandidacyEvent candidacyEvent) {
        super();
//        init(candidacy);
//        init(EventType.CANDIDACY_ENROLMENT, whenOccured, candidacy.getPerson());
        setPayedEvent(candidacyEvent);
    }

    @Override
    protected boolean checkIfIsProcessed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<EntryDTO> calculateEntries() {
        // TODO Auto-generated method stub
        return null;
    }

}
