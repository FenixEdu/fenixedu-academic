package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class CertificateRequestEvent extends CertificateRequestEvent_Base {

    public CertificateRequestEvent() {
        super();
    }

    @Override
    public Account getToAccount() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EntryDTO> calculateEntries(DateTime when) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getNumberOfUnits() {
        // TODO: implement
        //If number of units is not appliable to document, the document should return  0
        return 0;
    }
    
    public Integer getNumberOfPages() {
        // TODO: implement
        return 0;
    }

    public boolean isUrgentRequest() {
        // TODO: implement
        return false;
    }

}
