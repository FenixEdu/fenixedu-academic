package org.fenixedu.academic.ui.spring.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.accounting.Refund;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.accounting.calculator.CreditEntry;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtEntry;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.calculator.ExcessRefund;
import org.fenixedu.academic.domain.accounting.calculator.Fine;
import org.fenixedu.academic.domain.accounting.calculator.Interest;
import org.fenixedu.academic.domain.accounting.calculator.PartialPayment;
import org.fenixedu.academic.domain.accounting.events.EventExemption;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountFineExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountInterestExemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.CreateExemptionBean;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@Service
public class AccountingManagementService {

    public static class AccountingEntrySummary {
        final String id;
        final String description;
        final LocalizedString typeDescription;
        final DateTime created;
        final LocalDate date;
        final BigDecimal amount;
        final BigDecimal amountUsedInDebt;
        final BigDecimal amountUsedInInterest;
        final BigDecimal amountUsedInFine;
        final BigDecimal amountUsedInAdvance;

        public AccountingEntrySummary(String id, String description, LocalizedString typeDescription, DateTime created, LocalDate date, BigDecimal amount, BigDecimal amountUsedInDebt, BigDecimal amountUsedInInterest, BigDecimal amountUsedInFine, final BigDecimal amountUsedInAdvance) {
            this.id = id;
            this.description = description;
            this.typeDescription = typeDescription;
            this.created = created;
            this.date = date;
            this.amount = amount;
            this.amountUsedInDebt = amountUsedInDebt;
            this.amountUsedInInterest = amountUsedInInterest;
            this.amountUsedInFine = amountUsedInFine;
            this.amountUsedInAdvance = amountUsedInAdvance;
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

        public BigDecimal getAmountUsedInAdvance() {
            return amountUsedInAdvance;
        }

        @Override public String toString() {
            return "AccountingEntrySummary{" + "id='" + id + '\'' + ", description='" + description + '\'' + ", created=" + created
                    + ", date=" + date + ", amount=" + amount + ", amountUsedInDebt=" + amountUsedInDebt
                    + ", amountUsedInInterest=" + amountUsedInInterest + ", amountUsedInFine=" + amountUsedInFine + '}';
        }
    }

    public static class AccountingEntryWithDebt extends AccountingEntrySummary {
        private final DebtEntry debtEntry;

        public AccountingEntryWithDebt(String id, String description, LocalizedString typeDescription, DateTime created,
                LocalDate date, BigDecimal amount, BigDecimal amountUsedInDebt, BigDecimal amountUsedInInterest, BigDecimal
                amountUsedInFine, BigDecimal amountUsedInAdvance, DebtEntry debtEntry) {
            super(id, description, typeDescription, created, date , amount, amountUsedInDebt, amountUsedInInterest, amountUsedInFine,
                    amountUsedInAdvance);
            this.debtEntry = debtEntry;
        }

        public DebtEntry getDebtEntry() {
            return debtEntry;
        }
    }

    public List<AccountingEntrySummary> createAccountingEntrySummaries(DebtEntry debtEntry) {
        if (debtEntry instanceof ExcessRefund) {
            return createAccountingEntrySummaries((ExcessRefund) debtEntry);
        }
        if (debtEntry instanceof Debt) {
            return createAccountingEntrySummaries((Debt) debtEntry);
        }
        throw new UnsupportedOperationException("Can't create payment summaries for " + (debtEntry == null ? "null" : debtEntry
                .getClass().getName()));
    }

    public List<AccountingEntrySummary> createAccountingEntrySummaries(ExcessRefund excessRefund) {
        return createAccountingEntrySummaries(null, (entry) -> {
            final Multimap<CreditEntry, PartialPayment> creditEntryPartialPaymentMultimap =
                    HashMultimap.create();

                    excessRefund.getPartialPayments().stream()
                    .distinct()
                    .forEach(pp -> creditEntryPartialPaymentMultimap.put(pp.getCreditEntry(), pp));
                    
            return creditEntryPartialPaymentMultimap;
        });
    }

