/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.detail.SibsTransactionDetail;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class PostingRule extends PostingRule_Base {

    public static Comparator<PostingRule> COMPARATOR_BY_EVENT_TYPE = new Comparator<PostingRule>() {
        @Override
        public int compare(PostingRule leftPostingRule, PostingRule rightPostingRule) {
            int comparationResult = leftPostingRule.getEventType().compareTo(rightPostingRule.getEventType());
            return (comparationResult == 0) ? leftPostingRule.getExternalId().compareTo(rightPostingRule.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<PostingRule> COMPARATOR_BY_START_DATE = new Comparator<PostingRule>() {
        @Override
        public int compare(PostingRule leftPostingRule, PostingRule rightPostingRule) {
            int comparationResult = leftPostingRule.getStartDate().compareTo(rightPostingRule.getStartDate());
            return (comparationResult == 0) ? leftPostingRule.getExternalId().compareTo(rightPostingRule.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<PostingRule> COMPARATOR_BY_END_DATE = new Comparator<PostingRule>() {
        @Override
        public int compare(PostingRule left, PostingRule right) {
            int comparationResult;
            if (!left.hasEndDate() && !right.hasEndDate()) {
                comparationResult = 0;
            } else if (!left.hasEndDate()) {
                comparationResult = 1;
            } else if (!right.hasEndDate()) {
                return comparationResult = -1;
            } else {
                comparationResult = left.getEndDate().compareTo(right.getEndDate());
            }

            return comparationResult == 0 ? left.getExternalId().compareTo(right.getExternalId()) : comparationResult;
        }
    };

    static {
        getRelationServiceAgreementTemplatePostingRule().addListener(
                new RelationAdapter<ServiceAgreementTemplate, PostingRule>() {
                    @Override
                    public void beforeAdd(ServiceAgreementTemplate serviceAgreementTemplate, PostingRule postingRule) {
                        checkIfPostingRuleOverlapsExisting(serviceAgreementTemplate, postingRule);
                    }

                    private void checkIfPostingRuleOverlapsExisting(ServiceAgreementTemplate serviceAgreementTemplate,
                            PostingRule postingRule) {
                        if (serviceAgreementTemplate != null) {
                            for (final PostingRule existingPostingRule : serviceAgreementTemplate.getPostingRules()) {
                                if (postingRule.overlaps(existingPostingRule)) {
                                    throw new DomainException(
                                            "error.accounting.agreement.ServiceAgreementTemplate.postingRule.overlaps.existing.one");
                                }
                            }
                        }
                    }

                });
    }

    protected PostingRule() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setCreationDate(new DateTime());
    }

    protected void init(EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {

        checkParameters(eventType, startDate, serviceAgreementTemplate);

        super.setEventType(eventType);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
        super.setServiceAgreementTemplate(serviceAgreementTemplate);

    }

    private void checkParameters(EventType eventType, DateTime startDate, ServiceAgreementTemplate serviceAgreementTemplate) {
        if (eventType == null) {
            throw new DomainException("error.accounting.agreement.postingRule.eventType.cannot.be.null");
        }
        if (startDate == null) {
            throw new DomainException("error.accounting.agreement.postingRule.startDate.cannot.be.null");
        }
        if (serviceAgreementTemplate == null) {
            throw new DomainException("error.accounting.agreement.postingRule.serviceAgreementTemplate.cannot.be.null");
        }

    }

    public Set<Entry> process(User user, Collection<EntryDTO> entryDTOs, Event event, Account fromAccount, Account toAccount,
            AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.isEmpty()) {
            throw new DomainException("error.accounting.PostingRule.entries.to.process.cannot.be.empty");
        }

        for (final EntryDTO entryDTO : entryDTOs) {
            if (entryDTO.getAmountToPay().lessOrEqualThan(Money.ZERO)) {
                throw new DomainException("error.accounting.PostingRule.amount.to.pay.must.be.greater.than.zero");
            }
        }

        final Set<AccountingTransaction> resultingTransactions =
                internalProcess(user, entryDTOs, event, fromAccount, toAccount, transactionDetail);

        return getResultingEntries(resultingTransactions);
    }

    protected Set<Entry> getResultingEntries(Set<AccountingTransaction> resultingTransactions) {
        final Set<Entry> result = new HashSet<Entry>();

        for (final AccountingTransaction transaction : resultingTransactions) {
            result.add(transaction.getToAccountEntry());
        }

        return result;
    }

    public final List<EntryDTO> calculateEntries(Event event) {
        return calculateEntries(event, new DateTime());
    }

    public boolean isActiveForDate(DateTime when) {
        if (getStartDate().isAfter(when)) {
            return false;
        } else {
            return (hasEndDate()) ? !when.isAfter(getEndDate()) : true;
        }
    }

    public boolean isActive() {
        return isActiveForDate(new DateTime());
    }

    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    public boolean overlaps(PostingRule postingRule) {
        return overlaps(postingRule.getEventType(), postingRule.getStartDate(), postingRule.getEndDate());

    }

    public boolean overlaps(final EventType eventType, final DateTime startDate, final DateTime endDate) {
        if (getEventType() == eventType) {

            if (hasEndDate() && endDate != null) {
                return isActiveForDate(startDate) || isActiveForDate(endDate);

            } else if (hasEndDate()) {
                return !startDate.isAfter(getEndDate());

            } else if (endDate != null) {
                return !getStartDate().isAfter(endDate);
            }

            return true;

        }

        return false;
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
        throw new DomainException("error.accounting.agreement.postingRule.cannot.modify.creationDate");
    }

    @Override
    public void setEventType(EventType eventType) {
        throw new DomainException("error.accounting.agreement.postingRule.cannot.modify.eventType");
    }

    @Override
    public void setStartDate(DateTime startDate) {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
        super.setStartDate(startDate);
    }

    @Override
    public void setEndDate(DateTime endDate) {
        if (hasEndDate()) {
            throw new DomainException("error.accounting.PostingRule.endDate.is.already.set");
        }

        super.setEndDate(endDate);
    }

    public void deactivate() {
        deactivate(new DateTime());
    }

    public void deactivate(final DateTime when) {
        super.setEndDate(when.minus(10000));
    }

    public void delete() {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
        super.setServiceAgreementTemplate(null);

        setRootDomainObject(null);
        removeOtherRelations();
        deleteDomainObject();
    }

    protected void removeOtherRelations() {

    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
        throw new DomainException("error.accounting.agreement.postingRule.cannot.modify.serviceAgreementTemplate");
    }

    protected Entry makeEntry(EntryType entryType, Money amount, Account account) {
        return new Entry(entryType, amount, account);
    }

    protected AccountingTransaction makeAccountingTransaction(User responsibleUser, Event event, Account from, Account to,
            EntryType entryType, Money amount, AccountingTransactionDetailDTO transactionDetail) {
        return new AccountingTransaction(responsibleUser, event, makeEntry(entryType, amount.negate(), from), makeEntry(
                entryType, amount, to), makeAccountingTransactionDetail(transactionDetail));
    }

    protected AccountingTransactionDetail makeAccountingTransactionDetail(AccountingTransactionDetailDTO transactionDetailDTO) {
        if (transactionDetailDTO instanceof SibsTransactionDetailDTO) {
            final SibsTransactionDetailDTO sibsTransactionDetailDTO = (SibsTransactionDetailDTO) transactionDetailDTO;
            return new SibsTransactionDetail(sibsTransactionDetailDTO.getWhenRegistered(),
                    sibsTransactionDetailDTO.getSibsTransactionId(), sibsTransactionDetailDTO.getSibsCode(),
                    sibsTransactionDetailDTO.getComments());
        } else {
            return new AccountingTransactionDetail(transactionDetailDTO.getWhenRegistered(),
                    transactionDetailDTO.getPaymentMode(), transactionDetailDTO.getComments());
        }
    }

    public boolean isVisible() {
        return true;
    }

    public final Money calculateTotalAmountToPay(Event event, DateTime when) {
        return calculateTotalAmountToPay(event, when, true);
    }

    public void addOtherPartyAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount, Money amount,
            AccountingTransactionDetailDTO transactionDetailDTO) {

        if (!event.isOtherPartiesPaymentsSupported()) {
            throw new DomainException("error.accounting.PostingRule.event.does.not.support.other.party.payments");
        }

        checkRulesToAddOtherPartyAmount(event, amount);
        internalAddOtherPartyAmount(responsibleUser, event, fromAcount, toAccount, amount, transactionDetailDTO);

    }

    protected void checkRulesToAddOtherPartyAmount(Event event, Money amount) {
        if (amount.greaterThan(calculateTotalAmountToPay(event, event.getWhenOccured(), false))) {
            throw new DomainException(
                    "error.accounting.PostingRule.cannot.add.other.party.amount.that.exceeds.event.amount.to.pay");
        }

    }

    public void internalAddOtherPartyAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {
        throw new DomainException("error.accounting.PostingRule.does.not.implement.internal.other.party.amount");
    }

    public boolean isActiveForPeriod(DateTime startDate, DateTime endDate) {
        if (getStartDate().isAfter(endDate)) {
            return false;
        }

        if (getEndDate() != null && getEndDate().isBefore(startDate)) {
            return false;
        }

        return true;

    }

    public AccountingTransaction depositAmount(final User responsibleUser, final Event event, final Account fromAcount,
            final Account toAccount, final Money amount, final AccountingTransactionDetailDTO transactionDetailDTO) {
        throw new DomainException("error.accounting.PostingRule.does.not.implement.deposit.amount");
    }

    public AccountingTransaction depositAmount(final User responsibleUser, final Event event, final Account fromAcount,
            final Account toAccount, final Money amount, final EntryType entryType,
            final AccountingTransactionDetailDTO transactionDetailDTO) {

        checkEntryTypeForDeposit(event, entryType);

        return makeAccountingTransaction(responsibleUser, event, fromAcount, toAccount, entryType, amount, transactionDetailDTO);
    }

    protected void checkEntryTypeForDeposit(final Event event, final EntryType entryType) {
        if (!event.getPossibleEntryTypesForDeposit().contains(entryType)) {
            throw new DomainException("error.accounting.PostingRule.entry.type.not.supported.for.deposit");
        }
    }

    public boolean isMostRecent() {
        return Collections.max(getServiceAgreementTemplate().getAllPostingRulesFor(getEventType()),
                PostingRule.COMPARATOR_BY_END_DATE).equals(this);

    }

    public String getFormulaDescription() {
        return BundleUtil.getString(Bundle.APPLICATION, this.getClass().getSimpleName() + ".formulaDescription");
    }

    protected boolean has(final EventType eventType) {
        return getEventType().equals(eventType);
    }

    public final Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        Money amountToPay = doCalculationForAmountToPay(event, when, applyDiscount);

        if (!event.isExemptionAppliable()) {
            return amountToPay;
        }

        return subtractFromExemptions(event, when, applyDiscount, amountToPay);
    }

    protected abstract Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount);

    protected abstract Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay);

    public PaymentCodeType calculatePaymentCodeTypeFromEvent(Event event, DateTime when, boolean applyDiscount) {
        throw new DomainException("error.accounting.PostingRule.cannot.calculate.payment.code.type");
    }

    abstract public List<EntryDTO> calculateEntries(Event event, DateTime when);

    abstract protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail);

    static public Collection<PostingRule> findPostingRules(final EventType eventType) {
        final Collection<PostingRule> result = new HashSet<PostingRule>();
        for (final PostingRule postingRule : Bennu.getInstance().getPostingRulesSet()) {
            if (postingRule.has(eventType)) {
                result.add(postingRule);
            }
        }
        return result;
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStartDate() {
        return getStartDate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasEventType() {
        return getEventType() != null;
    }

}
