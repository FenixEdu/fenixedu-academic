package org.fenixedu.academic.domain.accounting.calculator;

import static org.fenixedu.academic.domain.accounting.calculator.BigDecimalUtil.sum;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private Map<LocalDate, BigDecimal> dueDateAmountFineMap = new HashMap<>();
    private Set<LocalDate> appliedFines = new HashSet<>();

    private CalculatorSerializer jsonSerializer;
    private boolean isToApplyInterest;

    private DebtInterestCalculator(DateTime when, List<Debt> debts, List<Payment> payments, List<DebtExemption> exemptions,
                                   List<InterestExemption> interestExemptions,
                                   Map<LocalDate, BigDecimal> dueDateAmountFineMap, boolean isToApplyInterest) {

        this.jsonSerializer = new CalculatorSerializer();
        this.debts.addAll(debts);
        this.creditEntries.addAll(payments);
        this.creditEntries.addAll(exemptions);
        this.creditEntries.addAll(interestExemptions);
        this.isToApplyInterest = isToApplyInterest;
        this.dueDateAmountFineMap.putAll(dueDateAmountFineMap);
        this.creditEntries.add(new PaymentPlaceholder(when));

        calculate();
    }

    interface PaymentPivotView extends CreditEntry.View.Detailed, PartialPayment.View.Simple, Debt.View.Simple, Fine.View.Simple, Interest.View.Simple {
    }

    interface DebtPivotView extends Debt.View.Detailed, PartialPayment.View.Detailed, Fine.View.Simple, Interest.View.Simple, CreditEntry.View.Simple {
    }

    public static class Builder {
        private List<Debt> debts = new ArrayList<>();
        private List<Payment> payments = new ArrayList<>();
        private List<DebtExemption> debtExemptions = new ArrayList<>();
        private List<InterestExemption> interestExemptions = new ArrayList<>();
        private Map<LocalDate, BigDecimal> fineTable = new HashMap<>();
        private DateTime when;
        private boolean toApplyInterest = true;

        public Builder(DateTime when) {
            this.when = when;
        }

        public Builder debt(String id, DateTime created, LocalDate dueDate, String description, BigDecimal amount) {
            return debt(id, created, dueDate, description, amount, false,false);
        }

        public Builder debt(String id, DateTime created, LocalDate dueDate, String description, BigDecimal amount, boolean exemptInterest, boolean exemptFine) {
            debts.add(new Debt(id, created, dueDate, description, amount, exemptInterest, exemptFine));
            return this;
        }

        public Builder payment(String id, DateTime created, LocalDate paymentDate, String description, BigDecimal amount) {
            payments.add(new Payment(id, created, paymentDate, description, amount));
            return this;
        }

        public Builder fine(LocalDate dueDate, BigDecimal amount) {
            fineTable.put(dueDate, amount);
            return this;
        }

        public Builder debtExemption(String id, DateTime created, LocalDate paymentDate, String description, BigDecimal amount) {
            debtExemptions.add(new DebtExemption(id, created, paymentDate, description, amount));
            return this;
        }

        public Builder interestExemption(String id, DateTime created, LocalDate paymentDate, String description, BigDecimal amount) {
            interestExemptions.add(new InterestExemption(id, created, paymentDate, description, amount));
            return this;
        }

        public Builder setToApplyInterest(boolean isToApplyInterest) {
            this.toApplyInterest = isToApplyInterest;
            return this;
        }

        public DebtInterestCalculator build() {
            return new DebtInterestCalculator(when, debts, payments, debtExemptions, interestExemptions, fineTable, toApplyInterest);
        }
    }

    public Stream<Payment> getPayments() {
        return this.creditEntries.stream().filter(c -> c.getClass().equals(Payment.class)).map(Payment.class::cast);
    }

    public Optional<Payment> getPaymentById(String id) {
        return getPayments().filter(p -> p.getId().equals(id)).findAny();
    }

    private List<CreditEntry> getCreditEntriesByCreationDate() {
        return getCreditEntryStream().collect(Collectors.toList());
    }

    public List<CreditEntry> getCreditEntries() {
        return getCreditEntryStream().filter(c -> ! (c instanceof PaymentPlaceholder)).collect(Collectors.toList());
    }

    private Stream<CreditEntry> getCreditEntryStream() {
        return creditEntries.stream().sorted(Comparator.comparing(CreditEntry::getCreated).thenComparing(CreditEntry::getId));
    }

    public List<AccountingEntry> getAccountingEntries() {
        return Stream.concat(debts.stream(),
                getCreditEntryStream()).sorted(Comparator.comparing(AccountingEntry::getCreated)
                                                         .thenComparing(AccountingEntry::getDate))
                .collect(Collectors.toList());
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

    public BigDecimal getDebtAmount() {
        return sum(getDebtStream(), Debt::getOriginalAmount);
    }

    private Stream<Debt> getDebtStream() {
        return debts.stream();
    }

    public BigDecimal getInterestAmount() {
        return sum(getInterestStream(), DebtEntry::getOriginalAmount);
        //return getInterestStream().map(Interest::getOriginalAmount).reduce(BigDecimal.ZERO, BigDecimalUtil::reducer);
    }

    public BigDecimal getFineAmount() {
        return sum(getFineStream(), Fine::getOriginalAmount);
    }

    public BigDecimal getDueAmount() {
        return sum(getDebtStream(), Debt::getOpenAmount);
    }

    public BigDecimal getDueInterestAmount() {
        return sum(getDebtStream(), Debt::getOpenInterestAmount);
    }

    public BigDecimal getDueFineAmount() {
        return sum(getDebtStream(), Debt::getOpenFineAmount);
    }

    public BigDecimal getTotalDueAmount() {
        return sum(Stream.of(getDueAmount(), getDueInterestAmount(), getDueFineAmount()));
    }

    public BigDecimal getPaidDebtAmount() {
        return sum(getDebtStream(),Debt::getPaymentsAmount);
    }

    public BigDecimal getPaidInterestAmount() {
        return sum(getInterestStream(),Interest::getPaymentsAmount);
    }

    public BigDecimal getPaidFineAmount() {
        return sum(getFineStream(), Fine::getPaymentsAmount);
    }

    public BigDecimal getTotalPaidAmount() {
        return sum(Stream.of(getPaidDebtAmount(), getPaidInterestAmount(), getPaidFineAmount()));
    }

    public BigDecimal getDebtExemptionAmount() {
        return sum(getDebtStream(),Debt::getDebtExemptionAmount);
    }

    public BigDecimal getInterestExemptionAmount() {
        return sum(getDebtStream().flatMap(d -> d.getInterests().stream()), DebtEntry::getInterestExemptionAmount);
    }

    public BigDecimal getFineExemptionAmount() {
        return sum(getDebtStream().flatMap(d -> d.getFines().stream()), DebtEntry::getFineExemptionAmount);
    }

    public BigDecimal getTotalAmount() {
        return sum(Stream.of(getDebtAmount(), getInterestAmount(), getFineAmount()));
    }

    private Stream<Interest> getInterestStream() {
        return getDebtStream().flatMap(d -> d.getInterests().stream());
    }

    private Stream<Fine> getFineStream() {
        return getDebtStream().flatMap(d -> d.getFines().stream());
    }

    private List<Debt> getDebtsOrderedByOpenInterest() {
        return getDebtStream().filter(Debt::isOpenInterest).sorted(Comparator.comparing(Debt::getDueDate)).collect(Collectors
                .toList());
    }

    private List<Debt> getDebtsOrderedByOpenFine() {
        return getDebtStream().sorted(Comparator.comparing(Debt::getOpenFineAmount)).collect(Collectors.toList());
    }

    public List<Debt> getDebtsOrderedByDueDate() {
        return getDebtStream().sorted(Comparator.comparing(Debt::getDueDate)).collect(Collectors.toList());
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
        for (CreditEntry creditEntry : getCreditEntriesByCreationDate()) {
            if (isToApplyInterest) {
                if (creditEntry.isToApplyInterest()) {
                    for (final Debt debt : getDebtsOrderedByDueDate()) {
                        if (debt.isOpen() && !debt.isToExemptInterest()) {
                            Optional<InterestRateBean> interestRateBean = calculateInterest(debt.getDueDateForInterest(), creditEntry.getDate(), debt.getOpenAmount());
                            interestRateBean.map(bean -> new Interest(creditEntry.getDate(), bean.getInterest(), debt,
                                    creditEntry, bean)).ifPresent(debt::addInterest);
                        }
                    }
                }

                for (Debt debt : getDebtsOrderedByOpenInterest()) {
                    debt.depositInterest(creditEntry);
                }
            }

            if (!dueDateAmountFineMap.isEmpty()) {
                if (creditEntry.isToApplyFine()) {
                    for (final Debt debt : getDebtsOrderedByDueDate()) {
                        if (debt.isOpen() && !debt.isToExemptFine()) {
                            dueDateAmountFineMap.forEach((dueDate, amount) -> {
                                if (!appliedFines.contains(dueDate) && creditEntry.getDate().isAfter(dueDate)) {
                                    debt.addFine(new Fine(creditEntry.getDate(), amount, creditEntry));
                                    appliedFines.add(dueDate);
                                }
                            });
                        }
                    }
                }
            }

            for (Debt debt : getDebtsOrderedByDueDate()) {
                debt.deposit(creditEntry);
            }

            for (Debt debt : getDebtsOrderedByOpenFine()) {
                debt.depositFine(creditEntry);
            }
        }
    }

    public String exportToString() {
        StringBuilder builder = new StringBuilder();

        getDebtStream().forEach(debt -> {
            builder.append(String.format("%s %s %s %s %s%n", debt.getClass().getSimpleName(),
                                         debt.getDueDate().toString("dd/MM/yyyy"),
                debt.getOriginalAmount(), debt.isToExemptInterest(), debt.isToExemptFine()));
        });

        getCreditEntriesByCreationDate().forEach(creditEntry -> {
            builder.append(String.format("%s %s %s %s%n", creditEntry.getClass().getSimpleName(),
                                         creditEntry.getCreated().toString(),
                                         creditEntry.getDate().toString("dd/MM/yyyy"),
                                         creditEntry.getAmount().toPlainString()
                           )
            );
        });
        return builder.toString();
    }

    public Spreadsheet exportToXLS(String spreadsheetName) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet(spreadsheetName);
        Row row = spreadsheet.addRow();

        Map<LocalDate, BigDecimal> debts = getDebtStream().collect(Collectors.toMap(Debt::getDueDate, Debt::getOriginalAmount));
        Map<LocalDate, BigDecimal> payments = creditEntries.stream().collect(Collectors.toMap(CreditEntry::getDate, CreditEntry::getAmount));

        row.setCell("debtAmount", getDebtAmount().toPlainString());
        row.setCell("dueDateDebtAmountMap", toString(debts));
        row.setCell("datePaymentMap", toString(payments));
        row.setCell("interestAmount", getInterestAmount().toPlainString());
        row.setCell("interestDescription", getInterestBean().map(InterestRateBean::toString).orElse("-"));
        row.setCell("paidDebtAmount", getPaidDebtAmount().toPlainString());
        row.setCell("debtExemptionAmount", getDebtExemptionAmount().toPlainString());
        row.setCell("paidInterestAmount", getPaidInterestAmount().toPlainString());
        row.setCell("interestExemptionAmount", getInterestExemptionAmount().toPlainString());
        row.setCell("paidFineAmount", getPaidFineAmount().toPlainString());
        row.setCell("fineExemptionAmount", getFineExemptionAmount().toPlainString());
        row.setCell("dueDebtAmount", getDueAmount().toPlainString());
        row.setCell("dueInterestAmount", getDueInterestAmount().toPlainString());

        return spreadsheet;
    }

}
