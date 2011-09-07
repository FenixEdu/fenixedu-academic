package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class InstitutionAffiliationEvent extends InstitutionAffiliationEvent_Base {

    protected InstitutionAffiliationEvent() {
	super();
    }

    public InstitutionAffiliationEvent(Person person) {
	super();
	init(person);
    }

    protected void init(Person person) {
	super.init(EventType.INSTITUTION_AFFILIATION, person);
	setInstitution(RootDomainObject.getInstance().getInstitutionUnit());
	setInstitutionWhereOpen(getInstitution());
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getInstitution().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	return labelFormatter;
    }

    @Override
    public PostingRule getPostingRule() {
	return getInstitution().getUnitServiceAgreementTemplate().findPostingRuleBy(getEventType(), getWhenOccured(), null);
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.INSTITUTION_ACCOUNT_CREDIT,
		new YearMonthDay(), new YearMonthDay().plusMonths(6), this, getOriginalAmountToPay(), null, getPerson()));
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	return getNonProcessedPaymentCodes();
    }

    @Override
    protected boolean canCloseEvent(DateTime whenRegistered) {
	// these events are closed when the affiliation status changes.
	return false;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
	    SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser,
		Collections.singletonList(new EntryDTO(EntryType.INSTITUTION_ACCOUNT_CREDIT, this, amountToPay)),
		transactionDetail);
    }

}
