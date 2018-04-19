package org.fenixedu.academic.domain.accounting.calculator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.accounting.InterestRate;
import org.fenixedu.academic.domain.accounting.InterestRate.InterestRateBean;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class DebtInterestCalculator {

    private List<Debt> debts = new ArrayList<>();
    private List<CreditEntry> creditEntries = new ArrayList<>();
    private CalculatorSerializer jsonSerializer;
    private boolean isToApplyInterest;
    

    private DebtInterestCalculator(DateTime when, List<Debt> debts, List<Payment> payments, List<DebtExemption> exemptions, List<InterestExemption> interestExemptions, boolean isToApplyInterest) {
        this.jsonSerializer = new CalculatorSerializer();
        this.debts.addAll(debts);
        this.creditEntries.addAll(payments);
        this.creditEntries.addAll(exemptions);
        this.creditEntries.addAll(interestExemptions);
        this.isToApplyInterest = isToApplyInterest;

        if (!hasPayments()) {
            creditEntries.add(new Payment(when.toLocalDate(), BigDecimal.ZERO));
        }
        calculate();
    }

    interface PaymentPivotView extends CreditEntry.View.Detailed, PartialPayment.View.Simple, Debt.View.Simple, Interest.View.Simple {}

    interface DebtPivotView extends Debt.View.Detailed , PartialPayment.View.Detailed, Interest.View.Simple, CreditEntry.View.Simple {}

    public static class Builder {
        private List<Debt> debts = new ArrayList<>();
        private List<Payment> payments = new ArrayList<>();
        private List<DebtExemption> debtExemptions = new ArrayList<>();
        private List<InterestExemption> interestExemptions = new ArrayList<>();
        private DateTime when;
        private boolean toApplyInterest = true;
        
        public Builder(DateTime when) {
            this.when = when;
        }


        public Builder debt(LocalDate dueDate, BigDecimal amount) {
            debts.add(new Debt(dueDate, amount));
            return this;
        }

        public Builder payment(DateTime created, LocalDate paymentDate, BigDecimal amount) {
            payments.add(new Payment(created, paymentDate, amount));
            return this;
        }

        public Builder debtExemption(DateTime created, LocalDate paymentDate, BigDecimal amount) {
            debtExemptions.add(new DebtExemption(created, paymentDate, amount));
            return this;
        }

        public Builder interestExemption(DateTime created, LocalDate paymentDate, BigDecimal amount) {
            interestExemptions.add(new InterestExemption(created, paymentDate, amount));
            return this;
        }

        public Builder setToApplyInterest(boolean isToApplyInterest) {
            this.toApplyInterest = isToApplyInterest;
            return this;
        }

        public DebtInterestCalculator build() {
            return new DebtInterestCalculator(when, debts, payments, debtExemptions, interestExemptions, toApplyInterest);
        }
    }

    private boolean hasPayments() {
        return this.creditEntries.stream().anyMatch(Payment.class::isInstance);
    }

    private List<CreditEntry> getPaymentsByCreationDate() {
        return creditEntries.stream().sorted(Comparator.comparing(CreditEntry::getCreated)).collect(Collectors.toList());
    }

    private List<Debt> getDebtsByDueDate() {
        return debts.stream().sorted(Comparator.comparing(Debt::getDueDate)).collect(Collectors.toList());
    }

    public Optional<InterestRateBean> getInterestBean() {
        if (getInterestStream().count() == 0) {
            return Optional.empty();
        }
        Optional<LocalDate> startDate = getInterestStream().map(Interest::getInterestRateBean).map(i -> i.getInterval().getStart().toLocalDate()).min(LocalDate::compareTo);
        Optional<LocalDate> endDate = getInterestStream().map(Interest::getInterestRateBean).map(i -> i.getInterval().getEnd().toLocalDate()).max(LocalDate::compareTo);
        BigDecimal totalAmount = getInterestStream().map(Interest::getInterestRateBean).map(InterestRateBean::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        InterestRateBean interestRateBean = new InterestRateBean(startDate.get().minusDays(1), endDate.get().minusDays(1), totalAmount);
        getInterestStream().map(Interest::getInterestRateBean).flatMap(i -> i.getPartials().stream()).forEach(interestRateBean::addPartial);
        return Optional.of(interestRateBean);
    }

    public BigDecimal getDebtAmount() { return debts.stream().map(Debt::getOriginalAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP); }

    public BigDecimal getInterestAmount () { return getInterestStream().map(Interest::getOriginalAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP); }
    public BigDecimal getDueAmount() { return debts.stream().map(Debt::getOpenAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getDueInterestAmount() { return debts.stream().map(Debt::getOpenInterestAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getPayedDebtAmount() { return debts.stream().map(Debt::getPaymentsAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getPayedInterestAmount() { return getInterestStream().map(Interest::getPaymentsAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getDebtExemptionAmount() { return debts.stream().map(Debt::getDebtExemptionAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getInterestExemptionAmount() { return debts.stream().map(Debt::getInterestExemptionAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);}

    private Stream<Interest> getInterestStream() {
        return debts.stream().flatMap(d -> d.getInterests().stream());
    }

    private List<Debt> getDebtsOrderedByOpenInterest() {
        return debts.stream().sorted(Comparator.comparing(Debt::getOpenInterestAmount)).collect(Collectors.toList());
    }

    public List<Debt> getDebtsOrderedByDueDate() {
        return debts.stream().sorted(Comparator.comparing(Debt::getDueDate)).collect(Collectors.toList());
    }

    private Optional<InterestRateBean> calculateInterest(LocalDate start, LocalDate end, BigDecimal baseAmount) {
        return InterestRate.getInterestBean(start, end, baseAmount);
    }

    private static String toString(Map<LocalDate, BigDecimal> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> String.format("%s -> %s", e.getKey(), e.getValue().toPlainString())).collect(Collectors.joining("\n"));
    }

    public String jsonDebts() {
        try {
            return jsonSerializer.getMapper().writerWithView(DebtPivotView.class).writeValueAsString(this.debts);
        } catch (JsonProcessingException e) {
            throw new Error(e);
        }
    }

    public String jsonCredits() {
        try {
            return jsonSerializer.getMapper().writerWithView(PaymentPivotView.class).writeValueAsString(this.creditEntries);
        } catch (JsonProcessingException e) {
            throw new Error(e);
        }
    }


    private void calculate() {

        for (CreditEntry creditEntry : getPaymentsByCreationDate()) {
            if (creditEntry.isToApplyInterest()) {
                for (final Debt debt : getDebtsOrderedByDueDate()) {
                    if (debt.isOpen() && isToApplyInterest) {
                        Optional<InterestRateBean> interestRateBean = calculateInterest(debt.getDueDateForInterest(), creditEntry.getDate(), debt.getOpenAmount());
                        interestRateBean.map(bean -> new Interest(creditEntry.getDate(), bean.getInterest(), creditEntry, bean)).ifPresent(debt::addInterest);
                    }
                }
            }
            
            if (isToApplyInterest) {
                for (Debt debt : getDebtsOrderedByOpenInterest()) {
                    debt.depositInterest(creditEntry);
                }
            }

            for (Debt debt : getDebtsOrderedByDueDate()) {
                debt.deposit(creditEntry);
            }
        }
    }

    public Spreadsheet exportToXLS(String spreadsheetName) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet(spreadsheetName);
        Row row = spreadsheet.addRow();

        Map<LocalDate, BigDecimal> debts = this.debts.stream().collect(Collectors.toMap(Debt::getDueDate, Debt::getOriginalAmount));
        Map<LocalDate, BigDecimal> payments = creditEntries.stream().collect(Collectors.toMap(CreditEntry::getDate, CreditEntry::getAmount));

        row.setCell("debtAmount", getDebtAmount().toPlainString());
        row.setCell("dueDateDebtAmountMap", toString(debts));
        row.setCell("datePaymentMap", toString(payments));
        row.setCell("interestAmount", getInterestAmount().toPlainString());
        row.setCell("interestDescription", getInterestBean().map(InterestRateBean::toString).orElse("-"));
        row.setCell("payedDebtAmount", getPayedDebtAmount().toPlainString());
        row.setCell("debtExemptionAmount", getDebtExemptionAmount().toPlainString());
        row.setCell("payedInterestAmount", getPayedInterestAmount().toPlainString());
        row.setCell("interestExemptionAmount", getInterestExemptionAmount().toPlainString());
        row.setCell("dueDebtAmount", getDueAmount().toPlainString());
        row.setCell("dueInterestAmount", getDueInterestAmount().toPlainString());

        return spreadsheet;
    }

}
