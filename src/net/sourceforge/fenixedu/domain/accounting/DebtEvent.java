package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

public abstract class DebtEvent extends DebtEvent_Base {

    protected DebtEvent() {
        super();
    }

    public DebtEvent(EventType eventType, DateTime whenOccured, Party party) {
        this();
        init(eventType, whenOccured, party);
    }

    protected void init(EventType eventType, DateTime whenOccured, Party party) {
        init(eventType, whenOccured);
        super.setParty(party);
    }

    @Override
    public void setParty(Party party) {
        throw new DomainException("error.accounting.debtEvent.cannot.modify.party");
    }

    @Override
    public void closeEvent() {
        if (canCloseEvent()) {
            super.closeEvent();
        }
    }

    protected BigDecimal calculateTotalPayedAmount() {
        BigDecimal totalPayedAmount = new BigDecimal("0");
        for (final PaymentEvent paymentEvent : getPaymentEvents()) {
            totalPayedAmount = totalPayedAmount.add(paymentEvent.calculateAmount());
        }

        return totalPayedAmount;
    }

    protected boolean canCloseEvent() {
        return (calculateTotalPayedAmount().equals(getAmountToPay()));

    }

    public PaymentEvent createPaymentEvent(EventType eventType, DateTime whenOccured,
            DebtEvent debtEvent, Account fromAccount, Account toAccount, List<EntryDTO> entryDTOs) {
        final PaymentEvent paymentEvent = new PaymentEvent(eventType, whenOccured, debtEvent,
                fromAccount, toAccount);
        paymentEvent.process(entryDTOs);

        return paymentEvent;
    }

    protected abstract BigDecimal getAmountToPay();

    public abstract List<EntryDTO> calculateEntries();

}
