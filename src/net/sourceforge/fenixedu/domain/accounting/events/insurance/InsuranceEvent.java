package net.sourceforge.fenixedu.domain.accounting.events.insurance;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class InsuranceEvent extends InsuranceEvent_Base {

    static {
	PersonAccountingEvent.addListener(new RelationAdapter<Event, Person>() {
	    @Override
	    public void beforeAdd(Event event, Person person) {
		if (event instanceof InsuranceEvent) {
		    if (person.hasAdministrativeOfficeFeeInsuranceEvent(((InsuranceEvent) event)
			    .getExecutionYear())) {
			throw new DomainException(
				"error.accounting.events.insurance.InsuranceEvent.person.already.has.insurance.event.for.execution.year");

		    }
		}
	    }
	});

    }

    private InsuranceEvent() {
	super();
    }

    public InsuranceEvent(Person person, ExecutionYear executionYear) {
	this();
	init(EventType.INSURANCE, person, executionYear);
    }

    protected void init(final EventType eventType, final Person person, final ExecutionYear executionYear) {
	checkRulesToCreate(person, executionYear);
	super.init(eventType, person, executionYear);
    }

    private void checkRulesToCreate(final Person person, final ExecutionYear executionYear) {
	if (person.hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
	    throw new DomainException(
		    "error.accounting.events.insurance.InsuranceEvent.person.already.has.insurance.event.for.execution.year");
	}
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(
		getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getInstitutionUnit().getUnitServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(
		getEventType(), whenRegistered);
    }

    @Override
    public Account getToAccount() {
	return getInstitutionUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getInstitutionUnit() {
	return RootDomainObject.getInstance().getInstitutionUnit();
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);
	getNonProcessedPaymentCodes().get(0).update(new YearMonthDay(), calculatePaymentCodeEndDate(),
		entryDTO.getAmountToPay(), entryDTO.getAmountToPay());

	return getNonProcessedPaymentCodes();

    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);

	return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.INSURANCE,
		new YearMonthDay(), calculatePaymentCodeEndDate(), this, entryDTO.getAmountToPay(),
		entryDTO.getAmountToPay(), getPerson().getStudent()));
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
	return new YearMonthDay().plusMonths(1);
    }

}
