package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.ReimbursementEntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class ReimbursementEvent extends ReimbursementEvent_Base {

    private transient Account fromAccount;

    private transient Account toAccount;

    private ReimbursementEvent() {
        super();
    }

    ReimbursementEvent(EventType eventType, DateTime whenOccured, PaymentEvent paymentEvent,
            Employee employee, Account fromAccount, Account toAccount) {
        this();
        init(eventType, whenOccured, paymentEvent, employee, fromAccount, toAccount);
    }

    private void init(EventType eventType, DateTime whenOccured, PaymentEvent paymentEvent,
            Employee employee, Account fromAccount, Account toAccount) {
        checkParameters(paymentEvent, employee, fromAccount, toAccount);
        init(eventType, whenOccured);
        super.setPaymentEvent(paymentEvent);
        super.setEmployee(employee);
        setFromAccount(fromAccount);
        setToAccount(toAccount);
    }

    private void checkParameters(PaymentEvent paymentEvent, Employee employee, Account fromAccount,
            Account toAccount) {
        if (paymentEvent == null) {
            throw new DomainException("error.accounting.reimbursementEvent.paymentEvent.cannot.be.null");
        }

        if (employee == null) {
            throw new DomainException("error.accounting.reimbursementEvent.employee.cannot.be.null");
        }

        if (fromAccount == null) {
            throw new DomainException("error.accounting.reimbursementEvent.fromAccount.cannot.be.null");
        }

        if (toAccount == null) {
            throw new DomainException("error.accounting.reimbursementEvent.toAccount.cannot.be.null");
        }
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {

        final BigDecimal debtAmountToPay = getDebtEvent().getAmountToPay();
        final BigDecimal reimbursementAmountLimit = debtAmountToPay.subtract(getDebtEvent()
                .calculateTotalPayedAmount());

        checkIfReimbursementLimitExceeds(reimbursementAmountLimit, new BigDecimal("0"));

        BigDecimal reimbursedAmount = new BigDecimal("0");
        for (final EntryDTO entry : entryDTOs) {
            final ReimbursementEntryDTO reimbursementEntry = (ReimbursementEntryDTO) entry;
            checkIfReimbursementLimitExceeds(reimbursementAmountLimit, reimbursementEntry
                    .getAmountToReimburse());
            makeAccountingTransaction(getFromAccount(), getToAccount(), reimbursementEntry
                    .getEntryType(), reimbursementEntry.getAmountToReimburse());
            reimbursedAmount = reimbursedAmount.add(reimbursementEntry.getAmountToReimburse());
        }

        closeEvent();

    }

    private void checkIfReimbursementLimitExceeds(final BigDecimal reimbursementAmountLimit,
            BigDecimal amount) {
        if (reimbursementAmountLimit.equals(amount)) {
            throw new DomainException("error.accounting.reimbursementEvent.amount.exceeds.limit");
        }
    }

    private DebtEvent getDebtEvent() {
        return getPaymentEvent().getDebtEvent();
    }

    public BigDecimal calculateAmount() {
        BigDecimal totalAmount = new BigDecimal("0");
        for (final Entry entry : getEntriesSet()) {
            totalAmount = totalAmount.add(entry.getAmount());
        }

        return totalAmount;
    }

    @Override
    public void setPaymentEvent(PaymentEvent paymentEvent) {
        throw new DomainException("error.accounting.reimbursementEvent.cannot.modify.paymentEvent");
    }

    @Override
    public void setEmployee(Employee employee) {
        throw new DomainException("error.accounting.reimbursementEvent.cannot.modify.employee");
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

}
