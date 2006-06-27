/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DFACandidacyPaymentEvent extends DFACandidacyPaymentEvent_Base {

    public DFACandidacyPaymentEvent(DFACandidacyEvent candidacyEvent, DateTime whenOccured) {
        super();
        checkParameters(candidacyEvent);
        init(EventType.CANDIDACY_ENROLMENT_PAYMENT, whenOccured, candidacyEvent.getCandidacy()
                .getPerson());
        initPayedEvent(candidacyEvent);
    }

    private void checkParameters(DFACandidacyEvent candidacyEvent) {
        if (candidacyEvent == null) {
            throw new DomainException("error.candidacy.dfaCandidacyPaymentEvent.invalid.candidacyEvent");
        }
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {

        if (getDFACandidacyEvent().isClosed()) {
            throw new DomainException("error.candidacy.dfaCandidacyPaymentEvent.candidacyEventIsClosed");
        }

        // TODO: delegate to posting rule

        final Account personExternalAccount = getPersonAccountBy(AccountType.EXTERNAL);
        final Account personInternalAccount = getPersonAccountBy(AccountType.INTERNAL);
        final Account degreeInternalAccount = getDegreeAccountBy(AccountType.INTERNAL);

        for (final EntryDTO entry : entryDTOs) {
            checkAmount(entry.getAmountToPay());
            makeAccountingTransaction(personExternalAccount, personInternalAccount, EntryType.TRANSFER,
                    entry.getAmountToPay());
            makeAccountingTransaction(personInternalAccount, degreeInternalAccount,
                    entry.getEntryType(), entry.getAmountToPay());
        }

        closeEvent();
        getDFACandidacyEvent().process(null);

    }

    private void checkAmount(BigDecimal amountToPay) {
        if (amountToPay.add(calculateTotalPayedAmount(EntryType.CANDIDACY_ENROLMENT_FEE)).compareTo(
                calculateAmountToPay()) > 1) {
            throw new DomainException("error.candidacy.dfaCandidacyPaymentEvent.invalid.amountToPay");
        }
    }

    // TODO: delegate to posting rule
    private BigDecimal calculateAmountToPay() {
        return new BigDecimal("100");

    }

    // TODO: remove after posting rules? (get from PayedEvent) must include
    // penalties
    public BigDecimal calculateAmount() {
        return calculateTotalPayedAmount(EntryType.CANDIDACY_ENROLMENT_FEE);
    }

    // TODO: remove after posting rules?
    private BigDecimal calculateTotalPayedAmount(EntryType entryType) {
        BigDecimal result = new BigDecimal("0");
        for (final Entry entry : getEntriesSet()) {
            if (entry.getEntryType() == entryType && entry.isPositiveAmount()) {
                result = result.add(entry.getAmount());
            }
        }
        return result;
    }

    private DFACandidacyEvent getDFACandidacyEvent() {
        return (DFACandidacyEvent) getPayedEvent();
    }

    private Account getDegreeAccountBy(AccountType accountType) {
        return getDFACandidacyEvent().getCandidacy().getExecutionDegree().getDegreeCurricularPlan()
                .getDegree().getUnit().getAccountBy(accountType);
    }

    private Account getPersonAccountBy(AccountType accountType) {
        return getDFACandidacyEvent().getCandidacy().getPerson().getAccountBy(accountType);
    }

    @Override
    public List<EntryDTO> calculateEntries() {
        // TODO Auto-generated method stub
        return null;
    }

}
