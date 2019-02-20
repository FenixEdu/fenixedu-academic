package org.fenixedu.academic.domain.accounting.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class EventStats implements Serializable {

    private static final long serialVersionUID = 1L;

    Money debt = Money.ZERO;
    Money debtOverdue = Money.ZERO;
    Money payed = Money.ZERO;
    Money payedOverdue = Money.ZERO;
    Money unused = Money.ZERO;

    public EventStats(final Set<Event> events) {
        events.stream().map(e -> e.getDebtInterestCalculator(new DateTime())).forEach(c -> addStats(c));
    }

    private void addStats(final DebtInterestCalculator calculator) {
        final BigDecimal temp1 = calculator.getDueInterestAmount().add(calculator.getDueFineAmount());
        if (temp1.signum() > 0) {
            debtOverdue = debtOverdue.add(temp1).add(calculator.getDueAmount());
        } else {
            debt = debt.add(calculator.getDueAmount());
        }

        final BigDecimal temp2 = calculator.getPaidInterestAmount().add(calculator.getPaidFineAmount());
        if (temp2.signum() > 0) {
            payedOverdue = payedOverdue.add(temp2);
            payedOverdue.add(calculator.getPaidDebtAmount());
        } else {
            payed = payed.add(calculator.getPaidDebtAmount());
        }

        unused = unused.add(calculator.getPaidUnusedAmount());
        // unused = unused.add(calculator.getTotalUnusedAmount());
    }

    public Money getDebt() {
        return debt;
    }

    public void setDebt(Money debt) {
        this.debt = debt;
    }

    public Money getDebtOverdue() {
        return debtOverdue;
    }

    public void setDebtOverdue(Money debtOverdue) {
        this.debtOverdue = debtOverdue;
    }

    public Money getPayed() {
        return payed;
    }

    public void setPayed(Money payed) {
        this.payed = payed;
    }

    public Money getPayedOverdue() {
        return payedOverdue;
    }

    public void setPayedOverdue(Money payedOverdue) {
        this.payedOverdue = payedOverdue;
    }

    public Money getUnused() {
        return unused;
    }

    public void setUnused(Money unused) {
        this.unused = unused;
    }

}
