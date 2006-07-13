package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.accounting.PostingRuleDomainException;

import org.joda.time.DateTime;

public class FixedAmountPR extends FixedAmountPR_Base {

    private FixedAmountPR() {
        super();
    }

    public FixedAmountPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal amount) {
        this();
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, amount);
    }

    private void checkParameters(EntryType entryType, BigDecimal amount) {
        if (entryType == null) {
            throw new DomainException(
                    "error.accounting.postingRules.FixedAmountPR.entryType.cannot.be.null");
        }
        if (amount == null) {
            throw new DomainException(
                    "error.accounting.postingRules.FixedAmountPR.amount.cannot.be.null");
        }

    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal amount) {
        super.init(eventType, startDate, endDate, serviceAgreementTemplate);
        checkParameters(entryType, amount);
        super.setAmount(amount);
        super.setEntryType(entryType);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
            Account toAccount) {

        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.FixedAmountPR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.get(0);
        checkIfCanAddAmount(entryDTO.getAmountToPay(), event);

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
                entryDTO.getEntryType(), entryDTO.getAmountToPay(), paymentMode, whenRegistered));

    }

    @Override
    public void setAmount(BigDecimal amount) {
        throw new DomainException("error.accounting.postingRules.FixedAmountPR.cannot.modify.amount");
    }

    private void checkIfCanAddAmount(final BigDecimal amountToPay, final Event event) {
        if (!amountToPay.equals(getAmount())) {
            throw new PostingRuleDomainException(
                    "error.accounting.postingRules.FixedAmountPR.amount.being.payed.must.match.amount.to.pay",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, getAmount(),
                new BigDecimal("0"), getAmount(), event.getDescriptionForEntryType(getEntryType())));
    }

    @Override
    public BigDecimal calculateTotalAmountToPay() {
        return getAmount();
    }

}
