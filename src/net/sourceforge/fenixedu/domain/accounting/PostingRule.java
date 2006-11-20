package net.sourceforge.fenixedu.domain.accounting;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.detail.SibsTransactionDetail;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class PostingRule extends PostingRule_Base {

    public static Comparator<PostingRule> COMPARATOR_BY_EVENT_TYPE = new Comparator<PostingRule>() {
	public int compare(PostingRule leftPostingRule, PostingRule rightPostingRule) {
	    int comparationResult = leftPostingRule.getEventType().compareTo(
		    rightPostingRule.getEventType());
	    return (comparationResult == 0) ? leftPostingRule.getIdInternal().compareTo(
		    rightPostingRule.getIdInternal()) : comparationResult;
	}
    };

    static {
	ServiceAgreementTemplatePostingRule
		.addListener(new RelationAdapter<PostingRule, ServiceAgreementTemplate>() {
		    @Override
		    public void beforeAdd(PostingRule postingRule,
			    ServiceAgreementTemplate serviceAgreementTemplate) {
			checkIfPostingRuleOverlapsExisting(serviceAgreementTemplate, postingRule);
		    }

		    private void checkIfPostingRuleOverlapsExisting(
			    ServiceAgreementTemplate serviceAgreementTemplate, PostingRule postingRule) {
			for (final PostingRule existingPostingRule : serviceAgreementTemplate
				.getPostingRules()) {
			    if (postingRule.overlaps(existingPostingRule)) {
				throw new DomainException(
					"error.accounting.agreement.ServiceAgreementTemplate.postingRule.overlaps.existing.one");
			    }
			}
		    }

		});
    }

    protected PostingRule() {
	super();
	super.setOjbConcreteClass(getClass().getName());
	super.setRootDomainObject(RootDomainObject.getInstance());
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

    private void checkParameters(EventType eventType, DateTime startDate,
	    ServiceAgreementTemplate serviceAgreementTemplate) {
	if (eventType == null) {
	    throw new DomainException("error.accounting.agreement.postingRule.eventType.cannot.be.null");
	}
	if (startDate == null) {
	    throw new DomainException("error.accounting.agreement.postingRule.startDate.cannot.be.null");
	}
	if (serviceAgreementTemplate == null) {
	    throw new DomainException(
		    "error.accounting.agreement.postingRule.serviceAgreementTemplate.cannot.be.null");
	}
    }

    public final Set<Entry> process(User user, List<EntryDTO> entryDTOs, Event event,
	    Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	for (final EntryDTO entryDTO : entryDTOs) {
	    if (entryDTO.getAmountToPay().lessOrEqualThan(Money.ZERO)) {
		throw new DomainException(
			"error.accounting.PostingRule.amount.to.pay.must.be.greater.than.zero");
	    }
	}

	final Set<AccountingTransaction> resultingTransactions = internalProcess(user, entryDTOs, event,
		fromAccount, toAccount, transactionDetail);

	return getResultingEntries(resultingTransactions);
    }

    private Set<Entry> getResultingEntries(Set<AccountingTransaction> resultingTransactions) {
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

    public boolean hasEndDate() {
	return getEndDate() != null;
    }

    public boolean overlaps(PostingRule postingRule) {
	return (getEventType() == postingRule.getEventType() && (isActiveForDate(postingRule
		.getStartDate()) || isActiveForDate(postingRule.getEndDate())));

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
	throw new DomainException("error.accounting.PostingRule.cannot.modify.startDate");
    }

    @Override
    public void setEndDate(DateTime endDate) {
	if (hasEndDate()) {
	    throw new DomainException("error.accounting.PostingRule.endDate.is.already.set");
	}

	super.setEndDate(endDate);
    }

    public void deactivate() {
	setEndDate(new DateTime().minus(10000));
    }

    public final void delete() {
	throw new DomainException("error.accounting.PostingRule.cannot.be.deleted");
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	throw new DomainException(
		"error.accounting.agreement.postingRule.cannot.modify.serviceAgreementTemplate");
    }

    protected Entry makeEntry(EntryType entryType, Money amount, Account account) {
	return new Entry(entryType, amount, account);
    }

    protected AccountingTransaction makeAccountingTransaction(User responsibleUser, Event event,
	    Account from, Account to, EntryType entryType, Money amount,
	    AccountingTransactionDetailDTO transactionDetail) {
	return new AccountingTransaction(responsibleUser, event, makeEntry(entryType, amount.negate(),
		from), makeEntry(entryType, amount, to),
		makeAccountingTransactionDetail(transactionDetail));
    }

    protected AccountingTransactionDetail makeAccountingTransactionDetail(
	    AccountingTransactionDetailDTO transactionDetailDTO) {
	if (transactionDetailDTO instanceof SibsTransactionDetailDTO) {
	    final SibsTransactionDetailDTO sibsTransactionDetailDTO = (SibsTransactionDetailDTO) transactionDetailDTO;
	    return new SibsTransactionDetail(sibsTransactionDetailDTO.getWhenRegistered(),
		    sibsTransactionDetailDTO.getSibsTransactionId(), sibsTransactionDetailDTO
			    .getSibsCode());
	} else {
	    return new AccountingTransactionDetail(transactionDetailDTO.getWhenRegistered(),
		    transactionDetailDTO.getPaymentMode());
	}
    }

    public boolean isVisible() {
	return true;
    }

    public abstract Money calculateTotalAmountToPay(Event event, DateTime when);

    public abstract List<EntryDTO> calculateEntries(Event event, DateTime when);

    protected abstract Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    Event event, Account fromAccount, Account toAccount,
	    AccountingTransactionDetailDTO transactionDetail);

}