    public List<AccountingEntrySummary> createAccountingEntrySummaries(Debt debt) {
        return createAccountingEntrySummaries(debt, (entry) -> {
            final Multimap<CreditEntry, PartialPayment> creditEntryPartialPaymentMultimap =
                    HashMultimap.create();
            Stream.concat(debt.getPartialPayments().stream(), Stream.concat(debt.getInterests().stream().flatMap(i -> i.getPartialPayments().stream()), debt.getFines().stream().flatMap(i -> i.getPartialPayments().stream())))
                    .distinct()
                    .forEach(pp -> creditEntryPartialPaymentMultimap.put(pp.getCreditEntry(), pp));
            return creditEntryPartialPaymentMultimap;
        });
    }

    private List<AccountingEntrySummary> createAccountingEntrySummaries(DebtEntry debtEntry, Function<DebtEntry, Multimap<CreditEntry,
                PartialPayment>>
            creditEntryPartialPaymentMultimapProvider) {
        final List<AccountingEntrySummary> paymentSummaries = new ArrayList<>();
        final Multimap<CreditEntry, PartialPayment> creditEntryPartialPaymentMultimap =
                creditEntryPartialPaymentMultimapProvider.apply(debtEntry);
        
        for (CreditEntry creditEntry : creditEntryPartialPaymentMultimap.keySet()) {
            final Collection<PartialPayment> partialPayments = creditEntryPartialPaymentMultimap.get(creditEntry);
            BigDecimal amountUsedInDebts = BigDecimal.ZERO;
            BigDecimal amountUsedInInterest = BigDecimal.ZERO;
            BigDecimal amountUsedInFine = BigDecimal.ZERO;
            BigDecimal amountUsedInAdvance = BigDecimal.ZERO;

            for (final PartialPayment partialPayment : partialPayments) {
                final DebtEntry targetDebtEntry = partialPayment.getDebtEntry();
                final BigDecimal amount = partialPayment.getAmount();
                if (targetDebtEntry == debtEntry) {
                    amountUsedInDebts = amountUsedInDebts.add(amount);
                } else if (targetDebtEntry instanceof Fine) {
                    amountUsedInFine = amountUsedInFine.add(amount);
                } else if (targetDebtEntry instanceof Interest) {
                    amountUsedInInterest = amountUsedInInterest.add(amount);
                } else if (targetDebtEntry instanceof ExcessRefund) {
                    amountUsedInAdvance = amountUsedInAdvance.add(amount);
                }
            }
            
            AccountingEntrySummary accountingEntrySummary =
                    createAccountingEntrySummary(creditEntry, amountUsedInDebts, amountUsedInInterest, amountUsedInFine,
                            amountUsedInAdvance);

            paymentSummaries.add(accountingEntrySummary);
        }

        return paymentSummaries;
    }

    public Set<AccountingEntryWithDebt> createAccountingEntrySummaries(CreditEntry creditEntry) {
        final Set<AccountingEntryWithDebt> accountingEntryWithDebtSet = new TreeSet<>(
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
            BigDecimal amountUsedInAdvance = BigDecimal.ZERO;

            Collection<PartialPayment> partialPayments = debtEntryPartialPaymentMultimap.get(debtEntry);
            for (PartialPayment partialPayment : partialPayments) {
                if (partialPayment.getDebtEntry() instanceof Interest) {
                    amountUsedInInterest = amountUsedInInterest.add(partialPayment.getAmount());
                } else if (partialPayment.getDebtEntry() instanceof Fine) {
                    amountUsedInFine = amountUsedInFine.add(partialPayment.getAmount());
                } else if (partialPayment.getDebtEntry() instanceof ExcessRefund) {
                    amountUsedInAdvance = amountUsedInAdvance.add(partialPayment.getAmount());
                }
                else {
                    amountUsedInDebt = amountUsedInDebt.add(partialPayment.getAmount());
                }
            }
            accountingEntryWithDebtSet
                    .add(new AccountingEntryWithDebt(debtEntry.getId(), creditEntry.getDescription(), creditEntry.getTypeDescription(), creditEntry
                    .getCreated(), creditEntry.getDate(), amountUsedInDebt.add(amountUsedInInterest.add(amountUsedInFine)),
                    amountUsedInDebt, amountUsedInInterest, amountUsedInFine, amountUsedInAdvance, debtEntry));
        }

        return accountingEntryWithDebtSet;
    }

