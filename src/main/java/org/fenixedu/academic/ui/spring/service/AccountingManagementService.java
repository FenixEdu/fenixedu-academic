package org.fenixedu.academic.ui.spring.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.calculator.CreditEntry;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtEntry;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.calculator.Fine;
import org.fenixedu.academic.domain.accounting.calculator.Interest;
import org.fenixedu.academic.domain.accounting.calculator.PartialPayment;
import org.fenixedu.academic.domain.accounting.events.EventExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountFineExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountInterestExemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.CreateExemptionBean;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import pt.ist.fenixframework.Atomic;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@Service
public class AccountingManagementService {

    public static class PaymentSummary {
        final String id;
        final String description;
        final LocalizedString typeDescription;
        final DateTime created;
        final LocalDate date;
        final BigDecimal amount;
        final BigDecimal amountUsedInDebt;
        final BigDecimal amountUsedInInterest;
        final BigDecimal amountUsedInFine;

        public PaymentSummary(String id, String description, LocalizedString typeDescription, DateTime created, LocalDate date, BigDecimal amount, BigDecimal amountUsedInDebt, BigDecimal amountUsedInInterest,
                BigDecimal amountUsedInFine) {
            this.id = id;
            this.description = description;
            this.typeDescription = typeDescription;
            this.created = created;
            this.date = date;
            this.amount = amount;
            this.amountUsedInDebt = amountUsedInDebt;
            this.amountUsedInInterest = amountUsedInInterest;
            this.amountUsedInFine = amountUsedInFine;
        }

        public String getId() {
            return id;
        }

