package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.calculator.Debt.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.Debt.View.Simple;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@JsonPropertyOrder({"type", "dueDate", "amount", "interests", "partialPayments"})
public class Debt extends DebtEntry {

    interface View {
        interface Simple extends DebtEntry.View.Simple {

        }

        interface Detailed extends Simple, DebtEntry.View.Detailed {

        }
    }

    @JsonView(Simple.class)
    private final LocalDate dueDate;

    @JsonView(Detailed.class)
    private final Set<Interest> interests = new HashSet<>();

    public Debt(LocalDate dueDate, BigDecimal amount) {
        super(amount);
        this.dueDate = dueDate;
    }

    @Override
    public boolean isOpen() {
        return super.isOpen() || isOpenInterest();
    }

    public boolean isOpenInterest() {
        return interests.stream().anyMatch(Interest::isOpen);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
    }

    public Set<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    public BigDecimal getOpenInterestAmount() {
        return interests.stream().map(Interest::getOpenAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void depositInterest(CreditEntry creditEntry) {
        if (creditEntry.isForInterest()) {
            getInterests().stream().sorted(Comparator.comparing(Interest::getDate)).forEach(interest -> interest.deposit(creditEntry));
        }
    }

    public LocalDate getDueDateForInterest() {
        return getInterests().stream().map(Interest::getDate).max(Comparator.naturalOrder()).orElse(getDueDate());
    }

    public

    @Override
    boolean isToDeposit(CreditEntry creditEntry) {
        return creditEntry.isForDebt();
    }

    @Override
    public String toString() {
        return "Debt{" + "dueDate=" + dueDate + ", interests=" + interests + ", open=" + isOpen() + ", openInterest=" + isOpenInterest() + ", openInterestAmount=" + getOpenInterestAmount() + "} "
                   + super.toString();
    }
}
