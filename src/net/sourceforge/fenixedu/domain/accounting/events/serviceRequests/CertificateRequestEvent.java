package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
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
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
        return getAdministrativeOffice().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered) {
        return getPostingRule(whenRegistered).process(responsibleUser, entryDTOs, paymentMode,
                whenRegistered, this, getPerson().getAccountBy(AccountType.EXTERNAL), getToAccount());
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        // TODO: delegate to document
        return null;

    }

    public Integer getNumberOfUnits() {
        // TODO: delegate to document
        // If number of units is not appliable to document, the document should
        // return 0
        return 0;
    }

    public Integer getNumberOfPages() {
        // TODO: delegate to document
        return 0;
    }

    public boolean isUrgentRequest() {
        // TODO: delegate to document
        return false;
    }

}