        public DateTime getCreated() {
            return created;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public LocalizedString getTypeDescription() { return typeDescription; }

        public BigDecimal getAmountUsedInDebt() {
            return amountUsedInDebt;
        }

        public BigDecimal getAmountUsedInInterest() {
            return amountUsedInInterest;
        }

        public BigDecimal getAmountUsedInFine() {
            return amountUsedInFine;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        @Override public String toString() {
            return "PaymentSummary{" + "id='" + id + '\'' + ", description='" + description + '\'' + ", created=" + created
                    + ", date=" + date + ", amount=" + amount + ", amountUsedInDebt=" + amountUsedInDebt
                    + ", amountUsedInInterest=" + amountUsedInInterest + ", amountUsedInFine=" + amountUsedInFine + '}';
        }
    }

    public static class PaymentSummaryWithDebt extends PaymentSummary {
        private final DebtEntry debtEntry;

        public PaymentSummaryWithDebt(String id, String description, LocalizedString typeDescription, DateTime created, LocalDate date, BigDecimal amount, BigDecimal amountUsedInDebt, BigDecimal amountUsedInInterest, BigDecimal amountUsedInFine, DebtEntry debtEntry) {
            super(id, description, typeDescription, created, date , amount, amountUsedInDebt, amountUsedInInterest, amountUsedInFine);
            this.debtEntry = debtEntry;
        }

        public DebtEntry getDebtEntry() {
            return debtEntry;
        }
    }

    public List<PaymentSummary> createPaymentSummaries(Debt debt) {
        final List<PaymentSummary> paymentSummaries = new ArrayList<>();
        final Multimap<CreditEntry, PartialPayment> creditEntryPartialPaymentMultimap = HashMultimap.create();

        Stream.concat(debt.getPartialPayments().stream(), Stream.concat(debt.getInterests().stream().flatMap(i -> i.getPartialPayments().stream()), debt.getFines().stream().flatMap(i -> i.getPartialPayments().stream())))
                .distinct()
                .forEach(pp -> creditEntryPartialPaymentMultimap.put(pp.getCreditEntry(), pp));

        for (CreditEntry creditEntry : creditEntryPartialPaymentMultimap.keySet()) {
            final BigDecimal amountUsedInDebts = creditEntryPartialPaymentMultimap.get(creditEntry).stream().filter(c -> c
                    .getDebtEntry() == debt).map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            final BigDecimal amountUsedInInterest = creditEntryPartialPaymentMultimap.get(creditEntry).stream().filter(c -> c
                    .getDebtEntry() instanceof Interest).map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            final BigDecimal amountUsedInFine = creditEntryPartialPaymentMultimap.get(creditEntry).stream().filter(c -> c
                    .getDebtEntry() instanceof Fine).map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            PaymentSummary paymentSummary = new PaymentSummary(creditEntry.getId(), creditEntry.getDescription(), creditEntry.getTypeDescription(),
                    creditEntry
                    .getCreated(), creditEntry.getDate(), creditEntry.getAmount(), amountUsedInDebts, amountUsedInInterest, amountUsedInFine);

            paymentSummaries.add(paymentSummary);
        }

        return paymentSummaries;
    }

    public Set<PaymentSummaryWithDebt> createPaymentSummaries(CreditEntry creditEntry) {
        final Set<PaymentSummaryWithDebt> paymentSummaryWithDebtSet = new TreeSet<>(
                Comparator.comparing(a -> a.getDebtEntry().getDate()));

        final Multimap<DebtEntry, PartialPayment> debtEntryPartialPaymentMultimap = HashMultimap.create();

        for (PartialPayment partialPayment : creditEntry.getPartialPayments()) {
            DebtEntry debtEntry = partialPayment.getDebtEntry();
            if (debtEntry instanceof Interest) {
                debtEntryPartialPaymentMultimap.put(((Interest) debtEntry).getTarget(), partialPayment);
            }
            else if (debtEntry instanceof Fine){
                debtEntryPartialPaymentMultimap.put(((Fine) debtEntry).getTarget(), partialPayment);
            }
            else {
                debtEntryPartialPaymentMultimap.put(debtEntry, partialPayment);
            }
        }

        for (DebtEntry debtEntry : debtEntryPartialPaymentMultimap.keySet()) {
            BigDecimal amountUsedInDebt = BigDecimal.ZERO;
            BigDecimal amountUsedInInterest = BigDecimal.ZERO;
            BigDecimal amountUsedInFine = BigDecimal.ZERO;
            Collection<PartialPayment> partialPayments = debtEntryPartialPaymentMultimap.get(debtEntry);
            for (PartialPayment partialPayment : partialPayments) {
                if (partialPayment.getDebtEntry() instanceof Interest) {
                    amountUsedInInterest = amountUsedInInterest.add(partialPayment.getAmount());
                }
                else if (partialPayment.getDebtEntry() instanceof Fine) {
                    amountUsedInFine = amountUsedInFine.add(partialPayment.getAmount());
                }
                else {
                    amountUsedInDebt = amountUsedInDebt.add(partialPayment.getAmount());
                }
            }
            paymentSummaryWithDebtSet.add(new PaymentSummaryWithDebt(debtEntry.getId(), creditEntry.getDescription(), creditEntry.getTypeDescription(), creditEntry
                    .getCreated(), creditEntry.getDate(), amountUsedInDebt.add(amountUsedInInterest.add(amountUsedInFine)), amountUsedInDebt, amountUsedInInterest, amountUsedInFine,
                    debtEntry));
        }

        return paymentSummaryWithDebtSet;
    }

    @Atomic
    public AccountingTransaction depositAmount(final Event event, final User depositor, final DepositAmountBean depositAmountBean) {
        return event.depositAmount(depositor, depositAmountBean.getAmount(), depositAmountBean.getEntryType(),
                new AccountingTransactionDetailDTO(depositAmountBean.getWhenRegistered(), depositAmountBean.getPaymentMethod(),
                        depositAmountBean.getPaymentReference(), depositAmountBean.getReason()));
    }

    @Atomic
    public void cancelEvent(final Event event, final Person responsible, final String justification) {
        event.cancel(responsible, justification);
    }

    @Atomic
    public Exemption exemptEvent(final Event event, final Person responsible, final CreateExemptionBean createExemptionBean) {
        switch (createExemptionBean.getExemptionType()) {
            case DEBT:
                final DebtInterestCalculator calculator = event.getDebtInterestCalculator(new DateTime());
                if (calculator.hasDueInterestAmount() || calculator.hasDueFineAmount()) {
                    throw new DomainException("error.EventExemption.event.has.positive.due.interests.or.fines");
                }
                return new EventExemption(event, responsible, createExemptionBean.getValue(), createExemptionBean.getJustificationType(), createExemptionBean.getDispatchDate(), createExemptionBean.getReason());
            case INTEREST:
                return new FixedAmountInterestExemption(event, responsible, createExemptionBean.getValue(), createExemptionBean.getJustificationType(), createExemptionBean.getDispatchDate(), createExemptionBean.getReason());
            case FINE:
                return new FixedAmountFineExemption(event, responsible, createExemptionBean.getValue(), createExemptionBean.getJustificationType(), createExemptionBean.getDispatchDate(), createExemptionBean.getReason());
        }
        throw new UnsupportedOperationException("Attempted to create an unknown exemption type");

    }

    @Atomic
    public static Void refundEvent(final Event event, final Person person) {
        // TODO Auto-generated method stub
        return null;
    }

    @Atomic
    public static Void refundExcessPayment(final Event event, final Person person) {
        // TODO Auto-generated method stub
        return null;
    }

}
