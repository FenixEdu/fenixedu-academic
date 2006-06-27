package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentEntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class PaymentEvent extends PaymentEvent_Base {

    private transient Account fromAccount;

    private transient Account toAccount;

    private PaymentEvent() {
        super();
    }

    PaymentEvent(EventType eventType, DateTime whenOccured, DebtEvent debtEvent, Account fromAccount,
            Account toAccount) {
        this();
        init(eventType, whenOccured, debtEvent, fromAccount, toAccount);
    }

    private void init(EventType eventType, DateTime whenOccured, DebtEvent debtEvent,
            Account fromAccount, Account toAccount) {
        init(eventType, whenOccured);
        checkParameters(debtEvent, fromAccount, toAccount);
        super.setDebtEvent(debtEvent);
        setFromAccount(fromAccount);
        setToAccount(toAccount);
    }

    private void checkParameters(DebtEvent debtEvent, Account fromAccount, Account toAccount) {
        if (debtEvent == null) {
            throw new DomainException("error.accounting.paymentEvent.debtEvent.cannot.be.null");
        }

        if (fromAccount == null) {
            throw new DomainException("error.accounting.paymentEvent.fromAccount.cannot.be.null");
        }

        if (toAccount == null) {
            throw new DomainException("error.accounting.paymentEvent.toAccount.cannot.be.null");
        }
    }

    private Account getFromAccount() {
        return fromAccount;
    }

    private void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    private Account getToAccount() {
        return toAccount;
    }

    private void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {
        if (getDebtEvent().isClosed()) {
            throw new DomainException("error.accounting.paymentEvent.debtEvent.is.closed");
        }

        final BigDecimal debtAmountToPay = getDebtEvent().getAmountToPay();
        BigDecimal currentPayedAmountToDebt = getDebtEvent().calculateTotalPayedAmount();
        for (final EntryDTO entry : entryDTOs) {
            PaymentEntryDTO paymentEntryDTO = (PaymentEntryDTO) entry;
            checkIfCanAddAmount(debtAmountToPay, currentPayedAmountToDebt, paymentEntryDTO);
            makeAccountingTransaction(getFromAccount(), getToAccount(), paymentEntryDTO.getEntryType(),
                    paymentEntryDTO.getAmountToPay());
            currentPayedAmountToDebt = currentPayedAmountToDebt.add(paymentEntryDTO.getAmountToPay());
        }

        closeEvent();
        getDebtEvent().closeEvent();

    }

    private void checkIfCanAddAmount(final BigDecimal debtAmountToPay,
            BigDecimal currentPayedAmountToDebt, final PaymentEntryDTO entry) {
        if (currentPayedAmountToDebt.add(entry.getAmountToPay()).compareTo(debtAmountToPay) > 1) {
            throw new DomainException(
                    "error.accounting.paymentEvent.amount.being.payed.exceeds.debt.total");
        }
    }

    @Override
    public void setDebtEvent(DebtEvent debtEvent) {
        throw new DomainException("error.accounting.paymentEvent.cannot.modify.debtEvent");
    }

    public BigDecimal calculateAmount() {
        BigDecimal totalAmount = new BigDecimal("0");

        for (final Entry entry : getEntriesSet()) {
            totalAmount = totalAmount.add(entry.getAmount());
        }

        for (final ReimbursementEvent reimbursementEvent : getReimbursementEvents()) {
            totalAmount = totalAmount.add(reimbursementEvent.calculateAmount());
        }

        return totalAmount;

    }

    public ReimbursementEvent createReimbursementEvent(EventType eventType, DateTime whenOccured,
            PaymentEvent paymentEvent, Employee employee, Account fromAccount, Account toAccount,
            List<EntryDTO> entryDTOs) {

        final ReimbursementEvent reimbursementEvent = new ReimbursementEvent(eventType, whenOccured,
                paymentEvent, employee, fromAccount, toAccount);

        reimbursementEvent.process(entryDTOs);

        return reimbursementEvent;

    }

}