    public List<AccountingEntrySummary> getAccountingEntries(DebtInterestCalculator calculator) {
        Stream<AccountingEntrySummary> creditEntriesStream = calculator.getCreditEntries().stream().map(entry -> createAccountingEntrySummary(
                entry, entry.getUsedAmountInDebts(), entry.getUsedAmountInInterests(), entry.getUsedAmountInFines(), entry
                        .getAmountInAdvance()));

        Stream<AccountingEntrySummary> excessRefundStream = calculator.getExcessRefundStream().map(
                excessRefund -> new AccountingEntrySummary(excessRefund.getId(), excessRefund.getDescription(), excessRefund.getTypeDescription(), excessRefund
                .getCreated(), excessRefund.getDate(), excessRefund.getAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, excessRefund.getAmount().negate()));

        return Stream.concat(creditEntriesStream, excessRefundStream).sorted(Comparator.comparing(AccountingEntrySummary::getCreated).thenComparing
                (AccountingEntrySummary::getDate)
                .thenComparing(AccountingEntrySummary::getId).reversed()).collect(Collectors.toList());
    }

    private AccountingEntrySummary createAccountingEntrySummary(final CreditEntry entry, final BigDecimal usedAmountInDebts, final BigDecimal usedAmountInInterests, final BigDecimal usedAmountInFines, final BigDecimal usedAmountInAdvance) {
        final BigDecimal amount = entry instanceof org.fenixedu.academic.domain.accounting.calculator.Refund ? entry.getAmount().negate() : entry.getAmount();

        return new AccountingEntrySummary(entry.getId(), entry.getDescription(), entry.getTypeDescription(), entry.getCreated(),
                entry.getDate(),amount, usedAmountInDebts,usedAmountInInterests,usedAmountInFines,usedAmountInAdvance);
    }

    @Atomic
    public AccountingTransaction depositAmount(final Event event, final User depositor, final DepositAmountBean depositAmountBean) {
        return event.depositAmount(depositor, depositAmountBean.getAmount(), depositAmountBean.getEntryType(),
                new AccountingTransactionDetailDTO(depositAmountBean.getWhenRegistered(), depositAmountBean.getPaymentMethod(),
                        depositAmountBean.getPaymentReference(), depositAmountBean.getReason()));
    }

    @Atomic
    public AccountingTransaction depositAdvancement(final Event eventToDeposit, final Event eventToRefund, final User user) {

        // In case of ResidenceEvents, source and target must be of the same type
        if (eventToRefund instanceof ResidenceEvent ^ eventToDeposit instanceof ResidenceEvent) {
            throw new DomainException("error.deposit.advancement.residenceEvent.source.not.equal.target");
        }

        final Money availableAmountToDeposit = eventToDeposit.getTotalAmountToPay();
        final Money availableAmountToRefund =
                new Money(eventToRefund.getDebtInterestCalculator(new DateTime()).getPaidUnusedAmount());
        final Money amountToRefund =
                Stream.of(availableAmountToDeposit, availableAmountToRefund).min(Money::compareTo).orElseThrow(UnsupportedOperationException::new);

        final Refund refund = refundExcessPayment(eventToRefund, user, amountToRefund);

        final AccountingTransaction tx = eventToDeposit.depositAmount(user, refund.getAmount(), eventToDeposit.getEntryType(),
                new AccountingTransactionDetailDTO(new DateTime(), PaymentMethod.getRefundPaymentMethod(), refund.getExternalId(), ""));
        refund.setAccountingTransaction(tx);
        return tx;
    }

    @Atomic
    public void cancelEvent(final Event event, final Person responsible, final String justification) {
        event.cancel(responsible, justification);
    }

    @Atomic
    public void markAsLapsed(final Event event, final Person responsible, final String justification) {
        event.markAsLapsed(responsible, justification);
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

    @Deprecated
    @Atomic
    public Refund refundEvent(final Event event, final User creator, EventExemptionJustificationType
            justificationType, String reason) {
        return event.refund(creator, justificationType, reason);
    }

    @Atomic
    public Refund refundEvent(final Event event, final User creator, EventExemptionJustificationType
            justificationType, String reason, BigDecimal value) {
        return event.refund(creator, justificationType, reason, value);
    }

    @Atomic
    public Refund refundExcessPayment(final Event event, final User creator, final Money amount) {
        return event.refundExcess(creator, amount);
    }

}
