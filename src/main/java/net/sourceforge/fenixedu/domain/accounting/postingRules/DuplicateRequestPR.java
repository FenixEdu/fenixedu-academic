package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DuplicateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.DuplicateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DuplicateRequestPR extends DuplicateRequestPR_Base {

    protected DuplicateRequestPR() {
        super();
    }

    public DuplicateRequestPR(final DateTime startDate, ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        init(EntryType.DUPLICATE_REQUEST_FEE, EventType.DUPLICATE_REQUEST, startDate, null, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        RegistrationAcademicServiceRequest academicServiceRequest = ((DuplicateRequestEvent) event).getAcademicServiceRequest();
        return ((DuplicateRequest) academicServiceRequest).getAmountToPay();
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        final Money totalAmountToPay = calculateTotalAmountToPay(event, when);
        return Collections.singletonList(new EntryDTO(getEntryType(), event, totalAmountToPay, Money.ZERO, totalAmountToPay,
                event.getDescriptionForEntryType(getEntryType()), totalAmountToPay));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        if (entryDTOs.size() != 1) {
            throw new DomainException("error.accounting.postingRules.DuplicateRequestPR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();
        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
                entryDTO.getAmountToPay(), transactionDetail));
    }

}
