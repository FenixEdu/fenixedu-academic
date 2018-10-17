package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.calculator.Debt.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.Debt.View.Simple;
import org.joda.time.DateTime;
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
    private final boolean exemptFine;
    
    @JsonView(Simple.class)
    private final boolean exemptInterest;

    @JsonView(Detailed.class)
    private final Set<Interest> interests = new HashSet<>();

    @JsonView(Detailed.class)
    private final Set<Fine> fines = new HashSet<>();

    public Debt(String id, DateTime created, LocalDate dueDate, String description, BigDecimal amount, boolean exemptInterest,
            boolean exemptFine) {
        super(id, created, dueDate, description, amount);
        this.exemptInterest = exemptInterest;
        this.exemptFine = exemptFine;
    }

    @Override
    public boolean isOpen() {
        return super.isOpen() || isOpenInterest() || isOpenFine();
    }

    public BigDecimal getPaidInterestAmount() {
        return BigDecimalUtil.sum(interests.stream(), Interest::getPaidAmount);
    }

    public BigDecimal getPaidFineAmount() {
        return BigDecimalUtil.sum(fines.stream(), Fine::getPaidAmount);
    }

    public boolean isOpenInterest() {
        return interests.stream().anyMatch(Interest::isOpen);
    }

    public BigDecimal getTotalOpenAmount() {
        return getOpenAmount().add(getOpenInterestAmount()).add(getOpenFineAmount());
    }

    public BigDecimal getTotalOpenPenaltyAmount() {
        return getOpenInterestAmount().add(getOpenFineAmount());
    }

    public boolean isOpenFine() {
        return fines.stream().anyMatch(Fine::isOpen);
    }

    public LocalDate getDueDate() {
        return getDate();
    }

    public boolean isAfterDueDate(LocalDate localDate) {
        return localDate.isAfter(getDate());
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
        return interests.stream().map(Interest::getOpenAmount).map(amount -> amount.setScale(2, RoundingMode.HALF_EVEN)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getOpenFineAmount() { return fines.stream().map(Fine::getOpenAmount).map(amount -> amount.setScale(2, RoundingMode.HALF_EVEN)).reduce(BigDecimal.ZERO, BigDecimal::add); }

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
        return "Debt{" + "dueDate=" + getDueDate() + ", interests=" + interests + ", open=" + isOpen() + ", openInterest=" +
                isOpenInterest() + ", openInterestAmount=" + getOpenInterestAmount() + "} "
                   + super.toString();
    }

    public boolean isToExemptInterest() {
        return exemptInterest;
    }

    public boolean isToExemptFine() { return exemptFine; }

    public int getProgress() {
        return getPaidAmount().divide(getOriginalAmount(), 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).intValue();
    }

}
