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
    private final boolean exemptInterest;

    @JsonView(Simple.class)
    private final LocalDate dueDate;

    @JsonView(Detailed.class)
    private final Set<Interest> interests = new HashSet<>();

    @JsonView(Detailed.class)
    private final Set<Fine> fines = new HashSet<>();

    public Debt(LocalDate dueDate, BigDecimal amount, boolean exemptInterest) {
        super(amount);
        this.dueDate = dueDate;
        this.exemptInterest = exemptInterest;
    }

    @Override
    public boolean isOpen() {
        return super.isOpen() || isOpenInterest();
    }

    public boolean isOpenInterest() {
        return interests.stream().anyMatch(Interest::isOpen);
    }

    public boolean isOpenFine() {
        return fines.stream().anyMatch(Fine::isOpen);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
    }

    public void addFine(Fine fine) {
        this.fines.add(fine);
    }

    public Set<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    public Set<Fine> getFines() { return Collections.unmodifiableSet(fines); }

    public BigDecimal getOpenInterestAmount() {
        return interests.stream().map(Interest::getOpenAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getOpenFineAmount() { return fines.stream().map(Fine::getOpenAmount).reduce(BigDecimal.ZERO, BigDecimal::add); }

    public void depositInterest(CreditEntry creditEntry) {
        if (creditEntry.isForInterest()) {
            getInterests().stream().sorted(Comparator.comparing(Interest::getDate)).forEach(interest -> interest.deposit(creditEntry));
        }
    }

    public void depositFine(CreditEntry creditEntry) {
        if (creditEntry.isForFine()) {
            getFines().stream().sorted(Comparator.comparing(Fine::getDate)).forEach(fine -> fine.deposit(creditEntry));
        }
    }

    public LocalDate getDueDateForInterest() {
        return getInterests().stream().map(Interest::getDate).max(Comparator.naturalOrder()).orElse(getDueDate());
    }

    @Override
    boolean isToDeposit(CreditEntry creditEntry) {
        return creditEntry.isForDebt();
    }

    @Override
    public String toString() {
        return "Debt{" + "dueDate=" + dueDate + ", interests=" + interests + ", open=" + isOpen() + ", openInterest=" + isOpenInterest() + ", openInterestAmount=" + getOpenInterestAmount() + "} "
                   + super.toString();
    }

    public boolean isToExemptInterest() {
        return exemptInterest;
    }

}
