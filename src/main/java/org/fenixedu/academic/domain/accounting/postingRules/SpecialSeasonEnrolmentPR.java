package org.fenixedu.academic.domain.accounting.postingRules;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.SpecialSeasonEnrolmentEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class SpecialSeasonEnrolmentPR extends SpecialSeasonEnrolmentPR_Base {
    
    protected SpecialSeasonEnrolmentPR() {
        super();
    }

    public SpecialSeasonEnrolmentPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
        super.init(EntryType.SPECIAL_SEASON_ENROLMENT_FEE, EventType.SPECIAL_SEASON_ENROLMENT, startDate,
                endDate, serviceAgreementTemplate);
        checkParameters(fixedAmount);
        super.setFixedAmount(fixedAmount);
    }

    private void checkParameters(Money fixedAmount) {
        if (fixedAmount == null) {
            throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmount.cannot.be.null");
        }
    }

    public SpecialSeasonEnrolmentPR edit(final Money fixedAmount) {
        deactivate();
        return new SpecialSeasonEnrolmentPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final SpecialSeasonEnrolmentEvent specialSeasonEnrolmentEvent = (SpecialSeasonEnrolmentEvent) event;
        final int numberOfEnrolments = specialSeasonEnrolmentEvent.getSpecialSeasonEnrolmentEvaluationsSet().size();
        return getFixedAmount().multiply(numberOfEnrolments);
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
            throw new DomainException(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();
        checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
                entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime when) {
        if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) < 0) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.amount.being.payed.must.match.amount.to.pay",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

}
